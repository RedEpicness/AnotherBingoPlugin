package me.redepicness.bingo.command;

import cloud.commandframework.Command.Builder;
import cloud.commandframework.arguments.flags.CommandFlag;
import cloud.commandframework.arguments.standard.IntegerArgument;
import cloud.commandframework.bukkit.parsers.WorldArgument;
import cloud.commandframework.paper.PaperCommandManager;
import me.redepicness.bingo.AnotherBingoPlugin;
import me.redepicness.bingo.world.BingoWorlds;
import me.redepicness.bingo.world.BingoWorldsConfig;
import me.redepicness.bingo.world.BingoWorldsConfig.BingoWorldPreloadConfig;
import me.redepicness.bingo.world.WorldManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class WorldCommands {

    public static void registerCommands(@NotNull PaperCommandManager<CommandSender> commandManager){

        Builder<CommandSender> builder = commandManager.commandBuilder("worlds");

        String permissionPrefix = "abp.worlds.";

        AnotherBingoPlugin plugin = JavaPlugin.getPlugin(AnotherBingoPlugin.class);
        WorldManager worldManager = plugin.getWorldManager();

        commandManager.command(
                builder
                        .literal("current")
                        .permission(permissionPrefix+"current")
                        .handler(context -> {
                            CommandSender sender = context.getSender();

                            if(sender instanceof Player player){
                                player.sendMessage(Component.text("Your world: "+player.getWorld().getKey()));
                            }
                            else {
                                sender.sendMessage(Component.text("You are not in a world, silly console."));
                            }
                        })

        );

        commandManager.command(
                builder
                        .literal("goto")
                        .argument(WorldArgument.of("world"))
                        .permission(permissionPrefix+"goto")
                        .senderType(Player.class)
                        .handler(context -> {
                            Player player = ((Player) context.getSender());
                            World world = context.get("world");

                            player.teleportAsync(world.getSpawnLocation());
                        })

        );

        commandManager.command(
                builder
                        .literal("list")
                        .permission(permissionPrefix+"list")
                        .handler(context -> {
                            CommandSender sender = context.getSender();

                            sender.sendMessage(Component.text("All worlds:"));
                            for (BingoWorlds worlds : worldManager.getAllWorlds()) {
                                sender.sendMessage(Component.text("- "+worlds.getOverworld().getKey()));
                                sender.sendMessage("  - Used: "+worlds.isUsed());
                                sender.sendMessage("  - Overworld loaded: "+worlds.isOverworldLoaded());
                                sender.sendMessage("  - Nether loaded: "+worlds.isNetherLoaded());
                                sender.sendMessage("  - End loaded: "+worlds.isEndLoaded());
                            }
                        })

        );

        commandManager.command(
                builder
                        .literal("create")
                        .flag(CommandFlag.builder("overworld-radius").withArgument(IntegerArgument.of("radius")))
                        .flag(CommandFlag.builder("nether-radius").withArgument(IntegerArgument.of("radius")))
                        .flag(CommandFlag.builder("end-radius").withArgument(IntegerArgument.of("radius")))
                        .flag(CommandFlag.builder("no-preload"))
                        .permission(permissionPrefix+"create")
                        .handler(context -> {
                            CommandSender sender = context.getSender();
                            BingoWorldPreloadConfig config = new BingoWorldPreloadConfig();
                            int overworldRadius = context.flags().<Integer>getValue("overworld-radius").orElse(config.overworldRadius());
                            int netherRadius = context.flags().<Integer>getValue("nether-radius").orElse(config.netherRadius());
                            int endRadius = context.flags().<Integer>getValue("end-radius").orElse(config.endRadius());
                            boolean preload = !context.flags().hasFlag("no-preload");

                            BingoWorldPreloadConfig conf = new BingoWorldPreloadConfig(preload, overworldRadius, netherRadius, endRadius);
                            BingoWorldsConfig c = new BingoWorldsConfig(conf);

                            Bukkit.getScheduler().runTask(plugin, () -> {
                                sender.sendMessage(Component.text("Creating new world!"));

                                worldManager.createNewWorld(c).thenAccept(worlds -> {
                                    sender.sendMessage(Component.text("Created world "+worlds.getOverworld().getKey()+"!"));
                                });
                            });
                        })
        );

        commandManager.command(
                builder
                        .literal("stop-loading")
                        .argument(BingoWorldsCommandArgument.of("worlds"))
                        .permission(permissionPrefix+"stop-loading")
                        .handler(context -> {
                            CommandSender sender = context.getSender();
                            BingoWorlds worlds = context.get("worlds");



                            sender.sendMessage(Component.text("Stopping loading chunks for worlds "+worlds.getOverworld().getKey()+"!"));
                            CompletableFuture<Void> future = worlds.stopLoadingChunks();
                            future.thenRun(() -> {
                                sender.sendMessage(Component.text("Stopped loading chunks for worlds "+worlds.getOverworld().getKey()+"!"));
                            });

                        })
        );

        commandManager.command(
                builder
                        .literal("remove")
                        .argument(BingoWorldsCommandArgument.of("worlds"))
                        .permission(permissionPrefix+"remove")
                        .handler(context -> {
                            CommandSender sender = context.getSender();
                            BingoWorlds worlds = context.get("worlds");

                            sender.sendMessage(Component.text("Deleting worlds "+worlds.getOverworld().getKey()+"!"));
                            CompletableFuture<Void> future = worldManager.removeWorlds(worlds);
                            future.thenRun(() -> {
                                sender.sendMessage(Component.text("Deleted worlds "+worlds.getOverworld().getKey()+"!"));
                            });

                        })
        );

    }

}
