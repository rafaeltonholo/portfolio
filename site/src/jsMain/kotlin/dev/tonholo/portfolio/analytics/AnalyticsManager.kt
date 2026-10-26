package dev.tonholo.portfolio.analytics

import androidx.compose.runtime.staticCompositionLocalOf
import dev.tonholo.portfolio.analytics.events.AnalyticEvent
import kotlinx.browser.window

/**
 * Manages tracking of [AnalyticEvent] instances using privacy-safe Umami analytics.
 */
class AnalyticsManager private constructor() {
    /**
     * Initializes analytics script injection via [UmamiInjector].
     */
    fun initialize() {
        UmamiInjector.inject()
    }

    /**
     * Sends an [AnalyticEvent] to Umami if available in the current window context.
     *
     * @param event the event to dispatch.
     */
    fun track(event: AnalyticEvent) {
        val umami = window.asDynamic().umami ?: return
        val params: Map<String, String> = event.toParams()
        if (params.isEmpty()) {
            umami.track(event.name)
            return
        }
        val payload = js("{}")
        params.forEach { (key: String, value: String) ->
            payload[key] = value
        }
        umami.track(event.name, payload)
    }

    companion object {
        private lateinit var instance: AnalyticsManager

        /**
         * Returns the singleton [AnalyticsManager], initializing it on first call.
         */
        operator fun invoke(): AnalyticsManager {
            if (!::instance.isInitialized) {
                instance = AnalyticsManager()
                instance.initialize()
            }
            return instance
        }
    }
}

/**
 * Ambient [AnalyticsManager] provided at the application root.
 */
val LocalAnalyticsManager = staticCompositionLocalOf<AnalyticsManager> {
    error("No AnalyticsManager provided")
}
