package dev.tonholo.portfolio.navigation

import kotlin.test.Test
import kotlin.test.assertEquals

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
    fun `given an article slug, when article is called, then it returns the articles path`() {
        // Arrange
        val slug = "compose-stability-without-the-cult"

        // Act
        val result = Routes.article(slug)

        // Assert
        assertEquals("/articles/compose-stability-without-the-cult", result)
    }

    @Test
    fun `given a project slug, when project is called, then it returns the projects path`() {
        // Arrange
        val slug = "thunderbird"

        // Act
        val result = Routes.project(slug)

        // Assert
        assertEquals("/projects/thunderbird", result)
    }
}
