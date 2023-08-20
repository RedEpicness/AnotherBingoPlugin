package me.redepicness.bingo.util

import me.redepicness.bingo.plugin
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitTask

fun runTaskLater(delay: Long, block: () -> Unit) = Bukkit.getScheduler().runTaskLater(plugin, Runnable { block() }, delay)

fun runTask(block: () -> Unit) = Bukkit.getScheduler().runTask(plugin, Runnable { block() })

fun registerEvents(listener: Listener) = Bukkit.getPluginManager().registerEvents(listener, plugin)

fun runTaskTimerAsynchronously(delay: Long, period: Long, block: () -> Unit): BukkitTask =
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, Runnable { block() }, delay, period)

fun runTaskAsynchronously(block: () -> Unit): BukkitTask =
        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable { block() })

fun ItemStack.setName(name: Component): ItemStack { // ik it clones the meta every time, but we do it for the clean code :smug:
        itemMeta = itemMeta.apply { this.displayName(name) }
        return this
}
