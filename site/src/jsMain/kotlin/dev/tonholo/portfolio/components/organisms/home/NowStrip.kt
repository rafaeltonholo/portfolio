package dev.tonholo.portfolio.components.organisms.home

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontStyle
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.ListStyleType
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.flexDirection
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontStyle
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.listStyle
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.components.atoms.MetaLabel
import dev.tonholo.portfolio.components.molecules.SectionHead
import dev.tonholo.portfolio.domain.model.Article
import dev.tonholo.portfolio.domain.model.ArticleStatus
import dev.tonholo.portfolio.state.StaticContent
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.layout.ShellStyle
import dev.tonholo.portfolio.theme.typography.FontFamilies
import dev.tonholo.portfolio.theme.typography.Typography
import dev.tonholo.portfolio.theme.typography.apply
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Section
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Ul
import org.jetbrains.compose.web.dom.Text as ComposeText

private const val SECTION_NUMBER = "// 01"
private const val SECTION_TITLE = "Currently"

// Grid layout per handoff: `grid-template-columns: 1.4fr 1fr 1fr; gap: 48px`.
private const val NOW_GRID_WIDE_FR = 1.4
private const val NOW_GRID_NARROW_FR = 1
private const val NOW_GRID_GAP_REM = 3.0 // 48px

// Spacing between the mono meta label and the column content.
private const val META_MARGIN_BOTTOM_REM = 0.75 // 12px

// Working column: body copy width capped to 48ch.
private const val BODY_MAX_WIDTH_CH = 48.0

// Writing list: gap between article entries; small-date uses metaSmall.
private const val WRITING_LIST_GAP_REM = 0.875 // 14px
private const val WRITING_TITLE_SIZE_REM = 1.0 // 16px

// Off-the-clock list: gap between hobby rows; body copy.
private const val HOBBY_LIST_GAP_REM = 0.625 // 10px
private const val WRITING_ARTICLES_TAKE = 3

// Title spacing under the meta label in the working column.
private const val WORKING_TITLE_MARGIN_BOTTOM_REM = 0.75 // 12px

/**
 * Home organism: the three-column "Currently" strip. Ports the `NowStrip`
 * component from the handoff JSX (`home.jsx`) and the matching
 * `.section-head`, `.now-grid` rules in the handoff CSS.
 *
 * Layout: a `SectionHead` followed by a three-column grid (`1.4fr 1fr 1fr`)
 * with a 48px gap. The grid collapses to a single column below
 * [Breakpoint.LG], matching the handoff media query.
 *
 * Columns:
 * 1. **working on**: mono meta label + display-font title + serif body copy.
 * 2. **writing**: mono meta label + a stacked list of the three most recent
 *    entries from [StaticContent.ARTICLES]; each entry renders a small meta
 *    row (`draft|published - date`) plus a display-font title.
 * 3. **off the clock**: mono meta label + a body-voice bullet list of hobby
 *    rows (hardcoded inline per the handoff).
 *
 * Data is sourced from [StaticContent]: [StaticContent.NOW_STRIP] for the
 * first column's title + body, and [StaticContent.ARTICLES] for the writing
 * list. The hobby list matches the handoff literally (the current
 * `NowStripEntry` shape cannot express structured bullets).
 *
 * @param modifier optional [Modifier] chained after the base section styles.
 */
@Composable
fun NowStrip(modifier: Modifier = Modifier) {
    val workingEntry = StaticContent.NOW_STRIP.firstOrNull()
    val recentArticles = StaticContent.ARTICLES.take(WRITING_ARTICLES_TAKE)

    val sectionModifier = NowStripStyle
        .toModifier()
        .then(modifier)

    Section(attrs = sectionModifier.toAttrs()) {
        Div(attrs = ShellStyle.toModifier().toAttrs()) {
            SectionHead(number = SECTION_NUMBER, title = SECTION_TITLE)
            Div(attrs = NowGridStyle.toModifier().toAttrs()) {
                WorkingOnColumn(
                    title = workingEntry?.title ?: DEFAULT_WORKING_TITLE,
                    body = workingEntry?.body ?: DEFAULT_WORKING_BODY,
                )
                WritingColumn(articles = recentArticles)
                OffTheClockColumn()
            }
        }
    }
}

// Fallbacks guard against the NOW_STRIP list being empty; keep the copy aligned
// with the handoff so the rendered section stays meaningful.
private const val DEFAULT_WORKING_TITLE =
    "A dynamic-feature system that doesn't fight the dev loop."
private const val DEFAULT_WORKING_BODY =
    "Module graphs that scale past 400 modules, without making local " +
        "compile-times tragic. Turns out the fix was in Gradle, mostly."

/**
 * First column: the mono "working on" meta label, a display-font title, and
 * a serif body paragraph capped at 48ch. Mirrors the first `<div>` inside
 * `.now-grid` in the handoff JSX.
 */
@Composable
private fun WorkingOnColumn(title: String, body: String) {
    val tokens = LocalPortfolioTokens.current

    Div(attrs = Modifier.toAttrs()) {
        MetaLabel(
            text = LABEL_WORKING_ON,
            modifier = Modifier
                .display(DisplayStyle.Block)
                .margin(bottom = META_MARGIN_BOTTOM_REM.cssRem),
        )
        H3(
            attrs = Modifier
                .apply(Typography.h3Display)
                .color(tokens.ink.ink)
                .margin(
                    top = 0.cssRem,
                    bottom = WORKING_TITLE_MARGIN_BOTTOM_REM.cssRem,
                )
                .toAttrs(),
        ) {
            ComposeText(title)
        }
        P(
            attrs = Modifier
                .apply(Typography.serifBody)
                .color(tokens.ink.ink2)
                .margin(topBottom = 0.cssRem)
                .maxWidth(BODY_MAX_WIDTH_CH.unsafeCast<CSSSizeValue<CSSUnit.ch>>())
                .toAttrs(),
        ) {
            ComposeText(body)
        }
    }
}

/**
 * Second column: the mono "writing" meta label above a stacked list of the
 * three most recent [Article]s. Each entry renders a small mono meta row
 * (`draft|published - short date`) and a display-font title.
 */
@Composable
private fun WritingColumn(articles: List<Article>) {
    Div(attrs = Modifier.toAttrs()) {
        MetaLabel(
            text = LABEL_WRITING,
            modifier = Modifier
                .display(DisplayStyle.Block)
                .margin(bottom = META_MARGIN_BOTTOM_REM.cssRem),
        )
        Ul(attrs = WritingListStyle.toModifier().toAttrs()) {
            articles.forEach { article ->
                WritingEntry(article = article)
            }
        }
    }
}

/**
 * One row inside the writing column: a small mono meta line followed by the
 * article title rendered in the display font.
 */
@Composable
private fun WritingEntry(article: Article) {
    val tokens = LocalPortfolioTokens.current
    val statusLabel = when (article.status) {
        ArticleStatus.Draft -> STATUS_DRAFT
        ArticleStatus.Published -> STATUS_PUBLISHED
    }
    val shortDate = article.date.substringBefore(delimiter = ',')
    val metaText = "$statusLabel $STATUS_SEPARATOR $shortDate"

    Li(attrs = Modifier.toAttrs()) {
        Div(
            attrs = Modifier
                .apply(Typography.metaSmall)
                .color(tokens.muted.muted)
                .toAttrs(),
        ) {
            ComposeText(metaText)
        }
        Div(
            attrs = Modifier
                .fontFamily(*FontFamilies.DISPLAY)
                .fontSize(WRITING_TITLE_SIZE_REM.cssRem)
                .fontWeight(FontWeight.Medium)
                .color(tokens.ink.ink)
                .toAttrs(),
        ) {
            ComposeText(article.title)
        }
    }
}

/**
 * Third column: the mono "off the clock" meta label above a body-voice list
 * of hobbies. Matches the hardcoded JSX list from the handoff (lines 182-188).
 */
@Composable
private fun OffTheClockColumn() {
    val tokens = LocalPortfolioTokens.current

    Div(attrs = Modifier.toAttrs()) {
        MetaLabel(
            text = LABEL_OFF_THE_CLOCK,
            modifier = Modifier
                .display(DisplayStyle.Block)
                .margin(bottom = META_MARGIN_BOTTOM_REM.cssRem),
        )
        Ul(
            attrs = HobbyListStyle
                .toModifier()
                .apply(Typography.bodySmall)
                .color(tokens.ink.ink2)
                .toAttrs(),
        ) {
            HobbyRow(label = HOBBY_COOKING_LABEL, body = HOBBY_COOKING_BODY)
            HobbyRow(label = HOBBY_TEA_LABEL, body = HOBBY_TEA_BODY)
            HobbyRow(label = HOBBY_RUNNING_LABEL, body = HOBBY_RUNNING_BODY)
            HobbyReadingRow()
        }
    }
}

/**
 * Plain hobby row: bold body label inline with a free-form body description.
 */
@Composable
private fun HobbyRow(label: String, body: String) {
    val tokens = LocalPortfolioTokens.current

    Li(attrs = Modifier.toAttrs()) {
        B(
            attrs = Modifier
                .color(tokens.ink.ink)
                .toAttrs(),
        ) {
            ComposeText(label)
        }
        ComposeText(" $HOBBY_SEPARATOR $body")
    }
}

/**
 * Reading hobby row: like [HobbyRow] but the book title is rendered in the
 * serif font and italicized, matching the handoff JSX.
 */
@Composable
private fun HobbyReadingRow() {
    val tokens = LocalPortfolioTokens.current

    Li(attrs = Modifier.toAttrs()) {
        B(
            attrs = Modifier
                .color(tokens.ink.ink)
                .toAttrs(),
        ) {
            ComposeText(HOBBY_READING_LABEL)
        }
        ComposeText(" $HOBBY_SEPARATOR ")
        Span(
            attrs = Modifier
                .fontFamily(*FontFamilies.SERIF)
                .fontStyle(FontStyle.Italic)
                .toAttrs(),
        ) {
            ComposeText(HOBBY_READING_TITLE)
        }
    }
}

// Column labels kept here so the organism is self-contained; matches the copy
// shown in the handoff JSX.
private const val LABEL_WORKING_ON = "working on"
private const val LABEL_WRITING = "writing"
private const val LABEL_OFF_THE_CLOCK = "off the clock"

// Meta row labels for the writing column.
private const val STATUS_DRAFT = "draft"
private const val STATUS_PUBLISHED = "published"
private const val STATUS_SEPARATOR = "\u00B7" // middle dot, matches handoff

// Hobby copy ported verbatim from the handoff JSX; the `NowStripEntry` shape
// cannot express this structure, so the content is inline.
private const val HOBBY_SEPARATOR = "-"
private const val HOBBY_COOKING_LABEL = "cooking"
private const val HOBBY_COOKING_BODY = "ragu, most Sundays"
private const val HOBBY_TEA_LABEL = "tea"
private const val HOBBY_TEA_BODY = "loose-leaf sencha, mornings"
private const val HOBBY_RUNNING_LABEL = "running"
private const val HOBBY_RUNNING_BODY = "Ottawa river path"
private const val HOBBY_READING_LABEL = "reading"
private const val HOBBY_READING_TITLE = "Designing Data-Intensive Applications"

/**
 * Base `CssStyle` for the outer `<section>` wrapping the whole strip. Kept
 * empty at the base level so the inner [ShellStyle] owns the horizontal
 * gutters; callers can chain extra spacing via [Modifier].
 */
val NowStripStyle = CssStyle {}

/**
 * Base `CssStyle` for `.now-grid`. Single column at the base width; switches
 * to the handoff `1.4fr 1fr 1fr` three-column layout at [Breakpoint.LG].
 * The 48px gap applies in both modes.
 */
val NowGridStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Grid)
            .gridTemplateColumns {
                size(NOW_GRID_NARROW_FR.fr)
            }
            .gap(NOW_GRID_GAP_REM.cssRem)
    }
    Breakpoint.LG {
        Modifier.gridTemplateColumns {
            size(NOW_GRID_WIDE_FR.fr)
            size(NOW_GRID_NARROW_FR.fr)
            size(NOW_GRID_NARROW_FR.fr)
        }
    }
}

/**
 * Base `CssStyle` for the writing column's `<ul>`. Removes the default bullet
 * marker and padding; stacks entries as a flex column with a 14px gap so each
 * article keeps a consistent rhythm.
 */
val WritingListStyle = CssStyle {
    base {
        Modifier
            .listStyle(ListStyleType.None)
            .margin(topBottom = 0.cssRem)
            .padding(0.cssRem)
            .display(DisplayStyle.Flex)
            .flexDirection(FlexDirection.Column)
            .gap(WRITING_LIST_GAP_REM.cssRem)
    }
}

/**
 * Base `CssStyle` for the off-the-clock column's `<ul>`. Same unmarked stack
 * as [WritingListStyle] but with a tighter 10px gap, matching the handoff.
 * Typography is applied inline by the composable so it stays token-aware.
 */
val HobbyListStyle = CssStyle {
    base {
        Modifier
            .listStyle(ListStyleType.None)
            .margin(topBottom = 0.cssRem)
            .padding(0.cssRem)
            .display(DisplayStyle.Flex)
            .flexDirection(FlexDirection.Column)
            .gap(HOBBY_LIST_GAP_REM.cssRem)
    }
}
