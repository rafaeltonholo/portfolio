package dev.tonholo.portfolio.components.organisms.home

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontStyle
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.ListStyleType
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
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
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.navigation.UncoloredLinkVariant
import com.varabyte.kobweb.silk.components.navigation.UndecoratedLinkVariant
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.selectors.hover
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.analytics.AnalyticsManager
import dev.tonholo.portfolio.analytics.LocalAnalyticsManager
import dev.tonholo.portfolio.analytics.events.AnalyticEvent
import dev.tonholo.portfolio.components.atoms.MetaLabel
import dev.tonholo.portfolio.components.atoms.MetaSeparator
import dev.tonholo.portfolio.components.template.NumberedSection
import dev.tonholo.portfolio.domain.model.Article
import dev.tonholo.portfolio.domain.model.ArticleStatus
import dev.tonholo.portfolio.domain.model.NowStripEntry
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.locale.Locale
import dev.tonholo.portfolio.locale.Strings
import dev.tonholo.portfolio.navigation.Routes
import dev.tonholo.portfolio.state.LocalLocaleState
import dev.tonholo.portfolio.state.StaticContent
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.PortfolioTokens
import dev.tonholo.portfolio.theme.color.AccentMint
import dev.tonholo.portfolio.theme.color.AccentSwatch
import dev.tonholo.portfolio.theme.color.Ink
import dev.tonholo.portfolio.theme.color.InkScale
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
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Ul
import org.jetbrains.compose.web.dom.Text as ComposeText

// Ordinal for the Currently strip: renders as `// 01`.
private const val SECTION_ORDINAL = 1

// Grid layout per handoff: `grid-template-columns: 1.4fr 1fr 1fr; gap: 48px`.
private const val NOW_GRID_WIDE_FR = 1.4
private const val NOW_GRID_NARROW_FR = 1
private val NOW_GRID_GAP = 48.dp

// Spacing between the mono meta label and the column content.
private val META_MARGIN_BOTTOM = 12.dp

// Working column: body copy width capped to 48ch.
private const val BODY_MAX_WIDTH_CH = 48.0

// Writing list: gap between article entries; small-date uses metaSmall.
private val WRITING_LIST_GAP = 14.dp
private val WRITING_TITLE_SIZE = 16.dp
private const val WRITING_TRANSITION_DURATION_S: Double = 0.15

// Inline gap inside the meta line between the status label, the separator
// dot, and the short date.
private val WRITING_META_GAP = 6.dp

// Off-the-clock list: gap between hobby rows; body copy.
private val HOBBY_LIST_GAP = 10.dp
private const val WRITING_ARTICLES_TAKE = 3

// Title spacing under the meta label in the working column.
private val WORKING_TITLE_MARGIN_BOTTOM = 12.dp

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
    val locale: Locale = LocalLocaleState.current.current
    val workingEntry: NowStripEntry? = StaticContent.nowStripFor(locale = locale).firstOrNull()
    val recentArticles: List<Article> = StaticContent.articlesFor(locale = locale).take(WRITING_ARTICLES_TAKE)
    val strings: Strings.NowStrip = LocalStrings.current.nowStrip
    NumberedSection(
        number = SECTION_ORDINAL,
        title = strings.sectionTitle,
        modifier = modifier,
    ) {
        Div(attrs = NowGridStyle.toModifier().toAttrs()) {
            WorkingOnColumn(
                title = workingEntry?.title ?: DEFAULT_WORKING_TITLE,
                body = workingEntry?.body ?: DEFAULT_WORKING_BODY,
                strings = strings,
            )
            WritingColumn(articles = recentArticles, strings = strings)
            OffTheClockColumn(strings = strings)
        }
    }
}

// Fallbacks guard against the NOW_STRIP list being empty; keep the copy aligned
// with the handoff so the rendered section stays meaningful.
private const val DEFAULT_WORKING_TITLE =
    "A Kotlin to GDScript transpiler for Godot."
private const val DEFAULT_WORKING_BODY =
    "Bringing Kotlin's type safety, expressive syntax, and compiler tooling " +
        "directly to Godot by transpiling into clean, idiomatic GDScript without " +
        "heavyweight native bindings."

/**
 * First column: the mono "working on" meta label, a display-font title, and
 * a serif body paragraph capped at 48ch. Mirrors the first `<div>` inside
 * `.now-grid` in the handoff JSX.
 */
@Composable
private fun WorkingOnColumn(title: String, body: String, strings: Strings.NowStrip) {
    val tokens = LocalPortfolioTokens.current

    Div(attrs = Modifier.toAttrs()) {
        MetaLabel(
            text = strings.labelWorkingOn,
            modifier = Modifier
                .display(DisplayStyle.Block)
                .margin(bottom = META_MARGIN_BOTTOM),
        )
        H3(
            attrs = Modifier
                .apply(Typography.h3Display)
                .color(tokens.ink.ink)
                .margin(
                    top = 0.cssRem,
                    bottom = WORKING_TITLE_MARGIN_BOTTOM,
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
private fun WritingColumn(articles: List<Article>, strings: Strings.NowStrip) {
    Div(attrs = Modifier.toAttrs()) {
        MetaLabel(
            text = strings.labelWriting,
            modifier = Modifier
                .display(DisplayStyle.Block)
                .margin(bottom = META_MARGIN_BOTTOM),
        )
        Ul(attrs = WritingListStyle.toModifier().toAttrs()) {
            articles.forEach { article ->
                WritingEntry(article = article, strings = strings)
            }
        }
    }
}

/**
 * One row inside the writing column: a small mono meta line followed by the
 * article title rendered in the display font. Navigates to the article detail route on click.
 */
@Composable
private fun WritingEntry(article: Article, strings: Strings.NowStrip) {
    val tokens: PortfolioTokens = LocalPortfolioTokens.current
    val locale: Locale = LocalLocaleState.current.current
    val analytics: AnalyticsManager = LocalAnalyticsManager.current
    val articlePath: String = Routes.article(slug = article.slug, locale = locale)
    val statusLabel: String = when (article.status) {
        ArticleStatus.Draft -> strings.statusDraft
        ArticleStatus.Published -> strings.statusPublished
    }
    val shortDate: String = article.date.substringBefore(delimiter = ',')
    Li(attrs = Modifier.toAttrs()) {
        Link(
            path = articlePath,
            modifier = WritingEntryLinkStyle
                .toModifier()
                .onClick {
                    analytics.track(
                        AnalyticEvent.ReadArticle(
                            articleTitle = article.title,
                            articleUrl = articlePath,
                        ),
                    )
                },
            variant = UndecoratedLinkVariant then UncoloredLinkVariant,
        ) {
            Div(
                attrs = Modifier
                    .apply(Typography.metaSmall)
                    .color(tokens.muted.muted)
                    .display(DisplayStyle.LegacyInlineFlex)
                    .alignItems(AlignItems.Center)
                    .gap(WRITING_META_GAP)
                    .toAttrs(),
            ) {
                Span(attrs = Modifier.toAttrs()) { ComposeText(statusLabel) }
                MetaSeparator()
                Span(attrs = Modifier.toAttrs()) { ComposeText(shortDate) }
            }
            Div(
                attrs = WritingEntryTitleStyle
                    .toModifier()
                    .toAttrs(),
            ) {
                ComposeText(article.title)
            }
        }
    }
}

/**
 * Third column: the mono "off the clock" meta label above a body-voice list
 * of hobbies. Matches the hardcoded JSX list from the handoff (lines 182-188).
 */
@Composable
private fun OffTheClockColumn(strings: Strings.NowStrip) {
    val tokens = LocalPortfolioTokens.current

    Div(attrs = Modifier.toAttrs()) {
        MetaLabel(
            text = strings.labelOffTheClock,
            modifier = Modifier
                .display(DisplayStyle.Block)
                .margin(bottom = META_MARGIN_BOTTOM),
        )
        Ul(
            attrs = HobbyListStyle
                .toModifier()
                .apply(Typography.bodySmall)
                .color(tokens.ink.ink2)
                .toAttrs(),
        ) {
            HobbyRow(label = HOBBY_COOKING_LABEL, body = HOBBY_COOKING_BODY)
            HobbyRow(label = HOBBY_SNOWBOARDING_LABEL, body = HOBBY_SNOWBOARDING_BODY)
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
 * serif font, italicized, and links out to the book.
 */
@Composable
private fun HobbyReadingRow() {
    val tokens: PortfolioTokens = LocalPortfolioTokens.current
    Li(attrs = Modifier.toAttrs()) {
        B(
            attrs = Modifier
                .color(tokens.ink.ink)
                .toAttrs(),
        ) {
            ComposeText(HOBBY_READING_LABEL)
        }
        ComposeText(" $HOBBY_SEPARATOR ")
        A(
            href = HOBBY_READING_URL,
            attrs = HobbyReadingLinkStyle
                .toModifier()
                .fontFamily(*FontFamilies.SERIF)
                .fontStyle(FontStyle.Italic)
                .toAttrs {
                    attr("target", "_blank")
                    attr("rel", "noopener noreferrer")
                },
        ) {
            ComposeText(HOBBY_READING_TITLE)
        }
    }
}

// Hobby copy ported verbatim from the handoff JSX; the `NowStripEntry` shape
// cannot express this structure, so the content is inline.
private const val HOBBY_SEPARATOR = "-"
private const val HOBBY_COOKING_LABEL = "cooking"
private const val HOBBY_COOKING_BODY = "ragu, most Sundays"
private const val HOBBY_SNOWBOARDING_LABEL = "snowboarding"
private const val HOBBY_SNOWBOARDING_BODY = "still learning, not stopping"
private const val HOBBY_READING_LABEL = "reading"
private const val HOBBY_READING_TITLE = "Refactoring: Improving the Design of Existing Code"
private const val HOBBY_READING_URL = "https://a.co/d/09Rs9Xy9"

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
            .gap(NOW_GRID_GAP)
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
            .gap(WRITING_LIST_GAP)
    }
}

/**
 * Base `CssStyle` for writing entry links. Renders as a block anchor,
 * overrides text decoration, and transitions color on hover to the accent mint color.
 */
val WritingEntryLinkStyle = CssStyle {
    base {
        val ink: InkScale = if (colorMode == ColorMode.DARK) Ink.dark else Ink.light
        Modifier
            .display(DisplayStyle.Block)
            .textDecorationLine(TextDecorationLine.None)
            .cursor(Cursor.Pointer)
            .color(ink.ink)
            .transition(
                Transition.of(
                    property = "color",
                    duration = WRITING_TRANSITION_DURATION_S.s,
                ),
            )
    }
    hover {
        val accent: AccentSwatch = if (colorMode == ColorMode.DARK) AccentMint.dark else AccentMint.light
        Modifier.color(accent.accent)
    }
}

/**
 * Base `CssStyle` for the writing entry article title.
 */
val WritingEntryTitleStyle = CssStyle {
    base {
        Modifier
            .fontFamily(*FontFamilies.DISPLAY)
            .fontSize(WRITING_TITLE_SIZE)
            .fontWeight(FontWeight.Medium)
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
            .gap(HOBBY_LIST_GAP)
    }
}

/**
 * Base `CssStyle` for the reading book link in the off-the-clock column.
 * Inherits surrounding typography, overrides text decoration, and transitions
 * to the accent color on hover.
 */
val HobbyReadingLinkStyle = CssStyle {
    base {
        val ink: InkScale = if (colorMode == ColorMode.DARK) Ink.dark else Ink.light
        Modifier
            .color(ink.ink2)
            .textDecorationLine(TextDecorationLine.None)
            .cursor(Cursor.Pointer)
            .transition(
                Transition.of(
                    property = "color",
                    duration = WRITING_TRANSITION_DURATION_S.s,
                ),
            )
    }
    hover {
        val accent: AccentSwatch = if (colorMode == ColorMode.DARK) AccentMint.dark else AccentMint.light
        Modifier
            .color(accent.accent)
            .textDecorationLine(TextDecorationLine.Underline)
    }
}
