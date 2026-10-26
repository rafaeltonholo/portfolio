package dev.tonholo.portfolio.components.atoms

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.css.VerticalAlign
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.ariaHidden
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.modifiers.verticalAlign
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.selectors.hover
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.color.AccentMint
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Span

private const val EXTERNAL_ARROW_ICON_NAME = "arrow-up-right"
private const val EXTERNAL_TARGET = "_blank"
private const val EXTERNAL_REL = "noopener noreferrer"
private const val EXTERNAL_ARROW_SIZE_REM = 0.85
private const val EXTERNAL_ARROW_MARGIN_LEFT_EM = 0.2
private const val EXTERNAL_ARROW_VERTICAL_ALIGN_EM = -0.05

/**
 * Checks whether an anchor [href] points to an external destination outside
 * the current origin.
 *
 * @param href target link URL.
 * @return `true` if the URL starts with a protocol scheme or protocol-relative prefix.
 */
fun isExternalLink(href: String): Boolean =
    href.startsWith("http://") || href.startsWith("https://") || href.startsWith("//")

/**
 * Inline link atom specialized for article prose and markdown bodies.
 *
 * For external links, automatically opens in a new tab with `rel="noopener noreferrer"`
 * and appends a trailing diagonal Phosphor `arrow-up-right` glyph, matching the
 * footer's external link styling.
 *
 * @param href destination URL.
 * @param modifier optional [Modifier] chained after the base link styles.
 * @param content composable lambda producing the anchor text or inline children.
 */
@Composable
fun ArticleLink(href: String, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    LocalPortfolioTokens.current
    val isExternal: Boolean = isExternalLink(href = href)
    val finalModifier: Modifier = ArticleLinkStyle.toModifier().then(modifier)
    A(
        href = href,
        attrs = finalModifier.toAttrs {
            if (isExternal) {
                attr("target", EXTERNAL_TARGET)
                attr("rel", EXTERNAL_REL)
            }
        },
    ) {
        content()
        if (isExternal) {
            Span(
                attrs = ArticleLinkArrowStyle
                    .toModifier()
                    .ariaHidden(true)
                    .toAttrs(),
            ) {
                PhosphorIcon(
                    name = EXTERNAL_ARROW_ICON_NAME,
                    size = EXTERNAL_ARROW_SIZE_REM.cssRem,
                )
            }
        }
    }
}

/**
 * Base `CssStyle` for links rendered within article prose.
 */
val ArticleLinkStyle = CssStyle {
    base {
        val accent = if (colorMode == ColorMode.DARK) AccentMint.dark else AccentMint.light
        Modifier
            .color(accent.accent)
            .textDecorationLine(TextDecorationLine.Underline)
            .display(DisplayStyle.Inline)
    }
    hover {
        val accent = if (colorMode == ColorMode.DARK) AccentMint.dark else AccentMint.light
        Modifier
            .color(accent.accent)
            .textDecorationLine(TextDecorationLine.Underline)
    }
}

/**
 * Base `CssStyle` for the trailing diagonal arrow glyph inside external [ArticleLink]s.
 */
val ArticleLinkArrowStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.InlineBlock)
            .margin(left = EXTERNAL_ARROW_MARGIN_LEFT_EM.em)
            .verticalAlign(VerticalAlign.Middle)
            .textDecorationLine(TextDecorationLine.None)
    }
}
