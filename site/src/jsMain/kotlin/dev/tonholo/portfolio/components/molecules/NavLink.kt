package dev.tonholo.portfolio.components.molecules

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.navigation.UncoloredLinkVariant
import com.varabyte.kobweb.silk.components.navigation.UndecoratedLinkVariant
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.selectors.hover
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.color.Ink
import dev.tonholo.portfolio.theme.color.Paper
import dev.tonholo.portfolio.theme.typography.FontFamilies
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.CSSMediaQuery
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.StylePropertyValue
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

// Touch target floor on pointer-coarse devices (phones, tablets). 44 / 16 = 2.75rem.
// WCAG 2.5.5 asks for 44x44; desktop/mouse users keep the compact editorial
// chrome because they can click pixel-precise.
private const val NAV_LINK_TOUCH_MIN_HEIGHT_REM = 2.75 // 44px

private const val ACTIVE_PREFIX = "> "

private val TransparentColor: Color = Color.rgba(r = 0, g = 0, b = 0, a = 0f)

/**
 * Lower-case mono navigation link that routes to a path. Maps to `.nav-link`
 * in the handoff CSS and is used in the top nav bar (`chrome.jsx`).
 *
 * Renders as a real `<a href>` via Kobweb's [Link] so middle-click /
 * cmd-click / "copy link address" behave like any browser anchor. The
 * inactive state renders as `ink-3` color over a transparent background;
 * the active state uses `ink` color over a `paper-2` background and prefixes
 * the label with `"> "` (matching the `.nav-link.active::before` pseudo in
 * the handoff, rendered here as an accent-colored [Span] sibling so the
 * color follows the active theme).
 *
 * @param label lower-case caption shown inside the anchor.
 * @param route destination path used as the anchor href.
 * @param active whether this link represents the current route or filter.
 * @param modifier optional [Modifier] chained after the base styles.
 */
@Composable
fun NavLink(
    label: String,
    route: String,
    active: Boolean,
    modifier: Modifier = Modifier,
) {
    val tokens = LocalPortfolioTokens.current
    val finalModifier = navLinkBaseModifier(active = active, modifier = modifier)

    Link(
        path = route,
        modifier = finalModifier,
        variant = UndecoratedLinkVariant then UncoloredLinkVariant,
    ) {
        NavLinkContent(label = label, active = active, accentColor = tokens.accent.accent)
    }
}

/**
 * Button variant of [NavLink] for client-side state toggles that should not
 * change the URL. Used by the Projects filter bar where selecting a filter
 * drives a local [remember]ed value rather than navigating.
 *
 * Shares [NavLinkStyle] and the same active / inactive palette as [NavLink]
 * so the two feel identical in the UI; the only difference is the emitted
 * element (`<button type="button">`) and that `onClick` drives local state
 * instead of a route change.
 *
 * @param label lower-case caption shown inside the button.
 * @param active whether this link represents the currently selected option.
 * @param onClick fired when the underlying `<button>` is activated.
 * @param modifier optional [Modifier] chained after the base styles.
 */
@Composable
fun NavLinkButton(
    label: String,
    active: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tokens = LocalPortfolioTokens.current
    val finalModifier = navLinkBaseModifier(active = active, modifier = modifier)
        .onClick { onClick() }

    WebButton(attrs = finalModifier.toAttrs { attr("type", "button") }) {
        NavLinkContent(label = label, active = active, accentColor = tokens.accent.accent)
    }
}

@Composable
private fun navLinkBaseModifier(active: Boolean, modifier: Modifier = Modifier): Modifier {
    val tokens = LocalPortfolioTokens.current
    val background = if (active) tokens.paper.paper2 else TransparentColor
    val foreground = if (active) tokens.ink.ink else tokens.ink.ink3
    return NavLinkStyle
        .toModifier()
        .backgroundColor(background)
        .color(foreground)
        .then(modifier)
}

@Composable
private fun NavLinkContent(label: String, active: Boolean, accentColor: Color) {
    if (active) {
        Span(
            attrs = Modifier
                .color(accentColor)
                .toAttrs { attr("aria-hidden", "true") },
        ) {
            ComposeText(ACTIVE_PREFIX)
        }
    }
    ComposeText(label)
}

/**
 * Base `CssStyle` for [NavLink] and [NavLinkButton]. Ports the `.nav-link`
 * rules from the handoff: mono typography at 13px, tight `6px 12px` padding,
 * a 4px radius, no border, and a smooth 0.18s color/background transition.
 * Foreground and background colors are applied inline so they stay
 * token-aware for the active/inactive split; the hover state resolves
 * static paper/ink palettes through [colorMode] so the hover background
 * lightens consistently on both themes.
 *
 * On `@media (pointer: coarse)` (phones and tablets where the primary input
 * is touch) the hit area grows to a 44px minimum height with the label
 * vertically centered. Desktop/mouse users keep the compact editorial
 * chrome because pixel-precise pointing does not need the WCAG 2.5.5 floor.
 */
val NavLinkStyle = CssStyle {
    base {
        Modifier
            .fontFamily(*FontFamilies.MONO)
            .fontSize(NAV_LINK_FONT_SIZE_REM.cssRem)
            .fontWeight(FontWeight.Normal)
            .padding(
                topBottom = NAV_LINK_PADDING_Y_REM.cssRem,
                leftRight = NAV_LINK_PADDING_X_REM.cssRem,
            )
            .borderRadius(NAV_LINK_BORDER_RADIUS_REM.cssRem)
            .border(style = LineStyle.None)
            .cursor(Cursor.Pointer)
            .textDecorationLine(TextDecorationLine.None)
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
    cssRule(
        mediaQuery = CSSMediaQuery.MediaFeature(
            name = "pointer",
            value = StylePropertyValue(value = "coarse"),
        ),
    ) {
        Modifier
            .minHeight(NAV_LINK_TOUCH_MIN_HEIGHT_REM.cssRem)
            .display(DisplayStyle.LegacyInlineFlex)
            .alignItems(AlignItems.Center)
    }
}
