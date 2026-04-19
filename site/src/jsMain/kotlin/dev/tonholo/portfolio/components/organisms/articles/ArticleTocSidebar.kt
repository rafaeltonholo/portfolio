package dev.tonholo.portfolio.components.organisms.articles

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.ListStyleType
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.css.TextTransform
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.alignSelf
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.flexDirection
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.listStyle
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.modifiers.textTransform
import com.varabyte.kobweb.compose.ui.modifiers.top
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.typography.FontFamilies
import org.jetbrains.compose.web.css.AlignSelf
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Aside
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Ul
import org.jetbrains.compose.web.dom.Text as ComposeText

// Handoff `.article-detail .sidebar` (styles.css 1088-1103):
//   font-family mono; font-size 11px; color muted; position sticky;
//   top 100px; align-self start.
private const val SIDEBAR_FONT_SIZE_REM = 0.6875 // 11px
private const val SIDEBAR_STICKY_TOP_REM = 6.25 // 100px

// Handoff `.article-detail .sidebar h5` (styles.css 1096-1099):
//   font-size 11px; letter-spacing 0.08em; uppercase; color ink-3;
//   margin-bottom 10px.
private const val HEADING_FONT_SIZE_REM = 0.6875 // 11px
private const val HEADING_LETTER_SPACING_EM = 0.08
private const val HEADING_MARGIN_BOTTOM_REM = 0.625 // 10px

// Handoff `.article-detail .sidebar ul` (styles.css 1100):
//   list-style none; flex column; gap 8px.
private const val LIST_GAP_REM = 0.5 // 8px

// Handoff inline `style={{marginTop:32}}` on the second h5 (pages1.jsx 168).
private const val SHARE_HEADING_MARGIN_TOP_REM = 2.0 // 32px

// Numeric prefix width: "01", "02", ... with a zero pad.
private const val ENTRY_NUMBER_PAD_LENGTH = 2
private const val ENTRY_NUMBER_PAD_CHAR = '0'

// Separator between the entry number and the label. Handoff uses an em-dash;
// the repo style forbids em-dashes so we fall back to a regular hyphen.
private const val ENTRY_SEPARATOR = " - "

// Share copy is sourced from `LocalStrings.articlesSidebar` so the two share
// rows that would normally wire twitter / linkedin intents can reuse the same
// translation table once those integrations land.

/**
 * One entry in the sidebar table of contents. [id] is the matching anchor id
 * on an in-body heading; [label] is the rendered title. The sidebar prefixes
 * each entry with a 1-indexed two-digit counter (e.g. `01 - The problem`).
 */
data class TocEntry(val id: String, val label: String)

/**
 * Sticky table-of-contents + share sidebar shown to the left of the article
 * body on detail pages. Ports the `.article-detail .sidebar` block from the
 * handoff (styles.css 1088-1103 and pages1.jsx 161-173):
 *
 * - Two mono `<h5>` sections: `Contents` and `Share`.
 * - The contents list numbers each entry (`01 - Label`) and highlights the
 *   [activeId] entry in the ink tone; non-active entries use the muted tone.
 * - The share list exposes three actions; `copy link` invokes [onCopyLink],
 *   while the twitter and linkedin intents are rendered as placeholders until
 *   the share integration lands.
 *
 * @param contents table-of-contents entries, rendered in order with a
 *   two-digit counter prefix.
 * @param activeId the id of the entry to highlight; falls back to the muted
 *   tone when no entry matches.
 * @param onCopyLink invoked when the `copy link` share action is clicked.
 * @param modifier optional [Modifier] chained after the base sidebar styles.
 */
@Composable
fun ArticleTocSidebar(
    contents: List<TocEntry>,
    activeId: String,
    onCopyLink: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tokens = LocalPortfolioTokens.current
    val strings = LocalStrings.current.articlesSidebar

    val finalModifier = ArticleTocSidebarStyle
        .toModifier()
        .color(tokens.muted.muted)
        .then(modifier)

    Aside(attrs = finalModifier.toAttrs()) {
        SidebarHeading(text = strings.contents, marginTopRem = 0.0)
        Ul(attrs = SidebarListStyle.toModifier().toAttrs()) {
            contents.forEachIndexed { index, entry ->
                TocAnchorRow(
                    entry = entry,
                    index = index,
                    activeId = activeId,
                    mutedColor = tokens.muted.muted,
                    activeColor = tokens.ink.ink,
                )
            }
        }
        SidebarHeading(text = strings.share, marginTopRem = SHARE_HEADING_MARGIN_TOP_REM)
        Ul(attrs = SidebarListStyle.toModifier().toAttrs()) {
            ShareAnchorRow(label = strings.shareCopyLink, onClick = onCopyLink, mutedColor = tokens.muted.muted)
            ShareAnchorRow(label = strings.shareTwitter, onClick = null, mutedColor = tokens.muted.muted)
            ShareAnchorRow(label = strings.shareLinkedIn, onClick = null, mutedColor = tokens.muted.muted)
        }
    }
}

/**
 * One contents row: renders the two-digit counter and label inside an anchor.
 * Active rows adopt [activeColor] (ink) while all others stay muted.
 */
@Composable
private fun TocAnchorRow(
    entry: TocEntry,
    index: Int,
    activeId: String,
    mutedColor: Color,
    activeColor: Color,
) {
    val isActive = entry.id == activeId
    val resolvedColor = if (isActive) activeColor else mutedColor
    val number = (index + 1).toString().padStart(ENTRY_NUMBER_PAD_LENGTH, ENTRY_NUMBER_PAD_CHAR)
    val label = "$number$ENTRY_SEPARATOR${entry.label}"

    Li(attrs = Modifier.toAttrs()) {
        A(
            href = "#${entry.id}",
            attrs = Modifier
                .color(resolvedColor)
                .textDecorationLine(TextDecorationLine.None)
                .toAttrs(),
        ) {
            ComposeText(label)
        }
    }
}

/**
 * One share row: an anchor that either invokes [onClick] (for `copy link`) or
 * renders as a non-navigating placeholder for twitter / linkedin until the
 * share integration lands.
 */
@Composable
private fun ShareAnchorRow(label: String, onClick: (() -> Unit)?, mutedColor: Color) {
    Li(attrs = Modifier.toAttrs()) {
        val baseModifier = Modifier
            .color(mutedColor)
            .cursor(Cursor.Pointer)
            .textDecorationLine(TextDecorationLine.None)

        val finalModifier = if (onClick != null) {
            baseModifier.onClick { event ->
                event.preventDefault()
                onClick()
            }
        } else {
            baseModifier
        }

        A(href = "#", attrs = finalModifier.toAttrs()) {
            ComposeText(label)
        }
    }
}

/**
 * Mono sidebar heading used by both the contents and share sections.
 * Rendered as an `<h2>` - the sidebar is a peer section to the article body
 * under the page `<h1>`. Visual weight is kept small via font-size; the
 * semantic level is what screen readers consume. [marginTopRem] is set
 * inline so the first heading can collapse its top margin while the
 * second heading reproduces the handoff's `marginTop: 32px` inline style.
 */
@Composable
private fun SidebarHeading(text: String, marginTopRem: Double) {
    val tokens = LocalPortfolioTokens.current

    H2(
        attrs = Modifier
            .fontFamily(*FontFamilies.MONO)
            .fontSize(HEADING_FONT_SIZE_REM.cssRem)
            .letterSpacing(HEADING_LETTER_SPACING_EM.em)
            .textTransform(TextTransform.Uppercase)
            .color(tokens.ink.ink3)
            .margin(top = marginTopRem.cssRem, bottom = HEADING_MARGIN_BOTTOM_REM.cssRem)
            .toAttrs(),
    ) {
        ComposeText(text)
    }
}

/**
 * Base `CssStyle` for the sticky sidebar wrapper. Ports the static pieces of
 * `.article-detail .sidebar`: mono font, 11px font-size, sticky positioning,
 * 100px top offset, and `align-self: start` so the sidebar does not stretch
 * to the body column's full height. The muted base color is applied inline
 * by [ArticleTocSidebar] so it stays reactive to the active color mode.
 */
val ArticleTocSidebarStyle = CssStyle {
    base {
        Modifier
            .fontFamily(*FontFamilies.MONO)
            .fontSize(SIDEBAR_FONT_SIZE_REM.cssRem)
            .position(Position.Sticky)
            .top(SIDEBAR_STICKY_TOP_REM.cssRem)
            .alignSelf(AlignSelf.Start)
    }
}

/**
 * Base `CssStyle` for the two `<ul>` lists inside the sidebar. Ports
 * `.article-detail .sidebar ul`: no markers, vertical flex stack with an 8px
 * gap between rows, and zero intrinsic padding so the list aligns with its
 * preceding heading.
 */
val SidebarListStyle = CssStyle {
    base {
        Modifier
            .listStyle(type = ListStyleType.None)
            .display(DisplayStyle.Flex)
            .flexDirection(FlexDirection.Column)
            .gap(LIST_GAP_REM.cssRem)
            .margin(topBottom = 0.cssRem)
            .padding(left = 0.cssRem)
    }
}
