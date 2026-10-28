package dev.tonholo.portfolio.buildlogic.markdown

import com.varabyte.kobwebx.gradle.markdown.MarkdownEntry
import dev.tonholo.portfolio.buildlogic.SlugUtils
import dev.tonholo.portfolio.buildlogic.splitLocalizedSlug
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.Locale
import kotlin.math.ceil

/**
 * Processor that reads markdown files and generates a single source of truth Kotlin
 * object (`GeneratedArticles.kt`) containing all metadata, localized dates, authors,
 * tags, reading times, summaries, and cover thumbnails.
 */
object MarkdownArticlesProcessor {
    private const val READING_SPEED_WORDS_PER_MINUTE: Double = 200.0

    data class AuthorRow(val name: String, val avatar: String)

    data class ArticleRow(
        val slug: String,
        val date: String,
        val title: String,
        val description: String,
        val tags: List<String>,
        val readTime: String,
        val status: String,
        val locale: String,
        val summary: String,
        val authors: List<AuthorRow>,
        val thumbnail: String,
    )

    private val articleDateFormatter: DateTimeFormatter = DateTimeFormatterBuilder()
        .parseCaseInsensitive()
        .appendPattern("MMM d, yyyy")
        .toFormatter(Locale.ENGLISH)

    fun escapeForKotlin(text: String): String = text
        .replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("\n", "\\n")
        .replace("\r", "\\r")
        .replace("\t", "\\t")

    fun toSortableDate(dateString: String): LocalDate = runCatching {
        LocalDate.parse(dateString.substringBefore('T').trim())
    }.recoverCatching {
        LocalDate.parse(dateString.trim(), articleDateFormatter)
    }.getOrElse { LocalDate.MIN }

    fun formatDisplayDate(rawDate: String, locale: String): String {
        val isoDate: LocalDate = runCatching {
            LocalDate.parse(rawDate.substringBefore('T').trim())
        }.getOrNull() ?: return rawDate
        return if (locale == "pt-BR") {
            val ptMonth: String = when (isoDate.monthValue) {
                1 -> "jan."
                2 -> "fev."
                3 -> "mar."
                4 -> "abr."
                5 -> "mai."
                6 -> "jun."
                7 -> "jul."
                8 -> "ago."
                9 -> "set."
                10 -> "out."
                11 -> "nov."
                else -> "dez."
            }
            "${isoDate.dayOfMonth} de $ptMonth de ${isoDate.year}"
        } else {
            val enMonth: String = when (isoDate.monthValue) {
                1 -> "Jan"
                2 -> "Feb"
                3 -> "Mar"
                4 -> "Apr"
                5 -> "May"
                6 -> "Jun"
                7 -> "Jul"
                8 -> "Aug"
                9 -> "Sep"
                10 -> "Oct"
                11 -> "Nov"
                else -> "Dec"
            }
            "$enMonth ${isoDate.dayOfMonth}, ${isoDate.year}"
        }
    }

    fun calculateReadingTime(markdownText: String): String {
        val body: String = if (markdownText.startsWith("---")) {
            val secondMarker: Int = markdownText.indexOf("---", startIndex = 3)
            if (secondMarker != -1) markdownText.substring(secondMarker + 3) else markdownText
        } else {
            markdownText
        }
        val words: Int = body.trim().split(Regex("\\s+")).count { it.isNotBlank() }
        val minutes: Int = maxOf(1, ceil(words / READING_SPEED_WORDS_PER_MINUTE).toInt())
        return "$minutes min"
    }

    fun extractScalar(key: String, frontMatterLines: List<String>): String? {
        val lineIndex: Int = frontMatterLines.indexOfFirst { it.trimStart().startsWith("$key:") }
        if (lineIndex == -1) return null
        val line: String = frontMatterLines[lineIndex]
        val afterColon: String = line.substringAfter("$key:").trim()
        if (afterColon == ">" || afterColon == ">-" || afterColon == "|") {
            val indentedLines: MutableList<String> = mutableListOf()
            for (i: Int in (lineIndex + 1) until frontMatterLines.size) {
                val nextLine: String = frontMatterLines[i]
                if (nextLine.isBlank()) {
                    indentedLines.add("")
                } else if (nextLine.startsWith(" ") || nextLine.startsWith("\t")) {
                    indentedLines.add(nextLine.trim())
                } else {
                    break
                }
            }
            return indentedLines.filter { it.isNotBlank() }.joinToString(" ")
        }
        return afterColon.removeSurrounding("\"").removeSurrounding("'")
    }

    fun extractAuthors(frontMatterLines: List<String>): List<AuthorRow> {
        val authorsIndex: Int = frontMatterLines.indexOfFirst { it.trimStart().startsWith("authors:") }
        if (authorsIndex == -1) return emptyList()
        val authorList: MutableList<AuthorRow> = mutableListOf()
        var currentName = ""
        var currentAvatar = ""
        fun saveCurrent() {
            if (currentName.isNotBlank() || currentAvatar.isNotBlank()) {
                authorList.add(AuthorRow(name = currentName, avatar = currentAvatar))
                currentName = ""
                currentAvatar = ""
            }
        }
        for (i: Int in (authorsIndex + 1) until frontMatterLines.size) {
            val line: String = frontMatterLines[i]
            if (line.isNotBlank() && !line.startsWith(" ") && !line.startsWith("\t") && !line.startsWith("-")) {
                break
            }
            val trimmed: String = line.trim()
            if (trimmed.startsWith("-")) {
                saveCurrent()
                val itemContent: String = trimmed.removePrefix("-").trim()
                if (itemContent.startsWith("name:")) {
                    currentName = itemContent.substringAfter("name:").trim().removeSurrounding("\"").removeSurrounding("'")
                } else if (itemContent.startsWith("avatar:")) {
                    currentAvatar = itemContent.substringAfter("avatar:").trim().removeSurrounding("\"").removeSurrounding("'")
                }
            } else if (trimmed.startsWith("name:")) {
                currentName = trimmed.substringAfter("name:").trim().removeSurrounding("\"").removeSurrounding("'")
            } else if (trimmed.startsWith("avatar:")) {
                currentAvatar = trimmed.substringAfter("avatar:").trim().removeSurrounding("\"").removeSurrounding("'")
            }
        }
        saveCurrent()
        return authorList
    }

    fun generateArticlesObject(entries: List<MarkdownEntry>, markdownResourceDir: File): String {
        val rows: List<ArticleRow> = entries
            .filter { it.filePath.startsWith("articles/") }
            .mapNotNull { entry ->
                val rawSlug: String = SlugUtils.camelCaseToKebabCase(
                    entry.filePath.removePrefix("articles/").removeSuffix(".md"),
                )
                val (canonicalSlug: String, lang: String?) = rawSlug.splitLocalizedSlug()
                val markdownFile = File(markdownResourceDir, entry.filePath)
                val fileText: String = if (markdownFile.exists()) markdownFile.readText() else ""
                val frontMatterLines: List<String> = if (fileText.startsWith("---")) {
                    val secondMarker: Int = fileText.indexOf("---", startIndex = 3)
                    if (secondMarker != -1) fileText.substring(3, secondMarker).lines() else emptyList()
                } else {
                    emptyList()
                }

                val slug: String = extractScalar("slug", frontMatterLines)?.takeIf { it.isNotBlank() }
                    ?: entry.frontMatter["slug"]?.singleOrNull()?.takeIf { it.isNotBlank() }
                    ?: canonicalSlug
                val locale: String = extractScalar("locale", frontMatterLines)?.takeIf { it.isNotBlank() }
                    ?: entry.frontMatter["locale"]?.singleOrNull()
                    ?: if (lang == "pt-br") "pt-BR" else "en-CA"
                val title: String = extractScalar("title", frontMatterLines)
                    ?: entry.frontMatter["title"]?.singleOrNull()
                    ?: return@mapNotNull null
                val rawDate: String = extractScalar("date", frontMatterLines)
                    ?: entry.frontMatter["date"]?.singleOrNull().orEmpty()
                val date: String = formatDisplayDate(rawDate = rawDate, locale = locale)
                val description: String = extractScalar("description", frontMatterLines)
                    ?: entry.frontMatter["description"]?.singleOrNull().orEmpty()
                val summary: String = extractScalar("summary", frontMatterLines)
                    ?: entry.frontMatter["summary"]?.singleOrNull().orEmpty()
                val thumbnail: String = extractScalar("thumbnail", frontMatterLines)
                    ?: entry.frontMatter["thumbnail"]?.singleOrNull().orEmpty()
                val authors: List<AuthorRow> = extractAuthors(frontMatterLines)
                val readTime: String = if (fileText.isNotEmpty()) {
                    calculateReadingTime(fileText)
                } else {
                    entry.frontMatter["readTime"]?.singleOrNull().orEmpty()
                }
                val statusRaw: String = (extractScalar("status", frontMatterLines) ?: entry.frontMatter["status"]?.singleOrNull()).orEmpty().lowercase()
                val tags: List<String> = entry.frontMatter["tags"].orEmpty()

                ArticleRow(
                    slug = slug,
                    date = date,
                    title = title,
                    description = description,
                    tags = tags,
                    readTime = readTime,
                    status = if (statusRaw == "draft") "Draft" else "Published",
                    locale = locale,
                    summary = summary,
                    authors = authors,
                    thumbnail = thumbnail,
                )
            }

        val enRows: List<ArticleRow> = rows.filter { it.locale == "en-CA" }.sortedByDescending { toSortableDate(it.date) }
        val ptRowsBySlug: Map<String, ArticleRow> = rows.filter { it.locale == "pt-BR" }.associateBy { it.slug }
        val ptRows: List<ArticleRow> = enRows.map { enRow -> ptRowsBySlug[enRow.slug] ?: enRow }

        fun StringBuilder.appendArticleList(listName: String, listRows: List<ArticleRow>) {
            appendLine("    val $listName: List<Article> = listOf(")
            listRows.forEach { row: ArticleRow ->
                val tagsLiteral: String = row.tags.joinToString(separator = ", ") { "\"$it\"" }
                val authorsLiteral: String = if (row.authors.isEmpty()) {
                    "emptyList()"
                } else {
                    "listOf(" + row.authors.joinToString(separator = ", ") {
                        "ArticleAuthor(name = \"${escapeForKotlin(it.name)}\", avatar = \"${escapeForKotlin(it.avatar)}\")"
                    } + ")"
                }
                appendLine("        Article(")
                appendLine("            slug = \"${row.slug}\",")
                appendLine("            date = \"${escapeForKotlin(row.date)}\",")
                appendLine("            title = \"${escapeForKotlin(row.title)}\",")
                appendLine("            description = \"${escapeForKotlin(row.description)}\",")
                appendLine("            tags = listOf($tagsLiteral),")
                appendLine("            readTime = \"${escapeForKotlin(row.readTime)}\",")
                appendLine("            status = ArticleStatus.${row.status},")
                appendLine("            summary = \"${escapeForKotlin(row.summary)}\",")
                appendLine("            authors = $authorsLiteral,")
                appendLine("            thumbnail = \"${escapeForKotlin(row.thumbnail)}\",")
                appendLine("        ),")
            }
            appendLine("    )")
        }

        return buildString {
            appendLine("package dev.tonholo.portfolio.generated.articles")
            appendLine()
            appendLine("import dev.tonholo.portfolio.domain.model.Article")
            appendLine("import dev.tonholo.portfolio.domain.model.ArticleAuthor")
            appendLine("import dev.tonholo.portfolio.domain.model.ArticleStatus")
            appendLine()
            appendLine("/**")
            appendLine(" * Article index generated from the markdown sources under")
            appendLine(" * `src/jsMain/resources/markdown/articles/`. Do not edit by hand;")
            appendLine(" * re-run the `kobwebxMarkdownProcess` task to regenerate.")
            appendLine(" */")
            appendLine("object GeneratedArticles {")
            appendArticleList("EN", enRows)
            appendLine()
            appendArticleList("PT_BR", ptRows)
            appendLine()
            appendLine("    val ALL: List<Article> = EN")
            appendLine("}")
        }
    }
}
