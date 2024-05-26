package dev.tonholo.portfolio.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.navigation.Route
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.core.analytics.LocalAnalyticsManager
import dev.tonholo.portfolio.core.analytics.events.AnalyticEvent
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.extensions.padding
import dev.tonholo.portfolio.core.foundation.layout.Scaffold
import dev.tonholo.portfolio.core.router.About
import dev.tonholo.portfolio.core.router.AppRoutes
import dev.tonholo.portfolio.core.router.Article
import dev.tonholo.portfolio.core.router.Home
import dev.tonholo.portfolio.core.router.Resume
import dev.tonholo.portfolio.core.sections.AppBar
import dev.tonholo.portfolio.core.sections.Footer
import dev.tonholo.portfolio.core.ui.theme.LocalLyricist
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.unit.dp
import dev.tonholo.portfolio.features.articles.components.ArticleCard
import dev.tonholo.portfolio.locale.Locale
import dev.tonholo.portfolio.locale.localStorageKey
import kotlinx.browser.localStorage
import kotlinx.browser.window

val ArticlePageStyle = CssStyle {
    base {
        Modifier
            .fillMaxSize()
            .gap(24.dp)
    }
}

@Page(AppRoutes.Articles.ROUTE)
@Composable
fun ArticlesPage() {
    val lyricist = LocalLyricist.current
    val analytics = LocalAnalyticsManager.current
    LaunchedEffect(Unit) {
        analytics.track(AnalyticEvent.PageView(language = lyricist.languageTag))
    }
    val context = rememberPageContext()
    Scaffold(
        topBar = {
            AppBar(
                selectedLanguage = lyricist.languageTag,
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
                onResumeClick = {
                    context.router.navigateTo(Route.Resume)
                },
            )
        },
        bottomBar = {
            Footer(
                modifier = Modifier.fillMaxWidth(),
            )
        },
        pageTitle = "${lyricist.strings.pages.article.title} | Rafael Tonholo",
    ) { paddingValues ->
        Column(modifier = ArticlePageStyle.toModifier().padding(paddingValues)) {
            Text(
                text = lyricist.strings.pages.article.title,
                style = Theme.typography.headlineLarge
            )

            lyricist.strings.articles.forEach { (key, value) ->
                val metadata = value.metadata ?: return@forEach
                ArticleCard(
                    title = metadata.title,
                    shortDescription = metadata.description.orEmpty(),
                    onClick = {
                        val url = Route.Article(lyricist.languageTag, key)
                        analytics.track(
                            AnalyticEvent.ReadArticle(
                                articleTitle = metadata.title,
                                articleUrl = "${window.location.origin}${url}",
                            )
                        )
                        context.router.navigateTo(url)
                    },
                    thumbnail = metadata.postThumbnail?.value,
                )
            }
        }
    }
}
