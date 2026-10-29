package dev.tonholo.portfolio.pages.projects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import dev.tonholo.portfolio.components.layouts.ProjectLayout
import dev.tonholo.portfolio.components.molecules.ProjectBreadcrumb
import dev.tonholo.portfolio.components.molecules.ProjectCover
import dev.tonholo.portfolio.components.organisms.projects.OutcomesGrid
import dev.tonholo.portfolio.components.organisms.projects.ProjectDetailHero
import dev.tonholo.portfolio.components.organisms.projects.ProjectOutcome
import dev.tonholo.portfolio.components.organisms.projects.ProjectSection
import dev.tonholo.portfolio.domain.model.Project
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.locale.Locale
import dev.tonholo.portfolio.locale.Strings
import dev.tonholo.portfolio.metadata.PageMetadata
import dev.tonholo.portfolio.navigation.Routes
import dev.tonholo.portfolio.state.LocalLocaleState
import dev.tonholo.portfolio.state.StaticContent
import org.jetbrains.compose.web.dom.Text

private const val PROJECT_SLUG_PARAM = "slug"

/**
 * Dynamic project route "/projects/{slug}" - looks up the requested project
 * in [StaticContent.projectsFor] (English locale) and renders a case-study
 * shell. Projects with a full editorial case study (e.g., Pinterest,
 * Bleacher Report) live as markdown under
 * `src/jsMain/resources/markdown/projects/` and render through
 * [dev.tonholo.portfolio.components.layouts.ProjectMarkdownLayout]; this
 * dynamic route is the fallback that serves every other slug using the
 * project's [Project.description] plus the [OutcomesGrid] derived from
 * [Project.stats].
 *
 * Localized variants live at literal-prefixed routes (e.g.
 * [DynamicPtBrProjectPage] for `/projects/pt-br/{slug}`). Kobweb's route
 * tree only accepts a single dynamic segment name per level, so each
 * non-default locale needs its own `@Page` with a static language segment.
 */
@Page(routeOverride = "/projects/{slug}")
@Composable
@Suppress("ModifierMissing")
fun DynamicProjectPage() {
    val pageContext = rememberPageContext()
    val slug = pageContext.route.params[PROJECT_SLUG_PARAM].orEmpty()
    RenderDynamicProject(slug = slug, locale = Locale.EnCa)
}

/**
 * Pt-BR dynamic project route "/projects/pt-br/{slug}". Kobweb only allows
 * a single dynamic segment name per level, so each non-default locale needs
 * its own page with a literal path segment. Delegates to the shared
 * [RenderDynamicProject] body after pinning the locale to [Locale.PtBr].
 */
@Page(routeOverride = "/projects/pt-br/{slug}")
@Composable
@Suppress("ModifierMissing")
fun DynamicPtBrProjectPage() {
    val pageContext = rememberPageContext()
    val slug = pageContext.route.params[PROJECT_SLUG_PARAM].orEmpty()
    RenderDynamicProject(slug = slug, locale = Locale.PtBr)
}

@Composable
private fun RenderDynamicProject(slug: String, locale: Locale) {
    SyncLocaleToUrl(locale = locale)
    val allStrings = LocalStrings.current
    val strings = allStrings.projectDetail
    val projects: List<Project> = StaticContent.projectsFor(locale = locale)
    val index: Int = projects.indexOfFirst { it.slug == slug }
    val project: Project? = projects.getOrNull(index = index)
    val nextProject: Project? = projects.getOrNull(index = index + 1)
    if (project == null) {
        ProjectLayout(
            backRoute = Routes.PROJECTS,
            nextProjectName = null,
            nextRoute = null,
            title = allStrings.pageTitles.projects,
        ) {
            Text(strings.notFoundCopy)
        }
        return
    }
    val projectTitle: String = "${project.name} | Rafael Tonholo"
    val projectCanonicalUrl: String =
        "https://rafael.tonholo.dev${Routes.project(slug = project.slug, locale = locale)}"
    val projectMetadata: PageMetadata = PageMetadata(
        title = projectTitle,
        ogTitle = project.name,
        description = project.description,
        canonicalUrl = projectCanonicalUrl,
        imageUrl = project.coverImage?.ifBlank { null },
    )
    ProjectLayout(
        backRoute = Routes.PROJECTS,
        nextProjectName = nextProject?.name,
        nextRoute = nextProject?.let { Routes.project(slug = it.slug, locale = locale) },
        metadata = projectMetadata,
    ) {
        ProjectBreadcrumb(projectName = project.name)
        ProjectDetailHero(project = project)
        ProjectCover(
            coverImage = project.coverImage,
            projectName = project.name,
            placeholderLabel = strings.coverPlaceholderLabel,
        )
        DefaultProjectSections(project = project, strings = strings)
    }
}

@Composable
private fun SyncLocaleToUrl(locale: Locale) {
    val localeState = LocalLocaleState.current
    LaunchedEffect(locale) {
        if (localeState.current != locale) {
            localeState.set(locale)
        }
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
