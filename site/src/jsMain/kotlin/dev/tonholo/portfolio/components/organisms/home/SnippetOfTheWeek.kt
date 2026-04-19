package dev.tonholo.portfolio.components.organisms.home

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.flexDirection
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.components.atoms.MetaLabel
import dev.tonholo.portfolio.components.atoms.MetaSeparator
import dev.tonholo.portfolio.components.template.NumberedSection
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.locale.Strings
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.layout.unit.dp
import dev.tonholo.portfolio.theme.typography.FontFamilies
import dev.tonholo.portfolio.theme.typography.Typography
import dev.tonholo.portfolio.theme.typography.apply
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text as ComposeText

// Ordinal for the Snippet of the week section: renders as `// 02`.
private const val SECTION_ORDINAL = 2
private const val SECTION_TRAILING_WEEK = "week 15"
private const val SECTION_TRAILING_YEAR = "2026"

// Inline gap between the trailing label segments and the centered separator dot.
private val SECTION_TRAILING_GAP = 6.dp

// Two-column grid (handoff line 202): `1fr 1.3fr`, 48px gap, align-items start.
// Collapses to 1 column below LG per the breakpoint convention used in this
// repo (see `NowStripStyle` / `UsesGridStyle`).
private const val GRID_COL_COPY_FR = 1.0
private const val GRID_COL_TERM_FR = 1.3
private val GRID_GAP = 48.dp

// Copy column typography. Handoff inline styles on `<h3>` (line 204):
//   font-size 28px (= 1.75rem), font-weight 500, line-height 1.1,
//   letter-spacing -0.02em, margin-bottom 16px. Size and weight come from
//   `Typography.h2`; `line-height` is overridden inline (h2 defaults to 1.15).
private const val TITLE_LINE_HEIGHT = 1.1
private val TITLE_MARGIN_BOTTOM = 16.dp

// Body paragraph (handoff line 207): serif 17px / line-height 1.6, max-width
// 48ch, margin-bottom 20px, color ink-2.
private val BODY_MARGIN_BOTTOM = 20.dp
private const val BODY_MAX_WIDTH_CH = 48.0

// Meta row (handoff line 210): mono 12px, muted, 4px gap between rows. Copy
// retains its handoff casing (`// debounced-click.kt`), so we cannot reuse
// `Typography.metaLabel` (which uppercases via `text-transform`).
private val META_ROW_FONT_SIZE = 12.dp
private val META_ROW_GAP = 4.dp

// Mono meta rows stay as technical literals (file path, last-edited line);
// the editorial title and body are sourced from [LocalStrings].
private const val META_FILE_NAME = "// debounced-click.kt"
private const val META_FILE_SIZE = "542 B"
private const val META_EDITED_ROW = "// last edited 2 days ago"

// Inline gap between the file-row segments and the centered separator dot.
private val META_FILE_ROW_GAP = 6.dp

/**
 * Home page organism: the "Snippet of the week" block. Ports the `Snippet`
 * component from the handoff JSX (`home.jsx` lines 194-229) and the
 * `.section`, `.shell`, and `.term`-family rules from `styles.css`.
 *
 * Layout: a [SectionHead] (`// 02 Snippet of the week ... week 15 - 2026`)
 * followed by a two-column grid (`1fr 1.3fr`, 48px gap). The left column
 * holds the display-font title, serif body copy, and two mono meta rows; the
 * right column delegates to [TerminalSnippet] with a hardcoded 10-line
 * Kotlin excerpt of `Modifier.debouncedClickable`. The grid collapses to a
 * single column below [Breakpoint.LG].
 *
 * @param modifier optional [Modifier] chained after the base section styles.
 */
@Composable
fun SnippetOfTheWeek(modifier: Modifier = Modifier) {
    val strings = LocalStrings.current.snippet

    NumberedSection(
        number = SECTION_ORDINAL,
        title = strings.sectionTitle,
        modifier = modifier,
        trailing = {
            Span(
                attrs = Modifier
                    .display(DisplayStyle.LegacyInlineFlex)
                    .alignItems(AlignItems.Center)
                    .gap(SECTION_TRAILING_GAP)
                    .toAttrs(),
            ) {
                MetaLabel(text = SECTION_TRAILING_WEEK)
                MetaSeparator()
                MetaLabel(text = SECTION_TRAILING_YEAR)
            }
        },
    ) {
        Div(attrs = SnippetGridStyle.toModifier().toAttrs()) {
            CopyColumn(strings = strings)
            TerminalSnippet(lines = DEBOUNCED_CLICKABLE_LINES)
        }
    }
}

/**
 * Left column of [SnippetOfTheWeek]: display-font title, serif body, and a
 * two-row mono meta block (file name and "last edited" line). Copy for the
 * title + body is passed in so the parent hoists the [LocalStrings] read.
 */
@Composable
private fun CopyColumn(strings: Strings.Snippet) {
    val tokens = LocalPortfolioTokens.current

    Div(attrs = Modifier.toAttrs()) {
        H3(
            attrs = Modifier
                .apply(Typography.h2)
                .lineHeight(TITLE_LINE_HEIGHT)
                .color(tokens.ink.ink)
                .margin(top = 0.cssRem, bottom = TITLE_MARGIN_BOTTOM)
                .toAttrs(),
        ) {
            ComposeText(strings.copyTitle)
        }
        P(
            attrs = Modifier
                .apply(Typography.serifBody)
                .color(tokens.ink.ink2)
                .margin(top = 0.cssRem, bottom = BODY_MARGIN_BOTTOM)
                .maxWidth(BODY_MAX_WIDTH_CH.unsafeCast<CSSSizeValue<CSSUnit.ch>>())
                .toAttrs(),
        ) {
            ComposeText(strings.copyBody)
        }
        Div(attrs = MetaRowsStyle.toModifier().toAttrs()) {
            FileMetaRow(color = tokens.muted.muted)
            MetaRow(text = META_EDITED_ROW, color = tokens.muted.muted)
        }
    }
}

/**
 * One line in the two-row mono meta block under the body copy. Kept private
 * so [CopyColumn] stays flat; color comes in from the caller so the tokens
 * are read once in the parent composable.
 */
@Composable
private fun MetaRow(text: String, color: Color) {
    Div(
        attrs = Modifier
            .fontFamily(*FontFamilies.MONO)
            .fontSize(META_ROW_FONT_SIZE)
            .color(color)
            .toAttrs(),
    ) {
        ComposeText(text)
    }
}

/**
 * File-name meta row that pairs the snippet path with its byte size around a
 * [MetaSeparator] dot, replacing the prior literal middle-dot glyph.
 */
@Composable
private fun FileMetaRow(color: Color) {
    Div(
        attrs = Modifier
            .display(DisplayStyle.Flex)
            .alignItems(AlignItems.Center)
            .gap(META_FILE_ROW_GAP)
            .fontFamily(*FontFamilies.MONO)
            .fontSize(META_ROW_FONT_SIZE)
            .color(color)
            .toAttrs(),
    ) {
        ComposeText(META_FILE_NAME)
        MetaSeparator(color = color)
        ComposeText(META_FILE_SIZE)
    }
}

// ---------------------------------------------------------------------------
// Hardcoded snippet payload. Ports the 10 lines of Kotlin shown in the
// handoff JSX (lines 216-225). Tokens mark keywords, numbers, and the one
// type literal (`Long`, `Unit`) the handoff colors as keywords; the rest is
// [TerminalTokenKind.Plain] so the terminal's inherited foreground applies.
// ---------------------------------------------------------------------------

private val DEBOUNCED_CLICKABLE_LINES: List<TerminalLine> = listOf(
    TerminalLine(
        number = 1,
        tokens = listOf(
            TerminalToken(text = "fun", kind = TerminalTokenKind.Keyword),
            TerminalToken(text = " Modifier.debouncedClickable("),
        ),
    ),
    TerminalLine(
        number = 2,
        tokens = listOf(
            TerminalToken(text = "  debounceMs: "),
            TerminalToken(text = "Long", kind = TerminalTokenKind.Keyword),
            TerminalToken(text = " = "),
            TerminalToken(text = "500", kind = TerminalTokenKind.Number),
            TerminalToken(text = ","),
        ),
    ),
    TerminalLine(
        number = 3,
        tokens = listOf(
            TerminalToken(text = "  onClick: () -> "),
            TerminalToken(text = "Unit", kind = TerminalTokenKind.Keyword),
        ),
    ),
    TerminalLine(
        number = 4,
        tokens = listOf(
            TerminalToken(text = "): Modifier = composed {"),
        ),
    ),
    TerminalLine(
        number = 5,
        tokens = listOf(
            TerminalToken(text = "  "),
            TerminalToken(text = "var", kind = TerminalTokenKind.Keyword),
            TerminalToken(text = " last "),
            TerminalToken(text = "by", kind = TerminalTokenKind.Keyword),
            TerminalToken(text = " remember { mutableLongStateOf(0L) }"),
        ),
    ),
    TerminalLine(
        number = 6,
        tokens = listOf(
            TerminalToken(text = "  clickable {"),
        ),
    ),
    TerminalLine(
        number = 7,
        tokens = listOf(
            TerminalToken(text = "    "),
            TerminalToken(text = "val", kind = TerminalTokenKind.Keyword),
            TerminalToken(text = " now = System.currentTimeMillis()"),
        ),
    ),
    TerminalLine(
        number = 8,
        tokens = listOf(
            TerminalToken(text = "    "),
            TerminalToken(text = "if", kind = TerminalTokenKind.Keyword),
            TerminalToken(text = " (now - last > debounceMs) { last = now; onClick() }"),
        ),
    ),
    TerminalLine(
        number = 9,
        tokens = listOf(
            TerminalToken(text = "  }"),
        ),
    ),
    TerminalLine(
        number = 10,
        tokens = listOf(
            TerminalToken(text = "}"),
        ),
    ),
)

/**
 * Base `CssStyle` for the two-column grid wrapping the copy column and the
 * [TerminalSnippet]. Single column below [Breakpoint.LG]; splits to `1fr 1.3fr`
 * with a 48px gap at `LG` and up, matching the handoff inline style on the
 * `<div className="snip-grid">`.
 */
val SnippetGridStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Grid)
            .gridTemplateColumns {
                size(GRID_COL_COPY_FR.fr)
            }
            .gap(GRID_GAP)
            .alignItems(AlignItems.Start)
    }
    Breakpoint.LG {
        Modifier.gridTemplateColumns {
            size(GRID_COL_COPY_FR.fr)
            size(GRID_COL_TERM_FR.fr)
        }
    }
}

/**
 * Base `CssStyle` for the stacked meta rows under the body copy: flex column
 * with a tight 4px gap so the `// file` and `// last edited` lines read as a
 * pair rather than unrelated lines.
 */
val MetaRowsStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Flex)
            .flexDirection(FlexDirection.Column)
            .gap(META_ROW_GAP)
    }
}
