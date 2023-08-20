package me.redepicness.bingo.card

import me.redepicness.bingo.goal.BingoGoal
import me.redepicness.bingo.goal.type.ItemGoal
import net.kyori.adventure.key.Key
import org.intellij.lang.annotations.Language
import org.intellij.lang.annotations.Pattern
import org.intellij.lang.annotations.Subst

class CardType<T : BingoGoal<*>> private constructor(@field:Subst("goal-type") @param:Pattern(GOAL_TYPE_PATTERN) val id: String) {
    private val goalsMutable: MutableMap<Key, T> = mutableMapOf()

    val goalsMap: Map<Key, T> = goalsMutable
    val goals: Collection<T> = goalsMutable.values

    init {
        TYPES.add(this)
    }

    fun makeKey(@Subst("id") id: String): Key {
        return Key.key(this.id, id)
    }

    internal fun addGoal(goal: BingoGoal<*>) {
        @Suppress("UNCHECKED_CAST")
        goalsMutable[goal.key] = goal as T
    }

    override fun toString(): String {
        return id
    }

    companion object {
        val TYPES = mutableListOf<CardType<*>>()
        @Language("RegExp")
        const val GOAL_TYPE_PATTERN = "[a-z0-9_\\-.]+"
        val ITEM = CardType<ItemGoal>("item")
    }
}
