package dev.tonholo.portfolio.components.layouts

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.layout.SiteDimensions
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.dom.Section

/**
 * Outer `<section>` chrome. `base` is mobile-first (the project does not
 * declare a separate `Breakpoint.SM` tier); from [Breakpoint.MD] the
 * horizontal gutter ramps up. Vertical padding is
 * [SiteDimensions.padding.section] at every breakpoint and grows through
 * the underlying [SiteDimensions.size.Xxl] CSS variable. The inner
 * [SectionContentStyle] caps content width and self-centers via `margin: 0
 * auto`, so the outer section does not need to be a flex container.
 */
val SectionContainerStyle = CssStyle {
    base {
        Modifier
            .fillMaxWidth()
            .padding(
                topBottom = SiteDimensions.padding.section,
                leftRight = SiteDimensions.size.Lg,
            )
    }
    Breakpoint.MD {
        Modifier.padding(
            topBottom = SiteDimensions.padding.section,
            leftRight = SiteDimensions.size.Xxl,
        )
    }
}

/**
 * Inner content container: caps width at [SiteDimensions.layout.contentMaxWidth]
 * and stretches to fill the available width below that cap. Self-centers
 * horizontally via `margin: 0 auto` so any parent (flex, block, grid) renders
 * the cap centered on viewports wider than 80rem without needing its own
 * centering rule.
 */
val SectionContentStyle = CssStyle {
    base {
        Modifier
            .fillMaxWidth()
            .maxWidth(SiteDimensions.layout.contentMaxWidth)
            .margin(
                topBottom = 0.cssRem,
                leftRight = auto.unsafeCast<CSSLengthOrPercentageNumericValue>(),
            )
    }
}

/**
 * Default section layout primitive for content-bearing organisms. Wraps the
 * passed [content] in a semantic `<section id="...">` with the standard
 * vertical rhythm ([SiteDimensions.padding.section]), the responsive
 * horizontal gutter ([SiteDimensions.size.Lg], `Xl`, `Xxl`), and a centered
 * inner column constrained to [SiteDimensions.layout.contentMaxWidth].
 *
 * Use this for default-rhythm organisms (selected work, articles list,
 * projects list, about tabs, resume body). Hero-style and chrome organisms
 * render their own `<section>` with a different padding token rather than
 * wrapping in this helper.
 *
 * @param id stable HTML id used for in-page anchors.
 * @param modifier applied to the outer `<section>` after the base style.
 * @param contentModifier applied to the inner column after the base style.
 * @param altBackground when true, paints the outer `<section>` with the
 *   `paper2` token to alternate visual rhythm between adjacent sections. Off
 *   by default; reserve for sections that earn the contrast (e.g., the
 *   hiring CTA on the home page).
 * @param content composable lambda rendered inside the constrained inner
 *   column.
 */
@Composable
fun SectionContainer(
    id: String,
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    altBackground: Boolean = false,
    content: @Composable () -> Unit,
) {
    val tokens = LocalPortfolioTokens.current
    Section(
        attrs = SectionContainerStyle.toModifier()
            .id(id)
            .then(if (altBackground) Modifier.backgroundColor(tokens.paper.paper2) else Modifier)
            .then(modifier)
            .toAttrs(),
    ) {
        Column(
            modifier = SectionContentStyle.toModifier().then(contentModifier),
            horizontalAlignment = Alignment.Start,
        ) {
            content()
        }
    }
}
