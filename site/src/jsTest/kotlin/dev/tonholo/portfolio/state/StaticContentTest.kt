package dev.tonholo.portfolio.state

import dev.tonholo.portfolio.domain.model.ArticleStatus
import dev.tonholo.portfolio.domain.model.ProjectKind
import dev.tonholo.portfolio.domain.model.SkillCategory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StaticContentTest {
    @Test
    fun `given StaticContent PROJECTS, when read, then it has four entries starting with thunderbird`() {
        // Arrange / Act
        val projects = StaticContent.PROJECTS

        // Assert
        assertEquals(expected = 4, actual = projects.size)
        assertEquals(expected = "thunderbird", actual = projects[0].slug)
        assertEquals(expected = "01", actual = projects[0].index)
        assertTrue(ProjectKind.Commercial in projects[0].kinds)
        assertTrue(ProjectKind.OpenSource in projects[0].kinds)
    }

    @Test
    fun `given StaticContent PROJECTS, when inspected, then slugs are in the expected order`() {
        // Arrange
        val expectedSlugs = listOf(
            "thunderbird",
            "bleacher-report",
            "pinterest",
            "svg-compose",
        )

        // Act
        val actualSlugs = StaticContent.PROJECTS.map { it.slug }

        // Assert
        assertEquals(expected = expectedSlugs, actual = actualSlugs)
    }

    @Test
    fun `given StaticContent ARTICLES, when read, then it has four entries starting with git-time-travel`() {
        // Arrange / Act
        val articles = StaticContent.ARTICLES
        // Assert
        assertEquals(expected = 4, actual = articles.size)
        assertEquals(
            expected = "git-time-travel-reflog-reset",
            actual = articles[0].slug,
        )
    }

    @Test
    fun `given StaticContent ARTICLES, when inspected, then all entries are Published`() {
        // Arrange / Act
        val published = StaticContent.ARTICLES
        // Assert
        assertTrue(actual = published.isNotEmpty())
        assertTrue(actual = published.all { it.status == ArticleStatus.Published })
    }

    @Test
    fun `given StaticContent EXPERIENCE, when read, then it has seven entries`() {
        // Arrange / Act
        val experience = StaticContent.EXPERIENCE

        // Assert
        assertEquals(expected = 7, actual = experience.size)
        assertEquals(expected = "Senior Software Engineer, Mobile", actual = experience[0].role)
    }

    @Test
    fun `given StaticContent EDUCATION, when read, then it has three entries`() {
        // Arrange / Act
        val education = StaticContent.EDUCATION

        // Assert
        assertEquals(expected = 3, actual = education.size)
    }

    @Test
    fun `given StaticContent USES, when read, then it has four categories`() {
        // Arrange / Act
        val uses = StaticContent.USES

        // Assert
        assertEquals(expected = 4, actual = uses.size)
        assertEquals(expected = listOf("editor", "hardware", "desk", "cli"), actual = uses.map { it.name })
    }

    @Test
    fun `given StaticContent NOW_STRIP, when read, then it has three entries`() {
        // Arrange / Act
        val now = StaticContent.NOW_STRIP

        // Assert
        assertEquals(expected = 3, actual = now.size)
    }

    @Test
    fun `given StaticContent PRIMARY_STACK, when read, then it is the primary stack category`() {
        // Arrange / Act
        val bucket = StaticContent.PRIMARY_STACK

        // Assert
        assertEquals(expected = SkillCategory.PrimaryStack, actual = bucket.category)
        assertTrue(actual = bucket.skills.isNotEmpty())
    }

    @Test
    fun `given StaticContent ALSO_COMFORTABLE, when read, then it is the also comfortable category`() {
        // Arrange / Act
        val bucket = StaticContent.ALSO_COMFORTABLE

        // Assert
        assertEquals(expected = SkillCategory.AlsoComfortable, actual = bucket.category)
        assertTrue(actual = bucket.skills.isNotEmpty())
    }
}
