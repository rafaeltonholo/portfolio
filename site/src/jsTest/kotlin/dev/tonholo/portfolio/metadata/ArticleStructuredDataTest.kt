package dev.tonholo.portfolio.metadata

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class ArticleStructuredDataTest {
    private fun article(imageUrl: String? = null, publishedDate: String = "2026-11-14") =
        ArticleStructuredData(
            headline = "Generative UI on a phone",
            description = "How A2UI works on a phone.",
            url = "https://rafael.tonholo.dev/articles/a",
            publishedDate = publishedDate,
            language = "en-CA",
            keywords = listOf("android", "a2ui"),
            imageUrl = imageUrl,
        )

    @Test
    fun `given an article with a thumbnail, when building json-ld, then it is a full BlogPosting`() {
        // Act
        val json = JSON.parse<dynamic>(article(imageUrl = "https://rafael.tonholo.dev/t.png").toJsonLd())
        // Assert
        assertEquals("https://schema.org", json["@context"])
        assertEquals("BlogPosting", json["@type"])
        assertEquals("Generative UI on a phone", json.headline)
        assertEquals("2026-11-14", json.datePublished)
        assertEquals("Person", json.author["@type"])
        assertEquals("Rafael Tonholo", json.author.name)
        assertEquals("https://rafael.tonholo.dev/about", json.author.url)
        assertEquals("https://rafael.tonholo.dev/t.png", json.image)
        assertEquals("en-CA", json.inLanguage)
        assertEquals("android, a2ui", json.keywords)
    }

    @Test
    fun `given an article without a thumbnail or date, when building json-ld, then image and date are left out`() {
        // Act
        val json = JSON.parse<dynamic>(article(publishedDate = "").toJsonLd())
        // Assert
        assertFalse(js("'image' in json") as Boolean)
        assertFalse(js("'datePublished' in json") as Boolean)
    }
}
