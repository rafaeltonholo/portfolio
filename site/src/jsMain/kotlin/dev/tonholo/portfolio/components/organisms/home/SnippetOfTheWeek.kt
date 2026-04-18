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
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.components.atoms.MetaLabel
import dev.tonholo.portfolio.components.molecules.SectionHead
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.layout.ShellStyle
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
import org.jetbrains.compose.web.dom.Section
import org.jetbrains.compose.web.dom.Text as ComposeText

// Outer `<section class="section shell">`: vertical padding keyed off `--pad-y`
// (80px base, per handoff `.section { padding: var(--pad-y) 0; }`).
private const val SECTION_PADDING_Y_REM = 5.0

// Ordinal prefix + week/year stamp are mono technical literals and stay
// stable across locales.
private const val SECTION_NUMBER = "// 02"
private const val SECTION_TRAILING_LABEL = "week 15 \u00B7 2026"

// Two-column grid (handoff line 202): `1fr 1.3fr`, 48px gap, align-items start.
// Collapses to 1 column below LG per the breakpoint convention used in this
// repo (see `NowStripStyle` / `UsesGridStyle`).
private const val GRID_COL_COPY_FR = 1.0
private const val GRID_COL_TERM_FR = 1.3
private const val GRID_GAP_REM = 3.0 // 48px

// Copy column typography. Handoff inline styles on `<h3>` (line 204):
//   font-size 28px (= 1.75rem), font-weight 500, line-height 1.1,
//   letter-spacing -0.02em, margin-bottom 16px. Size and weight come from
//   `Typography.h2`; `line-height` is overridden inline (h2 defaults to 1.15).
private const val TITLE_LINE_HEIGHT = 1.1
private const val TITLE_MARGIN_BOTTOM_REM = 1.0 // 16px

// Body paragraph (handoff line 207): serif 17px / line-height 1.6, max-width
// 48ch, margin-bottom 20px, color ink-2.
private const val BODY_MARGIN_BOTTOM_REM = 1.25 // 20px
private const val BODY_MAX_WIDTH_CH = 48.0

// Meta row (handoff line 210): mono 12px, muted, 4px gap between rows. Copy
// retains its handoff casing (`// debounced-click.kt`), so we cannot reuse
// `Typography.metaLabel` (which uppercases via `text-transform`).
private const val META_ROW_FONT_SIZE_REM = 0.75 // 12px
private const val META_ROW_GAP_REM = 0.25 // 4px

// Mono meta rows stay as technical literals (file path, last-edited line);
// the editorial title and body are sourced from [LocalStrings].
private const val META_FILE_ROW = "// debounced-click.kt \u00B7 542 B"
private const val META_EDITED_ROW = "// last edited 2 days ago"

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

    val sectionModifier = SnippetOfTheWeekSectionStyle
        .toModifier()
        .then(modifier)

    Section(attrs = sectionModifier.toAttrs()) {
        Div(attrs = ShellStyle.toModifier().toAttrs()) {
            SectionHead(
                number = SECTION_NUMBER,
                title = strings.sectionTitle,
                trailing = {
                    MetaLabel(text = SECTION_TRAILING_LABEL)
                },
            )
            Div(attrs = SnippetGridStyle.toModifier().toAttrs()) {
                CopyColumn()
                TerminalSnippet(lines = DEBOUNCED_CLICKABLE_LINES)
            }
        }
    }
}

/**
 * Left column of [SnippetOfTheWeek]: display-font title, serif body, and a
 * two-row mono meta block (file name and "last edited" line). All copy is
 * ported verbatim from the handoff JSX (lines 203-213).
 */
@Composable
private fun CopyColumn() {
    val tokens = LocalPortfolioTokens.current
    val strings = LocalStrings.current.snippet

    Div(attrs = Modifier.toAttrs()) {
        H3(
            attrs = Modifier
                .apply(Typography.h2)
                .lineHeight(TITLE_LINE_HEIGHT)
                .color(tokens.ink.ink)
                .margin(top = 0.cssRem, bottom = TITLE_MARGIN_BOTTOM_REM.cssRem)
                .toAttrs(),
        ) {
            ComposeText(strings.copyTitle)
        }
        P(
            attrs = Modifier
                .apply(Typography.serifBody)
                .color(tokens.ink.ink2)
                .margin(top = 0.cssRem, bottom = BODY_MARGIN_BOTTOM_REM.cssRem)
                .maxWidth(BODY_MAX_WIDTH_CH.unsafeCast<CSSSizeValue<CSSUnit.ch>>())
                .toAttrs(),
        ) {
            ComposeText(strings.copyBody)
        }
        Div(attrs = MetaRowsStyle.toModifier().toAttrs()) {
            MetaRow(text = META_FILE_ROW, color = tokens.muted.muted)
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
            .fontSize(META_ROW_FONT_SIZE_REM.cssRem)
            .color(color)
            .toAttrs(),
    ) {
        ComposeText(text)
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
 * Base `CssStyle` for the `<section>` wrapper. Mirrors the handoff `.section`
 * rule: vertical padding keyed off `--pad-y` (80px at the default density).
 */
val SnippetOfTheWeekSectionStyle = CssStyle {
    base {
        Modifier.padding(topBottom = SECTION_PADDING_Y_REM.cssRem)
    }
}

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
            .gap(GRID_GAP_REM.cssRem)
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
            .gap(META_ROW_GAP_REM.cssRem)
    }
}
