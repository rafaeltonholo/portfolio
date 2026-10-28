package dev.tonholo.portfolio.domain.model

/**
 * An author attribution for an article.
 *
 * @property name display name of the author.
 * @property avatar optional URL pointing to the author's avatar image.
 */
data class ArticleAuthor(val name: String, val avatar: String = "")

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
    val summary: String = "",
    val authors: List<ArticleAuthor> = emptyList(),
    val thumbnail: String = "",
)

enum class ArticleStatus { Draft, Published }
