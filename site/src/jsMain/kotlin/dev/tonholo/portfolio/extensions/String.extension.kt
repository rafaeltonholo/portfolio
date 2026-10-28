package dev.tonholo.portfolio.extensions

/**
 * Converts a [String] into `kebab-case` by inserting a dash on word boundaries,
 * including acronyms and consecutive capitals, and collapsing whitespace into dashes.
 *
 * Examples:
 * - `"ResumePage".toKebabCase() == "resume-page"`
 * - `"Bleacher Report".toKebabCase() == "bleacher-report"`
 * - `"Pinterest".toKebabCase() == "pinterest"`
 * - `"LogDHowToComposableText".toKebabCase() == "log-d-how-to-composable-text"`
 * - `"TheDayIFoundMyselfCountingBytes".toKebabCase() == "the-day-i-found-myself-counting-bytes"`
 */
fun String.toKebabCase(): String {
    val words: MutableList<String> = mutableListOf()
    val curr: StringBuilder = StringBuilder()
    var lastIsUpper: Boolean = false
    val sanitized: String = this.trim()
    sanitized.forEachIndexed { i: Int, c: Char ->
        if (c.isWhitespace() || c == '-' || c == '_') {
            if (curr.isNotEmpty()) {
                words += curr.toString()
                curr.clear()
            }
            lastIsUpper = false
            return@forEachIndexed
        }
        val isUpper: Boolean = c.isUpperCase()
        if (isUpper && curr.isNotEmpty() &&
            (!lastIsUpper || (i < sanitized.lastIndex && sanitized[i + 1].isLowerCase()))
        ) {
            words += curr.toString()
            curr.clear()
        }
        curr.append(c.lowercase())
        lastIsUpper = isUpper
    }
    if (curr.isNotEmpty()) {
        words += curr.toString()
    }
    return words.joinToString(separator = "-")
}
