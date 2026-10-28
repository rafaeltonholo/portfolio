package dev.tonholo.portfolio.buildlogic

import java.io.File

/**
 * Utility functions for converting Kotlin class/file names to URL slugs and handling
 * localized route suffixes.
 */
object SlugUtils {
    val LOCALIZED_PATH_SEGMENTS: List<String> = listOf("pt-br")

    /**
     * Splits a TitleCamelCase string into lower-case words. Acronym-aware.
     */
    fun splitCamelCase(input: String): List<String> {
        val currWord = StringBuilder()
        val words: MutableList<String> = mutableListOf()
        var lastIsUpper = false
        input.forEachIndexed { i: Int, c: Char ->
            val isUpper: Boolean = c.isUpperCase()
            if (isUpper) {
                if (currWord.isNotEmpty() && (!lastIsUpper || (i < input.lastIndex && input[i + 1].isLowerCase()))) {
                    words.add(currWord.toString())
                    currWord.clear()
                }
            }
            currWord.append(c.lowercase())
            lastIsUpper = isUpper
        }
        if (currWord.isNotEmpty()) words.add(currWord.toString())
        return words
    }

    /**
     * Converts a TitleCamelCase string to kebab-case.
     */
    fun camelCaseToKebabCase(input: String): String =
        splitCamelCase(input).joinToString("-")

    /**
     * Splits a kebab-cased slug into its canonical slug and optional locale segment.
     */
    fun splitLocalizedSlug(
        slug: String,
        localizedSegments: List<String> = LOCALIZED_PATH_SEGMENTS,
    ): Pair<String, String?> {
        for (lang: String in localizedSegments) {
            val suffix = "-$lang"
            if (slug.endsWith(suffix)) {
                return slug.removeSuffix(suffix) to lang
            }
        }
        return slug to null
    }

    /**
     * Finds and formats all markdown file slugs in a directory.
     */
    fun findMarkdownSlugs(directory: File): List<String> =
        directory.takeIf { it.exists() }
            ?.listFiles { file: File -> file.isFile && file.extension == "md" }
            ?.map { camelCaseToKebabCase(it.nameWithoutExtension) }
            ?.sorted()
            .orEmpty()
}

/** Extension wrapper for [SlugUtils.camelCaseToKebabCase]. */
fun String.camelCaseToKebabCase(): String = SlugUtils.camelCaseToKebabCase(this)

/** Extension wrapper for [SlugUtils.splitLocalizedSlug]. */
fun String.splitLocalizedSlug(): Pair<String, String?> = SlugUtils.splitLocalizedSlug(this)
