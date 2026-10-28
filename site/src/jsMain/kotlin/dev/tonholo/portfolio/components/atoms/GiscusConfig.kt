package dev.tonholo.portfolio.components.atoms

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
     * Determines whether Giscus should be injected based on the current search query string.
     *
     * @param search window location search string.
     * @return true if Giscus should run, false during static site export.
     */
    fun shouldInject(search: String): Boolean = !search.contains(EXPORT_QUERY_FLAG)

    /**
     * Identifies whether an existing iframe was baked during static export or points to localhost.
     *
     * @param src the src attribute of the iframe.
     * @return true if the iframe contains localhost or export flags.
     */
    fun isContaminatedIframe(src: String): Boolean =
        src.contains("localhost") || src.contains("_kobwebIsExporting")
}
