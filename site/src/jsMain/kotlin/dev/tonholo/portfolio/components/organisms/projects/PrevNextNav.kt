package dev.tonholo.portfolio.components.organisms.projects

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.borderTop
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.justifyContent
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.components.atoms.PhosphorIcon
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.PortfolioTokens
import dev.tonholo.portfolio.theme.typography.FontFamilies
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text as ComposeText

// Handoff inline style on the `.prev-next` container (lines 183-186 of
// `pages2.jsx`): flex row with space-between alignment, 48px vertical padding,
// 48px top margin, and a 1px hairline top border.
private const val NAV_PADDING_Y_REM = 3.0 // 48px
private const val NAV_MARGIN_TOP_REM = 3.0 // 48px
private const val NAV_BORDER_WIDTH_REM = 0.0625 // 1px

// Handoff link metrics (`.prev-next a`): mono font family at 12px. Left link
// uses `--muted`, right link uses `--accent`. Icon + label sit on the same
// baseline with an 8px gap.
private const val LINK_FONT_SIZE_REM = 0.75 // 12px
private const val LINK_GAP_REM = 0.5 // 8px

private const val ICON_ARROW_LEFT = "arrow-left"
private const val ICON_ARROW_RIGHT = "arrow-right"

/**
 * Sticky-bottom navigation strip for the project detail page. Maps to the
 * inline `<div style="...">` at the bottom of `ProjectPage` in the handoff
 * JSX (lines 183-186 of `pages2.jsx`): a flex row separated by a 1px top
 * hairline, with a muted `back to work` link on the left and an accent
 * `next: {ProjectName}` link on the right. The right slot is hidden entirely
 * when [nextProjectName] is `null` (last project in the series).
 *
 * Arrows use [PhosphorIcon]: `arrow-left` on the back link and `arrow-right`
 * on the next link.
 *
 * @param nextProjectName the human-readable name of the next project. Pass
 *   `null` to hide the right slot for the final project in the series.
 * @param onBack invoked when the back link is clicked.
 * @param onNext invoked when the next link is clicked. Only attached when
 *   [nextProjectName] is non-null.
 * @param modifier optional [Modifier] chained after the base styles.
 */
@Composable
fun PrevNextNav(
    nextProjectName: String?,
    onBack: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tokens = LocalPortfolioTokens.current
    val borderColor = prevNextBorderColor(tokens = tokens)

    val finalModifier = PrevNextNavStyle
        .toModifier()
        .borderTop(
            width = NAV_BORDER_WIDTH_REM.cssRem,
            style = LineStyle.Solid,
            color = borderColor,
        )
        .then(modifier)

    Div(attrs = finalModifier.toAttrs()) {
        BackLink(onBack = onBack)
        if (nextProjectName != null) {
            NextLink(projectName = nextProjectName, onNext = onNext)
        }
    }
}

/**
 * Back link: muted mono row that reads `← back to work`. Uses an anchor
 * without an `href` because route changes flow through [onBack].
 */
@Composable
private fun BackLink(onBack: () -> Unit) {
    val tokens = LocalPortfolioTokens.current
    val strings = LocalStrings.current.prevNext

    A(
        href = null,
        attrs = PrevNextLinkStyle
            .toModifier()
            .color(tokens.muted.muted)
            .onClick { event ->
                event.preventDefault()
                onBack()
            }
            .toAttrs { attr("role", "link") },
    ) {
        PhosphorIcon(name = ICON_ARROW_LEFT)
        ComposeText(strings.back)
    }
}

/**
 * Next link: accent mono row that reads `next: {projectName} →`. Uses an
 * anchor without an `href` because route changes flow through [onNext].
 */
@Composable
private fun NextLink(projectName: String, onNext: () -> Unit) {
    val tokens = LocalPortfolioTokens.current
    val strings = LocalStrings.current.prevNext

    A(
        href = null,
        attrs = PrevNextLinkStyle
            .toModifier()
            .color(tokens.accent.accent)
            .onClick { event ->
                event.preventDefault()
                onNext()
            }
            .toAttrs { attr("role", "link") },
    ) {
        ComposeText(strings.nextPrefix + projectName)
        PhosphorIcon(name = ICON_ARROW_RIGHT)
    }
}

/**
 * Resolves the hairline border color shared by [PrevNextNav]. Light theme
 * uses `paper-3`; dark theme uses `paper-2`, matching the
 * `[data-theme="dark"]` overrides used elsewhere on project detail chrome.
 */
@Composable
private fun prevNextBorderColor(tokens: PortfolioTokens): Color {
    val colorMode = ColorMode.current
    return when (colorMode) {
        ColorMode.LIGHT -> tokens.paper.paper3
        ColorMode.DARK -> tokens.paper.paper2
    }
}

/**
 * Base `CssStyle` for the outer nav strip. Flex row with space-between
 * alignment, 48px vertical padding, 48px top margin. The 1px top border is
 * applied inline so it can flip per theme.
 */
val PrevNextNavStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Flex)
            .justifyContent(JustifyContent.SpaceBetween)
            .alignItems(AlignItems.Center)
            .padding(topBottom = NAV_PADDING_Y_REM.cssRem)
            .margin(top = NAV_MARGIN_TOP_REM.cssRem)
    }
}

/**
 * Shared `CssStyle` for both the back and next anchors. Renders as an
 * inline-flex row so the [PhosphorIcon] and label share a baseline with an
 * 8px gap. Mono 12px typography matches the handoff inline styles; color is
 * applied inline by each link so back gets `--muted` and next gets `--accent`.
 */
val PrevNextLinkStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.LegacyInlineFlex)
            .alignItems(AlignItems.Center)
            .gap(LINK_GAP_REM.cssRem)
            .fontFamily(*FontFamilies.MONO)
            .fontSize(LINK_FONT_SIZE_REM.cssRem)
            .cursor(Cursor.Pointer)
    }
}
