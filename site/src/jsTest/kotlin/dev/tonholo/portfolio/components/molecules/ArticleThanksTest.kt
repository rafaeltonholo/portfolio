package dev.tonholo.portfolio.components.molecules

import dev.tonholo.portfolio.locale.Strings.ArticleThanks
import dev.tonholo.portfolio.locale.StringsEn
import kotlin.test.Test
import kotlin.test.assertEquals

class ArticleThanksTest {
    private val strings: ArticleThanks = StringsEn.articleThanks

    @Test
    fun `given one reviewer - when listing separators - then nothing follows the name`() {
        val separators: List<String> = separatorsFor(count = 1)

        assertEquals(expected = listOf(""), actual = separators)
    }

    @Test
    fun `given two reviewers - when listing separators - then the names are joined as a pair`() {
        val separators: List<String> = separatorsFor(count = 2)

        assertEquals(expected = listOf(strings.pairSeparator, ""), actual = separators)
    }

    @Test
    fun `given four reviewers - when listing separators - then the last name gets the last separator`() {
        val separators: List<String> = separatorsFor(count = 4)

        assertEquals(
            expected = listOf(strings.listSeparator, strings.listSeparator, strings.lastSeparator, ""),
            actual = separators,
        )
    }

    private fun separatorsFor(count: Int): List<String> =
        List(count) { index -> reviewerSeparator(index = index, count = count, strings = strings) }
}
