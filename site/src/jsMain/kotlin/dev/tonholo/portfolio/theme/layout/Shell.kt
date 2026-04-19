package dev.tonholo.portfolio.theme.layout

import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.extendedBy
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.keywords.auto

private const val SHELL_MAX_WIDTH_REM = 80.0
private const val SHELL_GUTTER_MOBILE_REM = 1.5
private const val SHELL_GUTTER_DESKTOP_REM = 3.0

/**
 * Page-level shell container. Caps content at 80rem (1280px) wide and centers
 * horizontally. Horizontal padding is only applied on mobile (1.5rem); from
 * [Breakpoint.MD] upwards the shell has zero horizontal padding so page-level
 * sections (hero, about, resume, article, project detail) align to the shell's
 * outer border - matching the handoff where every section class layered on
 * `.shell` overrides `padding: 0 var(--gutter)` to `padding: X 0 Y` and
 * neutralizes the horizontal gutter. Chrome elements that still need the 48px
 * gutter (nav-inner, footer) add it back explicitly.
 *
 * `Breakpoint.MD { ... }` resolves to `min-width: 45rem` (720px) per the Silk
 * breakpoint configuration in [dev.tonholo.portfolio.theme.initBreakpoints], so
 * the base block is the mobile case and the MD override is the desktop case.
 */
val ShellStyle = CssStyle {
    base {
        Modifier
            .maxWidth(SHELL_MAX_WIDTH_REM.cssRem)
            .margin(
                topBottom = 0.cssRem,
                leftRight = auto.unsafeCast<CSSLengthOrPercentageNumericValue>(),
            )
            .padding(leftRight = SHELL_GUTTER_MOBILE_REM.cssRem)
    }
    Breakpoint.MD {
        Modifier.padding(leftRight = 0.cssRem)
    }
}

/**
 * Chrome shell variant that keeps the desktop gutter: 1.5rem on mobile, 3rem
 * from [Breakpoint.MD]. Used by the top nav and footer inner containers so
 * their contents sit inside a familiar padded frame even though the body
 * sections align flush with the shell's outer border.
 */
val ChromeShellStyle = ShellStyle.extendedBy {
    Breakpoint.MD {
        Modifier.padding(leftRight = SHELL_GUTTER_DESKTOP_REM.cssRem)
    }
}

/**
 * Page section shell: extends [ShellStyle] with the handoff `.section`
 * vertical padding. The padding tracks the active density via
 * [SpacingVars.PadY], which [dev.tonholo.portfolio.theme.PortfolioTheme]
 * writes from the active [SpacingScale]; a density change flows to every
 * consumer without touching call sites. Used by route-level pages (About,
 * Resume, project detail, article detail) and by every numbered home-page
 * section via `NumberedSection`.
 */
val PageSectionShellStyle = ShellStyle.extendedBy {
    base {
        Modifier.padding(topBottom = SpacingVars.PadY.value())
    }
}
