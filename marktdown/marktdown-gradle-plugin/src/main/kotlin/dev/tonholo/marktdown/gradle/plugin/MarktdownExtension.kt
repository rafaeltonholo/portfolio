package dev.tonholo.marktdown.gradle.plugin

import dev.tonholo.marktdown.gradle.plugin.tasks.MarktdownProcessorTask
import dev.tonholo.marktdown.gradle.plugin.tasks.MarktdownRendererTask
import org.gradle.api.Project
import org.gradle.api.file.Directory

abstract class MarktdownExtension(
    project: Project,
) {
    var packageName: String? = null
    abstract var resources: Directory
    override fun toString(): String {
        return "MarktdownExtension(packageName='$packageName', resources=$resources)"
    }
}

internal fun MarktdownProcessorTask.apply(extension: MarktdownExtension) {
    check(!extension.packageName.isNullOrBlank()) {
        "The packageName must be provided"
    }
    packageName = extension.packageName!!
    resources = extension.resources
}

internal fun MarktdownRendererTask.apply(extension: MarktdownExtension) {
    check(!extension.packageName.isNullOrBlank()) {
        "The packageName must be provided"
    }
    packageName = extension.packageName!!
}
