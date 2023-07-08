package me.redepicness.bingo.world.generation

import org.bukkit.HeightMap
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.generator.BiomeProvider
import org.bukkit.generator.BlockPopulator
import org.bukkit.generator.ChunkGenerator
import org.bukkit.generator.WorldInfo
import java.util.*
import kotlin.math.abs
import kotlin.math.absoluteValue

class EmptyChunkGenerator(private val platformRadius: Int) : ChunkGenerator() {

    private val chunkRadius: Int = platformRadius / 16 + 1

    override fun generateNoise(worldInfo: WorldInfo, random: Random, chunkX: Int, chunkZ: Int, chunkData: ChunkData) {
        if (chunkX.absoluteValue > chunkRadius || chunkZ.absoluteValue > chunkRadius) {
            return
        }
        val safeY = getSafeY(worldInfo.minHeight, worldInfo.maxHeight)
        for (x in 0..15) {
            for (z in 0..15) {
                val absX = abs(chunkX * 16 + x)
                val absY = abs(chunkZ * 16 + z)
                if (absX <= platformRadius && absY <= platformRadius) {
                    chunkData.setBlock(x, safeY, z, Material.STONE)
                }
            }
        }
        if (chunkX == 0 && chunkZ == 0) {
            chunkData.setBlock(0, safeY, 0, Material.BEDROCK)
        }
    }

    override fun generateSurface(worldInfo: WorldInfo, random: Random, chunkX: Int, chunkZ: Int, chunkData: ChunkData) {}
    override fun generateBedrock(worldInfo: WorldInfo, random: Random, chunkX: Int, chunkZ: Int, chunkData: ChunkData) {}
    override fun generateCaves(worldInfo: WorldInfo, random: Random, chunkX: Int, chunkZ: Int, chunkData: ChunkData) {}
    override fun getDefaultBiomeProvider(worldInfo: WorldInfo): BiomeProvider {
        return VoidBiomeProvider
    }

    override fun getBaseHeight(worldInfo: WorldInfo, random: Random, x: Int, z: Int, heightMap: HeightMap): Int {
        return if (x.absoluteValue <= platformRadius && z.absoluteValue <= platformRadius) {
            getSafeY(worldInfo.minHeight, worldInfo.maxHeight)
        } else {
            worldInfo.minHeight
        }
    }

    override fun canSpawn(world: World, x: Int, z: Int): Boolean {
        return x.absoluteValue <= platformRadius && z.absoluteValue <= platformRadius
    }

    override fun getDefaultPopulators(world: World): List<BlockPopulator> {
        return emptyList()
    }

    override fun getFixedSpawnLocation(world: World, random: Random): Location? {
        return Location(world, 0.0, (getSafeY(world.minHeight, world.maxHeight) + 1).toDouble(), 0.0)
    }

    override fun shouldGenerateNoise(): Boolean {
        return false
    }

    override fun shouldGenerateNoise(worldInfo: WorldInfo, random: Random, chunkX: Int, chunkZ: Int): Boolean {
        return false
    }

    override fun shouldGenerateSurface(): Boolean {
        return false
    }

    override fun shouldGenerateSurface(worldInfo: WorldInfo, random: Random, chunkX: Int, chunkZ: Int): Boolean {
        return false
    }

    override fun shouldGenerateCaves(): Boolean {
        return false
    }

    override fun shouldGenerateCaves(worldInfo: WorldInfo, random: Random, chunkX: Int, chunkZ: Int): Boolean {
        return false
    }

    override fun shouldGenerateDecorations(): Boolean {
        return false
    }

    override fun shouldGenerateDecorations(worldInfo: WorldInfo, random: Random, chunkX: Int, chunkZ: Int): Boolean {
        return false
    }

    override fun shouldGenerateMobs(): Boolean {
        return false
    }

    override fun shouldGenerateMobs(worldInfo: WorldInfo, random: Random, chunkX: Int, chunkZ: Int): Boolean {
        return false
    }

    override fun shouldGenerateStructures(): Boolean {
        return false
    }

    override fun shouldGenerateStructures(worldInfo: WorldInfo, random: Random, chunkX: Int, chunkZ: Int): Boolean {
        return false
    }

    private fun getSafeY(minHeight: Int, maxHeight: Int): Int {
        return 99.coerceIn(minHeight..<maxHeight) // Math.max(minHeight, 99.coerceAtMost(maxHeight -1))
    }
}
