package dev.tonholo.portfolio.analytics

import kotlin.test.Test
import kotlin.test.assertTrue

class UmamiAnalyticsTest {
    @Test
    fun `given blank event, when track is called, then it returns safely without error`() {
        // Arrange
        val event = ""
        // Act & Assert
        UmamiAnalytics.track(event = event)
        assertTrue(true)
    }

    @Test
    fun `given custom event without umami in window, when track is called, then it does not throw`() {
        // Arrange
        val event = "custom_event"
        val data = mapOf("key" to "value")
        // Act & Assert
        UmamiAnalytics.track(event = event, data = data)
        assertTrue(true)
    }

    @Test
    fun `given resume download helper, when invoked, then it completes safely`() {
        // Arrange
        val language = "en"
        // Act & Assert
        UmamiAnalytics.trackResumeDownload(language = language)
        assertTrue(true)
    }

    @Test
    fun `given project click helper, when invoked, then it completes safely`() {
        // Arrange
        val projectName = "thunderbird"
        val projectUrl = "https://github.com/thunderbird"
        // Act & Assert
        UmamiAnalytics.trackProjectClick(projectName = projectName, projectUrl = projectUrl)
        assertTrue(true)
    }

    @Test
    fun `given article share helper, when invoked, then it completes safely`() {
        // Arrange
        val platform = "twitter"
        val articleTitle = "Time travel in git"
        // Act & Assert
        UmamiAnalytics.trackArticleShare(platform = platform, articleTitle = articleTitle)
        assertTrue(true)
    }

    @Test
    fun `given article copy link helper, when invoked, then it completes safely`() {
        // Arrange
        val articleTitle = "Time travel in git"
        // Act & Assert
        UmamiAnalytics.trackArticleCopyLink(articleTitle = articleTitle)
        assertTrue(true)
    }

    @Test
    fun `given language change helper, when invoked, then it completes safely`() {
        // Arrange
        val previousLanguage = "en"
        val newLanguage = "pt-BR"
        // Act & Assert
        UmamiAnalytics.trackLanguageChange(previousLanguage = previousLanguage, newLanguage = newLanguage)
        assertTrue(true)
    }

    @Test
    fun `given theme change helper, when invoked, then it completes safely`() {
        // Arrange
        val theme = "DARK"
        // Act & Assert
        UmamiAnalytics.trackThemeChange(theme = theme)
        assertTrue(true)
    }

    @Test
    fun `given social click helper, when invoked, then it completes safely`() {
        // Arrange
        val platform = "github"
        val url = "https://github.com/rafaeltonholo"
        // Act & Assert
        UmamiAnalytics.trackSocialClick(platform = platform, url = url)
        assertTrue(true)
    }
}
