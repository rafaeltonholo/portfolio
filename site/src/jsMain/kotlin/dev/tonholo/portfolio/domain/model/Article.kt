package dev.tonholo.portfolio.domain.model

/**
 * A writing entry listed on the `/articles` page and rendered on the article
 * detail route.
 */
data class Article(
    val slug: String,
    val date: String,
    val title: String,
    val description: String,
    val tags: List<String>,
    val readTime: String,
    val status: ArticleStatus = ArticleStatus.Published,
)

enum class ArticleStatus { Draft, Published }
