package dev.tonholo.portfolio.core.analytics.events

import dev.tonholo.portfolio.core.binding.firebase.EventParams
import dev.tonholo.portfolio.core.binding.firebase.eventsOf
import kotlinx.browser.document
import kotlinx.browser.window

sealed interface AnalyticEvent {
    val name: String
    fun toParams(): EventParams

    data class PageView(
        val language: String
    ) : AnalyticEvent {
        override val name = "page_view"

        override fun toParams(): EventParams = eventsOf(
            "page_title" to document.title,
            "page_location" to window.location.href,
            "lang" to language,
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

    data class SocialMediaView(
        val socialMedia: String,
    ) : AnalyticEvent {
        override val name = "social_media_view"

        override fun toParams(): EventParams = eventsOf(
            "social_media" to socialMedia,
        )
    }

    data class ViewResumePdf(
        val language: String,
    ) : AnalyticEvent {
        override val name = "view_resume_pdf"

        override fun toParams(): EventParams = eventsOf(
            "lang" to language,
        )
    }

    data class LanguageChange(
        val previousLanguage: String,
        val language: String,
    ) : AnalyticEvent {
        override val name = "language_change"

        override fun toParams(): EventParams = eventsOf(
            "previous_lang" to previousLanguage,
            "lang" to language,
        )
    }
}
