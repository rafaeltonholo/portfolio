package dev.tonholo.portfolio.components.atoms

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.BackgroundClip
import com.varabyte.kobweb.compose.css.BoxSizing
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.boxSizing
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.justifyContent
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.minWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.selectors.hover
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.theme.color.Ink
import dev.tonholo.portfolio.theme.color.Paper
import dev.tonholo.portfolio.theme.layout.unit.dp
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.CSSMediaQuery
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.StylePropertyValue
import org.jetbrains.compose.web.dom.Button as WebButton

private val ICON_BUTTON_SIZE = 32.dp
private val ICON_BUTTON_HIT_PADDING = 6.dp
private val ICON_BUTTON_BORDER_RADIUS = 4.dp

// WCAG 2.5.5 touch-target floor for the outer button box on coarse pointers.
private val ICON_BUTTON_TOUCH_MIN_SIZE = 44.dp

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
    var finalModifier = IconButtonStyle.toModifier()
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
 * Base `CssStyle` for [IconButton]. Ports `.icon-btn` from the handoff with
 * a WCAG 2.5.5 accommodation: the visible square stays 32x32 (matching the
 * editorial chrome), but 6px padding extends the hit box to 44x44 so mobile
 * taps meet the minimum target size. `background-clip: content-box` keeps
 * the hover fill inside the original 32x32 footprint even though the
 * clickable area is larger.
 */
val IconButtonStyle = CssStyle {
    val isDark = colorMode == ColorMode.DARK
    val paper = if (isDark) Paper.dark else Paper.light
    val ink = if (isDark) Ink.dark else Ink.light
    base {
        Modifier
            .width(ICON_BUTTON_SIZE)
            .height(ICON_BUTTON_SIZE)
            .padding(ICON_BUTTON_HIT_PADDING)
            .boxSizing(BoxSizing.ContentBox)
            .background { clip(BackgroundClip.ContentBox) }
            .display(DisplayStyle.LegacyInlineFlex)
            .alignItems(AlignItems.Center)
            .justifyContent(JustifyContent.Center)
            .borderRadius(ICON_BUTTON_BORDER_RADIUS)
            .backgroundColor(TransparentColor)
            .border(style = LineStyle.None)
            .cursor(Cursor.Pointer)
            .color(ink.ink3)
    }
    hover {
        Modifier
            .backgroundColor(paper.paper2)
            .color(ink.ink)
    }
    cssRule(
        mediaQuery = CSSMediaQuery.MediaFeature(
            name = "pointer",
            value = StylePropertyValue(value = "coarse"),
        ),
    ) {
        Modifier
            .minWidth(ICON_BUTTON_TOUCH_MIN_SIZE)
            .minHeight(ICON_BUTTON_TOUCH_MIN_SIZE)
    }
}
