import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kobweb.application)
    alias(libs.plugins.kobwebx.markdown)
    alias(libs.plugins.kotlin.power.assert)
    alias(libs.plugins.detekt)
}

group = "dev.tonholo.portfolio"
version = "0.1.0-SNAPSHOT"

val appProperties = Properties().apply {
    rootProject.rootDir.resolve("app.properties").inputStream().use { load(it) }
}

kobweb {
    app {
        index {
            description.set("Rafael Tonholo - Android Engineer")
        }
    }
}

kotlin {
    configAsKobwebApplication("portfolio")

    sourceSets {
        jsMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.html.core)
            implementation(libs.compose.components.resources)
            implementation(libs.kobweb.core)
            implementation(libs.kobweb.silk)
            implementation(libs.kobwebx.markdown)
        }
        jsTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

powerAssert {
    functions.addAll(
        "kotlin.assert",
        "kotlin.test.assertTrue",
        "kotlin.test.assertEquals",
        "kotlin.test.assertNull",
    )
}

detekt {
    config.setFrom(rootProject.file("config/detekt.yml"))
    buildUponDefaultConfig = true
    allRules = false
}

dependencies {
    detektPlugins(libs.detekt.rules.ktlint.wrapper)
    detektPlugins(libs.compose.rules.detekt)
}
