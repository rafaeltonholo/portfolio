package dev.tonholo.portfolio.locale

/**
 * The two locales supported by the portfolio.
 *
 * [tag] maps to the BCP-47 tag written on `<html lang>` and persisted under
 * the `rt_lang` localStorage key.
 */
enum class Locale(val tag: String) {
    EnCa(tag = "en-CA"),
    PtBr(tag = "pt-BR"),
    ;

    companion object {
        /**
         * Returns the [Locale] matching [tag], or [EnCa] when [tag] is null
         * or unknown.
         */
        fun fromTagOrDefault(tag: String?): Locale = entries.firstOrNull { it.tag == tag } ?: EnCa
    }
}
