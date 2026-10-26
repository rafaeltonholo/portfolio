package dev.tonholo.portfolio.components.atoms

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ArticleLinkTest {
    @Test
    fun `given external protocols, when isExternalLink is called, then returns true`() {
        assertTrue(isExternalLink("https://github.com/rafaeltonholo/svg-to-compose"))
        assertTrue(isExternalLink("http://example.com"))
        assertTrue(isExternalLink("//cdn.example.com/asset.js"))
    }

    @Test
    fun `given relative or anchor links, when isExternalLink is called, then returns false`() {
        assertFalse(isExternalLink("/articles"))
        assertFalse(isExternalLink("#introduction"))
        assertFalse(isExternalLink("relative/path"))
        assertFalse(isExternalLink("mailto:test@example.com"))
    }

    @Test
    fun `given ArticleLink styles, when referenced, then they are non-null`() {
        assertNotNull(ArticleLinkStyle)
        assertNotNull(ArticleLinkArrowStyle)
    }
}
