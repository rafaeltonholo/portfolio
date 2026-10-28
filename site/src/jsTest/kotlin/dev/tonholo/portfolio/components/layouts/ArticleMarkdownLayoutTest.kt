package dev.tonholo.portfolio.components.layouts

import dev.tonholo.portfolio.components.organisms.articles.TocEntry
import dev.tonholo.portfolio.generated.articles.GeneratedArticles
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ArticleMarkdownLayoutTest {
    @Test
    fun `given empty entries, when resolveActiveHeadingId is called, then returns empty string`() {
        assertEquals("", resolveActiveHeadingId(emptyList()))
    }

    @Test
    fun `given entries not in DOM, when resolveActiveHeadingId is called, then returns empty string`() {
        val entries = listOf(
            TocEntry(id = "non-existent-1", label = "Section 1"),
            TocEntry(id = "non-existent-2", label = "Section 2"),
        )
        assertEquals("", resolveActiveHeadingId(entries))
    }

    @Test
    fun `given layout styles, when referenced, then they are non-null`() {
        assertNotNull(ArticleMarkdownTitleStyle)
        assertNotNull(ArticleMarkdownLedeStyle)
        assertNotNull(ArticleMarkdownBodyStyle)
        assertNotNull(ArticleMarkdownGridStyle)
    }

    @Test
    fun `given all generated articles, then readTime is populated and non-blank`() {
        // Arrange
        val articles = GeneratedArticles.ALL
        // Act & Assert
        assertTrue(articles.isNotEmpty(), "Expected generated articles to not be empty")
        for (article in articles) {
            assertTrue(article.slug.isNotBlank(), "Expected slug to not be blank")
            assertTrue(article.readTime.isNotBlank(), "Expected readTime for ${article.slug} to not be blank")
        }
    }
}
