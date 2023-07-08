package me.redepicness.bingo.util

import me.redepicness.bingo.plugin
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.scheduler.BukkitTask

fun runTaskLater(delay: Long, block: () -> Unit) = Bukkit.getScheduler().runTaskLater(plugin, Runnable { block() }, delay)

fun runTask(block: () -> Unit) = Bukkit.getScheduler().runTask(plugin, Runnable { block() })

fun registerEvents(listener: Listener) = Bukkit.getPluginManager().registerEvents(listener, plugin)

fun runTaskTimerAsynchronously(delay: Long, period: Long, block: () -> Unit): BukkitTask =
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, Runnable { block() }, delay, period)
