package dev.tonholo.portfolio.components.molecules

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.borderBottom
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.components.atoms.Dot
import dev.tonholo.portfolio.components.atoms.DotVariant
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.layout.unit.dp
import dev.tonholo.portfolio.theme.typography.FontFamilies
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text as ComposeText

private const val DEFAULT_TERM_TITLE = "rafael@tonholo - zsh"

// Traffic-light colors from the handoff inline styles.
private const val DOT_COLOR_RED = 0xD75F5F
private const val DOT_COLOR_AMBER = 0xD7A75F
private const val DOT_COLOR_GREEN = 0x7DD198

// Terminal header dots are bigger than the nav-status dot (7px → 10px).
private val DOT_SIZE = 10.dp

// `.hero-c__term-head` layout.
private val HEADER_GAP = 10.dp
private val HEADER_PADDING_BOTTOM = 14.dp
private val HEADER_MARGIN_BOTTOM = 16.dp
private val HEADER_BORDER_WIDTH = 1.dp

// `.hero-c__term-head .title` typography.
private val TITLE_MARGIN_LEFT = 12.dp
private val TITLE_FONT_SIZE = 12.dp

// Border color mirrors `border-bottom: 1px solid #33312d` in the handoff CSS.
private const val HEADER_BORDER_COLOR = 0x33312D

/**
 * Traffic-light header for terminal-style cards. Maps to `.hero-c__term-head`
 * from the handoff CSS: three colored [Dot]s (red, amber, green) followed by
 * a monospace window title, separated from the card body by a 1px hairline
 * rule.
 *
 * Used at the top of terminal cards like `HeroC` and `Snippet`.
 *
 * @param modifier optional [Modifier] chained after the base styles.
 * @param title the window title rendered beside the dots; defaults to
 *   `"rafael@tonholo - zsh"`.
 */
@Composable
fun TermWindowHeader(modifier: Modifier = Modifier, title: String = DEFAULT_TERM_TITLE) {
    val tokens = LocalPortfolioTokens.current

    val finalModifier = TermWindowHeaderStyle
        .toModifier()
        .then(modifier)

    Div(attrs = finalModifier.toAttrs()) {
        val dotModifier = Modifier
            .width(DOT_SIZE)
            .height(DOT_SIZE)
        Dot(
            modifier = dotModifier,
            variant = DotVariant.Static,
            color = Color.rgb(DOT_COLOR_RED),
        )
        Dot(
            modifier = dotModifier,
            variant = DotVariant.Static,
            color = Color.rgb(DOT_COLOR_AMBER),
        )
        Dot(
            modifier = dotModifier,
            variant = DotVariant.Static,
            color = Color.rgb(DOT_COLOR_GREEN),
        )
        Span(
            attrs = Modifier
                .margin(left = TITLE_MARGIN_LEFT)
                .fontFamily(*FontFamilies.MONO)
                .fontSize(TITLE_FONT_SIZE)
                .color(tokens.muted.muted2)
                .toAttrs(),
        ) {
            ComposeText(title)
        }
    }
}

/**
 * Base `CssStyle` for [TermWindowHeader]. Ports `.hero-c__term-head` from the
 * handoff: a flex row with a 10px gap between the traffic-light dots, a 14px
 * bottom padding, a 16px bottom margin, and a 1px hairline rule underneath.
 */
val TermWindowHeaderStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Flex)
            .alignItems(AlignItems.Center)
            .gap(HEADER_GAP)
            .padding(bottom = HEADER_PADDING_BOTTOM)
            .margin(bottom = HEADER_MARGIN_BOTTOM)
            .borderBottom(
                width = HEADER_BORDER_WIDTH,
                style = LineStyle.Solid,
                color = Color.rgb(HEADER_BORDER_COLOR),
            )
    }
}
