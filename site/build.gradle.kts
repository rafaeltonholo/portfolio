import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrains.compose)
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

kobweb {
    app {
        index {
            description.set("Rafael Tonholo - Android Engineer")
        }
    }
}

kotlin {
    configAsKobwebApplication("portfolio")

    sourceSets {
        jsMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.html.core)
            implementation(libs.compose.components.resources)
            implementation(libs.kobweb.core)
            implementation(libs.kobweb.silk)
            implementation(libs.kobwebx.markdown)
        }
        jsTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "dev.tonholo.portfolio.resources"
    generateResClass = always
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

dependencies {
    detektPlugins(libs.detekt.rules.ktlint.wrapper)
    detektPlugins(libs.compose.rules.detekt)
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

    inputs.dir(pagesDir)
    inputs.file(appPropertiesFile)
    inputs.property("lastModified", lastModified)
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
            .sorted()
            .toList()

        val sitemap = buildString {
            appendLine($$"""<?xml version="1.0" encoding="UTF-8"?>""")
            appendLine($$"""<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">""")
            for (path in pagePaths) {
                val (changefreq, priority) = pageMetadata[path] ?: ("monthly" to "0.5")
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
