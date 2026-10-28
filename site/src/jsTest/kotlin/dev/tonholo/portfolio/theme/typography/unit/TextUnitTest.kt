package dev.tonholo.portfolio.theme.typography.unit

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TextUnitTest {
    @Test
    fun `given int equal to default font size, when sp, then toString is one rem`() {
        // Arrange
        val value = 16

        // Act
        val sp = value.sp

        // Assert
        assertEquals(expected = "1rem", actual = sp.toString())
    }

    @Test
    fun `given int 12, when sp, then toString is 0_75rem`() {
        // Arrange
        val value = 12

        // Act
        val sp = value.sp

        // Assert
        assertEquals(expected = "0.75rem", actual = sp.toString())
    }

    @Test
    fun `given int 14, when sp, then toString is 0_875rem`() {
        // Arrange
        val value = 14

        // Act
        val sp = value.sp

        // Assert
        assertEquals(expected = "0.875rem", actual = sp.toString())
    }

    @Test
    fun `given int zero, when sp, then toString is zero rem`() {
        // Arrange
        val value = 0

        // Act
        val sp = value.sp

        // Assert
        assertEquals(expected = "0rem", actual = sp.toString())
    }

    @Test
    fun `given double 17, when sp, then toString is 1_0625rem`() {
        // Arrange
        val value = 17.0

        // Act
        val sp = value.sp

        // Assert
        assertEquals(expected = "1.0625rem", actual = sp.toString())
    }

    @Test
    fun `given double 68, when sp, then toString is 4_25rem for h1`() {
        // Arrange
        val value = 68.0

        // Act
        val sp = value.sp

        // Assert
        assertEquals(expected = "4.25rem", actual = sp.toString())
    }

    @Test
    fun `given float 11, when sp, then toString is 0_6875rem for chip`() {
        // Arrange
        val value = 11f

        // Act
        val sp = value.sp

        // Assert
        assertEquals(expected = "0.6875rem", actual = sp.toString())
    }

    @Test
    fun `given Unspecified, when accessed, then value exists for CSS unset`() {
        // Arrange / Act
        val sp = TextUnit.Unspecified

        // Assert
        assertNotNull(sp)
    }

    @Test
    fun `given DEFAULT_FONT_SIZE constant, when read, then equals 16`() {
        // Arrange / Act / Assert
        assertEquals(expected = 16, actual = DEFAULT_FONT_SIZE)
    }
}
