package dev.tonholo.portfolio.components.organisms.home

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.CSSLengthNumericValue
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.TextTransform
import com.varabyte.kobweb.compose.css.functions.clamp
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.flexDirection
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textTransform
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.components.atoms.Button
import dev.tonholo.portfolio.components.atoms.ButtonVariant
import dev.tonholo.portfolio.components.atoms.PhosphorIcon
import dev.tonholo.portfolio.components.layouts.SectionContainerStyle
import dev.tonholo.portfolio.components.layouts.SectionContentStyle
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.state.LocalThemeState
import dev.tonholo.portfolio.state.ThemeState
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.color.AccentMint
import dev.tonholo.portfolio.theme.layout.unit.dp
import dev.tonholo.portfolio.theme.typography.FontFamilies
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vw
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Section
import org.jetbrains.compose.web.dom.Text as ComposeText

// Inverted block metrics from handoff lines 317-325.
private val BLOCK_PADDING_Y = 64.dp
private val BLOCK_PADDING_X = 48.dp
private val BLOCK_BORDER_RADIUS = 4.dp
private val BLOCK_GAP = 32.dp

// Left column: meta + display heading.
private val META_FONT_SIZE = 12.dp
private const val META_LETTER_SPACING_EM = 0.08
private val META_MARGIN_BOTTOM = 12.dp

private val HEADING_CLAMP_MIN = 32.dp
private const val HEADING_CLAMP_IDEAL_VW = 4.0 // 4vw
private val HEADING_CLAMP_MAX = 52.dp
private const val HEADING_WEIGHT_FIVE_HUNDRED = 500
private const val HEADING_LETTER_SPACING_EM = -0.025
private const val HEADING_LINE_HEIGHT = 1.05
private const val HEADING_MAX_WIDTH_CH = 20.0

// Right column: two stacked buttons, 10px gap.
private val BUTTONS_GAP = 10.dp

// Ghost button override. Handoff line 335 sets:
//   background: transparent; border-color: rgba(255,255,255,0.2);
//   color: var(--paper)
// so we override the base Ghost variant's tokens via the modifier slot.
private const val GHOST_BORDER_ALPHA = 0.2f
private val GHOST_BORDER_WIDTH = 1.dp
private const val GHOST_BORDER_DARK_SURFACE_RGB = 255
private const val GHOST_BORDER_LIGHT_SURFACE_RGB = 21

// Eyebrow accent colors:
// In light mode, the inverted block has a dark ink surface (#15130F), so AccentMint.dark.accent
// (#7DD198) clears WCAG AAA at ~10.5:1 contrast.
// In dark mode, the inverted block has a light cream surface (#F2EFE6), where #7DD198 would
// fail severely at 1.60:1. To satisfy WCAG AAA (>= 7.0:1) on this light surface, we use a deep
// mint green (#135328) with ~7.95:1 contrast ratio within the same 140° hue family.
private const val META_ACCENT_LIGHT_SURFACE_RGB = 0x135328
private val META_ACCENT_LIGHT_SURFACE = Color.rgb(META_ACCENT_LIGHT_SURFACE_RGB)

// Mono eyebrow is a shell-script-style technical literal and stays
// untranslated. The email address is also stable across locales.
private const val META_LABEL_COPY = "// hiring_me.sh"
private const val EMAIL_HREF = "mailto:rafael@tonholo.dev"

/**
 * Resolves the eyebrow color to ensure WCAG AAA compliance (>= 7.0:1) on both
 * light and dark inverted surfaces.
 *
 * @param isDark whether the site is currently rendering in dark mode.
 * @return [Color] for the eyebrow copy.
 */
internal fun resolveEyebrowColor(isDark: Boolean): Color =
    if (isDark) META_ACCENT_LIGHT_SURFACE else AccentMint.dark.accent

/**
 * Resolves the ghost button border color for the inverted block, adapting to
 * light and dark surfaces.
 *
 * @param isDark whether the site is currently rendering in dark mode.
 * @return [Color] for the ghost button border.
 */
internal fun resolveGhostBorderColor(isDark: Boolean): Color {
    val rgb: Int = if (isDark) GHOST_BORDER_LIGHT_SURFACE_RGB else GHOST_BORDER_DARK_SURFACE_RGB
    return Color.rgba(r = rgb, g = rgb, b = rgb, a = GHOST_BORDER_ALPHA)
}

/**
 * Home page organism. Inverted dark CTA block that sits at the bottom of the
 * homepage. Ports the `section` block in `HomePage` (handoff lines 316-338):
 * a two-column grid with a mono eyebrow + display heading on the left and a
 * stacked pair of buttons (accent email + ghost booking link) on the right.
 *
 * The block always renders on a dark surface: `tokens.ink.ink` for the
 * background (which is the "ink" text color in light mode, so the block
 * inverts the page palette) with `tokens.paper.paper` for the foreground.
 * The ghost booking button receives a modifier override to swap its default
 * on-page Ghost colors for the on-dark variant used in the handoff:
 * transparent background, semi-transparent white border, paper text.
 *
 * Below [Breakpoint.LG] the grid collapses to a single column so the
 * buttons stack under the heading.
 *
 * @param modifier optional [Modifier] chained after the base section styles.
 * @param onEmailClick invoked when the email CTA is clicked; defaults to a
 *   no-op, so the button still works as a `mailto:` anchor without any wiring.
 * @param onBookingClick invoked when the booking CTA is clicked; defaults to
 *   a no-op.
 */
@Composable
fun HiringCta(
    modifier: Modifier = Modifier,
    onEmailClick: () -> Unit = {},
    onBookingClick: () -> Unit = {},
) {
    Section(
        attrs = SectionContainerStyle
            .toModifier()
            .then(modifier)
            .toAttrs(),
    ) {
        Div(attrs = SectionContentStyle.toModifier().toAttrs()) {
            HiringCtaBlock(onEmailClick = onEmailClick, onBookingClick = onBookingClick)
        }
    }
}

/**
 * Inner dark block. Split out so the outer [Section] can remain full-width
 * (`padding: 40px 0` per handoff) while the dark surface stays constrained
 * inside the shell's gutters.
 */
@Composable
private fun HiringCtaBlock(onEmailClick: () -> Unit, onBookingClick: () -> Unit) {
    val tokens = LocalPortfolioTokens.current

    Div(
        attrs = HiringCtaBlockStyle
            .toModifier()
            .backgroundColor(tokens.ink.ink)
            .color(tokens.paper.paper)
            .toAttrs(),
    ) {
        HiringCtaCopy()
        HiringCtaActions(onEmailClick = onEmailClick, onBookingClick = onBookingClick)
    }
}

/**
 * Left column: mono accent eyebrow (`// hiring_me.sh`) stacked over the
 * display heading. Heading size clamps between 32px and 52px, tracking
 * 4vw between the two ends per handoff line 329.
 */
@Composable
private fun HiringCtaCopy() {
    val strings = LocalStrings.current.hiringCta
    val themeState: ThemeState = LocalThemeState.current
    val eyebrowColor: Color = resolveEyebrowColor(isDark = themeState.current.isDark)
    Div(attrs = Modifier.toAttrs()) {
        Div(
            attrs = Modifier
                .fontFamily(*FontFamilies.MONO)
                .fontSize(META_FONT_SIZE)
                .letterSpacing(META_LETTER_SPACING_EM.em)
                .textTransform(TextTransform.Uppercase)
                .color(eyebrowColor)
                .margin(bottom = META_MARGIN_BOTTOM)
                .toAttrs(),
        ) {
            ComposeText(META_LABEL_COPY)
        }
        H2(
            attrs = HiringCtaHeadingStyle.toModifier().toAttrs(),
        ) {
            ComposeText(strings.heading)
        }
    }
}

/**
 * Right column: accent email button over ghost "book 30 min" button with a
 * trailing arrow-up-right [PhosphorIcon]. The ghost button's color triplet
 * is replaced via the [Modifier] slot to match the handoff's on-dark
 * treatment (transparent bg, semi-transparent border, paper text).
 */
@Composable
private fun HiringCtaActions(onEmailClick: () -> Unit, onBookingClick: () -> Unit) {
    val tokens = LocalPortfolioTokens.current
    val strings = LocalStrings.current.hiringCta
    val themeState: ThemeState = LocalThemeState.current
    val ghostBorderColor: Color = resolveGhostBorderColor(isDark = themeState.current.isDark)
    Div(attrs = HiringCtaActionsStyle.toModifier().toAttrs()) {
        Button(
            text = strings.emailLabel,
            variant = ButtonVariant.Accent,
            href = EMAIL_HREF,
            onClick = onEmailClick,
        )
        Button(
            text = strings.bookingLabel,
            variant = ButtonVariant.Ghost,
            onClick = onBookingClick,
            modifier = Modifier
                .backgroundColor(
                    Color.rgba(r = 0, g = 0, b = 0, a = 0f),
                )
                .color(tokens.paper.paper)
                .border(
                    width = GHOST_BORDER_WIDTH,
                    style = LineStyle.Solid,
                    color = ghostBorderColor,
                ),
            trailing = { PhosphorIcon(name = "arrow-up-right") },
        )
    }
}

/**
 * Base `CssStyle` for the inverted block. Ports handoff lines 318-326: 64px
 * vertical / 48px horizontal padding, 4px radius, a 2-column grid with a 32px
 * gap aligning its contents on the center baseline. Below [Breakpoint.LG]
 * the grid collapses to a single column so the actions stack under the copy.
 * Background and text colors are applied inline by the composable so they
 * consume tokens.
 */
val HiringCtaBlockStyle = CssStyle {
    base {
        Modifier
            .padding(
                topBottom = BLOCK_PADDING_Y,
                leftRight = BLOCK_PADDING_X,
            )
            .borderRadius(BLOCK_BORDER_RADIUS)
            .display(DisplayStyle.Grid)
            .gridTemplateColumns {
                minmax(0.px, 1.fr)
            }
            .alignItems(AlignItems.Center)
            .gap(BLOCK_GAP)
    }
    Breakpoint.LG {
        Modifier.gridTemplateColumns {
            minmax(0.px, 1.fr)
            size(auto)
        }
    }
}

/**
 * Base `CssStyle` for the display heading. Ports handoff line 329: display
 * family, weight 500, clamped font-size between 32px and 52px (4vw ideal),
 * tight tracking, near-unity line height, capped at 20ch so line breaks land
 * in a pleasing place.
 */
val HiringCtaHeadingStyle = CssStyle {
    base {
        Modifier
            .margin(topBottom = 0.cssRem)
            .fontFamily(*FontFamilies.DISPLAY)
            .fontWeight(HEADING_WEIGHT_FIVE_HUNDRED)
            .fontSize(
                clamp(
                    min = HEADING_CLAMP_MIN.unsafeCast<CSSLengthNumericValue>(),
                    value = HEADING_CLAMP_IDEAL_VW.vw.unsafeCast<CSSLengthNumericValue>(),
                    max = HEADING_CLAMP_MAX.unsafeCast<CSSLengthNumericValue>(),
                ).unsafeCast<CSSLengthOrPercentageNumericValue>(),
            )
            .letterSpacing(HEADING_LETTER_SPACING_EM.em)
            .lineHeight(HEADING_LINE_HEIGHT)
            .maxWidth(HEADING_MAX_WIDTH_CH.unsafeCast<CSSSizeValue<CSSUnit.ch>>())
    }
}

/**
 * Base `CssStyle` for the right-column buttons container. Flex column with a
 * 10px gap per handoff line 333.
 */
val HiringCtaActionsStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Flex)
            .flexDirection(FlexDirection.Column)
            .gap(BUTTONS_GAP)
    }
}
