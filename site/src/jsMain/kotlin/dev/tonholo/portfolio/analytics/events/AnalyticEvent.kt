package dev.tonholo.portfolio.analytics.events

import kotlinx.browser.document
import kotlinx.browser.window

/**
 * Sealed interface representing an analytics event tracked across the site.
 */
sealed interface AnalyticEvent {
    val name: String
    fun toParams(): Map<String, String>

    /**
     * Page view event tracked on screen or route transitions.
     *
     * @param language the active language/locale tag.
     */
    data class PageView(val language: String) : AnalyticEvent {
        override val name: String = "page_view"
        override fun toParams(): Map<String, String> = mapOf(
            "page_title" to document.title,
            "page_location" to window.location.href,
            "lang" to language,
        )
    }

    /**
     * Screen view event.
     *
     * @param screenName name of the screen.
     * @param screenClass class or URL of the screen.
     */
    data class ScreenView(val screenName: String, val screenClass: String = "") : AnalyticEvent {
        override val name: String = "screen_view"
        override fun toParams(): Map<String, String> = mapOf(
            "screen_name" to screenName,
            "screen_class" to screenClass.ifBlank { window.location.href },
        )
    }

    /**
     * Tracked when a social media profile link is clicked.
     *
     * @param socialMedia name or label of the social media.
     * @param url destination URL.
     */
    data class SocialMediaView(val socialMedia: String, val url: String = "") : AnalyticEvent {
        override val name: String = "social_media_view"
        override fun toParams(): Map<String, String> = buildMap {
            put("social_media", socialMedia)
            if (url.isNotBlank()) put("url", url)
        }
    }

    /**
     * Tracked when a user clicks to view or download the resume PDF.
     *
     * @param language active language tag.
     */
    data class ViewResumePdf(val language: String) : AnalyticEvent {
        override val name: String = "view_resume_pdf"
        override fun toParams(): Map<String, String> = mapOf(
            "lang" to language,
        )
    }

    /**
     * Tracked when a user changes the interface language.
     *
     * @param previousLanguage the previous language tag.
     * @param language the new language tag.
     */
    data class LanguageChange(val previousLanguage: String, val language: String) : AnalyticEvent {
        override val name: String = "language_change"
        override fun toParams(): Map<String, String> = mapOf(
            "previous_lang" to previousLanguage,
            "lang" to language,
        )
    }

    /**
     * Tracked when a user clicks a project case study or external link.
     *
     * @param projectName name of the project.
     * @param projectUrl URL of the project.
     */
    data class ViewProject(val projectName: String, val projectUrl: String) : AnalyticEvent {
        override val name: String = "view_project"
        override fun toParams(): Map<String, String> = mapOf(
            "project_name" to projectName,
            "project_url" to projectUrl,
        )
    }

    /**
     * Tracked when a user opens or reads an article.
     *
     * @param articleTitle title of the article.
     * @param articleUrl URL of the article.
     */
    data class ReadArticle(val articleTitle: String, val articleUrl: String) : AnalyticEvent {
        override val name: String = "read_article"
        override fun toParams(): Map<String, String> = mapOf(
            "article_title" to articleTitle,
            "article_url" to articleUrl,
        )
    }

    /**
     * Tracked when a user shares an article to an external platform.
     *
     * @param platform destination platform (e.g. twitter, linkedin, bluesky).
     * @param articleTitle title of the shared article.
     */
    data class ShareArticle(val platform: String, val articleTitle: String) : AnalyticEvent {
        override val name: String = "share_article"
        override fun toParams(): Map<String, String> = mapOf(
            "platform" to platform,
            "article_title" to articleTitle,
        )
    }

    /**
     * Tracked when a user copies an article link to the clipboard.
     *
     * @param articleTitle title of the article whose link was copied.
     */
    data class CopyArticleLink(val articleTitle: String) : AnalyticEvent {
        override val name: String = "copy_article_link"
        override fun toParams(): Map<String, String> = mapOf(
            "article_title" to articleTitle,
        )
    }

    /**
     * Tracked when the user toggles the theme mode.
     *
     * @param theme chosen theme mode name (e.g. "DARK", "LIGHT").
     */
    data class ThemeChange(val theme: String) : AnalyticEvent {
        override val name: String = "theme_change"
        override fun toParams(): Map<String, String> = mapOf(
            "theme" to theme,
        )
    }
}
