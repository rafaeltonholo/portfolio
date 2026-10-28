import dev.tonholo.portfolio.buildlogic.tasks.GenerateBuildAnalyticsTask
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import java.io.File
import java.util.Properties

plugins {
    id("org.jetbrains.kotlin.multiplatform")
}

val resolvedAppPropertiesFile: File = rootProject.rootDir.resolve("app.properties")
val appProperties = Properties().apply {
    if (resolvedAppPropertiesFile.exists()) {
        resolvedAppPropertiesFile.inputStream().use { load(it) }
    }
}

val generatedAnalyticsSrcDir = layout.buildDirectory.dir("generated/analytics/kotlin")

val generateBuildAnalytics = tasks.register<GenerateBuildAnalyticsTask>("generateBuildAnalytics") {
    description = "Generates a Kotlin object containing the Umami script URL and website ID from app.properties."
    this.appPropertiesFile.set(resolvedAppPropertiesFile)
    this.scriptUrl.set(appProperties.getProperty("UMAMI_SCRIPT_URL").orEmpty())
    this.websiteId.set(appProperties.getProperty("UMAMI_WEBSITE_ID").orEmpty())
    this.outputDirectory.set(generatedAnalyticsSrcDir)
}

extensions.configure<KotlinMultiplatformExtension> {
    sourceSets.matching { it.name == "jsMain" }.configureEach {
        kotlin.srcDir(generatedAnalyticsSrcDir)
    }
}

tasks.configureEach {
    if (name == "compileKotlinJs" || name == "kspKotlinJs") {
        dependsOn(generateBuildAnalytics)
    }
}
