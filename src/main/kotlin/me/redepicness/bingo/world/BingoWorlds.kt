package me.redepicness.bingo.world

import cloud.commandframework.types.tuples.Pair
import me.redepicness.bingo.plugin
import me.redepicness.bingo.util.FileUtils
import me.redepicness.bingo.util.runTaskAsynchronously
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.Vector
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.roundToInt

class BingoWorlds internal constructor(private val config: BingoWorldsConfig, val overworld: World, val nether: World, val end: World) {

    private val logger = LoggerFactory.getLogger(overworld.key.key)
    private val loadingTasks: MutableMap<Key, BukkitTask> = ConcurrentHashMap()
    private val currentLoadingFutures: MutableMap<Key, CompletableFuture<Void>> = ConcurrentHashMap()
    private val loadedWorlds: MutableSet<Key> = mutableSetOf()
    var stopLoading = false
    var isUsed = false

    var spawns: Set<Location>? = null
        private set

    fun stopLoadingChunks(): CompletableFuture<Void> {
        stopLoading = true
        return CompletableFuture.allOf(*currentLoadingFutures.values.toTypedArray())
    }

    fun markAsUsed() {
        isUsed = true
    }

    val areAllWorldsLoaded: Boolean
        get() = isOverworldLoaded && isNetherLoaded && isEndLoaded

    val isOverworldLoaded: Boolean
        get() = loadedWorlds.contains(overworld.key)
    val isNetherLoaded: Boolean
        get() = loadedWorlds.contains(nether.key)
    val isEndLoaded: Boolean
        get() = loadedWorlds.contains(end.key)

    fun startLoadingWorlds() {
        loadingTasks[overworld.key] = runTaskAsynchronously { startLoadingWorld(overworld, config.preloading.overworldRadius) }
        loadingTasks[nether.key] = runTaskAsynchronously { startLoadingWorld(nether, config.preloading.netherRadius) }
        loadingTasks[end.key] = runTaskAsynchronously { startLoadingWorld(end, config.preloading.endRadius) }
    }

    fun findSpawns(count: Int): CompletableFuture<Set<Location>> {
        val angleSeparation = 2 * Math.PI / count
        val distance = 400 + count * 100
        val direction = Vector(distance, 0, 0)
        val futures = mutableSetOf<CompletableFuture<Location>>()
        spawns = emptySet() // mark that we are looking for spawns

        for (i in 0..count) {
            futures.add(findSpawnAt(direction.rotateAroundY(angleSeparation)))
        }

        val future = CompletableFuture<Set<Location>>()

        CompletableFuture.allOf(*futures.toTypedArray()).whenComplete { _, _ ->
            spawns = futures.map { it.get() }.toSet()
            future.complete(spawns)
        }

        return future
    }

    private fun findSpawnAt(loc: Vector, future: CompletableFuture<Location> = CompletableFuture<Location>()): CompletableFuture<Location> {
        overworld.getChunkAtAsync(loc.blockX, loc.blockZ, true).whenComplete { c, e ->
            if(c != null){
                val spawn = findSpawnIn(c)
                if(spawn != null) {
                    future.complete(spawn)
                }
                else {
                    findSpawnAt(loc.add(loc.clone().normalize().multiply(32)), future)
                }
            }
        }
        return future
    }

    private fun findSpawnIn(chunk: Chunk): Location? {
        var block: Block?
        for (x in 0..15 step 4){
            for (z in 0..15 step 4){
                block = overworld.getHighestBlockAt(chunk.x + x, chunk.z + z, HeightMap.MOTION_BLOCKING)
                if(validSpawn(block)){
                    Bukkit.broadcast(Component.text("Found a spawn...", NamedTextColor.DARK_GRAY))
                    return block.getRelative(BlockFace.UP).location.toCenterLocation()
                }
            }
        }
        return null
    }

    private fun validSpawn(block: Block) = block.isSolid

    fun destroyWorlds(): CompletableFuture<Void> {
        cancelLoadingTask(overworld.key)
        cancelLoadingTask(nether.key)
        cancelLoadingTask(end.key)
        return CompletableFuture.allOf(
            destroyWorld(overworld),
            destroyWorld(nether),
            destroyWorld(end)
        )
    }

    private fun startLoadingWorld(world: World, radius: Int) {
        val chunkFutureBatches: MutableList<MutableList<Pair<Int, Int>>> = LinkedList()
        chunkFutureBatches.add(LinkedList())
        val checkedChunks: MutableSet<Long> = HashSet()
        val chunkRadius = (radius / 16f).roundToInt()
        var currentChunkRadius: Int = WorldManager.CHUNK_RADIUS_LOADING_STEP
        val key = world.key
        logger.info("Queueing chunks for world {} with radius {} ({} chunk radius)...", key, radius, chunkRadius)

        //Todo put chunks into batches in a spiral, not this dumb hack
        do {
            val minChunkRadius = currentChunkRadius.coerceAtMost(chunkRadius)
            for (x in -minChunkRadius until minChunkRadius) {
                for (z in -minChunkRadius until minChunkRadius) {
                    if (!checkedChunks.add(Chunk.getChunkKey(x, z))) {
                        continue
                    }
                    var chunkBatch = chunkFutureBatches[chunkFutureBatches.size - 1]
                    if (chunkBatch.size >= WorldManager.CHUNK_LOADS_PER_BATCH) {
                        chunkBatch = LinkedList()
                        chunkFutureBatches.add(chunkBatch)
                    }
                    chunkBatch.add(Pair.of(x, z))
                }
            }
            currentChunkRadius += WorldManager.CHUNK_RADIUS_LOADING_STEP
        } while (currentChunkRadius < chunkRadius)
        processBatch(world, chunkFutureBatches.listIterator(), chunkFutureBatches.size)
    }

    private fun processBatch(world: World, batchIterator: ListIterator<MutableList<Pair<Int, Int>>>, batchCount: Int) {
        val key: Key = world.key
        if (stopLoading) {
            cancelLoadingTask(key)
            currentLoadingFutures.remove(key)
            logger.info("Stopped loading chunks for world {}!", key)
        } else if (batchIterator.hasNext()) {
            val completedBatches = batchIterator.nextIndex()
            val percentageBatches = (completedBatches * 100 / batchCount.toFloat()).roundToInt()
            logger.info(
                "Chunk progress for world {}: {}%, batch {}/{}, chunks {}/{}!",
                key,
                percentageBatches,
                completedBatches,
                batchCount,
                completedBatches * WorldManager.CHUNK_LOADS_PER_BATCH,
                batchCount * WorldManager.CHUNK_LOADS_PER_BATCH
            )
            val nextBatch =
                batchIterator.next().stream().map<CompletableFuture<Chunk>> { p: Pair<Int, Int> -> world.getChunkAtAsync(p.first, p.second) }.toList().toTypedArray()
            val future = CompletableFuture.allOf(*nextBatch)
            currentLoadingFutures[key] = future
            future.join()
            processBatch(world, batchIterator, batchCount)
        } else {
            cancelLoadingTask(key)
            currentLoadingFutures.remove(key)
            loadedWorlds.add(key)
            logger.info("Finished loading world {}!", key)
        }
    }

    private fun cancelLoadingTask(key: Key) {
        val task = loadingTasks.remove(key)
        task?.cancel()
    }

    private fun destroyWorld(world: World): CompletableFuture<Void> {
        world.isAutoSave = false
        val tpFutures: MutableSet<CompletableFuture<Void>> = mutableSetOf()
        val spawn = Bukkit.getWorlds()[0].spawnLocation
        for (p in world.players) {
            tpFutures.add(
                p.teleportAsync(spawn)
                    .thenAccept { success: Boolean ->
                        if (!success) {
                            p.kick(Component.text("Error teleporting to lobby!", NamedTextColor.RED))
                        }
                    }
            )
        }
        val future = CompletableFuture<Void>()
        CompletableFuture.allOf(*tpFutures.toTypedArray())
            .thenRun {
                Bukkit.unloadWorld(world, false)
                if (plugin.isEnabled) {
                    Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
                        FileUtils.deleteFolder(world.worldFolder.toPath())
                        future.complete(null)
                    })
                } else {
                    FileUtils.deleteFolder(world.worldFolder.toPath())
                    future.complete(null)
                }
            }
        return future
    }
}
