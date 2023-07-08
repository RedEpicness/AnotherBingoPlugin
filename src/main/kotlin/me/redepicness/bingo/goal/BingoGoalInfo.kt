package me.redepicness.bingo.goal

import me.redepicness.bingo.goal.internal.GoalDifficultyAndTagContainer

open class BingoGoalInfo(val difficulty: GoalDifficulty, val tags: Set<GoalTag>) {

    constructor(container: GoalDifficultyAndTagContainer) : this(container.difficulties.hardest(), container.tags)
}
