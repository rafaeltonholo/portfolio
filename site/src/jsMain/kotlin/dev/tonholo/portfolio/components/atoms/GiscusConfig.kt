package dev.tonholo.portfolio.components.atoms

import org.w3c.dom.url.URLSearchParams

/**
 * Configuration and evaluation rules for Giscus widget integration.
 */
internal object GiscusConfig {
    const val ORIGIN: String = "https://giscus.app"
    const val SCRIPT_SRC: String = "https://giscus.app/client.js"
    const val REPO: String = "rafaeltonholo/portfolio"
    const val REPO_ID: String = "R_kgDOL0CWLw"
    const val CATEGORY: String = "Show and tell"
    const val CATEGORY_ID: String = "DIC_kwDOL0CWL84DJluE"
    const val THEME_DARK: String = "transparent_dark"
    const val THEME_LIGHT: String = "light"
    const val EXPORT_QUERY_FLAG: String = "_kobwebIsExporting=true"

    /**
     * Id of the element that hosts the widget. Giscus adds it as the `#` part of the address it returns readers to
     * after they sign in with GitHub, so the page opens at the comments and not at the top.
     */
    const val CONTAINER_ID: String = "comments"

    /** How long after a sign-in return the page keeps the comments in view while the article above it loads. */
    const val SIGN_IN_SCROLL_WINDOW_MS: Double = 5_000.0

    private const val SESSION_QUERY_PARAM: String = "giscus"

    /**
     * Determines whether Giscus should be injected based on the current search query string.
     *
     * @param search window location search string.
     * @return true if Giscus should run, false during static site export.
     */
    fun shouldInject(search: String): Boolean = !search.contains(EXPORT_QUERY_FLAG)

    /**
     * Tells whether the reader just came back from signing in with GitHub, which Giscus marks with a `giscus` query
     * parameter holding the new session.
     *
     * @param search window location search string.
     */
    fun isSignInReturn(search: String): Boolean =
        URLSearchParams(search).get(SESSION_QUERY_PARAM) != null

    /**
     * Identifies whether an existing iframe was baked during static export or points to localhost.
     *
     * @param src the src attribute of the iframe.
     * @return true if the iframe contains localhost or export flags.
     */
    fun isContaminatedIframe(src: String): Boolean =
        src.contains("localhost") || src.contains("_kobwebIsExporting")
}
