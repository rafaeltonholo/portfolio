import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication
import dev.tonholo.portfolio.buildlogic.SlugUtils
import dev.tonholo.portfolio.buildlogic.camelCaseToKebabCase
import dev.tonholo.portfolio.buildlogic.splitLocalizedSlug
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
    id("portfolio.analytics")
    id("portfolio.sitemap")
}

group = "dev.tonholo.portfolio"
version = "0.1.0-SNAPSHOT"

val appProperties = Properties().apply {
    rootProject.rootDir.resolve("app.properties").inputStream().use { load(it) }
}

val localizedPathSegments: List<String> = SlugUtils.LOCALIZED_PATH_SEGMENTS
val projectSlugs: List<String> = listOf("svg-compose")

val articlesMarkdownDir = layout.projectDirectory.dir("src/jsMain/resources/markdown/articles")
val articleSlugs: List<String> = SlugUtils.findMarkdownSlugs(articlesMarkdownDir.asFile)

val projectsMarkdownDir = layout.projectDirectory.dir("src/jsMain/resources/markdown/projects")
val projectMarkdownSlugs: List<String> = SlugUtils.findMarkdownSlugs(projectsMarkdownDir.asFile)

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

            val localizedSegments = listOf("pt-br")
            fun String.splitLocalizedSlug(): Pair<String, String?> {
                for (lang in localizedSegments) {
                    val suffix = "-$lang"
                    if (this.endsWith(suffix)) {
                        return this.removeSuffix(suffix) to lang
                    }
                }
                return this to null
            }

            data class AuthorRow(
                val name: String,
                val avatar: String,
            )

            data class Row(
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

            val articleDateFormatter = DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("MMM d, yyyy")
                .toFormatter(Locale.ENGLISH)

            fun String.toSortableDate(): LocalDate = runCatching {
                LocalDate.parse(this.substringBefore('T').trim())
            }.recoverCatching {
                LocalDate.parse(this.trim(), articleDateFormatter)
            }.getOrElse { LocalDate.MIN }

            fun formatDisplayDate(rawDate: String, locale: String): String {
                val isoDate = runCatching {
                    LocalDate.parse(rawDate.substringBefore('T').trim())
                }.getOrNull() ?: return rawDate
                return if (locale == "pt-BR") {
                    val ptMonth = when (isoDate.monthValue) {
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
                    val enMonth = when (isoDate.monthValue) {
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
                    val rawSlug = entry.filePath
                        .removePrefix("articles/")
                        .removeSuffix(".md")
                        .toKebabSlug()
                    val (canonicalSlug, lang) = rawSlug.splitLocalizedSlug()
                    val markdownFile = File(markdownResourceDir, entry.filePath)
                    val fileText = if (markdownFile.exists()) markdownFile.readText() else ""
                    val frontMatterLines = if (fileText.startsWith("---")) {
                        val secondMarker = fileText.indexOf("---", startIndex = 3)
                        if (secondMarker != -1) {
                            fileText.substring(3, secondMarker).lines()
                        } else emptyList()
                    } else emptyList()

                    fun extractScalar(key: String): String? {
                        val lineIndex = frontMatterLines.indexOfFirst { line ->
                            line.trimStart().startsWith("$key:")
                        }
                        if (lineIndex == -1) return null
                        val line = frontMatterLines[lineIndex]
                        val afterColon = line.substringAfter("$key:").trim()
                        if (afterColon == ">" || afterColon == ">-" || afterColon == "|") {
                            val indentedLines = mutableListOf<String>()
                            for (i in (lineIndex + 1) until frontMatterLines.size) {
                                val nextLine = frontMatterLines[i]
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

                    fun extractAuthors(): List<AuthorRow> {
                        val authorsIndex = frontMatterLines.indexOfFirst { line ->
                            line.trimStart().startsWith("authors:")
                        }
                        if (authorsIndex == -1) return emptyList()
                        val authorList = mutableListOf<AuthorRow>()
                        var currentName = ""
                        var currentAvatar = ""
                        fun saveCurrent() {
                            if (currentName.isNotBlank() || currentAvatar.isNotBlank()) {
                                authorList.add(AuthorRow(name = currentName, avatar = currentAvatar))
                                currentName = ""
                                currentAvatar = ""
                            }
                        }
                        for (i in (authorsIndex + 1) until frontMatterLines.size) {
                            val line = frontMatterLines[i]
                            if (line.isNotBlank() && !line.startsWith(" ") && !line.startsWith("\t") && !line.startsWith("-")) {
                                break
                            }
                            val trimmed = line.trim()
                            if (trimmed.startsWith("-")) {
                                saveCurrent()
                                val itemContent = trimmed.removePrefix("-").trim()
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

                    val slug = extractScalar("slug")?.takeIf { it.isNotBlank() }
                        ?: frontMatter["slug"]?.singleOrNull()?.takeIf { it.isNotBlank() }
                        ?: canonicalSlug
                    val locale = extractScalar("locale")?.takeIf { it.isNotBlank() }
                        ?: frontMatter["locale"]?.singleOrNull()
                        ?: if (lang == "pt-br") "pt-BR" else "en-CA"
                    val title = extractScalar("title")
                        ?: frontMatter["title"]?.singleOrNull()
                        ?: return@mapNotNull null
                    val rawDate = extractScalar("date")
                        ?: frontMatter["date"]?.singleOrNull().orEmpty()
                    val date = formatDisplayDate(rawDate = rawDate, locale = locale)
                    val description = extractScalar("description")
                        ?: frontMatter["description"]?.singleOrNull().orEmpty()
                    val summary = extractScalar("summary")
                        ?: frontMatter["summary"]?.singleOrNull().orEmpty()
                    val thumbnail = extractScalar("thumbnail")
                        ?: frontMatter["thumbnail"]?.singleOrNull().orEmpty()
                    val authors = extractAuthors()
                    val readTime = if (fileText.isNotEmpty()) {
                        calculateReadingTime(fileText)
                    } else {
                        frontMatter["readTime"]?.singleOrNull().orEmpty()
                    }
                    val statusRaw = (extractScalar("status") ?: frontMatter["status"]?.singleOrNull()).orEmpty().lowercase()
                    val tags = frontMatter["tags"].orEmpty()
                    Row(
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

            val enRows = rows
                .filter { it.locale == "en-CA" }
                .sortedByDescending { it.date.toSortableDate() }

            val ptRowsBySlug = rows
                .filter { it.locale == "pt-BR" }
                .associateBy { it.slug }

            val ptRows = enRows.map { enRow ->
                ptRowsBySlug[enRow.slug] ?: enRow
            }

            fun StringBuilder.appendArticleList(listName: String, listRows: List<Row>) {
                appendLine("    val $listName: List<Article> = listOf(")
                listRows.forEach { row ->
                    val tagsLiteral = row.tags.joinToString(separator = ", ") { "\"${it}\"" }
                    val authorsLiteral = if (row.authors.isEmpty()) {
                        "emptyList()"
                    } else {
                        "listOf(" + row.authors.joinToString(separator = ", ") {
                            "ArticleAuthor(name = \"${it.name.escapeForKotlin()}\", avatar = \"${it.avatar.escapeForKotlin()}\")"
                        } + ")"
                    }
                    appendLine("        Article(")
                    appendLine("            slug = \"${row.slug}\",")
                    appendLine("            date = \"${row.date.escapeForKotlin()}\",")
                    appendLine("            title = \"${row.title.escapeForKotlin()}\",")
                    appendLine("            description = \"${row.description.escapeForKotlin()}\",")
                    appendLine("            tags = listOf($tagsLiteral),")
                    appendLine("            readTime = \"${row.readTime.escapeForKotlin()}\",")
                    appendLine("            status = ArticleStatus.${row.status},")
                    appendLine("            summary = \"${row.summary.escapeForKotlin()}\",")
                    appendLine("            authors = $authorsLiteral,")
                    appendLine("            thumbnail = \"${row.thumbnail.escapeForKotlin()}\",")
                    appendLine("        ),")
                }
                appendLine("    )")
            }

            val content = buildString {
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
    doLast {
        val genDir = getGenDir().get().asFile
        if (genDir.exists()) {
            val localizedArticleRegex = Regex("""@Page\("/articles/(.+)-pt-br"\)""")
            genDir.walkTopDown().filter { it.isFile && it.extension == "kt" }.forEach { file ->
                var text = file.readText()
                val builderStart = "FrontMatterElement.Builder {"
                val startIndex = text.indexOf(builderStart)
                if (startIndex != -1) {
                    val endIndex = text.indexOf("})", startIndex)
                    if (endIndex != -1) {
                        val prefix = text.substring(0, startIndex + builderStart.length)
                        val builderContent = text.substring(startIndex + builderStart.length, endIndex)
                        val suffix = text.substring(endIndex)
                        val fixedBuilder = buildString {
                            var inString = false
                            var isEscaped = false
                            for (i in builderContent.indices) {
                                val c = builderContent[i]
                                if (isEscaped) {
                                    append(c)
                                    isEscaped = false
                                } else if (c == '\\') {
                                    append(c)
                                    isEscaped = true
                                } else if (c == '"') {
                                    append(c)
                                    inString = !inString
                                } else if (inString && (c == '\n' || c == '\r')) {
                                    if (c == '\n') append("\\n")
                                } else {
                                    append(c)
                                }
                            }
                        }
                        text = prefix + fixedBuilder + suffix
                    }
                }
                if (localizedArticleRegex.containsMatchIn(text)) {
                    text = localizedArticleRegex.replace(text) { matchResult ->
                        val slug = matchResult.groupValues[1]
                        """@Page("/articles/pt-br/$slug")"""
                    }
                }
                file.writeText(text)
            }
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
