package dev.tonholo.portfolio.extensions

import kotlin.test.Test
import kotlin.test.assertEquals

class StringExtensionTest {
    @Test
    fun `given camelCase input, when toKebabCase is called, then dashes separate words`() {
        // Arrange
        val input = "ResumePage"

        // Act
        val actual = input.toKebabCase()

        // Assert
        assertEquals("resume-page", actual)
    }

    @Test
    fun `given whitespace-separated input, when toKebabCase is called, then whitespace becomes a dash`() {
        // Arrange
        val input = "Bleacher Report"

        // Act
        val actual = input.toKebabCase()

        // Assert
        assertEquals("bleacher-report", actual)
    }

    @Test
    fun `given single-word input, when toKebabCase is called, then output is lowercase with no dashes`() {
        // Arrange
        val input = "Pinterest"

        // Act
        val actual = input.toKebabCase()

        // Assert
        assertEquals("pinterest", actual)
    }
}
