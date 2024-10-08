package dev.tonholo.portfolio.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.navigation.Route
import dev.tonholo.portfolio.core.analytics.LocalAnalyticsManager
import dev.tonholo.portfolio.core.analytics.events.AnalyticEvent
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.router.About
import dev.tonholo.portfolio.core.router.AppRoutes
import dev.tonholo.portfolio.core.router.Articles
import dev.tonholo.portfolio.core.router.Home
import dev.tonholo.portfolio.core.router.Resume
import dev.tonholo.portfolio.core.ui.theme.LocalLyricist
import dev.tonholo.portfolio.features.about.AboutContent
import dev.tonholo.portfolio.features.projects.ProjectContent
import dev.tonholo.portfolio.locale.Locale
import dev.tonholo.portfolio.locale.localStorageKey
import dev.tonholo.portfolio.resources.Projects
import kotlinx.browser.localStorage

@Page(AppRoutes.Project.ROUTE)
@Composable
fun ProjectPage() {
    val lyricist = LocalLyricist.current
    val analytics = LocalAnalyticsManager.current
    LaunchedEffect(Unit) {
        analytics.track(AnalyticEvent.PageView(language = lyricist.languageTag))
    }
    val context = rememberPageContext()
    val projectKey = context.route.params.getValue(AppRoutes.Project.PROJECT_KEY_PARAM)
    val project = remember(projectKey, lyricist.languageTag) {
        Projects.findProject(lyricist.languageTag, projectKey)
    }
    if (project == null) {
        // redirect to 404
        Text("404.")
        return
    }
    ProjectContent(
        strings = lyricist.strings.pages.project,
        project = project,
        selectedLanguage = lyricist.languageTag,
        modifier = Modifier.fillMaxSize(),
        onLocaleChange = { languageTag ->
            lyricist.languageTag = languageTag
            localStorage.setItem(Locale.localStorageKey, languageTag)
        },
        onHomeClick = {
            context.router.navigateTo(Route.Home)
        },
        onAboutClick = {
            context.router.navigateTo(Route.About)
        },
        onArticleClick = {
            context.router.navigateTo(Route.Articles)
        },
    )
}
