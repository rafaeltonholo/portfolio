package dev.tonholo.portfolio.metadata

import dev.tonholo.portfolio.domain.model.Project
import dev.tonholo.portfolio.locale.Locale
import dev.tonholo.portfolio.navigation.Routes

private const val PROJECT_TITLE_SUFFIX: String = "Rafael Tonholo"

/**
 * Builds the [PageMetadata] for a project detail page.
 *
 * @param locale the locale the project page is rendered in.
 * @param coverImage optional cover image overriding [Project.coverImage].
 */
fun Project.toPageMetadata(locale: Locale, coverImage: String? = this.coverImage): PageMetadata =
    PageMetadata(
        title = "$name | $PROJECT_TITLE_SUFFIX",
        ogTitle = name,
        description = description,
        canonicalUrl = "${PageMetadata.SITE_URL}${Routes.project(slug = slug, locale = locale)}",
        imageUrl = coverImage?.ifBlank { null },
        locale = locale.toOgLocale(),
    )
