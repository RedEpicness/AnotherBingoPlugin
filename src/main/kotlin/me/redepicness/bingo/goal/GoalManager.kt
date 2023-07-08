package me.redepicness.bingo.goal

import me.redepicness.bingo.goal.internal.InternalItemGoals
import net.kyori.adventure.key.Key

object GoalManager {
    private val GOALS: MutableMap<Key, BingoGoal<*>> = mutableMapOf()
    private val GOALS_BY_DIFFICULTY: MutableMap<GoalDifficulty, MutableSet<BingoGoal<*>>> = mutableMapOf()
    private val GOALS_BY_TAG: MutableMap<GoalTag, MutableSet<BingoGoal<*>>> = mutableMapOf()

    internal fun setup() {
        InternalItemGoals.registerInternalGoals()
    }

    internal fun shutdown() {
    }

    val goals: Map<Key, BingoGoal<*>> = GOALS

    val goalsByDifficulty: Map<GoalDifficulty, Set<BingoGoal<*>>> = GOALS_BY_DIFFICULTY

    val goalsByTag: Map<GoalTag, Set<BingoGoal<*>>> = GOALS_BY_TAG

    fun getGoalsByDifficulty(difficulty: GoalDifficulty): Set<BingoGoal<*>> = getGoalsByDifficultyModifiable(difficulty)
    fun getGoalsByTag(tag: GoalTag): Set<BingoGoal<*>> = getGoalsByTagModifiable(tag)

    private fun getGoalsByDifficultyModifiable(difficulty: GoalDifficulty): MutableSet<BingoGoal<*>> = GOALS_BY_DIFFICULTY.computeIfAbsent(difficulty) { mutableSetOf() }

    private fun getGoalsByTagModifiable(tag: GoalTag): MutableSet<BingoGoal<*>> = GOALS_BY_TAG.computeIfAbsent(tag) { mutableSetOf() }

    fun registerGoal(goal: BingoGoal<*>) {
        goal.type.addGoal(goal)
        GOALS[goal.key] = goal
        getGoalsByDifficultyModifiable(goal.difficulty).add(goal)
        for (tag in goal.tags) {
            getGoalsByTagModifiable(tag).add(goal)
        }
    }
}
