package me.redepicness.bingo.goal.type

import me.redepicness.bingo.goal.BingoGoal
import me.redepicness.bingo.goal.GoalType
import me.redepicness.bingo.goal.info.ItemGoalInfo

class ItemGoal(id: String, info: ItemGoalInfo) : BingoGoal<ItemGoalInfo>(GoalType.ITEM, id, info)
