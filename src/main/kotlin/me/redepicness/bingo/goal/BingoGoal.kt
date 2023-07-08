package me.redepicness.bingo.goal

open class BingoGoal<I : BingoGoalInfo>(val type: GoalType<*>, id: String, val info: I) {

    val key = type.makeKey(id)
    val difficulty = info.difficulty
    val tags = info.tags

    init {
        check(!type.goalsMap.containsKey(key)) { "Cannot make another goal with the same key $key!" }
    }

    fun hasTag(tag: GoalTag) = tags.contains(tag)

}
