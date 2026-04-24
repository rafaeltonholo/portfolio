package dev.tonholo.portfolio.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.components.atoms.MetaLabel
import dev.tonholo.portfolio.components.atoms.MetaLabelVariant
import dev.tonholo.portfolio.components.atoms.VisuallyHiddenStyle
import dev.tonholo.portfolio.components.layouts.PageLayout
import dev.tonholo.portfolio.components.layouts.SectionContainer
import dev.tonholo.portfolio.components.layouts.SectionContentStyle
import dev.tonholo.portfolio.components.organisms.home.ProjectsGrid
import dev.tonholo.portfolio.components.organisms.projects.ProjectsFilter
import dev.tonholo.portfolio.components.organisms.projects.ProjectsFilterBar
import dev.tonholo.portfolio.domain.model.ProjectKind
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.state.LocalLocaleState
import dev.tonholo.portfolio.state.StaticContent
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.layout.SiteDimensions
import dev.tonholo.portfolio.theme.typography.Typography
import dev.tonholo.portfolio.theme.typography.apply
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Section
import org.jetbrains.compose.web.dom.Text as ComposeText

// Mono eyebrow is a technical path literal and stays untranslated.
private const val META_TEXT = "// projects/"

private const val META_MARGIN_BOTTOM_REM = 1.0
private const val TITLE_MARGIN_BOTTOM_REM = 2.0

/**
 * Outer `<section>` chrome for [ProjectsPageHeader]. Mirrors the hero-shaped
 * pattern shared with `ArticlesHeaderSectionStyle` and `HeroEditorialSectionStyle`:
 * vertical rhythm uses [SiteDimensions.padding.heroTop] on top and
 * [SiteDimensions.padding.section] on bottom; the horizontal gutter ramps
 * mobile-first from [SiteDimensions.size.Lg] at the base to
 * [SiteDimensions.size.Xxl] from [Breakpoint.MD].
 */
val ProjectsHeaderSectionStyle = CssStyle {
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
 * Projects route "/projects" - renders the editorial "Work." header, a client-
 * side filter bar (all / commercial / open source), and the selected-work grid
 * filtered against the active [ProjectsFilter].
 *
 * The `routeOverride` collapses Kobweb's default `ProjectsPage` ->
 * `/projects-page` path to the expected `/projects` slug.
 */
@Page(routeOverride = "/projects")
@Composable
@Suppress("ModifierMissing")
fun ProjectsPage() {
    var filter by remember { mutableStateOf(value = ProjectsFilter.All) }
    val locale = LocalLocaleState.current.current
    val projects = StaticContent.projectsFor(locale = locale)
    val filtered = when (filter) {
        ProjectsFilter.All -> projects
        ProjectsFilter.Commercial -> projects.filter { it.kind == ProjectKind.Commercial }
        ProjectsFilter.OpenSource -> projects.filter { it.kind == ProjectKind.OpenSource }
    }

    PageLayout {
        ProjectsPageHeader(current = filter, onSelect = { filter = it })
        SectionContainer(id = "projects-grid") {
            // Visually-hidden section heading: the card grid below is a peer
            // section to the "Work." page title. Keeps the document outline
            // h1 -> h2 -> h3 (footer) without adding visible chrome.
            H2(attrs = VisuallyHiddenStyle.toModifier().toAttrs()) {
                ComposeText("Selected projects")
            }
            ProjectsGrid(projects = filtered)
        }
    }
}

/**
 * Editorial header for the Projects list route: accent meta label, display
 * "Work." title, and the inline [ProjectsFilterBar] for the active filter.
 * Renders its own `<section>` with the hero-shaped padding pattern shared
 * by the home, articles, about, and resume page headers.
 */
@Composable
private fun ProjectsPageHeader(current: ProjectsFilter, onSelect: (ProjectsFilter) -> Unit) {
    val tokens = LocalPortfolioTokens.current
    val strings = LocalStrings.current.projectsList
    Section(
        attrs = ProjectsHeaderSectionStyle
            .toModifier()
            .id("projects-header")
            .toAttrs(),
    ) {
        Div(attrs = SectionContentStyle.toModifier().toAttrs()) {
            MetaLabel(
                text = META_TEXT,
                variant = MetaLabelVariant.Accent,
                modifier = Modifier
                    .display(DisplayStyle.Block)
                    .margin(bottom = META_MARGIN_BOTTOM_REM.cssRem),
            )
            H1(
                attrs = Modifier
                    .apply(Typography.h1)
                    .color(tokens.ink.ink)
                    .margin(top = 0.cssRem, bottom = TITLE_MARGIN_BOTTOM_REM.cssRem)
                    .toAttrs(),
            ) {
                ComposeText(strings.title)
            }
            ProjectsFilterBar(current = current, onSelect = onSelect)
        }
    }
}
