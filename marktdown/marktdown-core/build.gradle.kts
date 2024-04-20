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
        }
    }
}
