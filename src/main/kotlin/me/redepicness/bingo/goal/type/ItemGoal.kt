package me.redepicness.bingo.goal.type

import me.redepicness.bingo.card.CompletionListener
import me.redepicness.bingo.goal.BingoGoal
import me.redepicness.bingo.goal.GoalListener
import me.redepicness.bingo.goal.GoalType
import me.redepicness.bingo.goal.info.ItemGoalInfo
import me.redepicness.bingo.team.getTeam
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class ItemGoal(id: String, info: ItemGoalInfo) : BingoGoal<ItemGoalInfo>(GoalType.ITEM, id, info){

    override fun getIcon(): ItemStack = ItemStack(info.material).apply {
        lore(listOf(Component.text(info.difficulty.name, NamedTextColor.WHITE)))
    }

    override fun getListener(listener: CompletionListener): GoalListener = ItemGoalListener(listener)

    override fun toString(): String {
        return "Item(${info.material.name}, ${info.difficulty})"
    }

    inner class ItemGoalListener(listener: CompletionListener): GoalListener(listener) {

        @EventHandler
        fun onPickup(event: EntityPickupItemEvent){
            if(event.item.itemStack.type != info.material) return
            val team = (event.entity as? Player)?.getTeam() ?: return
            listener.onComplete(team, this@ItemGoal)
        }

        @EventHandler
        fun onPickup(event: InventoryClickEvent){
            if(event.currentItem?.type != info.material && event.cursor?.type != info.material) return

            val team = (event.view.player as? Player)?.getTeam() ?: return
            listener.onComplete(team, this@ItemGoal)
        }

    }

}


