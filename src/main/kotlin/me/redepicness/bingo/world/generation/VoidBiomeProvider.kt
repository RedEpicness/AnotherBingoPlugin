package me.redepicness.bingo.world.generation

import org.bukkit.block.Biome
import org.bukkit.generator.BiomeProvider
import org.bukkit.generator.WorldInfo

object VoidBiomeProvider : BiomeProvider() {
    override fun getBiome(worldInfo: WorldInfo, x: Int, y: Int, z: Int): Biome {
        return Biome.THE_VOID
    }

    override fun getBiomes(worldInfo: WorldInfo): List<Biome> {
        return listOf(Biome.THE_VOID)
    }

}
