package dev.tonholo.portfolio.navigation

import dev.tonholo.portfolio.locale.Locale

/**
 * Type-safe route constants for every page reachable from the UI, and helpers
 * for building dynamic article and project URLs. Localized article and
 * project URLs follow the pattern `/<section>/<lang>/<slug>`, where the
 * language segment is omitted for the default locale ([Locale.EnCa]).
 */
object Routes {
    const val HOME: String = "/"
    const val ABOUT: String = "/about"
    const val PROJECTS: String = "/projects"
    const val ARTICLES: String = "/articles"
    const val RESUME: String = "/resume"

    /**
     * Builds the canonical article URL for [slug] under [locale].
     * The default locale stays at `/articles/<slug>`; every other locale
     * is prefixed with its [Locale.pathSegment].
     */
    fun article(slug: String, locale: Locale = Locale.EnCa): String =
        buildLocalizedPath(section = ARTICLES, slug = slug, locale = locale)

    /**
     * Builds the canonical project URL for [slug] under [locale].
     * The default locale stays at `/projects/<slug>`; every other locale
     * is prefixed with its [Locale.pathSegment].
     */
    fun project(slug: String, locale: Locale = Locale.EnCa): String =
        buildLocalizedPath(section = PROJECTS, slug = slug, locale = locale)

    /**
     * Parses [path] into a [LocalizedSlug] when it matches the article or
     * project patterns (`/<section>/<slug>` or `/<section>/<lang>/<slug>`).
     * Returns `null` for any other URL so callers can skip navigation.
     */
    fun parseLocalizedSlug(path: String): LocalizedSlug? {
        val clean = path.substringBefore('?').substringBefore('#').trim().trimEnd('/')
        val segments = clean.split('/').filter { it.isNotEmpty() }
        if (segments.size < 2) return null
        val section = "/${segments[0]}"
        if (section != ARTICLES && section != PROJECTS) return null
        return when (segments.size) {
            2 -> LocalizedSlug(section = section, locale = Locale.EnCa, slug = segments[1])

            LOCALIZED_SEGMENT_COUNT -> {
                val locale = Locale.fromPathSegmentOrDefault(segments[1])
                if (locale == Locale.EnCa) return null
                LocalizedSlug(section = section, locale = locale, slug = segments[2])
            }

            else -> null
        }
    }

    private fun buildLocalizedPath(section: String, slug: String, locale: Locale): String {
        val pathSegment = locale.pathSegment
        return if (pathSegment.isNullOrBlank()) {
            "$section/$slug"
        } else {
            "$section/$pathSegment/$slug"
        }
    }

    private const val LOCALIZED_SEGMENT_COUNT = 3
}

/**
 * A parsed article/project URL with its canonical [slug], the [section] it
 * belongs to (`/articles` or `/projects`), and the active [locale].
 */
data class LocalizedSlug(val section: String, val locale: Locale, val slug: String)
