plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.compose)
    alias(libs.plugins.kobweb.library)
    alias(libs.plugins.dev.tonholo.marktdown)
    alias(libs.plugins.com.google.devtools.ksp)
}

kotlin {
    js {
        browser()
        useEsModules()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core)
            api(libs.lyricist)
            implementation(compose.ui)
            implementation(libs.kotlinx.datetime)
            implementation(libs.dev.tonholo.marktdown.core)
        }
        jsMain.dependencies {
            implementation(compose.html.core)
        }
    }
}

marktdown {
    packageName = "dev.tonholo.portfolio"

    models {
        srcDir(layout.projectDirectory.dir("markdown"))
    }
    renderers {
        disableGeneration()
    }
}

dependencies {
    add("kspJs", libs.lyricist.processor)
}
