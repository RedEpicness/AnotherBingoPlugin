package me.redepicness.bingo.card

import me.redepicness.bingo.goal.GoalDifficulty

class CardDifficulties {

    private val difficulties: MutableMap<GoalDifficulty, Int> = mutableMapOf()

    val totalCount: Int
        get() { return difficulties.values.sum() }

    fun set(difficulty: GoalDifficulty, count: Int){
        difficulties[difficulty] = count
    }

    fun get(difficulty: GoalDifficulty) = difficulties.getOrDefault(difficulty, 0)

    fun getShuffledDifficulties(): List<GoalDifficulty> = difficulties.map { e -> MutableList(e.value) { e.key } }.flatten().shuffled()

}
