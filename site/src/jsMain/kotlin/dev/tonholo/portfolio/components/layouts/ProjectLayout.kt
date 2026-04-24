package dev.tonholo.portfolio.components.layouts

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.components.organisms.projects.PrevNextNav
import dev.tonholo.portfolio.theme.layout.SiteDimensions
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Section

/**
 * Outer `<section>` chrome for the project detail page. Vertical rhythm uses
 * [SiteDimensions.padding.heroTop] on top (extra breathing room above the
 * breadcrumb + hero) and [SiteDimensions.padding.section] on bottom; the
 * horizontal gutter ramps mobile-first from [SiteDimensions.size.Lg] at the
 * base to [SiteDimensions.size.Xxl] from [Breakpoint.MD], matching every
 * other section atom across the site.
 */
val ProjectLayoutShellStyle = CssStyle {
    base {
        Modifier.padding(
            top = SiteDimensions.padding.heroTop,
            bottom = SiteDimensions.padding.section,
            leftRight = SiteDimensions.size.Lg,
        )
    }
    Breakpoint.MD {
        Modifier.padding(
            top = SiteDimensions.padding.heroTop,
            bottom = SiteDimensions.padding.section,
            leftRight = SiteDimensions.size.Xxl,
        )
    }
}

/**
 * Layout for project detail pages. Delegates the chrome (top nav, footer,
 * language toast) to [PageLayout] and wraps the page body in a single
 * `<section>` styled with [ProjectLayoutShellStyle], with the inner
 * [SectionContentStyle] cap so content stays under
 * [SiteDimensions.layout.contentMaxWidth]. A [PrevNextNav] strip is rendered
 * after the content so the sticky footer sits within the same capped
 * container as the article body.
 *
 * When [nextProjectName] / [nextRoute] is `null` the `next: {project}` slot
 * in [PrevNextNav] is hidden, matching the behaviour for the final project
 * in the series. Back / next links are real `<a href>` anchors.
 *
 * @param backRoute destination path for the back link in [PrevNextNav].
 * @param nextProjectName the human-readable name of the next project, or
 *   `null` to hide the right slot of [PrevNextNav].
 * @param nextRoute destination path for the next link; pass `null` alongside
 *   [nextProjectName] for the final project in the series.
 * @param modifier optional [Modifier] applied to the outer `<section>`.
 * @param content the project body rendered inside the shell.
 */
@Composable
fun ProjectLayout(
    backRoute: String,
    nextProjectName: String?,
    nextRoute: String?,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    PageLayout {
        Section(
            attrs = ProjectLayoutShellStyle
                .toModifier()
                .then(modifier)
                .toAttrs(),
        ) {
            Div(attrs = SectionContentStyle.toModifier().toAttrs()) {
                content()
                PrevNextNav(
                    backRoute = backRoute,
                    nextProjectName = nextProjectName,
                    nextRoute = nextRoute,
                )
            }
        }
    }
}
