package dev.tonholo.marktdown.gradle.plugin.dsl

import dev.tonholo.marktdown.gradle.plugin.internal.source.ModelsSourceConfiguration
import dev.tonholo.marktdown.gradle.plugin.internal.source.RendererSourceConfiguration
import dev.tonholo.marktdown.gradle.plugin.tasks.MarktdownProcessorTask
import dev.tonholo.marktdown.gradle.plugin.tasks.MarktdownRendererTask
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Nested

/**
 * Extension for the Markdown Gradle plugin.
 *
 * @param project The Gradle project.
 */
@MarktdownGradlePluginDsl
abstract class MarktdownExtension(
    private val project: Project
) {
    /**
     * The package name for the generated Markdown models and renderers.
     */
    var packageName: String? = null

    /**
     * Configuration for the Markdown models.
     */
    @get:Internal
    @get:Nested
    abstract val models: ModelsSourceConfiguration

    /**
     * Configuration for the Markdown renderers.
     */
    @get:Internal
    @get:Nested
    abstract val renderers: RendererSourceConfiguration

    /**
     * Configures the Markdown models using an Action.
     *
     * @param configure The configuration action.
     * @return The configured models.
     */
    fun models(configure: Action<ModelsSourceConfiguration>): ModelsSourceConfiguration =
        models { configure.execute(this) }

    /**
     * Configures the Markdown renderers using an Action.
     *
     * @param configure The configuration action.
     * @return The configured renderers.
     */
    fun renderers(configure: Action<RendererSourceConfiguration>): RendererSourceConfiguration =
        renderers { configure.execute(this) }

    override fun toString(): String {
        return "MarktdownExtension(packageName='$packageName')"
    }

    /**
     * Configures the Markdown renderers.
     *
     * @param configure The configuration action.
     * @return The configured renderers.
     */
    private fun renderers(configure: RendererSourceConfiguration.() -> Unit): RendererSourceConfiguration = renderers
        .apply(configure)

    /**
     * Configures the Markdown models.
     *
     * @param configure The configuration action.
     * @return The configured models.
     */
    private fun models(configure: ModelsSourceConfiguration.() -> Unit): ModelsSourceConfiguration = models
        .apply(configure)
}

/**
 * Applies the Markdown extension to a MarkdownProcessorTask.
 *
 * @param extension The Markdown extension.
 */
internal fun MarktdownProcessorTask.with(extension: MarktdownExtension) {
    check(!extension.packageName.isNullOrBlank()) {
        "The packageName must be provided"
    }
    packageName = extension.packageName!!
    if (extension.models.disabledGeneration.not() && extension.models.origin == null) {
        error("The models' origin must be provided when the Markdown models generation is enabled")
    }

    models = extension.models
    modelsDir.set(extension.models.origin)
}

/**
 * Applies the Markdown extension to a MarkdownRendererTask.
 *
 * @param extension The Markdown extension.
 */
internal fun MarktdownRendererTask.with(extension: MarktdownExtension) {
    // TODO: improve error messages by showing what is the expected way to configure the plugin.
    check(!extension.packageName.isNullOrBlank()) {
        "The packageName must be provided"
    }
    packageName = extension.packageName!!

    if (extension.models.disabledGeneration && extension.models.origin == null) {
        error("The models' origin must be provided when the Markdown models generation is disabled")
    }

    if (extension.models.disabledGeneration && extension.renderers.modelsContainerProject == null) {
        error("The modelsContainerProject must be provided when the Markdown models generation is disabled")
    }

    modelsOriginPath = extension.models.origin.takeIf {
        extension.models.disabledGeneration
    }?.asFile?.toPath()
    modelsContainerProject = extension.renderers.modelsContainerProject ?: project
}
