package dev.tonholo.portfolio.components.molecules

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.components.atoms.Dot
import dev.tonholo.portfolio.components.atoms.DotVariant
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.typography.FontFamilies
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text as ComposeText

private const val BRAND_GAP_REM = 0.625 // 10px between dot and wordmark
private const val BRAND_FONT_SIZE_REM = 0.875 // 14px
private const val BRAND_LETTER_SPACING_EM = -0.01

// `.brand-dot` is a 10x10 square with a 2px radius; it reuses the [Dot]
// atom for the accent fill but overrides geometry via a trailing modifier.
private const val BRAND_DOT_SIZE_REM = 0.625 // 10px
private const val BRAND_DOT_RADIUS_REM = 0.125 // 2px

private const val BRAND_NAME = "rafael"
private const val BRAND_PATH = ".tonholo.dev"

/**
 * Wordmark used in the top nav. Maps to the handoff `.brand` anchor: a mono
 * 14px label with an accent `.brand-dot` swatch, the base name (`"rafael"`),
 * and a muted `.brand-path` suffix (`".tonholo.dev"`). The whole element is
 * clickable and behaves as an anchor (`href="#"`) that routes via [onClick].
 *
 * @param onClick invoked when the brand is clicked (typically navigates home).
 * @param modifier optional [Modifier] chained after the base styles.
 */
@Composable
fun BrandMark(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val tokens = LocalPortfolioTokens.current

    val finalModifier = BrandMarkStyle
        .toModifier()
        .color(tokens.ink.ink)
        .onClick { event ->
            event.preventDefault()
            onClick()
        }
        .then(modifier)

    A(
        href = "#",
        attrs = finalModifier.toAttrs(),
    ) {
        Dot(
            modifier = Modifier
                .width(BRAND_DOT_SIZE_REM.cssRem)
                .height(BRAND_DOT_SIZE_REM.cssRem)
                .borderRadius(BRAND_DOT_RADIUS_REM.cssRem),
            variant = DotVariant.Static,
        )
        ComposeText(BRAND_NAME)
        Span(
            attrs = Modifier
                .color(tokens.muted.muted)
                .fontWeight(FontWeight.Normal)
                .toAttrs(),
        ) {
            ComposeText(BRAND_PATH)
        }
    }
}

/**
 * Base `CssStyle` for [BrandMark]. Ports `.brand` from the handoff CSS: a mono
 * 14px flex row with a 10px gap, tight letter-spacing, and inherited color
 * so the label adapts to the surrounding surface. The pointer cursor keeps
 * the anchor feeling interactive even though [onClick] handles routing.
 */
val BrandMarkStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Flex)
            .alignItems(AlignItems.Center)
            .gap(BRAND_GAP_REM.cssRem)
            .fontFamily(*FontFamilies.MONO)
            .fontSize(BRAND_FONT_SIZE_REM.cssRem)
            .fontWeight(FontWeight.SemiBold)
            .letterSpacing(BRAND_LETTER_SPACING_EM.em)
            .textDecorationLine(TextDecorationLine.None)
            .cursor(Cursor.Pointer)
    }
}
