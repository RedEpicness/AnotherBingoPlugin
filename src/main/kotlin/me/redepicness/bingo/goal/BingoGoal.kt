package me.redepicness.bingo.goal

import me.redepicness.bingo.card.CompletionListener
import org.bukkit.inventory.ItemStack

abstract class BingoGoal<I : BingoGoalInfo>(val type: GoalType<*>, id: String, val info: I) {

    val key = type.makeKey(id)
    val difficulty = info.difficulty
    val tags = info.tags

    init {
        check(!type.goalsMap.containsKey(key)) { "Cannot make another goal with the same key $key!" }
    }

    abstract fun getIcon(): ItemStack

    abstract fun getListener(listener: CompletionListener): GoalListener

    fun hasTag(tag: GoalTag) = tags.contains(tag)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BingoGoal<*>) return false

        if (key != other.key) return false

        return true
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }

    abstract override fun toString(): String

}
