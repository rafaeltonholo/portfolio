package dev.tonholo.portfolio.components.atoms

import com.varabyte.kobweb.compose.ui.graphics.Color
import dev.tonholo.portfolio.theme.PortfolioTokens
import dev.tonholo.portfolio.theme.color.AccentMint
import dev.tonholo.portfolio.theme.color.Ink
import dev.tonholo.portfolio.theme.color.Muted
import dev.tonholo.portfolio.theme.color.Paper
import dev.tonholo.portfolio.theme.color.Syntax
import dev.tonholo.portfolio.theme.typography.Typography
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ChipTest {
    private val tokens = PortfolioTokens(
        ink = Ink.light,
        paper = Paper.light,
        muted = Muted.light,
        accent = AccentMint.light,
        syntax = Syntax.light,
        typography = Typography,
    )

    @Test
    fun `given ChipVariant Default, when background is resolved, then it returns paper2`() {
        // Arrange
        val variant = ChipVariant.Default

        // Act
        val background: Color = variant.background(tokens = tokens)

        // Assert
        assertEquals(tokens.paper.paper2, background)
    }

    @Test
    fun `given ChipVariant Default, when foreground is resolved, then it returns ink3`() {
        assertEquals(tokens.ink.ink3, ChipVariant.Default.foreground(tokens = tokens))
    }

    @Test
    fun `given ChipVariant Accent, when background is resolved, then it returns accentSoft`() {
        assertEquals(tokens.accent.accentSoft, ChipVariant.Accent.background(tokens = tokens))
    }

    @Test
    fun `given ChipVariant Accent, when foreground is resolved, then it returns accentInk`() {
        assertEquals(tokens.accent.accentInk, ChipVariant.Accent.foreground(tokens = tokens))
    }

    @Test
    fun `given ChipStyle is a val, when referenced, then it is non-null`() {
        assertNotNull(ChipStyle)
    }
}
