import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
}

group = "dev.tonholo.portfolio"
version = "1.0-SNAPSHOT"

kotlin {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    applyDefaultHierarchyTemplate {
        common {
            group("web") {
                withJs()
                withWasmJs()
            }
        }
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "portfolio-showcase"
        useEsModules()
        browser {
            commonWebpackConfig {
                outputFileName = "portfolio-showcase.wasm.js"
//                @OptIn(ExperimentalDistributionDsl::class)
//                distribution {
//                    outputDirectory.set(File("../site/src/jsMain/resources/public/showcase"))
//                }
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(project.projectDir.path)
                    }
                    port = 3000
                }
            }
        }
        binaries.executable()
    }

    js {
        moduleName = "portfolio-showcase"
        useEsModules()
        browser {
            commonWebpackConfig {
                outputFileName = "portfolio-showcase.js"
            }
        }
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
        }
    }
}

val buildWebApp by tasks.creating(Copy::class) {
    val wasmWebpack = "wasmJsBrowserDistribution"
    val jsWebpack = "jsBrowserDistribution"
    dependsOn(wasmWebpack, jsWebpack)

    val wasmJsProductionExecutableCompileSync = tasks.named("wasmJsProductionExecutableCompileSync").get()
    val jsWebpackTask = tasks.named(jsWebpack).get()
    wasmJsProductionExecutableCompileSync.mustRunAfter(jsWebpackTask)

    from(jsWebpackTask.outputs.files)
    from(tasks.named(wasmWebpack).get().outputs.files)

    into("../site/src/jsMain/resources/public/showcase")

    duplicatesStrategy = DuplicatesStrategy.INCLUDE

}
