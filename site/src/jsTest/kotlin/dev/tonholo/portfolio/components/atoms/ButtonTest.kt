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

class ButtonTest {
    private val tokens = PortfolioTokens(
        ink = Ink.light,
        paper = Paper.light,
        muted = Muted.light,
        accent = AccentMint.light,
        syntax = Syntax.light,
        typography = Typography,
    )

    @Test
    fun `given ButtonVariant Solid, when background is resolved, then it returns ink`() {
        // Arrange
        val variant = ButtonVariant.Solid

        // Act
        val background: Color = variant.background(tokens = tokens)

        // Assert
        assertEquals(tokens.ink.ink, background)
    }

    @Test
    fun `given ButtonVariant Solid, when foreground is resolved, then it returns paper`() {
        assertEquals(tokens.paper.paper, ButtonVariant.Solid.foreground(tokens = tokens))
    }

    @Test
    fun `given ButtonVariant Solid, when borderColor is resolved, then it returns ink`() {
        assertEquals(tokens.ink.ink, ButtonVariant.Solid.borderColor(tokens = tokens))
    }

    @Test
    fun `given ButtonVariant Accent, when background is resolved, then it returns accent`() {
        assertEquals(tokens.accent.accent, ButtonVariant.Accent.background(tokens = tokens))
    }

    @Test
    fun `given ButtonVariant Accent, when foreground is resolved, then it returns accentInk`() {
        assertEquals(tokens.accent.accentInk, ButtonVariant.Accent.foreground(tokens = tokens))
    }

    @Test
    fun `given ButtonVariant Accent, when borderColor is resolved, then it returns accent`() {
        assertEquals(tokens.accent.accent, ButtonVariant.Accent.borderColor(tokens = tokens))
    }

    @Test
    fun `given ButtonVariant Ghost, when background is resolved, then it returns transparent`() {
        assertEquals(Color.rgba(r = 0, g = 0, b = 0, a = 0f), ButtonVariant.Ghost.background(tokens = tokens))
    }

    @Test
    fun `given ButtonVariant Ghost, when foreground is resolved, then it returns ink`() {
        assertEquals(tokens.ink.ink, ButtonVariant.Ghost.foreground(tokens = tokens))
    }

    @Test
    fun `given ButtonVariant Ghost, when borderColor is resolved, then it returns ink4`() {
        assertEquals(tokens.ink.ink4, ButtonVariant.Ghost.borderColor(tokens = tokens))
    }

    @Test
    fun `given ButtonVariant Inverse, when background is resolved, then it returns paper`() {
        assertEquals(tokens.paper.paper, ButtonVariant.Inverse.background(tokens = tokens))
    }

    @Test
    fun `given ButtonVariant Inverse, when foreground is resolved, then it returns ink`() {
        assertEquals(tokens.ink.ink, ButtonVariant.Inverse.foreground(tokens = tokens))
    }

    @Test
    fun `given ButtonVariant Inverse, when borderColor is resolved, then it returns paper`() {
        assertEquals(tokens.paper.paper, ButtonVariant.Inverse.borderColor(tokens = tokens))
    }

    @Test
    fun `given ButtonStyle is a val, when referenced, then it is non-null`() {
        assertNotNull(ButtonStyle)
    }
}
