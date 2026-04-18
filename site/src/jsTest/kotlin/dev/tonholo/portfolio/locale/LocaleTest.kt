package dev.tonholo.portfolio.locale

import kotlin.test.Test
import kotlin.test.assertEquals

class LocaleTest {
    @Test
    fun `given null tag, when fromTagOrDefault is called, then returns EnCa`() {
        // Arrange
        val tag: String? = null

        // Act
        val actual = Locale.fromTagOrDefault(tag)

        // Assert
        assertEquals(Locale.EnCa, actual)
    }

    @Test
    fun `given pt-BR tag, when fromTagOrDefault is called, then returns PtBr`() {
        // Arrange
        val tag = "pt-BR"

        // Act
        val actual = Locale.fromTagOrDefault(tag)

        // Assert
        assertEquals(Locale.PtBr, actual)
    }

    @Test
    fun `given en-CA tag, when fromTagOrDefault is called, then returns EnCa`() {
        // Arrange
        val tag = "en-CA"

        // Act
        val actual = Locale.fromTagOrDefault(tag)

        // Assert
        assertEquals(Locale.EnCa, actual)
    }

    @Test
    fun `given unknown tag, when fromTagOrDefault is called, then returns EnCa default`() {
        // Arrange
        val tag = "fr-FR"

        // Act
        val actual = Locale.fromTagOrDefault(tag)

        // Assert
        assertEquals(Locale.EnCa, actual)
    }

    @Test
    fun `given EnCa entry, when tag is read, then equals en-CA`() {
        // Arrange / Act
        val actual = Locale.EnCa.tag

        // Assert
        assertEquals("en-CA", actual)
    }

    @Test
    fun `given PtBr entry, when tag is read, then equals pt-BR`() {
        // Arrange / Act
        val actual = Locale.PtBr.tag

        // Assert
        assertEquals("pt-BR", actual)
    }
}
