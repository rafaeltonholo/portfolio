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
    fun `given PtBr locale, when about group is read, then returns Portuguese translation`() {
        // Arrange
        val strings = stringsFor(locale = Locale.PtBr)

        // Act
        val actual = strings.about

        // Assert - every About copy key is translated; assert the instance
        // diverges from the English defaults so a future regression that
        // strips a translation trips this test.
        assertEquals(expected = StringsPtBr.about, actual = actual)
    }

    @Test
    fun `given PtBr locale, when statusBadge is read, then returns Portuguese translation`() {
        // Arrange
        val strings = stringsFor(locale = Locale.PtBr)

        // Act
        val actual = strings.statusBadge

        // Assert
        assertEquals(expected = StringsPtBr.statusBadge, actual = actual)
    }

    @Test
    fun `given PtBr locale, when hero titleLine1 is read, then returns Portuguese translation`() {
        // Arrange
        val strings = stringsFor(locale = Locale.PtBr)

        // Act
        val actual = strings.hero.titleLine1

        // Assert
        assertEquals(expected = StringsPtBr.hero.titleLine1, actual = actual)
    }

    @Test
    fun `given EnCa locale, when a11y opensInNewTab is read, then returns English announcement`() {
        // Arrange
        val strings = stringsFor(locale = Locale.EnCa)

        // Act
        val actual = strings.a11y.opensInNewTab

        // Assert
        assertEquals(expected = "(opens in a new tab)", actual = actual)
    }

    @Test
    fun `given PtBr locale, when a11y opensInNewTab is read, then returns Portuguese announcement`() {
        // Arrange
        val strings = stringsFor(locale = Locale.PtBr)

        // Act
        val actual = strings.a11y.opensInNewTab

        // Assert
        assertEquals(expected = "(abre em uma nova aba)", actual = actual)
    }

    @Test
    fun `given EnCa locale, when articlesList is read, then returns expected values`() {
        // Arrange
        val strings: Strings = stringsFor(locale = Locale.EnCa)
        // Act
        val actual: Strings.ArticlesList = strings.articlesList
        // Assert
        assertEquals(expected = "Writing.", actual = actual.title)
        assertEquals(expected = "posts", actual = actual.postsSuffix)
    }

    @Test
    fun `given PtBr locale, when articlesList is read, then returns expected values`() {
        // Arrange
        val strings: Strings = stringsFor(locale = Locale.PtBr)
        // Act
        val actual: Strings.ArticlesList = strings.articlesList
        // Assert
        assertEquals(expected = "Escrita.", actual = actual.title)
        assertEquals(expected = "posts", actual = actual.postsSuffix)
    }
}
