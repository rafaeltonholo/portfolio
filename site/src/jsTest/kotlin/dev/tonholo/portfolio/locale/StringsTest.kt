package dev.tonholo.portfolio.locale

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class StringsTest {
    @Test
    fun `given EnCa locale, when stringsFor is called, then returns StringsEn`() {
        // Arrange
        val locale = Locale.EnCa

        // Act
        val actual = stringsFor(locale = locale)

        // Assert
        assertSame(expected = StringsEn, actual = actual)
    }

    @Test
    fun `given PtBr locale, when stringsFor is called, then returns StringsPtBr`() {
        // Arrange
        val locale = Locale.PtBr

        // Act
        val actual = stringsFor(locale = locale)

        // Assert
        assertSame(expected = StringsPtBr, actual = actual)
    }

    @Test
    fun `given PtBr locale, when nav home is read, then returns inicio translation`() {
        // Arrange
        val strings = stringsFor(locale = Locale.PtBr)

        // Act
        val actual = strings.nav.home

        // Assert
        assertEquals(expected = "início", actual = actual)
    }

    @Test
    fun `given PtBr locale, when nav resume is read, then returns curriculo translation`() {
        // Arrange
        val strings = stringsFor(locale = Locale.PtBr)

        // Act
        val actual = strings.nav.resume

        // Assert
        assertEquals(expected = "currículo", actual = actual)
    }

    @Test
    fun `given PtBr locale, when languageToast secondary is read, then returns Portuguese copy`() {
        // Arrange
        val strings = stringsFor(locale = Locale.PtBr)

        // Act
        val actual = strings.languageToast.secondary

        // Assert
        assertEquals(
            expected = "A maior parte do conteúdo ainda está em inglês por enquanto.",
            actual = actual,
        )
    }

    @Test
    fun `given PtBr locale, when footer headingPages is read, then returns Paginas translation`() {
        // Arrange
        val strings = stringsFor(locale = Locale.PtBr)

        // Act
        val actual = strings.footer.headingPages

        // Assert
        assertEquals(expected = "Páginas", actual = actual)
    }

    @Test
    fun `given PtBr locale, when hero ctaPrimary is read, then returns Portuguese label`() {
        // Arrange
        val strings = stringsFor(locale = Locale.PtBr)

        // Act
        val actual = strings.hero.ctaPrimary

        // Assert
        assertEquals(expected = "ver trabalhos em destaque", actual = actual)
    }

    @Test
    fun `given PtBr locale, when about group is read, then falls back to English defaults`() {
        // Arrange
        val strings = stringsFor(locale = Locale.PtBr)

        // Act
        val actual = strings.about

        // Assert - untranslated groups reuse the English defaults verbatim.
        assertEquals(expected = StringsEn.about, actual = actual)
    }

    @Test
    fun `given PtBr locale, when statusBadge is read, then falls back to English defaults`() {
        // Arrange
        val strings = stringsFor(locale = Locale.PtBr)

        // Act
        val actual = strings.statusBadge

        // Assert
        assertEquals(expected = StringsEn.statusBadge, actual = actual)
    }

    @Test
    fun `given PtBr locale, when hero titleLine1 is read, then reuses English default`() {
        // Arrange
        val strings = stringsFor(locale = Locale.PtBr)

        // Act
        val actual = strings.hero.titleLine1

        // Assert - only CTA buttons are translated on the hero for v1.
        assertEquals(expected = StringsEn.hero.titleLine1, actual = actual)
    }
}
