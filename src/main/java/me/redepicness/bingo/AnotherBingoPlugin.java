package me.redepicness.bingo;

import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class AnotherBingoPlugin extends JavaPlugin {

    @SuppressWarnings("UnstableApiUsage")
    public AnotherBingoPlugin(@NotNull PluginProviderContext context) {
        context.getLogger().info(Component.text("Creating ABP instance!"));
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

}
