package dev.eyrond.shulker

import dev.eyrond.paperkt.listener.KotlinListener
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class ShulkerInHandProcessor : KotlinListener {

    private val ItemStack.isShulker: Boolean
        get() = type.name.endsWith("SHULKER_BOX")

    @EventHandler(ignoreCancelled = false)
    fun onShulkerRightClick(event: PlayerInteractEvent) {
        val isRightClick = event.action == Action.RIGHT_CLICK_AIR
        if (!isRightClick) return
        val player = event.player
        val inventory = player.inventory
        val shulker = if (inventory.itemInMainHand.isShulker) {
            inventory.itemInMainHand
        } else if (inventory.itemInOffHand.isShulker) {
            inventory.itemInOffHand
        } else return
        val hasInventoryOpen = player.openInventory.type != InventoryType.CRAFTING
        if (hasInventoryOpen) return
        ShulkerInventory.open(event.player, shulker)
    }
}
