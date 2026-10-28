import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication
import dev.tonholo.portfolio.buildlogic.SlugUtils
import dev.tonholo.portfolio.buildlogic.splitLocalizedSlug
import kotlinx.html.link
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kobweb.application)
    alias(libs.plugins.kotlin.power.assert)
    id("portfolio.analytics")
    id("portfolio.sitemap")
    id("portfolio.detekt")
    id("portfolio.markdown")
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

dependencies {
    detektPlugins(libs.detekt.rules.ktlint.wrapper)
    detektPlugins(libs.compose.rules.detekt)
}
