package dev.tonholo.portfolio.components.molecules

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.components.atoms.PhosphorIcon
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.theme.color.AccentMint
import dev.tonholo.portfolio.theme.color.Ink
import dev.tonholo.portfolio.theme.color.Paper
import dev.tonholo.portfolio.theme.layout.unit.dp
import dev.tonholo.portfolio.theme.typography.FontFamilies
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

private val PROMPT_BORDER_WIDTH = 1.dp
private val PROMPT_BORDER_RADIUS = 6.dp
private const val PROMPT_PADDING_REM = 1.25
private const val PROMPT_GAP_REM = 0.9
private const val ICON_SIZE_REM = 1.4
private const val TITLE_FONT_SIZE_REM = 1.05
private const val BODY_FONT_SIZE_REM = 0.95
private const val BODY_LINE_HEIGHT = 1.5

/**
 * The invitation above the comments widget, asking readers to leave a reaction or a comment.
 *
 * @param modifier optional [Modifier] applied to the prompt card.
 */
@Composable
fun ArticleCommentsPrompt(modifier: Modifier = Modifier) {
    val strings = LocalStrings.current.articleBody
    Div(attrs = ArticleCommentsPromptStyle.toModifier().then(modifier).toAttrs()) {
        PhosphorIcon(
            name = "chats-circle",
            size = ICON_SIZE_REM.cssRem,
            modifier = CommentsPromptIconStyle.toModifier(),
        )
        Div {
            P(attrs = CommentsPromptTitleStyle.toModifier().toAttrs()) { Text(strings.commentsTitle) }
            P(attrs = CommentsPromptBodyStyle.toModifier().toAttrs()) { Text(strings.commentsBody) }
        }
    }
}

/** The bordered card holding the comments invitation. */
val ArticleCommentsPromptStyle = CssStyle {
    base {
        val border = if (colorMode == ColorMode.DARK) Paper.dark.paper3 else Paper.light.paper3
        Modifier
            .display(DisplayStyle.Flex)
            .alignItems(AlignItems.FlexStart)
            .gap(PROMPT_GAP_REM.cssRem)
            .padding(PROMPT_PADDING_REM.cssRem)
            .border(width = PROMPT_BORDER_WIDTH, style = LineStyle.Solid, color = border)
            .borderRadius(PROMPT_BORDER_RADIUS)
    }
}

/** The chat icon, in the accent colour. */
val CommentsPromptIconStyle = CssStyle {
    base {
        val accent = if (colorMode == ColorMode.DARK) AccentMint.dark else AccentMint.light
        Modifier.color(accent.accent)
    }
}

/** The prompt's title line. */
val CommentsPromptTitleStyle = CssStyle {
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

/** The prompt's explanation under the title. */
val CommentsPromptBodyStyle = CssStyle {
    base {
        val ink = if (colorMode == ColorMode.DARK) Ink.dark else Ink.light
        Modifier
            .margin(top = 0.3.cssRem, bottom = 0.cssRem)
            .fontFamily(*FontFamilies.BODY)
            .fontSize(BODY_FONT_SIZE_REM.cssRem)
            .lineHeight(BODY_LINE_HEIGHT)
            .color(ink.ink2)
    }
}
