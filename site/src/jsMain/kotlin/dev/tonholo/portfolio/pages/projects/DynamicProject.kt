package dev.tonholo.portfolio.pages.projects

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import dev.tonholo.portfolio.components.atoms.AspectRatio
import dev.tonholo.portfolio.components.atoms.Placeholder
import dev.tonholo.portfolio.components.layouts.ProjectLayout
import dev.tonholo.portfolio.components.molecules.ProjectBreadcrumb
import dev.tonholo.portfolio.components.organisms.projects.OutcomesGrid
import dev.tonholo.portfolio.components.organisms.projects.ProjectDetailHero
import dev.tonholo.portfolio.components.organisms.projects.ProjectOutcome
import dev.tonholo.portfolio.components.organisms.projects.ProjectSection
import dev.tonholo.portfolio.domain.model.Project
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.locale.Strings
import dev.tonholo.portfolio.navigation.Routes
import dev.tonholo.portfolio.state.LocalLocaleState
import dev.tonholo.portfolio.state.StaticContent
import dev.tonholo.portfolio.theme.layout.unit.dp
import org.jetbrains.compose.web.dom.Text

private const val PROJECT_SLUG_PARAM = "slug"

// `.project-cover` in the handoff CSS: 4px radius, 64px bottom margin above
// the first case-study section. Aspect ratio comes from the Placeholder atom.
private val COVER_RADIUS = 4.dp
private val COVER_MARGIN_BOTTOM = 64.dp

/**
 * Dynamic project route "/projects/{slug}" - looks up the requested project
 * in [StaticContent.PROJECTS] and renders a case-study shell. Projects with a
 * full editorial case study (currently Pinterest) live as markdown under
 * `src/jsMain/resources/markdown/projects/` and render through
 * [dev.tonholo.portfolio.components.layouts.ProjectMarkdownLayout]; this
 * dynamic route is the fallback that serves every other slug using the
 * project's [Project.description] plus the [OutcomesGrid] derived from
 * [Project.stats].
 *
 * The sticky [ProjectLayout] footer wires back/next navigation using the
 * project's index in the seed list.
 */
@Page(routeOverride = "/projects/{slug}")
@Composable
@Suppress("ModifierMissing")
fun DynamicProjectPage() {
    val pageContext = rememberPageContext()
    val strings = LocalStrings.current.projectDetail
    val locale = LocalLocaleState.current.current
    val projects = StaticContent.projectsFor(locale = locale)
    val slug = pageContext.route.params[PROJECT_SLUG_PARAM].orEmpty()
    val index = projects.indexOfFirst { it.slug == slug }
    val project = projects.getOrNull(index = index)
    val nextProject = projects.getOrNull(index = index + 1)

    if (project == null) {
        ProjectLayout(
            backRoute = Routes.PROJECTS,
            nextProjectName = null,
            nextRoute = null,
        ) {
            Text(strings.notFoundCopy)
        }
        return
    }

    ProjectLayout(
        backRoute = Routes.PROJECTS,
        nextProjectName = nextProject?.name,
        nextRoute = nextProject?.let { Routes.project(slug = it.slug) },
    ) {
        ProjectBreadcrumb(projectName = project.name)
        ProjectDetailHero(project = project)
        Placeholder(
            aspectRatio = AspectRatio.Hero16x9,
            label = strings.coverPlaceholderLabel,
            modifier = Modifier
                .borderRadius(COVER_RADIUS)
                .margin(bottom = COVER_MARGIN_BOTTOM),
        )
        DefaultProjectSections(project = project, strings = strings)
    }
}

/**
 * Fallback case-study layout for projects without a markdown case study: a
 * single context section with [Project.description] as body copy, followed by
 * an outcomes grid built from [Project.stats].
 */
@Composable
private fun DefaultProjectSections(project: Project, strings: Strings.ProjectDetail) {
    ProjectSection(label = strings.contextLabel, title = project.name) {
        Text(project.description)
    }
    ProjectSection(label = strings.outcomesLabel, title = strings.outcomesTitle) {
        OutcomesGrid(
            outcomes = project.stats.map { stat ->
                ProjectOutcome(value = stat.value, label = stat.label)
            },
        )
    }
}
