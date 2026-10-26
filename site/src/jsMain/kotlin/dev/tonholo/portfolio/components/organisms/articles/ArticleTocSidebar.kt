package dev.tonholo.portfolio.components.organisms.articles

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.ListStyleType
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.css.TextTransform
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.alignSelf
import com.varabyte.kobweb.compose.ui.modifiers.animation
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
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
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.scale
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.modifiers.textTransform
import com.varabyte.kobweb.compose.ui.modifiers.top
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.animation.Keyframes
import com.varabyte.kobweb.silk.style.animation.toAnimation
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.selectors.hover
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.analytics.UmamiAnalytics
import dev.tonholo.portfolio.components.atoms.PhosphorIcon
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.color.Ink
import dev.tonholo.portfolio.theme.layout.SiteDimensions
import dev.tonholo.portfolio.theme.layout.unit.dp
import dev.tonholo.portfolio.theme.typography.FontFamilies
import dev.tonholo.portfolio.theme.typography.unit.sp
import kotlinx.browser.window
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.AlignSelf
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Aside
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Ul
import org.jetbrains.compose.web.dom.Button as WebButton
import org.jetbrains.compose.web.dom.Text as ComposeText

private val SIDEBAR_FONT_SIZE = 11.sp
private val SIDEBAR_STICKY_TOP = 100.dp

private val HEADING_FONT_SIZE = 11.sp
private const val HEADING_LETTER_SPACING_EM = 0.08

private const val ENTRY_NUMBER_PAD_LENGTH = 2
private const val ENTRY_NUMBER_PAD_CHAR = '0'
private const val ENTRY_SEPARATOR = " - "

private const val COPY_FEEDBACK_DURATION_MS = 2000
private const val COPY_CHECKMARK_ANIMATION_MS = 240
private const val COPY_ICON_SIZE_REM = 0.85
private val COPY_BUTTON_GAP_PX = 6.px

private const val CHECKMARK_START_PERCENT = 0
private const val CHECKMARK_MID_PERCENT = 60
private const val CHECKMARK_END_PERCENT = 100
private const val CHECKMARK_START_SCALE = 0.5
private const val CHECKMARK_MID_SCALE = 1.2
private const val CHECKMARK_END_SCALE = 1.0

private external fun encodeURIComponent(uriComponent: String): String

/**
 * Keyframe animation giving the copy confirmation checkmark a tactile pop.
 */
val CopyCheckmarkPopKeyframes = Keyframes {
    CHECKMARK_START_PERCENT.percent {
        Modifier
            .opacity(0.0)
            .scale(CHECKMARK_START_SCALE)
    }
    CHECKMARK_MID_PERCENT.percent {
        Modifier
            .opacity(1.0)
            .scale(CHECKMARK_MID_SCALE)
    }
    CHECKMARK_END_PERCENT.percent {
        Modifier
            .opacity(1.0)
            .scale(CHECKMARK_END_SCALE)
    }
}

/**
 * One entry in the sidebar table of contents. [id] is the matching anchor id
 * on an in-body heading; [label] is the rendered title.
 *
 * @param id the HTML element id matching the heading.
 * @param label the display text for the heading.
 */
data class TocEntry(val id: String, val label: String)

/**
 * All share targets supported by the article detail sidebar.
 *
 * @param twitter external share URL for Twitter / X.
 * @param linkedIn external share URL for LinkedIn.
 * @param bluesky external share URL for Bluesky.
 */
data class ArticleShareUrls(val twitter: String, val linkedIn: String, val bluesky: String)

/**
 * Builds external share intent URLs for Twitter/X, LinkedIn, and Bluesky.
 *
 * @param title article title to prefill into the share message.
 * @param url canonical article URL.
 * @return [ArticleShareUrls] populated with destination intents.
 */
fun buildShareUrls(title: String, url: String): ArticleShareUrls {
    val encodedUrl: String = encodeURIComponent(url)
    val encodedTitle: String = encodeURIComponent(title)
    val blueskyText: String = if (title.isNotBlank()) "$title $url" else url
    val encodedBlueskyText: String = encodeURIComponent(blueskyText)
    return ArticleShareUrls(
        twitter = "https://twitter.com/intent/tweet?text=$encodedTitle&url=$encodedUrl",
        linkedIn = "https://www.linkedin.com/sharing/share-offsite/?url=$encodedUrl",
        bluesky = "https://bsky.app/intent/compose?text=$encodedBlueskyText",
    )
}

/**
 * Sticky table-of-contents + share sidebar shown to the left of the article
 * body on detail pages.
 *
 * @param contents table-of-contents entries, rendered in order with a
 *   two-digit counter prefix.
 * @param activeId the id of the entry to highlight.
 * @param onCopyLink invoked when the `copy link` share action is clicked.
 * @param modifier optional [Modifier] chained after the base sidebar styles.
 * @param articleTitle article title used to compose share intent messages.
 * @param articleUrl canonical URL of the article.
 */
@Composable
fun ArticleTocSidebar(
    contents: List<TocEntry>,
    activeId: String,
    onCopyLink: () -> Unit,
    modifier: Modifier = Modifier,
    articleTitle: String = "",
    articleUrl: String = "",
) {
    val tokens = LocalPortfolioTokens.current
    val strings = LocalStrings.current.articlesSidebar

    var isCopied by remember { mutableStateOf(false) }
    var copyTimerId by remember { mutableStateOf<Int?>(null) }

    val resolvedUrl: String = articleUrl.ifBlank {
        runCatching { window.location.href }.getOrDefault("")
    }
    val shareUrls: ArticleShareUrls = remember(resolvedUrl, articleTitle) {
        buildShareUrls(title = articleTitle, url = resolvedUrl)
    }

    val handleCopy: () -> Unit = {
        UmamiAnalytics.trackArticleCopyLink(articleTitle = articleTitle)
        onCopyLink()
        isCopied = true
        copyTimerId?.let { window.clearTimeout(it) }
        copyTimerId = window.setTimeout({ isCopied = false }, COPY_FEEDBACK_DURATION_MS)
    }

    DisposableEffect(Unit) {
        onDispose {
            copyTimerId?.let { window.clearTimeout(it) }
        }
    }

    val finalModifier = ArticleTocSidebarStyle
        .toModifier()
        .color(tokens.muted.muted)
        .then(modifier)

    Aside(attrs = finalModifier.toAttrs()) {
        SidebarHeading(
            text = strings.contents,
            modifier = Modifier.margin { top(0.dp) },
        )
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
        SidebarHeading(
            text = strings.share,
            modifier = Modifier.margin { top(SiteDimensions.size.Xxl) },
        )
        Ul(attrs = SidebarListStyle.toModifier().toAttrs()) {
            CopyLinkRow(
                label = strings.shareCopyLink,
                copiedLabel = strings.shareCopied,
                isCopied = isCopied,
                onClick = handleCopy,
                mutedColor = tokens.muted.muted,
                accentColor = tokens.accent.accent,
            )
            ShareLinkRow(
                label = strings.shareTwitter,
                href = shareUrls.twitter,
                mutedColor = tokens.muted.muted,
                onClick = {
                    UmamiAnalytics.trackArticleShare(platform = "twitter", articleTitle = articleTitle)
                },
            )
            ShareLinkRow(
                label = strings.shareLinkedIn,
                href = shareUrls.linkedIn,
                mutedColor = tokens.muted.muted,
                onClick = {
                    UmamiAnalytics.trackArticleShare(platform = "linkedin", articleTitle = articleTitle)
                },
            )
            ShareLinkRow(
                label = strings.shareBluesky,
                href = shareUrls.bluesky,
                mutedColor = tokens.muted.muted,
                onClick = {
                    UmamiAnalytics.trackArticleShare(platform = "bluesky", articleTitle = articleTitle)
                },
            )
        }
    }
}

/**
 * One contents row: renders the two-digit counter and label inside an anchor.
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
            attrs = TocAnchorStyle
                .toModifier()
                .color(resolvedColor)
                .toAttrs(),
        ) {
            ComposeText(label)
        }
    }
}

/**
 * Renders the copy link button with confirmation icon and animated feedback.
 */
@Composable
private fun CopyLinkRow(
    label: String,
    copiedLabel: String,
    isCopied: Boolean,
    onClick: () -> Unit,
    mutedColor: Color,
    accentColor: Color,
) {
    Li(attrs = Modifier.toAttrs()) {
        WebButton(
            attrs = ShareButtonStyle
                .toModifier()
                .color(if (isCopied) accentColor else mutedColor)
                .onClick { onClick() }
                .toAttrs {
                    attr("type", "button")
                    if (isCopied) {
                        attr("aria-live", "polite")
                    }
                },
        ) {
            ComposeText(if (isCopied) copiedLabel else label)
            if (isCopied) {
                Span(
                    attrs = Modifier
                        .display(DisplayStyle.LegacyInlineFlex)
                        .alignItems(AlignItems.Center)
                        .color(accentColor)
                        .animation(
                            CopyCheckmarkPopKeyframes.toAnimation(
                                duration = COPY_CHECKMARK_ANIMATION_MS.ms,
                                timingFunction = AnimationTimingFunction.EaseOut,
                            ),
                        )
                        .toAttrs(),
                ) {
                    PhosphorIcon(
                        name = "check",
                        size = COPY_ICON_SIZE_REM.cssRem,
                    )
                }
            }
        }
    }
}

/**
 * One external share link row navigating to Twitter, LinkedIn, or Bluesky.
 */
@Composable
private fun ShareLinkRow(
    label: String,
    href: String,
    mutedColor: Color,
    onClick: () -> Unit = {},
) {
    Li(attrs = Modifier.toAttrs()) {
        A(
            href = href,
            attrs = ShareLinkStyle
                .toModifier()
                .color(mutedColor)
                .toAttrs {
                    attr("target", "_blank")
                    attr("rel", "noopener noreferrer")
                    onClick { onClick() }
                },
        ) {
            ComposeText(label)
        }
    }
}

/**
 * Mono sidebar heading used by both the contents and share sections.
 */
@Composable
private fun SidebarHeading(text: String, modifier: Modifier = Modifier) {
    val tokens = LocalPortfolioTokens.current

    H2(
        attrs = modifier
            .fontFamily(*FontFamilies.MONO)
            .fontSize(HEADING_FONT_SIZE)
            .letterSpacing(HEADING_LETTER_SPACING_EM.em)
            .textTransform(TextTransform.Uppercase)
            .color(tokens.ink.ink3)
            .margin {
                bottom(SiteDimensions.size.Md)
            }
            .toAttrs(),
    ) {
        ComposeText(text)
    }
}

/**
 * Base `CssStyle` for the sticky sidebar wrapper.
 */
val ArticleTocSidebarStyle = CssStyle {
    base {
        Modifier
            .fontFamily(*FontFamilies.MONO)
            .fontSize(SIDEBAR_FONT_SIZE)
    }
    Breakpoint.LG {
        Modifier
            .position(Position.Sticky)
            .top(SIDEBAR_STICKY_TOP)
            .alignSelf(AlignSelf.Start)
    }
}

/**
 * Base `CssStyle` for table-of-contents anchor rows.
 */
val TocAnchorStyle = CssStyle {
    base {
        Modifier.textDecorationLine(TextDecorationLine.None)
    }
    hover {
        val ink = if (colorMode == ColorMode.DARK) Ink.dark else Ink.light
        Modifier.color(ink.ink)
    }
}

/**
 * Base `CssStyle` for the `copy link` interactive button.
 */
val ShareButtonStyle = CssStyle {
    base {
        Modifier
            .fontFamily(*FontFamilies.MONO)
            .fontSize(SIDEBAR_FONT_SIZE)
            .cursor(Cursor.Pointer)
            .textDecorationLine(TextDecorationLine.None)
            .textAlign(TextAlign.Start)
            .backgroundColor(Colors.Transparent)
            .border(style = LineStyle.None)
            .padding(0.px)
            .display(DisplayStyle.Flex)
            .alignItems(AlignItems.Center)
            .gap(COPY_BUTTON_GAP_PX)
    }
    hover {
        val ink = if (colorMode == ColorMode.DARK) Ink.dark else Ink.light
        Modifier.color(ink.ink)
    }
}

/**
 * Base `CssStyle` for external share anchor links.
 */
val ShareLinkStyle = CssStyle {
    base {
        Modifier
            .fontFamily(*FontFamilies.MONO)
            .fontSize(SIDEBAR_FONT_SIZE)
            .textDecorationLine(TextDecorationLine.None)
            .display(DisplayStyle.InlineBlock)
    }
    hover {
        val ink = if (colorMode == ColorMode.DARK) Ink.dark else Ink.light
        Modifier.color(ink.ink)
    }
}

/**
 * Base `CssStyle` for the two `<ul>` lists inside the sidebar.
 */
val SidebarListStyle = CssStyle {
    base {
        Modifier
            .listStyle(type = ListStyleType.None)
            .display(DisplayStyle.Flex)
            .flexDirection(FlexDirection.Column)
            .gap(SiteDimensions.size.Sm)
            .margin(topBottom = 0.cssRem)
            .padding(left = 0.cssRem)
    }
}
