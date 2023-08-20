package me.redepicness.bingo.command

import cloud.commandframework.arguments.standard.IntegerArgument
import cloud.commandframework.paper.PaperCommandManager
import me.redepicness.bingo.card.CardDifficulties
import me.redepicness.bingo.game.GameManager
import me.redepicness.bingo.goal.GoalDifficulty
import net.kyori.adventure.text.Component
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object CardCommands {
    fun registerCommands(commandManager: PaperCommandManager<CommandSender>) {
        val builder = commandManager.commandBuilder("card")
        val permissionPrefix = "abp.card."

        commandManager.command(
            builder
                .literal("difficulty")
                .argument(IntegerArgument.of("free"))
                .argument(IntegerArgument.of("easy"))
                .argument(IntegerArgument.of("medium"))
                .argument(IntegerArgument.of("hard"))
                .argument(IntegerArgument.of("insane"))
                .permission(permissionPrefix + "difficulty")
                .handler { context ->
                    val sender = context.sender
                    val difficulties = CardDifficulties()

                    difficulties.set(GoalDifficulty.FREE, context.get("free"))
                    difficulties.set(GoalDifficulty.EASY, context.get("easy"))
                    difficulties.set(GoalDifficulty.MEDIUM, context.get("medium"))
                    difficulties.set(GoalDifficulty.HARD, context.get("hard"))
                    difficulties.set(GoalDifficulty.INSANE, context.get("insane"))


                    if (GameManager.game != null) {
                        GameManager.game?.difficulties = difficulties
                        sender.sendMessage("Difficulties set")
                    }
                    else {
                        sender.sendMessage(Component.text("No game..."))
                    }
                }
        )

        commandManager.command(
            builder
                .literal("view")
                .permission(permissionPrefix + "view")
                .senderType(Player::class.java)
                .handler { context ->
                    val sender = context.sender as Player
                    val game = GameManager.game
                    if (game != null) {
                        sender.openInventory(game.card.createInventory())
                    }
                    else {
                        sender.sendMessage(Component.text("No game..."))
                    }
                }
        )

        commandManager.command(
            builder
                .literal("reroll")
                .permission(permissionPrefix + "reroll")
                .senderType(Player::class.java)
                .handler { context ->
                    val sender = context.sender as Player
                    val game = GameManager.game
                    if (game != null) {
                        game.gridSize = game.gridSize // todo lmao
                        sender.sendMessage("Rerolled")
                    }
                    else {
                        sender.sendMessage(Component.text("No game..."))
                    }
                }
        )
    }
}
