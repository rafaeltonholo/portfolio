package dev.tonholo.portfolio.metadata

import kotlin.js.json

private const val SCHEMA_CONTEXT = "https://schema.org"
private const val BLOG_POSTING = "BlogPosting"
private const val PERSON = "Person"

/**
 * The facts about an article that go into its schema.org `BlogPosting` JSON-LD.
 *
 * @property headline the article title.
 * @property description the article summary.
 * @property url the canonical article URL.
 * @property publishedDate ISO 8601 publish date, or blank when unknown; a blank date is left out.
 * @property language the BCP 47 language tag of this version.
 * @property keywords the article tags.
 * @property imageUrl an image that represents the article, or null; site logos don't qualify, so pass null instead.
 */
data class ArticleStructuredData(
    val headline: String,
    val description: String,
    val url: String,
    val publishedDate: String,
    val language: String,
    val keywords: List<String> = emptyList(),
    val imageUrl: String? = null,
)

/** Serializes this article as schema.org `BlogPosting` JSON-LD, with the site owner as author. */
fun ArticleStructuredData.toJsonLd(): String {
    val data = json(
        "@context" to SCHEMA_CONTEXT,
        "@type" to BLOG_POSTING,
        "headline" to headline,
        "description" to description,
        "url" to url,
        "mainEntityOfPage" to url,
        "inLanguage" to language,
        "author" to json(
            "@type" to PERSON,
            "name" to PageMetadata.SITE_NAME,
            "url" to PageMetadata.AUTHOR_URL,
        ),
    )
    publishedDate.takeIf { it.isNotBlank() }?.let { data["datePublished"] = it }
    keywords.takeIf { it.isNotEmpty() }?.let { data["keywords"] = it.joinToString(separator = ", ") }
    imageUrl?.takeIf { it.isNotBlank() }?.let { data["image"] = it }
    return JSON.stringify(data)
}
