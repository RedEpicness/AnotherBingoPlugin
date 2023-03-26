package me.redepicness.bingo.world;

import me.redepicness.bingo.AnotherBingoPlugin;
import net.kyori.adventure.util.TriState;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class WorldManager {

    //TODO Make these configurable
    static final int CHUNK_RADIUS_LOADING_STEP = 5;
    static final int CHUNK_LOADS_PER_BATCH = 200;
    static final int MIN_WORLDS_TO_KEEP_READY = 1;
    static final CompletableFuture<?>[] EMPTY_FUTURES_ARRAY = new CompletableFuture[0];

    private final @NotNull Logger logger = LoggerFactory.getLogger("world-manager");
    private final @NotNull AnotherBingoPlugin plugin;
    private final @NotNull Map<NamespacedKey, BingoWorlds> gameWorlds = new ConcurrentHashMap<>();
    private BukkitTask task;

    public WorldManager(@NotNull AnotherBingoPlugin plugin) {
        this.plugin = plugin;
    }

    @Internal
    public void setup(){
        AnotherBingoPlugin plugin = JavaPlugin.getPlugin(AnotherBingoPlugin.class);
        Bukkit.getPluginManager().registerEvents(new WorldListener(), plugin);
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::processWorlds, 0, 10 * 20);
    }

    @Internal
    public void shutdown(){
        if(task != null){
            task.cancel();
        }
        Set<CompletableFuture<Void>> futures = new HashSet<>();
        for (BingoWorlds worlds : new HashSet<>(gameWorlds.values())) {
            futures.add(removeWorlds(worlds));
        }
        CompletableFuture.allOf(futures.toArray(WorldManager.EMPTY_FUTURES_ARRAY)).join();
    }

    public @NotNull Set<BingoWorlds> getAllWorlds(){
        return new HashSet<>(gameWorlds.values());
    }

    public @NotNull Optional<BingoWorlds> getWorlds(@NotNull World world){
        return getWorlds(world.getKey());
    }

    public @NotNull Optional<BingoWorlds> getWorlds(@NotNull NamespacedKey key){
        return Optional.ofNullable(gameWorlds.get(key));
    }

    public @NotNull CompletableFuture<Void> removeWorlds(@NotNull BingoWorlds worlds){
        gameWorlds.remove(worlds.overworld.getKey());
        gameWorlds.remove(worlds.nether.getKey());
        gameWorlds.remove(worlds.end.getKey());
        return worlds.destroyWorlds();
    }

    public @NotNull CompletableFuture<BingoWorlds> createNewWorld(@NotNull BingoWorldsConfig config){
        NamespacedKey overworldKey = generateWorldKey();
        NamespacedKey netherKey = new NamespacedKey(overworldKey.namespace(), overworldKey.getKey()+"-nether");
        NamespacedKey endKey = new NamespacedKey(overworldKey.namespace(), overworldKey.getKey()+"-end");

        CompletableFuture<BingoWorlds> future = new CompletableFuture<>();

        logger.info("Creating new worlds for key {}...", overworldKey);

        WorldCreator overworldCreator = new WorldCreator(overworldKey)
                .environment(Environment.NORMAL)
                .keepSpawnLoaded(TriState.FALSE)
                .type(WorldType.NORMAL);

        WorldCreator netherCreator = new WorldCreator(netherKey)
                .environment(Environment.NETHER)
                .keepSpawnLoaded(TriState.FALSE)
                .type(WorldType.NORMAL);

        WorldCreator endCreator = new WorldCreator(endKey)
                .environment(Environment.THE_END)
                .keepSpawnLoaded(TriState.FALSE)
                .type(WorldType.NORMAL);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            logger.info("Creating overworld with key {}...", overworldKey);
            World overworld = overworldCreator.createWorld();
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                logger.info("Creating nether with key {}...", netherKey);
                World nether = netherCreator.createWorld();
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    logger.info("Creating end with key {}...", endKey);
                    World end = endCreator.createWorld();

                    if(overworld == null){
                        logger.warn("Failed to create overworld {}!", overworldKey);
                        future.completeExceptionally(new IllegalStateException());
                        return;
                    }
                    if(nether == null){
                        logger.warn("Failed to create nether {}!", netherKey);
                        future.completeExceptionally(new IllegalStateException());
                        return;
                    }
                    if(end == null){
                        logger.warn("Failed to create end {}!", endKey);
                        future.completeExceptionally(new IllegalStateException());
                        return;
                    }

                    BingoWorlds bingoWorlds = new BingoWorlds(config, overworld, nether, end);
                    gameWorlds.put(overworldKey, bingoWorlds);
                    gameWorlds.put(netherKey, bingoWorlds);
                    gameWorlds.put(endKey, bingoWorlds);

                    logger.info("Created worlds for key {}!", overworldKey);

                    if(config.preloading().preload()){
                        bingoWorlds.startLoadingWorlds();
                    }

                    future.complete(bingoWorlds);

                }, 20);
            }, 20);
        }, 20);

        return future;
    }

    private void processWorlds(){
        int readyWorlds = 0;

        for (BingoWorlds worlds : new HashSet<>(gameWorlds.values())) {
            if(!worlds.used) {
                readyWorlds++;
            }
        }

        for (int i = readyWorlds; i < MIN_WORLDS_TO_KEEP_READY; i++) {
            logger.info("Creating new world, as current number of ready worlds is too low! {} < {}", readyWorlds, MIN_WORLDS_TO_KEEP_READY);
            Bukkit.getScheduler().runTask(plugin, () -> createNewWorld(new BingoWorldsConfig())); //TODO Use a default config
        }
    }

    private @NotNull NamespacedKey generateWorldKey(){
        NamespacedKey key;
        do {
            @Subst("1a2b3c4d") String uuid = UUID.randomUUID().toString().substring(0, 8);
            key = new NamespacedKey("abp", "bingo-"+ uuid);
        }
        while (gameWorlds.containsKey(key));
        return key;
    }

}
