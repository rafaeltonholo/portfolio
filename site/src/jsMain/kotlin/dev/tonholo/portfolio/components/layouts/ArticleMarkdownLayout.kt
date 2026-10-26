package dev.tonholo.portfolio.components.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.BorderCollapse
import com.varabyte.kobweb.compose.css.FontStyle
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderCollapse
import com.varabyte.kobweb.compose.ui.modifiers.borderLeft
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontStyle
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.PageContext
import com.varabyte.kobweb.core.data.add
import com.varabyte.kobweb.core.data.get
import com.varabyte.kobweb.core.init.InitRoute
import com.varabyte.kobweb.core.init.InitRouteContext
import com.varabyte.kobweb.core.layout.Layout
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.selectors.descendants
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobwebx.markdown.markdown
import dev.tonholo.portfolio.components.atoms.GiscusWrapper
import dev.tonholo.portfolio.components.molecules.ArticleMetaRow
import dev.tonholo.portfolio.components.organisms.articles.ArticleTocSidebar
import dev.tonholo.portfolio.components.organisms.articles.TocEntry
import dev.tonholo.portfolio.generated.articles.GeneratedArticles
import dev.tonholo.portfolio.navigation.Routes
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.color.AccentMint
import dev.tonholo.portfolio.theme.color.Ink
import dev.tonholo.portfolio.theme.color.Paper
import dev.tonholo.portfolio.theme.layout.unit.dp
import dev.tonholo.portfolio.theme.typography.FontFamilies
import dev.tonholo.portfolio.theme.typography.Typography
import dev.tonholo.portfolio.theme.typography.apply
import dev.tonholo.portfolio.theme.typography.applyInlineCodeStyle
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.ch
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Article
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.P
import org.w3c.dom.HTMLElement
import org.w3c.dom.Node
import org.w3c.dom.asList
import org.jetbrains.compose.web.dom.Text as ComposeText

// Handoff `.article-detail .body`: `grid-template-columns: 220px 1fr; gap: 64px`.
// Below 900px (handoff) the grid collapses to a single column with a 24px gap.
// We convert both widths to rem (220px = 13.75rem; 64px = 4rem; 24px = 1.5rem)
// and reuse the existing `Breakpoint.LG` threshold so the sidebar appears at
// the same breakpoint the rest of the site uses for two-column layouts.
private const val GRID_SIDEBAR_COL_REM = 13.75
private const val GRID_GAP_REM = 4.0
private const val GRID_GAP_MOBILE_REM = 1.5

// Handoff `.article-detail h1`: 22ch max-width, 16px bottom margin.
private const val TITLE_MAX_WIDTH_CH = 22.0
private const val TITLE_MARGIN_BOTTOM_REM = 1.0

// Handoff `.article-detail .lede`: serif italic, 22px / 1.45, 48ch cap, 40px
// bottom margin.
private const val LEDE_SIZE_REM = 1.375
private const val LEDE_LINE_HEIGHT = 1.45
private const val LEDE_MAX_WIDTH_CH = 48.0
private const val LEDE_MARGIN_BOTTOM_REM = 2.5

// Handoff `.prose h3` (applied to any nested heading under the article body):
// display mono at 22px, weight 500, -0.015em tracking, margin `2em 0 0.6em`.
private val BODY_HEADING_SIZE = 22.dp
private const val BODY_HEADING_LETTER_SPACING_EM = -0.015
private const val BODY_HEADING_MARGIN_TOP_EM = 2.0
private const val BODY_HEADING_MARGIN_BOTTOM_EM = 0.6

private const val PROSE_FONT_SIZE_REM = 1.125
private const val PROSE_LINE_HEIGHT = 1.65
private const val PROSE_MAX_WIDTH_CH = 68
private const val PROSE_BLOCK_GAP_EM = 1.2
private const val PROSE_LIST_PADDING_LEFT_REM = 1.5
private const val PROSE_LIST_ITEM_GAP_EM = 0.4
private val PROSE_QUOTE_BORDER_WIDTH = 4.dp
private const val PROSE_QUOTE_PADDING_Y_REM = 0.875
private const val PROSE_QUOTE_PADDING_X_REM = 1.125
private val PROSE_QUOTE_BORDER_RADIUS_END = 6.dp
private val PROSE_QUOTE_BORDER_RADIUS_START = 0.dp
private const val PROSE_QUOTE_BG_ALPHA_LIGHT = 0.03f
private const val PROSE_QUOTE_BG_ALPHA_DARK = 0.04f
private val PROSE_TABLE_BORDER_WIDTH = 1.dp
private val PROSE_TABLE_PADDING_Y = 8.px
private val PROSE_TABLE_PADDING_X = 12.px
private val PROSE_TH_FONT_SIZE = 13.px
private val PROSE_TD_FONT_SIZE = 14.px
private const val PROSE_TABLE_MARGIN_BOTTOM_EM = 1.5

// Selector the runtime TOC scraper uses to find every heading under the
// markdown body. `<article>` is the body wrapper so we scope the query to
// avoid grabbing the page `<h1>`, which is rendered separately by the layout.
private const val TOC_SELECTOR = "article h2, article h3"

/**
 * Route-local data bag populated by [initArticleMarkdownLayout] so the layout
 * composable can render the article chrome (title, lede, meta row) from values
 * lifted out of the markdown's frontmatter.
 *
 * Each markdown page gets a fresh instance per route resolution via
 * [InitRouteContext.data]; no global mutable state is involved.
 */
data class ArticleMarkdownLayoutData(
    val slug: String,
    val title: String,
    val date: String,
    val description: String,
    val readTime: String,
)

/**
 * `@InitRoute` hook Kobweb calls before rendering any page that uses
 * [ArticleMarkdownLayout]. Pulls frontmatter from the markdown context and
 * stashes a typed [ArticleMarkdownLayoutData] into the route's data map so the
 * layout composable can read it back.
 *
 * Markdown files are required to declare `title`, `date`, `description`, and
 * `readTime`; missing values fall back to empty strings rather than throwing
 * so a partial draft still renders.
 */
@InitRoute
fun initArticleMarkdownLayout(ctx: InitRouteContext) {
    val frontMatter = ctx.markdown?.frontMatter
    val path = ctx.markdown?.path.orEmpty()
    val slug = path.substringAfterLast('/').removeSuffix(".md")
    val readTime = GeneratedArticles.ALL.firstOrNull { it.slug == slug }?.readTime
        ?: frontMatter?.get("readTime")?.singleOrNull().orEmpty()
    ctx.data.add(
        ArticleMarkdownLayoutData(
            slug = slug,
            title = frontMatter?.get("title")?.singleOrNull().orEmpty(),
            date = frontMatter?.get("date")?.singleOrNull().orEmpty(),
            description = frontMatter?.get("description")?.singleOrNull().orEmpty(),
            readTime = readTime,
        ),
    )
}

/**
 * Two-column layout for markdown-authored article detail pages. Ports the
 * handoff `.article-detail` block: a sticky [ArticleTocSidebar] on the left
 * (narrow column, generated from the document's `<h2>` / `<h3>` headings at
 * render time) and the article column on the right (meta row, title, lede,
 * markdown body).
 *
 * The layout reads frontmatter out of the route data populated by
 * [initArticleMarkdownLayout] so individual markdown files never need to
 * pass chrome values through - they just author body content.
 *
 * @param context the active [PageContext]; used for back-navigation from the
 *   [ArticleMetaRow] link.
 * @param content the markdown body rendered inside the right column.
 */
@Composable
@Layout
@Suppress("ModifierMissing")
fun ArticleMarkdownLayout(context: PageContext, content: @Composable () -> Unit) {
    val tokens = LocalPortfolioTokens.current
    val data = context.data.get<ArticleMarkdownLayoutData>() ?: ArticleMarkdownLayoutData(
        slug = "",
        title = "",
        date = "",
        description = "",
        readTime = "",
    )

    var tocEntries by remember(data.slug) { mutableStateOf<List<TocEntry>>(emptyList()) }
    val activeId: String = rememberScrollActiveHeadingId(slug = data.slug, entries = tocEntries)

    PageLayout {
        HeroSection(id = "article-shell") {
            // Handoff structure: meta row, title, and lede span the full
            // shell width above the sidebar/body grid. The two-column
            // layout only applies to the article body + TOC, not to the
            // editorial header.
            ArticleMetaRow(
                date = data.date,
                readTime = data.readTime,
                backRoute = Routes.ARTICLES,
            )
            H1(
                attrs = ArticleMarkdownTitleStyle
                    .toModifier()
                    .apply(Typography.h1)
                    .color(tokens.ink.ink)
                    .toAttrs(),
            ) {
                ComposeText(data.title)
            }
            if (data.description.isNotEmpty()) {
                P(
                    attrs = ArticleMarkdownLedeStyle
                        .toModifier()
                        .color(tokens.ink.ink2)
                        .toAttrs(),
                ) {
                    ComposeText(data.description)
                }
            }
            Div(attrs = ArticleMarkdownGridStyle.toModifier().toAttrs()) {
                ArticleTocSidebar(
                    contents = tocEntries,
                    activeId = activeId.ifBlank { tocEntries.firstOrNull()?.id.orEmpty() },
                    onCopyLink = { copyCurrentLocation() },
                    articleTitle = data.title,
                )
                Div(attrs = Modifier.fillMaxWidth().toAttrs()) {
                    Article(attrs = ArticleMarkdownBodyStyle.toModifier().toAttrs()) {
                        content()

                        DisposableEffect(data.slug) {
                            tocEntries = collectTocEntries()
                            val observer = observeHeadingsIfEmpty(
                                entries = tocEntries,
                                onCollected = { tocEntries = it },
                            )
                            onDispose { observer?.disconnect() }
                        }
                    }
                    if (data.title.isNotBlank()) {
                        GiscusWrapper(
                            term = data.title,
                            modifier = Modifier.fillMaxWidth().margin(top = 4.cssRem),
                        )
                    }
                }
            }
        }
    }
}

/**
 * Remembers and updates the active table-of-contents entry based on viewport scroll position.
 *
 * @param slug current article slug used as a key for state reset.
 * @param entries list of collected table-of-contents entries.
 * @return anchor id of the heading element currently considered active.
 */
@Composable
fun rememberScrollActiveHeadingId(slug: String, entries: List<TocEntry>): String {
    var activeId by remember(slug) { mutableStateOf("") }
    DisposableEffect(entries) {
        if (entries.isEmpty()) {
            return@DisposableEffect onDispose {}
        }
        val initialId: String = resolveActiveHeadingId(entries = entries)
        if (initialId.isNotBlank()) {
            activeId = initialId
        }
        var rafHandle: Int? = null
        val onScrollOrResize: (org.w3c.dom.events.Event) -> Unit = {
            if (rafHandle == null) {
                rafHandle = window.requestAnimationFrame {
                    rafHandle = null
                    val currentActiveId: String = resolveActiveHeadingId(entries = entries)
                    if (currentActiveId.isNotBlank() && currentActiveId != activeId) {
                        activeId = currentActiveId
                    }
                }
            }
        }
        window.addEventListener("scroll", onScrollOrResize)
        window.addEventListener("resize", onScrollOrResize)
        onDispose {
            rafHandle?.let { window.cancelAnimationFrame(it) }
            window.removeEventListener("scroll", onScrollOrResize)
            window.removeEventListener("resize", onScrollOrResize)
        }
    }
    return activeId
}

private const val TOP_NAV_BAR_HEIGHT_PX = 80.0

/**
 * Resolves the id of the first heading element currently visible in the viewport,
 * falling back to the most recent heading scrolled past the top.
 *
 * @param entries list of collected table-of-contents entries.
 * @return matching anchor id, or empty string if no heading is resolvable.
 */
fun resolveActiveHeadingId(entries: List<TocEntry>): String {
    if (entries.isEmpty()) return ""
    val headingsWithRect = entries.mapNotNull { entry ->
        val element = document.getElementById(entry.id) ?: return@mapNotNull null
        val rect = element.getBoundingClientRect()
        entry.id to rect
    }
    if (headingsWithRect.isEmpty()) return ""
    val firstVisible = headingsWithRect.firstOrNull { (_, rect) ->
        rect.bottom > TOP_NAV_BAR_HEIGHT_PX && rect.top < window.innerHeight
    }
    if (firstVisible != null) {
        return firstVisible.first
    }
    return headingsWithRect.lastOrNull { (_, rect) ->
        rect.bottom <= TOP_NAV_BAR_HEIGHT_PX
    }?.first ?: headingsWithRect.first().first
}

/**
 * Walks the DOM under the article body and collects every `<h2>` / `<h3>`
 * with a non-blank id into a [TocEntry] list.
 */
private fun collectTocEntries(): List<TocEntry> =
    document.querySelectorAll(TOC_SELECTOR).asList().mapNotNull { node ->
        val element = node as? HTMLElement ?: return@mapNotNull null
        val id = element.id.takeIf { it.isNotBlank() } ?: return@mapNotNull null
        val label = element.textContent?.trim().orEmpty().takeIf { it.isNotEmpty() }
            ?: return@mapNotNull null
        TocEntry(id = id, label = label)
    }

/**
 * If [entries] is empty (headings not yet in the DOM), attaches a
 * [MutationObserver] to the `<article>` element. Once headings appear,
 * the observer fires [onCollected] with the new list and disconnects.
 */
private fun observeHeadingsIfEmpty(
    entries: List<TocEntry>,
    onCollected: (List<TocEntry>) -> Unit,
): MutationObserver? {
    if (entries.isNotEmpty()) return null
    val articleEl = document.querySelector("article") ?: return null
    return MutationObserver { _, obs ->
        val collected = collectTocEntries()
        if (collected.isNotEmpty()) {
            onCollected(collected)
            obs.disconnect()
        }
    }.also { obs ->
        val options = createMutationObserverInit(childList = true, subtree = true)
        obs.observe(target = articleEl, options = options)
    }
}

private external class MutationObserver(
    callback: (records: Array<dynamic>, observer: MutationObserver) -> Unit,
) {
    fun observe(target: Node, options: MutationObserverInit)
    fun disconnect()
}

private external interface MutationObserverInit {
    var childList: Boolean
    var subtree: Boolean
}

private fun createMutationObserverInit(
    childList: Boolean = false,
    subtree: Boolean = false,
): MutationObserverInit {
    val options = js("({})").unsafeCast<MutationObserverInit>()
    options.childList = childList
    options.subtree = subtree
    return options
}

/**
 * Copies the active page URL to the clipboard when the sidebar `copy link`
 * action is clicked. Silently no-ops on the server or when the clipboard API
 * is unavailable (older browsers, non-HTTPS origins).
 */
private fun copyCurrentLocation() {
    val clipboard = window.navigator.clipboard
    clipboard.writeText(window.location.href)
}

/**
 * Base `CssStyle` for the two-column grid inside the article layout. Below
 * [Breakpoint.LG] the grid collapses to a single column so mobile readers
 * see the body first and the sidebar stacks below it.
 */
val ArticleMarkdownGridStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Grid)
            .gridTemplateColumns { minmax(0.px, 1.fr) }
            .gap(GRID_GAP_MOBILE_REM.cssRem)
            .alignItems(AlignItems.Start)
    }
    Breakpoint.LG {
        Modifier
            .gridTemplateColumns {
                size(GRID_SIDEBAR_COL_REM.cssRem)
                minmax(0.px, 1.fr)
            }
            .gap(GRID_GAP_REM.cssRem)
    }
}

/**
 * Base `CssStyle` for the article `<h1>`. Ports the static parts of
 * `.article-detail h1`: 22ch max-width and a 16px bottom margin. Display
 * typography is applied inline by the layout so the type system stays the
 * single source of truth for font family/weight/size/line-height.
 */
val ArticleMarkdownTitleStyle = CssStyle {
    base {
        Modifier
            .margin(top = 0.cssRem, bottom = TITLE_MARGIN_BOTTOM_REM.cssRem)
            .maxWidth(TITLE_MAX_WIDTH_CH.ch)
    }
}

/**
 * Base `CssStyle` for the article lede. Uses body sans (Geist) at 22px with 1.45 line-height,
 * a 48ch cap, and a 40px bottom margin, matching the typography of the rest of the site.
 */
val ArticleMarkdownLedeStyle = CssStyle {
    base {
        Modifier
            .fontFamily(*FontFamilies.BODY)
            .fontSize(LEDE_SIZE_REM.cssRem)
            .lineHeight(LEDE_LINE_HEIGHT)
            .maxWidth(LEDE_MAX_WIDTH_CH.ch)
            .margin(top = 0.cssRem, bottom = LEDE_MARGIN_BOTTOM_REM.cssRem)
    }
}

/**
 * Base `CssStyle` for the markdown article body wrapper. Uses the site's body sans
 * (Geist) for prose paragraphs, display mono for subsection headings, styled code
 * badges for inline snippets, and clean blockquote/table treatments.
 */
val ArticleMarkdownBodyStyle = CssStyle {
    base {
        val isDark: Boolean = colorMode == ColorMode.DARK
        val ink = if (isDark) Ink.dark else Ink.light
        Modifier
            .fontFamily(*FontFamilies.BODY)
            .fontSize(PROSE_FONT_SIZE_REM.cssRem)
            .lineHeight(PROSE_LINE_HEIGHT)
            .color(ink.ink2)
            .maxWidth(PROSE_MAX_WIDTH_CH.ch)
    }
    descendants("p") {
        Modifier.margin(top = 0.em, bottom = PROSE_BLOCK_GAP_EM.em)
    }
    descendants("h2", "h3") {
        val isDark: Boolean = colorMode == ColorMode.DARK
        val ink = if (isDark) Ink.dark else Ink.light
        Modifier
            .fontFamily(*FontFamilies.DISPLAY)
            .fontSize(BODY_HEADING_SIZE)
            .fontWeight(FontWeight.Medium)
            .letterSpacing(BODY_HEADING_LETTER_SPACING_EM.em)
            .color(ink.ink)
            .margin(
                top = BODY_HEADING_MARGIN_TOP_EM.em,
                bottom = BODY_HEADING_MARGIN_BOTTOM_EM.em,
            )
    }
    descendants("ul", "ol") {
        Modifier
            .margin(top = 0.em, bottom = PROSE_BLOCK_GAP_EM.em)
            .padding(left = PROSE_LIST_PADDING_LEFT_REM.cssRem)
    }
    descendants("li") {
        Modifier.margin(bottom = PROSE_LIST_ITEM_GAP_EM.em)
    }
    descendants("blockquote") {
        val isDark: Boolean = colorMode == ColorMode.DARK
        val accent = if (isDark) AccentMint.dark else AccentMint.light
        val ink = if (isDark) Ink.dark else Ink.light
        val bgAlpha = if (isDark) PROSE_QUOTE_BG_ALPHA_DARK else PROSE_QUOTE_BG_ALPHA_LIGHT
        val bg = if (isDark) {
            Color.rgba(r = 255, g = 255, b = 255, a = bgAlpha)
        } else {
            Color.rgba(r = 0, g = 0, b = 0, a = bgAlpha)
        }
        Modifier
            .borderLeft(width = PROSE_QUOTE_BORDER_WIDTH, style = LineStyle.Solid, color = accent.accent)
            .backgroundColor(bg)
            .borderRadius(
                topLeft = PROSE_QUOTE_BORDER_RADIUS_START,
                bottomLeft = PROSE_QUOTE_BORDER_RADIUS_START,
                topRight = PROSE_QUOTE_BORDER_RADIUS_END,
                bottomRight = PROSE_QUOTE_BORDER_RADIUS_END,
            )
            .padding(
                topBottom = PROSE_QUOTE_PADDING_Y_REM.cssRem,
                leftRight = PROSE_QUOTE_PADDING_X_REM.cssRem,
            )
            .margin(top = PROSE_BLOCK_GAP_EM.em, bottom = PROSE_BLOCK_GAP_EM.em)
            .color(ink.ink)
            .fontStyle(FontStyle.Italic)
    }
    descendants("a") {
        val isDark: Boolean = colorMode == ColorMode.DARK
        val accent = if (isDark) AccentMint.dark else AccentMint.light
        Modifier
            .color(accent.accent)
            .textDecorationLine(TextDecorationLine.Underline)
    }
    descendants("table") {
        Modifier
            .borderCollapse(BorderCollapse.Collapse)
            .margin(top = 0.em, bottom = PROSE_TABLE_MARGIN_BOTTOM_EM.em)
            .maxWidth(100.percent)
    }
    descendants("th") {
        val isDark: Boolean = colorMode == ColorMode.DARK
        val border = if (isDark) Paper.dark.paper3 else Paper.light.paper3
        val ink = if (isDark) Ink.dark else Ink.light
        Modifier
            .border(width = PROSE_TABLE_BORDER_WIDTH, style = LineStyle.Solid, color = border)
            .padding(topBottom = PROSE_TABLE_PADDING_Y, leftRight = PROSE_TABLE_PADDING_X)
            .fontFamily(*FontFamilies.DISPLAY)
            .fontSize(PROSE_TH_FONT_SIZE)
            .fontWeight(FontWeight.SemiBold)
            .color(ink.ink)
    }
    descendants("td") {
        val isDark: Boolean = colorMode == ColorMode.DARK
        val border = if (isDark) Paper.dark.paper3 else Paper.light.paper3
        Modifier
            .border(width = PROSE_TABLE_BORDER_WIDTH, style = LineStyle.Solid, color = border)
            .padding(topBottom = PROSE_TABLE_PADDING_Y, leftRight = PROSE_TABLE_PADDING_X)
            .fontSize(PROSE_TD_FONT_SIZE)
    }
    cssRule(" :not(pre) > code") {
        Modifier.applyInlineCodeStyle(isDark = colorMode == ColorMode.DARK)
    }
}
