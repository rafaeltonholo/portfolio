package dev.tonholo.portfolio.components.organisms.articles

import kotlin.test.Test
import kotlin.test.assertEquals

class ArticleContentsJumpTest {
    @Test
    fun `given an eight letter label, when contentsButtonWidthPx is called, then chrome and letters add up`() {
        // Arrange
        val label = "Contents"
        // Act
        val width = contentsButtonWidthPx(label = label)
        // Assert
        assertEquals(expected = 113.84, actual = width, absoluteTolerance = 0.001)
    }

    @Test
    fun `given an empty label, when contentsButtonWidthPx is called, then only the chrome is left`() {
        // Act
        val width = contentsButtonWidthPx(label = "")
        // Assert
        assertEquals(expected = 54.0, actual = width, absoluteTolerance = 0.001)
    }
}
