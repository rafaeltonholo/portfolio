package dev.tonholo.portfolio.components.organisms.chrome

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.ListStyleType
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.css.TextTransform
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.borderTop
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.flexDirection
import com.varabyte.kobweb.compose.ui.modifiers.flexWrap
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.justifyContent
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.listStyle
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.modifiers.textTransform
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.navigation.UncoloredLinkVariant
import com.varabyte.kobweb.silk.components.navigation.UndecoratedLinkVariant
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.selectors.hover
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.components.atoms.MetaSeparator
import dev.tonholo.portfolio.components.molecules.SocialLink
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.locale.Strings
import dev.tonholo.portfolio.navigation.Routes
import dev.tonholo.portfolio.state.LocalLocaleState
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.color.AccentMint
import dev.tonholo.portfolio.theme.color.Ink
import dev.tonholo.portfolio.theme.layout.ChromeShellStyle
import dev.tonholo.portfolio.theme.layout.SiteDimensions
import dev.tonholo.portfolio.theme.layout.unit.dp
import dev.tonholo.portfolio.theme.typography.FontFamilies
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.CSSMediaQuery
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.FlexWrap
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.StylePropertyValue
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Ul
import org.jetbrains.compose.web.dom.Footer as WebFooter
import org.jetbrains.compose.web.dom.Text as ComposeText

// ---------------------------------------------------------------------------
// Container chrome. Handoff `.footer`:
//   border-top 1px paper-3 (paper-2 dark), mono family, 12px, muted color.
// Vertical padding is SiteDimensions.padding.footerVertical (token-driven so
// it scales with breakpoints). The handoff's 80px margin-top is intentionally
// dropped: the previous content section's padding.section bottom token now
// provides the separation between page body and footer.
// ---------------------------------------------------------------------------
private val FOOTER_BORDER_WIDTH = 1.dp
private val FOOTER_FONT_SIZE = 12.dp

// Handoff `.footer-grid`:
//   grid 1.4fr 1fr 1fr 1fr, gap 48px, margin-bottom 48px; 2 cols below 820px.
private const val FOOTER_GRID_TAGLINE_FR = 1.4
private const val FOOTER_GRID_COL_FR = 1
private val FOOTER_GRID_GAP = 48.dp
private val FOOTER_GRID_MARGIN_BOTTOM = 48.dp

// Handoff `.footer h4`:
//   mono 11px / 600 / 0.08em / uppercase, color ink-3, margin-bottom 14px.
private val FOOTER_HEADING_FONT_SIZE = 11.dp
private const val FOOTER_HEADING_LETTER_SPACING_EM = 0.08
private val FOOTER_HEADING_MARGIN_BOTTOM = 14.dp

// Handoff tagline `p` in first column: 13px / 1.55 / body family / ink-3 / 34ch.
private val FOOTER_TAGLINE_FONT_SIZE = 13.dp
private const val FOOTER_TAGLINE_LINE_HEIGHT = 1.55
private const val FOOTER_TAGLINE_MAX_WIDTH_CH = 34.0

// Handoff `.footer ul`:
//   list-style none, flex column, gap 8px.
private val FOOTER_LIST_GAP = 8.dp

// Handoff `.footer a`:
//   color ink-2, transition color 0.18s; hover → accent.
private const val FOOTER_LINK_TRANSITION_DURATION_S = 0.18

// WCAG 2.5.5 touch-target floor applied on `pointer: coarse` devices so the
// 16-31px inline heights grow to a comfortable tap zone.
private val FOOTER_LINK_TOUCH_MIN_HEIGHT = 44.dp
private val FOOTER_LINK_TOUCH_PADDING_Y = 10.dp

// Handoff `.footer-bottom`:
//   flex row, space-between, padding-top 24px, 1px top border.
private val FOOTER_BOTTOM_PADDING_TOP = 24.dp

// Inline gap between the copyright segments and the centered separator dots.
private val FOOTER_BOTTOM_LEFT_GAP = 8.dp

// Vertical rhythm between stacked rows of the footer bottom bar on mobile.
private val FOOTER_BOTTOM_STACK_GAP = 8.dp

// Work column entries stay as mono technical literals (product / project
// names), not user-facing copy that needs translation. Slug values match
// the seed project ids in `StaticContent.PROJECTS` and route through
// `Routes.project(...)` when the row is clicked.
private const val WORK_THUNDERBIRD_LABEL = "Thunderbird"
private const val WORK_THUNDERBIRD_SLUG = "thunderbird"
private const val WORK_BLEACHER_REPORT_LABEL = "Bleacher Report"
private const val WORK_BLEACHER_REPORT_SLUG = "bleacher-report"
private const val WORK_PINTEREST_LABEL = "Pinterest"
private const val WORK_PINTEREST_SLUG = "pinterest"
private const val WORK_SVG_TO_COMPOSE_LABEL = "SVG to Compose"
private const val WORK_SVG_TO_COMPOSE_SLUG = "svg-compose"

// Elsewhere column rows are static social destinations (labels match the
// handles), so they stay as constants rather than localized strings.
private const val GITHUB_LABEL = "github.com/rafaeltonholo"
private const val GITHUB_HREF = "https://github.com/rafaeltonholo"
private const val LINKEDIN_LABEL = "linkedin.com/in/rafaeltonholo"
private const val LINKEDIN_HREF = "https://www.linkedin.com/in/rafaeltonholo"
private const val TWITTER_LABEL = "twitter/@rafaeltonholo"
private const val TWITTER_HREF = "https://twitter.com/rafaeltonholo"
private const val EMAIL_LABEL = "rafael@tonholo.dev"
private const val EMAIL_HREF = "mailto:rafael@tonholo.dev"

/**
 * Resolves the hairline border color shared by `.footer` and `.footer-bottom`.
 * Light theme uses `paper-3`; dark theme overrides to `paper-2` (matches the
 * `[data-theme="dark"] .footer` / `.footer-bottom` selectors in the handoff).
 */
@Composable
private fun footerBorderColor(): Color {
    val tokens = LocalPortfolioTokens.current
    val colorMode = ColorMode.current
    return when (colorMode) {
        ColorMode.LIGHT -> tokens.paper.paper3
        ColorMode.DARK -> tokens.paper.paper2
    }
}

/**
 * Chrome footer organism. Maps to `Footer` in `chrome.jsx` and the `.footer`,
 * `.footer-grid`, `.footer-bottom` rules in the handoff CSS.
 *
 * Layout: a 4-column grid (`1.4fr 1fr 1fr 1fr`) of column blocks, followed by
 * a bottom bar with a top hairline. Below the `LG` breakpoint (820px) the grid
 * collapses to two columns, matching the handoff media query.
 *
 * The first column is a short tagline (hardcoded for v1). The `Pages` and
 * `Work` columns render real `<a href>` anchors via Kobweb's [Link], so
 * middle-click, cmd-click, and "copy link address" behave like any browser
 * anchor. `Elsewhere` renders [SocialLink] rows with external URLs.
 *
 * @param modifier optional [Modifier] chained after the base styles.
 * @param versionInfo right-aligned bottom bar text, e.g.
 *   `"v4.0.0 - last deployed 3 days ago"`. Defaults to a placeholder so the
 *   build metadata can be wired up later without changing call sites.
 */
@Composable
fun Footer(modifier: Modifier = Modifier, versionInfo: String = DEFAULT_VERSION_INFO) {
    val tokens = LocalPortfolioTokens.current
    val strings = LocalStrings.current.footer
    val borderColor = footerBorderColor()

    val finalModifier = FooterStyle
        .toModifier()
        .color(tokens.muted.muted)
        .borderTop(
            width = FOOTER_BORDER_WIDTH,
            style = LineStyle.Solid,
            color = borderColor,
        )
        .then(modifier)

    WebFooter(attrs = finalModifier.toAttrs()) {
        Div(attrs = ChromeShellStyle.toModifier().toAttrs()) {
            Div(attrs = FooterGridStyle.toModifier().toAttrs()) {
                TaglineColumn(strings = strings)
                PagesColumn(strings = strings)
                WorkColumn(heading = strings.headingWork)
                ElsewhereColumn(heading = strings.headingElsewhere)
            }
            FooterBottom(
                leftSegments = strings.bottomLeftSegments,
                versionInfo = versionInfo,
                borderColor = borderColor,
            )
        }
    }
}

/**
 * First column: `Rafael Tonholo` heading + short tagline paragraph capped at
 * 34ch (matches `maxWidth:'34ch'` in the JSX).
 */
@Composable
private fun TaglineColumn(strings: Strings.Footer) {
    val tokens = LocalPortfolioTokens.current

    Div(attrs = Modifier.toAttrs()) {
        ColumnHeading(text = strings.headingName)
        P(
            attrs = Modifier
                .margin(topBottom = 0.cssRem)
                .fontFamily(*FontFamilies.BODY)
                .fontSize(FOOTER_TAGLINE_FONT_SIZE)
                .lineHeight(FOOTER_TAGLINE_LINE_HEIGHT)
                .color(tokens.ink.ink3)
                .maxWidth(
                    FOOTER_TAGLINE_MAX_WIDTH_CH.unsafeCast<CSSSizeValue<CSSUnit.ch>>(),
                )
                .toAttrs(),
        ) {
            ComposeText(strings.tagline)
        }
    }
}

/**
 * Second column: the four primary navigation links. Each anchor is a real
 * `<a href>` via Kobweb's [Link] so browser navigation affordances work.
 */
@Composable
private fun PagesColumn(strings: Strings.Footer) {
    Div(attrs = Modifier.toAttrs()) {
        ColumnHeading(text = strings.headingPages)
        Ul(attrs = FooterListStyle.toModifier().toAttrs()) {
            FooterNavItem(label = strings.linkHome, route = Routes.HOME)
            FooterNavItem(label = strings.linkAbout, route = Routes.ABOUT)
            FooterNavItem(label = strings.linkArticles, route = Routes.ARTICLES)
            FooterNavItem(label = strings.linkResume, route = Routes.RESUME)
        }
    }
}

/**
 * Third column: featured project names. Each row renders as a real
 * `<a href>` to its project detail page; slugs mirror the ids in
 * `StaticContent.PROJECTS`. Labels are mono technical literals and stay
 * untranslated.
 */
@Composable
private fun WorkColumn(heading: String) {
    val locale = LocalLocaleState.current.current
    Div(attrs = Modifier.toAttrs()) {
        ColumnHeading(text = heading)
        Ul(attrs = FooterListStyle.toModifier().toAttrs()) {
            FooterNavItem(
                label = WORK_THUNDERBIRD_LABEL,
                route = Routes.project(slug = WORK_THUNDERBIRD_SLUG, locale = locale),
            )
            FooterNavItem(
                label = WORK_BLEACHER_REPORT_LABEL,
                route = Routes.project(slug = WORK_BLEACHER_REPORT_SLUG, locale = locale),
            )
            FooterNavItem(
                label = WORK_PINTEREST_LABEL,
                route = Routes.project(slug = WORK_PINTEREST_SLUG, locale = locale),
            )
            FooterNavItem(
                label = WORK_SVG_TO_COMPOSE_LABEL,
                route = Routes.project(slug = WORK_SVG_TO_COMPOSE_SLUG, locale = locale),
            )
        }
    }
}

/**
 * Fourth column: external social/email destinations. Uses the [SocialLink]
 * molecule so each row gets the shared `arrow-up-right` trailing glyph and the
 * hover accent flip defined there. Row labels are handles / URLs and stay
 * untranslated.
 */
@Composable
private fun ElsewhereColumn(heading: String) {
    Div(attrs = Modifier.toAttrs()) {
        ColumnHeading(text = heading)
        Ul(attrs = FooterListStyle.toModifier().toAttrs()) {
            Li(attrs = Modifier.toAttrs()) {
                SocialLink(label = GITHUB_LABEL, href = GITHUB_HREF)
            }
            Li(attrs = Modifier.toAttrs()) {
                SocialLink(label = LINKEDIN_LABEL, href = LINKEDIN_HREF)
            }
            Li(attrs = Modifier.toAttrs()) {
                SocialLink(label = TWITTER_LABEL, href = TWITTER_HREF)
            }
            Li(attrs = Modifier.toAttrs()) {
                SocialLink(label = EMAIL_LABEL, href = EMAIL_HREF)
            }
        }
    }
}

/**
 * Footer column heading. Maps to `.footer h4` in the handoff: mono, 11px / 600,
 * uppercase with +0.08em tracking, colored `ink-3`, 14px bottom margin.
 */
@Composable
private fun ColumnHeading(text: String) {
    val tokens = LocalPortfolioTokens.current

    H3(
        attrs = FooterHeadingStyle
            .toModifier()
            .color(tokens.ink.ink3)
            .toAttrs(),
    ) {
        ComposeText(text)
    }
}

/**
 * Nav list row rendered as a real `<li><a href>` anchor via Kobweb's [Link].
 * The route resolves against registered Kobweb pages; browser middle-click
 * and cmd-click behave naturally.
 */
@Composable
private fun FooterNavItem(label: String, route: String) {
    Li(attrs = Modifier.toAttrs()) {
        Link(
            path = route,
            modifier = FooterLinkStyle
                .toModifier()
                .cursor(Cursor.Pointer),
            variant = UndecoratedLinkVariant then UncoloredLinkVariant,
        ) {
            ComposeText(label)
        }
    }
}

/**
 * Bottom bar. Renders a two-row flex strip with a hairline top border (same
 * color as the outer footer border).
 */
@Composable
private fun FooterBottom(leftSegments: List<String>, versionInfo: String, borderColor: Color) {
    Div(
        attrs = FooterBottomStyle
            .toModifier()
            .borderTop(
                width = FOOTER_BORDER_WIDTH,
                style = LineStyle.Solid,
                color = borderColor,
            )
            .toAttrs(),
    ) {
        Span(attrs = FooterBottomLeftSegmentsStyle.toModifier().toAttrs()) {
            leftSegments.forEachIndexed { index, segment ->
                if (index > 0) {
                    MetaSeparator()
                }
                Span(attrs = Modifier.toAttrs()) { ComposeText(segment) }
            }
        }
        Span(attrs = Modifier.toAttrs()) {
            ComposeText(versionInfo)
        }
    }
}

// ---------------------------------------------------------------------------
// Styles
// ---------------------------------------------------------------------------

/**
 * Base `CssStyle` for the `<footer>` container. Ports `.footer` from the
 * handoff minus the border color (applied inline so it can flip per theme).
 * Color, border-top, and hover states are either inline or scoped to nested
 * styles.
 */
val FooterStyle = CssStyle {
    base {
        Modifier
            .padding(topBottom = SiteDimensions.padding.footerVertical)
            .fontFamily(*FontFamilies.MONO)
            .fontSize(FOOTER_FONT_SIZE)
    }
}

/**
 * Base `CssStyle` for `.footer-grid`. Below [Breakpoint.LG] (820px) the grid
 * uses two equal columns; from `LG` up it matches the handoff layout of
 * `1.4fr 1fr 1fr 1fr`.
 */
val FooterGridStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Grid)
            .gridTemplateColumns {
                minmax(0.px, FOOTER_GRID_COL_FR.fr)
                minmax(0.px, FOOTER_GRID_COL_FR.fr)
            }
            .gap(FOOTER_GRID_GAP)
            .margin(bottom = FOOTER_GRID_MARGIN_BOTTOM)
    }
    Breakpoint.LG {
        Modifier.gridTemplateColumns {
            minmax(0.px, FOOTER_GRID_TAGLINE_FR.fr)
            minmax(0.px, FOOTER_GRID_COL_FR.fr)
            minmax(0.px, FOOTER_GRID_COL_FR.fr)
            minmax(0.px, FOOTER_GRID_COL_FR.fr)
        }
    }
}

/**
 * Base `CssStyle` for `.footer h4`. Typography is static so the entire rule is
 * encoded here; color is applied inline so it can read from tokens.
 */
val FooterHeadingStyle = CssStyle {
    base {
        Modifier
            .fontFamily(*FontFamilies.MONO)
            .fontSize(FOOTER_HEADING_FONT_SIZE)
            .fontWeight(FontWeight.SemiBold)
            .letterSpacing(FOOTER_HEADING_LETTER_SPACING_EM.em)
            .textTransform(TextTransform.Uppercase)
            .margin(
                top = 0.cssRem,
                bottom = FOOTER_HEADING_MARGIN_BOTTOM,
            )
    }
}

/**
 * Base `CssStyle` for `.footer ul`. Flex column with 8px gap; strips default
 * list markers and padding so the rows align with the column heading.
 */
val FooterListStyle = CssStyle {
    base {
        Modifier
            .listStyle(type = ListStyleType.None)
            .display(DisplayStyle.Flex)
            .flexDirection(FlexDirection.Column)
            .gap(FOOTER_LIST_GAP)
            .margin(topBottom = 0.cssRem)
            .padding(left = 0.cssRem)
    }
}

/**
 * Shared `CssStyle` for footer anchors (both routed nav and static Work rows).
 * Ports `.footer a` / `.footer a:hover`: rests on the `ink-2` token so links
 * read as copy rather than chrome, and transitions to the theme accent on
 * hover. The explicit base color overrides the Silk `Link` variant defaults
 * so visited links do not drift to browser purple.
 */
val FooterLinkStyle = CssStyle {
    base {
        val isDark = colorMode == ColorMode.DARK
        val ink = if (isDark) Ink.dark else Ink.light
        Modifier
            .color(ink.ink2)
            .textDecorationLine(TextDecorationLine.None)
            .cursor(Cursor.Pointer)
            .transition(
                Transition.of(
                    property = "color",
                    duration = FOOTER_LINK_TRANSITION_DURATION_S.s,
                ),
            )
    }
    hover {
        val accent = if (colorMode == ColorMode.DARK) AccentMint.dark else AccentMint.light
        Modifier.color(accent.accent)
    }
    // Coarse-pointer devices (phones, tablets) grow each row to a 44px
    // tap zone per WCAG 2.5.5; mouse pointing stays pixel-precise so
    // desktop keeps the compact editorial rhythm.
    cssRule(
        mediaQuery = CSSMediaQuery.MediaFeature(
            name = "pointer",
            value = StylePropertyValue(value = "coarse"),
        ),
    ) {
        Modifier
            .display(DisplayStyle.LegacyInlineFlex)
            .alignItems(AlignItems.Center)
            .minHeight(FOOTER_LINK_TOUCH_MIN_HEIGHT)
            .padding(topBottom = FOOTER_LINK_TOUCH_PADDING_Y)
    }
}

/**
 * Base `CssStyle` for `.footer-bottom`. On mobile the bar stacks as a column
 * with a small gap so the copyright/build segments and the version slug each
 * get their own line instead of fighting `space-between` for horizontal room
 * (the handoff row otherwise wraps segments into narrow vertical stacks under
 * ~600px). From [Breakpoint.MD] the handoff layout kicks in: flex row with
 * space-between and a 24px top padding. The 1px top border is applied inline
 * by the composable so its color can follow the active theme.
 */
val FooterBottomStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Flex)
            .flexDirection(FlexDirection.Column)
            .gap(FOOTER_BOTTOM_STACK_GAP)
            .padding(top = FOOTER_BOTTOM_PADDING_TOP)
    }
    Breakpoint.MD {
        Modifier
            .flexDirection(FlexDirection.Row)
            .justifyContent(JustifyContent.SpaceBetween)
            .gap(0.cssRem)
    }
}

/**
 * Base `CssStyle` for the left cluster of the bottom bar (copyright + build
 * notes joined by [MetaSeparator] dots). Renders as a wrapping inline-flex so
 * the segments reflow across rows on narrow viewports instead of overflowing
 * the shell, while the dot separators stay inline with their neighbouring
 * copy on every row.
 */
val FooterBottomLeftSegmentsStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Flex)
            .flexWrap(FlexWrap.Wrap)
            .alignItems(AlignItems.Center)
            .gap(rowGap = FOOTER_BOTTOM_STACK_GAP, columnGap = FOOTER_BOTTOM_LEFT_GAP)
    }
}

/**
 * Fallback copy for the bottom bar version slug. Call sites should pass in a
 * real value derived from `app.properties`.
 */
private const val DEFAULT_VERSION_INFO = "v4.0.0 - last deployed 3 days ago"
