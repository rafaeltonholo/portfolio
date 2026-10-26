import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication
import kotlinx.html.link
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatterBuilder
import java.util.Locale
import java.util.Properties
import kotlin.math.ceil

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kobweb.application)
    alias(libs.plugins.kobwebx.markdown)
    alias(libs.plugins.kotlin.power.assert)
    alias(libs.plugins.detekt)
}

group = "dev.tonholo.portfolio"
version = "0.1.0-SNAPSHOT"

val appProperties = Properties().apply {
    rootProject.rootDir.resolve("app.properties").inputStream().use { load(it) }
}

/**
 * Convert a TitleCamelCase string to kebab-case, matching Kobweb's own
 * `camelCaseToKebabCase` used to derive URL slugs from markdown filenames.
 * Acronym-aware: "ExampleABC" -> "example-abc", "ABCExample" -> "abc-example".
 */
fun String.splitCamelCase(): List<String> {
    val currWord = StringBuilder()
    val words = mutableListOf<String>()
    var lastIsUpper = false
    forEachIndexed { i, c ->
        val isUpper = c.isUpperCase()
        if (isUpper) {
            if (currWord.isNotEmpty() && (!lastIsUpper || (i < lastIndex && this[i + 1].isLowerCase()))) {
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

fun String.camelCaseToKebabCase(): String = splitCamelCase().joinToString("-")

/**
 * Lower-cased URL segments of every non-default locale supported by the site.
 * Must stay in sync with `dev.tonholo.portfolio.locale.Locale.pathSegment` for
 * every non-null value. Used to split a kebab-cased filename slug into its
 * canonical slug and locale (e.g., `bleacher-report-pt-br` -> `bleacher-report` + `pt-br`).
 */
val localizedPathSegments = listOf("pt-br")

/**
 * Splits a kebab-cased slug into (canonical, locale?). Returns (slug, null)
 * when no known locale suffix is present.
 */
fun String.splitLocalizedSlug(): Pair<String, String?> {
    for (lang in localizedPathSegments) {
        val suffix = "-$lang"
        if (this.endsWith(suffix)) {
            return this.removeSuffix(suffix) to lang
        }
    }
    return this to null
}

// Dynamic-route slugs registered with the Kobweb static exporter. Project
// slugs stay hard-coded for `@Page("/projects/{slug}")`; the markdown-backed
// project slugs (full case studies) are discovered by scanning
// `src/jsMain/resources/markdown/projects/`. Article slugs are also scanned
// so the exporter can emit one HTML per markdown file without maintaining a
// parallel list.
val projectSlugs = listOf(
    "svg-compose",
    "ivy-wallet",
)

val articlesMarkdownDir = layout.projectDirectory
    .dir("src/jsMain/resources/markdown/articles")

val articleSlugs: List<String> = articlesMarkdownDir.asFile
    .takeIf { it.exists() }
    ?.listFiles { file -> file.isFile && file.extension == "md" }
    ?.map { it.nameWithoutExtension.camelCaseToKebabCase() }
    ?.sorted()
    .orEmpty()

val projectsMarkdownDir = layout.projectDirectory
    .dir("src/jsMain/resources/markdown/projects")

val projectMarkdownSlugs: List<String> = projectsMarkdownDir.asFile
    .takeIf { it.exists() }
    ?.listFiles { file -> file.isFile && file.extension == "md" }
    ?.map { it.nameWithoutExtension.camelCaseToKebabCase() }
    ?.sorted()
    .orEmpty()

kobweb {
    app {
        index {
            description.set("Rafael Tonholo - Android Engineer")
            head.add {
                link {
                    rel = "preconnect"
                    href = "https://fonts.googleapis.com"
                }
                link {
                    rel = "preconnect"
                    href = "https://fonts.gstatic.com"
                    attributes["crossorigin"] = "anonymous"
                }
                link {
                    href = "https://fonts.googleapis.com/css2" +
                        "?family=JetBrains+Mono:wght@400;500;600;700" +
                        "&family=Geist:wght@400;500;600;700" +
                        "&family=Fraunces:opsz,wght@9..144,400;9..144,500;9..144,600" +
                        "&display=swap"
                    rel = "stylesheet"
                }
                // Phosphor icon stylesheets. Only the weights actually rendered
                // by the site ship. If a new weight is needed, add it here AND
                // expose it on PhosphorWeight; the enum and the loaded
                // stylesheets must stay in sync or the icon will render blank.
                link {
                    rel = "stylesheet"
                    href = "https://unpkg.com/@phosphor-icons/web@2.1.2/src/regular/style.css"
                }
                link {
                    rel = "stylesheet"
                    href = "https://unpkg.com/@phosphor-icons/web@2.1.2/src/fill/style.css"
                }
                link {
                    rel = "apple-touch-icon"
                    href = "/logo192.png"
                }
                link {
                    rel = "icon"
                    type = "image/png"
                    sizes = "192x192"
                    href = "/logo192.png"
                }
                link {
                    rel = "icon"
                    type = "image/png"
                    sizes = "512x512"
                    href = "/logo512.png"
                }
            }
        }
        export {
            // Hardcoded dynamic project slugs (no markdown case study) are
            // rendered by `DynamicProjectPage` (EN) and
            // `DynamicLocalizedProjectPage` (non-default locales). Emit one
            // extra route per locale.
            projectSlugs.forEach { slug ->
                addExtraRoute(route = "/projects/$slug", exportPath = "projects/$slug.html")
                localizedPathSegments.forEach { lang ->
                    addExtraRoute(
                        route = "/projects/$lang/$slug",
                        exportPath = "projects/$lang/$slug.html",
                    )
                }
            }
            articleSlugs.forEach { kebabSlug ->
                val (canonical, lang) = kebabSlug.splitLocalizedSlug()
                val route = if (lang == null) "/articles/$canonical" else "/articles/$lang/$canonical"
                val exportPath = if (lang == null) "articles/$canonical.html" else "articles/$lang/$canonical.html"
                addExtraRoute(route = route, exportPath = exportPath)
            }
            // Emit 404.html so GitHub Pages (and most static hosts) serve the
            // IDE-themed recovery screen for unknown URLs. The SPA router
            // also re-uses this content via setErrorPage for in-app nav.
            addExtraRoute(route = "/404", exportPath = "404.html")
        }
    }

    markdown {
        // Every generated markdown page wraps its body in `ArticleMarkdownLayout`
        // so detail pages inherit the sidebar, meta row, title, and lede chrome.
        defaultLayout.set("dev.tonholo.portfolio.components.layouts.ArticleMarkdownLayout")

        // Process callback that collects frontmatter from every article under
        // `src/jsMain/resources/markdown/articles/` and generates a Kotlin
        // object (`GeneratedArticles`) with the same shape as the old
        // `StaticContent.ARTICLES` list. The generated object is the new single
        // source of truth for the /articles index.
        //
        // All helper logic is inlined inside the lambda so the configuration
        // cache can serialize the task without capturing build-script types.
        process.set { entries ->
            fun String.escapeForKotlin(): String = this
                .replace(oldValue = "\\", newValue = "\\\\")
                .replace(oldValue = "\"", newValue = "\\\"")
                .replace(oldValue = "\n", newValue = "\\n")
                .replace(oldValue = "\r", newValue = "\\r")
                .replace(oldValue = "\t", newValue = "\\t")

            // Local re-impl of Kobweb's `camelCaseToKebabCase` so the
            // configuration cache can serialize the lambda without capturing
            // script-level helpers. Acronym-aware: "ExampleABC" -> "example-abc",
            // "ABCExample" -> "abc-example".
            fun String.toKebabSlug(): String {
                val words = mutableListOf<String>()
                val curr = StringBuilder()
                var lastIsUpper = false
                forEachIndexed { i, c ->
                    val isUpper = c.isUpperCase()
                    if (isUpper && curr.isNotEmpty() &&
                        (!lastIsUpper || (i < lastIndex && this[i + 1].isLowerCase()))
                    ) {
                        words += curr.toString()
                        curr.clear()
                    }
                    curr.append(c.lowercase())
                    lastIsUpper = isUpper
                }
                if (curr.isNotEmpty()) words += curr.toString()
                return words.joinToString("-")
            }

            data class Row(
                val slug: String,
                val date: String,
                val title: String,
                val description: String,
                val tags: List<String>,
                val readTime: String,
                val status: String,
            )

            val articleDateFormatter = DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("MMM d, yyyy")
                .toFormatter(Locale.ENGLISH)

            fun String.toSortableDate(): LocalDate = runCatching {
                LocalDate.parse(this.trim(), articleDateFormatter)
            }.getOrElse { LocalDate.MIN }

            val markdownResourceDir = layout.projectDirectory.dir("src/jsMain/resources/markdown").asFile
            val readingSpeedWordsPerMinute = 200.0
            fun calculateReadingTime(markdownText: String): String {
                val body = if (markdownText.startsWith("---")) {
                    val secondMarker = markdownText.indexOf("---", startIndex = 3)
                    if (secondMarker != -1) markdownText.substring(secondMarker + 3) else markdownText
                } else {
                    markdownText
                }
                val words = body.trim().split(Regex("\\s+")).count { it.isNotBlank() }
                val minutes = maxOf(1, ceil(words / readingSpeedWordsPerMinute).toInt())
                return "$minutes min"
            }

            val rows = entries
                .filter { entry -> entry.filePath.startsWith("articles/") }
                .mapNotNull { entry ->
                    val frontMatter = entry.frontMatter
                    val slug = entry.filePath
                        .removePrefix("articles/")
                        .removeSuffix(".md")
                        .toKebabSlug()
                    val title = frontMatter["title"]?.singleOrNull() ?: return@mapNotNull null
                    val date = frontMatter["date"]?.singleOrNull().orEmpty()
                    val description = frontMatter["description"]?.singleOrNull().orEmpty()
                    val markdownFile = File(markdownResourceDir, entry.filePath)
                    val readTime = if (markdownFile.exists()) {
                        calculateReadingTime(markdownFile.readText())
                    } else {
                        frontMatter["readTime"]?.singleOrNull().orEmpty()
                    }
                    val statusRaw = frontMatter["status"]?.singleOrNull()?.lowercase().orEmpty()
                    val tags = frontMatter["tags"].orEmpty()
                    Row(
                        slug = slug,
                        date = date,
                        title = title,
                        description = description,
                        tags = tags,
                        readTime = readTime,
                        status = if (statusRaw == "draft") "Draft" else "Published",
                    )
                }
                .sortedByDescending { it.date.toSortableDate() }

            val content = buildString {
                appendLine("package dev.tonholo.portfolio.generated.articles")
                appendLine()
                appendLine("import dev.tonholo.portfolio.domain.model.Article")
                appendLine("import dev.tonholo.portfolio.domain.model.ArticleStatus")
                appendLine()
                appendLine("/**")
                appendLine(" * Article index generated from the markdown sources under")
                appendLine(" * `src/jsMain/resources/markdown/articles/`. Do not edit by hand;")
                appendLine(" * re-run the `kobwebxMarkdownProcess` task to regenerate.")
                appendLine(" */")
                appendLine("object GeneratedArticles {")
                appendLine("    val ALL: List<Article> = listOf(")
                rows.forEach { row ->
                    val tagsLiteral = row.tags.joinToString(separator = ", ") { "\"${it}\"" }
                    appendLine("        Article(")
                    appendLine("            slug = \"${row.slug}\",")
                    appendLine("            date = \"${row.date}\",")
                    appendLine("            title = \"${row.title.escapeForKotlin()}\",")
                    appendLine("            description = \"${row.description.escapeForKotlin()}\",")
                    appendLine("            tags = listOf($tagsLiteral),")
                    appendLine("            readTime = \"${row.readTime}\",")
                    appendLine("            status = ArticleStatus.${row.status},")
                    appendLine("        ),")
                }
                appendLine("    )")
                appendLine("}")
            }

            generateKotlin(
                filePath = "dev/tonholo/portfolio/generated/articles/GeneratedArticles.kt",
                content = content,
            )
        }
    }
}

tasks.withType<com.varabyte.kobwebx.gradle.markdown.tasks.ProcessMarkdownTask>().configureEach {
    notCompatibleWithConfigurationCache("Kobweb markdown processing task")
}

tasks.withType<com.varabyte.kobwebx.gradle.markdown.tasks.ConvertMarkdownTask>().configureEach {
    notCompatibleWithConfigurationCache("Kobweb markdown conversion task")
    markdownHandlers.code.set { codeBlock ->
        val rawInfo = codeBlock.info.trim()
        val lang = rawInfo.split(Regex("\\s+")).firstOrNull()?.ifBlank { "text" } ?: "text"
        val escaped = codeBlock.literal
            .replace("$", "\${'$'}")
            .replace("\"\"\"", "\${\"\\\"\\\"\\\"\"}")
        "dev.tonholo.portfolio.components.atoms.ShikiCodeBlock(code = \"\"\"$escaped\"\"\", language = \"$lang\")"
    }
    markdownHandlers.a.set { link ->
        val dest = link.destination
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("$", "\${'$'}")
        "dev.tonholo.portfolio.components.atoms.ArticleLink(href = \"$dest\")"
    }
    markdownHandlers.blockquote.set { blockQuote ->
        val firstParagraph = blockQuote.firstChild as? org.commonmark.node.Paragraph
        val firstText = firstParagraph?.firstChild as? org.commonmark.node.Text
        val regex = """\[!(NOTE|TIP|IMPORTANT|WARNING|CAUTION|QUOTE)(?:\s+"([^"]*)")?\]""".toRegex(RegexOption.IGNORE_CASE)
        val match = firstText?.let { regex.find(it.literal) }
        if (match != null) {
            val typeStr = match.groupValues[1].uppercase()
            val customTitle = match.groupValues[2].takeIf { it.isNotBlank() }
            val remaining = firstText.literal.substringAfter(match.value).trimStart()
            if (remaining.isEmpty()) {
                firstText.unlink()
                if (firstParagraph.firstChild == null) {
                    firstParagraph.unlink()
                }
            } else {
                firstText.literal = remaining
            }
            val titleParam = if (customTitle != null) ", title = \"$customTitle\"" else ""
            "dev.tonholo.portfolio.components.molecules.Callout(type = dev.tonholo.portfolio.components.molecules.CalloutType.$typeStr$titleParam)"
        } else {
            "dev.tonholo.portfolio.components.molecules.Callout(type = dev.tonholo.portfolio.components.molecules.CalloutType.QUOTE)"
        }
    }
}

kotlin {
    configAsKobwebApplication("portfolio")

    sourceSets {
        jsMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.html.core)
            implementation(libs.kobweb.core)
            implementation(libs.kobweb.silk)
            implementation(libs.kobwebx.markdown)
            implementation(npm("@phosphor-icons/web", "2.1.2"))
        }
        jsTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

powerAssert {
    functions.addAll(
        "kotlin.assert",
        "kotlin.test.assertTrue",
        "kotlin.test.assertEquals",
        "kotlin.test.assertNull",
    )
}

detekt {
    config.setFrom(rootProject.file("config/detekt.yml"))
    buildUponDefaultConfig = true
    allRules = false
}

// Exclude all generated sources from every Detekt task.
// Generated dirs include Compose resource accessors, KSP output, and Kobweb entry files.
tasks.matching { it.name.startsWith("detekt") }.configureEach {
    if (this is org.gradle.api.tasks.SourceTask) {
        exclude { element ->
            element.file.absolutePath.contains("/build/generated/")
        }
    }
}

dependencies {
    detektPlugins(libs.detekt.rules.ktlint.wrapper)
    detektPlugins(libs.compose.rules.detekt)
}

val generatedAnalyticsSrcDir = layout.buildDirectory.dir("generated/analytics/kotlin")

val generateBuildAnalytics by tasks.registering {
    val scriptUrl = appProperties["UMAMI_SCRIPT_URL"]?.toString()
        ?: error("UMAMI_SCRIPT_URL not set in app.properties")
    val websiteId = appProperties["UMAMI_WEBSITE_ID"]?.toString()
        ?: error("UMAMI_WEBSITE_ID not set in app.properties")
    val outputDir = generatedAnalyticsSrcDir
    val outputFile = outputDir.map { it.file("dev/tonholo/portfolio/analytics/BuildAnalytics.kt") }
    val appPropertiesFile = rootProject.rootDir.resolve("app.properties")

    inputs.file(appPropertiesFile)
    inputs.property("scriptUrl", scriptUrl)
    inputs.property("websiteId", websiteId)
    outputs.file(outputFile)

    doLast {
        val file = outputFile.get().asFile
        file.parentFile.mkdirs()
        file.writeText(
            """
                |package dev.tonholo.portfolio.analytics
                |
                |internal object BuildAnalytics {
                |    const val UMAMI_SCRIPT_URL: String = "$scriptUrl"
                |    const val UMAMI_WEBSITE_ID: String = "$websiteId"
                |}
                |
            """.trimMargin()
        )
    }
}

kotlin.sourceSets.named("jsMain") {
    kotlin.srcDir(generatedAnalyticsSrcDir)
}

tasks.configureEach {
    if (name == "compileKotlinJs" || name == "kspKotlinJs") {
        dependsOn(generateBuildAnalytics)
    }
}

val generateSitemap by tasks.registering {
    val pagesDir = layout.projectDirectory.dir("src/jsMain/kotlin/dev/tonholo/portfolio/pages")
    val outputFile = layout.projectDirectory.file("src/jsMain/resources/public/sitemap.xml")
    val baseUrl = "https://tonholo.dev"
    val appPropertiesFile = rootProject.rootDir.resolve("app.properties")
    val lastModified = appProperties["LAST_MODIFIED"]?.toString()
        ?: error("LAST_MODIFIED not set in app.properties")

    val pageMetadata = mapOf(
        "/" to ("weekly" to "1.0"),
        "/about" to ("monthly" to "0.8"),
        "/projects" to ("weekly" to "0.9"),
        "/articles" to ("weekly" to "0.9"),
        "/resume" to ("monthly" to "0.7"),
    )

    val dynamicProjectUrls = projectSlugs.flatMap { slug ->
        listOf("/projects/$slug") + localizedPathSegments.map { lang -> "/projects/$lang/$slug" }
    }
    val markdownProjectUrls = projectMarkdownSlugs.map { kebabSlug ->
        val (canonical, lang) = kebabSlug.splitLocalizedSlug()
        if (lang == null) "/projects/$canonical" else "/projects/$lang/$canonical"
    }
    val articleUrls = articleSlugs.map { kebabSlug ->
        val (canonical, lang) = kebabSlug.splitLocalizedSlug()
        if (lang == null) "/articles/$canonical" else "/articles/$lang/$canonical"
    }
    val extraSlugs = dynamicProjectUrls + markdownProjectUrls + articleUrls

    inputs.dir(pagesDir)
    inputs.file(appPropertiesFile)
    inputs.property("lastModified", lastModified)
    inputs.property("extraSlugs", extraSlugs.joinToString(","))
    outputs.file(outputFile)

    doLast {
        val pagePaths = pagesDir.asFile.walkTopDown()
            .filter { it.isFile && it.extension == "kt" }
            .filter { file ->
                val text = file.readText()
                // Include only static @Page routes; skip dynamic routes like @Page("/foo/{slug}")
                val pageAnnotationLine = text.lines().firstOrNull { it.trimStart().startsWith("@Page") }
                pageAnnotationLine != null && !pageAnnotationLine.contains("{")
            }
            .map { file ->
                val text = file.readText()
                // Prefer the explicit routeOverride from the @Page annotation when present
                val routeOverride = Regex("""@Page\(routeOverride\s*=\s*"([^"]+)"""")
                    .find(text)?.groupValues?.get(1)
                if (routeOverride != null) {
                    routeOverride
                } else {
                    val relative = file.relativeTo(pagesDir.asFile).path
                        .removeSuffix(".kt")
                        .replace("\\", "/")
                    if (relative == "Index") {
                        "/"
                    } else {
                        "/" + relative
                            .replace("/Index", "")
                            .split("/")
                            .joinToString("/") { segment ->
                                segment
                                    .replace(Regex("([a-z])([A-Z])")) { m ->
                                        "${m.groupValues[1]}-${m.groupValues[2]}"
                                    }
                                    .lowercase()
                            }
                    }
                }
            }
            .toList()

        val extraSlugMetadata = extraSlugs.associate { slug ->
            val priority = if (slug.startsWith("/projects/")) "0.7" else "0.6"
            slug to ("monthly" to priority)
        }
        val allMetadata = pageMetadata + extraSlugMetadata

        val allPaths = (pagePaths + extraSlugs).distinct().sorted()

        val sitemap = buildString {
            appendLine($$"""<?xml version="1.0" encoding="UTF-8"?>""")
            appendLine($$"""<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">""")
            for (path in allPaths) {
                val (changefreq, priority) = allMetadata[path] ?: ("monthly" to "0.5")
                appendLine("  <url>")
                appendLine("    <loc>$baseUrl$path</loc>")
                appendLine("    <lastmod>$lastModified</lastmod>")
                appendLine("    <changefreq>$changefreq</changefreq>")
                appendLine("    <priority>$priority</priority>")
                appendLine("  </url>")
            }
            appendLine("</urlset>")
        }
        outputFile.asFile.writeText(sitemap)
    }
}

tasks.configureEach {
    if (name == "jsProcessResources") {
        dependsOn(generateSitemap)
    }
}
