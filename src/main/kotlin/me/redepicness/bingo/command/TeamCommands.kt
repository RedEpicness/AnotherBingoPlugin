package me.redepicness.bingo.command

import cloud.commandframework.arguments.standard.StringArgument
import cloud.commandframework.paper.PaperCommandManager
import me.redepicness.bingo.command.argument.BingoTeamCommandArgument
import me.redepicness.bingo.team.Team
import me.redepicness.bingo.team.getTeam
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object TeamCommands {
    fun registerCommands(commandManager: PaperCommandManager<CommandSender>) {
        val builder = commandManager.commandBuilder("team")
        val permissionPrefix = "abp.team."

        commandManager.command(
                builder
                    .literal("join")
                    .argument(BingoTeamCommandArgument.of("team"))
                    .permission(permissionPrefix + "join")
                    .senderType(Player::class.java)
                    .handler { context ->
                        val sender = context.sender as Player
                        val team: Team = context["team"]

                        team.addMember(sender)
                        Bukkit.broadcast(sender.displayName().append(Component.text(" has joined team ", NamedTextColor.WHITE)).append(team.displayName()))
                    }
        )

        commandManager.command(
            builder
                .literal("leave")
                .permission(permissionPrefix + "leave")
                .senderType(Player::class.java)
                .handler { context ->
                    val sender = context.sender as Player
                    val team: Team? = sender.getTeam()

                    if(team == null){
                        sender.sendMessage(Component.text("Not in a team!", NamedTextColor.RED))
                        return@handler
                    }

                    team.removeMember(sender)
                    Bukkit.broadcast(sender.displayName().append(Component.text(" has left team ", NamedTextColor.WHITE)).append(team.displayName()))
                }
        )

        commandManager.command(
            builder
                .literal("rename")
                .argument(StringArgument.greedy("name"))
                .permission(permissionPrefix + "rename")
                .senderType(Player::class.java)
                .handler { context ->
                    val sender = context.sender as Player
                    val team: Team? = sender.getTeam()
                    val name: String = context["name"]

                    if(team == null){
                        sender.sendMessage(Component.text("Not in a team!", NamedTextColor.RED))
                        return@handler
                    }

                    team.name = name
                    Bukkit.broadcast(Component.text("Renamed team to: ", NamedTextColor.WHITE).append(team.displayName()))
                }
        )
    }
}
