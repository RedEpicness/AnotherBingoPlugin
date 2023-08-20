package me.redepicness.bingo.command.argument

import cloud.commandframework.arguments.CommandArgument
import cloud.commandframework.arguments.parser.ArgumentParseResult
import cloud.commandframework.arguments.parser.ArgumentParser
import cloud.commandframework.context.CommandContext
import cloud.commandframework.exceptions.parsing.NoInputProvidedException
import io.leangen.geantyref.TypeToken
import me.redepicness.bingo.team.Team
import me.redepicness.bingo.team.TeamManager
import org.bukkit.command.CommandSender
import java.util.*

class BingoTeamCommandArgument(required: Boolean, name: String) : CommandArgument<CommandSender, Team>(
        required,
        name,
        BingoTeamParser(),
        "",
        TypeToken.get(Team::class.java),
        null
) {
    class BingoTeamParser : ArgumentParser<CommandSender, Team> {
        override fun parse(context: CommandContext<CommandSender>, inputQueue: Queue<String>): ArgumentParseResult<Team> {
            val input = inputQueue.peek() ?: return ArgumentParseResult.failure(NoInputProvidedException(BingoTeamParser::class.java, context))
            val team = TeamManager.teams[input.lowercase()]
                ?: return ArgumentParseResult.failure(IllegalArgumentException("Invalid key " + getCurrentArgumentInfo(context) + ", no team found! (" + input + ")"))
            inputQueue.poll()
            return ArgumentParseResult.success(team)
        }

        override fun suggestions(commandContext: CommandContext<CommandSender>, input: String): List<String> {
            return TeamManager.teams.keys
                    .stream()
                    .filter { it.startsWith(input) }
                    .toList()
        }

        override fun isContextFree(): Boolean {
            return true
        }
    }

    companion object {
        fun of(name: String): BingoTeamCommandArgument {
            return BingoTeamCommandArgument(true, name)
        }

        fun optional(name: String): BingoTeamCommandArgument {
            return BingoTeamCommandArgument(false, name)
        }

        private fun getCurrentArgumentInfo(commandContext: CommandContext<CommandSender>): String {
            val currentArgument = commandContext.currentArgument ?: return "unknown"
            return "BingoTeam '" + currentArgument.name + "'"
        }
    }
}
