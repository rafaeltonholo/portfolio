package dev.tonholo.portfolio.components.organisms.home

import com.varabyte.kobweb.compose.ui.graphics.Color
import dev.tonholo.portfolio.theme.color.AccentMint
import dev.tonholo.portfolio.theme.color.Ink
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HiringCtaTest {
    @Test
    fun `given dark mode, when eyebrow color is resolved, then it has AAA contrast on light paper`() {
        // Arrange / Act
        val actualColor: Color = resolveEyebrowColor(isDark = true)
        val expectedColor: Color = Color.rgb(0x135328)

        // Assert
        assertEquals(expectedColor, actualColor)
        val surfaceColor: Color = Ink.dark.ink
        val contrastRatio: Double = calculateContrastRatio(foreground = actualColor, background = surfaceColor)
        assertTrue(contrastRatio >= 7.0, "Expected AAA contrast (>= 7.0), but was $contrastRatio")
    }

    @Test
    fun `given light mode, when eyebrow color is resolved, then it has AAA contrast on dark ink`() {
        // Arrange / Act
        val actualColor: Color = resolveEyebrowColor(isDark = false)
        val expectedColor: Color = AccentMint.dark.accent

        // Assert
        assertEquals(expectedColor, actualColor)
        val surfaceColor: Color = Ink.light.ink
        val contrastRatio: Double = calculateContrastRatio(foreground = actualColor, background = surfaceColor)
        assertTrue(contrastRatio >= 7.0, "Expected AAA contrast (>= 7.0), but was $contrastRatio")
    }

    @Test
    fun `given dark mode, when ghost border is resolved, then it uses dark surface border`() {
        // Arrange / Act
        val actualColor: Color = resolveGhostBorderColor(isDark = true)
        val expectedColor: Color = Color.rgba(r = 21, g = 21, b = 21, a = 0.2f)

        // Assert
        assertEquals(expectedColor, actualColor)
    }

    @Test
    fun `given light mode, when ghost border is resolved, then it uses light surface border`() {
        // Arrange / Act
        val actualColor: Color = resolveGhostBorderColor(isDark = false)
        val expectedColor: Color = Color.rgba(r = 255, g = 255, b = 255, a = 0.2f)

        // Assert
        assertEquals(expectedColor, actualColor)
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
