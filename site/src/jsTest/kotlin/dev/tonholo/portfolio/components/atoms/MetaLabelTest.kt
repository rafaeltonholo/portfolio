package dev.tonholo.portfolio.components.atoms

import com.varabyte.kobweb.compose.ui.graphics.Color
import dev.tonholo.portfolio.theme.PortfolioTokens
import dev.tonholo.portfolio.theme.color.AccentMint
import dev.tonholo.portfolio.theme.color.Ink
import dev.tonholo.portfolio.theme.color.Muted
import dev.tonholo.portfolio.theme.color.Paper
import dev.tonholo.portfolio.theme.layout.Density
import dev.tonholo.portfolio.theme.layout.SpacingScale
import dev.tonholo.portfolio.theme.typography.Typography
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class MetaLabelTest {
    private val tokens = PortfolioTokens(
        ink = Ink.light,
        paper = Paper.light,
        muted = Muted.light,
        accent = AccentMint.light,
        spacing = SpacingScale.from(density = Density.Compact),
        typography = Typography,
    )

    @Test
    fun `given MetaLabelVariant Default, when color is resolved, then it returns muted`() {
        // Arrange
        val variant = MetaLabelVariant.Default

        // Act
        val color: Color = variant.color(tokens = tokens)

        // Assert
        assertEquals(tokens.muted.muted, color)
    }

    @Test
    fun `given MetaLabelVariant Accent, when color is resolved, then it returns accent`() {
        // Arrange
        val variant = MetaLabelVariant.Accent

        // Act
        val color: Color = variant.color(tokens = tokens)

        // Assert
        assertEquals(tokens.accent.accent, color)
    }

    @Test
    fun `given MetaLabelStyle is a val, when referenced, then it is non-null`() {
        assertNotNull(MetaLabelStyle)
    }
}
