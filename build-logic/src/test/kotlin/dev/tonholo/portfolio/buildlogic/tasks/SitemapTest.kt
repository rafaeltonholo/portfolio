package dev.tonholo.portfolio.buildlogic.tasks

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SitemapTest {
    @Test
    fun `given site paths including the 404 page - when rendering the sitemap - then urls use the site host and skip 404`() {
        val sitemap = GenerateSitemapTask.renderSitemap(
            paths = listOf("/", "/404", "/articles/a"),
            metadata = emptyMap(),
            baseUrl = GenerateSitemapTask.DEFAULT_BASE_URL,
            lastModified = "2026-11-14",
        )

        assertTrue(sitemap.contains("<loc>https://rafael.tonholo.dev/articles/a</loc>"))
        assertTrue(sitemap.contains("<loc>https://rafael.tonholo.dev/</loc>"))
        assertFalse(sitemap.contains("/404"))
    }
}
