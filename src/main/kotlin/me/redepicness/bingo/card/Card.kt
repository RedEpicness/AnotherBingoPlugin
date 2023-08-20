package me.redepicness.bingo.card

import me.redepicness.bingo.game.Game
import me.redepicness.bingo.goal.BingoGoal
import me.redepicness.bingo.goal.GoalManager
import me.redepicness.bingo.plugin
import me.redepicness.bingo.team.Team
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.inventory.Inventory

class Card(
    val game: Game,
    val gridSize: Int,
    val difficulties: CardDifficulties
) {

    private val totalGridSize = gridSize * gridSize
    val grid: Array<Array<CardGoal>>  // todo dont have publicly modifiable...
    val listeners: List<Listener>

    init {
        if (difficulties.totalCount != totalGridSize) {
            throw RuntimeException("Invalid number of difficulties! Got ${difficulties.totalCount}, expecting $totalGridSize")
        }

        val difficultyList = difficulties.getShuffledDifficulties()

        grid = Array(gridSize){ x ->
            Array(gridSize){ y ->
                CardGoal(game, GoalManager.getGoalsByDifficulty(difficultyList[x * gridSize + y]).random())
                // todo check for duplicated goals...
            }
        }
        listeners = grid.flatten().map { it.getListener() }
    }

    fun createInventory(): Inventory{
        return Bukkit.createInventory(null, 9*5, Component.text("Bingo Card!!!!!!!!!!")).apply {
            for ((i, goals) in grid.withIndex()) {
                for ((j, goal) in goals.withIndex()) {
                    setItem(i*9 + j, goal.goal.getIcon())
                }
            }
        }
    }

    fun registerListeners(){
        for (listener in listeners) {
            Bukkit.getPluginManager().registerEvents(listener, plugin)
        }
    }

    fun unregisterListeners(){
        for (listener in listeners) {
            HandlerList.unregisterAll(listener)
        }
    }

}

data class CardGoal(val game: Game, val goal: BingoGoal<*>, val teamsCompleted: MutableSet<Team> = mutableSetOf()): CompletionListener {

    override fun onComplete(team: Team, goal: BingoGoal<*>) {
        if(this.goal == goal && !teamsCompleted.contains(team)) {
            teamsCompleted.add(team)
            Bukkit.broadcast(team.displayName().append(Component.text(" completed $goal")))
        }
    }

    fun getListener() = goal.getListener(this)

}

interface CompletionListener {

    fun onComplete(team: Team, goal: BingoGoal<*>)

}
