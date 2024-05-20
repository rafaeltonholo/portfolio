package dev.tonholo.portfolio.core.analytics.events

import dev.tonholo.portfolio.core.binding.firebase.EventParams
import dev.tonholo.portfolio.core.binding.firebase.eventsOf
import kotlinx.browser.document
import kotlinx.browser.window

sealed interface AnalyticEvent {
    val name: String
    fun toParams(): EventParams

    data object PageView : AnalyticEvent {
        override val name = "page_view"

        override fun toParams(): EventParams = eventsOf(
            "page_title" to document.title,
            "page_location" to window.location.href,
        )
    }

    data class ScreenView(
        val screenName: String,
        val screenClass: String = window.location.href,
    ) : AnalyticEvent {
        override val name = "screen_view"

        override fun toParams(): EventParams = eventsOf(
            "screen_name" to screenName,
            "screen_class" to screenClass,
        )
    }
}
