package dev.tonholo.marktdown.gradle.plugin

import com.google.devtools.ksp.gradle.KspExtension
import dev.tonholo.marktdown.gradle.plugin.dsl.MarktdownExtension
import dev.tonholo.marktdown.gradle.plugin.tasks.registerMarktdownProcessorTask
import dev.tonholo.marktdown.gradle.plugin.tasks.registerMarktdownRendererTask
import dev.tonholo.marktdown.processor.ksp.MarktdownSymbolProcessorProvider
import org.gradle.api.Plugin
import org.gradle.api.Project
import kotlin.io.path.absolutePathString

/**
 * Gradle plugin for the MarKTdown library.
 */
class MarktdownPlugin : Plugin<Project> {
    /**
     * Applies the plugin to the given project.
     *
     * @param project The project to apply the plugin to.
     */
    override fun apply(project: Project) {
        val extension = project.extensions.create(
            "marktdown",
            MarktdownExtension::class.java,
        )

        project.configure(extension)
    }

    /**
     * Configures the project with the given extension.
     *
     * @receiver The project to configure.
     * @param extension The extension to use for configuration.
     */
    private fun Project.configure(extension: MarktdownExtension) {
        afterEvaluate {
            val rendererTask = tasks.registerMarktdownRendererTask(extension)
            val rendererOutputDirectory = rendererTask
                ?.map { it.rootOutputDirectory }
                ?.get()

            val hasProcessor = configurations
                .findByName("kspJs")
                ?.allDependencies
                // TODO: Use Build logic to get group and name
                ?.any { it.group == "dev.tonholo.marktdown" && it.name == "marktdown-processor" } ?: false

            val kspExtension = project.extensions.getByType(KspExtension::class.java)
            if (
                rendererOutputDirectory != null &&
                hasProcessor &&
                !kspExtension.arguments.containsKey(MarktdownSymbolProcessorProvider.ARG_PACKAGE_NAME)
            ) {
                extension.packageName?.let { packageName ->
                    kspExtension.arg(MarktdownSymbolProcessorProvider.ARG_PACKAGE_NAME, packageName)
                    kspExtension.arg(
                        MarktdownSymbolProcessorProvider.ARG_DEFAULT_RENDERER_PATH,
                        rendererOutputDirectory.absolutePathString(),
                    )
                }
            }

            tasks.registerMarktdownProcessorTask(extension)
        }
    }
}
