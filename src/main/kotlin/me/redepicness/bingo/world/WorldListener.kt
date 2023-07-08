package me.redepicness.bingo.world

import io.papermc.paper.event.entity.EntityPortalReadyEvent
import org.bukkit.PortalType
import org.bukkit.World.Environment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPortalEvent
import org.bukkit.event.player.PlayerPortalEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.event.player.PlayerRespawnEvent.RespawnFlag
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause
import org.slf4j.LoggerFactory

class WorldListener : Listener {
    private val logger = LoggerFactory.getLogger("world-listener")

    @EventHandler
    fun onPortal(event: EntityPortalReadyEvent) {
        val world = event.entity.world
        val worlds = WorldManager.getWorlds(world)
        if (worlds == null) {
            event.targetWorld = null
            event.isCancelled = true
            return
        }

        val target = when (event.portalType) {
            PortalType.NETHER -> if (world.environment == Environment.NETHER) worlds.overworld else worlds.nether
            PortalType.ENDER -> if (world.environment == Environment.THE_END) worlds.overworld else worlds.end
            else -> null
        }
        logger.info("Portal ready from world {}, type {}, target: {}", world.key, event.portalType, target?.key)
        event.targetWorld = target
    }

    @EventHandler
    fun onPortal(event: PlayerRespawnEvent) {
        if (!event.respawnFlags.contains(RespawnFlag.END_PORTAL)) return

        val world = event.player.world
        val worlds = WorldManager.getWorlds(world) ?: return
        event.respawnLocation = worlds.overworld.spawnLocation
    }

    @EventHandler
    fun onPortal(event: PlayerPortalEvent) {
        if (event.cause != TeleportCause.END_PORTAL) return

        val world = event.from.world
        val worlds = WorldManager.getWorlds(world)
        if (worlds == null) {
            event.isCancelled = true
            return
        }
        logger.info("Player portal event {} {} {}", world.key, event.to, worlds.overworld.key)
        if (world.environment == Environment.NORMAL || world.environment == Environment.NETHER) {
            event.to.world = worlds.end
        }
    }

    @EventHandler
    fun onPortal(event: EntityPortalEvent) {
        if (event.portalType != PortalType.ENDER) {
            return
        }
        val world = event.from.world
        val worlds = WorldManager.getWorlds(world)
        if (worlds == null) {
            event.isCancelled = true
            return
        }

        logger.info("Entity portal event {} {} {}", world.key, event.to, worlds.overworld.key)
        val to = event.to ?: return
        when (world.environment) {
            Environment.NORMAL, Environment.NETHER -> to.world = worlds.end
            Environment.THE_END -> event.to = worlds.overworld.spawnLocation.add(0.0, 1.0, 0.0)
            else -> {}
        }
    }
}
