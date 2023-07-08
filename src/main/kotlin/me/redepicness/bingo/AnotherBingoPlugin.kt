@file:Suppress("UnstableApiUsage")

package me.redepicness.bingo

import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.execution.postprocessor.CommandPostprocessingContext
import cloud.commandframework.extra.confirmation.CommandConfirmationManager
import cloud.commandframework.paper.PaperCommandManager
import io.papermc.paper.plugin.bootstrap.PluginProviderContext
import me.redepicness.bingo.command.GoalCommands
import me.redepicness.bingo.command.WorldCommands
import me.redepicness.bingo.goal.GoalManager
import me.redepicness.bingo.world.WorldManager
import me.redepicness.bingo.world.generation.EmptyChunkGenerator
import me.redepicness.bingo.world.generation.VoidBiomeProvider
import net.kyori.adventure.text.Component
import org.bukkit.command.CommandSender
import org.bukkit.generator.BiomeProvider
import org.bukkit.generator.ChunkGenerator
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.TimeUnit
import java.util.function.Function

lateinit var plugin: AnotherBingoPlugin

class AnotherBingoPlugin(context: PluginProviderContext) : JavaPlugin() {

    init {
        plugin = this
        context.logger.info(Component.text("Creating ABP instance!"))
    }

    override fun onLoad() {}
    override fun onEnable() {
        try {
            val commandManager = PaperCommandManager(
                    this,
                    CommandExecutionCoordinator.simpleCoordinator(),
                    Function.identity(),
                    Function.identity()
            )
            commandManager.registerBrigadier()
            commandManager.registerAsynchronousCompletions()
            val confirmationManager = CommandConfirmationManager(
                    30L,
                    TimeUnit.SECONDS,
                    { context: CommandPostprocessingContext<CommandSender> -> context.commandContext.sender.sendMessage(Component.text("Do /confirm to confirm!")) }
            ) { sender: CommandSender -> sender.sendMessage(Component.text("No commands to confirm...")) }
            confirmationManager.registerConfirmationProcessor(commandManager)
            commandManager.command(commandManager.commandBuilder("confirm").handler(confirmationManager.createConfirmationExecutionHandler()))
            WorldCommands.registerCommands(commandManager)
            GoalCommands.registerCommands(commandManager)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        WorldManager.setup()
        GoalManager.setup()
    }

    override fun onDisable() {
        GoalManager.shutdown()
        WorldManager.shutdown()
    }

    override fun getDefaultWorldGenerator(worldName: String, id: String?): ChunkGenerator {
        return EmptyChunkGenerator(20)
    }

    override fun getDefaultBiomeProvider(worldName: String, id: String?): BiomeProvider {
        return VoidBiomeProvider
    }
}
