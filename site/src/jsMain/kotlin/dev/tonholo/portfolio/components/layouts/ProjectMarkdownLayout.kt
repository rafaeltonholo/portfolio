package dev.tonholo.portfolio.components.layouts

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.core.PageContext
import com.varabyte.kobweb.core.data.add
import com.varabyte.kobweb.core.data.get
import com.varabyte.kobweb.core.init.InitRoute
import com.varabyte.kobweb.core.init.InitRouteContext
import com.varabyte.kobweb.core.layout.Layout
import com.varabyte.kobwebx.markdown.markdown
import dev.tonholo.portfolio.components.atoms.AspectRatio
import dev.tonholo.portfolio.components.atoms.Placeholder
import dev.tonholo.portfolio.components.molecules.ProjectBreadcrumb
import dev.tonholo.portfolio.components.organisms.projects.ProjectDetailHero
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.navigation.Routes
import dev.tonholo.portfolio.state.StaticContent
import dev.tonholo.portfolio.theme.layout.unit.dp
import org.jetbrains.compose.web.dom.Text

// Matches the handoff `.project-cover` chrome used by the dynamic project page:
// 4px radius, 64px bottom margin above the first case-study section.
private val COVER_RADIUS = 4.dp
private val COVER_MARGIN_BOTTOM = 64.dp

/**
 * Route-local payload populated by [initProjectMarkdownLayout]. Only the slug
 * is lifted out of frontmatter; everything else (hero copy, stack, stats) is
 * resolved from [StaticContent.PROJECTS] so the projects index and the detail
 * page share a single source of truth.
 */
data class ProjectMarkdownLayoutData(val slug: String)

/**
 * `@InitRoute` hook for markdown-authored project detail pages. Reads the
 * `slug` frontmatter key so [ProjectMarkdownLayout] can look the project up in
 * [StaticContent.PROJECTS]. Falls back to the markdown file stem (kebab-cased
 * by the file path) if the frontmatter omits it.
 */
@InitRoute
fun initProjectMarkdownLayout(ctx: InitRouteContext) {
    val frontMatter = ctx.markdown?.frontMatter
    val path = ctx.markdown?.path.orEmpty()
    val fallbackSlug = path
        .substringAfterLast('/')
        .removeSuffix(".md")
        .kebab()
    val slug = frontMatter?.get("slug")?.singleOrNull()?.takeIf { it.isNotBlank() } ?: fallbackSlug
    ctx.data.add(ProjectMarkdownLayoutData(slug = slug))
}

private fun String.kebab(): String {
    val words = mutableListOf<String>()
    val curr = StringBuilder()
    var lastIsUpper = false
    forEachIndexed { i, c ->
        val isUpper = c.isUpperCase()
        if (isUpper && curr.isNotEmpty() &&
            (!lastIsUpper || (i < lastIndex && this[i + 1].isLowerCase()))
        ) {
            words += curr.toString()
            curr.clear()
        }
        curr.append(c.lowercase())
        lastIsUpper = isUpper
    }
    if (curr.isNotEmpty()) words += curr.toString()
    return words.joinToString(separator = "-")
}

/**
 * Layout for markdown-authored project detail pages. Wraps [ProjectLayout] and
 * renders the chrome shared across every case study (breadcrumb, hero, cover
 * placeholder) before handing the rest of the page off to the markdown body.
 *
 * Frontmatter requirement: `slug` matching an entry in [StaticContent.PROJECTS].
 * Missing slugs render the shared not-found copy.
 *
 * @param context the active [PageContext]; used to look up the typed route data.
 * @param content the markdown body rendered below the cover placeholder.
 */
@Composable
@Layout
@Suppress("ModifierMissing")
fun ProjectMarkdownLayout(context: PageContext, content: @Composable () -> Unit) {
    val strings = LocalStrings.current.projectDetail
    val data = context.data.get<ProjectMarkdownLayoutData>()
        ?: ProjectMarkdownLayoutData(slug = "")
    val index = StaticContent.PROJECTS.indexOfFirst { it.slug == data.slug }
    val project = StaticContent.PROJECTS.getOrNull(index = index)
    val nextProject = StaticContent.PROJECTS.getOrNull(index = index + 1)

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
        content()
    }
}
