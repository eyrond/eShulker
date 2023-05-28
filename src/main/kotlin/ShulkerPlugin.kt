package dev.eyrond.shulker

import dev.eyrond.paperkt.plugin.KotlinPlugin

class ShulkerPlugin : KotlinPlugin() {

    private val shulkerInHandProcessor = ShulkerInHandProcessor()

    override suspend fun loadConfig() {
        // No configuration needed.
    }

    override suspend fun onEnabled() {
        _instance = this
        shulkerInHandProcessor.register(this)
    }

    override suspend fun onDisabled() {
        shulkerInHandProcessor.unregister()
        _instance = null
    }

    companion object {

        private var _instance: ShulkerPlugin? = null
        fun get(): ShulkerPlugin = _instance!!
    }
}
