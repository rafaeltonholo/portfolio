package dev.tonholo.portfolio.components.layouts

import dev.tonholo.portfolio.locale.Locale

/**
 * Route-local payload populated by [initProjectMarkdownLayout]. The [slug] is
 * the canonical project slug shared across locales; [locale] is the locale
 * resolved from the markdown frontmatter `locale` key (or the default when
 * the key is absent) and used to keep content, chrome, and prev/next links
 * in sync with the URL. The optional [coverImage] overrides the project default.
 *
 * @property slug Canonical project slug.
 * @property locale Target display locale.
 * @property coverImage Optional cover/hero image URL or asset path.
 */
data class ProjectMarkdownLayoutData(
    val slug: String,
    val locale: Locale,
    val coverImage: String? = null,
)
