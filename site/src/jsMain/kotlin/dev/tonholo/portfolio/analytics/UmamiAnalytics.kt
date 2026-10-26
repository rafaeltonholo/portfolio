package dev.tonholo.portfolio.analytics

import kotlinx.browser.window

/**
 * Lightweight, privacy-safe analytics tracker wrapping Umami's client tracking API.
 * Calls safely no-op if the script is not loaded (e.g. on localhost or when blocked by privacy extensions).
 */
object UmamiAnalytics {
    /**
     * Sends a custom event to Umami with optional key-value metadata.
     *
     * @param event name of the event to track.
     * @param data optional key-value properties associated with the event.
     */
    fun track(event: String, data: Map<String, String> = emptyMap()) {
        if (event.isBlank()) return
        val umami = window.asDynamic().umami ?: return
        if (data.isEmpty()) {
            umami.track(event)
            return
        }
        val payload = js("{}")
        data.forEach { (key: String, value: String) ->
            payload[key] = value
        }
        umami.track(event, payload)
    }

    /**
     * Tracks a resume PDF download or external view.
     *
     * @param language the active locale tag, e.g. "en" or "pt-BR".
     */
    fun trackResumeDownload(language: String) {
        track(event = "view_resume_pdf", data = mapOf("lang" to language))
    }

    /**
     * Tracks navigation to an external project link or demo.
     *
     * @param projectName display name of the project.
     * @param projectUrl destination URL.
     */
    fun trackProjectClick(projectName: String, projectUrl: String) {
        track(
            event = "view_project",
            data = mapOf("project_name" to projectName, "project_url" to projectUrl),
        )
    }

    /**
     * Tracks an article share action on external platforms.
     *
     * @param platform target platform, e.g. "twitter", "linkedin", "bluesky".
     * @param articleTitle title of the shared article.
     */
    fun trackArticleShare(platform: String, articleTitle: String) {
        track(
            event = "share_article",
            data = mapOf("platform" to platform, "article_title" to articleTitle),
        )
    }

    /**
     * Tracks copying an article URL to the clipboard.
     *
     * @param articleTitle title of the article.
     */
    fun trackArticleCopyLink(articleTitle: String) {
        track(event = "copy_article_link", data = mapOf("article_title" to articleTitle))
    }

    /**
     * Tracks an interface language switch.
     *
     * @param previousLanguage previous locale tag.
     * @param newLanguage selected locale tag.
     */
    fun trackLanguageChange(previousLanguage: String, newLanguage: String) {
        track(
            event = "language_change",
            data = mapOf("previous_lang" to previousLanguage, "lang" to newLanguage),
        )
    }

    /**
     * Tracks a theme mode toggle.
     *
     * @param theme chosen theme mode name (e.g. "DARK", "LIGHT").
     */
    fun trackThemeChange(theme: String) {
        track(event = "theme_change", data = mapOf("theme" to theme))
    }

    /**
     * Tracks clicks on social media links.
     *
     * @param platform name of the social platform, e.g. "github", "linkedin".
     * @param url destination profile URL.
     */
    fun trackSocialClick(platform: String, url: String) {
        track(
            event = "social_media_view",
            data = mapOf("platform" to platform, "url" to url),
        )
    }
}
