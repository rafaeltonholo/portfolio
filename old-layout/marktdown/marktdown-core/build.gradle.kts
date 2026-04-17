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
            api(libs.org.jetbrains.kotlinx.datetime)
        }
    }
}
