package dev.tonholo.portfolio.components.organisms.chrome

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderBottom
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.borderTop
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.flexWrap
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.components.molecules.TickerEntry
import dev.tonholo.portfolio.state.StaticContent
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.layout.ShellStyle
import dev.tonholo.portfolio.theme.typography.FontFamilies
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexWrap
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span

// Handoff spec: `.ticker { padding: 14px 0 }`, inner `.shell` row
// `display: flex; gap: 40px; font-size: 12px; color: var(--ink-3)`.
// Separator dot: `width: 4px; height: 4px; border-radius: 50%;
// background: var(--muted-2)`.
private const val STRIP_PADDING_REM = 0.875 // 14px
private const val ROW_GAP_REM = 2.5 // 40px
private const val ROW_FONT_SIZE_REM = 0.75 // 12px
private const val BORDER_WIDTH_REM = 0.0625 // 1px hairline
private const val DOT_SIZE_REM = 0.25 // 4px
private const val DOT_RADIUS_PERCENT = 50.0

/**
 * Returns the hairline border color for the ticker strip, matching the handoff:
 * light theme uses `--paper-3`; dark theme overrides to `--paper-2`.
 */
@Composable
private fun tickerBorderColor(): Color {
    val tokens = LocalPortfolioTokens.current
    val colorMode = ColorMode.current
    return when (colorMode) {
        ColorMode.LIGHT ->
            tokens.paper.paper3

        ColorMode.DARK ->
            tokens.paper.paper2
    }
}

/**
 * Chrome organism: the ticker strip that sits beneath the site nav. Ports the
 * `Ticker` component in the handoff `chrome.jsx`: a horizontal, shell-constrained
 * row of key/value entries in a muted mono voice, separated by small circular
 * dots. Wraps onto multiple lines on narrow viewports.
 *
 * Data is sourced directly from [StaticContent.TICKER]; this organism owns no
 * callbacks or external state.
 *
 * @param modifier optional [Modifier] chained after the base strip styles.
 */
@Composable
fun Ticker(modifier: Modifier = Modifier) {
    val tokens = LocalPortfolioTokens.current
    val borderColor = tickerBorderColor()
    val items = StaticContent.TICKER

    val stripModifier = TickerStyle
        .toModifier()
        .borderTop(
            width = BORDER_WIDTH_REM.cssRem,
            style = LineStyle.Solid,
            color = borderColor,
        )
        .borderBottom(
            width = BORDER_WIDTH_REM.cssRem,
            style = LineStyle.Solid,
            color = borderColor,
        )
        .then(modifier)

    Div(attrs = stripModifier.toAttrs()) {
        Div(
            attrs = ShellStyle
                .toModifier()
                .then(TickerTrackStyle.toModifier())
                .color(tokens.ink.ink3)
                .toAttrs(),
        ) {
            items.forEachIndexed { index, item ->
                TickerEntry(item = item)
                if (index < items.lastIndex) {
                    Span(
                        attrs = TickerDotStyle
                            .toModifier()
                            .backgroundColor(tokens.muted.muted2)
                            .toAttrs { attr("aria-hidden", "true") },
                    )
                }
            }
        }
    }
}

/**
 * Base `CssStyle` for the outer ticker strip. Ports `.ticker` from the handoff:
 * hairline rules top and bottom, 14px vertical padding, and hidden overflow so
 * the inner row cannot spill the shell.
 */
val TickerStyle = CssStyle {
    base {
        Modifier
            .padding(topBottom = STRIP_PADDING_REM.cssRem)
            .overflow(Overflow.Hidden)
    }
}

/**
 * Inner row inside the shell. Matches the inline `style` on the `.shell` div in
 * the handoff `Ticker`: flex row, 40px gap, wrapping, centered items, 12px mono
 * body copy. Color is applied inline so it resolves per active theme.
 */
val TickerTrackStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Flex)
            .flexWrap(FlexWrap.Wrap)
            .alignItems(AlignItems.Center)
            .gap(ROW_GAP_REM.cssRem)
            .fontFamily(*FontFamilies.MONO)
            .fontSize(ROW_FONT_SIZE_REM.cssRem)
    }
}

/**
 * 4px circular separator between ticker entries. Ports `.ticker-dot` from the
 * handoff CSS. Background is applied inline so the active `muted-2` token can
 * resolve per theme.
 */
val TickerDotStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.InlineBlock)
            .width(DOT_SIZE_REM.cssRem)
            .height(DOT_SIZE_REM.cssRem)
            .borderRadius(DOT_RADIUS_PERCENT.percent)
    }
}
