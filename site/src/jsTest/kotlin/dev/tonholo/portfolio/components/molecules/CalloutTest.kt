package dev.tonholo.portfolio.components.molecules

import com.varabyte.kobweb.compose.ui.graphics.Color
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CalloutTest {
    @Test
    fun `given all CalloutTypes, when inspected, then each has a valid Phosphor icon name`() {
        CalloutType.entries.forEach { type ->
            assertTrue(type.iconName.isNotBlank(), "Expected non-blank icon for $type")
            assertFalse(type.iconName.startsWith("ph-"), "Icon name should omit 'ph-' prefix for $type")
        }
    }

    @Test
    fun `given GitHub callout types, when default title is checked, then each matches GitHub alert title`() {
        assertEquals("Note", CalloutType.NOTE.defaultTitle)
        assertEquals("Tip", CalloutType.TIP.defaultTitle)
        assertEquals("Important", CalloutType.IMPORTANT.defaultTitle)
        assertEquals("Warning", CalloutType.WARNING.defaultTitle)
        assertEquals("Caution", CalloutType.CAUTION.defaultTitle)
        assertEquals("", CalloutType.QUOTE.defaultTitle)
    }

    @Test
    fun `given dark and light modes, when accent colors are resolved, then they meet contrast expectations`() {
        val lightPaper = Color.rgb(0xF7F5EF)
        val darkPaper = Color.rgb(0x15130F)

        CalloutType.entries.forEach { type ->
            val lightColor = type.resolveAccentColor(isDark = false)
            val darkColor = type.resolveAccentColor(isDark = true)

            val lightContrast = calculateContrastRatio(foreground = lightColor, background = lightPaper)
            val darkContrast = calculateContrastRatio(foreground = darkColor, background = darkPaper)

            assertTrue(lightContrast >= 4.0, "Expected strong contrast in light mode for $type, got $lightContrast")
            assertTrue(darkContrast >= 4.5, "Expected AA contrast (>= 4.5) in dark mode for $type, got $darkContrast")
        }
    }

    @Test
    fun `given dark and light modes, when background tint is resolved, then alpha is applied`() {
        CalloutType.entries.forEach { type ->
            val lightBg = type.resolveBackgroundColor(isDark = false)
            val darkBg = type.resolveBackgroundColor(isDark = true)

            assertEquals(0.06f, lightBg.toRgb().alphaf, 0.01f)
            assertEquals(0.12f, darkBg.toRgb().alphaf, 0.01f)
        }
    }

    private fun calculateContrastRatio(foreground: Color, background: Color): Double {
        val lum1: Double = calculateRelativeLuminance(color = foreground)
        val lum2: Double = calculateRelativeLuminance(color = background)
        val lighter: Double = max(lum1, lum2)
        val darker: Double = min(lum1, lum2)
        return (lighter + 0.05) / (darker + 0.05)
    }

    private fun calculateRelativeLuminance(color: Color): Double {
        val rgb = color.toRgb()
        val r: Double = transformComponent(component = rgb.redf.toDouble())
        val g: Double = transformComponent(component = rgb.greenf.toDouble())
        val b: Double = transformComponent(component = rgb.bluef.toDouble())
        return 0.2126 * r + 0.7152 * g + 0.0722 * b
    }

    private fun transformComponent(component: Double): Double = if (component <= 0.04045) {
        component / 12.92
    } else {
        ((component + 0.055) / 1.055).pow(2.4)
    }
}
