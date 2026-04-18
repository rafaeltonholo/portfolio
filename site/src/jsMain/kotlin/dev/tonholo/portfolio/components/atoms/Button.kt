package dev.tonholo.portfolio.components.atoms

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.modifiers.translateY
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.selectors.hover
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.PortfolioTokens
import dev.tonholo.portfolio.theme.typography.Typography
import dev.tonholo.portfolio.theme.typography.apply
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Button as WebButton
import org.jetbrains.compose.web.dom.Text as ComposeText

private const val BUTTON_BORDER_RADIUS_REM = 0.125 // 2px
private const val BUTTON_PADDING_Y_REM = 0.625 // 10px
private const val BUTTON_PADDING_X_REM = 1.125 // 18px
private const val BUTTON_GAP_REM = 0.625 // 10px
private const val BUTTON_BORDER_WIDTH_REM = 0.0625 // 1px
private const val BUTTON_HOVER_OPACITY = 0.88
private const val BUTTON_HOVER_LIFT_REM = -0.0625 // -1px

private val Transparent: Color = Color.rgba(r = 0, g = 0, b = 0, a = 0f)

/**
 * Primary CTA atom. Maps to `.btn` (and `.btn.accent`, `.btn.ghost`) from the
 * handoff CSS, plus an `Inverse` variant used on dark HiringCta-style blocks.
 *
 * Renders an `<a>` element when [href] is non-null, otherwise a `<button>`.
 *
 * @param text label copy, rendered in mono font.
 * @param modifier optional [Modifier] chained after the base styles.
 * @param variant color pairing; defaults to [ButtonVariant.Solid].
 * @param href optional link; when provided, renders as an anchor for use as
 *   navigation CTAs (`See featured work`, `Download CV`, etc.).
 * @param onClick optional click handler, wired to both button and anchor.
 */
@Composable
fun Button(
    text: String,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.Solid,
    href: String? = null,
    onClick: (() -> Unit)? = null,
) {
    val tokens = LocalPortfolioTokens.current
    val background = variant.background(tokens = tokens)
    val foreground = variant.foreground(tokens = tokens)
    val borderColor = variant.borderColor(tokens = tokens)

    var finalModifier = ButtonStyle
        .toModifier()
        .apply(Typography.chip)
        .backgroundColor(background)
        .color(foreground)
        .border(
            width = BUTTON_BORDER_WIDTH_REM.cssRem,
            style = LineStyle.Solid,
            color = borderColor,
        )
    if (onClick != null) {
        finalModifier = finalModifier.onClick { onClick() }
    }
    finalModifier = finalModifier.then(modifier)

    if (href != null) {
        A(href = href, attrs = finalModifier.textDecorationLine(TextDecorationLine.None).toAttrs()) {
            ComposeText(text)
        }
    } else {
        WebButton(attrs = finalModifier.toAttrs()) {
            ComposeText(text)
        }
    }
}

/**
 * Color variant for [Button]. Each variant resolves a triplet
 * (background, foreground, border) from the active [PortfolioTokens].
 *
 * - [Solid]: default CTA, dark ink background with paper text. Matches `.btn`.
 * - [Accent]: mint accent background with accent-ink text. Matches `.btn.accent`.
 * - [Ghost]: transparent background, ink text, ink-4 border. Matches `.btn.ghost`.
 * - [Inverse]: paper background with ink text; used over dark surfaces like
 *   the HiringCta block.
 */
enum class ButtonVariant {
    Solid,
    Accent,
    Ghost,
    Inverse,
    ;

    /** Resolves the button background color for this variant. */
    fun background(tokens: PortfolioTokens): Color = when (this) {
        Solid -> tokens.ink.ink
        Accent -> tokens.accent.accent
        Ghost -> Transparent
        Inverse -> tokens.paper.paper
    }

    /** Resolves the button text color for this variant. */
    fun foreground(tokens: PortfolioTokens): Color = when (this) {
        Solid -> tokens.paper.paper
        Accent -> tokens.accent.accentInk
        Ghost -> tokens.ink.ink
        Inverse -> tokens.ink.ink
    }

    /** Resolves the button border color for this variant. */
    fun borderColor(tokens: PortfolioTokens): Color = when (this) {
        Solid -> tokens.ink.ink
        Accent -> tokens.accent.accent
        Ghost -> tokens.ink.ink4
        Inverse -> tokens.paper.paper
    }
}

/**
 * Base `CssStyle` for [Button]: layout, radius, padding, hover state.
 * Colors are applied inline by the composable to stay token-aware.
 */
val ButtonStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.LegacyInlineFlex)
            .alignItems(AlignItems.Center)
            .gap(BUTTON_GAP_REM.cssRem)
            .padding(topBottom = BUTTON_PADDING_Y_REM.cssRem, leftRight = BUTTON_PADDING_X_REM.cssRem)
            .borderRadius(BUTTON_BORDER_RADIUS_REM.cssRem)
            .cursor(Cursor.Pointer)
    }
    hover {
        Modifier
            .opacity(BUTTON_HOVER_OPACITY)
            .translateY(BUTTON_HOVER_LIFT_REM.cssRem)
    }
}
