package dev.eyrond.shulker

import dev.eyrond.paperkt.listener.KotlinListener
import dev.eyrond.paperkt.plugin.IKotlinPlugin
import net.kyori.adventure.sound.Sound
import net.minecraft.world.item.BlockItem
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_20_R1.block.CraftShulkerBox
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.bukkit.inventory.meta.BlockStateMeta
import org.bukkit.Sound as MinecraftSound

class ShulkerInventory private constructor(
    private val plugin: IKotlinPlugin,
    private val player: Player,
    private val itemStack: ItemStack
) : KotlinListener {


    private val inventoryHolder = InventoryHolder { error("Should not be called.") }
    private val meta = itemStack.itemMeta as BlockStateMeta
    private val blockState = meta.blockState as CraftShulkerBox
    private var inventory: Inventory = createInventory()
    private var isOpen = false

    fun open(): Boolean = synchronized(this) {
        if (isOpen) return false
        playOpenSoundForPlayer()
        register(plugin)
        player.openInventory(inventory)
        isOpen = true
        return true
    }

    @Suppress("unused")
    fun close(): Boolean = close(false)

    private fun close(isInventoryClosing: Boolean): Boolean = synchronized(this) {
        if (!isOpen) return false
        playCloseSoundForPlayer()
        if (!isInventoryClosing) player.closeInventory()
        updateItemStack()
        unregister()
        isOpen = false
        return true
    }

    private fun playCloseSoundForPlayer() {
        val closeSound = Sound.sound {
            it.type(MinecraftSound.BLOCK_SHULKER_BOX_CLOSE.key())
            it.source(Sound.Source.BLOCK)
        }
        player.playSound(closeSound)
    }

    private fun playOpenSoundForPlayer() {
        val openSound = Sound.sound {
            it.type(MinecraftSound.BLOCK_SHULKER_BOX_OPEN.key())
            it.source(Sound.Source.BLOCK)
        }
        player.playSound(openSound)
    }

    @EventHandler(ignoreCancelled = true)
    @Suppress("unused")
    fun onInventoryClose(event: InventoryCloseEvent) {
        if (event.inventory.holder !== inventoryHolder) return
        close(true)
    }

    @EventHandler(ignoreCancelled = true)
    @Suppress("unused")
    fun onInventoryClick(event: InventoryClickEvent) {
        val inventory = event.inventory
        if (inventory is PlayerInventory && inventory.holder == player) return
        if (event.currentItem == itemStack) {
            event.isCancelled = true
        }
    }

    private fun updateItemStack() {
        val tileEntity = blockState.tileEntity
        inventory.contents.forEachIndexed { index, itemStack ->
            val nms = (itemStack as CraftItemStack?)?.handle ?: CraftItemStack.asNMSCopy(ItemStack(Material.AIR))
            tileEntity.setItem(index, nms)
        }
        BlockItem.setBlockEntityData(
            (itemStack as CraftItemStack).handle,
            tileEntity.type,
            tileEntity.saveWithFullMetadata()
        )
    }

    private fun createInventory(): Inventory {
        val displayName = meta.displayName()
        val inventory = if (displayName != null) {
            Bukkit.createInventory(inventoryHolder, InventoryType.SHULKER_BOX, displayName)
        } else {
            Bukkit.createInventory(inventoryHolder, InventoryType.SHULKER_BOX)
        }
        blockState.snapshotInventory.forEachIndexed { index, itemStack ->
            inventory.setItem(index, itemStack)
        }
        return inventory
    }

    companion object {

        fun open(plugin: IKotlinPlugin, player: Player, shulker: ItemStack): ShulkerInventory {
            require(player.isOnline) { "Player must be online to open a shulker!" }
            require(shulker.isShulker()) { "Only shulkers can be opened with ShulkerInventory!" }
            return ShulkerInventory(plugin, player, shulker).also { it.open() }
        }
    }
}
