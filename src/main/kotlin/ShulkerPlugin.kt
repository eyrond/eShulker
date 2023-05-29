package dev.eyrond.shulker

import dev.eyrond.paperkt.plugin.KotlinPlugin
import org.bstats.bukkit.Metrics

class ShulkerPlugin : KotlinPlugin() {

    private lateinit var metrics: Metrics
    private val shulkerInHandProcessor = ShulkerInHandProcessor()

    override suspend fun loadConfig() {
        // No configuration needed.
    }

    override suspend fun onEnabled() {
        _instance = this
        metrics = Metrics(this, 18619)
        shulkerInHandProcessor.register(this)
    }

    override suspend fun onDisabled() {
        shulkerInHandProcessor.unregister()
        metrics.shutdown()
        _instance = null
    }

    companion object {

        private var _instance: ShulkerPlugin? = null
        fun get(): ShulkerPlugin = _instance!!
    }
}
