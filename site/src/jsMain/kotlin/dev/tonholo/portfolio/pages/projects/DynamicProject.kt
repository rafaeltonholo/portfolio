package dev.tonholo.portfolio.pages.projects

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import dev.tonholo.portfolio.components.layouts.ProjectLayout
import dev.tonholo.portfolio.components.organisms.projects.OutcomesGrid
import dev.tonholo.portfolio.components.organisms.projects.ProjectDetailHero
import dev.tonholo.portfolio.components.organisms.projects.ProjectOutcome
import dev.tonholo.portfolio.components.organisms.projects.ProjectSection
import dev.tonholo.portfolio.domain.model.Project
import dev.tonholo.portfolio.navigation.Routes
import dev.tonholo.portfolio.state.StaticContent
import org.jetbrains.compose.web.dom.Text

private const val PROJECT_SLUG_PARAM = "slug"
private const val CONTEXT_LABEL = "/ context"
private const val OUTCOMES_LABEL = "/ outcomes"
private const val OUTCOMES_TITLE = "Numbers worth keeping."
private const val NOT_FOUND_COPY = "Project not found"

/**
 * Dynamic project route "/projects/{slug}" - looks up the requested project
 * in [StaticContent.PROJECTS] and renders a case-study shell around its
 * editorial hero, a single context section (v1 uses [Project.description] as
 * body copy), and the numeric outcomes grid. The sticky [ProjectLayout] footer
 * wires back/next navigation using the project's index in the seed list.
 */
@Page(routeOverride = "/projects/{slug}")
@Composable
@Suppress("ModifierMissing")
fun DynamicProjectPage() {
    val pageContext = rememberPageContext()
    val slug = pageContext.route.params[PROJECT_SLUG_PARAM].orEmpty()
    val index = StaticContent.PROJECTS.indexOfFirst { it.slug == slug }
    val project = StaticContent.PROJECTS.getOrNull(index = index)
    val nextProject = StaticContent.PROJECTS.getOrNull(index = index + 1)

    if (project == null) {
        ProjectLayout(
            nextProjectName = null,
            onBack = {
                pageContext.router.tryRoutingTo(pathQueryAndFragment = Routes.HOME)
            },
            onNext = {},
        ) {
            Text(NOT_FOUND_COPY)
        }
        return
    }

    ProjectLayout(
        nextProjectName = nextProject?.name,
        onBack = {
            pageContext.router.tryRoutingTo(pathQueryAndFragment = Routes.HOME)
        },
        onNext = {
            nextProject?.let { target ->
                pageContext.router.tryRoutingTo(
                    pathQueryAndFragment = Routes.project(slug = target.slug),
                )
            }
        },
    ) {
        ProjectDetailHero(project = project)
        ProjectSection(label = CONTEXT_LABEL, title = project.name) {
            Text(project.description)
        }
        ProjectSection(label = OUTCOMES_LABEL, title = OUTCOMES_TITLE) {
            OutcomesGrid(
                outcomes = project.stats.map { stat ->
                    ProjectOutcome(value = stat.value, label = stat.label)
                },
            )
        }
    }
}
