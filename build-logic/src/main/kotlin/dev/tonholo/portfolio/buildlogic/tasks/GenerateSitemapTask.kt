package dev.tonholo.portfolio.buildlogic.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

/**
 * Generates sitemap.xml containing all static pages and dynamic extra slugs.
 */
abstract class GenerateSitemapTask : DefaultTask() {
    @get:InputDirectory
    abstract val pagesDir: DirectoryProperty

    @get:InputFile
    abstract val appPropertiesFile: RegularFileProperty

    @get:Input
    abstract val lastModified: Property<String>

    @get:Input
    abstract val baseUrl: Property<String>

    @get:Input
    abstract val extraSlugs: ListProperty<String>

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    init {
        baseUrl.convention(DEFAULT_BASE_URL)
    }

    @TaskAction
    fun execute() {
        val pagesDirectory: File = pagesDir.get().asFile
        val targetFile: File = outputFile.get().asFile
        val pagePaths: List<String> = pagesDirectory.walkTopDown()
            .filter { it.isFile && it.extension == "kt" }
            .filter { file: File ->
                val text: String = file.readText()
                val pageAnnotationLine: String? = text.lines().firstOrNull { it.trimStart().startsWith("@Page") }
                pageAnnotationLine != null && !pageAnnotationLine.contains("{")
            }
            .map { file: File ->
                val text: String = file.readText()
                val routeOverride: String? = Regex("""@Page\(routeOverride\s*=\s*"([^"]+)"""")
                    .find(text)?.groupValues?.get(1)
                if (routeOverride != null) {
                    routeOverride
                } else {
                    val relative: String = file.relativeTo(pagesDirectory).path
                        .removeSuffix(".kt")
                        .replace("\\", "/")
                    if (relative == "Index") {
                        "/"
                    } else {
                        "/" + relative.replace("/Index", "").split("/").joinToString("/") { segment: String ->
                            segment.replace(Regex("([a-z])([A-Z])")) { match ->
                                "${match.groupValues[1]}-${match.groupValues[2]}"
                            }.lowercase()
                        }
                    }
                }
            }
            .toList()

        val basePageMetadata: Map<String, Pair<String, String>> = mapOf(
            "/" to ("weekly" to "1.0"),
            "/about" to ("monthly" to "0.8"),
            "/projects" to ("weekly" to "0.9"),
            "/articles" to ("weekly" to "0.9"),
            "/resume" to ("monthly" to "0.7"),
        )

        val slugs: List<String> = extraSlugs.get()
        val extraSlugMetadata: Map<String, Pair<String, String>> = slugs.associateWith { slug: String ->
            val priority: String = if (slug.startsWith("/projects/")) "0.7" else "0.6"
            "monthly" to priority
        }
        val allMetadata: Map<String, Pair<String, String>> = basePageMetadata + extraSlugMetadata
        val allPaths: List<String> = (pagePaths + slugs).distinct().sorted()

        val sitemapContent: String = renderSitemap(
            paths = allPaths,
            metadata = allMetadata,
            baseUrl = baseUrl.get(),
            lastModified = lastModified.get(),
        )

        targetFile.parentFile.mkdirs()
        targetFile.writeText(sitemapContent)
    }

    companion object {
        /** The site's public host; sitemap URLs must match the canonical URLs pages declare. */
        const val DEFAULT_BASE_URL: String = "https://rafael.tonholo.dev"
        private val EXCLUDED_PATHS: Set<String> = setOf("/404")

        /** Renders sitemap XML for [paths], leaving out pages that shouldn't be indexed, such as the 404 page. */
        fun renderSitemap(
            paths: List<String>,
            metadata: Map<String, Pair<String, String>>,
            baseUrl: String,
            lastModified: String,
        ): String = buildString {
            appendLine("""<?xml version="1.0" encoding="UTF-8"?>""")
            appendLine("""<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">""")
            for (path: String in paths.filterNot { it in EXCLUDED_PATHS }) {
                val (changefreq: String, priority: String) = metadata[path] ?: ("monthly" to "0.5")
                appendLine("  <url>")
                appendLine("    <loc>$baseUrl$path</loc>")
                appendLine("    <lastmod>$lastModified</lastmod>")
                appendLine("    <changefreq>$changefreq</changefreq>")
                appendLine("    <priority>$priority</priority>")
                appendLine("  </url>")
            }
            appendLine("</urlset>")
        }
    }
}
