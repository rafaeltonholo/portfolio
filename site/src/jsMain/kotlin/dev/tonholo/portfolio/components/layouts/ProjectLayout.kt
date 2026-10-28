package dev.tonholo.portfolio.components.layouts

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import dev.tonholo.portfolio.components.organisms.projects.PrevNextNav

/**
 * Layout for project detail pages. Delegates the chrome (top nav, footer,
 * language toast) to [PageLayout] and wraps the page body in a [HeroSection],
 * sharing the same hero-shaped padding (heroTop top, section bottom, Lg→Xxl
 * gutter) and content cap as every other long-form route. A [PrevNextNav]
 * strip is rendered after the content so the sticky footer sits within the
 * same capped container as the article body.
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
 * @param title optional page title applied to [document.title] via [PageLayout].
 * @param content the project body rendered inside the shell.
 */
@Composable
fun ProjectLayout(
    backRoute: String,
    nextProjectName: String?,
    nextRoute: String?,
    modifier: Modifier = Modifier,
    title: String? = null,
    content: @Composable () -> Unit,
) {
    PageLayout(title = title) {
        HeroSection(id = "project-shell", modifier = modifier) {
            content()
            PrevNextNav(
                backRoute = backRoute,
                nextProjectName = nextProjectName,
                nextRoute = nextRoute,
            )
        }
    }
}
