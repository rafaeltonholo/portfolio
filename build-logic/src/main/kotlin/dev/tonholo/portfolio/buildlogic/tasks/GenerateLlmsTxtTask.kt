package dev.tonholo.portfolio.buildlogic.tasks

import dev.tonholo.portfolio.buildlogic.SlugUtils
import dev.tonholo.portfolio.buildlogic.markdown.MarkdownArticlesProcessor
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

/**
 * A link listed in `llms.txt`.
 *
 * @property title the page title.
 * @property url the absolute page URL.
 * @property description a short summary, or blank for none.
 */
data class SiteLink(val title: String, val url: String, val description: String)

/**
 * Generates `llms.txt` (see https://llmstxt.org) from the English article and project markdown frontmatter, so the
 * list never drifts from the published pages. Drafts and translations are left out.
 */
abstract class GenerateLlmsTxtTask : DefaultTask() {
    @get:InputDirectory
    abstract val articlesDir: DirectoryProperty

    @get:InputDirectory
    abstract val projectsDir: DirectoryProperty

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun execute() {
        val text = renderLlmsTxt(
            articles = readLinks(directory = articlesDir.get().asFile, section = "articles"),
            projects = readLinks(directory = projectsDir.get().asFile, section = "projects"),
        )
        outputFile.get().asFile.apply { parentFile.mkdirs() }.writeText(text)
    }

    private fun readLinks(directory: File, section: String): List<SiteLink> =
        directory.listFiles { file -> file.extension == "md" && !file.nameWithoutExtension.endsWith(TRANSLATION_SUFFIX) }
            .orEmpty()
            .sortedBy { it.name }
            .mapNotNull { file -> file.toSiteLink(section = section) }

    private fun File.toSiteLink(section: String): SiteLink? {
        val frontMatter: List<String> = readText().frontMatterLines()
        if (MarkdownArticlesProcessor.extractScalar("status", frontMatter).equals("draft", ignoreCase = true)) return null
        val title: String = MarkdownArticlesProcessor.extractScalar("title", frontMatter) ?: return null
        val slug: String = MarkdownArticlesProcessor.extractScalar("slug", frontMatter)?.takeIf { it.isNotBlank() }
            ?: SlugUtils.camelCaseToKebabCase(nameWithoutExtension)
        return SiteLink(
            title = title,
            url = "${GenerateSitemapTask.DEFAULT_BASE_URL}/$section/$slug",
            description = MarkdownArticlesProcessor.extractScalar("description", frontMatter).orEmpty(),
        )
    }

    private fun String.frontMatterLines(): List<String> {
        if (!startsWith(FRONT_MATTER_MARKER)) return emptyList()
        val end: Int = indexOf(FRONT_MATTER_MARKER, startIndex = FRONT_MATTER_MARKER.length)
        return if (end == -1) emptyList() else substring(FRONT_MATTER_MARKER.length, end).lines()
    }

    companion object {
        private const val TRANSLATION_SUFFIX = "PtBr"
        private const val FRONT_MATTER_MARKER = "---"

        /** The site summary at the top of `llms.txt`. */
        const val SUMMARY: String = "Personal site of Rafael Tonholo, a senior Android engineer in Halifax, Nova Scotia. " +
            "Case studies of production Android work and long-form, tested articles on Android and Kotlin, " +
            "in English and Brazilian Portuguese."

        /** Renders `llms.txt`: the site name, a summary, then link lists for articles, projects and pages. */
        fun renderLlmsTxt(articles: List<SiteLink>, projects: List<SiteLink>): String = buildString {
            appendLine("# Rafael Tonholo")
            appendLine()
            appendLine("> $SUMMARY")
            appendSection(heading = "Articles", links = articles)
            appendSection(heading = "Projects", links = projects)
            appendSection(
                heading = "Pages",
                links = listOf(
                    SiteLink(title = "About", url = "${GenerateSitemapTask.DEFAULT_BASE_URL}/about", description = ""),
                    SiteLink(title = "Resume", url = "${GenerateSitemapTask.DEFAULT_BASE_URL}/resume", description = ""),
                ),
            )
        }

        private fun StringBuilder.appendSection(heading: String, links: List<SiteLink>) {
            appendLine()
            appendLine("## $heading")
            appendLine()
            links.forEach { link ->
                val notes: String = link.description.takeIf { it.isNotBlank() }?.let { ": $it" }.orEmpty()
                appendLine("- [${link.title}](${link.url})$notes")
            }
        }
    }
}
