@file:Suppress("UnstableApiUsage")

package me.redepicness.bingo

import io.papermc.paper.plugin.loader.PluginClasspathBuilder
import io.papermc.paper.plugin.loader.PluginLoader

class AnotherBingoPluginLoader : PluginLoader {
    override fun classloader(classpathBuilder: PluginClasspathBuilder) {}
}
