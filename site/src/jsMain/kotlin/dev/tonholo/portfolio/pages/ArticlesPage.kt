package dev.tonholo.portfolio.pages

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.core.Page
import dev.tonholo.portfolio.components.layouts.PageLayout
import dev.tonholo.portfolio.components.layouts.SectionContainer
import dev.tonholo.portfolio.components.organisms.articles.ArticleList
import dev.tonholo.portfolio.components.organisms.articles.ArticlesHeader
import dev.tonholo.portfolio.domain.model.Article
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.locale.Locale
import dev.tonholo.portfolio.state.LocalLocaleState
import dev.tonholo.portfolio.state.StaticContent

/**
 * Articles route "/articles" - renders the editorial writing header followed
 * by the full article list. Each row is a real `<a href>` (via [ArticleList])
 * so browser navigation affordances work.
 *
 * The `routeOverride` collapses Kobweb's default `ArticlesPage` ->
 * `/articles-page` path to the expected `/articles` slug.
 */
@Page(routeOverride = "/articles")
@Composable
@Suppress("ModifierMissing")
fun ArticlesPage() {
    val locale: Locale = LocalLocaleState.current.current
    val articles: List<Article> = StaticContent.articlesFor(locale = locale)
    val strings = LocalStrings.current
    PageLayout(title = strings.pageTitles.articles) {
        ArticlesHeader(articleCount = articles.size)
        SectionContainer(id = "articles-list") {
            ArticleList(articles = articles)
        }
    }
}
