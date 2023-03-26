package me.redepicness.bingo.world;

import cloud.commandframework.types.tuples.Pair;
import me.redepicness.bingo.AnotherBingoPlugin;
import me.redepicness.bingo.util.FileUtils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class BingoWorlds {

    private final @NotNull AnotherBingoPlugin plugin = JavaPlugin.getPlugin(AnotherBingoPlugin.class);
    private final @NotNull Logger logger;

    private final @NotNull Map<Key, BukkitTask> loadingTasks = new ConcurrentHashMap<>();
    private final @NotNull Map<Key, CompletableFuture<Void>> currentLoadingFutures = new ConcurrentHashMap<>();
    private final @NotNull Set<Key> loadedWorlds = new HashSet<>();

    private final @NotNull BingoWorldsConfig config;

    final @NotNull World overworld;
    final @NotNull World nether;
    final @NotNull World end;

    boolean stopLoading = false;
    boolean used = false;

    BingoWorlds(@NotNull BingoWorldsConfig config, @NotNull World overworld, @NotNull World nether, @NotNull World end) {
        this.config = config;
        this.logger = LoggerFactory.getLogger(overworld.getKey().getKey());
        this.overworld = overworld;
        this.nether = nether;
        this.end = end;
    }

    public @NotNull CompletableFuture<Void> stopLoadingChunks(){
        stopLoading = true;
        return CompletableFuture.allOf(currentLoadingFutures.values().toArray(WorldManager.EMPTY_FUTURES_ARRAY));
    }

    public void markAsUsed() {
        used = true;
    }

    public @NotNull World getOverworld() {
        return overworld;
    }

    public @NotNull World getNether() {
        return nether;
    }

    public @NotNull World getEnd() {
        return end;
    }

    public boolean areAllWorldsLoaded() {
        return isOverworldLoaded() && isNetherLoaded() && isEndLoaded();
    }

    public boolean isOverworldLoaded() {
        return loadedWorlds.contains(overworld.getKey());
    }

    public boolean isNetherLoaded() {
        return loadedWorlds.contains(nether.getKey());
    }

    public boolean isEndLoaded() {
        return loadedWorlds.contains(end.getKey());
    }

    public boolean isUsed() {
        return used;
    }

    void startLoadingWorlds() {
        loadingTasks.put(
                overworld.getKey(),
                Bukkit.getScheduler().runTaskAsynchronously(
                        plugin,
                        () -> startLoadingWorld(overworld, config.preloading().overworldRadius())
                )
        );
        loadingTasks.put(
                nether.getKey(),
                Bukkit.getScheduler().runTaskAsynchronously(
                        plugin,
                        () -> startLoadingWorld(overworld, config.preloading().netherRadius())
                )
        );
        loadingTasks.put(
                end.getKey(),
                Bukkit.getScheduler().runTaskAsynchronously(
                        plugin,
                        () -> startLoadingWorld(overworld, config.preloading().endRadius())
                )
        );
    }

    CompletableFuture<Void> destroyWorlds() {
        cancelLoadingTask(overworld.getKey());
        cancelLoadingTask(nether.getKey());
        cancelLoadingTask(end.getKey());
        return CompletableFuture.allOf(
                destroyWorld(overworld),
                destroyWorld(nether),
                destroyWorld(end)
        );
    }

    private void startLoadingWorld(@NotNull World world, int radius) {

        List<List<Pair<Integer, Integer>>> chunkFutureBatches = new LinkedList<>();
        chunkFutureBatches.add(new LinkedList<>());

        Set<Long> checkedChunks = new HashSet<>();

        int chunkRadius = Math.round(radius / 16f);
        int currentChunkRadius = WorldManager.CHUNK_RADIUS_LOADING_STEP;

        NamespacedKey key = world.getKey();
        logger.info("Queueing chunks for world {} with radius {} ({} chunk radius)...", key, radius, chunkRadius);

        //Todo put chunks into batches in a spiral, not this dumb hack
        do {
            int minChunkRadius = Math.min(currentChunkRadius, chunkRadius);

            for (int x = -minChunkRadius; x < minChunkRadius; x++) {
                for (int z = -minChunkRadius; z < minChunkRadius; z++) {

                    if (!checkedChunks.add(Chunk.getChunkKey(x, z))) {
                        continue;
                    }

                    List<Pair<Integer, Integer>> chunkBatch = chunkFutureBatches.get(chunkFutureBatches.size()-1);

                    if(chunkBatch.size() >= WorldManager.CHUNK_LOADS_PER_BATCH){
                        chunkBatch = new LinkedList<>();
                        chunkFutureBatches.add(chunkBatch);
                    }

                    chunkBatch.add(Pair.of(x, z));

                }
            }

            currentChunkRadius += WorldManager.CHUNK_RADIUS_LOADING_STEP;
        }
        while (currentChunkRadius < chunkRadius);

        processBatch(world, chunkFutureBatches.listIterator(), chunkFutureBatches.size());
    }

    private void processBatch(@NotNull World world, @NotNull ListIterator<List<Pair<Integer, Integer>>> batchIterator, int batchCount){
        Key key = world.getKey();
        if(stopLoading){
            cancelLoadingTask(key);
            currentLoadingFutures.remove(key);
            logger.info("Stopped loading chunks for world {}!", key);
        }
        else if(batchIterator.hasNext()){

            int completedBatches = batchIterator.nextIndex();
            float percentageBatches = Math.round(completedBatches * 100 / (float) batchCount);
            logger.info("Chunk progress for world {}: {}%, batch {}/{}, chunks {}/{}!", key, percentageBatches, completedBatches, batchCount, completedBatches*WorldManager.CHUNK_LOADS_PER_BATCH, batchCount*WorldManager.CHUNK_LOADS_PER_BATCH);

            CompletableFuture<?>[] nextBatch = batchIterator.next().stream().map(p -> world.getChunkAtAsync(p.getFirst(), p.getSecond())).toList().toArray(WorldManager.EMPTY_FUTURES_ARRAY);
            CompletableFuture<Void> future = CompletableFuture.allOf(nextBatch);
            currentLoadingFutures.put(key, future);
            future.join();
            processBatch(world, batchIterator, batchCount);
        }
        else {
            cancelLoadingTask(key);
            currentLoadingFutures.remove(key);
            loadedWorlds.add(key);
            logger.info("Finished loading world {}!", key);
        }
    }

    private void cancelLoadingTask(@NotNull Key key){
        BukkitTask task = loadingTasks.remove(key);
        if(task != null){
            task.cancel();
        }
    }

    private CompletableFuture<Void> destroyWorld(@NotNull World world) {
        world.setAutoSave(false);
        Set<CompletableFuture<Void>> tpFutures = new HashSet<>();
        Location spawn = Bukkit.getWorlds().get(0).getSpawnLocation();
        for (Player p : world.getPlayers()) {
            tpFutures.add(
                    p.teleportAsync(spawn)
                            .thenAccept(success -> {
                                if (!success) {
                                    p.kick(Component.text("Error teleporting to lobby!", NamedTextColor.RED));
                                }
                            })
            );
        }

        CompletableFuture<Void> future = new CompletableFuture<>();

        CompletableFuture.allOf(tpFutures.toArray(WorldManager.EMPTY_FUTURES_ARRAY))
                .thenRun(() -> {
                    Bukkit.unloadWorld(world, false);
                    if(plugin.isEnabled()){
                        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                            FileUtils.deleteFolder(world.getWorldFolder().toPath());
                            future.complete(null);
                        });
                    }
                    else {
                        FileUtils.deleteFolder(world.getWorldFolder().toPath());
                        future.complete(null);
                    }

                });

        return future;
    }

}
