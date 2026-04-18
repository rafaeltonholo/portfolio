package dev.tonholo.portfolio.pages

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import dev.tonholo.portfolio.components.layouts.PageLayout
import dev.tonholo.portfolio.components.organisms.articles.ArticleList
import dev.tonholo.portfolio.components.organisms.articles.ArticlesHeader
import dev.tonholo.portfolio.navigation.Routes
import dev.tonholo.portfolio.state.StaticContent

/**
 * Articles route "/articles" - renders the editorial writing header followed
 * by the full article list. Row clicks route to each article's detail page.
 *
 * The `routeOverride` collapses Kobweb's default `ArticlesPage` ->
 * `/articles-page` path to the expected `/articles` slug.
 */
@Page(routeOverride = "/articles")
@Composable
@Suppress("ModifierMissing")
fun ArticlesPage() {
    val router = rememberPageContext().router
    val articles = StaticContent.ARTICLES
    PageLayout {
        ArticlesHeader(articleCount = articles.size)
        ArticleList(
            articles = articles,
            onArticleClick = { slug ->
                router.tryRoutingTo(pathQueryAndFragment = Routes.article(slug = slug))
            },
        )
    }
}
