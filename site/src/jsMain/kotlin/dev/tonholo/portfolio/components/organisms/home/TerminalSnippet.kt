package dev.tonholo.portfolio.components.organisms.home

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.minWidth
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.color.SyntaxScale
import dev.tonholo.portfolio.theme.layout.unit.dp
import dev.tonholo.portfolio.theme.typography.FontFamilies
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.TagElement
import org.w3c.dom.HTMLElement
import org.jetbrains.compose.web.dom.Text as ComposeText

// Terminal card visuals. Handoff `.term` (styles.css lines 385-399):
//   font-family mono, font-size 13px, background --ink, color --paper-2,
//   border-radius 4px, padding 20px 24px, line-height 1.7, overflow hidden.
// Dark mode swaps: background --paper-2, color --ink, border 1px --paper-3.
private val TERM_FONT_SIZE = 13.dp
private const val TERM_LINE_HEIGHT = 1.7
private val TERM_BORDER_RADIUS = 4.dp
private val TERM_PADDING_Y = 20.dp
private val TERM_PADDING_X = 24.dp
private val TERM_BORDER_WIDTH = 1.dp

/**
 * One rendered line inside a [TerminalSnippet]. The [number] is the 1-indexed
 * gutter marker shown in the dim color; [tokens] is the syntax-highlighted
 * payload. The line itself provides no trailing newline; [TerminalSnippet]
 * inserts one between adjacent lines.
 */
data class TerminalLine(val number: Int, val tokens: List<TerminalToken>)

/**
 * One token inside a [TerminalLine]. The [text] is rendered verbatim (spaces
 * and angle brackets preserved); [kind] selects the color treatment per
 * [TerminalTokenKind].
 */
data class TerminalToken(val text: String, val kind: TerminalTokenKind = TerminalTokenKind.Plain)

/**
 * Token color axis for [TerminalToken]. Maps 1:1 to the handoff CSS classes:
 *
 * - [Plain]: inherited foreground (no `.term-*` class in the handoff).
 * - [Dim]: `.term-dim`, the muted gutter / comment color (`--muted-2`).
 * - [Keyword]: `.term-kw`, purple for Kotlin keywords (`fun`, `val`, `by`...).
 * - [Number]: `.term-num`, warm amber for numeric literals.
 * - [String]: `.term-str`, mint green for string literals.
 */
enum class TerminalTokenKind {
    Plain,
    Dim,
    Keyword,
    Number,
    String,
}

/**
 * Reusable terminal-style code card. Ports the `<pre class="term">` block
 * from the handoff (`home.jsx` Snippet lines 215-226 and `styles.css`
 * `.term` / `.term-*`): a dark-filled rounded card (inverted in dark mode),
 * with each line prefixed by a dim gutter number and tokens colored per
 * [TerminalTokenKind].
 *
 * The component owns no state; callers build the full [TerminalLine] list
 * (including the gutter numbers) so the component stays layout-agnostic and
 * can back other snippet surfaces besides [SnippetOfTheWeek].
 *
 * @param lines the list of rendered lines; each line is emitted in order and
 *   separated by a single literal newline inside the `<pre>`.
 * @param modifier optional [Modifier] chained after the base terminal styles.
 */
@Composable
fun TerminalSnippet(lines: List<TerminalLine>, modifier: Modifier = Modifier) {
    val tokens = LocalPortfolioTokens.current
    val colorMode = ColorMode.current
    val isDark = colorMode == ColorMode.DARK

    val background = if (isDark) tokens.paper.paper2 else tokens.ink.ink
    val foreground = if (isDark) tokens.ink.ink else tokens.paper.paper2

    var preModifier = TerminalSnippetStyle
        .toModifier()
        .backgroundColor(background)
        .color(foreground)

    if (isDark) {
        preModifier = preModifier.border(
            width = TERM_BORDER_WIDTH,
            style = LineStyle.Solid,
            color = tokens.paper.paper3,
        )
    }

    preModifier = preModifier.then(modifier)

    TagElement<HTMLElement>(
        tagName = "pre",
        applyAttrs = preModifier.toAttrs(),
    ) {
        TagElement<HTMLElement>(
            tagName = "code",
            applyAttrs = null,
        ) {
            lines.forEachIndexed { index, line ->
                TerminalLineRow(line = line)
                if (index != lines.lastIndex) {
                    ComposeText("\n")
                }
            }
        }
    }
}

/**
 * Emits a single [TerminalLine]: the gutter number (dim) followed by each
 * [TerminalToken] rendered in a color-keyed `<span>`. Kept small and private
 * so the public [TerminalSnippet] stays a pure assembly function. Reads the
 * syntax swatch from [LocalPortfolioTokens] so the palette follows
 * [ColorMode] without the caller having to thread `isDark` down.
 */
@Composable
private fun TerminalLineRow(line: TerminalLine) {
    val tokens = LocalPortfolioTokens.current

    Span(
        attrs = Modifier
            .color(tokens.muted.muted2)
            .toAttrs(),
    ) {
        ComposeText(formatGutter(line.number))
    }
    line.tokens.forEach { token ->
        val color = tokenColor(
            kind = token.kind,
            syntax = tokens.syntax,
            dimColor = tokens.muted.muted2,
        )
        if (color != null) {
            Span(
                attrs = Modifier.color(color).toAttrs(),
            ) {
                ComposeText(token.text)
            }
        } else {
            ComposeText(token.text)
        }
    }
}

/**
 * Resolves the foreground color for a token [kind]. Returns `null` for
 * [TerminalTokenKind.Plain] so the caller can skip wrapping the text in a
 * `<span>` and let it inherit the terminal's base color.
 *
 * The keyword / string / number hues come from [SyntaxScale]; the caller
 * passes the already-resolved swatch so this helper stays pure. Light and
 * dark page modes each provide a palette tuned for the inverted terminal
 * card surface.
 *
 * @param kind the token color axis.
 * @param syntax the [SyntaxScale] for the active color mode.
 * @param dimColor the `MutedScale.muted2` color used for [TerminalTokenKind.Dim].
 */
private fun tokenColor(kind: TerminalTokenKind, syntax: SyntaxScale, dimColor: Color): Color? =
    when (kind) {
        TerminalTokenKind.Plain -> null
        TerminalTokenKind.Dim -> dimColor
        TerminalTokenKind.Keyword -> syntax.keyword
        TerminalTokenKind.Number -> syntax.number
        TerminalTokenKind.String -> syntax.string
    }

// Fixed gutter width of three columns: `  1 `, ` 12 `, `123 `. Matches the
// handoff JSX which pads 1-9 as `"1  "`, `"2  "`, ..., `"9  "` and `"10 "`.
private const val GUTTER_WIDTH = 3

/** Formats a line number into a three-column, trailing-space gutter label. */
private fun formatGutter(number: Int): String =
    number.toString().padEnd(length = GUTTER_WIDTH, padChar = ' ')

/**
 * Base `CssStyle` for the `<pre class="term">` surface. Ports the static
 * typography, radius, padding, and overflow from the handoff `.term` rule.
 * Background, foreground, and the dark-mode hairline border are applied
 * inline by [TerminalSnippet] so they stay reactive to [ColorMode].
 */
val TerminalSnippetStyle = CssStyle {
    base {
        Modifier
            .fontFamily(*FontFamilies.MONO)
            .fontSize(TERM_FONT_SIZE)
            .lineHeight(TERM_LINE_HEIGHT)
            .borderRadius(TERM_BORDER_RADIUS)
            .padding(
                topBottom = TERM_PADDING_Y,
                leftRight = TERM_PADDING_X,
            )
            .margin(topBottom = 0.cssRem)
            // Allow the card to scroll its unwrapped code lines horizontally
            // without pushing the parent grid past the viewport on mobile.
            .minWidth(0.px)
            .overflow {
                x(Overflow.Auto)
                y(Overflow.Hidden)
            }
    }
}
