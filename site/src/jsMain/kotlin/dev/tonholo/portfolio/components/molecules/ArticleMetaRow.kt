package dev.tonholo.portfolio.components.molecules

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.flexWrap
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.navigation.UncoloredLinkVariant
import com.varabyte.kobweb.silk.components.navigation.UndecoratedLinkVariant
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.components.atoms.MetaSeparator
import dev.tonholo.portfolio.components.atoms.PhosphorIcon
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.navigation.Routes
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.layout.SiteDimensions
import dev.tonholo.portfolio.theme.layout.unit.dp
import dev.tonholo.portfolio.theme.typography.FontFamilies
import dev.tonholo.portfolio.theme.typography.unit.sp
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexWrap
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text as ComposeText

private val META_ROW_FONT_SIZE = 12.sp

// Byline default is the author's first name; keeping it here as a constant
// avoids threading identity through an i18n surface that describes formatting,
// not personal data.
private const val DEFAULT_AUTHOR = "rafael"
private const val BACK_ICON_NAME = "arrow-left"

/**
 * Inline meta row rendered at the top of an article detail page. Ports
 * `.meta-row` from the `.article-detail` section of the handoff CSS: a
 * flex row in the mono font at 12px, muted color, 20px gap between
 * segments, and 24px bottom margin.
 *
 * The first segment is a real `<a href>` anchor (via Kobweb's [Link])
 * prefixed with a Phosphor `arrow-left` glyph that routes back to the
 * articles index; subsequent segments are separated by middle dots and
 * carry the date, read time, and author attribution
 * (each prefixed by a [MetaSeparator] dot, e.g. `dec 20, 2025`, `4 min read`, `by rafael`).
 *
 * @param date human-readable publish date, e.g. `"dec 20, 2025"`.
 * @param readTime estimated read duration, e.g. `"4 min"`; the component
 *   appends the `"read"` suffix when rendering.
 * @param modifier optional [Modifier] chained after the base styles.
 * @param backRoute destination path for the back link; defaults to the
 *   articles index.
 * @param author byline to render after `by`; defaults to `"rafael"`.
 * @param authorAvatar optional URL for the author avatar image.
 */
@Composable
fun ArticleMetaRow(
    date: String,
    readTime: String,
    modifier: Modifier = Modifier,
    backRoute: String = Routes.ARTICLES,
    author: String = DEFAULT_AUTHOR,
    authorAvatar: String = "",
) {
    val tokens = LocalPortfolioTokens.current
    val strings = LocalStrings.current.articleMeta
    val finalModifier = ArticleMetaRowStyle
        .toModifier()
        .color(tokens.muted.muted)
        .then(modifier)
    Div(attrs = finalModifier.toAttrs()) {
        Link(
            path = backRoute,
            modifier = Modifier
                .color(tokens.accent.accent)
                .cursor(Cursor.Pointer)
                .display(DisplayStyle.LegacyInlineFlex)
                .alignItems(AlignItems.Center)
                .gap(SiteDimensions.size.Xsm)
                .textDecorationLine(TextDecorationLine.None),
            variant = UndecoratedLinkVariant then UncoloredLinkVariant,
        ) {
            PhosphorIcon(
                name = BACK_ICON_NAME,
                size = SiteDimensions.layout.button.icon.Medium,
            )
            ComposeText(strings.backLabel)
        }
        ArticleMetaSegment { ComposeText(date) }
        if (readTime.isNotBlank()) {
            ArticleMetaSegment { ComposeText("$readTime ${strings.readSuffix}") }
        }
        ArticleMetaSegment {
            if (authorAvatar.isNotBlank()) {
                Image(
                    src = authorAvatar,
                    alt = author,
                    modifier = Modifier
                        .size(16.dp)
                        .borderRadius(100.percent)
                        .objectFit(ObjectFit.Cover),
                )
            }
            ComposeText("${strings.authorPrefix} $author")
        }
    }
}

/**
 * Base `CssStyle` for [ArticleMetaRow]. Ports `.article-detail .meta-row`
 * from the handoff: mono font at 12px, a 20px flex gap between segments,
 * and a 24px bottom margin. The muted text color is applied inline by the
 * composable so it stays token-aware across color modes.
 */
val ArticleMetaRowStyle = CssStyle {
    base {
        // `flexWrap` with a tighter row gap prevents the mono segments from
        // pushing the row past the mobile viewport (they would otherwise
        // fracture into a broken vertical stack with the centered dots hanging
        // in their own columns).
        Modifier
            .display(DisplayStyle.Flex)
            .flexWrap(FlexWrap.Wrap)
            .alignItems(AlignItems.Center)
            .fontFamily(*FontFamilies.MONO)
            .fontSize(META_ROW_FONT_SIZE)
            .gap(rowGap = SiteDimensions.size.Sm, columnGap = SiteDimensions.size.Lg)
            .margin(bottom = SiteDimensions.size.Xl)
    }
}

/**
 * Inline-flex segment that pairs a [MetaSeparator] dot with the supplied
 * [content]. Used by [ArticleMetaRow] to render the date / read-time / author
 * cells without resorting to literal middle-dot glyphs.
 */
@Composable
private fun ArticleMetaSegment(content: @Composable () -> Unit) {
    Span(
        attrs = Modifier
            .display(DisplayStyle.LegacyInlineFlex)
            .alignItems(AlignItems.Center)
            .gap(value = 6.dp)
            .toAttrs(),
    ) {
        MetaSeparator()
        content()
    }
}
