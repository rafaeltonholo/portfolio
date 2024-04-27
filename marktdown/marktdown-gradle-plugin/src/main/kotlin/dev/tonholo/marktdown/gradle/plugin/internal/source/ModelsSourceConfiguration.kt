package dev.tonholo.marktdown.gradle.plugin.internal.source

import dev.tonholo.marktdown.gradle.plugin.internal.file.HasOrigin
import javax.inject.Inject
import org.gradle.api.file.Directory
import org.gradle.api.tasks.Internal

abstract class ModelsSourceConfiguration @Inject constructor() : SourceConfiguration, HasOrigin {
    @Internal
    final override var origin: Directory? = null
        private set

    @Internal
    final override var disabledGeneration: Boolean = false

    override fun srcDir(dir: Directory) {
        origin = dir
    }

    override fun disableGeneration() {
        disabledGeneration = true
    }
}
