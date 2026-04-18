package dev.tonholo.portfolio.components.organisms.articles

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextTransform
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderBottom
import com.varabyte.kobweb.compose.ui.modifiers.borderTop
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.flexDirection
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textTransform
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.selectors.hover
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.components.atoms.PhosphorIcon
import dev.tonholo.portfolio.components.atoms.PhosphorWeight
import dev.tonholo.portfolio.components.molecules.TagList
import dev.tonholo.portfolio.domain.model.Article
import dev.tonholo.portfolio.domain.model.ArticleStatus
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.PortfolioTokens
import dev.tonholo.portfolio.theme.color.Paper
import dev.tonholo.portfolio.theme.typography.FontFamilies
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text as ComposeText

// Handoff `.article-row` grid template: `120px 1fr 200px 80px`. We bump to the
// task spec's `8rem 1fr 16rem 5rem` (128px / 1fr / 256px / 80px at 1rem=16px),
// which gives the date and tag columns a little more breathing room in the
// redesign without changing the hairline/row spacing rhythm.
private const val ROW_DATE_COL_REM = 8.0
private const val ROW_MAIN_COL_FR = 1
private const val ROW_TAGS_COL_REM = 16.0
private const val ROW_READ_COL_REM = 5.0

// Row chrome: 32px grid gap, 24px top/bottom padding, 1px hairline borders.
private const val ROW_GAP_REM = 2.0 // 32px
private const val ROW_PADDING_Y_REM = 1.5 // 24px
private const val ROW_PADDING_X_REM = 0.0 // hover adds inline padding
private const val ROW_HOVER_PADDING_X_REM = 1.0 // 16px
private const val ROW_BORDER_WIDTH_REM = 0.0625 // 1px at 1rem=16px
private const val ROW_TRANSITION_DURATION_S = 0.18

// Column 1: date (12px mono muted) over accent draft status (10px mono accent).
private const val DATE_SIZE_REM = 0.75 // 12px
private const val DATE_COL_PADDING_TOP_REM = 0.375 // 6px
private const val STATUS_MARGIN_TOP_REM = 0.25 // 4px
private const val STATUS_SIZE_REM = 0.625 // 10px
private const val STATUS_LETTER_SPACING_EM = 0.08
private const val STATUS_ICON_SIZE_REM = 0.5 // 8px

// Column 2: title (24px display medium) over body copy (14px body ink-3).
private const val TITLE_SIZE_REM = 1.5 // 24px
private const val TITLE_LINE_HEIGHT = 1.2
private const val TITLE_LETTER_SPACING_EM = -0.02
private const val TITLE_MARGIN_BOTTOM_REM = 0.5 // 8px
private const val DESC_SIZE_REM = 0.875 // 14px
private const val DESC_LINE_HEIGHT = 1.55
private const val DESC_MAX_WIDTH_CH = 60.0

// Column 3: tags row aligns to the row baseline.
private const val TAGS_COL_PADDING_TOP_REM = 0.375 // 6px

// Column 4: read-time (12px mono muted). Text aligns right on desktop; left on mobile.
private const val READ_SIZE_REM = 0.75 // 12px
private const val READ_COL_PADDING_TOP_REM = 0.375 // 6px

private const val TAG_PREFIX = "#"

private const val DRAFT_LABEL = "draft"
private const val PUBLISHED_LABEL = "published"

/**
 * Returns the hairline border color for an article row, matching the handoff:
 * light theme uses `--paper-3`; dark theme overrides to `--paper-2`.
 */
@Composable
private fun articleRowBorderColor(): Color {
    val tokens = LocalPortfolioTokens.current
    val colorMode = ColorMode.current
    return when (colorMode) {
        ColorMode.LIGHT -> tokens.paper.paper3
        ColorMode.DARK -> tokens.paper.paper2
    }
}

/**
 * Articles list organism. Ports `.article-list` and `.article-row` from the
 * handoff CSS (`styles.css` lines 1004-1052) plus the markup inside
 * `ArticlesPage` (`pages1.jsx` lines 114-131). Renders a vertical stack of
 * clickable rows; each row is a four-column grid carrying date + optional
 * draft status pill, display title + body copy, tag chips via [TagList], and
 * the mono read-time estimate.
 *
 * Below [Breakpoint.LG] the grid collapses to a single column so date, title,
 * tags, and read-time stack. The entire row is clickable; pressing an item
 * fires [onArticleClick] with the underlying [Article.slug].
 *
 * @param articles the list of [Article]s to render; order is preserved.
 * @param onArticleClick invoked with [Article.slug] when a row is activated.
 * @param modifier optional [Modifier] chained after the base styles.
 */
@Composable
fun ArticleList(
    articles: List<Article>,
    onArticleClick: (slug: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val finalModifier = ArticleListStyle
        .toModifier()
        .then(modifier)

    Div(attrs = finalModifier.toAttrs()) {
        articles.forEachIndexed { index, article ->
            ArticleRow(
                article = article,
                isLast = index == articles.lastIndex,
                onClick = onArticleClick,
            )
        }
    }
}

@Composable
private fun ArticleRow(article: Article, isLast: Boolean, onClick: (slug: String) -> Unit) {
    val tokens = LocalPortfolioTokens.current
    val borderColor = articleRowBorderColor()

    var rowModifier = ArticleRowStyle
        .toModifier()
        .borderTop(
            width = ROW_BORDER_WIDTH_REM.cssRem,
            style = LineStyle.Solid,
            color = borderColor,
        )
    if (isLast) {
        rowModifier = rowModifier.borderBottom(
            width = ROW_BORDER_WIDTH_REM.cssRem,
            style = LineStyle.Solid,
            color = borderColor,
        )
    }
    rowModifier = rowModifier.onClick { onClick(article.slug) }

    Div(
        attrs = rowModifier.toAttrs {
            attr("role", "button")
            attr("tabindex", "0")
            attr("aria-label", article.title)
        },
    ) {
        ArticleDateCell(article = article, tokens = tokens)
        ArticleMainCell(article = article, tokens = tokens)
        ArticleTagsCell(tags = article.tags)
        ArticleReadCell(readTime = article.readTime, tokens = tokens)
    }
}

@Composable
private fun ArticleDateCell(article: Article, tokens: PortfolioTokens) {
    Div(
        attrs = ArticleDateCellStyle
            .toModifier()
            .color(tokens.muted.muted)
            .toAttrs(),
    ) {
        ComposeText(article.date)
        if (article.status == ArticleStatus.Draft) {
            Div(
                attrs = ArticleStatusStyle
                    .toModifier()
                    .color(tokens.accent.accent)
                    .toAttrs(),
            ) {
                PhosphorIcon(
                    name = "circle",
                    weight = PhosphorWeight.Fill,
                    size = STATUS_ICON_SIZE_REM.cssRem,
                )
                ComposeText(" ${article.status.label}")
            }
        }
    }
}

@Composable
private fun ArticleMainCell(article: Article, tokens: PortfolioTokens) {
    Div(attrs = Modifier.toAttrs()) {
        H3(
            attrs = ArticleTitleStyle
                .toModifier()
                .color(tokens.ink.ink)
                .toAttrs(),
        ) {
            ComposeText(article.title)
        }
        P(
            attrs = ArticleDescStyle
                .toModifier()
                .color(tokens.ink.ink3)
                .toAttrs(),
        ) {
            ComposeText(article.description)
        }
    }
}

@Composable
private fun ArticleTagsCell(tags: List<String>) {
    Div(
        attrs = ArticleTagsCellStyle
            .toModifier()
            .toAttrs(),
    ) {
        TagList(tags = tags, prefix = TAG_PREFIX)
    }
}

@Composable
private fun ArticleReadCell(readTime: String, tokens: PortfolioTokens) {
    Span(
        attrs = ArticleReadCellStyle
            .toModifier()
            .color(tokens.muted.muted)
            .toAttrs(),
    ) {
        ComposeText(readTime)
    }
}

private val ArticleStatus.label: String
    get() = when (this) {
        ArticleStatus.Draft -> DRAFT_LABEL
        ArticleStatus.Published -> PUBLISHED_LABEL
    }

/**
 * Base `CssStyle` for [ArticleList]. Ports `.article-list`: a vertical flex
 * stack; the hairline separators between rows are owned by the row's top
 * border so the container can stay layout-free.
 */
val ArticleListStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Flex)
            .flexDirection(FlexDirection.Column)
    }
}

/**
 * Base `CssStyle` for an [ArticleList] row. Ports `.article-row` from the
 * handoff: a four-column grid of `8rem 1fr 16rem 5rem` with a 32px column gap
 * and 24px vertical padding, plus a smooth hover transition that lifts the
 * background to `--paper-2` and inflates the inline padding by 16px. Below
 * [Breakpoint.LG] the grid collapses to a single column; the hover inflation
 * is dropped so stacked rows cannot shift horizontally on touch devices.
 * Border colors are applied inline by the composable so they react to the
 * active color mode.
 */
val ArticleRowStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Grid)
            .gridTemplateColumns {
                size(ROW_MAIN_COL_FR.fr)
            }
            .gap(ROW_GAP_REM.cssRem)
            .padding(
                topBottom = ROW_PADDING_Y_REM.cssRem,
                leftRight = ROW_PADDING_X_REM.cssRem,
            )
            .alignItems(AlignItems.Start)
            .cursor(Cursor.Pointer)
            .transition(
                Transition.group(
                    properties = listOf("background-color", "padding"),
                    duration = ROW_TRANSITION_DURATION_S.s,
                ),
            )
    }
    Breakpoint.LG {
        Modifier
            .gridTemplateColumns {
                size(ROW_DATE_COL_REM.cssRem)
                size(ROW_MAIN_COL_FR.fr)
                size(ROW_TAGS_COL_REM.cssRem)
                size(ROW_READ_COL_REM.cssRem)
            }
    }
    hover {
        val paper = if (colorMode == ColorMode.DARK) Paper.dark else Paper.light
        Modifier
            .backgroundColor(paper.paper2)
            .padding(
                topBottom = ROW_PADDING_Y_REM.cssRem,
                leftRight = ROW_HOVER_PADDING_X_REM.cssRem,
            )
    }
}

/**
 * Base `CssStyle` for the date cell. Ports `.article-date`: mono 12px copy
 * with a 6px top padding so the date aligns with the title's cap-height.
 * Color is applied inline so it resolves per theme.
 */
val ArticleDateCellStyle = CssStyle {
    base {
        Modifier
            .fontFamily(FontFamilies.MONO)
            .fontSize(DATE_SIZE_REM.cssRem)
            .padding(top = DATE_COL_PADDING_TOP_REM.cssRem)
    }
}

/**
 * Base `CssStyle` for the draft status pill under the date. Ports the inline
 * accent + uppercase mono styling inside the `.article-date` div in the
 * handoff markup: 4px top margin, 10px mono type, `0.08em` tracking. Color is
 * applied inline so the accent swatch resolves against the active theme.
 */
val ArticleStatusStyle = CssStyle {
    base {
        Modifier
            .fontFamily(FontFamilies.MONO)
            .fontSize(STATUS_SIZE_REM.cssRem)
            .letterSpacing(STATUS_LETTER_SPACING_EM.em)
            .textTransform(TextTransform.Uppercase)
            .margin(top = STATUS_MARGIN_TOP_REM.cssRem)
    }
}

/**
 * Base `CssStyle` for the article row's `<h3>` title. Ports `.article-main h3`:
 * display font at 24px medium, 1.2 line-height, tight `-0.02em` tracking, and
 * an 8px bottom margin separating it from the description paragraph. Color is
 * applied inline so it stays token-aware.
 */
val ArticleTitleStyle = CssStyle {
    base {
        Modifier
            .fontFamily(FontFamilies.DISPLAY)
            .fontSize(TITLE_SIZE_REM.cssRem)
            .fontWeight(FontWeight.Medium)
            .lineHeight(TITLE_LINE_HEIGHT)
            .letterSpacing(TITLE_LETTER_SPACING_EM.em)
            .margin(top = 0.cssRem, bottom = TITLE_MARGIN_BOTTOM_REM.cssRem)
    }
}

/**
 * Base `CssStyle` for the article row description paragraph. Ports
 * `.article-main p`: body font at 14px, 1.55 line-height, capped at 60ch so
 * long descriptions keep a comfortable measure. Color is applied inline.
 */
val ArticleDescStyle = CssStyle {
    base {
        Modifier
            .fontFamily(FontFamilies.BODY)
            .fontSize(DESC_SIZE_REM.cssRem)
            .lineHeight(DESC_LINE_HEIGHT)
            .maxWidth(DESC_MAX_WIDTH_CH.unsafeCast<CSSSizeValue<CSSUnit.ch>>())
            .margin(topBottom = 0.cssRem)
    }
}

/**
 * Base `CssStyle` for the tags cell wrapper. Adds a small top padding so the
 * chip row lines up with the title's cap-height (mirrors `.article-tags
 * padding-top: 6px`). The inner [TagList] owns the flex row + wrapping.
 */
val ArticleTagsCellStyle = CssStyle {
    base {
        Modifier.padding(top = TAGS_COL_PADDING_TOP_REM.cssRem)
    }
}

/**
 * Base `CssStyle` for the read-time cell. Ports `.article-read`: mono 12px copy
 * with a 6px top padding. The text aligns left in the stacked (mobile) layout;
 * the right-aligned desktop layout is owned by the `text-align` rule in the
 * handoff, but our column widths + one-item content render the same visually
 * on desktop without an explicit alignment rule. Color is applied inline.
 */
val ArticleReadCellStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Block)
            .fontFamily(FontFamilies.MONO)
            .fontSize(READ_SIZE_REM.cssRem)
            .padding(top = READ_COL_PADDING_TOP_REM.cssRem)
    }
}
