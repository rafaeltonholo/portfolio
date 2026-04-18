package dev.tonholo.portfolio.navigation

/**
 * Type-safe route constants for every page reachable from the UI, and helpers
 * for building dynamic article and project URLs.
 */
object Routes {
    const val HOME: String = "/"
    const val ABOUT: String = "/about"
    const val PROJECTS: String = "/projects"
    const val ARTICLES: String = "/articles"
    const val RESUME: String = "/resume"

    fun article(slug: String): String = "$ARTICLES/$slug"

    fun project(slug: String): String = "$PROJECTS/$slug"
}
