package dev.tonholo.portfolio.components.molecules

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.setVariable
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.navigation.UncoloredLinkVariant
import com.varabyte.kobweb.silk.components.navigation.UndecoratedLinkVariant
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.components.atoms.Dot
import dev.tonholo.portfolio.components.atoms.DotVariant
import dev.tonholo.portfolio.components.atoms.DotVars
import dev.tonholo.portfolio.navigation.Routes
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.layout.unit.dp
import dev.tonholo.portfolio.theme.typography.FontFamilies
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text as ComposeText

private val BRAND_GAP = 10.dp
private val BRAND_FONT_SIZE = 14.dp
private const val BRAND_LETTER_SPACING_EM = -0.01

// `.brand-dot` is a 10x10 square with a 2px radius; it reuses the [Dot]
// atom for the accent fill but overrides geometry via a trailing modifier.
private val BRAND_DOT_SIZE = 10.dp
private val BRAND_DOT_RADIUS = 2.dp

private const val BRAND_NAME = "rafael"
private const val BRAND_PATH = ".tonholo.dev"

/**
 * Wordmark used in the top nav. Maps to the handoff `.brand` anchor: a mono
 * 14px label with an accent `.brand-dot` swatch, the base name (`"rafael"`),
 * and a muted `.brand-path` suffix (`".tonholo.dev"`). Emits a real `<a>`
 * via Kobweb's [Link] so middle-click / cmd-click / copy-link-address work.
 *
 * @param route destination path; defaults to the home route.
 * @param modifier optional [Modifier] chained after the base styles.
 */
@Composable
fun BrandMark(modifier: Modifier = Modifier, route: String = Routes.HOME) {
    val tokens = LocalPortfolioTokens.current

    val finalModifier = BrandMarkStyle
        .toModifier()
        .color(tokens.ink.ink)
        .then(modifier)

    Link(
        path = route,
        modifier = finalModifier,
        variant = UndecoratedLinkVariant then UncoloredLinkVariant,
    ) {
        Dot(
            modifier = BrandMarkDotStyle.toModifier(),
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
 * the anchor feeling interactive.
 */
val BrandMarkStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Flex)
            .alignItems(AlignItems.Center)
            .fontFamily(*FontFamilies.MONO)
            .fontSize(BRAND_FONT_SIZE)
            .fontWeight(FontWeight.SemiBold)
            .letterSpacing(BRAND_LETTER_SPACING_EM.em)
            .textDecorationLine(TextDecorationLine.None)
            .cursor(Cursor.Pointer)
    }
}

val BrandMarkDotStyle = CssStyle {
    base {
        Modifier
            .setVariable(DotVars.Size, BRAND_DOT_SIZE)
            .setVariable(DotVars.BorderRadius, BRAND_DOT_RADIUS)
            .margin(right = BRAND_GAP)
    }
}
