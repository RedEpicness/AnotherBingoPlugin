package me.redepicness.bingo.goal.internal

import me.redepicness.bingo.goal.GoalDifficulty
import me.redepicness.bingo.goal.GoalTag
import me.redepicness.bingo.goal.hardest
import org.jetbrains.annotations.Contract

class GoalDifficultyAndTagContainer private constructor() {

    private val difficultiesMutable: MutableSet<GoalDifficulty> = mutableSetOf()
    private val tagsMutable: MutableSet<GoalTag> = mutableSetOf()

    val tags: Set<GoalTag> = tagsMutable
    val difficulties: Set<GoalDifficulty> = difficultiesMutable

    constructor(difficulty: GoalDifficulty, vararg tags: GoalTag): this() {
        difficultiesMutable.add(difficulty)
        this.tagsMutable.addAll(tags)
    }

    constructor(vararg tags: GoalTag): this()  {
        this.tagsMutable.addAll(tags)
    }

    private constructor(difficulties: Set<GoalDifficulty>, tags: Set<GoalTag>): this()  {
        this.difficultiesMutable.addAll(difficulties)
        this.tagsMutable.addAll(tags)
    }

    @Contract(value = "_ -> new", pure = true)
    fun addDifficulties(vararg difficulties: GoalDifficulty): GoalDifficultyAndTagContainer {
        val container = GoalDifficultyAndTagContainer(this.difficultiesMutable, tagsMutable)
        container.difficultiesMutable.addAll(difficulties)
        return container
    }

    @Contract(value = "_ -> new", pure = true)
    fun addDifficulties(difficulties: Collection<GoalDifficulty>): GoalDifficultyAndTagContainer {
        val container = GoalDifficultyAndTagContainer(this.difficultiesMutable, tagsMutable)
        container.difficultiesMutable.addAll(difficulties)
        return container
    }

    @Contract(value = "_ -> new", pure = true)
    fun addTags(vararg tags: GoalTag): GoalDifficultyAndTagContainer {
        val container = GoalDifficultyAndTagContainer(difficultiesMutable, this.tagsMutable)
        container.tagsMutable.addAll(tags)
        return container
    }

    @Contract(value = "_ -> new", pure = true)
    fun addTags(tags: Collection<GoalTag>): GoalDifficultyAndTagContainer {
        val container = GoalDifficultyAndTagContainer(difficultiesMutable, this.tagsMutable)
        container.tagsMutable.addAll(tags)
        return container
    }

    @Contract(value = "_ -> new", pure = true)
    fun combine(other: GoalDifficultyAndTagContainer): GoalDifficultyAndTagContainer {
        val container = GoalDifficultyAndTagContainer()
        container.difficultiesMutable.addAll(difficultiesMutable)
        container.difficultiesMutable.addAll(other.difficultiesMutable)
        container.tagsMutable.addAll(tagsMutable)
        container.tagsMutable.addAll(other.tagsMutable)
        return container
    }

    @Contract(value = "_ -> new", pure = true)
    fun combineKeepingHardestDifficulty(other: GoalDifficultyAndTagContainer): GoalDifficultyAndTagContainer {
        val container = combine(other)
        val hardest: GoalDifficulty = container.difficultiesMutable.hardest()
        container.difficultiesMutable.clear()
        container.difficultiesMutable.add(hardest)
        return container
    }

    @Contract(value = "_ -> new", pure = true)
    fun combineKeepingEasiestDifficulty(other: GoalDifficultyAndTagContainer): GoalDifficultyAndTagContainer {
        val container = combine(other)
        val hardest: GoalDifficulty = container.difficultiesMutable.hardest()
        container.difficultiesMutable.clear()
        container.difficultiesMutable.add(hardest)
        return container
    }

    companion object {
        /*var NETHER_CONTAINER: GoalDifficultyAndTagContainer = GoalDifficultyAndTagContainer(GoalDifficulty.MEDIUM)
        var END_CONTAINER: GoalDifficultyAndTagContainer = GoalDifficultyAndTagContainer(GoalDifficulty.INSANE)*/

        val EMPTY = GoalDifficultyAndTagContainer()

        fun container(difficulty: GoalDifficulty, vararg tags: GoalTag): GoalDifficultyAndTagContainer {
            return GoalDifficultyAndTagContainer(difficulty, *tags)
        }
    }
}
