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
}
