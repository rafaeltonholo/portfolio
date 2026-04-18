package dev.tonholo.portfolio.components.molecules

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.selectors.hover
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.color.Ink
import dev.tonholo.portfolio.theme.color.Paper
import dev.tonholo.portfolio.theme.typography.FontFamilies
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Button as WebButton
import org.jetbrains.compose.web.dom.Text as ComposeText

private const val NAV_LINK_FONT_SIZE_REM = 0.8125 // 13px
private const val NAV_LINK_PADDING_Y_REM = 0.375 // 6px
private const val NAV_LINK_PADDING_X_REM = 0.75 // 12px
private const val NAV_LINK_BORDER_RADIUS_REM = 0.25 // 4px
private const val NAV_LINK_TRANSITION_DURATION_S = 0.18

private const val ACTIVE_PREFIX = "> "

private val TransparentColor: Color = Color.rgba(r = 0, g = 0, b = 0, a = 0f)

/**
 * Lower-case mono navigation link. Maps to `.nav-link` in the handoff CSS and
 * is used both in the top nav bar (`chrome.jsx`) and the project-filter row
 * (`home.jsx` ProjectsPage); both share identical styling.
 *
 * The inactive state renders as `ink-3` color over a transparent background;
 * the active state uses `ink` color over a `paper-2` background and prefixes
 * the label with `"> "` (matching the `.nav-link.active::before` pseudo in the
 * handoff, rendered here as an accent-colored [Span] sibling so the color
 * follows the active theme).
 *
 * @param label lower-case caption shown inside the button.
 * @param active whether this link represents the current route or filter.
 * @param onClick handler fired when the underlying `<button>` is clicked.
 * @param modifier optional [Modifier] chained after the base styles.
 */
@Composable
fun NavLink(
    label: String,
    active: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tokens = LocalPortfolioTokens.current

    val background = if (active) tokens.paper.paper2 else TransparentColor
    val foreground = if (active) tokens.ink.ink else tokens.ink.ink3

    val finalModifier = NavLinkStyle
        .toModifier()
        .backgroundColor(background)
        .color(foreground)
        .onClick { onClick() }
        .then(modifier)

    WebButton(attrs = finalModifier.toAttrs { attr("type", "button") }) {
        if (active) {
            Span(
                attrs = Modifier
                    .color(tokens.accent.accent)
                    .toAttrs { attr("aria-hidden", "true") },
            ) {
                ComposeText(ACTIVE_PREFIX)
            }
        }
        ComposeText(label)
    }
}

/**
 * Base `CssStyle` for [NavLink]. Ports the `.nav-link` rules from the handoff:
 * mono typography at 13px, tight `6px 12px` padding, a 4px radius, no border,
 * and a smooth 0.18s color/background transition. Foreground and background
 * colors are applied inline by the composable so they stay token-aware for
 * the active/inactive split; the hover state resolves static paper/ink
 * palettes through [colorMode] so the hover background lightens consistently
 * on both themes.
 */
val NavLinkStyle = CssStyle {
    base {
        Modifier
            .fontFamily(FontFamilies.MONO)
            .fontSize(NAV_LINK_FONT_SIZE_REM.cssRem)
            .fontWeight(FontWeight.Normal)
            .padding(
                topBottom = NAV_LINK_PADDING_Y_REM.cssRem,
                leftRight = NAV_LINK_PADDING_X_REM.cssRem,
            )
            .borderRadius(NAV_LINK_BORDER_RADIUS_REM.cssRem)
            .border(style = LineStyle.None)
            .cursor(Cursor.Pointer)
            .transition(
                Transition.group(
                    properties = listOf("color", "background-color"),
                    duration = NAV_LINK_TRANSITION_DURATION_S.s,
                ),
            )
    }
    hover {
        val isDark = colorMode == ColorMode.DARK
        val paper = if (isDark) Paper.dark else Paper.light
        val ink = if (isDark) Ink.dark else Ink.light
        Modifier
            .backgroundColor(paper.paper2)
            .color(ink.ink)
    }
}
