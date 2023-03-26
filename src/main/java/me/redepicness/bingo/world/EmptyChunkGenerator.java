package me.redepicness.bingo.world;

import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static me.redepicness.bingo.world.VoidBiomeProvider.VOID_BIOME_PROVIDER;

public class EmptyChunkGenerator extends ChunkGenerator {

    private final int platformRadius;
    private final int chunkRadius;

    public EmptyChunkGenerator(int platformRadius) {
        this.platformRadius = platformRadius;
        this.chunkRadius = (platformRadius / 16) + 1;
    }

    @Override
    public void generateNoise(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {
        if (Math.abs(chunkX) > chunkRadius || Math.abs(chunkZ) > chunkRadius) {
            return;
        }

        int safeY = getSafeY(worldInfo.getMinHeight(), worldInfo.getMaxHeight());

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int absX = Math.abs(chunkX * 16 + x);
                int absY = Math.abs(chunkZ * 16 + z);

                if (absX <= platformRadius && absY <= platformRadius) {
                    chunkData.setBlock(x, safeY, z, Material.STONE);
                }
            }
        }
        if (chunkX == 0 && chunkZ == 0) {
            chunkData.setBlock(0, safeY, 0, Material.BEDROCK);
        }
    }

    @Override
    public void generateSurface(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {}

    @Override
    public void generateBedrock(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {}

    @Override
    public void generateCaves(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {}

    @Override
    public @Nullable BiomeProvider getDefaultBiomeProvider(@NotNull WorldInfo worldInfo) {
        return VOID_BIOME_PROVIDER;
    }

    @Override
    public int getBaseHeight(@NotNull WorldInfo worldInfo, @NotNull Random random, int x, int z, @NotNull HeightMap heightMap) {
        if (Math.abs(x) <= platformRadius && Math.abs(z) <= platformRadius) {
            return getSafeY(worldInfo.getMinHeight(), worldInfo.getMaxHeight());
        } else {
            return worldInfo.getMinHeight();
        }
    }

    @Override
    public boolean canSpawn(@NotNull World world, int x, int z) {
        return Math.abs(x) <= platformRadius && Math.abs(z) <= platformRadius;
    }

    @Override
    public @NotNull List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return Collections.emptyList();
    }

    @Override
    public @Nullable Location getFixedSpawnLocation(@NotNull World world, @NotNull Random random) {
        return new Location(world, 0, getSafeY(world.getMinHeight(), world.getMaxHeight()) + 1, 0);
    }

    @Override
    public boolean shouldGenerateNoise() {
        return false;
    }

    @Override
    public boolean shouldGenerateNoise(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ) {
        return false;
    }

    @Override
    public boolean shouldGenerateSurface() {
        return false;
    }

    @Override
    public boolean shouldGenerateSurface(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ) {
        return false;
    }

    @Override
    public boolean shouldGenerateCaves() {
        return false;
    }

    @Override
    public boolean shouldGenerateCaves(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ) {
        return false;
    }

    @Override
    public boolean shouldGenerateDecorations() {
        return false;
    }

    @Override
    public boolean shouldGenerateDecorations(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ) {
        return false;
    }

    @Override
    public boolean shouldGenerateMobs() {
        return false;
    }

    @Override
    public boolean shouldGenerateMobs(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ) {
        return false;
    }

    @Override
    public boolean shouldGenerateStructures() {
        return false;
    }

    @Override
    public boolean shouldGenerateStructures(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ) {
        return false;
    }

    private int getSafeY(int minHeight, int maxHeight) {
        return Math.max(minHeight, Math.min(99, maxHeight - 1));
    }

}
