package dev.tonholo.portfolio.components.organisms.chrome

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.AnimationIterationCount
import com.varabyte.kobweb.compose.css.BoxShadow
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.animation
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.bottom
import com.varabyte.kobweb.compose.ui.modifiers.boxShadow
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.right
import com.varabyte.kobweb.compose.ui.modifiers.role
import com.varabyte.kobweb.compose.ui.modifiers.translateY
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.animation.Keyframes
import com.varabyte.kobweb.silk.style.animation.toAnimation
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.layout.unit.dp
import dev.tonholo.portfolio.theme.typography.FontFamilies
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text as ComposeText

// `.lang-toast` geometry (from handoff `styles.css`, distilled so the card is
// a single uniform surface - no more accent side-stripe).
private val TOAST_OFFSET = 24.dp
private val TOAST_PADDING_Y = 12.dp
private val TOAST_PADDING_X = 16.dp
private val TOAST_RADIUS = 2.dp
private val TOAST_BORDER_WIDTH = 1.dp
private val TOAST_FONT_SIZE = 12.dp
private val TOAST_MAX_WIDTH = 320.dp
private const val TOAST_Z_INDEX = 100

// Secondary note, tuned to 0.75 so 11px text contrast passes WCAG AA (4.5:1).
private val TOAST_NOTE_FONT_SIZE = 11.dp
private val TOAST_NOTE_MARGIN_TOP = 2.dp
private const val TOAST_NOTE_OPACITY = 0.75

// Dim suffix opacity next to the bold `pt-BR` label.
private const val TOAST_SUFFIX_OPACITY = 0.7

// `animation: toast-in 0.24s ease-out` from the handoff.
private const val TOAST_ANIMATION_DURATION_S = 0.24
private const val TOAST_ANIMATION_ITERATIONS = 1
private val TOAST_ENTER_TRANSLATE = 6.dp
private const val TOAST_ENTER_START_PERCENT = 0.0
private const val TOAST_ENTER_END_PERCENT = 100.0

// Drop shadow (`box-shadow: 0 8px 24px rgba(0,0,0,0.08)`).
private val TOAST_SHADOW_OFFSET_Y = 8.dp
private val TOAST_SHADOW_BLUR = 24.dp
private const val TOAST_SHADOW_ALPHA = 0.08f
private const val TOAST_SHADOW_CHANNEL = 0

// The bold language code stays as a literal BCP-47 tag; it is a technical
// identifier that does not change between locales.
private const val PT_BR_LABEL = "pt-BR"

private val ToastShadowColor: Color = Color.rgba(
    r = TOAST_SHADOW_CHANNEL,
    g = TOAST_SHADOW_CHANNEL,
    b = TOAST_SHADOW_CHANNEL,
    a = TOAST_SHADOW_ALPHA,
)

/**
 * Transient status banner shown when the user switches the site to pt-BR.
 * Fixed-position bottom-right card with a uniform 1px paper-3 border: the
 * bold `pt-BR` code renders in the accent color so the locale change is the
 * one thing that draws the eye. Secondary copy (primary phrase, muted
 * note) carries the surrounding context.
 *
 * The earlier build paired a 2px accent `border-left` with a pulsing dot;
 * both were removed because the side-stripe is a recognized AI-dashboard
 * tell and the dot duplicated signal the bold accent-colored label already
 * carries. `role="status"` stays so assistive tech announces the switch
 * without stealing focus.
 *
 * The composable is fully controlled: the caller (typically `AppEntry`)
 * owns the 3.6s visibility timer and simply toggles [visible]. When
 * [visible] is `false`, nothing renders.
 *
 * @param modifier optional [Modifier] chained after the base styles.
 * @param visible whether the toast should be rendered right now. The caller
 *   is responsible for flipping this back to `false` once the dismiss timer
 *   elapses.
 */
@Composable
fun LanguageToast(visible: Boolean, modifier: Modifier = Modifier) {
    if (!visible) return

    val tokens = LocalPortfolioTokens.current
    val strings = LocalStrings.current.languageToast

    val finalModifier = LanguageToastStyle
        .toModifier()
        .backgroundColor(tokens.paper.paper)
        .border(
            width = TOAST_BORDER_WIDTH,
            style = LineStyle.Solid,
            color = tokens.paper.paper3,
        )
        .color(tokens.ink.ink2)
        .role("status")
        .then(modifier)

    Div(attrs = finalModifier.toAttrs { attr("aria-live", "polite") }) {
        B(
            attrs = Modifier
                .fontWeight(FontWeight.Bold)
                .color(tokens.accent.accent)
                .toAttrs(),
        ) {
            ComposeText(PT_BR_LABEL)
        }
        Span(
            attrs = Modifier
                .opacity(TOAST_SUFFIX_OPACITY)
                .toAttrs(),
        ) {
            ComposeText(strings.primary)
        }
        Div(
            attrs = LanguageToastNoteStyle
                .toModifier()
                .toAttrs(),
        ) {
            ComposeText(strings.secondary)
        }
    }
}

/**
 * Base `CssStyle` for [LanguageToast]. Fixed bottom-right card with paper
 * background, 12px x 16px padding, 2px radius, mono 12px body copy, and the
 * 8px x 24px ambient drop shadow. Background, border color, and text color
 * are applied inline by the composable so they stay token-aware across
 * color modes.
 */
val LanguageToastStyle = CssStyle {
    base {
        Modifier
            .position(Position.Fixed)
            .bottom(TOAST_OFFSET)
            .right(TOAST_OFFSET)
            .zIndex(TOAST_Z_INDEX)
            .maxWidth(TOAST_MAX_WIDTH)
            .padding(
                topBottom = TOAST_PADDING_Y,
                leftRight = TOAST_PADDING_X,
            )
            .borderRadius(TOAST_RADIUS)
            .fontFamily(*FontFamilies.MONO)
            .fontSize(TOAST_FONT_SIZE)
            .boxShadow(
                BoxShadow.of(
                    offsetX = 0.px,
                    offsetY = TOAST_SHADOW_OFFSET_Y,
                    blurRadius = TOAST_SHADOW_BLUR,
                    color = ToastShadowColor,
                ),
            )
            .animation(
                LanguageToastEnterKeyframes.toAnimation(
                    duration = TOAST_ANIMATION_DURATION_S.s,
                    timingFunction = AnimationTimingFunction.EaseOut,
                    iterationCount = AnimationIterationCount.of(TOAST_ANIMATION_ITERATIONS),
                ),
            )
    }
}

/**
 * Base `CssStyle` for the small secondary line rendered under the bold
 * `pt-BR` label. Matches the inline `{fontSize:11, opacity:0.55,
 * marginTop:2}` style in the handoff JSX.
 */
val LanguageToastNoteStyle = CssStyle {
    base {
        Modifier
            .fontSize(TOAST_NOTE_FONT_SIZE)
            .opacity(TOAST_NOTE_OPACITY)
            .margin(top = TOAST_NOTE_MARGIN_TOP)
    }
}

/**
 * `@keyframes toast-in` from the handoff CSS:
 * ```
 * from { opacity: 0; transform: translateY(6px); }
 * to   { opacity: 1; transform: translateY(0); }
 * ```
 * Rendered as a single 0 -> 100% declaration so the ending frame inherits
 * the resting state from the base style (opacity 1, no translation).
 */
val LanguageToastEnterKeyframes = Keyframes {
    TOAST_ENTER_START_PERCENT.percent {
        Modifier
            .opacity(value = 0.0)
            .translateY(TOAST_ENTER_TRANSLATE)
    }
    TOAST_ENTER_END_PERCENT.percent {
        Modifier
            .opacity(value = 1.0)
            .translateY(0.cssRem)
    }
}
