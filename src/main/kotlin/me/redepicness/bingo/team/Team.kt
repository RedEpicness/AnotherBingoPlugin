package me.redepicness.bingo.team

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Team
import java.util.*

class Team(
    val id: String,
    name: String,
    color: TextColor,
) {

    private val scoreboardTeam: Team = TeamManager.scoreboard.registerNewTeam(id)
    val members: MutableSet<UUID> = mutableSetOf() // todo make less open?

    var name = name
        set(name) {
            field = name
            updateScoreboardTeam()
        }
    var color = color
        set(color) {
            field = color
            updateScoreboardTeam()
        }

    init {
        updateScoreboardTeam()
    }

    fun isMember(uuid: UUID) = members.contains(uuid)

    fun isMember(player: Player) = isMember(player.uniqueId)

    fun addMember(player: Player){
        addMember(player.uniqueId, player)
    }

    fun addMember(uuid: UUID, player: Player? = null){
        TeamManager.getTeam(uuid)?.removeMember(uuid, player)
        members.add(uuid)
        scoreboardTeam.addPlayer(player ?: Bukkit.getOfflinePlayer(uuid))
        player?.gameMode = GameMode.SURVIVAL
    }

    fun removeMember(player: Player){
        removeMember(player.uniqueId, player)
    }

    fun removeMember(uuid: UUID, player: Player? = null){
        members.remove(uuid)
        scoreboardTeam.removePlayer(player ?: Bukkit.getOfflinePlayer(uuid))
        player?.gameMode = GameMode.SPECTATOR
    }

    fun displayName() = scoreboardTeam.displayName()

    private fun updateScoreboardTeam(){
        scoreboardTeam.displayName(Component.text(name, color))
        scoreboardTeam.prefix(Component.text(name, color).append(Component.text(" ")))
        scoreboardTeam.color(NamedTextColor.nearestTo(color))
        scoreboardTeam.setAllowFriendlyFire(false) // todo config
        scoreboardTeam.setCanSeeFriendlyInvisibles(true)
        scoreboardTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER)
        scoreboardTeam.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.ALWAYS)
        scoreboardTeam.setOption(Team.Option.DEATH_MESSAGE_VISIBILITY, Team.OptionStatus.ALWAYS)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is me.redepicness.bingo.team.Team) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }


}
