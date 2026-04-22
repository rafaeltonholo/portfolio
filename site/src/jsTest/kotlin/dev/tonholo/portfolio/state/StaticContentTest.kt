package dev.tonholo.portfolio.state

import dev.tonholo.portfolio.domain.model.ArticleStatus
import dev.tonholo.portfolio.domain.model.ProjectKind
import dev.tonholo.portfolio.domain.model.SkillCategory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StaticContentTest {
    @Test
    fun `given StaticContent PROJECTS, when read, then it has five entries starting with thunderbird`() {
        // Arrange / Act
        val projects = StaticContent.PROJECTS

        // Assert
        assertEquals(expected = 5, actual = projects.size)
        assertEquals(expected = "thunderbird", actual = projects[0].slug)
        assertEquals(expected = "01", actual = projects[0].index)
        assertEquals(expected = ProjectKind.OpenSource, actual = projects[0].kind)
    }

    @Test
    fun `given StaticContent PROJECTS, when inspected, then slugs are in the expected order`() {
        // Arrange
        val expectedSlugs = listOf(
            "thunderbird",
            "bleacher-report",
            "pinterest",
            "svg-compose",
            "ivy-wallet",
        )

        // Act
        val actualSlugs = StaticContent.PROJECTS.map { it.slug }

        // Assert
        assertEquals(expected = expectedSlugs, actual = actualSlugs)
    }

    @Test
    fun `given StaticContent ARTICLES, when read, then it has seven entries and first is draft`() {
        // Arrange / Act
        val articles = StaticContent.ARTICLES

        // Assert
        assertEquals(expected = 7, actual = articles.size)
        assertEquals(
            expected = "compose-stability-without-the-cult",
            actual = articles[0].slug,
        )
        assertEquals(expected = ArticleStatus.Draft, actual = articles[0].status)
    }

    @Test
    fun `given StaticContent ARTICLES, when inspected, then non-draft entries are Published`() {
        // Arrange / Act
        val published = StaticContent.ARTICLES.drop(1)

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
    fun `given StaticContent TICKER, when read, then it has four items`() {
        // Arrange / Act
        val ticker = StaticContent.TICKER

        // Assert
        assertEquals(expected = 4, actual = ticker.size)
        assertEquals(expected = "now", actual = ticker[0].label)
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
