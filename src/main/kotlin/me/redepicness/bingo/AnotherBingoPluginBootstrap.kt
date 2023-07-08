@file:Suppress("UnstableApiUsage")

package me.redepicness.bingo

import io.papermc.paper.plugin.bootstrap.BootstrapContext
import io.papermc.paper.plugin.bootstrap.PluginBootstrap
import io.papermc.paper.plugin.bootstrap.PluginProviderContext
import net.kyori.adventure.text.Component
import org.bukkit.plugin.java.JavaPlugin

class AnotherBingoPluginBootstrap : PluginBootstrap {
    override fun bootstrap(bootstrapContext: BootstrapContext) {
        bootstrapContext.logger.info(Component.text("Bootstrapping ABP!"))
    }

    override fun createPlugin(context: PluginProviderContext): JavaPlugin {
        return AnotherBingoPlugin(context)
    }
}
