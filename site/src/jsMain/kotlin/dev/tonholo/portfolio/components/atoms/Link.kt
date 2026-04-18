package dev.tonholo.portfolio.components.atoms

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.selectors.hover
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.color.AccentMint
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Text as ComposeText

/**
 * Inline link atom wrapping Kobweb's `A` element. Inherits color by default to
 * match the handoff's `a { color: inherit; text-decoration: none; }`, and
 * flips to the accent color on hover (mirroring `.footer a:hover`).
 *
 * Use this atom whenever the element is rendered inside running prose or a
 * Text-ish container. For CTAs, prefer [Button] with `href`.
 *
 * @param text visible label rendered inside the anchor.
 * @param href destination URL.
 * @param modifier optional [Modifier] chained after the base styles.
 * @param target optional anchor target (`_blank`, etc.).
 * @param rel optional anchor rel attribute, typically `"noopener noreferrer"`
 *   when [target] is `_blank`.
 * @param ariaLabel optional accessibility label override.
 */
@Composable
fun Link(
    text: String,
    href: String,
    modifier: Modifier = Modifier,
    target: String? = null,
    rel: String? = null,
    ariaLabel: String? = null,
) {
    // Read tokens so the link recomposes when the theme changes even though
    // color is inherited from the parent by default.
    LocalPortfolioTokens.current

    val finalModifier = LinkStyle.toModifier().then(modifier)

    A(
        href = href,
        attrs = finalModifier.toAttrs {
            if (target != null) attr("target", target)
            if (rel != null) attr("rel", rel)
            if (ariaLabel != null) attr("aria-label", ariaLabel)
        },
    ) {
        ComposeText(text)
    }
}

/**
 * Base `CssStyle` for [Link]. Inherits the surrounding color and strips the
 * default underline; the hover state swaps the color to the theme accent
 * (resolved via [colorMode]).
 */
val LinkStyle = CssStyle {
    base {
        Modifier.textDecorationLine(TextDecorationLine.None)
    }
    hover {
        val accent = if (colorMode == ColorMode.DARK) AccentMint.dark else AccentMint.light
        Modifier
            .color(accent.accent)
            .textDecorationLine(TextDecorationLine.Underline)
    }
}
