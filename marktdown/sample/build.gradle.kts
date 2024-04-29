import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.org.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.org.jetbrains.compose)
    id("dev.tonholo.marktdown") version "+"
    alias(libs.plugins.com.google.devtools.ksp)
}

kotlin {
    js {
        browser {
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
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation("dev.tonholo.marktdown:marktdown-core:1.0.0")
        }
        jsMain.dependencies {
            implementation(compose.html.core)
        }
        jvmMain.dependencies {
            implementation("dev.tonholo.marktdown:marktdown-processor:1.0.0")
            implementation(libs.org.yaml.snakeyaml)
        }
    }
}

marktdown {
    packageName = "dev.tonholo.marktdown.sample"
    models {
        srcDir(layout.projectDirectory.dir("src/jvmMain/resources"))
    }
}

dependencies {
    add("kspJs", "dev.tonholo.marktdown:marktdown-processor:1.0.0")
}
