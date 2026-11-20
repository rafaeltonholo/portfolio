package dev.tonholo.portfolio.components.molecules

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.borderTop
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.components.atoms.ArticleLink
import dev.tonholo.portfolio.domain.model.ArticleReviewer
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.locale.Strings.ArticleThanks
import dev.tonholo.portfolio.theme.color.Ink
import dev.tonholo.portfolio.theme.color.Paper
import dev.tonholo.portfolio.theme.layout.unit.dp
import dev.tonholo.portfolio.theme.typography.FontFamilies
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Section
import org.jetbrains.compose.web.dom.Text

private val THANKS_BORDER_WIDTH = 1.dp
private const val THANKS_PADDING_TOP_REM = 1.5
private const val TITLE_FONT_SIZE_REM = 1.05
private const val BODY_FONT_SIZE_REM = 1.0
private const val BODY_LINE_HEIGHT = 1.6

/**
 * The thanks section at the end of an article, naming the people who reviewed it. Each name links to the reviewer's
 * profile when the frontmatter gives one. Renders nothing when [reviewers] is empty.
 *
 * @param reviewers the people to thank, in the frontmatter order.
 * @param modifier optional [Modifier] applied to the section.
 */
@Composable
fun ArticleThanks(reviewers: List<ArticleReviewer>, modifier: Modifier = Modifier) {
    if (reviewers.isEmpty()) return
    val strings = LocalStrings.current.articleThanks
    Section(attrs = ArticleThanksStyle.toModifier().then(modifier).toAttrs()) {
        H2(attrs = ArticleThanksTitleStyle.toModifier().toAttrs()) { Text(strings.title) }
        P(attrs = ArticleThanksBodyStyle.toModifier().toAttrs()) {
            Text(strings.intro)
            reviewers.forEachIndexed { index, reviewer ->
                ReviewerName(reviewer = reviewer)
                Text(reviewerSeparator(index = index, count = reviewers.size, strings = strings))
            }
            Text(strings.outro)
        }
    }
}

@Composable
private fun ReviewerName(reviewer: ArticleReviewer) {
    if (reviewer.link.isBlank()) {
        Text(reviewer.name)
    } else {
        ArticleLink(href = reviewer.link) { Text(reviewer.name) }
    }
}

/**
 * Returns the text that goes after the reviewer at [index] in a list of [count] names, so the names read as a
 * natural sentence: "A", "A and B", or "A, B, and C".
 */
fun reviewerSeparator(index: Int, count: Int, strings: ArticleThanks): String = when {
    index >= count - 1 -> ""
    count == 2 -> strings.pairSeparator
    index == count - 2 -> strings.lastSeparator
    else -> strings.listSeparator
}

/** The thanks section, set apart from the article body by a thin rule. */
val ArticleThanksStyle = CssStyle {
    base {
        val border = if (colorMode == ColorMode.DARK) Paper.dark.paper3 else Paper.light.paper3
        Modifier
            .padding(top = THANKS_PADDING_TOP_REM.cssRem)
            .borderTop(width = THANKS_BORDER_WIDTH, style = LineStyle.Solid, color = border)
    }
}

/** The thanks section title. */
val ArticleThanksTitleStyle = CssStyle {
    base {
        val ink = if (colorMode == ColorMode.DARK) Ink.dark else Ink.light
        Modifier
            .margin(0.cssRem)
            .fontFamily(*FontFamilies.DISPLAY)
            .fontSize(TITLE_FONT_SIZE_REM.cssRem)
            .fontWeight(FontWeight.Medium)
            .color(ink.ink)
    }
}

/** The sentence naming the reviewers. */
val ArticleThanksBodyStyle = CssStyle {
    base {
        val ink = if (colorMode == ColorMode.DARK) Ink.dark else Ink.light
        Modifier
            .margin(top = 0.5.cssRem, bottom = 0.cssRem)
            .fontFamily(*FontFamilies.BODY)
            .fontSize(BODY_FONT_SIZE_REM.cssRem)
            .lineHeight(BODY_LINE_HEIGHT)
            .color(ink.ink2)
    }
}
