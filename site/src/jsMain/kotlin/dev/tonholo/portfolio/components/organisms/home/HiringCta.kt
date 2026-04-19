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
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.color.AccentMint
import dev.tonholo.portfolio.theme.layout.ShellStyle
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
import org.jetbrains.compose.web.css.vw
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Section
import org.jetbrains.compose.web.dom.Text as ComposeText

// Outer section spacing. Handoff line 316: `paddingTop:40, paddingBottom:40`
// overrides the default `.section` padding for this block.
private const val SECTION_PADDING_Y_REM = 2.5 // 40px

// Inverted block metrics from handoff lines 317-325.
private const val BLOCK_PADDING_Y_REM = 4.0 // 64px
private const val BLOCK_PADDING_X_REM = 3.0 // 48px
private const val BLOCK_BORDER_RADIUS_REM = 0.25 // 4px
private const val BLOCK_GAP_REM = 2.0 // 32px

// Left column: meta + display heading.
private const val META_FONT_SIZE_REM = 0.75 // 12px
private const val META_LETTER_SPACING_EM = 0.08
private const val META_MARGIN_BOTTOM_REM = 0.75 // 12px

private const val HEADING_CLAMP_MIN_REM = 2.0 // 32px at 16px root
private const val HEADING_CLAMP_IDEAL_VW = 4.0 // 4vw
private const val HEADING_CLAMP_MAX_REM = 3.25 // 52px at 16px root
private const val HEADING_WEIGHT_FIVE_HUNDRED = 500
private const val HEADING_LETTER_SPACING_EM = -0.025
private const val HEADING_LINE_HEIGHT = 1.05
private const val HEADING_MAX_WIDTH_CH = 20.0

// Right column: two stacked buttons, 10px gap.
private const val BUTTONS_GAP_REM = 0.625 // 10px

// Ghost button override. Handoff line 335 sets:
//   background: transparent; border-color: rgba(255,255,255,0.2);
//   color: var(--paper)
// so we override the base Ghost variant's tokens via the modifier slot.
private const val GHOST_BORDER_ALPHA = 0.2f
private const val GHOST_BORDER_WIDTH_REM = 0.0625 // 1px
private const val GHOST_BORDER_RGB = 255

// Mono eyebrow is a shell-script-style technical literal and stays
// untranslated. The email address is also stable across locales.
private const val META_LABEL_COPY = "// hiring_me.sh"
private const val EMAIL_HREF = "mailto:rafael@tonholo.dev"

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
    val sectionModifier = HiringCtaSectionStyle
        .toModifier()
        .then(modifier)

    Section(attrs = sectionModifier.toAttrs()) {
        Div(attrs = ShellStyle.toModifier().toAttrs()) {
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

    Div(attrs = Modifier.toAttrs()) {
        Div(
            attrs = Modifier
                .fontFamily(*FontFamilies.MONO)
                .fontSize(META_FONT_SIZE_REM.cssRem)
                .letterSpacing(META_LETTER_SPACING_EM.em)
                .textTransform(TextTransform.Uppercase)
                // The CTA block paints a fixed ink surface regardless of
                // theme, so we pin to the dark-swatch mint (~10:1 contrast)
                // rather than the ambient light-mode accent (fails 4.5:1).
                .color(AccentMint.dark.accent)
                .margin(bottom = META_MARGIN_BOTTOM_REM.cssRem)
                .toAttrs(),
        ) {
            ComposeText(META_LABEL_COPY)
        }
        H3(
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
 * treatment (transparent bg, semi-transparent white border, paper text).
 */
@Composable
private fun HiringCtaActions(onEmailClick: () -> Unit, onBookingClick: () -> Unit) {
    val tokens = LocalPortfolioTokens.current
    val strings = LocalStrings.current.hiringCta

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
                    width = GHOST_BORDER_WIDTH_REM.cssRem,
                    style = LineStyle.Solid,
                    color = Color.rgba(
                        r = GHOST_BORDER_RGB,
                        g = GHOST_BORDER_RGB,
                        b = GHOST_BORDER_RGB,
                        a = GHOST_BORDER_ALPHA,
                    ),
                ),
            trailing = { PhosphorIcon(name = "arrow-up-right") },
        )
    }
}

/**
 * Base `CssStyle` for the outer `<section>` wrapper. Handoff lines 316-317
 * set 40px top and bottom padding for this section specifically (the default
 * section rule would use `--pad-y`).
 */
val HiringCtaSectionStyle = CssStyle {
    base {
        Modifier.padding(topBottom = SECTION_PADDING_Y_REM.cssRem)
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
                topBottom = BLOCK_PADDING_Y_REM.cssRem,
                leftRight = BLOCK_PADDING_X_REM.cssRem,
            )
            .borderRadius(BLOCK_BORDER_RADIUS_REM.cssRem)
            .display(DisplayStyle.Grid)
            .gridTemplateColumns {
                size(1.fr)
            }
            .alignItems(AlignItems.Center)
            .gap(BLOCK_GAP_REM.cssRem)
    }
    Breakpoint.LG {
        Modifier.gridTemplateColumns {
            size(1.fr)
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
                    min = HEADING_CLAMP_MIN_REM.cssRem.unsafeCast<CSSLengthNumericValue>(),
                    value = HEADING_CLAMP_IDEAL_VW.vw.unsafeCast<CSSLengthNumericValue>(),
                    max = HEADING_CLAMP_MAX_REM.cssRem.unsafeCast<CSSLengthNumericValue>(),
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
            .gap(BUTTONS_GAP_REM.cssRem)
    }
}
