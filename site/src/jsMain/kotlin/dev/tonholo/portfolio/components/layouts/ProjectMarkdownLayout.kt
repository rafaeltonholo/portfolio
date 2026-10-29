package dev.tonholo.portfolio.components.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.varabyte.kobweb.core.PageContext
import com.varabyte.kobweb.core.data.add
import com.varabyte.kobweb.core.data.get
import com.varabyte.kobweb.core.init.InitRoute
import com.varabyte.kobweb.core.init.InitRouteContext
import com.varabyte.kobweb.core.layout.Layout
import com.varabyte.kobwebx.markdown.markdown
import dev.tonholo.portfolio.components.molecules.ProjectBreadcrumb
import dev.tonholo.portfolio.components.molecules.ProjectCover
import dev.tonholo.portfolio.components.organisms.projects.ProjectDetailHero
import dev.tonholo.portfolio.extensions.toKebabCase
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.locale.Locale
import dev.tonholo.portfolio.metadata.PageMetadata
import dev.tonholo.portfolio.metadata.toPageMetadata
import dev.tonholo.portfolio.navigation.Routes
import dev.tonholo.portfolio.state.LocalLocaleState
import dev.tonholo.portfolio.state.StaticContent
import org.jetbrains.compose.web.dom.Text

/**
 * `@InitRoute` hook for markdown-authored project detail pages. Reads the
 * `slug` and `locale` frontmatter keys so [ProjectMarkdownLayout] can look
 * the project up in [StaticContent] under the right locale. Falls back to
 * the markdown file stem (kebab-cased by the file path, locale suffix
 * stripped) when the frontmatter omits them.
 */
@InitRoute
fun initProjectMarkdownLayout(ctx: InitRouteContext) {
    val frontMatter = ctx.markdown?.frontMatter
    val path = ctx.markdown?.path.orEmpty()
    val fallbackSlug = path
        .substringAfterLast('/')
        .removeSuffix(".md")
        .toKebabCase()
    val localeTag = frontMatter?.get("locale")?.singleOrNull()
    val locale = Locale.fromTagOrDefault(tag = localeTag)
    val langSuffix = locale.pathSegment?.let { "-$it" }.orEmpty()
    val strippedFallbackSlug = if (langSuffix.isNotEmpty() && fallbackSlug.endsWith(langSuffix)) {
        fallbackSlug.removeSuffix(langSuffix)
    } else {
        fallbackSlug
    }
    val slug = frontMatter?.get("slug")?.singleOrNull()?.takeIf { it.isNotBlank() }
        ?: strippedFallbackSlug
    val coverImage = frontMatter?.get("coverImage")?.singleOrNull()
        ?: frontMatter?.get("heroImage")?.singleOrNull()
    ctx.data.add(ProjectMarkdownLayoutData(slug = slug, locale = locale, coverImage = coverImage))
}

/**
 * Layout for markdown-authored project detail pages. Wraps [ProjectLayout] and
 * renders the chrome shared across every case study (breadcrumb, hero, cover
 * placeholder) before handing the rest of the page off to the markdown body.
 *
 * Frontmatter requirement: `slug` matching an entry in [StaticContent.projectsFor];
 * optional `locale` (BCP-47) to select the PT-BR variant of the case study.
 * Missing slugs render the shared not-found copy.
 *
 * @param context the active [PageContext]; used to look up the typed route data.
 * @param content the markdown body rendered below the cover placeholder.
 */
@Composable
@Layout
@Suppress("ModifierMissing")
fun ProjectMarkdownLayout(context: PageContext, content: @Composable () -> Unit) {
    val allStrings = LocalStrings.current
    val strings = allStrings.projectDetail
    val data = context.data.get<ProjectMarkdownLayoutData>()
        ?: ProjectMarkdownLayoutData(slug = "", locale = Locale.EnCa)
    val localeState = LocalLocaleState.current
    LaunchedEffect(data.locale) {
        if (localeState.current != data.locale) {
            localeState.set(data.locale)
        }
    }
    val projects = StaticContent.projectsFor(locale = data.locale)
    val index = projects.indexOfFirst { it.slug == data.slug }
    val project = projects.getOrNull(index = index)
    val nextProject = projects.getOrNull(index = index + 1)
    if (project == null) {
        ProjectLayout(
            backRoute = Routes.PROJECTS,
            nextProjectName = null,
            nextRoute = null,
            metadata = PageMetadata(title = allStrings.pageTitles.projects),
        ) {
            Text(strings.notFoundCopy)
        }
        return
    }
    val coverImage: String? = data.coverImage ?: project.coverImage
    ProjectLayout(
        backRoute = Routes.PROJECTS,
        nextProjectName = nextProject?.name,
        nextRoute = nextProject?.let { Routes.project(slug = it.slug, locale = data.locale) },
        metadata = project.toPageMetadata(locale = data.locale, coverImage = coverImage),
    ) {
        ProjectBreadcrumb(projectName = project.name)
        ProjectDetailHero(project = project)
        ProjectCover(
            coverImage = coverImage,
            projectName = project.name,
            placeholderLabel = strings.coverPlaceholderLabel,
        )
        content()
    }
}
