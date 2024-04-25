plugins {
    dev.tonholo.publishing
    org.jetbrains.kotlin.multiplatform
    alias(libs.plugins.org.jetbrains.compose)
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
        }
    }

    targets.all {
        compilations.all {
            compilerOptions.options.freeCompilerArgs.add("-Xcontext-receivers")
        }
    }
}
