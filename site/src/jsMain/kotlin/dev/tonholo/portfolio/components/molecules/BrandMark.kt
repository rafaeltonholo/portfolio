package dev.tonholo.portfolio.components.molecules

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.ariaHidden
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.navigation.UncoloredLinkVariant
import com.varabyte.kobweb.silk.components.navigation.UndecoratedLinkVariant
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
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

private val BRAND_MARK_SIZE = 22.dp
private const val BRAND_MARK_SRC = "/images/brand-mark.svg"

private const val BRAND_NAME = "rafael"
private const val BRAND_PATH = ".tonholo.dev"

/**
 * Wordmark used in the top nav. Maps to the handoff `.brand` anchor: a mono
 * 14px label with the cube logo, the base name (`"rafael"`),
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
        Image(
            src = BRAND_MARK_SRC,
            alt = "",
            modifier = BrandMarkLogoStyle.toModifier().ariaHidden(true),
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

/** The cube logo in front of the wordmark. It is decorative, since the wordmark next to it names the site. */
val BrandMarkLogoStyle = CssStyle {
    base {
        Modifier
            .size(BRAND_MARK_SIZE)
            .margin(right = BRAND_GAP)
    }
}
