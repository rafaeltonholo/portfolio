package dev.tonholo.portfolio.metadata

import dev.tonholo.portfolio.locale.Locale
import kotlinx.browser.document
import org.w3c.dom.Element
import org.w3c.dom.HTMLMetaElement
import org.w3c.dom.asList
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class PageMetadataTest {
    @BeforeTest
    fun removeMetaTags() {
        document.querySelectorAll("meta").asList().forEach { (it as Element).remove() }
    }

    @Test
    fun `given custom metadata, when applyPageMetadata is called, then head contains updated meta tags`() {
        // Arrange
        val expectedTitle: String = "Test Article Title | Rafael Tonholo"
        val expectedOgTitle: String = "Test Article Title"
        val expectedDescription: String = "This is a detailed summary of the article."
        val expectedUrl: String = "https://rafael.tonholo.dev/articles/test"
        val expectedImage: String = "https://rafael.tonholo.dev/images/articles/test.png"
        val inputMetadata: PageMetadata = PageMetadata(
            title = expectedTitle,
            ogTitle = expectedOgTitle,
            description = expectedDescription,
            canonicalUrl = expectedUrl,
            imageUrl = expectedImage,
            type = PageMetadata.TYPE_ARTICLE,
        )
        // Act
        applyPageMetadata(metadata = inputMetadata, defaultTitle = "Home")
        // Assert
        assertEquals(expected = expectedTitle, actual = document.title)
        val actualDescriptionMeta: HTMLMetaElement? = document
            .querySelector("meta[name='description']")
            .unsafeCast<HTMLMetaElement?>()
        assertNotNull(actualDescriptionMeta)
        assertEquals(expected = expectedDescription, actual = actualDescriptionMeta.content)
        val actualOgTitleMeta: HTMLMetaElement? = document
            .querySelector("meta[property='og:title']")
            .unsafeCast<HTMLMetaElement?>()
        assertNotNull(actualOgTitleMeta)
        assertEquals(expected = expectedOgTitle, actual = actualOgTitleMeta.content)
        val actualOgDescriptionMeta: HTMLMetaElement? = document
            .querySelector("meta[property='og:description']")
            .unsafeCast<HTMLMetaElement?>()
        assertNotNull(actualOgDescriptionMeta)
        assertEquals(expected = expectedDescription, actual = actualOgDescriptionMeta.content)
        val actualOgUrlMeta: HTMLMetaElement? = document
            .querySelector("meta[property='og:url']")
            .unsafeCast<HTMLMetaElement?>()
        assertNotNull(actualOgUrlMeta)
        assertEquals(expected = expectedUrl, actual = actualOgUrlMeta.content)
        val actualOgImageMeta: HTMLMetaElement? = document
            .querySelector("meta[property='og:image']")
            .unsafeCast<HTMLMetaElement?>()
        assertNotNull(actualOgImageMeta)
        assertEquals(expected = expectedImage, actual = actualOgImageMeta.content)
        val actualTwitterDescriptionMeta: HTMLMetaElement? = document
            .querySelector("meta[name='twitter:description']")
            .unsafeCast<HTMLMetaElement?>()
        assertNotNull(actualTwitterDescriptionMeta)
        assertEquals(expected = expectedDescription, actual = actualTwitterDescriptionMeta.content)
    }

    @Test
    fun `given empty metadata, when applyPageMetadata is called, then default values are used`() {
        // Arrange
        val expectedDefaultTitle: String = "Default Title"
        val inputMetadata: PageMetadata = PageMetadata()
        // Act
        applyPageMetadata(metadata = inputMetadata, defaultTitle = expectedDefaultTitle)
        // Assert
        assertEquals(expected = expectedDefaultTitle, actual = document.title)
        val actualDescriptionMeta: HTMLMetaElement? = document
            .querySelector("meta[name='description']")
            .unsafeCast<HTMLMetaElement?>()
        assertNotNull(actualDescriptionMeta)
        assertEquals(expected = PageMetadata.DEFAULT_DESCRIPTION, actual = actualDescriptionMeta.content)
        val actualTwitterCardMeta: HTMLMetaElement? = document
            .querySelector("meta[name='twitter:card']")
            .unsafeCast<HTMLMetaElement?>()
        assertNotNull(actualTwitterCardMeta)
        assertEquals(expected = PageMetadata.TWITTER_CARD_SUMMARY, actual = actualTwitterCardMeta.content)
    }

    @Test
    fun `given no ogTitle, when applyPageMetadata is called, then og title falls back to title`() {
        // Arrange
        val expectedTitle: String = "Only Title"
        // Act
        applyPageMetadata(metadata = PageMetadata(title = expectedTitle), defaultTitle = "Home")
        // Assert
        assertEquals(expected = expectedTitle, actual = metaContent(selector = "meta[property='og:title']"))
    }

    @Test
    fun `given imageUrl, when applyPageMetadata is called, then twitter card is large`() {
        // Arrange
        val inputMetadata: PageMetadata = PageMetadata(imageUrl = "https://example.com/a.png")
        // Act
        applyPageMetadata(metadata = inputMetadata, defaultTitle = "Home")
        // Assert
        assertEquals(
            expected = PageMetadata.TWITTER_CARD_LARGE,
            actual = metaContent(selector = "meta[name='twitter:card']"),
        )
    }

    @Test
    fun `given null imageUrl, when applyPageMetadata is called, then default image is used`() {
        // Act
        applyPageMetadata(metadata = PageMetadata(), defaultTitle = "Home")
        // Assert
        assertEquals(
            expected = PageMetadata.DEFAULT_IMAGE_URL,
            actual = metaContent(selector = "meta[property='og:image']"),
        )
    }

    @Test
    fun `given relative imageUrl, when applyPageMetadata is called, then og image is absolute`() {
        // Arrange
        val inputMetadata: PageMetadata = PageMetadata(imageUrl = "/images/projects/hero.png")
        // Act
        applyPageMetadata(metadata = inputMetadata, defaultTitle = "Home")
        // Assert
        assertEquals(
            expected = "${PageMetadata.SITE_URL}/images/projects/hero.png",
            actual = metaContent(selector = "meta[property='og:image']"),
        )
    }

    @Test
    fun `given applied twice, when applyPageMetadata is called, then tags are updated not duplicated`() {
        // Act
        applyPageMetadata(metadata = PageMetadata(description = "first"), defaultTitle = "Home")
        applyPageMetadata(metadata = PageMetadata(description = "second"), defaultTitle = "Home")
        // Assert
        assertEquals(expected = 1, actual = document.querySelectorAll("meta[name='description']").length)
        assertEquals(expected = "second", actual = metaContent(selector = "meta[name='description']"))
    }

    @Test
    fun `given previous canonical url, when applied without one, then og url is removed`() {
        // Arrange
        applyPageMetadata(metadata = PageMetadata(canonicalUrl = "https://example.com/a"), defaultTitle = "Home")
        // Act
        applyPageMetadata(metadata = PageMetadata(), defaultTitle = "Home")
        // Assert
        assertNull(document.querySelector("meta[property='og:url']"))
    }

    @Test
    fun `given pt-BR locale, when toOgLocale is called, then underscore format is returned`() {
        // Act
        val actual: String = Locale.PtBr.toOgLocale()
        // Assert
        assertEquals(expected = "pt_BR", actual = actual)
    }

    @Test
    fun `given pt_BR locale metadata, when applyPageMetadata is called, then og locale is set`() {
        // Act
        applyPageMetadata(metadata = PageMetadata(locale = "pt_BR"), defaultTitle = "Home")
        // Assert
        assertEquals(expected = "pt_BR", actual = metaContent(selector = "meta[property='og:locale']"))
    }

    private fun metaContent(selector: String): String? =
        document.querySelector(selector).unsafeCast<HTMLMetaElement?>()?.content
}
