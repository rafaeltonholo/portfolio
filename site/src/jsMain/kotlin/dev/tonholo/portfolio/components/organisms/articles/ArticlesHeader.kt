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
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.components.atoms.MetaLabel
import dev.tonholo.portfolio.components.atoms.MetaLabelVariant
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.typography.FontFamilies
import dev.tonholo.portfolio.theme.typography.Typography
import dev.tonholo.portfolio.theme.typography.apply
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text as ComposeText

// Handoff `.articles-hdr`:
//   padding: var(--pad-y) 0 var(--pad-y-sm);
//   display: grid;
//   grid-template-columns: 1fr auto;
//   align-items: end;
//   gap: 32px;
// Density-pinned `--pad-y` = 80px * 0.72 ≈ 57.6px, `--pad-y-sm` ≈ 28.8px. The
// rem values below approximate those for the compact density used across
// the redesign.
private const val HEADER_PADDING_TOP_REM = 3.6 // ~57.6px
private const val HEADER_PADDING_BOTTOM_REM = 1.8 // ~28.8px
private const val HEADER_GRID_GAP_REM = 2.0 // 32px
private const val HEADER_GRID_FLEX_COL_FR = 1

// Handoff `.meta.meta-accent { margin-bottom: 16px }` on the accent label above the title.
private const val META_MARGIN_BOTTOM_REM = 1.0 // 16px

// Handoff `.articles-hdr p { color: var(--muted); font-family: var(--font-mono); font-size: 13px; }`.
private const val META_COPY_SIZE_REM = 0.8125 // 13px

// Hardcoded v1 copy from `pages1.jsx` line 108-111. Lifts into Compose Resources during i18n.
private const val META_TEXT = "// articles/"
private const val TITLE_TEXT = "Writing."
private const val POSTS_SUFFIX = "posts"
private const val RSS_COPY = "RSS available"
private const val SUBSCRIBERS_COPY = "subscribed by 342 readers"
private const val SEPARATOR = " \u00B7 " // " · " middle dot

/**
 * Hero header for the Articles list page. Ports `.articles-hdr` from the
 * handoff CSS (`styles.css` lines 988-1002) and the surrounding markup in
 * `pages1.jsx` `ArticlesPage` (lines 106-112): a two-column grid with an
 * editorial title block on the left (accent meta label + display `H1`) and a
 * mono muted sentence on the right summarising the post count, RSS status, and
 * subscriber count. Below [Breakpoint.MD] the grid collapses to a single
 * column so both slots stack.
 *
 * @param articleCount number of articles in the feed; rendered as the leading
 *   integer of the mono summary copy.
 * @param modifier optional [Modifier] chained after the base styles.
 */
@Composable
fun ArticlesHeader(articleCount: Int, modifier: Modifier = Modifier) {
    val tokens = LocalPortfolioTokens.current

    val finalModifier = ArticlesHeaderStyle
        .toModifier()
        .then(modifier)

    Div(attrs = finalModifier.toAttrs()) {
        Div(attrs = Modifier.toAttrs()) {
            MetaLabel(
                text = META_TEXT,
                variant = MetaLabelVariant.Accent,
                modifier = Modifier
                    .display(DisplayStyle.Block)
                    .margin(bottom = META_MARGIN_BOTTOM_REM.cssRem),
            )
            H1(
                attrs = Modifier
                    .apply(Typography.h1)
                    .color(tokens.ink.ink)
                    .margin(topBottom = 0.cssRem)
                    .toAttrs(),
            ) {
                ComposeText(TITLE_TEXT)
            }
        }
        P(
            attrs = ArticlesHeaderSummaryStyle
                .toModifier()
                .color(tokens.muted.muted)
                .toAttrs(),
        ) {
            ComposeText("$articleCount $POSTS_SUFFIX$SEPARATOR$RSS_COPY$SEPARATOR$SUBSCRIBERS_COPY")
        }
    }
}

/**
 * Base `CssStyle` for [ArticlesHeader]. Ports `.articles-hdr` from the handoff:
 * a `1fr auto` grid with `align-items: end` and a 32px gap, wrapped in the
 * density-pinned vertical padding. Below [Breakpoint.MD] the grid collapses to
 * a single column so the summary copy stacks below the title block on narrow
 * viewports.
 */
val ArticlesHeaderStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Grid)
            .gridTemplateColumns {
                size(HEADER_GRID_FLEX_COL_FR.fr)
            }
            .gap(HEADER_GRID_GAP_REM.cssRem)
            .alignItems(AlignItems.Start)
            .padding(
                top = HEADER_PADDING_TOP_REM.cssRem,
                bottom = HEADER_PADDING_BOTTOM_REM.cssRem,
            )
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
            .fontFamily(FontFamilies.MONO)
            .fontSize(META_COPY_SIZE_REM.cssRem)
            .margin(topBottom = 0.cssRem)
    }
}
