package me.redepicness.bingo.goal.info

import me.redepicness.bingo.goal.BingoGoalInfo
import me.redepicness.bingo.goal.GoalDifficulty
import me.redepicness.bingo.goal.GoalTag
import me.redepicness.bingo.goal.internal.GoalDifficultyAndTagContainer
import org.bukkit.Material

class ItemGoalInfo : BingoGoalInfo {

    val material: Material

    constructor(material: Material, container: GoalDifficultyAndTagContainer) : super(container) {
        this.material = material
    }

    constructor(material: Material, difficulty: GoalDifficulty, tags: Set<GoalTag>) : super(difficulty, tags) {
        this.material = material
    }

}
