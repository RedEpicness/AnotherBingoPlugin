package me.redepicness.bingo.world

import me.redepicness.bingo.util.registerEvents
import me.redepicness.bingo.util.runTask
import me.redepicness.bingo.util.runTaskLater
import me.redepicness.bingo.util.runTaskTimerAsynchronously
import net.kyori.adventure.util.TriState
import org.bukkit.NamespacedKey
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.WorldType
import org.bukkit.scheduler.BukkitTask
import org.intellij.lang.annotations.Subst
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap

object WorldManager {

    //TODO Make these configurable
    const val CHUNK_RADIUS_LOADING_STEP = 5
    const val CHUNK_LOADS_PER_BATCH = 50
    const val MIN_WORLDS_TO_KEEP_READY = 1

    private val logger = LoggerFactory.getLogger("world-manager")
    private val gameWorlds: MutableMap<NamespacedKey, BingoWorlds> = ConcurrentHashMap<NamespacedKey, BingoWorlds>()

    private lateinit var task: BukkitTask
    val allWorlds: Set<BingoWorlds>
        get() = gameWorlds.values.toSet()
    fun setup() {
        registerEvents(WorldListener())
        task = runTaskTimerAsynchronously(0, 10 * 20) { processWorlds() }
    }

    fun shutdown() {
        task.cancel()
        val futures: MutableSet<CompletableFuture<Void>> = mutableSetOf()
        for (worlds in gameWorlds.values.toSet()) {
            futures.add(removeWorlds(worlds))
        }
        CompletableFuture.allOf(*futures.toTypedArray()).join()
    }

    fun getWorldsForGame(): CompletableFuture<BingoWorlds> {
        return allWorlds.find { !it.isUsed }.let { if(it != null) CompletableFuture.completedFuture(it) else createNewWorld(BingoWorldsConfig()) }
    }

    fun getWorlds(world: World) = getWorlds(world.key)

    fun getWorlds(key: NamespacedKey) = gameWorlds[key]

    fun removeWorlds(worlds: BingoWorlds): CompletableFuture<Void> {
        gameWorlds.remove(worlds.overworld.key)
        gameWorlds.remove(worlds.nether.key)
        gameWorlds.remove(worlds.end.key)
        return worlds.destroyWorlds()
    }

    fun createNewWorld(config: BingoWorldsConfig): CompletableFuture<BingoWorlds> {
        val overworldKey: NamespacedKey = generateWorldKey()
        val netherKey = NamespacedKey(overworldKey.namespace, overworldKey.key + "-nether")
        val endKey = NamespacedKey(overworldKey.namespace, overworldKey.key + "-end")
        val future: CompletableFuture<BingoWorlds> = CompletableFuture<BingoWorlds>()
        logger.info("Creating new worlds for key {}...", overworldKey)

        val overworldCreator: WorldCreator = WorldCreator(overworldKey)
                .environment(World.Environment.NORMAL)
                .keepSpawnLoaded(TriState.FALSE)
                .type(WorldType.NORMAL)

        val netherCreator: WorldCreator = WorldCreator(netherKey)
                .environment(World.Environment.NETHER)
                .keepSpawnLoaded(TriState.FALSE)
                .type(WorldType.NORMAL)

        val endCreator: WorldCreator = WorldCreator(endKey)
                .environment(World.Environment.THE_END)
                .keepSpawnLoaded(TriState.FALSE)
                .type(WorldType.NORMAL)

        runTaskLater(20) {
            logger.info("Creating overworld with key {}...", overworldKey)
            val overworld = overworldCreator.createWorld()
            runTaskLater(20) {
                logger.info("Creating nether with key {}...", netherKey)
                val nether = netherCreator.createWorld()
                runTaskLater(20) {
                    logger.info("Creating end with key {}...", endKey)
                    val end = endCreator.createWorld()
                    if (overworld == null) {
                        logger.warn("Failed to create overworld {}!", overworldKey)
                        future.completeExceptionally(IllegalStateException())
                    }
                    else if (nether == null) {
                        logger.warn("Failed to create nether {}!", netherKey)
                        future.completeExceptionally(IllegalStateException())
                    }
                    else if (end == null) {
                        logger.warn("Failed to create end {}!", endKey)
                        future.completeExceptionally(IllegalStateException())
                    }
                    else {
                        val bingoWorlds = BingoWorlds(config, overworld, nether, end)
                        gameWorlds[overworldKey] = bingoWorlds
                        gameWorlds[netherKey] = bingoWorlds
                        gameWorlds[endKey] = bingoWorlds
                        logger.info("Created worlds for key {}!", overworldKey)
                        if (config.preloading.preload) {
                            bingoWorlds.startLoadingWorlds()
                        }
                        future.complete(bingoWorlds)
                    }
                }
            }
        }
        return future
    }

    private fun processWorlds() {
        var readyWorlds = 0
        for (worlds in gameWorlds.values.toMutableSet()) {
            if (!worlds.isUsed) {
                readyWorlds++
            }
        }
        for (i in readyWorlds until MIN_WORLDS_TO_KEEP_READY) {
            logger.info("Creating new world, as current number of ready worlds is too low! {} < {}", readyWorlds, MIN_WORLDS_TO_KEEP_READY)
            runTask { createNewWorld(BingoWorldsConfig()) } //TODO Use a default config
        }
    }

    private fun generateWorldKey(): NamespacedKey {
        var key: NamespacedKey
        do {
            @Subst("1a2b3c4d") val uuid: String = UUID.randomUUID().toString().substring(0, 8)
            key = NamespacedKey("abp", "bingo-$uuid")
        } while (gameWorlds.containsKey(key))
        return key
    }
}
