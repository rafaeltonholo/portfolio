package dev.tonholo.portfolio.components.atoms

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.bottom
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.justifyContent
import com.varabyte.kobweb.compose.ui.modifiers.left
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.right
import com.varabyte.kobweb.compose.ui.modifiers.top
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.color.Paper
import dev.tonholo.portfolio.theme.layout.unit.dp
import dev.tonholo.portfolio.theme.typography.FontFamilies
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text as ComposeText

private val LABEL_PADDING_Y = 4.dp
private val LABEL_PADDING_X = 10.dp
private val LABEL_RADIUS = 2.dp
private const val LABEL_Z_INDEX = 1
private const val PATTERN_OPACITY = 0.4
private const val PATTERN_STOP_PERCENT = "25%"
private const val PATTERN_TILE_PX = "12px"

// Handoff `.ph` CSS: mono family at 11px, no uppercase transform. Chip
// typography in the project theme forces uppercase, which does not match,
// so the placeholder sets these directly.
private val PLACEHOLDER_FONT_SIZE = 11.dp

/**
 * Aspect-ratio-aware placeholder block. Maps to `.ph` in the handoff CSS:
 * a paper-2 flex container that centers a mono 11px label on top of a
 * 45deg checker pattern painted by an absolute-positioned layer (the
 * handoff uses a `::before` pseudo-element; we keep the same visual by
 * rendering the layer as a sibling element so background colors can react
 * to the theme at runtime).
 *
 * Used wherever the redesign would eventually show a project image; the
 * [aspectRatio] enum pins the outer shape (portrait card, hero, square
 * thumbnail) so the layout stays stable when real imagery replaces it.
 *
 * @param aspectRatio desired aspect ratio; pick from [AspectRatio].
 * @param label short text rendered inside the paper-on-paper capsule.
 * @param modifier optional [Modifier] chained after the base styles.
 */
@Composable
fun Placeholder(aspectRatio: AspectRatio, label: String, modifier: Modifier = Modifier) {
    val tokens = LocalPortfolioTokens.current
    val isDark = ColorMode.current == ColorMode.DARK
    val stripeCssColor = (if (isDark) Paper.dark.paper3 else Paper.light.paper3).toString()
    val patternCss = buildString {
        append("linear-gradient(45deg, $stripeCssColor $PATTERN_STOP_PERCENT, transparent $PATTERN_STOP_PERCENT), ")
        append("linear-gradient(-45deg, $stripeCssColor $PATTERN_STOP_PERCENT, transparent $PATTERN_STOP_PERCENT)")
    }

    val finalModifier = PlaceholderStyle
        .toModifier()
        .fontFamily(*FontFamilies.MONO)
        .fontSize(PLACEHOLDER_FONT_SIZE)
        .backgroundColor(tokens.paper.paper2)
        .color(tokens.muted.muted2)
        .aspectRatio(width = aspectRatio.widthRatio, height = aspectRatio.heightRatio)
        .then(modifier)

    Div(attrs = finalModifier.toAttrs()) {
        Div(
            attrs = PlaceholderPatternStyle
                .toModifier()
                .styleModifier {
                    property("background-image", patternCss)
                    property("background-size", "$PATTERN_TILE_PX $PATTERN_TILE_PX")
                }
                .toAttrs { attr("aria-hidden", "true") },
        )
        Span(
            attrs = PlaceholderLabelStyle
                .toModifier()
                .backgroundColor(tokens.paper.paper)
                .toAttrs(),
        ) {
            ComposeText(label)
        }
    }
}

/**
 * Curated aspect ratios accepted by [Placeholder]. Values match the handoff
 * usage: tall portrait cards (4:5), hero panels (16:9), and square tiles.
 *
 * @property widthRatio numerator of the CSS `aspect-ratio` ratio.
 * @property heightRatio denominator of the CSS `aspect-ratio` ratio.
 */
enum class AspectRatio(val widthRatio: Int, val heightRatio: Int) {
    Portrait4x5(widthRatio = 4, heightRatio = 5),
    Hero16x9(widthRatio = 16, heightRatio = 9),
    Square(widthRatio = 1, heightRatio = 1),
}

/**
 * Base `CssStyle` for [Placeholder]. Ports `.ph` layout (flex center),
 * overflow clipping, and the mono typography hooks.
 */
val PlaceholderStyle = CssStyle {
    base {
        Modifier
            .position(Position.Relative)
            .display(DisplayStyle.Flex)
            .alignItems(AlignItems.Center)
            .justifyContent(JustifyContent.Center)
            .overflow(Overflow.Hidden)
    }
}

/**
 * Absolute-positioned layer that paints the diagonal checker pattern. Two
 * stacked linear gradients at opposing angles (+/-45deg) reproduce the
 * handoff's `.ph::before` look; the layer is rendered as a sibling element
 * so its background can be swapped per color mode from Kotlin.
 */
val PlaceholderPatternStyle = CssStyle {
    base {
        Modifier
            .position(Position.Absolute)
            .top(0.px)
            .right(0.px)
            .bottom(0.px)
            .left(0.px)
            .opacity(PATTERN_OPACITY)
    }
}

/**
 * Centered paper-on-paper label inside a [Placeholder]. Mirrors the handoff
 * `.ph span` selector: 4px x 10px padding, 2px radius, mono uppercase
 * typography lifted above the pattern layer via `z-index: 1`.
 */
val PlaceholderLabelStyle = CssStyle {
    base {
        Modifier
            .position(Position.Relative)
            .zIndex(LABEL_Z_INDEX)
            .padding(topBottom = LABEL_PADDING_Y, leftRight = LABEL_PADDING_X)
            .borderRadius(LABEL_RADIUS)
    }
}
