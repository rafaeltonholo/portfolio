package dev.tonholo.portfolio.components.atoms

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.AnimationIterationCount
import com.varabyte.kobweb.compose.css.VerticalAlign
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.animation
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.marginInline
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.verticalAlign
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.animation.Keyframes
import com.varabyte.kobweb.silk.style.animation.toAnimation
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.dom.Span

private const val CARET_WIDTH_REM = 0.625 // 10px at 1rem=16px
private const val CARET_HEIGHT_EM = 1.2
private const val CARET_MARGIN_LEFT_REM = 0.125 // 2px
private const val CARET_ANIMATION_DURATION_S = 1.05
private const val BLINK_MID_PERCENT = 50.0

// Terminal caret color from handoff (#e8e4d8) - a warm paper tone. Kept as an
// explicit literal because it is independent of the theme and matches the
// hardcoded terminal chrome in `styles.css`.
private const val CARET_COLOR_RGB = 0xE8E4D8

private val DefaultCaretColor: Color = Color.rgb(CARET_COLOR_RGB)

/**
 * Blinking terminal caret. Maps to `.hero-c__term .caret` from the handoff
 * CSS: a 10px x 1.2em swatch that toggles opacity every 1.05s via the
 * `caret` keyframe animation with `step-end` easing.
 *
 * @param modifier optional [Modifier] chained after the base styles.
 * @param color optional color override; defaults to the handoff's terminal
 *   highlight color.
 */
@Composable
fun Caret(modifier: Modifier = Modifier, color: Color? = null) {
    // Read tokens so the caret recomposes on theme changes even though the
    // default color is a constant from the handoff terminal chrome.
    LocalPortfolioTokens.current
    val fill = color ?: DefaultCaretColor

    val finalModifier = CaretStyle
        .toModifier()
        .backgroundColor(fill)
        .animation(
            CaretBlinkKeyframes.toAnimation(
                duration = CARET_ANIMATION_DURATION_S.s,
                timingFunction = AnimationTimingFunction.StepEnd,
                iterationCount = AnimationIterationCount.Infinite,
            ),
        )
        .then(modifier)

    Span(attrs = finalModifier.toAttrs { attr("aria-hidden", "true") })
}

/**
 * Base `CssStyle` for [Caret]. Ports `.hero-c__term .caret` geometry: an
 * inline block sized 10px x 1.2em, aligned to text-bottom with a 2px left
 * margin. Background is applied inline so callers can override the color.
 */
val CaretStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.InlineBlock)
            .width(CARET_WIDTH_REM.cssRem)
            .height(CARET_HEIGHT_EM.em)
            .verticalAlign(VerticalAlign.TextBottom)
            .marginInline { start(CARET_MARGIN_LEFT_REM.cssRem) }
    }
}

/**
 * `@keyframes caret { 50% { opacity: 0; } }` from the handoff CSS. Because
 * `step-end` easing holds the last value until the next stop, the visible
 * cycle is opaque for the first half, transparent for the second.
 */
val CaretBlinkKeyframes = Keyframes {
    BLINK_MID_PERCENT.percent { Modifier.opacity(value = 0.0) }
}
