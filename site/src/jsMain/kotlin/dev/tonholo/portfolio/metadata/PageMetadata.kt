package dev.tonholo.portfolio.metadata

import dev.tonholo.portfolio.components.layouts.PageLayout
import dev.tonholo.portfolio.locale.Locale
import kotlinx.browser.document
import org.w3c.dom.Element
import org.w3c.dom.HTMLHeadElement
import org.w3c.dom.HTMLLinkElement
import org.w3c.dom.HTMLMetaElement
import org.w3c.dom.HTMLScriptElement
import org.w3c.dom.asList

/**
 * Metadata configuration for HTML `<head>` elements managed by [PageLayout].
 *
 * @property title optional document title applied to `<title>`.
 * @property ogTitle optional Open Graph title, falls back to [title].
 * @property description optional page description applied to `meta[name='description']`, `og:description`,
 *   and `twitter:description`.
 * @property canonicalUrl optional canonical URL applied to `og:url` and `link[rel='canonical']`.
 * @property imageUrl optional image URL applied to `og:image` and `twitter:image`.
 * @property type Open Graph object type, defaults to [PageMetadata.TYPE_WEBSITE].
 * @property locale Open Graph locale applied to `og:locale`, defaults to [PageMetadata.DEFAULT_OG_LOCALE].
 * @property alternates language versions of this page, applied as `link[rel='alternate'][hreflang]`. Every version
 *   must list all versions, itself included.
 * @property article article-only Open Graph tags, or null for other pages.
 * @property structuredData JSON-LD applied as a `script[type='application/ld+json']`, or null for none.
 */
data class PageMetadata(
    val title: String? = null,
    val ogTitle: String? = null,
    val description: String? = null,
    val canonicalUrl: String? = null,
    val imageUrl: String? = null,
    val type: String = TYPE_WEBSITE,
    val locale: String = DEFAULT_OG_LOCALE,
    val alternates: List<AlternateLink> = emptyList(),
    val article: ArticleMetadata? = null,
    val structuredData: String? = null,
) {
    companion object {
        const val TYPE_WEBSITE: String = "website"
        const val TYPE_ARTICLE: String = "article"
        const val DEFAULT_DESCRIPTION: String = "Rafael Tonholo - Android Engineer"
        const val DEFAULT_OG_LOCALE: String = "en_CA"
        const val SITE_URL: String = "https://rafael.tonholo.dev"
        const val DEFAULT_IMAGE_URL: String = "$SITE_URL/logo512.png"
        const val SITE_NAME: String = "Rafael Tonholo"
        const val AUTHOR_URL: String = "$SITE_URL/about"
        const val TWITTER_SITE: String = "@rafaeltonholo"
        const val TWITTER_CARD_LARGE: String = "summary_large_image"
        const val TWITTER_CARD_SUMMARY: String = "summary"
    }
}

/**
 * One language version of a page.
 *
 * @property hreflang the BCP 47 language tag, such as `en-CA` or `pt-BR`.
 * @property href the absolute URL of that version.
 */
data class AlternateLink(val hreflang: String, val href: String)

/**
 * Open Graph tags specific to articles.
 *
 * @property publishedDate ISO 8601 date the article was first published, or blank when unknown.
 * @property authorUrl URL of the author's profile page.
 */
data class ArticleMetadata(
    val publishedDate: String,
    val authorUrl: String = PageMetadata.AUTHOR_URL,
)

/**
 * Updates or creates a `<meta>` tag with [attributeName]=[attributeValue] and [content].
 *
 * @param attributeName the attribute name (e.g. "name" or "property").
 * @param attributeValue the attribute value (e.g. "description" or "og:title").
 * @param content the text content to populate in the `content` attribute.
 */
private fun updateMetaTag(attributeName: String, attributeValue: String, content: String) {
    if (attributeName.isBlank() || attributeValue.isBlank() || content.isBlank()) return
    val head: HTMLHeadElement = document.head ?: return
    val selector: String = "meta[$attributeName='$attributeValue']"
    val existing: HTMLMetaElement? = document.querySelector(selector).unsafeCast<HTMLMetaElement?>()
    if (existing != null) {
        existing.content = content
        return
    }
    val meta: HTMLMetaElement = (document.createElement("meta") as HTMLMetaElement).apply {
        setAttribute(attributeName, attributeValue)
        this.content = content
    }
    head.appendChild(meta)
}

/**
 * Updates or creates a `<meta name="...">` tag in `<head>`.
 *
 * @param name the `name` attribute value.
 * @param content the `content` attribute value.
 */
fun updateMetaTagByName(name: String, content: String): Unit =
    updateMetaTag(attributeName = "name", attributeValue = name, content = content)

/**
 * Updates or creates a `<meta property="...">` tag in `<head>`.
 *
 * @param property the `property` attribute value.
 * @param content the `content` attribute value.
 */
fun updateMetaTagByProperty(property: String, content: String): Unit =
    updateMetaTag(attributeName = "property", attributeValue = property, content = content)

/**
 * Removes the `<meta property="...">` tag from `<head>` when present.
 *
 * @param property the `property` attribute value.
 */
fun removeMetaTagByProperty(property: String) {
    document.querySelector("meta[property='$property']")?.remove()
}

private const val STRUCTURED_DATA_SELECTOR = "script[type='application/ld+json']"
private const val ALTERNATE_SELECTOR = "link[rel='alternate'][hreflang]"
private const val CANONICAL_SELECTOR = "link[rel='canonical']"

private fun applyCanonicalLink(url: String?) {
    val existing: HTMLLinkElement? = document.querySelector(CANONICAL_SELECTOR).unsafeCast<HTMLLinkElement?>()
    if (url == null) {
        existing?.remove()
        return
    }
    val link: HTMLLinkElement = existing ?: (document.createElement("link") as HTMLLinkElement).also {
        it.rel = "canonical"
        document.head?.appendChild(it)
    }
    link.href = url
}

private fun applyAlternateLinks(alternates: List<AlternateLink>) {
    document.querySelectorAll(ALTERNATE_SELECTOR).asList().forEach { (it as Element).remove() }
    alternates.forEach { alternate ->
        val link: HTMLLinkElement = (document.createElement("link") as HTMLLinkElement).apply {
            rel = "alternate"
            hreflang = alternate.hreflang
            href = alternate.href
        }
        document.head?.appendChild(link)
    }
}

private fun applyArticleTags(article: ArticleMetadata?) {
    val publishedDate: String? = article?.publishedDate?.takeIf { it.isNotBlank() }
    if (publishedDate != null) {
        updateMetaTagByProperty(property = "article:published_time", content = publishedDate)
    } else {
        removeMetaTagByProperty(property = "article:published_time")
    }
    if (article != null) {
        updateMetaTagByProperty(property = "article:author", content = article.authorUrl)
    } else {
        removeMetaTagByProperty(property = "article:author")
    }
}

private fun applyStructuredData(json: String?) {
    val existing: HTMLScriptElement? = document.querySelector(STRUCTURED_DATA_SELECTOR).unsafeCast<HTMLScriptElement?>()
    if (json.isNullOrBlank()) {
        existing?.remove()
        return
    }
    val script: HTMLScriptElement = existing ?: (document.createElement("script") as HTMLScriptElement).also {
        it.type = "application/ld+json"
        document.head?.appendChild(it)
    }
    script.textContent = json
}

/** Turns a site-relative path such as `/images/a.png` into an absolute URL on the site's host. */
fun String.toAbsoluteSiteUrl(): String =
    if (startsWith("/")) "${PageMetadata.SITE_URL}$this" else this

/**
 * Converts this [Locale] to the Open Graph locale format (e.g. `pt_BR`).
 */
fun Locale.toOgLocale(): String = tag.replace(oldValue = "-", newValue = "_")

/**
 * Applies [metadata] to the current HTML document `<head>`.
 *
 * @param metadata the [PageMetadata] payload to inject.
 * @param defaultTitle fallback page title when [PageMetadata.title] is null.
 */
fun applyPageMetadata(metadata: PageMetadata, defaultTitle: String) {
    val resolvedTitle: String = metadata.title?.takeIf { it.isNotBlank() } ?: defaultTitle
    val resolvedOgTitle: String = metadata.ogTitle?.takeIf { it.isNotBlank() } ?: resolvedTitle
    val resolvedDescription: String = metadata.description?.takeIf { it.isNotBlank() }
        ?: PageMetadata.DEFAULT_DESCRIPTION
    val resolvedImage: String = metadata.imageUrl
        ?.takeIf { it.isNotBlank() }
        ?.toAbsoluteSiteUrl()
        ?: PageMetadata.DEFAULT_IMAGE_URL
    document.title = resolvedTitle
    updateMetaTagByName(name = "description", content = resolvedDescription)
    updateMetaTagByProperty(property = "og:title", content = resolvedOgTitle)
    updateMetaTagByProperty(property = "og:description", content = resolvedDescription)
    updateMetaTagByProperty(property = "og:type", content = metadata.type)
    updateMetaTagByProperty(property = "og:locale", content = metadata.locale)
    updateMetaTagByProperty(property = "og:site_name", content = PageMetadata.SITE_NAME)
    updateMetaTagByProperty(property = "og:image", content = resolvedImage)
    val canonicalUrl: String? = metadata.canonicalUrl?.takeIf { it.isNotBlank() }
    if (canonicalUrl != null) {
        updateMetaTagByProperty(property = "og:url", content = canonicalUrl)
    } else {
        removeMetaTagByProperty(property = "og:url")
    }
    applyCanonicalLink(url = canonicalUrl)
    applyAlternateLinks(alternates = metadata.alternates)
    applyArticleTags(article = metadata.article)
    applyStructuredData(json = metadata.structuredData)
    val twitterCard: String = if (metadata.imageUrl.isNullOrBlank()) {
        PageMetadata.TWITTER_CARD_SUMMARY
    } else {
        PageMetadata.TWITTER_CARD_LARGE
    }
    updateMetaTagByName(name = "twitter:card", content = twitterCard)
    updateMetaTagByName(name = "twitter:site", content = PageMetadata.TWITTER_SITE)
    updateMetaTagByName(name = "twitter:creator", content = PageMetadata.TWITTER_SITE)
    updateMetaTagByName(name = "twitter:title", content = resolvedOgTitle)
    updateMetaTagByName(name = "twitter:description", content = resolvedDescription)
    updateMetaTagByName(name = "twitter:image", content = resolvedImage)
}
