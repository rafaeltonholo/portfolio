package dev.tonholo.portfolio.components.organisms.chrome

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.borderBottom
import com.varabyte.kobweb.compose.ui.modifiers.borderLeft
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.title
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.whiteSpace
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.extendedBy
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.components.atoms.Dot
import dev.tonholo.portfolio.components.atoms.DotVariant
import dev.tonholo.portfolio.components.atoms.IconButton
import dev.tonholo.portfolio.components.atoms.PhosphorIcon
import dev.tonholo.portfolio.components.molecules.BrandMark
import dev.tonholo.portfolio.components.molecules.LanguageSwitch
import dev.tonholo.portfolio.components.molecules.NavLink
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.locale.Strings
import dev.tonholo.portfolio.navigation.Routes
import dev.tonholo.portfolio.state.LocalLocaleState
import dev.tonholo.portfolio.state.LocalThemeState
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.color.Muted
import dev.tonholo.portfolio.theme.color.Paper
import dev.tonholo.portfolio.theme.layout.ShellStyle
import dev.tonholo.portfolio.theme.typography.FontFamilies
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Nav as WebNav
import org.jetbrains.compose.web.dom.Text as ComposeText

// `.nav-inner` layout geometry.
private const val NAV_INNER_PADDING_Y_REM = 1.0 // 16px
private const val NAV_INNER_GAP_REM = 2.0 // 32px

// `.nav` bottom hairline width.
private const val NAV_BORDER_WIDTH_REM = 0.0625 // 1px

// `.nav-links` cluster.
private const val NAV_LINKS_GAP_REM = 0.25 // 4px

// `.nav-meta` layout.
private const val NAV_META_FONT_SIZE_REM = 0.75 // 12px
private const val NAV_META_GAP_EXPANDED_REM = 1.0 // 16px
private const val NAV_META_PADDING_LEFT_EXPANDED_REM = 1.0 // 16px
private const val NAV_META_PADDING_LEFT_COLLAPSED_REM = 0.875 // 14px
private const val NAV_META_BORDER_WIDTH_REM = 0.0625 // 1px
private const val NAV_META_TEXT_MAX_WIDTH_REM = 15.0 // 240px

// Transition durations mirror `.nav-meta` and `.nav-meta__text` from the handoff.
private const val NAV_META_LAYOUT_TRANSITION_S = 0.28
private const val NAV_META_OPACITY_TRANSITION_S = 0.22

// Delay (in ms) before the status badge auto-collapses after mount.
private const val STATUS_AUTO_COLLAPSE_DELAY_MS = 4500L

// Phosphor glyph names for the theme toggle.
private const val THEME_ICON_SUN = "sun"
private const val THEME_ICON_MOON = "moon"

// Fully transparent color used as the collapsed-border stand-in.
private val TransparentColor: Color = Color.rgba(r = 0, g = 0, b = 0, a = 0f)

private data class NavLinkEntry(val label: String, val route: String)

/**
 * Builds the ordered list of nav link entries for the current locale. Route
 * constants stay stable across locales; only the rendered label changes.
 */
private fun navLinksFor(nav: Strings.Nav): List<NavLinkEntry> = listOf(
    NavLinkEntry(label = nav.home, route = Routes.HOME),
    NavLinkEntry(label = nav.projects, route = Routes.PROJECTS),
    NavLinkEntry(label = nav.about, route = Routes.ABOUT),
    NavLinkEntry(label = nav.articles, route = Routes.ARTICLES),
    NavLinkEntry(label = nav.resume, route = Routes.RESUME),
)

/**
 * Top site chrome. Maps to the `<nav className="nav">` block in the handoff
 * `chrome.jsx`: [BrandMark] on the left, the navigation links cluster, a
 * collapsible status badge, the [LanguageSwitch] pair, and the theme toggle
 * [IconButton].
 *
 * The status badge auto-collapses [STATUS_AUTO_COLLAPSE_DELAY_MS] after mount;
 * pointer hover re-expands it, and the pointer-leave collapses it again
 * (matching `.nav-meta.collapsed:hover` in the handoff CSS).
 *
 * Routing is delegated to the caller through [onNavigate]; the caller is
 * expected to forward the target route to `PageContext.router.tryRoutingTo`.
 *
 * @param currentRoute fully-qualified current path (e.g. `/`, `/about`) used
 *   to flag the active [NavLink].
 * @param onNavigate invoked with the [Routes] constant the user clicked.
 * @param modifier optional [Modifier] chained after the base [NavStyle].
 */
@Composable
fun Nav(currentRoute: String, onNavigate: (route: String) -> Unit, modifier: Modifier = Modifier) {
    val themeState = LocalThemeState.current
    val localeState = LocalLocaleState.current
    val strings = LocalStrings.current
    val isDark = themeState.current.isDark

    val finalModifier = NavStyle
        .toModifier()
        .then(modifier)

    WebNav(attrs = finalModifier.toAttrs()) {
        Div(attrs = NavInnerStyle.toModifier().toAttrs()) {
            BrandMark(onClick = { onNavigate(Routes.HOME) })
            NavLinksCluster(
                currentRoute = currentRoute,
                onNavigate = onNavigate,
                links = navLinksFor(nav = strings.nav),
            )
            StatusBadge(text = strings.statusBadge.text)
            LanguageSwitch(current = localeState.current, onSwitch = { localeState.set(it) })
            IconButton(
                modifier = Modifier.title(
                    if (isDark) strings.themeToggle.titleToLight else strings.themeToggle.titleToDark,
                ),
                ariaLabel = strings.themeToggle.ariaLabel,
                onClick = { themeState.toggle() },
            ) {
                PhosphorIcon(name = if (isDark) THEME_ICON_SUN else THEME_ICON_MOON)
            }
        }
    }
}

@Composable
private fun NavLinksCluster(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    links: List<NavLinkEntry>,
) {
    Div(attrs = NavLinksStyle.toModifier().toAttrs()) {
        links.forEach { entry ->
            NavLink(
                label = entry.label,
                active = currentRoute == entry.route,
                onClick = { onNavigate(entry.route) },
            )
        }
    }
}

@Composable
private fun StatusBadge(text: String) {
    val tokens = LocalPortfolioTokens.current
    var collapsed by remember { mutableStateOf(value = false) }

    LaunchedEffect(Unit) {
        delay(timeMillis = STATUS_AUTO_COLLAPSE_DELAY_MS)
        collapsed = true
    }

    val gap = if (collapsed) 0.cssRem else NAV_META_GAP_EXPANDED_REM.cssRem
    val paddingLeft = if (collapsed) {
        NAV_META_PADDING_LEFT_COLLAPSED_REM.cssRem
    } else {
        NAV_META_PADDING_LEFT_EXPANDED_REM.cssRem
    }
    val borderColor = if (collapsed) TransparentColor else tokens.paper.paper3
    val textMaxWidth = if (collapsed) 0.cssRem else NAV_META_TEXT_MAX_WIDTH_REM.cssRem
    val textOpacity = if (collapsed) 0.0 else 1.0

    val badgeModifier = NavMetaStyle
        .toModifier()
        .gap(gap)
        .padding(left = paddingLeft)
        .borderLeft(
            width = NAV_META_BORDER_WIDTH_REM.cssRem,
            style = LineStyle.Solid,
            color = borderColor,
        )
        .onMouseEnter { collapsed = false }
        .onMouseLeave { collapsed = true }
        .title(text)

    Div(attrs = badgeModifier.toAttrs()) {
        Dot(variant = DotVariant.Static, color = tokens.muted.muted2)
        Span(
            attrs = NavMetaTextStyle
                .toModifier()
                .maxWidth(textMaxWidth)
                .opacity(value = textOpacity)
                .toAttrs(),
        ) {
            ComposeText(text)
        }
    }
}

/**
 * Base `CssStyle` for [Nav]. Ports `.nav` from the handoff minus the sticky
 * positioning (the redesign uses a static top bar): a 1px bottom hairline that
 * borrows the `paper-3` swatch on light mode and `paper-2` on dark mode.
 */
val NavStyle = CssStyle {
    base {
        val isDark = colorMode == ColorMode.DARK
        val paper = if (isDark) Paper.dark else Paper.light
        val borderColor = if (isDark) paper.paper2 else paper.paper3
        Modifier
            .borderBottom(
                width = NAV_BORDER_WIDTH_REM.cssRem,
                style = LineStyle.Solid,
                color = borderColor,
            )
    }
}

/**
 * Base `CssStyle` for the inner flex row. Ports `.nav-inner`: caps content at
 * the [ShellStyle] max-width, centers horizontally with auto margins, and
 * applies the symmetric vertical padding plus the 32px gap between children.
 * Horizontal gutters come from [ShellStyle].
 */
val NavInnerStyle = ShellStyle.extendedBy {
    base {
        Modifier
            .display(DisplayStyle.Flex)
            .alignItems(AlignItems.Center)
            .gap(NAV_INNER_GAP_REM.cssRem)
            .padding(
                topBottom = NAV_INNER_PADDING_Y_REM.cssRem,
                leftRight = 0.cssRem,
            )
    }
}

/**
 * Base `CssStyle` for the `.nav-links` cluster: a flex row with a 4px gap,
 * pushed to the right edge via `margin-left: auto`.
 */
val NavLinksStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Flex)
            .gap(NAV_LINKS_GAP_REM.cssRem)
            .margin(left = auto.unsafeCast<CSSLengthOrPercentageNumericValue>())
    }
}

/**
 * Base `CssStyle` for the `.nav-meta` status pill. Mirrors the handoff: flex
 * row, mono 12px text at the `muted` color, animated gap/padding/border-color
 * so the collapse/expand transition stays smooth. Gap, padding-left, and
 * border color are applied inline by [StatusBadge] so the animation can react
 * to state.
 */
val NavMetaStyle = CssStyle {
    base {
        val isDark = colorMode == ColorMode.DARK
        val textColor = if (isDark) Muted.dark.muted else Muted.light.muted
        Modifier
            .display(DisplayStyle.Flex)
            .alignItems(AlignItems.Center)
            .fontFamily(*FontFamilies.MONO)
            .fontSize(NAV_META_FONT_SIZE_REM.cssRem)
            .color(textColor)
            .cursor(Cursor.Default)
            .transition(
                Transition.group(
                    properties = listOf("gap", "padding-left", "border-color"),
                    duration = NAV_META_LAYOUT_TRANSITION_S.s,
                ),
            )
    }
}

/**
 * Base `CssStyle` for `.nav-meta__text`. Keeps the text on one line and clips
 * the overflow so the animated `max-width` collapses cleanly.
 */
val NavMetaTextStyle = CssStyle {
    base {
        Modifier
            .overflow(Overflow.Hidden)
            .whiteSpace(WhiteSpace.NoWrap)
            .transition(
                Transition.of(
                    property = "max-width",
                    duration = NAV_META_LAYOUT_TRANSITION_S.s,
                ),
                Transition.of(
                    property = "opacity",
                    duration = NAV_META_OPACITY_TRANSITION_S.s,
                ),
            )
    }
}
