package dev.tonholo.portfolio.theme.layout.unit

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DpTest {
    @Test
    fun `given int equal to default font size, when dp, then toString is one rem`() {
        // Arrange
        val value = 16

        // Act
        val dp = value.dp

        // Assert
        assertEquals(expected = "1rem", actual = dp.toString())
    }

    @Test
    fun `given int double of default font size, when dp, then toString is two rem`() {
        // Arrange
        val value = 32

        // Act
        val dp = value.dp

        // Assert
        assertEquals(expected = "2rem", actual = dp.toString())
    }

    @Test
    fun `given int half of default font size, when dp, then toString is half rem`() {
        // Arrange
        val value = 8

        // Act
        val dp = value.dp

        // Assert
        assertEquals(expected = "0.5rem", actual = dp.toString())
    }

    @Test
    fun `given int zero, when dp, then toString is zero rem`() {
        // Arrange
        val value = 0

        // Act
        val dp = value.dp

        // Assert
        assertEquals(expected = "0rem", actual = dp.toString())
    }

    @Test
    fun `given double that equals default font size, when dp, then toString is one rem`() {
        // Arrange
        val value = 16.0

        // Act
        val dp = value.dp

        // Assert
        assertEquals(expected = "1rem", actual = dp.toString())
    }

    @Test
    fun `given double 24, when dp, then toString is 1_5rem`() {
        // Arrange
        val value = 24.0

        // Act
        val dp = value.dp

        // Assert
        assertEquals(expected = "1.5rem", actual = dp.toString())
    }

    @Test
    fun `given float 4, when dp, then toString is 0_25rem`() {
        // Arrange
        val value = 4f

        // Act
        val dp = value.dp

        // Assert
        assertEquals(expected = "0.25rem", actual = dp.toString())
    }

    @Test
    fun `given float 1, when dp, then toString is 0_0625rem for hairline`() {
        // Arrange
        val value = 1f

        // Act
        val dp = value.dp

        // Assert
        assertEquals(expected = "0.0625rem", actual = dp.toString())
    }

    @Test
    fun `given negative int, when dp, then toString preserves sign`() {
        // Arrange
        val value = -16

        // Act
        val dp = value.dp

        // Assert
        assertEquals(expected = "-1rem", actual = dp.toString())
    }

    @Test
    fun `given Unspecified, when accessed, then value exists for CSS unset`() {
        // Arrange / Act
        val dp = Dp.Unspecified

        // Assert
        assertNotNull(dp)
    }
}
