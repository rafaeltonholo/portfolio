package dev.tonholo.portfolio.buildlogic.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

/**
 * Gradle task that generates a Kotlin object containing Umami analytics constants
 * from the app.properties file.
 */
abstract class GenerateBuildAnalyticsTask : DefaultTask() {
    @get:InputFile
    abstract val appPropertiesFile: RegularFileProperty

    @get:Input
    abstract val scriptUrl: Property<String>

    @get:Input
    abstract val websiteId: Property<String>

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun execute() {
        val outputFile: File = outputDirectory.file("dev/tonholo/portfolio/analytics/BuildAnalytics.kt").get().asFile
        outputFile.parentFile.mkdirs()
        outputFile.writeText(
            """
            |package dev.tonholo.portfolio.analytics
            |
            |internal object BuildAnalytics {
            |    const val UMAMI_SCRIPT_URL: String = "${scriptUrl.get()}"
            |    const val UMAMI_WEBSITE_ID: String = "${websiteId.get()}"
            |}
            |
            """.trimMargin(),
        )
    }
}
