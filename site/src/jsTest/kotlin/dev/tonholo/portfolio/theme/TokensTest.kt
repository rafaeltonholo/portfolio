package dev.tonholo.portfolio.theme

import com.varabyte.kobweb.compose.ui.graphics.Color
import dev.tonholo.portfolio.theme.color.AccentMint
import dev.tonholo.portfolio.theme.color.Ink
import dev.tonholo.portfolio.theme.color.Muted
import dev.tonholo.portfolio.theme.color.Paper
import dev.tonholo.portfolio.theme.color.Syntax
import dev.tonholo.portfolio.theme.layout.Density
import dev.tonholo.portfolio.theme.layout.SpacingScale
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TokensTest {
    @Test
    fun `given Ink light, when color values are read, then they match handoff CSS`() {
        // Arrange / Act
        val scale = Ink.light

        // Assert
        assertEquals(Color.rgb(0x141311), scale.ink)
        assertEquals(Color.rgb(0x1D1B17), scale.ink2)
        assertEquals(Color.rgb(0x2A2824), scale.ink3)
        assertEquals(Color.rgb(0x3A3833), scale.ink4)
    }

    @Test
    fun `given Ink dark, when color values are read, then they match handoff CSS`() {
        // Arrange / Act
        val scale = Ink.dark

        // Assert
        assertEquals(Color.rgb(0xF2EFE6), scale.ink)
        assertEquals(Color.rgb(0xE4E0D3), scale.ink2)
        assertEquals(Color.rgb(0xB8B3A5), scale.ink3)
        assertEquals(Color.rgb(0x7E7A70), scale.ink4)
    }

    @Test
    fun `given Paper light, when color values are read, then they match handoff CSS`() {
        // Arrange / Act
        val scale = Paper.light

        // Assert
        assertEquals(Color.rgb(0xF7F5EF), scale.paper)
        assertEquals(Color.rgb(0xEFECE3), scale.paper2)
        assertEquals(Color.rgb(0xE5E1D4), scale.paper3)
    }

    @Test
    fun `given Paper dark, when color values are read, then they match handoff CSS`() {
        // Arrange / Act
        val scale = Paper.dark

        // Assert
        assertEquals(Color.rgb(0x15130F), scale.paper)
        assertEquals(Color.rgb(0x1D1B16), scale.paper2)
        assertEquals(Color.rgb(0x25231D), scale.paper3)
    }

    @Test
    fun `given Muted light, when color values are read, then they match handoff CSS`() {
        // Arrange / Act
        val scale = Muted.light

        // Assert
        assertEquals(Color.rgb(0x6B675E), scale.muted)
        assertEquals(Color.rgb(0x8A857A), scale.muted2)
    }

    @Test
    fun `given Muted dark, when color values are read, then they match handoff CSS`() {
        // Arrange / Act
        val scale = Muted.dark

        // Assert
        assertEquals(Color.rgb(0x8A857A), scale.muted)
        assertEquals(Color.rgb(0x6B675E), scale.muted2)
    }

    @Test
    fun `given AccentMint light, when color values are read, then they meet WCAG AA on paper`() {
        // Arrange / Act
        val swatch = AccentMint.light

        // Assert - accent is darkened from the handoff #3D8A54 to pass 4.5:1
        // on paper; the soft and ink pairings keep the handoff values so the
        // filled-chip treatment still matches.
        assertEquals(Color.rgb(0x2F7A45), swatch.accent)
        assertEquals(Color.rgb(0xC8E5CF), swatch.accentSoft)
        assertEquals(Color.rgb(0xF2F7F4), swatch.accentInk)
    }

    @Test
    fun `given AccentMint dark, when color values are read, then they match handoff CSS`() {
        // Arrange / Act
        val swatch = AccentMint.dark

        // Assert
        assertEquals(Color.rgb(0x7DD198), swatch.accent)
        assertEquals(Color.rgb(0x1D4028), swatch.accentSoft)
        assertEquals(Color.rgb(0x051A0C), swatch.accentInk)
    }

    @Test
    fun `given Syntax light, when color values are read, then they match handoff term palette`() {
        // Arrange / Act
        val scale = Syntax.light

        // Assert - light-mode page -> dark terminal card -> lighter hues.
        assertEquals(Color.rgb(0xB99CF7), scale.keyword)
        assertEquals(Color.rgb(0xA3D9A5), scale.string)
        assertEquals(Color.rgb(0xF2C078), scale.number)
    }

    @Test
    fun `given Syntax dark, when color values are read, then they match handoff term palette`() {
        // Arrange / Act
        val scale = Syntax.dark

        // Assert - dark-mode page -> paper-2 terminal card -> darker hues.
        assertEquals(Color.rgb(0x8B5CF6), scale.keyword)
        assertEquals(Color.rgb(0x3D8A4D), scale.string)
        assertEquals(Color.rgb(0xF2C078), scale.number)
    }

    @Test
    fun `given Density Compact, when multiplier is read, then it equals 0_72`() {
        // Arrange / Act
        val multiplier = Density.Compact.multiplier

        // Assert
        assertEquals(0.72, multiplier)
    }

    @Test
    fun `given Density Normal, when multiplier is read, then it equals 1_0`() {
        // Arrange / Act
        val multiplier = Density.Normal.multiplier

        // Assert
        assertEquals(1.0, multiplier)
    }

    @Test
    fun `given Density Spacious, when multiplier is read, then it equals 1_25`() {
        // Arrange / Act
        val multiplier = Density.Spacious.multiplier

        // Assert
        assertEquals(1.25, multiplier)
    }

    @Test
    fun `given Density Compact, when SpacingScale is created, then padY equals 3_6rem`() {
        // Arrange
        val density = Density.Compact

        // Act
        val scale = SpacingScale.from(density = density)

        // Assert - 5rem * 0.72 = 3.6rem
        assertEquals(3.6, scale.padY.value.toDouble(), absoluteTolerance = 0.001)
    }

    @Test
    fun `given Density Compact, when SpacingScale is created, then padYSmall equals 1_8rem`() {
        // Arrange
        val density = Density.Compact

        // Act
        val scale = SpacingScale.from(density = density)

        // Assert - 2.5rem * 0.72 = 1.8rem
        assertEquals(1.8, scale.padYSmall.value.toDouble(), absoluteTolerance = 0.001)
    }

    @Test
    fun `given Density Compact, when SpacingScale is created, then gap equals 1_08rem`() {
        // Arrange
        val density = Density.Compact

        // Act
        val scale = SpacingScale.from(density = density)

        // Assert - 1.5rem * 0.72 = 1.08rem
        assertEquals(1.08, scale.gap.value.toDouble(), absoluteTolerance = 0.001)
    }

    @Test
    fun `given Density Normal, when SpacingScale is created, then padY equals 5rem`() {
        // Arrange
        val density = Density.Normal

        // Act
        val scale = SpacingScale.from(density = density)

        // Assert
        assertEquals(5.0, scale.padY.value.toDouble(), absoluteTolerance = 0.001)
    }

    @Test
    fun `given Density Spacious, when SpacingScale is created, then padY equals 6_25rem`() {
        // Arrange
        val density = Density.Spacious

        // Act
        val scale = SpacingScale.from(density = density)

        // Assert - 5rem * 1.25 = 6.25rem
        assertEquals(6.25, scale.padY.value.toDouble(), absoluteTolerance = 0.001)
    }

    @Test
    fun `given LocalPortfolioTokens declaration, when accessed as a reference, then it is non-null`() {
        // Arrange / Act
        val local = LocalPortfolioTokens

        // Assert
        assertNotNull(local)
    }
}
