import com.varabyte.kobweb.gradle.application.extensions.AppBlock.LegacyRouteRedirectStrategy
import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication
import kotlinx.html.link
import kotlinx.html.meta

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kobweb.application)
    alias(libs.plugins.dev.tonholo.marktdown)
    alias(libs.plugins.com.google.devtools.ksp)
}

group = "dev.tonholo.portfolio"
version = "1.0-SNAPSHOT"

kobweb {
    app {
        index {
            head.add {
                meta {
                    name = "viewport"
                    content = "width=device-width,initial-scale=1"
                }
                link {
                    rel = "stylesheet"
                    href = "fonts/faces.css"
                }
            }
            description.set("Powered by Kobweb")
        }

        // Only legacy sites need this set. Sites built after 0.16.0 should default to DISALLOW.
        // See https://github.com/varabyte/kobweb#legacy-routes for more information.
        legacyRouteRedirectStrategy.set(LegacyRouteRedirectStrategy.DISALLOW)
    }
}

kotlin {
    configAsKobwebApplication("portfolio")

    targets.all {
        compilations.all {
            compilerOptions.configure {
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(projects.resources)
            implementation(libs.kotlinx.datetime)
        }

        jsMain.dependencies {
            implementation(compose.html.core)
            implementation(libs.kobweb.core)
            implementation(libs.kobweb.silk)
            implementation(libs.silk.icons.fa)
            implementation(libs.dev.tonholo.kotlin.wrapper.highlightjs.core)
            implementation(libs.dev.tonholo.kotlin.wrapper.highlightjs.compose.html)
            implementation(libs.dev.tonholo.marktdown.core)
        }
    }
}

marktdown {
    packageName = group.toString()
    models {
        disableGeneration()
        srcDir(layout.projectDirectory.dir("../resources/build/generated/marktdown/kotlin/commonMain"))
    }
    renderers {
        modelsContainerProject = projects.resources.dependencyProject
    }
}

dependencies {
    add("kspJs", libs.dev.tonholo.marktdown.processor)
}

tasks.withType<org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrLink> {
    compilerOptions.moduleKind.set(org.jetbrains.kotlin.gradle.dsl.JsModuleKind.MODULE_COMMONJS)
}
