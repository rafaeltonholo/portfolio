package dev.tonholo.portfolio.locale

/**
 * The two locales supported by the portfolio.
 *
 * [tag] maps to the BCP-47 tag written on `<html lang>` and persisted under
 * the `rt_lang` localStorage key. [pathSegment] is the lower-cased URL
 * fragment used for localized article/project URLs. The default locale
 * ([EnCa]) has a `null` path segment so its URLs stay bare
 * (`/projects/<slug>`), while other locales prefix their variant
 * (`/projects/pt-br/<slug>`).
 */
enum class Locale(val tag: String, val pathSegment: String?) {
    EnCa(tag = "en-CA", pathSegment = null),
    PtBr(tag = "pt-BR", pathSegment = "pt-br"),
    ;

    companion object {
        /**
         * Returns the [Locale] matching [tag], or [EnCa] when [tag] is null
         * or unknown.
         */
        fun fromTagOrDefault(tag: String?): Locale = entries.firstOrNull { it.tag == tag } ?: EnCa

        /**
         * Returns the [Locale] matching [segment] (case-insensitive), or
         * [EnCa] when [segment] is null, blank, or not a known locale path
         * segment.
         */
        fun fromPathSegmentOrDefault(segment: String?): Locale {
            if (segment.isNullOrBlank()) return EnCa
            val normalized = segment.lowercase()
            return entries.firstOrNull { it.pathSegment == normalized } ?: EnCa
        }
    }
}
