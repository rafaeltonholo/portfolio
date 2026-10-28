package dev.tonholo.portfolio.components.atoms

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.left
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.modifiers.top
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.navigation.UncoloredLinkVariant
import com.varabyte.kobweb.silk.components.navigation.UndecoratedLinkVariant
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.selectors.focus
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.theme.color.AccentMint
import dev.tonholo.portfolio.theme.layout.unit.dp
import dev.tonholo.portfolio.theme.typography.FontFamilies
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.px

/**
 * Default target anchor used by [SkipLink]. Pages that want a skip-link to
 * work must render an element with this id at the start of their meaningful
 * content so the anchor resolves the browser-jump.
 */
const val SKIP_LINK_TARGET_ID = "main-content"

private const val SKIP_LINK_LABEL_EN = "Skip to main content"
private val SKIP_LINK_FONT_SIZE = 13.dp
private val SKIP_LINK_PADDING_Y = 8.dp
private val SKIP_LINK_PADDING_X = 16.dp
private val SKIP_LINK_BORDER_RADIUS = 4.dp
private const val SKIP_LINK_Z_INDEX = 100

// Off-screen x-position used to hide the link until focus moves to it.
private const val SKIP_LINK_HIDDEN_LEFT_PX = -9999

/**
 * WCAG 2.4.1 "Bypass Blocks" skip link. Invisible by default; on keyboard
 * focus it paints an accent-colored pill at the top-left of the viewport
 * that jumps past the site chrome to the element with id [SKIP_LINK_TARGET_ID].
 *
 * Label stays in English for v1 because the component is primarily served
 * to screen-reader / keyboard users who expect a predictable anchor; if
 * translation becomes a requirement, hoist the label into [Strings].
 */
@Composable
fun SkipLink(modifier: Modifier = Modifier) {
    Link(
        path = "#$SKIP_LINK_TARGET_ID",
        modifier = SkipLinkStyle.toModifier().then(modifier),
        variant = UndecoratedLinkVariant then UncoloredLinkVariant,
    ) {
        org.jetbrains.compose.web.dom.Text(value = SKIP_LINK_LABEL_EN)
    }
}

/**
 * Base `CssStyle` for [SkipLink]. Hidden off-screen at rest; on `:focus-visible`
 * it springs to the top-left corner as a solid accent pill so keyboard users
 * can activate it before tabbing through the nav.
 */
val SkipLinkStyle = CssStyle {
    base {
        Modifier
            .position(Position.Absolute)
            .left(SKIP_LINK_HIDDEN_LEFT_PX.px)
            .top(0.px)
            .zIndex(SKIP_LINK_Z_INDEX)
            .fontFamily(*FontFamilies.MONO)
            .fontSize(SKIP_LINK_FONT_SIZE)
            .fontWeight(FontWeight.SemiBold)
            .padding(
                topBottom = SKIP_LINK_PADDING_Y,
                leftRight = SKIP_LINK_PADDING_X,
            )
            .borderRadius(SKIP_LINK_BORDER_RADIUS)
            .textDecorationLine(TextDecorationLine.None)
    }
    focus {
        val isDark = colorMode == ColorMode.DARK
        val accent = if (isDark) AccentMint.dark else AccentMint.light
        Modifier
            .left(8.px)
            .top(8.px)
            .backgroundColor(accent.accent)
            .color(accent.accentInk)
    }
}
