package dev.tonholo.portfolio.components.organisms.articles

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ArticleTocSidebarTest {
    @Test
    fun `given title and url, when buildShareUrls is called, then generates valid intent links`() {
        val title = "Log.d(\"HowTo\", \"@Composable fun Text()\")"
        val url = "https://tonholo.dev/articles/log-d-how-to-composable-text"

        val shareUrls: ArticleShareUrls = buildShareUrls(title = title, url = url)

        assertTrue(shareUrls.twitter.startsWith("https://twitter.com/intent/tweet?text="))
        assertTrue(shareUrls.twitter.contains("url="))
        assertTrue(shareUrls.linkedIn.startsWith("https://www.linkedin.com/sharing/share-offsite/?url="))
        assertTrue(shareUrls.bluesky.startsWith("https://bsky.app/intent/compose?text="))
    }

    @Test
    fun `given empty title, when buildShareUrls is called for Bluesky, then only url is present`() {
        val url = "https://tonholo.dev/articles/my-post"
        val shareUrls: ArticleShareUrls = buildShareUrls(title = "", url = url)
        val expected = "https://bsky.app/intent/compose?text=https%3A%2F%2Ftonholo.dev%2Farticles%2Fmy-post"

        assertEquals(expected, shareUrls.bluesky)
    }

    @Test
    fun `given TocEntry, when created, then fields match`() {
        val entry = TocEntry(id = "intro", label = "Introduction")
        assertEquals("intro", entry.id)
        assertEquals("Introduction", entry.label)
    }

    @Test
    fun `given sidebar styles, when referenced, then they are non-null`() {
        assertNotNull(ArticleTocSidebarStyle)
        assertNotNull(TocAnchorStyle)
        assertNotNull(ShareButtonStyle)
        assertNotNull(ShareLinkStyle)
        assertNotNull(ShareLinkArrowStyle)
        assertNotNull(SidebarListStyle)
        assertNotNull(CopyCheckmarkPopKeyframes)
    }
}
