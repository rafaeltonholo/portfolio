package dev.tonholo.portfolio.buildlogic.markdown

import kotlin.test.Test
import kotlin.test.assertEquals

class MarkdownArticlesProcessorTest {
    @Test
    fun `given an english display date - when converting to iso - then the calendar date is returned`() {
        assertEquals(expected = "2026-10-25", actual = MarkdownArticlesProcessor.toIsoDate("oct 25, 2026"))
    }

    @Test
    fun `given an iso timestamp - when converting to iso - then only the date is kept`() {
        assertEquals(expected = "2024-05-19", actual = MarkdownArticlesProcessor.toIsoDate("2024-05-19T17:16:34-03:00"))
    }

    @Test
    fun `given an unparseable date - when converting to iso - then it is blank instead of a placeholder`() {
        assertEquals(expected = "", actual = MarkdownArticlesProcessor.toIsoDate("someday"))
    }
}
