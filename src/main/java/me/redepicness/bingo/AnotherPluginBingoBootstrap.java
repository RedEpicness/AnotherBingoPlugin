package me.redepicness.bingo;

import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage")
public class AnotherPluginBingoBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(@NotNull PluginProviderContext pluginProviderContext) {
        pluginProviderContext.getLogger().info(Component.text("Bootstrapping ABP!"));
    }

    @Override
    public @NotNull JavaPlugin createPlugin(@NotNull PluginProviderContext context) {
        return new AnotherBingoPlugin(context);
    }

}
