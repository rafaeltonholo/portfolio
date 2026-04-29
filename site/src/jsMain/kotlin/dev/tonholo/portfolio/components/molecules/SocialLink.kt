package dev.tonholo.portfolio.components.molecules

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.OverflowWrap
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.ariaHidden
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.overflowWrap
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.selectors.hover
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.components.atoms.PhosphorIcon
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.color.AccentMint
import dev.tonholo.portfolio.theme.color.Ink
import dev.tonholo.portfolio.theme.layout.SiteDimensions
import dev.tonholo.portfolio.theme.layout.unit.dp
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.CSSMediaQuery
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.StylePropertyValue
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text as ComposeText

private const val EXTERNAL_ARROW_ICON_NAME = "arrow-up-right"
private const val EXTERNAL_TARGET = "_blank"
private const val EXTERNAL_REL = "noopener noreferrer"

private val SOCIAL_LINK_GAP = 6.dp
private const val SOCIAL_LINK_TRANSITION_DURATION_S = 0.18

/**
 * Footer-style social link. Renders a horizontal row composed of an optional
 * leading [icon] slot, the [label] text, and a trailing Phosphor
 * `arrow-up-right` glyph indicating an external destination.
 *
 * Maps to the footer `"Elsewhere"` column entries in the handoff (`.footer a`),
 * where links read as `"github.com/rafaeltonholo"`, `"linkedin.com/in/..."`,
 * etc. Colors inherit from the surrounding footer by default and flip to the
 * theme accent on hover (matching `.footer a:hover`). The link opens in a new
 * tab with the standard `rel="noopener noreferrer"` safety attributes.
 *
 * @param label visible link text (e.g. `"github.com/rafaeltonholo"`).
 * @param href absolute destination URL.
 * @param modifier optional [Modifier] chained after the base styles.
 * @param icon optional composable slot rendered before the label (typically
 *   a small SVG or glyph).
 */
@Composable
fun SocialLink(
    label: String,
    href: String,
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
) {
    // Read tokens so the link recomposes when the theme changes even though
    // the base color is inherited from the surrounding footer.
    LocalPortfolioTokens.current

    val finalModifier = SocialLinkStyle.toModifier().then(modifier)

    A(
        href = href,
        attrs = finalModifier.toAttrs {
            attr("target", EXTERNAL_TARGET)
            attr("rel", EXTERNAL_REL)
        },
    ) {
        if (icon != null) {
            Span(
                attrs = Modifier
                    .display(DisplayStyle.LegacyInlineFlex)
                    .alignItems(AlignItems.Center)
                    .ariaHidden(true)
                    .toAttrs(),
            ) {
                icon()
            }
        }

        Span(attrs = Modifier.toAttrs()) {
            ComposeText(label)
        }

        Span(
            attrs = SocialLinkArrowStyle
                .toModifier()
                .ariaHidden(true)
                .toAttrs(),
        ) {
            PhosphorIcon(
                name = EXTERNAL_ARROW_ICON_NAME,
                size = SiteDimensions.layout.button.icon.Medium,
            )
        }
    }
}

/**
 * Base `CssStyle` for the trailing external-arrow glyph inside [SocialLink].
 * Displayed inline so the Phosphor icon flows with the label text; spacing is
 * handled by the parent anchor's `gap`.
 */
val SocialLinkArrowStyle = CssStyle {
    base {
        Modifier.display(DisplayStyle.InlineBlock)
    }
}

/**
 * Base `CssStyle` for [SocialLink]. Inherits the surrounding color (matches
 * `.footer a { color: var(--ink-2) }` via the footer container), strips the
 * default underline, and flips to the theme accent on hover.
 */
val SocialLinkStyle = CssStyle {
    base {
        // The anchor UA style sets `color: -webkit-link` / `color: blue`, so
        // leaving the base unset leaks browser defaults (and the purple
        // visited state) into the footer. Match `FooterLinkStyle` and paint
        // the resting state with the muted `ink-2` token per theme.
        val ink = if (colorMode == ColorMode.DARK) Ink.dark else Ink.light
        Modifier
            .color(ink.ink2)
            .display(DisplayStyle.LegacyInlineFlex)
            .alignItems(AlignItems.Center)
            .gap(SOCIAL_LINK_GAP)
            .textDecorationLine(TextDecorationLine.None)
            // URLs like linkedin.com/in/rafaeltonholo have no natural break
            // points; allow the renderer to break anywhere so the label does
            // not push the footer column past the mobile viewport.
            .overflowWrap(OverflowWrap.Anywhere)
            .transition(
                Transition.of(
                    property = "color",
                    duration = SOCIAL_LINK_TRANSITION_DURATION_S.s,
                ),
            )
    }
    hover {
        val accent = if (colorMode == ColorMode.DARK) AccentMint.dark else AccentMint.light
        Modifier.color(accent.accent)
    }
    cssRule(
        mediaQuery = CSSMediaQuery.MediaFeature(
            name = "pointer",
            value = StylePropertyValue(value = "coarse"),
        ),
    ) {
        Modifier
            .minHeight(SiteDimensions.layout.minimumTouchSize)
            .padding(topBottom = SiteDimensions.size.Md)
    }
}
