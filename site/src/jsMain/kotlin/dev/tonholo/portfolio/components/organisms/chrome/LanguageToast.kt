package dev.tonholo.portfolio.components.organisms.chrome

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.AnimationIterationCount
import com.varabyte.kobweb.compose.css.BoxShadow
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.animation
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderLeft
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.bottom
import com.varabyte.kobweb.compose.ui.modifiers.boxShadow
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.flexShrink
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.right
import com.varabyte.kobweb.compose.ui.modifiers.role
import com.varabyte.kobweb.compose.ui.modifiers.translateY
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.animation.Keyframes
import com.varabyte.kobweb.silk.style.animation.toAnimation
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.components.atoms.Dot
import dev.tonholo.portfolio.components.atoms.DotVariant
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.typography.FontFamilies
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.DisplayStyle
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

// `.lang-toast` geometry (from handoff `styles.css`).
private const val TOAST_OFFSET_REM = 1.5 // 24px
private const val TOAST_PADDING_Y_REM = 0.75 // 12px
private const val TOAST_PADDING_X_REM = 1.0 // 16px
private const val TOAST_GAP_REM = 0.625 // 10px
private const val TOAST_RADIUS_REM = 0.125 // 2px
private const val TOAST_BORDER_WIDTH_REM = 0.0625 // 1px
private const val TOAST_ACCENT_BORDER_WIDTH_REM = 0.125 // 2px
private const val TOAST_FONT_SIZE_REM = 0.75 // 12px
private const val TOAST_MAX_WIDTH_REM = 20.0 // 320px
private const val TOAST_Z_INDEX = 100

// `.lang-toast__dot` overrides sitting on top of the base `.dot`.
private const val TOAST_DOT_SIZE_REM = 0.375 // 6px
private const val TOAST_DOT_MARGIN_TOP_REM = 0.375 // 6px

// Secondary note (`<div style={{fontSize:11, opacity:0.55, marginTop:2}}>`).
private const val TOAST_NOTE_FONT_SIZE_REM = 0.6875 // 11px
private const val TOAST_NOTE_MARGIN_TOP_REM = 0.125 // 2px
private const val TOAST_NOTE_OPACITY = 0.55

// Dim suffix opacity next to the bold `pt-BR` label.
private const val TOAST_SUFFIX_OPACITY = 0.7

// `animation: toast-in 0.24s ease-out` from the handoff.
private const val TOAST_ANIMATION_DURATION_S = 0.24
private const val TOAST_ANIMATION_ITERATIONS = 1
private const val TOAST_ENTER_TRANSLATE_REM = 0.375 // 6px
private const val TOAST_ENTER_START_PERCENT = 0.0
private const val TOAST_ENTER_END_PERCENT = 100.0

// Drop shadow (`box-shadow: 0 8px 24px rgba(0,0,0,0.08)`).
private const val TOAST_SHADOW_OFFSET_Y_REM = 0.5 // 8px
private const val TOAST_SHADOW_BLUR_REM = 1.5 // 24px
private const val TOAST_SHADOW_ALPHA = 0.08f
private const val TOAST_SHADOW_CHANNEL = 0

private const val PT_BR_LABEL = "pt-BR"
private const val PT_BR_TRANSLATION_NOTE = " - tradução em andamento"
private const val PT_BR_SECONDARY_NOTE = "Most content is still in English for now."

private val ToastShadowColor: Color = Color.rgba(
    r = TOAST_SHADOW_CHANNEL,
    g = TOAST_SHADOW_CHANNEL,
    b = TOAST_SHADOW_CHANNEL,
    a = TOAST_SHADOW_ALPHA,
)

/**
 * Transient status banner shown when the user switches the site to pt-BR.
 * Maps to `.lang-toast` in the handoff `styles.css`: a fixed-position bottom-
 * right card with a 2px accent border on the left, a small pulsing [Dot], a
 * bold `pt-BR` label followed by a dim localized note, and a smaller muted
 * secondary line. The outer element carries `role="status"` so assistive
 * technology announces the language change without stealing focus.
 *
 * The composable is fully controlled: the caller (typically `AppEntry`) owns
 * the 3.6s visibility timer and simply toggles [visible]. When [visible] is
 * `false`, nothing renders.
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

    val finalModifier = LanguageToastStyle
        .toModifier()
        .backgroundColor(tokens.paper.paper)
        .border(
            width = TOAST_BORDER_WIDTH_REM.cssRem,
            style = LineStyle.Solid,
            color = tokens.paper.paper3,
        )
        .borderLeft(
            width = TOAST_ACCENT_BORDER_WIDTH_REM.cssRem,
            style = LineStyle.Solid,
            color = tokens.accent.accent,
        )
        .color(tokens.ink.ink2)
        .role("status")
        .then(modifier)

    Div(attrs = finalModifier.toAttrs()) {
        Dot(
            modifier = LanguageToastDotStyle.toModifier(),
            variant = DotVariant.Pulsing,
        )
        Div {
            B(
                attrs = Modifier
                    .fontWeight(FontWeight.Bold)
                    .toAttrs(),
            ) {
                ComposeText(PT_BR_LABEL)
            }
            Span(
                attrs = Modifier
                    .opacity(TOAST_SUFFIX_OPACITY)
                    .toAttrs(),
            ) {
                ComposeText(PT_BR_TRANSLATION_NOTE)
            }
            Div(
                attrs = LanguageToastNoteStyle
                    .toModifier()
                    .toAttrs(),
            ) {
                ComposeText(PT_BR_SECONDARY_NOTE)
            }
        }
    }
}

/**
 * Base `CssStyle` for [LanguageToast]. Ports `.lang-toast` geometry from the
 * handoff: a fixed bottom-right card with paper background, 12px x 16px
 * padding, 2px radius, mono 12px body copy, and the 8px x 24px ambient drop
 * shadow. Background, border colors, and text color are applied inline by
 * the composable so they stay token-aware across color modes.
 */
val LanguageToastStyle = CssStyle {
    base {
        Modifier
            .position(Position.Fixed)
            .bottom(TOAST_OFFSET_REM.cssRem)
            .right(TOAST_OFFSET_REM.cssRem)
            .zIndex(TOAST_Z_INDEX)
            .maxWidth(TOAST_MAX_WIDTH_REM.cssRem)
            .display(DisplayStyle.Flex)
            .alignItems(AlignItems.FlexStart)
            .gap(TOAST_GAP_REM.cssRem)
            .padding(
                topBottom = TOAST_PADDING_Y_REM.cssRem,
                leftRight = TOAST_PADDING_X_REM.cssRem,
            )
            .borderRadius(TOAST_RADIUS_REM.cssRem)
            .fontFamily(FontFamilies.MONO)
            .fontSize(TOAST_FONT_SIZE_REM.cssRem)
            .boxShadow(
                BoxShadow.of(
                    offsetX = 0.px,
                    offsetY = TOAST_SHADOW_OFFSET_Y_REM.cssRem,
                    blurRadius = TOAST_SHADOW_BLUR_REM.cssRem,
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
 * Base `CssStyle` for `.lang-toast__dot`. Overrides the default [Dot] size
 * (7px -> 6px), nudges the dot down 6px so it optically aligns with the
 * first line of bold body text, and prevents the flex parent from squishing
 * it when the copy wraps.
 */
val LanguageToastDotStyle = CssStyle {
    base {
        Modifier
            .width(TOAST_DOT_SIZE_REM.cssRem)
            .height(TOAST_DOT_SIZE_REM.cssRem)
            .margin(top = TOAST_DOT_MARGIN_TOP_REM.cssRem)
            .flexShrink(0)
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
            .fontSize(TOAST_NOTE_FONT_SIZE_REM.cssRem)
            .opacity(TOAST_NOTE_OPACITY)
            .margin(top = TOAST_NOTE_MARGIN_TOP_REM.cssRem)
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
            .translateY(TOAST_ENTER_TRANSLATE_REM.cssRem)
    }
    TOAST_ENTER_END_PERCENT.percent {
        Modifier
            .opacity(value = 1.0)
            .translateY(0.cssRem)
    }
}
