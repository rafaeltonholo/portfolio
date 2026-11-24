package dev.tonholo.portfolio.components.atoms

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.browser.dom.ElementTarget
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.setVariable
import com.varabyte.kobweb.compose.ui.modifiers.whiteSpace
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.overlay.PopupPlacement
import com.varabyte.kobweb.silk.components.overlay.TooltipStyle
import com.varabyte.kobweb.silk.components.overlay.TooltipVars
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.addVariant
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.theme.color.Ink
import dev.tonholo.portfolio.theme.color.Paper
import dev.tonholo.portfolio.theme.layout.unit.dp
import dev.tonholo.portfolio.theme.typography.FontFamilies
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.dom.Span
import com.varabyte.kobweb.silk.components.overlay.Tooltip as SilkTooltip
import org.jetbrains.compose.web.dom.Text as ComposeText

private val TOOLTIP_RADIUS = 4.dp
private val TOOLTIP_PADDING_Y = 6.dp
private val TOOLTIP_PADDING_X = 10.dp
private val TOOLTIP_FONT_SIZE = 12.dp
private const val TOOLTIP_LINE_HEIGHT = 1.4
private const val TOOLTIP_Z_INDEX = 100

/**
 * A short label that appears next to [target] while the reader hovers it, drawn in the site's colours and mono type,
 * with an arrow pointing at the target.
 *
 * @param target the element the tooltip describes, for example [ElementTarget.PreviousSibling].
 * @param text the label to show.
 * @param modifier optional [Modifier] applied to the tooltip bubble.
 * @param placement where the tooltip sits relative to [target].
 * @param placementTarget the element the tooltip points at, when it differs from the hover area [target].
 */
@Composable
fun Tooltip(
    target: ElementTarget,
    text: String,
    modifier: Modifier = Modifier,
    placement: PopupPlacement = PopupPlacement.Bottom,
    placementTarget: ElementTarget? = null,
) {
    SilkTooltip(
        target = target,
        variant = SiteTooltipVariant,
        placement = placement,
        placementTarget = placementTarget,
        modifier = modifier,
    ) {
        Span(attrs = TooltipTextStyle.toModifier().toAttrs()) { ComposeText(text) }
    }
}

/** The tooltip bubble and arrow, in the ink colour of the active theme and above the sticky nav. */
val SiteTooltipVariant = TooltipStyle.addVariant {
    base {
        val isDark = colorMode == ColorMode.DARK
        Modifier
            .setVariable(TooltipVars.BackgroundColor, if (isDark) Ink.dark.ink else Ink.light.ink)
            .setVariable(TooltipVars.Color, if (isDark) Paper.dark.paper else Paper.light.paper)
            .borderRadius(TOOLTIP_RADIUS)
            .zIndex(TOOLTIP_Z_INDEX)
    }
}

/** The tooltip label: one line of small mono text. */
val TooltipTextStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Block)
            .padding(topBottom = TOOLTIP_PADDING_Y, leftRight = TOOLTIP_PADDING_X)
            .fontFamily(*FontFamilies.MONO)
            .fontSize(TOOLTIP_FONT_SIZE)
            .lineHeight(TOOLTIP_LINE_HEIGHT)
            .whiteSpace(WhiteSpace.NoWrap)
    }
}
