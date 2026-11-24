package dev.tonholo.portfolio.state

import dev.tonholo.portfolio.domain.model.Article
import dev.tonholo.portfolio.domain.model.ArticleStatus
import dev.tonholo.portfolio.domain.model.ProjectKind
import dev.tonholo.portfolio.domain.model.SkillCategory
import dev.tonholo.portfolio.locale.Locale
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
    fun `given StaticContent ARTICLES, when read, then it has six entries starting with the newest`() {
        // Arrange / Act
        val articles: List<Article> = StaticContent.ARTICLES
        // Assert
        assertEquals(expected = 6, actual = articles.size)
        assertEquals(expected = "how-a2ui-works", actual = articles[0].slug)
        assertEquals(expected = "git-time-travel-reflog-reset", actual = articles[1].slug)
    }

    @Test
    fun `given StaticContent ARTICLES, when inspected, then all entries are Published`() {
        // Arrange / Act
        val published: List<Article> = StaticContent.ARTICLES
        // Assert
        assertTrue(actual = published.isNotEmpty())
        assertTrue(actual = published.all { it.status == ArticleStatus.Published })
    }

    @Test
    fun `given StaticContent articlesFor with PtBr, when read, then translated entries are localized`() {
        // Arrange / Act
        val ptArticles: List<Article> = StaticContent.articlesFor(locale = Locale.PtBr)
        // Assert
        assertEquals(expected = 6, actual = ptArticles.size)
        val gitTimeTravel = ptArticles.first { it.slug == "git-time-travel-reflog-reset" }
        assertTrue(actual = gitTimeTravel.title.contains("Viagem no tempo"))
        val howItWorks = ptArticles.first { it.slug == "how-a2ui-works" }
        assertTrue(actual = howItWorks.title.startsWith("Como o A2UI funciona"))
    }

    @Test
    fun `given StaticContent articlesFor with PtBr, when inspecting hello-world, then rich frontmatter is parsed`() {
        // Arrange
        val ptArticles: List<Article> = StaticContent.articlesFor(locale = Locale.PtBr)
        // Act
        val actualArticle: Article = ptArticles.first { it.slug == "hello-world" }
        // Assert
        assertEquals(expected = "Uma nova aventura começa", actual = actualArticle.summary)
        assertEquals(expected = 1, actual = actualArticle.authors.size)
        assertEquals(expected = "Rafael Tonholo", actual = actualArticle.authors[0].name)
        assertEquals(
            expected = "https://rafael.tonholo.dev/images/writing.jpg",
            actual = actualArticle.authors[0].avatar,
        )
        assertEquals(
            expected = "https://rafael.tonholo.dev/images/articles/hello-world/thumbnail.png",
            actual = actualArticle.thumbnail,
        )
        assertEquals(expected = "19 de mai. de 2024", actual = actualArticle.date)
    }

    @Test
    fun `given StaticContent ARTICLES, when inspecting hello-world, then rich frontmatter is parsed`() {
        // Arrange
        val enArticles: List<Article> = StaticContent.ARTICLES
        // Act
        val actualArticle: Article = enArticles.first { it.slug == "hello-world" }
        // Assert
        assertEquals(expected = "A new journey begins", actual = actualArticle.summary)
        assertEquals(expected = 1, actual = actualArticle.authors.size)
        assertEquals(expected = "Rafael Tonholo", actual = actualArticle.authors[0].name)
        assertEquals(
            expected = "https://rafael.tonholo.dev/images/writing.jpg",
            actual = actualArticle.authors[0].avatar,
        )
        assertEquals(
            expected = "https://rafael.tonholo.dev/images/articles/hello-world/thumbnail.png",
            actual = actualArticle.thumbnail,
        )
        assertEquals(expected = "May 19, 2024", actual = actualArticle.date)
    }

    @Test
    fun `given StaticContent articlesFor with EnCa, when read, then it matches ARTICLES`() {
        // Arrange / Act
        val enArticles = StaticContent.articlesFor(locale = Locale.EnCa)

        // Assert
        assertEquals(expected = StaticContent.ARTICLES, actual = enArticles)
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

    @Test
    fun `given the how a2ui works article - when building its pt-BR route - then it uses the Portuguese slug`() {
        // Arrange / Act
        val route: String = StaticContent.articleRoute(slug = "how-a2ui-works", locale = Locale.PtBr)
        // Assert
        assertEquals(expected = "/articles/pt-br/como-o-a2ui-funciona", actual = route)
    }

    @Test
    fun `given the how a2ui works article - when building its English route - then it uses the English slug`() {
        // Arrange / Act
        val route: String = StaticContent.articleRoute(slug = "how-a2ui-works", locale = Locale.EnCa)
        // Assert
        assertEquals(expected = "/articles/how-a2ui-works", actual = route)
    }

    @Test
    fun `given an older translated article - when building its pt-BR route - then it keeps the shared slug`() {
        // Arrange / Act
        val route: String = StaticContent.articleRoute(slug = "git-time-travel-reflog-reset", locale = Locale.PtBr)
        // Assert
        assertEquals(expected = "/articles/pt-br/git-time-travel-reflog-reset", actual = route)
    }

    @Test
    fun `given a Portuguese path slug - when finding the article slug - then the shared slug is returned`() {
        // Arrange / Act
        val slug: String = StaticContent.articleSlugForPath(pathSlug = "como-o-a2ui-funciona", locale = Locale.PtBr)
        // Assert
        assertEquals(expected = "how-a2ui-works", actual = slug)
    }
}
