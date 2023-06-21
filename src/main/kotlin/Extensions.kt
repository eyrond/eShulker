package dev.eyrond.shulker

import org.bukkit.inventory.ItemStack

fun ItemStack.isShulker() = type.name.endsWith("SHULKER_BOX")
