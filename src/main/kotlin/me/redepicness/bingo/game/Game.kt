package me.redepicness.bingo.game

import me.redepicness.bingo.card.Card
import me.redepicness.bingo.card.CardDifficulties
import me.redepicness.bingo.card.CardManager
import me.redepicness.bingo.goal.GoalDifficulty
import me.redepicness.bingo.team.TeamManager
import me.redepicness.bingo.world.BingoWorlds
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import java.util.concurrent.CompletableFuture

class Game(val id: Int, val worlds: BingoWorlds) {

    var stage: GameStage = GameStage.WAITING

    var gridSize: Int = 5
        set(value) {
            field = value
            generateCard()
        }
    var difficulties: CardDifficulties = CardDifficulties().apply { set(GoalDifficulty.FREE, 25) }
        set(value) {
            field = value
            generateCard()
        }

    lateinit var card: Card
        private set


    init {
        generateCard()
    }

    fun startGame() {
        if(stage > GameStage.WAITING) return
        stage = GameStage.STARTING
        card.registerListeners()
        Bukkit.broadcast(Component.text("Game starting!"))
        Bukkit.broadcast(Component.text("Stopping active chunk generation...", NamedTextColor.DARK_GRAY))
        worlds.stopLoadingChunks().whenComplete { _, _ ->
            Bukkit.broadcast(Component.text("Locating spawns...", NamedTextColor.DARK_GRAY))
            val activeTeams = TeamManager.activeTeams
            worlds.findSpawns(activeTeams.size).thenAccept{
                Bukkit.broadcast(Component.text("Teleporting players...", NamedTextColor.DARK_GRAY))
                val locs = it.toMutableList()
                val futures = mutableSetOf<CompletableFuture<Boolean>?>()  // todo clean up
                for (team in activeTeams) {
                    val loc = locs.removeFirst()
                    for (member in team.members) {
                        futures.add(Bukkit.getPlayer(member)?.teleportAsync(loc))
                        // todo set spawn and respawning etc
                    }
                }
                CompletableFuture.allOf(*futures.filterNotNull().toTypedArray()).whenComplete{ _, _ ->
                    stage = GameStage.RUNNING
                    Bukkit.broadcast(Component.text("Game running!"))
                }
            }
        }

    }

    private fun generateCard(){
        if(this::card.isInitialized){
            card.unregisterListeners()
        }
        card = CardManager.createCard(this, gridSize, difficulties)
        if(stage > GameStage.WAITING){
            card.registerListeners()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Game) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }


}
