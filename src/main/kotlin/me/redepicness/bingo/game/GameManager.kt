package me.redepicness.bingo.game

import me.redepicness.bingo.world.BingoWorlds

object GameManager {

    var game: Game? = null // todo multi game?

    internal fun setup() {
    }

    internal fun shutdown() {
    }

    fun createGame(worlds: BingoWorlds): Game {
        if (game != null){
            throw RuntimeException("Game already running")
        }
        val game = Game(0, worlds)
        this.game = game
        return game;
    }

}

enum class GameStage {

    WAITING,
    STARTING,
    RUNNING,
    FINISHED

}
