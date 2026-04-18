package dev.tonholo.portfolio.extensions

/**
 * Converts a [String] into `kebab-case` by inserting a dash between any
 * lower-to-upper case boundary and by collapsing any run of whitespace into
 * a single dash. The result is always lowercased.
 *
 * Examples:
 * - `"ResumePage".toKebabCase() == "resume-page"`
 * - `"Bleacher Report".toKebabCase() == "bleacher-report"`
 * - `"Pinterest".toKebabCase() == "pinterest"`
 */
fun String.toKebabCase(): String = this
    .replace(Regex("([a-z0-9])([A-Z])"), "$1-$2")
    .replace(Regex("\\s+"), "-")
    .lowercase()
