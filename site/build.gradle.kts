import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication
import kotlinx.html.link
import kotlinx.html.meta

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.compose)
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
                    href = "/fonts/faces.css"
                }
            }
            description.set("Powered by Kobweb")
        }

        export {
            addExtraRoute("/en/articles/hello-world", "/en/articles/hello-world.html")
            addExtraRoute("/pt-BR/articles/hello-world", "/pt-BR/articles/hello-world.html")
            addExtraRoute("/project/pinterest", "/project/pinterest.html")
        }
    }
}

kotlin {
    configAsKobwebApplication("portfolio")
    js {
        useEsModules()
    }

    targets.all {
        compilations.all {
            compileTaskProvider.configure{
                compilerOptions {
                    freeCompilerArgs.add("-Xexpect-actual-classes")
                }
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core)
            implementation(compose.runtime)
            implementation(projects.resources)
            implementation(libs.kotlinx.datetime)
        }

        jsMain.dependencies {
            implementation(compose.html.core)
            implementation(libs.kobweb.core)
            implementation(libs.kobweb.silk)
            implementation(libs.silk.icons.mdi)
            implementation(libs.dev.tonholo.kotlin.wrapper.shiki.core)
            implementation(libs.dev.tonholo.kotlin.wrapper.shiki.compose.html)
            implementation(libs.dev.tonholo.marktdown.core)
            implementation(npm(name = "shiki", version = "1.5.1"))

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
