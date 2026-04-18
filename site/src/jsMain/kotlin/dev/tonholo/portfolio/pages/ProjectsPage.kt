package dev.tonholo.portfolio.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.components.atoms.MetaLabel
import dev.tonholo.portfolio.components.atoms.MetaLabelVariant
import dev.tonholo.portfolio.components.layouts.PageLayout
import dev.tonholo.portfolio.components.organisms.home.SelectedWorkGrid
import dev.tonholo.portfolio.components.organisms.projects.ProjectsFilter
import dev.tonholo.portfolio.components.organisms.projects.ProjectsFilterBar
import dev.tonholo.portfolio.domain.model.ProjectKind
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.navigation.Routes
import dev.tonholo.portfolio.state.StaticContent
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.layout.ShellStyle
import dev.tonholo.portfolio.theme.typography.Typography
import dev.tonholo.portfolio.theme.typography.apply
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Text as ComposeText

// Mono eyebrow is a technical path literal and stays untranslated.
private const val META_TEXT = "// projects/"

// Header vertical rhythm, mirrors the pad-y density cadence used by
// [ArticlesHeader] and [ResumeHeader].
private const val HEADER_PADDING_TOP_REM = 3.6
private const val HEADER_PADDING_BOTTOM_REM = 1.8
private const val META_MARGIN_BOTTOM_REM = 1.0
private const val TITLE_MARGIN_BOTTOM_REM = 2.0

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
    val router = rememberPageContext().router
    var filter by remember { mutableStateOf(value = ProjectsFilter.All) }
    val filtered = when (filter) {
        ProjectsFilter.All -> StaticContent.PROJECTS
        ProjectsFilter.Commercial -> StaticContent.PROJECTS.filter { it.kind == ProjectKind.Commercial }
        ProjectsFilter.OpenSource -> StaticContent.PROJECTS.filter { it.kind == ProjectKind.OpenSource }
    }

    PageLayout {
        Div(attrs = ShellStyle.toModifier().toAttrs()) {
            ProjectsPageHeader(current = filter, onSelect = { filter = it })
            SelectedWorkGrid(
                projects = filtered,
                onProjectClick = { slug ->
                    router.tryRoutingTo(pathQueryAndFragment = Routes.project(slug = slug))
                },
                showSectionHead = false,
            )
        }
    }
}

/**
 * Editorial header for the Projects list route: accent meta label, display
 * "Work." title, and the inline [ProjectsFilterBar] for the active filter.
 */
@Composable
private fun ProjectsPageHeader(current: ProjectsFilter, onSelect: (ProjectsFilter) -> Unit) {
    val tokens = LocalPortfolioTokens.current
    val strings = LocalStrings.current.projectsList
    Div(
        attrs = Modifier
            .padding(
                top = HEADER_PADDING_TOP_REM.cssRem,
                bottom = HEADER_PADDING_BOTTOM_REM.cssRem,
            )
            .toAttrs(),
    ) {
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
