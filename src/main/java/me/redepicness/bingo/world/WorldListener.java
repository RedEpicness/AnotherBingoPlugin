package me.redepicness.bingo.world;

import io.papermc.paper.event.entity.EntityPortalReadyEvent;
import me.redepicness.bingo.AnotherBingoPlugin;
import org.bukkit.Location;
import org.bukkit.PortalType;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerRespawnEvent.RespawnFlag;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class WorldListener implements Listener {

    private final @NotNull Logger logger = LoggerFactory.getLogger("world-listener");
    private final @NotNull WorldManager worldManager = JavaPlugin.getPlugin(AnotherBingoPlugin.class).getWorldManager();

    @EventHandler
    public void onPortal(@NotNull EntityPortalReadyEvent event){
        World world = event.getEntity().getWorld();
        Optional<BingoWorlds> worldsOpt = worldManager.getWorlds(world);
        if(worldsOpt.isEmpty()){
            event.setTargetWorld(null);
            event.setCancelled(true);
            return;
        }
        BingoWorlds worlds = worldsOpt.get();
        World target = null;
        if(event.getPortalType() == PortalType.NETHER){
            target = world.getEnvironment().equals(Environment.NETHER) ? worlds.overworld : worlds.nether;
        }
        else if(event.getPortalType() == PortalType.ENDER){
            target = world.getEnvironment().equals(Environment.THE_END) ? worlds.overworld : worlds.end;
        }
        logger.info("Portal ready from world {}, type {}, target: {}", world.getKey(), event.getPortalType(), target == null ? null : target.getKey());
        event.setTargetWorld(target);
    }

    @EventHandler
    public void onPortal(@NotNull PlayerRespawnEvent event){
        if(!event.getRespawnFlags().contains(RespawnFlag.END_PORTAL)){
            return;
        }

        World world = event.getPlayer().getWorld();
        Optional<BingoWorlds> worldsOpt = worldManager.getWorlds(world);
        if(worldsOpt.isEmpty()){
            return;
        }
        BingoWorlds worlds = worldsOpt.get();
        event.setRespawnLocation(worlds.overworld.getSpawnLocation());
    }

    @EventHandler
    public void onPortal(@NotNull PlayerPortalEvent event){
        if(event.getCause() != TeleportCause.END_PORTAL) {
            return;
        }

        World world = event.getFrom().getWorld();
        Optional<BingoWorlds> worldsOpt = worldManager.getWorlds(world);
        if(worldsOpt.isEmpty()){
            event.setCancelled(true);
            return;
        }
        BingoWorlds worlds = worldsOpt.get();
        logger.info("Player portal event {} {} {}", world.getKey(), event.getTo(), worlds.overworld.getKey());
        switch (world.getEnvironment()){
            case NORMAL, NETHER -> event.getTo().setWorld(worlds.end);
        }
    }

    @EventHandler
    public void onPortal(@NotNull EntityPortalEvent event){
        if(event.getPortalType() != PortalType.ENDER) {
            return;
        }

        World world = event.getFrom().getWorld();
        Optional<BingoWorlds> worldsOpt = worldManager.getWorlds(world);
        if(worldsOpt.isEmpty()){
            event.setCancelled(true);
            return;
        }
        BingoWorlds worlds = worldsOpt.get();
        logger.info("Entity portal event {} {} {}", world.getKey(), event.getTo(), worlds.overworld.getKey());
        final Location to = event.getTo();
        if(to == null) return;
        switch (world.getEnvironment()){
            case NORMAL, NETHER -> to.setWorld(worlds.end);
            case THE_END -> event.setTo(worlds.overworld.getSpawnLocation().add(0, 1, 0));
        }
    }

}
