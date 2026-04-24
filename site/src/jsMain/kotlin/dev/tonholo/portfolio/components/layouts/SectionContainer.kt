package dev.tonholo.portfolio.components.layouts

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flexDirection
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.layout.SiteDimensions
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.dom.Section

/**
 * Outer `<section>` chrome. `base` is mobile-first (the project does not
 * declare a separate `Breakpoint.SM` tier); from [Breakpoint.MD] the
 * horizontal gutter ramps up. Vertical padding is
 * [SiteDimensions.padding.section] at every breakpoint and grows through
 * the underlying [SiteDimensions.size.Xxl] CSS variable. Inner content is
 * centered as a flex column so the [SectionContentStyle] cap applies
 * max-width.
 */
val SectionContainerStyle = CssStyle {
    base {
        Modifier
            .fillMaxWidth()
            .padding(
                topBottom = SiteDimensions.padding.section,
                leftRight = SiteDimensions.size.Lg,
            )
            .display(DisplayStyle.Flex)
            .flexDirection(FlexDirection.Column)
            .alignItems(AlignItems.Center)
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
 * and stretches to fill the available width below that cap. Consumers compose
 * their own grid / column inside this wrapper.
 */
val SectionContentStyle = CssStyle {
    base {
        Modifier
            .fillMaxWidth()
            .maxWidth(SiteDimensions.layout.contentMaxWidth)
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
