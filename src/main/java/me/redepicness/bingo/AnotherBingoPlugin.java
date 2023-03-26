package me.redepicness.bingo;

import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.extra.confirmation.CommandConfirmationManager;
import cloud.commandframework.paper.PaperCommandManager;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import me.redepicness.bingo.command.WorldCommands;
import me.redepicness.bingo.world.EmptyChunkGenerator;
import me.redepicness.bingo.world.WorldManager;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static me.redepicness.bingo.world.VoidBiomeProvider.VOID_BIOME_PROVIDER;

public class AnotherBingoPlugin extends JavaPlugin {

    private final @NotNull WorldManager worldManager = new WorldManager(this);

    @SuppressWarnings("UnstableApiUsage")
    public AnotherBingoPlugin(@NotNull PluginProviderContext context) {
        context.getLogger().info(Component.text("Creating ABP instance!"));
    }

    public @NotNull WorldManager getWorldManager() {
        return worldManager;
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        try{
            PaperCommandManager<CommandSender> commandManager = new PaperCommandManager<>(
                    this,
                    CommandExecutionCoordinator.simpleCoordinator(),
                    Function.identity(),
                    Function.identity()
            );
            commandManager.registerBrigadier();
            commandManager.registerAsynchronousCompletions();

            CommandConfirmationManager<CommandSender> confirmationManager = new CommandConfirmationManager<>(
                    30L,
                    TimeUnit.SECONDS,
                    context -> context.getCommandContext().getSender().sendMessage(Component.text("Do /confirm to confirm!")),
                    sender -> sender.sendMessage(Component.text("No commands to confirm..."))
            );

            confirmationManager.registerConfirmationProcessor(commandManager);

            commandManager.command(commandManager.commandBuilder("confirm").handler(confirmationManager.createConfirmationExecutionHandler()));

            WorldCommands.registerCommands(commandManager);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        worldManager.setup();
    }

    @Override
    public void onDisable() {
        worldManager.shutdown();
    }

    @Override
    public @Nullable ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
        return new EmptyChunkGenerator(20);
    }

    @Override
    public @Nullable BiomeProvider getDefaultBiomeProvider(@NotNull String worldName, @Nullable String id) {
        return VOID_BIOME_PROVIDER;
    }

}
