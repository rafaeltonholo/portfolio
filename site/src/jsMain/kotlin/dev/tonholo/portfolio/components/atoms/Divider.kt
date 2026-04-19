package dev.tonholo.portfolio.components.atoms

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignSelf
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.flex
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.layout.unit.dp
import org.jetbrains.compose.web.css.AlignSelf
import org.jetbrains.compose.web.dom.Div

private val DIVIDER_HEIGHT = 1.dp
private val DIVIDER_MARGIN_Y = 48.dp

/**
 * Thin horizontal rule. Maps to `.divider` / `.rule` from the handoff CSS:
 * 1px high, 48px vertical margin, colored with `paper-3` in light mode and
 * `paper-2` in dark mode. The [Flex1] variant drops the vertical margin and
 * fills the available inline space (used inside `.section-head` grids).
 *
 * @param modifier optional [Modifier] chained after the base styles.
 * @param variant [DividerVariant.Horizontal] (default) or [DividerVariant.Flex1].
 */
@Composable
fun Divider(modifier: Modifier = Modifier, variant: DividerVariant = DividerVariant.Horizontal) {
    val tokens = LocalPortfolioTokens.current
    val colorMode by ColorMode.currentState
    val isDark = colorMode == ColorMode.DARK
    val background = if (isDark) tokens.paper.paper2 else tokens.paper.paper3

    val variantModifier = when (variant) {
        DividerVariant.Horizontal -> {
            Modifier.margin(topBottom = DIVIDER_MARGIN_Y)
        }

        DividerVariant.Flex1 -> {
            Modifier
                .flex(value = 1)
                .alignSelf(AlignSelf.Center)
        }
    }

    val finalModifier = DividerStyle
        .toModifier()
        .backgroundColor(background)
        .then(variantModifier)
        .then(modifier)

    Div(attrs = finalModifier.toAttrs { attr("role", "separator") })
}

/**
 * Variant axis for [Divider].
 *
 * - [Horizontal]: the default `.divider`, stretches to 100% width with 48px
 *   vertical margin. Used between content blocks.
 * - [Flex1]: the `.section-head .rule`, drops the margin and takes `flex: 1`
 *   so it can sit inside a flex row as a filler rule.
 */
enum class DividerVariant {
    Horizontal,
    Flex1,
}

/**
 * Base `CssStyle` for [Divider]. Applies the 1px height. Background color is
 * applied inline by the composable so it reacts to the active color mode.
 */
val DividerStyle = CssStyle {
    base {
        Modifier.height(DIVIDER_HEIGHT)
    }
}
