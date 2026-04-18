package dev.tonholo.portfolio.pages

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import dev.tonholo.portfolio.components.layouts.PageLayout
import dev.tonholo.portfolio.components.organisms.home.HeroEditorial
import dev.tonholo.portfolio.components.organisms.home.HiringCta
import dev.tonholo.portfolio.components.organisms.home.NowStrip
import dev.tonholo.portfolio.components.organisms.home.SelectedWorkGrid
import dev.tonholo.portfolio.components.organisms.home.SnippetOfTheWeek
import dev.tonholo.portfolio.components.organisms.home.UsesGrid
import dev.tonholo.portfolio.navigation.Routes
import dev.tonholo.portfolio.state.StaticContent

/**
 * Home route "/" - renders the full editorial stack: hero, selected-work
 * grid, now strip, snippet of the week, uses grid, and the hiring CTA.
 * Card clicks route to each project's detail page.
 */
@Page
@Composable
@Suppress("ModifierMissing")
fun HomePage() {
    val router = rememberPageContext().router
    PageLayout {
        HeroEditorial()
        SelectedWorkGrid(
            projects = StaticContent.PROJECTS,
            onProjectClick = { slug ->
                router.tryRoutingTo(pathQueryAndFragment = Routes.project(slug = slug))
            },
        )
        NowStrip()
        SnippetOfTheWeek()
        UsesGrid()
        HiringCta()
    }
}
