package dev.tonholo.portfolio.components.organisms.articles

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontStyle
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontStyle
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.components.molecules.ArticleMetaRow
import dev.tonholo.portfolio.components.organisms.home.TerminalLine
import dev.tonholo.portfolio.components.organisms.home.TerminalSnippet
import dev.tonholo.portfolio.components.organisms.home.TerminalToken
import dev.tonholo.portfolio.components.organisms.home.TerminalTokenKind
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.typography.FontFamilies
import dev.tonholo.portfolio.theme.typography.Typography
import dev.tonholo.portfolio.theme.typography.apply
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.dom.Code
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Em
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text as ComposeText

// Handoff `.article-detail h1` (styles.css 1064-1072): display font, clamped
// 40-68px, line-height 1.0, -0.03em tracking, max-width 22ch, 16px bottom
// margin. [Typography.h1] already covers family/weight/size/line-height.
private const val TITLE_MAX_WIDTH_CH = 22.0
private const val TITLE_MARGIN_BOTTOM_REM = 1.0 // 16px

// Handoff `.article-detail .lede` (styles.css 1073-1081): serif, italic,
// 22px, 1.45 line-height, 48ch cap, 40px bottom margin. [Typography.serifLead]
// covers the serif family + 20px/1.5; we override the size, line-height, and
// italic slant inline to stay faithful to the handoff.
private const val LEDE_SIZE_REM = 1.375 // 22px
private const val LEDE_LINE_HEIGHT = 1.45
private const val LEDE_MAX_WIDTH_CH = 48.0
private const val LEDE_MARGIN_BOTTOM_REM = 2.5 // 40px

// Handoff `.prose` (styles.css 969-985): serif body copy at 19px, 1.65 line-
// height, 66ch cap, ink-2 color. Paragraphs gain a 1.2em top margin when they
// follow another paragraph; prose h3 heads use the display font at 22px with
// a 2em top margin and a 0.6em bottom margin.
private const val PROSE_SIZE_REM = 1.1875 // 19px
private const val PROSE_LINE_HEIGHT = 1.65
private const val PROSE_MAX_WIDTH_CH = 66.0
private const val PROSE_PARAGRAPH_TOP_MARGIN_EM = 1.2
private const val PROSE_H3_SIZE_REM = 1.375 // 22px
private const val PROSE_H3_TOP_MARGIN_EM = 2.0
private const val PROSE_H3_BOTTOM_MARGIN_EM = 0.6
private const val PROSE_H3_LETTER_SPACING_EM = -0.015

// Inline `<code>` spans. Handoff pages1.jsx line 188 sets the inline style
// `padding: 2px 6px; border-radius: 2px; font-size: 13px`; the background is
// `paper-2` and the font-family is the mono stack.
private const val INLINE_CODE_SIZE_REM = 0.8125 // 13px
private const val INLINE_CODE_PADDING_X_REM = 0.375 // 6px
private const val INLINE_CODE_PADDING_Y_REM = 0.125 // 2px
private const val INLINE_CODE_BORDER_RADIUS_REM = 0.125 // 2px

// Handoff inline margin on the `<pre class="term">` snippet (pages1.jsx 179).
private const val TERMINAL_MARGIN_TOP_REM = 1.5 // 24px
private const val TERMINAL_MARGIN_BOTTOM_REM = 1.5 // 24px

// Hardcoded v1 article copy. Lifted verbatim from pages1.jsx lines 176-196;
// em-dashes in the handoff are intentionally replaced with regular dashes.
private const val INTRO_PARAGRAPH =
    "I spent a week last month trying to figure out why a list was re-composing " +
        "on every scroll tick. Not every frame - just most of them. Enough that you " +
        "could feel it. Enough that QA opened a ticket."

private const val SECTION_ID_PROBLEM = "the-problem"
private const val SECTION_TITLE_PROBLEM = "The problem"
private const val SECTION_PARAGRAPH_PROBLEM =
    "In Compose, everything is a function. That's the gift. The trap is that " +
        "functions re-run easily, and nothing tells you, by default, which functions " +
        "are running how often. The recomposition layer is deeply hidden, and most " +
        "of the advice online is either (a) cargo-culted or (b) last year's."

private const val SECTION_CODE_TRAIL_INTRO_PART_1 = "This looks fine. This "
private const val SECTION_CODE_TRAIL_INTRO_PART_2 = "is"
private const val SECTION_CODE_TRAIL_INTRO_PART_3 = " fine, until "
private const val SECTION_CODE_TRAIL_INTRO_CODE = "enrich"
private const val SECTION_CODE_TRAIL_INTRO_PART_4 =
    " starts doing more than it lets on. And then, suddenly, it isn't."

private const val SECTION_ID_STABLE = "what-stable-really-means"
private const val SECTION_TITLE_STABLE = "What @Stable really means"
private const val SECTION_STABLE_PART_1 =
    "It means: I promise, to the best of my ability, that if two instances of this " +
        "class are "
private const val SECTION_STABLE_CODE_EQUALS = ".equals()"
private const val SECTION_STABLE_PART_2 =
    ", then their observable properties will be "
private const val SECTION_STABLE_PART_3 =
    " too. It is a promise - not an assertion. The compiler cannot verify it. " +
        "You, the human, are responsible."

private const val SECTION_ID_PROFILING = "profiling-for-real"
private const val SECTION_TITLE_PROFILING = "Profiling, for real"
private const val SECTION_PARAGRAPH_PROFILING =
    "Android Studio has a Compose recomposition layer inspector. Most people don't " +
        "know it exists. Enable it, scroll the UI, watch which composables pulse " +
        "yellow, repeatedly. That's your list."

private const val SECTION_ID_WORKFLOW = "a-workflow-that-scales"
private const val SECTION_TITLE_WORKFLOW = "A workflow that scales"
private const val SECTION_WORKFLOW_PART_1 = "Profile first, annotate second. Adding "
private const val SECTION_WORKFLOW_CODE_STABLE = "@Stable"
private const val SECTION_WORKFLOW_PART_2 =
    " everywhere is cargo-culting. Adding it where the profiler showed you a problem " +
        "is engineering."

private const val SECTION_ID_CONCLUSION = "conclusion"
private const val SECTION_TITLE_CONCLUSION = "Conclusion"
private const val SECTION_PARAGRAPH_CONCLUSION =
    "There is no cult here. There are tools, there are measurements, and there is " +
        "the slow, boring work of making your code a little less chatty over time. " +
        "That's the whole game."

// Terminal snippet line content, mirroring pages1.jsx 180-186. The snippet
// is kept in a private constant so the rendering helper stays declarative.
private const val TERM_COMMENT = "// lazy-column.kt"
private const val TERM_KW_LAZY = "LazyColumn"
private const val TERM_BRACE_OPEN = " {"
private const val TERM_ITEMS_PREFIX = "  items(state.items, key = { it.id }) { item ->"
private const val TERM_KW_VAL = "    val"
private const val TERM_VAL_SUFFIX = " enriched = remember(item.id) { enrich(item) }"
private const val TERM_ITEM_ROW = "    ItemRow(enriched)"
private const val TERM_INNER_CLOSE = "  }"
private const val TERM_OUTER_CLOSE = "}"

/**
 * Main article body organism. Ports the right-hand column of `ArticleDetail`
 * from the handoff (pages1.jsx 147-201 and styles.css 969-1103): a meta row,
 * the article title and lede, then a serif prose column that drops the
 * sample `@Stable` article verbatim.
 *
 * The body content is hardcoded for v1; the follow-up markdown migration
 * will replace it with parsed content. The [TerminalSnippet] organism is
 * reused for the Kotlin snippet between paragraphs.
 *
 * @param title the article title rendered as the page `<h1>`.
 * @param dateLabel human-readable publish date passed straight to
 *   [ArticleMetaRow] (e.g. `"apr 10, 2026"`).
 * @param readTime estimated read duration passed to [ArticleMetaRow]
 *   (e.g. `"9 min"`).
 * @param lede editorial subtitle rendered below the title in serif italic
 *   (the `.lede` paragraph in the handoff CSS).
 * @param modifier optional [Modifier] chained after the base styles.
 * @param onBack invoked when the back-to-articles link in [ArticleMetaRow]
 *   is clicked; defaults to a no-op so the organism can be previewed.
 */
@Composable
fun ArticleBody(
    title: String,
    dateLabel: String,
    readTime: String,
    lede: String,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
) {
    val tokens = LocalPortfolioTokens.current

    Div(attrs = modifier.toAttrs()) {
        ArticleMetaRow(
            date = dateLabel,
            readTime = readTime,
            onBack = onBack,
        )
        H1(
            attrs = ArticleBodyTitleStyle
                .toModifier()
                .apply(Typography.h1)
                .color(tokens.ink.ink)
                .toAttrs(),
        ) {
            ComposeText(title)
        }
        P(
            attrs = ArticleBodyLedeStyle
                .toModifier()
                .color(tokens.ink.ink2)
                .toAttrs(),
        ) {
            ComposeText(lede)
        }
        Div(attrs = ArticleBodyProseStyle.toModifier().color(tokens.ink.ink2).toAttrs()) {
            ProseParagraph(text = INTRO_PARAGRAPH)
            ProseH3(id = SECTION_ID_PROBLEM, text = SECTION_TITLE_PROBLEM)
            ProseParagraph(text = SECTION_PARAGRAPH_PROBLEM)
            TerminalSnippet(
                lines = sampleTerminalLines(),
                modifier = Modifier.margin(
                    top = TERMINAL_MARGIN_TOP_REM.cssRem,
                    bottom = TERMINAL_MARGIN_BOTTOM_REM.cssRem,
                ),
            )
            ProseCodeIntro()
            ProseH3(id = SECTION_ID_STABLE, text = SECTION_TITLE_STABLE)
            ProseStableParagraph()
            ProseH3(id = SECTION_ID_PROFILING, text = SECTION_TITLE_PROFILING)
            ProseParagraph(text = SECTION_PARAGRAPH_PROFILING)
            ProseH3(id = SECTION_ID_WORKFLOW, text = SECTION_TITLE_WORKFLOW)
            ProseWorkflowParagraph()
            ProseH3(id = SECTION_ID_CONCLUSION, text = SECTION_TITLE_CONCLUSION)
            ProseParagraph(text = SECTION_PARAGRAPH_CONCLUSION)
        }
    }
}

/**
 * Plain serif paragraph inside the prose column. Uses the `<p>` tag so the
 * `.prose p + p` adjacent-sibling top margin from the handoff CSS (ported
 * inline by [ArticleBodyProseStyle]) kicks in between paragraphs.
 */
@Composable
private fun ProseParagraph(text: String) {
    P(attrs = ProseParagraphStyle.toModifier().toAttrs()) {
        ComposeText(text)
    }
}

/**
 * Prose subheading. Maps to `.prose h3` in the handoff: display font, 22px,
 * tight tracking, ink color. [id] anchors the heading so the sidebar TOC can
 * link to it via `#$id`.
 */
@Composable
private fun ProseH3(id: String, text: String) {
    val tokens = LocalPortfolioTokens.current

    H3(
        attrs = ArticleProseHeadingStyle
            .toModifier()
            .id(id)
            .color(tokens.ink.ink)
            .toAttrs(),
    ) {
        ComposeText(text)
    }
}

/**
 * The first post-snippet paragraph, which interleaves the italicised `is`
 * emphasis with an inline code chip around `enrich`.
 */
@Composable
private fun ProseCodeIntro() {
    P(attrs = ProseParagraphStyle.toModifier().toAttrs()) {
        ComposeText(SECTION_CODE_TRAIL_INTRO_PART_1)
        EmphasisSpan(text = SECTION_CODE_TRAIL_INTRO_PART_2)
        ComposeText(SECTION_CODE_TRAIL_INTRO_PART_3)
        InlineCode(text = SECTION_CODE_TRAIL_INTRO_CODE)
        ComposeText(SECTION_CODE_TRAIL_INTRO_PART_4)
    }
}

/**
 * The `@Stable` paragraph: two `.equals()` inline code chips surround the
 * body text pulled from the handoff.
 */
@Composable
private fun ProseStableParagraph() {
    P(attrs = ProseParagraphStyle.toModifier().toAttrs()) {
        ComposeText(SECTION_STABLE_PART_1)
        InlineCode(text = SECTION_STABLE_CODE_EQUALS)
        ComposeText(SECTION_STABLE_PART_2)
        InlineCode(text = SECTION_STABLE_CODE_EQUALS)
        ComposeText(SECTION_STABLE_PART_3)
    }
}

/**
 * The workflow paragraph, which wraps the `@Stable` annotation in an inline
 * code chip.
 */
@Composable
private fun ProseWorkflowParagraph() {
    P(attrs = ProseParagraphStyle.toModifier().toAttrs()) {
        ComposeText(SECTION_WORKFLOW_PART_1)
        InlineCode(text = SECTION_WORKFLOW_CODE_STABLE)
        ComposeText(SECTION_WORKFLOW_PART_2)
    }
}

/**
 * Inline `<code>` chip: mono font, paper2 background, 2px/6px padding,
 * 2px radius. Matches the handoff inline style on pages1.jsx 188.
 */
@Composable
private fun InlineCode(text: String) {
    val tokens = LocalPortfolioTokens.current

    Code(
        attrs = InlineCodeStyle
            .toModifier()
            .backgroundColor(tokens.paper.paper2)
            .color(tokens.ink.ink)
            .toAttrs(),
    ) {
        ComposeText(text)
    }
}

/**
 * Inline italic emphasis using the native `<em>` tag. The surrounding prose
 * paragraph already supplies the serif family, so the emphasis inherits it
 * and the browser default italic slant does the rest.
 */
@Composable
private fun EmphasisSpan(text: String) {
    Em {
        ComposeText(text)
    }
}

/**
 * Canonical terminal snippet content for the sample article. Lifted verbatim
 * from the handoff (pages1.jsx 180-186) with a single shared 1-indexed line
 * numbering.
 */
private fun sampleTerminalLines(): List<TerminalLine> = listOf(
    TerminalLine(
        number = 1,
        tokens = listOf(TerminalToken(text = TERM_COMMENT, kind = TerminalTokenKind.Dim)),
    ),
    TerminalLine(
        number = 2,
        tokens = listOf(
            TerminalToken(text = TERM_KW_LAZY, kind = TerminalTokenKind.Keyword),
            TerminalToken(text = TERM_BRACE_OPEN),
        ),
    ),
    TerminalLine(
        number = 3,
        tokens = listOf(TerminalToken(text = TERM_ITEMS_PREFIX)),
    ),
    TerminalLine(
        number = 4,
        tokens = listOf(
            TerminalToken(text = TERM_KW_VAL, kind = TerminalTokenKind.Keyword),
            TerminalToken(text = TERM_VAL_SUFFIX),
        ),
    ),
    TerminalLine(
        number = 5,
        tokens = listOf(TerminalToken(text = TERM_ITEM_ROW)),
    ),
    TerminalLine(
        number = 6,
        tokens = listOf(TerminalToken(text = TERM_INNER_CLOSE)),
    ),
    TerminalLine(
        number = 7,
        tokens = listOf(TerminalToken(text = TERM_OUTER_CLOSE)),
    ),
)

/**
 * Base `CssStyle` for the article title. Ports the static pieces of
 * `.article-detail h1`: 22ch max-width and a 16px bottom margin. Display
 * typography is applied inline by the composable so we stay aligned with
 * the rest of the app's type system.
 */
val ArticleBodyTitleStyle = CssStyle {
    base {
        Modifier
            .margin(top = 0.cssRem, bottom = TITLE_MARGIN_BOTTOM_REM.cssRem)
            .maxWidth(TITLE_MAX_WIDTH_CH.unsafeCast<CSSSizeValue<CSSUnit.ch>>())
    }
}

/**
 * Base `CssStyle` for the article lede paragraph. Ports `.article-detail
 * .lede`: serif italic at 22px with 1.45 line-height, a 48ch cap, and a 40px
 * bottom margin. Color is applied inline from the `ink-2` token.
 */
val ArticleBodyLedeStyle = CssStyle {
    base {
        Modifier
            .fontFamily(*FontFamilies.SERIF)
            .fontSize(LEDE_SIZE_REM.cssRem)
            .lineHeight(LEDE_LINE_HEIGHT)
            .fontStyle(FontStyle.Italic)
            .maxWidth(LEDE_MAX_WIDTH_CH.unsafeCast<CSSSizeValue<CSSUnit.ch>>())
            .margin(top = 0.cssRem, bottom = LEDE_MARGIN_BOTTOM_REM.cssRem)
    }
}

/**
 * Base `CssStyle` for the prose wrapper `<div>`. Ports the static pieces of
 * `.prose`: serif family at 19px with 1.65 line-height and a 66ch column
 * cap. Color is applied inline from the `ink-2` token so it stays reactive
 * to the active color mode.
 */
val ArticleBodyProseStyle = CssStyle {
    base {
        Modifier
            .fontFamily(*FontFamilies.SERIF)
            .fontSize(PROSE_SIZE_REM.cssRem)
            .lineHeight(PROSE_LINE_HEIGHT)
            .maxWidth(PROSE_MAX_WIDTH_CH.unsafeCast<CSSSizeValue<CSSUnit.ch>>())
    }
}

/**
 * Base `CssStyle` for every `<p>` inside the prose column. Reproduces the
 * handoff `.prose p + p { margin-top: 1.2em; }` by setting the paragraph's
 * own top margin. Since every paragraph uses this style, the first
 * paragraph still lands flush against its preceding heading or lede; the
 * cascade of `1.2em` then separates each subsequent paragraph.
 */
val ProseParagraphStyle = CssStyle {
    base {
        Modifier.margin(
            top = PROSE_PARAGRAPH_TOP_MARGIN_EM.em,
            bottom = 0.cssRem,
        )
    }
}

/**
 * Base `CssStyle` for `<h3>` headings inside the prose column. Ports
 * `.prose h3`: display font at 22px with -0.015em tracking, 2em top margin,
 * and 0.6em bottom margin. Color is applied inline from the `ink` token.
 */
val ArticleProseHeadingStyle = CssStyle {
    base {
        Modifier
            .fontFamily(*FontFamilies.DISPLAY)
            .fontSize(PROSE_H3_SIZE_REM.cssRem)
            .letterSpacing(PROSE_H3_LETTER_SPACING_EM.em)
            .margin(
                top = PROSE_H3_TOP_MARGIN_EM.em,
                bottom = PROSE_H3_BOTTOM_MARGIN_EM.em,
            )
    }
}

/**
 * Base `CssStyle` for inline `<code>` spans inside prose paragraphs. Ports
 * the handoff inline style on pages1.jsx line 188: mono font at 13px with
 * 2px/6px padding and a 2px radius. Background and foreground colors are
 * applied inline by [InlineCode] so they stay token-aware.
 */
val InlineCodeStyle = CssStyle {
    base {
        Modifier
            .fontFamily(*FontFamilies.MONO)
            .fontSize(INLINE_CODE_SIZE_REM.cssRem)
            .padding(
                topBottom = INLINE_CODE_PADDING_Y_REM.cssRem,
                leftRight = INLINE_CODE_PADDING_X_REM.cssRem,
            )
            .borderRadius(INLINE_CODE_BORDER_RADIUS_REM.cssRem)
    }
}
