package dev.tonholo.portfolio.buildlogic

import dev.tonholo.portfolio.buildlogic.markdown.MarkdownArticlesProcessor
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
     * Finds the route of every markdown page in [directory], sorted like [findMarkdownSlugs]. A page's `routeOverride`
     * frontmatter wins; otherwise the route is `/<section>/<slug>`, or `/<section>/<lang>/<slug>` for a translation
     * such as `ArticlePtBr.md`.
     */
    fun findMarkdownRoutes(directory: File, section: String): List<String> =
        markdownFiles(directory).map { file: File ->
            routeOverrideOf(file) ?: defaultRoute(section = section, kebabSlug = camelCaseToKebabCase(file.nameWithoutExtension))
        }

    /**
     * Finds and formats all markdown file slugs in a directory.
     */
    fun findMarkdownSlugs(directory: File): List<String> =
        markdownFiles(directory).map { camelCaseToKebabCase(it.nameWithoutExtension) }

    private fun markdownFiles(directory: File): List<File> =
        directory.takeIf { it.exists() }
            ?.listFiles { file: File -> file.isFile && file.extension == "md" }
            ?.sortedBy { camelCaseToKebabCase(it.nameWithoutExtension) }
            .orEmpty()

    private fun routeOverrideOf(file: File): String? =
        MarkdownArticlesProcessor.extractScalar("routeOverride", frontMatterLinesOf(file.readText()))
            ?.takeIf { it.isNotBlank() }

    private fun defaultRoute(section: String, kebabSlug: String): String {
        val (canonical: String, lang: String?) = splitLocalizedSlug(kebabSlug)
        return if (lang == null) "/$section/$canonical" else "/$section/$lang/$canonical"
    }
}

/** Returns the lines of the `---` frontmatter block at the top of a markdown [text], or an empty list. */
fun frontMatterLinesOf(text: String): List<String> {
    if (!text.startsWith("---")) return emptyList()
    val closingMarker: Int = text.indexOf("---", startIndex = 3)
    return if (closingMarker == -1) emptyList() else text.substring(3, closingMarker).lines()
}

/** Extension wrapper for [SlugUtils.camelCaseToKebabCase]. */
fun String.camelCaseToKebabCase(): String = SlugUtils.camelCaseToKebabCase(this)

/** Extension wrapper for [SlugUtils.splitLocalizedSlug]. */
fun String.splitLocalizedSlug(): Pair<String, String?> = SlugUtils.splitLocalizedSlug(this)
