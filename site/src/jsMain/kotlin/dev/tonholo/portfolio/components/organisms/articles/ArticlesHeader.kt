package dev.tonholo.portfolio.components.organisms.articles

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.components.atoms.MetaLabel
import dev.tonholo.portfolio.components.atoms.MetaLabelVariant
import dev.tonholo.portfolio.components.layouts.HeroSection
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.layout.unit.dp
import dev.tonholo.portfolio.theme.typography.FontFamilies
import dev.tonholo.portfolio.theme.typography.Typography
import dev.tonholo.portfolio.theme.typography.apply
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text as ComposeText

// Internal grid: `1fr` on mobile (single column, summary stacks below the
// title block) and `1fr auto` from `Breakpoint.MD` (summary aligns to the
// title's right baseline). Outer chrome (vertical rhythm + horizontal
// gutter) is owned by [HeroSection].
private val HEADER_GRID_GAP = 32.dp
private const val HEADER_GRID_FLEX_COL_FR = 1

// Handoff `.meta.meta-accent { margin-bottom: 16px }` on the accent label above the title.
private val META_MARGIN_BOTTOM = 16.dp

// Handoff `.articles-hdr p { color: var(--muted); font-family: var(--font-mono); font-size: 13px; }`.
private val META_COPY_SIZE = 13.dp

// Mono eyebrow is a technical path literal and stays untranslated.
private const val META_TEXT = "// articles/"

// Inline gap between the summary segments and the centered separator dots.
private val SUMMARY_GAP = 8.dp

/**
 * Hero header for the Articles list page. Ports `.articles-hdr` from the
 * handoff CSS (`styles.css` lines 988-1002) and the surrounding markup in
 * `pages1.jsx` `ArticlesPage` (lines 106-112): a two-column grid with an
 * editorial title block on the left (accent meta label + display `H1`) and a
 * mono muted sentence on the right summarising the post count. Below [Breakpoint.MD] the grid collapses to a single
 * column so both slots stack.
 *
 * @param articleCount number of articles in the feed; rendered as the leading
 *   integer of the mono summary copy.
 * @param modifier optional [Modifier] chained after the base styles.
 */
@Composable
fun ArticlesHeader(articleCount: Int, modifier: Modifier = Modifier) {
    val tokens = LocalPortfolioTokens.current
    val strings = LocalStrings.current.articlesList

    HeroSection(id = "articles-header", modifier = modifier) {
        Div(attrs = ArticlesHeaderGridStyle.toModifier().toAttrs()) {
            Div(attrs = Modifier.toAttrs()) {
                MetaLabel(
                    text = META_TEXT,
                    variant = MetaLabelVariant.Accent,
                    modifier = Modifier
                        .display(DisplayStyle.Block)
                        .margin(bottom = META_MARGIN_BOTTOM),
                )
                H1(
                    attrs = Modifier
                        .apply(Typography.h1)
                        .color(tokens.ink.ink)
                        .margin(topBottom = 0.cssRem)
                        .toAttrs(),
                ) {
                    ComposeText(strings.title)
                }
            }
            P(
                attrs = ArticlesHeaderSummaryStyle
                    .toModifier()
                    .color(tokens.muted.muted)
                    .toAttrs(),
            ) {
                ComposeText("$articleCount ${strings.postsSuffix}")
            }
        }
    }
}

/**
 * Internal grid for the editorial title block + summary copy. Single column
 * at the mobile-first base; switches to `1fr auto` with `align-items: end`
 * from [Breakpoint.MD] so the summary slot aligns to the title's baseline on
 * tablet and desktop.
 */
val ArticlesHeaderGridStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Grid)
            .gridTemplateColumns {
                size(HEADER_GRID_FLEX_COL_FR.fr)
            }
            .gap(HEADER_GRID_GAP)
            .alignItems(AlignItems.Start)
    }
    Breakpoint.MD {
        Modifier
            .gridTemplateColumns {
                size(HEADER_GRID_FLEX_COL_FR.fr)
                size(auto)
            }
            .alignItems(AlignItems.End)
    }
}

/**
 * Base `CssStyle` for the mono summary paragraph. Ports `.articles-hdr p`:
 * mono font at 13px. Color is applied inline by the composable so it stays
 * token-aware across color modes.
 */
val ArticlesHeaderSummaryStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Flex)
            .alignItems(AlignItems.Center)
            .gap(SUMMARY_GAP)
            .fontFamily(*FontFamilies.MONO)
            .fontSize(META_COPY_SIZE)
            .margin(topBottom = 0.cssRem)
    }
}
