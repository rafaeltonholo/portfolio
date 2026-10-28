package dev.tonholo.portfolio.theme.layout

import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.keywords.auto

private const val SHELL_MAX_WIDTH_REM = 80.0
private const val SHELL_GUTTER_MOBILE_REM = 1.5
private const val SHELL_GUTTER_DESKTOP_REM = 3.0

/**
 * Chrome shell for the nav-inner and footer-inner containers. Caps content
 * at 80rem (1280px) wide and centers horizontally; from [Breakpoint.MD]
 * upwards the horizontal gutter ramps to 3rem so chrome elements keep their
 * familiar padded frame even though body sections align flush with the
 * shell's outer border.
 *
 * Page content sections do NOT use this style; they consume
 * [dev.tonholo.portfolio.components.layouts.SectionContainerStyle] (or one
 * of the per-page hero shells), which sources horizontal padding from the
 * mobile-first [SiteDimensions.size] ramp.
 */
val ChromeShellStyle = CssStyle {
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
        Modifier.padding(leftRight = SHELL_GUTTER_DESKTOP_REM.cssRem)
    }
}
