package me.redepicness.bingo.world;

import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class VoidBiomeProvider extends BiomeProvider {

    public static final @NotNull VoidBiomeProvider VOID_BIOME_PROVIDER = new VoidBiomeProvider();

    private VoidBiomeProvider() {}

    @Override
    public @NotNull Biome getBiome(@NotNull WorldInfo worldInfo, int x, int y, int z) {
        return Biome.THE_VOID;
    }

    @Override
    public @NotNull List<Biome> getBiomes(@NotNull WorldInfo worldInfo) {
        return Collections.singletonList(Biome.THE_VOID);
    }

}
