package dev.eyrond.shulker

import dev.eyrond.paperkt.listener.KotlinListener
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class ShulkerInHandProcessor : KotlinListener {

    @EventHandler(ignoreCancelled = false)
    fun onShulkerRightClick(event: PlayerInteractEvent) {
        val isRightClick = event.action == Action.RIGHT_CLICK_AIR
        if (!isRightClick) return
        val item = event.item ?: return
        val isHoldingShulker = item.type.name.endsWith("SHULKER_BOX")
        if (!isHoldingShulker) return
        ShulkerInventory.open(event.player, item)
    }
}
