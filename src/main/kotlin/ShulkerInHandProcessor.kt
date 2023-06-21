package dev.eyrond.shulker

import dev.eyrond.paperkt.listener.KotlinListener
import dev.eyrond.paperkt.plugin.IKotlinPlugin
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class ShulkerInHandProcessor(private val plugin: IKotlinPlugin) : KotlinListener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    @Suppress("unused")
    fun openShulkerInHand(event: PlayerInteractEvent) {
        if (!event.isRightClickOnAir()) return
        val player = event.player
        val shulker = player.getShulkerInHand() ?: return
        if (player.hasInventoryOpen()) return
        ShulkerInventory.open(plugin, player, shulker)
    }

    private fun Player.hasInventoryOpen() = openInventory.type != InventoryType.CRAFTING

    private fun PlayerInteractEvent.isRightClickOnAir() = action == Action.RIGHT_CLICK_AIR

    private fun Player.getShulkerInHand(): ItemStack? {
        with(inventory.itemInMainHand) { if (isShulker()) return this }
        with(inventory.itemInOffHand) { if (isShulker()) return this }
        return null
    }
}
