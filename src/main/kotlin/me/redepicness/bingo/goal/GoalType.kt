package me.redepicness.bingo.goal

import me.redepicness.bingo.goal.type.ItemGoal
import net.kyori.adventure.key.Key
import org.intellij.lang.annotations.Language
import org.intellij.lang.annotations.Pattern
import org.intellij.lang.annotations.Subst

class GoalType<T : BingoGoal<*>> private constructor(@field:Subst("goal-type") @param:Pattern(GOAL_TYPE_PATTERN) val id: String) {
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GoalType<*>) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }


    companion object {
        val TYPES = mutableListOf<GoalType<*>>()
        @Language("RegExp")
        const val GOAL_TYPE_PATTERN = "[a-z0-9_\\-.]+"
        val ITEM = GoalType<ItemGoal>("item")
    }
}
