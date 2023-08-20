package me.redepicness.bingo.team

import me.redepicness.bingo.plugin
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.scoreboard.Scoreboard
import java.util.*

object TeamManager {

    internal lateinit var scoreboard: Scoreboard

    internal val teams = mutableMapOf<String, Team>() // todo tighten up access?
    internal val activeTeams: List<Team>
        get() {
            return teams.values.filter { it.members.size > 0 }
        }

    internal fun setup() {
        scoreboard = Bukkit.getScoreboardManager().newScoreboard
        for (entry in NamedTextColor.NAMES.valueToKey()) {
            createTeam(entry.value, entry.value.replace('_', ' ').replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }, entry.key)
        }

        //todo make not temp
        Bukkit.getPluginManager().registerEvents(object: Listener {
            @EventHandler
            fun onJoin(event: PlayerJoinEvent){
                val player = event.player
                player.scoreboard = scoreboard
                if(player.getTeam() == null) {
                    player.gameMode = GameMode.SPECTATOR
                }
                else {
                    player.gameMode = GameMode.SURVIVAL
                }
            }
        }, plugin)
    }

    internal fun shutdown() {

    }

    internal fun createTeam(id: String, name: String, color: TextColor): Team {
        if(teams.containsKey(id)){
            throw RuntimeException("Team with that id already exists!")
        }
        val team = Team(id, name, color)
        teams[id] = team
        return team
    }

    internal fun getTeam(uuid: UUID) = teams.values.find { it.isMember(uuid) }

    // no removing teams for now, lmao

}

fun Player.getTeam() = TeamManager.teams.values.find { it.isMember(this) }
