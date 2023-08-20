package me.redepicness.bingo.goal

import me.redepicness.bingo.card.CompletionListener
import org.bukkit.event.Listener

abstract class GoalListener(protected val listener: CompletionListener): Listener
