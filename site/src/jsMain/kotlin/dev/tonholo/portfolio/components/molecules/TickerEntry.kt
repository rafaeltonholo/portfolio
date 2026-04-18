package dev.tonholo.portfolio.components.molecules

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.domain.model.TickerItem
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.typography.FontFamilies
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text as ComposeText

// Typography is inline in the handoff JSX (`Ticker` component): mono font,
// 12px body with 10px gap between label and value.
private const val FONT_SIZE_REM = 0.75 // 12px
private const val INNER_GAP_REM = 0.625 // 10px

// Handoff separator between label and value: " - " (regular hyphen, not em dash).
private const val LABEL_VALUE_SEPARATOR = " - "

/**
 * A single entry in the top-of-page ticker bar. Renders an inline row of
 * `<b>label</b> - <span>value</span>` matching the `.ticker` item block in
 * the handoff JSX (`Ticker` component in `chrome.jsx`).
 *
 * @param item the [TickerItem] domain model providing `label` and `value`.
 * @param modifier optional [Modifier] chained after the base ticker-entry styles.
 */
@Composable
fun TickerEntry(
    item: TickerItem,
    modifier: Modifier = Modifier,
) {
    val tokens = LocalPortfolioTokens.current

    Div(attrs = TickerEntryStyle.toModifier().then(modifier).toAttrs()) {
        val labelModifier = Modifier
            .color(tokens.accent.accent)
            .fontWeight(FontWeight.SemiBold)
        B(attrs = labelModifier.toAttrs()) {
            ComposeText(item.label)
        }
        Span {
            ComposeText(LABEL_VALUE_SEPARATOR + item.value)
        }
    }
}

/**
 * Base `CssStyle` for [TickerEntry]. Matches the inline style on each ticker
 * item `<div>` in the handoff: inline-flex row, centered, with a 10px gap
 * between the bold label and the value. Color of the value is inherited from
 * the parent ticker strip (`tokens.ink.ink3`); the label overrides it inline.
 */
val TickerEntryStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.LegacyInlineFlex)
            .alignItems(AlignItems.Center)
            .gap(INNER_GAP_REM.cssRem)
            .fontFamily(FontFamilies.MONO)
            .fontSize(FONT_SIZE_REM.cssRem)
    }
}
