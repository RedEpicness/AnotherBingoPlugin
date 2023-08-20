package me.redepicness.bingo.card

import me.redepicness.bingo.game.Game

object CardManager {

    internal fun setup() {
    }

    internal fun shutdown() {
    }

    fun createCard(game: Game,  gridSize: Int, difficulties: CardDifficulties): Card {
        return Card(game, gridSize, difficulties)
    }

}
