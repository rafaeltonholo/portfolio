package dev.tonholo.portfolio.theme.layout

import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.css.px

private const val SHELL_MAX_WIDTH_PX = 1280
private const val SHELL_GUTTER_MOBILE_PX = 24
private const val SHELL_GUTTER_DESKTOP_PX = 48

/**
 * Page-level shell container. Caps content at 1280px wide, centers horizontally,
 * and applies an inline gutter (24px on mobile, 48px from [Breakpoint.MD] upwards).
 *
 * `Breakpoint.MD { ... }` resolves to `min-width: 45rem` (720px) per the Silk
 * breakpoint configuration in [dev.tonholo.portfolio.theme.initBreakpoints], so
 * the base block is the mobile case and the MD override is the desktop case.
 */
val ShellStyle = CssStyle {
    base {
        Modifier
            .maxWidth(SHELL_MAX_WIDTH_PX.px)
            .margin(
                topBottom = 0.px,
                leftRight = auto.unsafeCast<CSSLengthOrPercentageNumericValue>(),
            )
            .padding(leftRight = SHELL_GUTTER_MOBILE_PX.px)
    }
    Breakpoint.MD {
        Modifier.padding(leftRight = SHELL_GUTTER_DESKTOP_PX.px)
    }
}
