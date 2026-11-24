package dev.tonholo.portfolio.domain.model

/**
 * An author attribution for an article.
 *
 * @property name display name of the author.
 * @property avatar optional URL pointing to the author's avatar image.
 */
data class ArticleAuthor(val name: String, val avatar: String = "")

/**
 * A person thanked at the end of an article for reviewing it.
 *
 * @property name display name of the reviewer.
 * @property link optional URL to the reviewer's profile, such as GitHub or LinkedIn.
 */
data class ArticleReviewer(val name: String, val link: String = "")

/**
 * A writing entry listed on the `/articles` page and rendered on the article
 * detail route.
 *
 * @property slug shared id that pairs an article with its translations.
 * @property pathSlug the slug this version uses in its URL; a translation can use its own words.
 */
data class Article(
    val slug: String,
    val pathSlug: String = slug,
    val date: String,
    val title: String,
    val description: String,
    val tags: List<String>,
    val readTime: String,
    val status: ArticleStatus = ArticleStatus.Published,
    val summary: String = "",
    val authors: List<ArticleAuthor> = emptyList(),
    val reviewers: List<ArticleReviewer> = emptyList(),
    val thumbnail: String = "",
    val publishedDate: String = "",
)

enum class ArticleStatus { Draft, Published }
