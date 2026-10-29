package dev.tonholo.portfolio.metadata

import kotlinx.browser.document
import org.w3c.dom.HTMLHeadElement
import org.w3c.dom.HTMLMetaElement

/**
 * Metadata configuration for HTML `<head>` elements managed by [dev.tonholo.portfolio.components.layouts.PageLayout].
 *
 * @property title optional document title applied to `<title>`.
 * @property ogTitle optional Open Graph title, falls back to [title].
 * @property description optional page description applied to `meta[name='description']`, `og:description`,
 *   and `twitter:description`.
 * @property canonicalUrl optional canonical URL applied to `og:url`.
 * @property imageUrl optional image URL applied to `og:image` and `twitter:image`.
 * @property type Open Graph object type, defaults to [PageMetadata.TYPE_WEBSITE].
 */
data class PageMetadata(
    val title: String? = null,
    val ogTitle: String? = null,
    val description: String? = null,
    val canonicalUrl: String? = null,
    val imageUrl: String? = null,
    val type: String = TYPE_WEBSITE,
) {
    companion object {
        const val TYPE_WEBSITE: String = "website"
        const val TYPE_ARTICLE: String = "article"
        const val DEFAULT_DESCRIPTION: String = "Rafael Tonholo - Android Engineer"
        const val DEFAULT_IMAGE_URL: String = "https://rafael.tonholo.dev/logo512.png"
        const val SITE_NAME: String = "Rafael Tonholo"
        const val TWITTER_SITE: String = "@rafaeltonholo"
        const val TWITTER_CARD_LARGE: String = "summary_large_image"
        const val TWITTER_CARD_SUMMARY: String = "summary"
    }
}

/**
 * Updates or creates a `<meta>` tag with [attributeName]=[attributeValue] and [content].
 *
 * @param attributeName the attribute name (e.g. "name" or "property").
 * @param attributeValue the attribute value (e.g. "description" or "og:title").
 * @param content the text content to populate in the `content` attribute.
 */
fun updateMetaTag(attributeName: String, attributeValue: String, content: String) {
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
    val resolvedImage: String = metadata.imageUrl?.takeIf { it.isNotBlank() }
        ?: PageMetadata.DEFAULT_IMAGE_URL
    document.title = resolvedTitle
    updateMetaTagByName(name = "description", content = resolvedDescription)
    updateMetaTagByProperty(property = "og:title", content = resolvedOgTitle)
    updateMetaTagByProperty(property = "og:description", content = resolvedDescription)
    updateMetaTagByProperty(property = "og:type", content = metadata.type)
    updateMetaTagByProperty(property = "og:site_name", content = PageMetadata.SITE_NAME)
    updateMetaTagByProperty(property = "og:image", content = resolvedImage)
    metadata.canonicalUrl?.takeIf { it.isNotBlank() }?.let { url: String ->
        updateMetaTagByProperty(property = "og:url", content = url)
    }
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
