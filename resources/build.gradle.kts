plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kobweb.library)
    alias(libs.plugins.com.google.devtools.ksp)
}

kotlin {
    js {
        browser()
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.lyricist)
            implementation(compose.ui)
            implementation(libs.kotlinx.datetime)
        }
    }
}

dependencies {
    ksp(libs.lyricist.processor)
}