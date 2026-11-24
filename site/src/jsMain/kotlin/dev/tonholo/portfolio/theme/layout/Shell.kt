package dev.tonholo.portfolio.theme.layout

import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.functions.calc
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.keywords.auto

/**
 * Chrome shell for the nav-inner and footer-inner containers. Uses the same horizontal gutter and the same
 * [SiteDimensions.layout.contentMaxWidth] column as the page sections
 * ([dev.tonholo.portfolio.components.layouts.SectionContainerStyle] and the hero shells), so the nav, the page
 * content and the footer share the same left and right edges at every width: [SiteDimensions.size.Lg] below
 * [Breakpoint.MD] and [SiteDimensions.size.Xxl] from it.
 */
val ChromeShellStyle = CssStyle {
    base {
        Modifier
            .maxWidth(contentWidthWithGutter(gutter = SiteDimensions.size.Lg))
            .margin(
                topBottom = 0.cssRem,
                leftRight = auto.unsafeCast<CSSLengthOrPercentageNumericValue>(),
            )
            .padding(leftRight = SiteDimensions.size.Lg)
    }
    Breakpoint.MD {
        Modifier
            .maxWidth(contentWidthWithGutter(gutter = SiteDimensions.size.Xxl))
            .padding(leftRight = SiteDimensions.size.Xxl)
    }
}

private fun contentWidthWithGutter(gutter: CSSSizeValue<CSSUnit.rem>) =
    calc { SiteDimensions.layout.contentMaxWidth + gutter + gutter }
