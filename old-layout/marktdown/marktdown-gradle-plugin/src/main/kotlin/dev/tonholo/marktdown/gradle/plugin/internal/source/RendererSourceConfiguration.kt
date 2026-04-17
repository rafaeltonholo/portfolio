package dev.tonholo.marktdown.gradle.plugin.internal.source

import javax.inject.Inject
import org.gradle.api.Project
import org.gradle.api.tasks.Internal

abstract class RendererSourceConfiguration @Inject constructor() : SourceConfiguration {
    @Internal
    final override var disabledGeneration: Boolean = false

    var modelsContainerProject: Project? = null

    override fun disableGeneration() {
        disabledGeneration = true
    }
}
