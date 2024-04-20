package dev.tonholo.marktdown.gradle.plugin

import dev.tonholo.marktdown.gradle.plugin.tasks.MarktdownProcessorTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

class MarktdownPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create(
            "marktdown",
            MarktdownExtension::class.java,
            project,
        )

        project.configure(extension)
    }

    private fun Project.configure(extension: MarktdownExtension) {
        val kmpExtension = extensions.getByType(KotlinMultiplatformExtension::class.java)
        afterEvaluate {
            val commonTarget = kmpExtension.targets
                .first { it.platformType == KotlinPlatformType.common }

            val commonMainSourceSet = commonTarget.compilations
                .first { it.platformType == KotlinPlatformType.common}
                .defaultSourceSet

            val task = tasks.register<MarktdownProcessorTask>(
                "processMarkdownFiles",
            ) {
                apply(extension)
            }

            val outputDir = task.map { it.rootOutputDirectory }
            commonMainSourceSet.kotlin.srcDirs(outputDir)
        }
    }
}
