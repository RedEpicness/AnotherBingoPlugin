package me.redepicness.bingo.command

import cloud.commandframework.arguments.flags.CommandFlag
import cloud.commandframework.arguments.standard.IntegerArgument
import cloud.commandframework.bukkit.parsers.WorldArgument
import cloud.commandframework.context.CommandContext
import cloud.commandframework.paper.PaperCommandManager
import me.redepicness.bingo.command.argument.BingoWorldsCommandArgument
import me.redepicness.bingo.plugin
import me.redepicness.bingo.world.BingoWorldPreloadConfig
import me.redepicness.bingo.world.BingoWorlds
import me.redepicness.bingo.world.BingoWorldsConfig
import me.redepicness.bingo.world.WorldManager
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object WorldCommands {
    fun registerCommands(commandManager: PaperCommandManager<CommandSender>) {
        val builder = commandManager.commandBuilder("worlds")
        val permissionPrefix = "abp.worlds."

        commandManager.command(
                builder
                        .literal("current")
                        .permission(permissionPrefix + "current")
                        .handler { context: CommandContext<CommandSender> ->
                            val sender = context.sender
                            if (sender is Player) {
                                sender.sendMessage(Component.text("Your world: " + sender.world.key))
                            } else {
                                sender.sendMessage(Component.text("You are not in a world, silly console."))
                            }
                        }
        )
        commandManager.command(
                builder
                        .literal("goto")
                        .argument(WorldArgument.of("world"))
                        .permission(permissionPrefix + "goto")
                        .senderType(Player::class.java)
                        .handler { context: CommandContext<CommandSender> ->
                            val player = context.sender as Player
                            val world = context.get<World>("world")
                            player.teleportAsync(world.spawnLocation)
                        }
        )
        commandManager.command(
                builder
                        .literal("list")
                        .permission(permissionPrefix + "list")
                        .handler { context ->
                            val sender = context.sender
                            sender.sendMessage(Component.text("All worlds:"))
                            for (worlds in WorldManager.allWorlds) {
                                sender.sendMessage(Component.text("- " + worlds.overworld.key))
                                sender.sendMessage("  - Used: " + worlds.isUsed)
                                sender.sendMessage("  - Overworld loaded: " + worlds.isOverworldLoaded)
                                sender.sendMessage("  - Nether loaded: " + worlds.isNetherLoaded)
                                sender.sendMessage("  - End loaded: " + worlds.isEndLoaded)
                            }
                        }
        )
        commandManager.command(
                builder
                        .literal("create")
                        .flag(CommandFlag.builder("overworld-radius").withArgument(IntegerArgument.of<Any>("radius")))
                        .flag(CommandFlag.builder("nether-radius").withArgument(IntegerArgument.of<Any>("radius")))
                        .flag(CommandFlag.builder("end-radius").withArgument(IntegerArgument.of<Any>("radius")))
                        .flag(CommandFlag.builder("no-preload"))
                        .permission(permissionPrefix + "create")
                        .handler { context ->
                            val sender = context.sender
                            val config = BingoWorldPreloadConfig()
                            val overworldRadius = context.flags().getValue<Int>("overworld-radius").orElse(config.overworldRadius)
                            val netherRadius = context.flags().getValue<Int>("nether-radius").orElse(config.netherRadius)
                            val endRadius = context.flags().getValue<Int>("end-radius").orElse(config.endRadius)
                            val preload = !context.flags().hasFlag("no-preload")
                            val conf = BingoWorldPreloadConfig(preload, overworldRadius, netherRadius, endRadius)
                            val c = BingoWorldsConfig(conf)
                            Bukkit.getScheduler().runTask(plugin, Runnable {
                                sender.sendMessage(Component.text("Creating new world!"))
                                WorldManager.createNewWorld(c).thenAccept { worlds: BingoWorlds -> sender.sendMessage(Component.text("Created world " + worlds.overworld.key + "!")) }
                            })
                        }
        )
        commandManager.command(
                builder
                        .literal("stop-loading")
                        .argument<BingoWorlds>(BingoWorldsCommandArgument.of("worlds"))
                        .permission(permissionPrefix + "stop-loading")
                        .handler { context ->
                            val sender = context.sender
                            val worlds = context.get<BingoWorlds>("worlds")
                            sender.sendMessage(Component.text("Stopping loading chunks for worlds " + worlds.overworld.key + "!"))
                            val future = worlds.stopLoadingChunks()
                            future.thenRun { sender.sendMessage(Component.text("Stopped loading chunks for worlds " + worlds.overworld.key + "!")) }
                        }
        )
        commandManager.command(
                builder
                        .literal("remove")
                        .argument<BingoWorlds>(BingoWorldsCommandArgument.of("worlds"))
                        .permission(permissionPrefix + "remove")
                        .handler { context ->
                            val sender = context.sender
                            val worlds = context.get<BingoWorlds>("worlds")
                            sender.sendMessage(Component.text("Deleting worlds " + worlds.overworld.key + "!"))
                            val future = WorldManager.removeWorlds(worlds)
                            future.thenRun { sender.sendMessage(Component.text("Deleted worlds " + worlds.overworld.key + "!")) }
                        }
        )
    }
}
