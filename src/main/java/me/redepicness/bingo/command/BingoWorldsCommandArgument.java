package me.redepicness.bingo.command;

import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.exceptions.parsing.NoInputProvidedException;
import io.leangen.geantyref.TypeToken;
import me.redepicness.bingo.AnotherBingoPlugin;
import me.redepicness.bingo.world.BingoWorlds;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.Queue;

public class BingoWorldsCommandArgument extends CommandArgument<CommandSender, BingoWorlds> {

    public static @NonNull BingoWorldsCommandArgument of(final @NonNull String name) {
        return new BingoWorldsCommandArgument(true, name);
    }

    public static @NonNull BingoWorldsCommandArgument optional(final @NonNull String name) {
        return new BingoWorldsCommandArgument(false, name);
    }

    public BingoWorldsCommandArgument(boolean required, @NonNull String name) {
        super(
                required,
                name,
                new BingoWorldsParser(),
                "",
                TypeToken.get(BingoWorlds.class),
                null
        );
    }

    public static final class BingoWorldsParser implements ArgumentParser<CommandSender, BingoWorlds> {

        @Override
        public @NonNull ArgumentParseResult<@NonNull BingoWorlds> parse(
                @NonNull CommandContext<@NonNull CommandSender> context,
                @NonNull Queue<@NonNull String> inputQueue
        ) {
            String input = inputQueue.peek();
            if (input == null) {
                return ArgumentParseResult.failure(new NoInputProvidedException(BingoWorldsParser.class, context));
            }

            NamespacedKey key;
            try {
                key = new NamespacedKey("abp", "bingo-"+input);
            }
            catch (Exception e) {
                return ArgumentParseResult.failure(new IllegalArgumentException("Could not parse " + getCurrentArgumentInfo(context) + ", invalid key! (" + input + ")"));
            }
            Optional<BingoWorlds> worlds = JavaPlugin.getPlugin(AnotherBingoPlugin.class).getWorldManager().getWorlds(key);
            if (worlds.isEmpty()) {
                return ArgumentParseResult.failure(new IllegalArgumentException("Invalid key " + getCurrentArgumentInfo(context) + ", no bingoWorlds found! (" + input + ")"));
            }

            inputQueue.poll();
            return ArgumentParseResult.success(worlds.get());
        }

        @Override
        public @NonNull List<@NonNull String> suggestions(@NonNull CommandContext<CommandSender> commandContext, @NonNull String input) {
            return JavaPlugin.getPlugin(AnotherBingoPlugin.class).getWorldManager().getAllWorlds()
                    .stream()
                    .map(w -> w.getOverworld().getKey().getKey().substring(6))
                    .filter(wk -> wk.startsWith(input))
                    .toList();
        }

        @Override
        public boolean isContextFree() {
            return true;
        }

    }

    private static @NotNull String getCurrentArgumentInfo(@NonNull CommandContext<@NonNull CommandSender> commandContext) {
        CommandArgument<@NonNull CommandSender, ?> currentArgument = commandContext.getCurrentArgument();
        if (currentArgument == null) return "unknown";
        return  "BingoWorlds '" + currentArgument.getName() + "'";
    }

}
