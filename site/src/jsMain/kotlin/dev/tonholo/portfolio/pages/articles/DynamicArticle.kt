package dev.tonholo.portfolio.pages.articles

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import dev.tonholo.portfolio.components.layouts.ArticleLayout
import dev.tonholo.portfolio.components.organisms.articles.ArticleBody
import dev.tonholo.portfolio.navigation.Routes
import dev.tonholo.portfolio.state.StaticContent
import org.jetbrains.compose.web.dom.Text

private const val ARTICLE_SLUG_PARAM = "slug"

// Not-found fallback stays in English for v1; it is only shown when a
// bookmarked or mistyped slug is visited, which is rare enough that deferring
// the translation is fine.
private const val NOT_FOUND_COPY = "Article not found"

/**
 * Dynamic article route "/articles/{slug}" - looks up the requested article
 * in [StaticContent.ARTICLES] and renders it inside an [ArticleLayout]. When
 * the slug does not match any seeded article the layout still renders so the
 * chrome stays intact while surfacing the missing-article copy.
 */
@Page(routeOverride = "/articles/{slug}")
@Composable
@Suppress("ModifierMissing")
fun DynamicArticlePage() {
    val pageContext = rememberPageContext()
    val slug = pageContext.route.params[ARTICLE_SLUG_PARAM].orEmpty()
    val article = StaticContent.ARTICLES.firstOrNull { it.slug == slug }
    if (article == null) {
        ArticleLayout {
            Text(NOT_FOUND_COPY)
        }
        return
    }
    ArticleLayout {
        ArticleBody(
            title = article.title,
            dateLabel = article.date,
            readTime = article.readTime,
            lede = article.description,
            onBack = {
                pageContext.router.tryRoutingTo(pathQueryAndFragment = Routes.ARTICLES)
            },
        )
    }
}
