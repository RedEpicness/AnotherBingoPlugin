package me.redepicness.bingo.command.argument

import cloud.commandframework.arguments.CommandArgument
import cloud.commandframework.arguments.parser.ArgumentParseResult
import cloud.commandframework.arguments.parser.ArgumentParser
import cloud.commandframework.context.CommandContext
import cloud.commandframework.exceptions.parsing.NoInputProvidedException
import io.leangen.geantyref.TypeToken
import me.redepicness.bingo.world.BingoWorlds
import me.redepicness.bingo.world.WorldManager
import org.bukkit.NamespacedKey
import org.bukkit.command.CommandSender
import java.util.*

class BingoWorldsCommandArgument(required: Boolean, name: String) : CommandArgument<CommandSender, BingoWorlds>(
        required,
        name,
        BingoWorldsParser(),
        "",
        TypeToken.get(BingoWorlds::class.java),
        null
) {
    class BingoWorldsParser : ArgumentParser<CommandSender, BingoWorlds> {
        override fun parse(context: CommandContext<CommandSender>, inputQueue: Queue<String>): ArgumentParseResult<BingoWorlds> {
            val input = inputQueue.peek() ?: return ArgumentParseResult.failure(NoInputProvidedException(BingoWorldsParser::class.java, context))
            val key = try {
                NamespacedKey("abp", "bingo-$input")
            } catch (e: Exception) {
                return ArgumentParseResult.failure(IllegalArgumentException("Could not parse " + getCurrentArgumentInfo(context) + ", invalid key! (" + input + ")"))
            }
            val worlds = WorldManager.getWorlds(key)
                ?: return ArgumentParseResult.failure(IllegalArgumentException("Invalid key " + getCurrentArgumentInfo(context) + ", no bingoWorlds found! (" + input + ")"))
            inputQueue.poll()
            return ArgumentParseResult.success(worlds)
        }

        override fun suggestions(commandContext: CommandContext<CommandSender>, input: String): List<String> {
            return WorldManager.allWorlds
                    .stream()
                    .map { it.overworld.key.key.substring(6) }
                    .filter { it.startsWith(input) }
                    .toList()
        }

        override fun isContextFree(): Boolean {
            return true
        }
    }

    companion object {
        fun of(name: String): BingoWorldsCommandArgument {
            return BingoWorldsCommandArgument(true, name)
        }

        fun optional(name: String): BingoWorldsCommandArgument {
            return BingoWorldsCommandArgument(false, name)
        }

        private fun getCurrentArgumentInfo(commandContext: CommandContext<CommandSender>): String {
            val currentArgument = commandContext.currentArgument ?: return "unknown"
            return "BingoWorlds '" + currentArgument.name + "'"
        }
    }
}
