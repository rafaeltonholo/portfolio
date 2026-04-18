package dev.tonholo.portfolio.components.molecules

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.components.atoms.Divider
import dev.tonholo.portfolio.components.atoms.DividerVariant
import dev.tonholo.portfolio.components.atoms.MetaLabel
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.typography.Typography
import dev.tonholo.portfolio.theme.typography.apply
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Text as ComposeText

private const val SECTION_HEAD_GAP_REM = 1.5 // 24px
private const val SECTION_HEAD_MARGIN_BOTTOM_REM = 2.25 // gap * 1.5
private const val SECTION_HEAD_NUM_COL_REM = 4.5 // 72px numeric column
private const val SECTION_HEAD_RULE_COL_FR = 1 // 1fr stretch for the rule

/**
 * Section header used across Home, Resume, and feature pages. Maps to
 * `.section-head` in the handoff CSS: a four-column grid of a numeric
 * marker (`// 01`), the section `h2`, a stretching rule, and an optional
 * trailing slot.
 *
 * @param number the mono marker copy, e.g. `"// 01"`.
 * @param title the `h2` copy.
 * @param modifier optional [Modifier] chained after the base styles.
 * @param trailing optional composable rendered in the last grid column; use
 *   this for meta chips like `week 15 - 2026` or `most recent first`.
 */
@Composable
fun SectionHead(
    number: String,
    title: String,
    modifier: Modifier = Modifier,
    trailing: (@Composable () -> Unit)? = null,
) {
    val tokens = LocalPortfolioTokens.current

    val finalModifier = SectionHeadStyle
        .toModifier()
        .then(modifier)

    Div(attrs = finalModifier.toAttrs()) {
        MetaLabel(text = number)
        H2(
            attrs = Modifier
                .apply(Typography.h2)
                .color(tokens.ink.ink)
                .margin(topBottom = 0.cssRem)
                .toAttrs(),
        ) {
            ComposeText(title)
        }
        Divider(variant = DividerVariant.Flex1)
        if (trailing != null) {
            trailing()
        } else {
            // Reserve the trailing grid cell so the stretching rule keeps the
            // same width whether a meta chip is supplied or not.
            Div(attrs = Modifier.toAttrs())
        }
    }
}

/**
 * Base `CssStyle` for [SectionHead]. Ports `.section-head` from the handoff:
 * a four-column grid with a 72px numeric column, an auto title column, a
 * flex-growing rule column, and an auto trailing column. 24px gap between
 * columns; 36px bottom margin (`gap * 1.5` in the handoff).
 */
val SectionHeadStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Grid)
            .gridTemplateColumns {
                size(SECTION_HEAD_NUM_COL_REM.cssRem)
                minmax(min = minContent, max = auto)
                size(SECTION_HEAD_RULE_COL_FR.fr)
                size(auto)
            }
            .gap(SECTION_HEAD_GAP_REM.cssRem)
            .alignItems(AlignItems.Baseline)
            .margin(bottom = SECTION_HEAD_MARGIN_BOTTOM_REM.cssRem)
    }
}
