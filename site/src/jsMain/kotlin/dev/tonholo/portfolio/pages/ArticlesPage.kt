package dev.tonholo.portfolio.pages

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.components.layouts.PageLayout
import dev.tonholo.portfolio.components.organisms.articles.ArticleList
import dev.tonholo.portfolio.components.organisms.articles.ArticlesHeader
import dev.tonholo.portfolio.state.StaticContent
import dev.tonholo.portfolio.theme.layout.ShellStyle
import org.jetbrains.compose.web.dom.Div

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
    val articles = StaticContent.ARTICLES
    PageLayout {
        Div(attrs = ShellStyle.toModifier().toAttrs()) {
            ArticlesHeader(articleCount = articles.size)
            ArticleList(articles = articles)
        }
    }
}
