package dev.tonholo.portfolio.components.molecules

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.gap
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
import dev.tonholo.portfolio.theme.layout.unit.dp
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text as ComposeText

private const val EXTERNAL_ARROW_ICON_NAME = "arrow-up-right"
private const val EXTERNAL_ARROW_SIZE_REM = 0.875
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
                    .toAttrs { attr("aria-hidden", "true") },
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
                .toAttrs { attr("aria-hidden", "true") },
        ) {
            PhosphorIcon(
                name = EXTERNAL_ARROW_ICON_NAME,
                size = EXTERNAL_ARROW_SIZE_REM.cssRem,
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
        Modifier
            .display(DisplayStyle.LegacyInlineFlex)
            .alignItems(AlignItems.Center)
            .gap(SOCIAL_LINK_GAP)
            .textDecorationLine(TextDecorationLine.None)
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
}
