package dev.tonholo.portfolio.components.atoms

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.PortfolioTokens
import dev.tonholo.portfolio.theme.layout.unit.dp
import dev.tonholo.portfolio.theme.typography.Typography
import dev.tonholo.portfolio.theme.typography.apply
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text as ComposeText

private val CHIP_BORDER_RADIUS = 2.dp
private val CHIP_PADDING_Y = 4.dp
private val CHIP_PADDING_X = 10.dp
private val CHIP_GAP = 6.dp
private val CHIP_BORDER_WIDTH = 1.dp

/**
 * Inline-flex pill tag, mono uppercase typography with a hairline border.
 * Maps to `.chip` in the handoff CSS. Variants swap only the fill / text
 * color pair via [ChipVariant].
 *
 * @param text body copy rendered inside the chip.
 * @param modifier optional [Modifier] chained after the base styles.
 * @param variant color pairing, defaults to [ChipVariant.Default].
 */
@Composable
fun Chip(text: String, modifier: Modifier = Modifier, variant: ChipVariant = ChipVariant.Default) {
    val tokens = LocalPortfolioTokens.current
    val background = variant.background(tokens = tokens)
    val foreground = variant.foreground(tokens = tokens)

    val finalModifier = ChipStyle
        .toModifier()
        .apply(Typography.chip)
        .backgroundColor(background)
        .color(foreground)
        .border(
            width = CHIP_BORDER_WIDTH,
            style = LineStyle.Solid,
            color = tokens.paper.paper3,
        )
        .then(modifier)

    Span(attrs = finalModifier.toAttrs()) {
        ComposeText(text)
    }
}

/**
 * Two-axis color variant for [Chip]. Each variant resolves a background and
 * a foreground color from the active [PortfolioTokens] snapshot.
 */
enum class ChipVariant {
    Default,
    Accent,
    ;

    /** Background color of the chip for this variant. */
    fun background(tokens: PortfolioTokens): Color = when (this) {
        Default -> tokens.paper.paper2
        Accent -> tokens.accent.accentSoft
    }

    /** Foreground (text) color of the chip for this variant. */
    fun foreground(tokens: PortfolioTokens): Color = when (this) {
        Default -> tokens.ink.ink3
        Accent -> tokens.accent.accentInk
    }
}

/**
 * Base `CssStyle` for [Chip]: sets the flex layout, radius, and spacing.
 * Color and typography are applied inline by the composable so they can
 * react to the active [PortfolioTokens] snapshot.
 */
val ChipStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.LegacyInlineFlex)
            .alignItems(AlignItems.Center)
            .gap(CHIP_GAP)
            .padding(topBottom = CHIP_PADDING_Y, leftRight = CHIP_PADDING_X)
            .borderRadius(CHIP_BORDER_RADIUS)
    }
}
