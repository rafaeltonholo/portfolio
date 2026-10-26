package dev.tonholo.portfolio.analytics

import dev.tonholo.portfolio.analytics.events.AnalyticEvent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class AnalyticsManagerTest {
    @Test
    fun `given singleton invocation, when invoke is called, then instance is non-null`() {
        // Arrange & Act
        val manager = AnalyticsManager()
        // Assert
        assertNotNull(manager)
    }

    @Test
    fun `given analytics event, when track is called without window umami, then it does not throw`() {
        // Arrange
        val manager = AnalyticsManager()
        val event = AnalyticEvent.ViewResumePdf(language = "en")
        // Act & Assert
        manager.track(event = event)
        assertTrue(true)
    }

    @Test
    fun `given page view event, when toParams is called, then params contain language`() {
        // Arrange
        val event = AnalyticEvent.PageView(language = "pt-BR")
        // Act
        val params = event.toParams()
        // Assert
        assertEquals("pt-BR", params["lang"])
        assertEquals("page_view", event.name)
    }

    @Test
    fun `given view project event, when toParams is called, then params contain project details`() {
        // Arrange
        val event = AnalyticEvent.ViewProject(projectName = "Thunderbird", projectUrl = "/projects/thunderbird")
        // Act
        val params = event.toParams()
        // Assert
        assertEquals("Thunderbird", params["project_name"])
        assertEquals("/projects/thunderbird", params["project_url"])
        assertEquals("view_project", event.name)
    }

    @Test
    fun `given language change event, when toParams is called, then params contain previous and new languages`() {
        // Arrange
        val event = AnalyticEvent.LanguageChange(previousLanguage = "en", language = "pt-BR")
        // Act
        val params = event.toParams()
        // Assert
        assertEquals("en", params["previous_lang"])
        assertEquals("pt-BR", params["lang"])
        assertEquals("language_change", event.name)
    }

    @Test
    fun `given share article event, when toParams is called, then params contain platform and title`() {
        // Arrange
        val event = AnalyticEvent.ShareArticle(platform = "twitter", articleTitle = "Time travel in git")
        // Act
        val params = event.toParams()
        // Assert
        assertEquals("twitter", params["platform"])
        assertEquals("Time travel in git", params["article_title"])
        assertEquals("share_article", event.name)
    }

    @Test
    fun `given theme change event, when toParams is called, then params contain theme`() {
        // Arrange
        val event = AnalyticEvent.ThemeChange(theme = "DARK")
        // Act
        val params = event.toParams()
        // Assert
        assertEquals("DARK", params["theme"])
        assertEquals("theme_change", event.name)
    }
}
