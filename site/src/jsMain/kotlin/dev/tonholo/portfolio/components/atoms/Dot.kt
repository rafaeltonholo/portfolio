package dev.tonholo.portfolio.components.atoms

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.varabyte.kobweb.compose.css.AnimationIterationCount
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.StyleVariable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.animation
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.scale
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.animation.Keyframes
import com.varabyte.kobweb.silk.style.animation.toAnimation
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.layout.unit.dp
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.dom.Span

private val DOT_SIZE = 7.dp
private const val DOT_RADIUS_PERCENT = 50.0
private const val DOT_ANIMATION_DURATION_S = 2.0
private const val PULSE_DIM_OPACITY = 0.6
private const val PULSE_DIM_SCALE = 0.85
private const val PULSE_START_PERCENT = 0.0
private const val PULSE_MID_PERCENT = 50.0
private const val PULSE_END_PERCENT = 100.0
private const val SCALE_FULL = 1

/**
 * Status circle. Maps to `.dot` / `.nav-meta .dot` from the handoff CSS: a
 * 7px round swatch, colored with the accent token by default. The [Pulsing]
 * variant animates opacity and scale on a 2s ease-in-out loop (`@keyframes
 * pulse`); the [Static] variant renders a steady swatch.
 *
 * @param modifier optional [Modifier] chained after the base styles.
 * @param variant [DotVariant.Pulsing] (default, nav status) or
 *   [DotVariant.Static] (terminal header, list bullets).
 * @param color optional color override; defaults to the active accent.
 */
@Composable
fun Dot(
    modifier: Modifier = Modifier,
    variant: DotVariant = DotVariant.Pulsing,
    color: Color? = null,
) {
    val tokens = LocalPortfolioTokens.current
    val fill = color ?: tokens.accent.accent

    var finalModifier = DotStyle
        .toModifier()
        .backgroundColor(fill)

    if (variant == DotVariant.Pulsing) {
        finalModifier = finalModifier.animation(
            DotPulseKeyframes.toAnimation(
                duration = DOT_ANIMATION_DURATION_S.s,
                timingFunction = AnimationTimingFunction.EaseInOut,
                iterationCount = AnimationIterationCount.Infinite,
            ),
        )
    }

    finalModifier = finalModifier.then(modifier)

    Span(attrs = finalModifier.toAttrs { attr("aria-hidden", "true") })
}

/**
 * Variant axis for [Dot].
 *
 * - [Pulsing]: runs the `pulse` keyframe animation forever; used by the nav
 *   availability badge.
 * - [Static]: no animation; used for terminal window bullets.
 */
enum class DotVariant {
    Pulsing,
    Static,
}

/**
 * Base `CssStyle` for [Dot]. Ports `.nav-meta .dot` geometry: a 7px inline
 * block rounded to a circle via a 50% radius. Background is applied inline
 * so callers can override the color.
 */
val DotStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.InlineBlock)
            .size(DotVars.Size.value(DOT_SIZE))
            .borderRadius(DotVars.BorderRadius.value(DOT_RADIUS_PERCENT.percent))
    }
}

object DotVars {
    val Size by StyleVariable<CSSSizeValue<CSSUnit.rem>>()
    val BorderRadius by StyleVariable<CSSLengthOrPercentageNumericValue>()
}

/**
 * `@keyframes pulse` from the handoff CSS:
 * ```
 * 0%, 100% { opacity: 1; transform: scale(1); }
 * 50%      { opacity: 0.6; transform: scale(0.85); }
 * ```
 */
val DotPulseKeyframes = Keyframes {
    PULSE_START_PERCENT.percent { Modifier.opacity(value = 1.0).scale(s = SCALE_FULL) }
    PULSE_MID_PERCENT.percent { Modifier.opacity(value = PULSE_DIM_OPACITY).scale(s = PULSE_DIM_SCALE) }
    PULSE_END_PERCENT.percent { Modifier.opacity(value = 1.0).scale(s = SCALE_FULL) }
}
