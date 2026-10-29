package dev.tonholo.portfolio.metadata

import kotlinx.browser.document
import org.w3c.dom.HTMLMetaElement
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class PageMetadataTest {
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
}
