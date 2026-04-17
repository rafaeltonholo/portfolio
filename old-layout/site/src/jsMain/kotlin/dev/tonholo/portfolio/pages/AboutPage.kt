package dev.tonholo.portfolio.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.navigation.Route
import dev.tonholo.portfolio.core.analytics.LocalAnalyticsManager
import dev.tonholo.portfolio.core.analytics.events.AnalyticEvent
import dev.tonholo.portfolio.core.router.AppRoutes
import dev.tonholo.portfolio.core.router.Articles
import dev.tonholo.portfolio.core.router.Home
import dev.tonholo.portfolio.core.router.Resume
import dev.tonholo.portfolio.core.ui.theme.LocalLyricist
import dev.tonholo.portfolio.features.about.AboutContent
import dev.tonholo.portfolio.locale.Locale
import dev.tonholo.portfolio.locale.localStorageKey
import kotlinx.browser.localStorage

@Page(AppRoutes.About.ROUTE)
@Composable
fun AboutPage() {
    val lyricist = LocalLyricist.current
    val analytics = LocalAnalyticsManager.current
    LaunchedEffect(Unit) {
        analytics.track(AnalyticEvent.PageView(language = lyricist.languageTag))
    }
    val context = rememberPageContext()
    val about = lyricist.strings.pages.about
    AboutContent(
        about = about,
        selectedLanguage = lyricist.languageTag,
        modifier = Modifier.fillMaxSize(),
        onLocaleChange = { languageTag ->
            lyricist.languageTag = languageTag
            localStorage.setItem(Locale.localStorageKey, languageTag)
        },
        onHomeClick = {
            context.router.navigateTo(Route.Home)
        },
        onArticleClick = {
            context.router.navigateTo(Route.Articles)
        },
        onResumeClick = {
            context.router.navigateTo(Route.Resume)
        },
    )
}
