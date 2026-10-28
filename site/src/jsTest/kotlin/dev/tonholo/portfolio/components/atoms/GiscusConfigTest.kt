package dev.tonholo.portfolio.components.atoms

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Unit tests for [GiscusConfig] evaluation logic.
 */
class GiscusConfigTest {
    @Test
    fun givenStandardSearchQuery_whenShouldInjectIsCalled_thenReturnsTrue() {
        val inputSearch: String = "?ref=twitter"
        val actualResult: Boolean = GiscusConfig.shouldInject(search = inputSearch)
        assertTrue(actualResult)
    }

    @Test
    fun givenEmptySearchQuery_whenShouldInjectIsCalled_thenReturnsTrue() {
        val inputSearch: String = ""
        val actualResult: Boolean = GiscusConfig.shouldInject(search = inputSearch)
        assertTrue(actualResult)
    }

    @Test
    fun givenKobwebExportSearchQuery_whenShouldInjectIsCalled_thenReturnsFalse() {
        val inputSearch: String = "?_kobwebIsExporting=true&_kobwebColorModeStrategy=BOTH"
        val actualResult: Boolean = GiscusConfig.shouldInject(search = inputSearch)
        assertFalse(actualResult)
    }

    @Test
    fun givenCleanProductionIframeSrc_whenIsContaminatedIframeIsCalled_thenReturnsFalse() {
        val inputSrc: String = "https://giscus.app/en/widget" +
            "?origin=https%3A%2F%2Frafael.tonholo.dev%2Farticles%2Fhello-world"
        val actualResult: Boolean = GiscusConfig.isContaminatedIframe(src = inputSrc)
        assertFalse(actualResult)
    }

    @Test
    fun givenLocalhostIframeSrc_whenIsContaminatedIframeIsCalled_thenReturnsTrue() {
        val inputSrc: String = "https://giscus.app/en/widget" +
            "?origin=http%3A%2F%2Flocalhost%3A8080%2Farticles%2Fhello-world"
        val actualResult: Boolean = GiscusConfig.isContaminatedIframe(src = inputSrc)
        assertTrue(actualResult)
    }

    @Test
    fun givenExportFlagIframeSrc_whenIsContaminatedIframeIsCalled_thenReturnsTrue() {
        val inputSrc: String = "https://giscus.app/en/widget" +
            "?origin=http%3A%2F%2Fdomain%2Fpage%3F_kobwebIsExporting%3Dtrue"
        val actualResult: Boolean = GiscusConfig.isContaminatedIframe(src = inputSrc)
        assertTrue(actualResult)
    }
}
