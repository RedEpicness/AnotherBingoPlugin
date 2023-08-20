package me.redepicness.bingo.command

import cloud.commandframework.paper.PaperCommandManager
import me.redepicness.bingo.game.GameManager
import me.redepicness.bingo.world.WorldManager
import net.kyori.adventure.text.Component
import org.bukkit.command.CommandSender

object GameCommands {
    fun registerCommands(commandManager: PaperCommandManager<CommandSender>) {
        val builder = commandManager.commandBuilder("game")
        val permissionPrefix = "abp.game."

        commandManager.command(
            builder
                .literal("create")
                .permission(permissionPrefix + "create")
                .handler { context ->
                    val sender = context.sender


                    sender.sendMessage("Game creating...")
                    WorldManager.getWorldsForGame().whenComplete{ worlds, e  ->
                        if(worlds != null){
                            GameManager.createGame(worlds)
                            sender.sendMessage("Game created!")
                        }
                        else {
                            sender.sendMessage("Error creating worlds?")
                            e.printStackTrace()
                        }
                    }
                }
        )

        commandManager.command(
            builder
                .literal("start")
                .permission(permissionPrefix + "start")
                .handler { context ->
                    val sender = context.sender

                    if (GameManager.game != null) {
                        GameManager.game?.startGame()
                    }
                    else {
                        sender.sendMessage(Component.text("No game..."))
                    }
                }
        )
    }
}
