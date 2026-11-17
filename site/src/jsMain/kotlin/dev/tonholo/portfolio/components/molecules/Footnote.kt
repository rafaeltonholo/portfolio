package dev.tonholo.portfolio.components.molecules

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.ariaLabel
import com.varabyte.kobweb.compose.ui.modifiers.classNames
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.scrollMargin
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.selectors.hover
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.components.atoms.PhosphorIcon
import dev.tonholo.portfolio.components.molecules.footnotes.FootnoteRegistry
import dev.tonholo.portfolio.components.molecules.footnotes.FootnoteSegment
import dev.tonholo.portfolio.components.molecules.footnotes.splitFootnoteReferences
import dev.tonholo.portfolio.extensions.jumpToAnchor
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.theme.color.AccentMint
import dev.tonholo.portfolio.theme.color.Muted
import dev.tonholo.portfolio.theme.typography.FontFamilies
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Sup
import org.jetbrains.compose.web.dom.Text

private const val ANCHOR_SCROLL_MARGIN_REM = 6.0
private const val REFERENCE_FONT_SIZE_EM = 0.75
private const val DEFINITION_FONT_SIZE_EM = 0.9
private const val DEFINITION_GAP_EM = 0.6
private const val DEFINITION_MARGIN_EM = 0.6
private const val NUMBER_COLUMN_EM = 1.75
private const val BACK_ICON_REM = 0.85
private const val REFERENCE_TAP_PADDING_Y_EM = 1.0
private const val REFERENCE_TAP_PADDING_X_EM = 0.45
private const val REFERENCE_TAP_OVERLAP_X_EM = 0.3
private const val BACK_TAP_PADDING_Y_EM = 0.75
private const val BACK_TAP_PADDING_X_EM = 0.95
private const val BACK_LINK_GAP_EM = 0.25

/**
 * Footnote numbering for one article. The article layout provides a fresh registry per article so references
 * and definitions agree on numbers.
 */
val LocalFootnoteRegistry = staticCompositionLocalOf<FootnoteRegistry?> { null }

/**
 * Prose text that may contain `[^id]` footnote references. Each reference renders as a superscript number
 * linking to its definition.
 *
 * @param text the text as written in markdown, including the `[^id]` markers.
 */
@Suppress("ModifierMissing")
@Composable
fun FootnoteText(text: String) {
    val registry = LocalFootnoteRegistry.current ?: remember { FootnoteRegistry() }
    val segments = remember(text) { splitFootnoteReferences(text) }
    segments.forEach { segment ->
        when (segment) {
            is FootnoteSegment.Text -> Text(segment.value)
            is FootnoteSegment.Reference -> FootnoteReference(id = segment.id, registry = registry)
        }
    }
}

@Composable
private fun FootnoteReference(id: String, registry: FootnoteRegistry) {
    val anchor = remember(id) { registry.registerReference(id) }
    val label = LocalStrings.current.articleBody.footnoteReference
    Sup(attrs = FootnoteReferenceStyle.toModifier().toAttrs()) {
        A(
            href = "#${anchor.targetId}",
            attrs = FootnoteReferenceLinkStyle
                .toModifier()
                .classNames(ARTICLE_PLAIN_LINK_CLASS)
                .ariaLabel("$label ${anchor.number}")
                .scrollMargin(top = ANCHOR_SCROLL_MARGIN_REM.cssRem)
                .onClick { event ->
                    event.preventDefault()
                    jumpToAnchor(anchor.targetId)
                }
                .toAttrs { id(anchor.anchorId) },
        ) {
            Text(anchor.number.toString())
        }
    }
}

/**
 * One footnote definition, written in markdown as `[^id]: text`. Renders its number, the text and a link back
 * to the first place the footnote is referenced.
 *
 * @param id the footnote id between `[^` and `]`.
 * @param modifier optional [Modifier] applied to the definition row.
 * @param content the definition text.
 */
@Composable
fun FootnoteDefinition(id: String, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val registry = LocalFootnoteRegistry.current ?: remember { FootnoteRegistry() }
    val anchor = remember(id) { registry.definition(id) }
    val backLabel = LocalStrings.current.articleBody.footnoteBack
    Div(
        attrs = FootnoteDefinitionStyle
            .toModifier()
            .then(modifier)
            .scrollMargin(top = ANCHOR_SCROLL_MARGIN_REM.cssRem)
            .toAttrs { id(anchor.anchorId) },
    ) {
        Span(attrs = FootnoteNumberStyle.toModifier().toAttrs()) { Text("${anchor.number}.") }
        Div {
            content()
            anchor.backTargetId?.let { target ->
                A(
                    href = "#$target",
                    attrs = FootnoteBackLinkStyle
                        .toModifier()
                        .classNames(ARTICLE_PLAIN_LINK_CLASS)
                        .ariaLabel(backLabel)
                        .onClick { event ->
                            event.preventDefault()
                            jumpToAnchor(target)
                        }
                        .toAttrs(),
                ) {
                    PhosphorIcon(name = "arrow-u-left-up", size = BACK_ICON_REM.cssRem)
                }
            }
        }
    }
}

/** Superscript footnote number in running text. */
val FootnoteReferenceStyle = CssStyle {
    base {
        Modifier
            .fontFamily(*FontFamilies.MONO)
            .fontSize(REFERENCE_FONT_SIZE_EM.em)
            .lineHeight(0)
            .padding(left = 0.1.em)
    }
}

/** The footnote number link: accent colour with no underline, underlined only on hover. */
val FootnoteReferenceLinkStyle = CssStyle {
    base {
        val accent = if (colorMode == ColorMode.DARK) AccentMint.dark else AccentMint.light
        Modifier
            .color(accent.accent)
            .textDecorationLine(TextDecorationLine.None)
            .padding(topBottom = REFERENCE_TAP_PADDING_Y_EM.em, leftRight = REFERENCE_TAP_PADDING_X_EM.em)
            .margin(topBottom = 0.em, leftRight = (-REFERENCE_TAP_OVERLAP_X_EM).em)
    }
    hover {
        Modifier.textDecorationLine(TextDecorationLine.Underline)
    }
}

/** A footnote definition row: the number in a narrow column, then the text. */
val FootnoteDefinitionStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Grid)
            .gridTemplateColumns {
                size(NUMBER_COLUMN_EM.em)
                minmax(0.cssRem, 1.fr)
            }
            .gap(DEFINITION_GAP_EM.em)
            .margin(bottom = DEFINITION_MARGIN_EM.em)
            .fontSize(DEFINITION_FONT_SIZE_EM.em)
    }
}

/** The footnote number in a definition row. */
val FootnoteNumberStyle = CssStyle {
    base {
        val muted = if (colorMode == ColorMode.DARK) Muted.dark else Muted.light
        Modifier
            .fontFamily(*FontFamilies.MONO)
            .fontWeight(FontWeight.Medium)
            .color(muted.muted)
    }
}

/** The arrow linking a definition back to its reference. */
val FootnoteBackLinkStyle = CssStyle {
    base {
        val accent = if (colorMode == ColorMode.DARK) AccentMint.dark else AccentMint.light
        Modifier
            .display(DisplayStyle.InlineBlock)
            .padding(topBottom = BACK_TAP_PADDING_Y_EM.em, leftRight = BACK_TAP_PADDING_X_EM.em)
            .margin(
                top = (-BACK_TAP_PADDING_Y_EM).em,
                bottom = (-BACK_TAP_PADDING_Y_EM).em,
                left = BACK_LINK_GAP_EM.em,
                right = (-BACK_TAP_PADDING_X_EM).em,
            )
            .color(accent.accent)
            .textDecorationLine(TextDecorationLine.None)
    }
}
