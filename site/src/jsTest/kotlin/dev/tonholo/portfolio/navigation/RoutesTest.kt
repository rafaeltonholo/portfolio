package dev.tonholo.portfolio.navigation

import dev.tonholo.portfolio.locale.Locale
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class RoutesTest {
    @Test
    fun `given Routes constants, when read, then they match the expected URLs`() {
        // Arrange / Act / Assert
        assertEquals("/", Routes.HOME)
        assertEquals("/about", Routes.ABOUT)
        assertEquals("/projects", Routes.PROJECTS)
        assertEquals("/articles", Routes.ARTICLES)
        assertEquals("/resume", Routes.RESUME)
    }

    @Test
    fun `given an article slug, when article is called with default locale, then it returns the bare articles path`() {
        // Arrange
        val slug = "compose-stability-without-the-cult"

        // Act
        val result = Routes.article(slug)

        // Assert
        assertEquals("/articles/compose-stability-without-the-cult", result)
    }

    @Test
    fun `given an article slug and PtBr locale, when article is called, then it returns the prefixed path`() {
        // Arrange
        val slug = "hello-world"

        // Act
        val result = Routes.article(slug = slug, locale = Locale.PtBr)

        // Assert
        assertEquals("/articles/pt-br/hello-world", result)
    }

    @Test
    fun `given a project slug, when project is called with default locale, then it returns the bare projects path`() {
        // Arrange
        val slug = "thunderbird"

        // Act
        val result = Routes.project(slug)

        // Assert
        assertEquals("/projects/thunderbird", result)
    }

    @Test
    fun `given a project slug and PtBr locale, when project is called, then it returns the prefixed path`() {
        // Arrange
        val slug = "bleacher-report"

        // Act
        val result = Routes.project(slug = slug, locale = Locale.PtBr)

        // Assert
        assertEquals("/projects/pt-br/bleacher-report", result)
    }

    @Test
    fun `given a bare project URL, when parseLocalizedSlug is called, then it resolves to EnCa`() {
        // Arrange
        val path = "/projects/pinterest"

        // Act
        val actual = Routes.parseLocalizedSlug(path)

        // Assert
        assertEquals(LocalizedSlug(section = "/projects", locale = Locale.EnCa, slug = "pinterest"), actual)
    }

    @Test
    fun `given a pt-br project URL, when parseLocalizedSlug is called, then it resolves to PtBr`() {
        // Arrange
        val path = "/projects/pt-br/pinterest"

        // Act
        val actual = Routes.parseLocalizedSlug(path)

        // Assert
        assertEquals(LocalizedSlug(section = "/projects", locale = Locale.PtBr, slug = "pinterest"), actual)
    }

    @Test
    fun `given a bare article URL, when parseLocalizedSlug is called, then it resolves to EnCa`() {
        // Arrange
        val path = "/articles/hello-world"

        // Act
        val actual = Routes.parseLocalizedSlug(path)

        // Assert
        assertEquals(LocalizedSlug(section = "/articles", locale = Locale.EnCa, slug = "hello-world"), actual)
    }

    @Test
    fun `given the about route, when parseLocalizedSlug is called, then it returns null`() {
        // Arrange
        val path = "/about"

        // Act
        val actual = Routes.parseLocalizedSlug(path)

        // Assert
        assertNull(actual)
    }

    @Test
    fun `given a trailing slash and query, when parseLocalizedSlug is called, then it still parses`() {
        // Arrange
        val path = "/projects/pt-br/pinterest/?foo=1#bar"

        // Act
        val actual = Routes.parseLocalizedSlug(path)

        // Assert
        assertEquals(LocalizedSlug(section = "/projects", locale = Locale.PtBr, slug = "pinterest"), actual)
    }

    @Test
    fun `given a section-only URL, when parseLocalizedSlug is called, then it returns null`() {
        // Arrange
        val path = "/projects"

        // Act
        val actual = Routes.parseLocalizedSlug(path)

        // Assert
        assertNull(actual)
    }
}
