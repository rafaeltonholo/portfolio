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

    @Test
    fun `given pascalCase with acronyms, when toKebabCase is called, then acronyms are split correctly`() {
        // Arrange
        val input = "LogDHowToComposableText"
        // Act
        val actual = input.toKebabCase()
        // Assert
        assertEquals("log-d-how-to-composable-text", actual)
    }

    @Test
    fun `given pascalCase with single letter capital, when toKebabCase is called, then single letter is separated`() {
        // Arrange
        val input = "TheDayIFoundMyselfCountingBytes"
        // Act
        val actual = input.toKebabCase()
        // Assert
        assertEquals("the-day-i-found-myself-counting-bytes", actual)
    }

    @Test
    fun `given article file stem, when toKebabCase is called, then matches article slug`() {
        // Arrange
        val input = "GitTimeTravelReflogReset"
        // Act
        val actual = input.toKebabCase()
        // Assert
        assertEquals("git-time-travel-reflog-reset", actual)
    }
}
