package dev.tonholo.portfolio.theme.typography

import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextTransform
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

private const val TOLERANCE = 0.001

/**
 * Validates that [Typography] styles match the Phase 2 handoff CSS at
 * `/tmp/rafael-redesign/rafael-portfolio-redesign/project/styles.css`.
 *
 * Every `size` is encoded as `cssRem` (1rem == 16px). Every `letterSpacing`
 * is `em`. Line-heights stay unitless doubles per the CSS.
 */
class TypographyTest {
    @Test
    fun `given FontFamilies, when constants are read, then they match handoff stack`() {
        // Arrange / Act / Assert
        assertEquals(
            "'JetBrains Mono', ui-monospace, SFMono-Regular, Menlo, monospace",
            FontFamilies.MONO,
        )
        assertEquals("'Geist', -apple-system, sans-serif", FontFamilies.SANS)
        assertEquals("'Fraunces', Georgia, serif", FontFamilies.SERIF)
        assertEquals(FontFamilies.MONO, FontFamilies.DISPLAY)
        assertEquals(FontFamilies.SANS, FontFamilies.BODY)
    }

    @Test
    fun `given Typography hero, when values are read, then they match handoff CSS`() {
        // Arrange / Act
        val style = Typography.hero

        // Assert
        assertEquals(FontFamilies.DISPLAY, style.family)
        assertEquals(expected = 5.5, actual = style.size.value.toDouble(), absoluteTolerance = TOLERANCE)
        assertEquals(FontWeight.Medium, style.weight)
        assertEquals(expected = 0.95, actual = style.lineHeight, absoluteTolerance = TOLERANCE)
        val letterSpacing = assertNotNull(style.letterSpacing)
        assertEquals(expected = -0.03, actual = letterSpacing.value.toDouble(), absoluteTolerance = TOLERANCE)
        assertNull(style.transform)
    }

    @Test
    fun `given Typography h1, when values are read, then they match handoff CSS`() {
        // Arrange / Act
        val style = Typography.h1

        // Assert
        assertEquals(FontFamilies.DISPLAY, style.family)
        assertEquals(expected = 4.25, actual = style.size.value.toDouble(), absoluteTolerance = TOLERANCE)
        assertEquals(FontWeight.Medium, style.weight)
        assertEquals(expected = 1.05, actual = style.lineHeight, absoluteTolerance = TOLERANCE)
        val letterSpacing = assertNotNull(style.letterSpacing)
        assertEquals(expected = -0.025, actual = letterSpacing.value.toDouble(), absoluteTolerance = TOLERANCE)
        assertNull(style.transform)
    }

    @Test
    fun `given Typography h2, when values are read, then they match handoff CSS`() {
        // Arrange / Act
        val style = Typography.h2

        // Assert
        assertEquals(FontFamilies.DISPLAY, style.family)
        assertEquals(expected = 1.75, actual = style.size.value.toDouble(), absoluteTolerance = TOLERANCE)
        assertEquals(FontWeight.Medium, style.weight)
        assertEquals(expected = 1.15, actual = style.lineHeight, absoluteTolerance = TOLERANCE)
        val letterSpacing = assertNotNull(style.letterSpacing)
        assertEquals(expected = -0.02, actual = letterSpacing.value.toDouble(), absoluteTolerance = TOLERANCE)
        assertNull(style.transform)
    }

    @Test
    fun `given Typography h3Display, when values are read, then they match handoff CSS`() {
        // Arrange / Act
        val style = Typography.h3Display

        // Assert
        assertEquals(FontFamilies.DISPLAY, style.family)
        assertEquals(expected = 1.625, actual = style.size.value.toDouble(), absoluteTolerance = TOLERANCE)
        assertEquals(FontWeight.Medium, style.weight)
        assertEquals(expected = 1.2, actual = style.lineHeight, absoluteTolerance = TOLERANCE)
        val letterSpacing = assertNotNull(style.letterSpacing)
        assertEquals(expected = -0.02, actual = letterSpacing.value.toDouble(), absoluteTolerance = TOLERANCE)
        assertNull(style.transform)
    }

    @Test
    fun `given Typography bodyLarge, when values are read, then they match handoff CSS`() {
        // Arrange / Act
        val style = Typography.bodyLarge

        // Assert
        assertEquals(FontFamilies.BODY, style.family)
        assertEquals(expected = 1.0625, actual = style.size.value.toDouble(), absoluteTolerance = TOLERANCE)
        assertEquals(FontWeight.Normal, style.weight)
        assertEquals(expected = 1.55, actual = style.lineHeight, absoluteTolerance = TOLERANCE)
        assertNull(style.letterSpacing)
        assertNull(style.transform)
    }

    @Test
    fun `given Typography body, when values are read, then they match handoff CSS`() {
        // Arrange / Act
        val style = Typography.body

        // Assert
        assertEquals(FontFamilies.BODY, style.family)
        assertEquals(expected = 1.0, actual = style.size.value.toDouble(), absoluteTolerance = TOLERANCE)
        assertEquals(FontWeight.Normal, style.weight)
        assertEquals(expected = 1.55, actual = style.lineHeight, absoluteTolerance = TOLERANCE)
        assertNull(style.letterSpacing)
        assertNull(style.transform)
    }

    @Test
    fun `given Typography bodySmall, when values are read, then they match handoff CSS`() {
        // Arrange / Act
        val style = Typography.bodySmall

        // Assert
        assertEquals(FontFamilies.BODY, style.family)
        assertEquals(expected = 0.875, actual = style.size.value.toDouble(), absoluteTolerance = TOLERANCE)
        assertEquals(FontWeight.Normal, style.weight)
        assertEquals(expected = 1.55, actual = style.lineHeight, absoluteTolerance = TOLERANCE)
        assertNull(style.letterSpacing)
        assertNull(style.transform)
    }

    @Test
    fun `given Typography serifLead, when values are read, then they match handoff CSS`() {
        // Arrange / Act
        val style = Typography.serifLead

        // Assert
        assertEquals(FontFamilies.SERIF, style.family)
        assertEquals(expected = 1.25, actual = style.size.value.toDouble(), absoluteTolerance = TOLERANCE)
        assertEquals(FontWeight.Normal, style.weight)
        assertEquals(expected = 1.5, actual = style.lineHeight, absoluteTolerance = TOLERANCE)
        assertNull(style.letterSpacing)
        assertNull(style.transform)
    }

    @Test
    fun `given Typography serifBody, when values are read, then they match handoff CSS`() {
        // Arrange / Act
        val style = Typography.serifBody

        // Assert
        assertEquals(FontFamilies.SERIF, style.family)
        assertEquals(expected = 1.0625, actual = style.size.value.toDouble(), absoluteTolerance = TOLERANCE)
        assertEquals(FontWeight.Normal, style.weight)
        assertEquals(expected = 1.6, actual = style.lineHeight, absoluteTolerance = TOLERANCE)
        assertNull(style.letterSpacing)
        assertNull(style.transform)
    }

    @Test
    fun `given Typography metaLabel, when values are read, then they match handoff CSS`() {
        // Arrange / Act
        val style = Typography.metaLabel

        // Assert
        assertEquals(FontFamilies.MONO, style.family)
        assertEquals(expected = 0.75, actual = style.size.value.toDouble(), absoluteTolerance = TOLERANCE)
        assertEquals(FontWeight.Medium, style.weight)
        assertEquals(expected = 1.4, actual = style.lineHeight, absoluteTolerance = TOLERANCE)
        val letterSpacing = assertNotNull(style.letterSpacing)
        assertEquals(expected = 0.02, actual = letterSpacing.value.toDouble(), absoluteTolerance = TOLERANCE)
        assertEquals(TextTransform.Uppercase, style.transform)
    }

    @Test
    fun `given Typography metaSmall, when values are read, then they match handoff CSS`() {
        // Arrange / Act
        val style = Typography.metaSmall

        // Assert
        assertEquals(FontFamilies.MONO, style.family)
        assertEquals(expected = 0.6875, actual = style.size.value.toDouble(), absoluteTolerance = TOLERANCE)
        assertEquals(FontWeight.Medium, style.weight)
        assertEquals(expected = 1.4, actual = style.lineHeight, absoluteTolerance = TOLERANCE)
        val letterSpacing = assertNotNull(style.letterSpacing)
        assertEquals(expected = 0.06, actual = letterSpacing.value.toDouble(), absoluteTolerance = TOLERANCE)
        assertEquals(TextTransform.Uppercase, style.transform)
    }

    @Test
    fun `given Typography chip, when values are read, then they match handoff CSS`() {
        // Arrange / Act
        val style = Typography.chip

        // Assert
        assertEquals(FontFamilies.MONO, style.family)
        assertEquals(expected = 0.6875, actual = style.size.value.toDouble(), absoluteTolerance = TOLERANCE)
        assertEquals(FontWeight.Medium, style.weight)
        assertEquals(expected = 1.4, actual = style.lineHeight, absoluteTolerance = TOLERANCE)
        assertNull(style.letterSpacing)
        assertEquals(TextTransform.Uppercase, style.transform)
    }

    @Test
    fun `given Typography code, when values are read, then they match handoff CSS`() {
        // Arrange / Act
        val style = Typography.code

        // Assert
        assertEquals(FontFamilies.MONO, style.family)
        assertEquals(expected = 0.8125, actual = style.size.value.toDouble(), absoluteTolerance = TOLERANCE)
        assertEquals(FontWeight.Normal, style.weight)
        assertEquals(expected = 1.6, actual = style.lineHeight, absoluteTolerance = TOLERANCE)
        assertNull(style.letterSpacing)
        assertNull(style.transform)
    }
}
