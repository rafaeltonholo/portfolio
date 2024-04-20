import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.org.jetbrains.kotlin.multiplatform)
    id("dev.tonholo.marktdown") version "+"
}

kotlin {
    js {
        browser()
        nodejs {
            binaries.executable()
        }
    }

    jvm {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        mainRun {
            mainClass.set("MainKt")
        }
    }

    sourceSets {
        commonMain.dependencies {  }
        jsMain.dependencies {  }
        jvmMain.dependencies {
            implementation("dev.tonholo.marktdown:marktdown-processor:1.0.0")
        }
    }
}

marktdown {
    packageName = "dev.tonholo.marktdown.sample"
    resources = layout.projectDirectory.dir("resources")
}

dependencies {
}
