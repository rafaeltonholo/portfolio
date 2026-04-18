import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication
import kotlinx.html.link
import java.util.Properties

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

// Dynamic-route slugs registered with the Kobweb static exporter. Each entry
// in `projectSlugs` and `articleSlugs` maps to a `@Page("/projects/{slug}")` or
// `@Page("/articles/{slug}")` composable; the exporter needs an explicit URL
// + destination HTML file for each known value to produce static pages.
val projectSlugs = listOf(
    "thunderbird",
    "bleacher-report",
    "pinterest",
    "svg-compose",
    "ivy-wallet",
)
val articleSlugs = listOf("a1", "a2", "a3", "a4", "a5", "a6", "a7")

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
                link {
                    rel = "stylesheet"
                    href = "https://unpkg.com/@phosphor-icons/web@2.1.2/src/regular/style.css"
                }
                link {
                    rel = "stylesheet"
                    href = "https://unpkg.com/@phosphor-icons/web@2.1.2/src/fill/style.css"
                }
                link {
                    rel = "stylesheet"
                    href = "https://unpkg.com/@phosphor-icons/web@2.1.2/src/duotone/style.css"
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
            projectSlugs.forEach { slug ->
                addExtraRoute(route = "/projects/$slug", exportPath = "projects/$slug.html")
            }
            articleSlugs.forEach { slug ->
                addExtraRoute(route = "/articles/$slug", exportPath = "articles/$slug.html")
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
                |    const val UMAMI_SCRIPT_URL: String = "${scriptUrl}"
                |    const val UMAMI_WEBSITE_ID: String = "${websiteId}"
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

    val extraSlugs = projectSlugs.map { "/projects/$it" } + articleSlugs.map { "/articles/$it" }

    inputs.dir(pagesDir)
    inputs.file(appPropertiesFile)
    inputs.property("lastModified", lastModified)
    inputs.property("extraSlugs", extraSlugs.joinToString(","))
    outputs.file(outputFile)

    doLast {
        val pagePaths = pagesDir.asFile.walkTopDown()
            .filter { it.isFile && it.extension == "kt" }
            .filter { file -> file.readText().contains("@Page") }
            .map { file ->
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
                                    $$"${m.groupValues[1]}-${m.groupValues[2]}"
                                }
                                .lowercase()
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
