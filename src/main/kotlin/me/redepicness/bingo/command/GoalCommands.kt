package me.redepicness.bingo.command

import cloud.commandframework.paper.PaperCommandManager
import me.redepicness.bingo.goal.GoalDifficulty
import me.redepicness.bingo.goal.GoalManager
import me.redepicness.bingo.goal.GoalType
import net.kyori.adventure.text.Component
import org.bukkit.command.CommandSender

object GoalCommands {
    fun registerCommands(commandManager: PaperCommandManager<CommandSender>) {
        val builder = commandManager.commandBuilder("goals")
        val permissionPrefix = "abp.goals."

        commandManager.command(
                builder
                        .literal("summary")
                        .permission(permissionPrefix + "summary")
                        .handler { context ->
                            val sender = context.sender
                            sender.sendMessage(Component.text("Registered Goals summary:"))
                            sender.sendMessage(Component.text("Goal count: ${GoalManager.goals.size}"))
                            sender.sendMessage(Component.text("Goal difficulties:"))
                            for (difficulty in GoalDifficulty.entries) {
                                sender.sendMessage(Component.text("- $difficulty: ${GoalManager.getGoalsByDifficulty(difficulty).size}"))
                            }
                            sender.sendMessage(Component.text("Goal tags:"))
                            for ((tag, goals) in GoalManager.goalsByTag) {
                                sender.sendMessage(Component.text("- $tag: ${goals.size}"))
                            }
                            sender.sendMessage(Component.text("Goal types:"))
                            for (type in GoalType.TYPES) {
                                sender.sendMessage(Component.text("- $type: ${type.goals.size}"))
                            }
                        }
        )
    }
}
