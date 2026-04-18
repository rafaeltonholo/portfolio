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

// Handoff `.section { padding: var(--pad-y) 0; }` with --pad-y = 80px at the
// default density. Translates to 5rem top/bottom breathing room for page-level
// sections (About, Resume) that don't use the hero's larger rhythm.
private const val SECTION_PADDING_Y_REM = 5.0

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
 * vertical padding (5rem top and bottom). Used by route-level pages like
 * About and Resume whose top-of-page organisms expect a hero-like top gutter
 * below the sticky nav.
 */
val PageSectionShellStyle = ShellStyle.extendedBy {
    base {
        Modifier.padding(topBottom = SECTION_PADDING_Y_REM.cssRem)
    }
}
