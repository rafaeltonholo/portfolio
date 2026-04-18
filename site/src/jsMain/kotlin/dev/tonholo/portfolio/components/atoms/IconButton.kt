package dev.tonholo.portfolio.components.atoms

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.justifyContent
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.selectors.hover
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.color.Ink
import dev.tonholo.portfolio.theme.color.Paper
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.dom.Button as WebButton

private const val ICON_BUTTON_SIZE_REM = 2.0 // 32px
private const val ICON_BUTTON_BORDER_RADIUS_REM = 0.25 // 4px

private val TransparentColor: Color = Color.rgba(r = 0, g = 0, b = 0, a = 0f)

/**
 * Square icon-only button. Maps to `.icon-btn` in the handoff CSS.
 *
 * Renders a transparent 32x32 button that hosts whatever composable content
 * the caller places in [content] (usually an `<svg>` glyph). On hover the
 * background becomes `paper-2` and the color becomes `ink`.
 *
 * @param modifier optional [Modifier] chained after the base styles.
 * @param ariaLabel accessibility label, required since the button has no
 *   visible text.
 * @param onClick optional click handler.
 * @param content slot for the inner icon; receives no parameters.
 */
@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    ariaLabel: String? = null,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val tokens = LocalPortfolioTokens.current

    var finalModifier = IconButtonStyle
        .toModifier()
        .color(tokens.ink.ink3)
    if (onClick != null) {
        finalModifier = finalModifier.onClick { onClick() }
    }
    finalModifier = finalModifier.then(modifier)

    WebButton(
        attrs = finalModifier.toAttrs {
            if (ariaLabel != null) {
                attr("aria-label", ariaLabel)
            }
            attr("type", "button")
        },
    ) {
        content()
    }
}

/**
 * Base `CssStyle` for [IconButton]. Ports `.icon-btn` exactly: 32x32 square,
 * transparent fill, no border, radius 4px, hover swaps to `paper-2 / ink`.
 */
val IconButtonStyle = CssStyle {
    base {
        Modifier
            .width(ICON_BUTTON_SIZE_REM.cssRem)
            .height(ICON_BUTTON_SIZE_REM.cssRem)
            .display(DisplayStyle.LegacyInlineFlex)
            .alignItems(AlignItems.Center)
            .justifyContent(JustifyContent.Center)
            .borderRadius(ICON_BUTTON_BORDER_RADIUS_REM.cssRem)
            .backgroundColor(TransparentColor)
            .border(style = LineStyle.None)
            .cursor(Cursor.Pointer)
    }
    hover {
        val isDark = colorMode == ColorMode.DARK
        val paper = if (isDark) Paper.dark else Paper.light
        val ink = if (isDark) Ink.dark else Ink.light
        Modifier
            .backgroundColor(paper.paper2)
            .color(ink.ink)
    }
}
