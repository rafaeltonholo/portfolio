plugins {
    dev.tonholo.publishing
    org.jetbrains.kotlin.multiplatform
    alias(libs.plugins.org.jetbrains.compose)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.compose)
}

kotlin {
    jvm()
    js {
        browser()
        nodejs()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(libs.org.jetbrains.kotlinx.datetime)
            api(libs.org.jetbrains.markdown)
            implementation(projects.marktdownCore)
        }

        jvmMain.dependencies {
            implementation(kotlin("reflect"))
            implementation(libs.bundles.org.commonmark)
            implementation(libs.com.squareup.kotlinpoet)
            implementation(libs.org.yaml.snakeyaml)
            // Get access to Kotlin multiplatform source sets
            implementation(kotlin("gradle-plugin"))
            implementation(libs.com.google.devtools.ksp.symbol.processing.api)
        }
    }

    targets.all {
        compilations.all {
            compilerOptions.options.freeCompilerArgs.add("-Xcontext-receivers")
        }
    }
}
