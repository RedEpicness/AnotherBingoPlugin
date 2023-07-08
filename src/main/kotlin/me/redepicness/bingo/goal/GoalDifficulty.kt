package me.redepicness.bingo.goal

enum class GoalDifficulty {
    FREE,
    EASY,
    MEDIUM,
    HARD,
    INSANE,
    ;
}

fun Iterable<GoalDifficulty>.hardest(): GoalDifficulty {
    return this.maxOrNull() ?: throw IllegalArgumentException("Cannot get the hardest difficulty of none!")
}

fun Array<GoalDifficulty>.hardest(): GoalDifficulty {
    return this.maxOrNull() ?: throw IllegalArgumentException("Cannot get the hardest difficulty of none!")
}

fun Iterable<GoalDifficulty>.easiest(): GoalDifficulty {
    return this.minOrNull() ?: throw IllegalArgumentException("Cannot get the easiest difficulty of none!")
}

fun Array<GoalDifficulty>.easiest(): GoalDifficulty {
    return this.minOrNull() ?: throw IllegalArgumentException("Cannot get the easiest difficulty of none!")
}
