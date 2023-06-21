package dev.eyrond.shulker

import dev.eyrond.paperkt.plugin.KotlinPlugin
import org.bstats.bukkit.Metrics

@Suppress("unused")
class ShulkerPlugin : KotlinPlugin() {

    private lateinit var metrics: Metrics
    private val shulkerInHandProcessor = ShulkerInHandProcessor(this)

    override suspend fun loadConfig() {
        // No configuration needed.
    }

    override suspend fun onEnabled() {
        metrics = Metrics(this, 18619)
        shulkerInHandProcessor.register(this)
    }

    override suspend fun onDisabled() {
        shulkerInHandProcessor.unregister()
        metrics.shutdown()
    }
}
