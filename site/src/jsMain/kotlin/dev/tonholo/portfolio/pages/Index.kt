package dev.tonholo.portfolio.pages

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.core.Page
import dev.tonholo.portfolio.components.layouts.PageLayout
import dev.tonholo.portfolio.components.organisms.home.HeroEditorial
import dev.tonholo.portfolio.components.organisms.home.HiringCta
import dev.tonholo.portfolio.components.organisms.home.NowStrip
import dev.tonholo.portfolio.components.organisms.home.SelectedWorkGrid
import dev.tonholo.portfolio.components.organisms.home.SnippetOfTheWeek
import dev.tonholo.portfolio.components.organisms.home.UsesGrid
import dev.tonholo.portfolio.state.StaticContent

/**
 * Home route "/" - renders the full editorial stack: hero, selected-work
 * grid, now strip, snippet of the week, uses grid, and the hiring CTA. Each
 * project card is a real `<a href>` anchor so browser navigation
 * affordances (middle-click, cmd-click, copy-link-address) work.
 */
@Page
@Composable
@Suppress("ModifierMissing")
fun HomePage() {
    PageLayout {
        HeroEditorial()
        SelectedWorkGrid(projects = StaticContent.PROJECTS)
        NowStrip()
        SnippetOfTheWeek()
        UsesGrid()
        HiringCta()
    }
}
