package dev.tonholo.marktdown.gradle.plugin.tasks

import dev.tonholo.marktdown.processor.MarktdownProcessor
import java.io.File
import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

abstract class MarktdownProcessorTask : DefaultTask() {
    @get:OutputDirectory
    val rootOutputDirectory: File
        get() = project.layout.buildDirectory
            .dir("generated/marktdown/kotlin/commonMain")
            .get()
            .asFile

    @get:Internal
    val outputDirectory
        get() = File(
            "${rootOutputDirectory.absolutePath}/${packageName.replace(".", "/")}",
        )

    @get:Input
    lateinit var packageName: String

    @InputDirectory
    lateinit var resources: Directory

    init {
        group = "MarKTdown"
        description = "Process markdown files"
    }

    @TaskAction
    fun processMarkdownFiles() {
        println("Cleaning up generated files")
        rootOutputDirectory.parentFile.deleteRecursively()
        println("Creating output directory for generated files")
        println("output = ${outputDirectory.absolutePath}")
        outputDirectory.mkdirs()
        val mdExtension = "md"

        val processor = MarktdownProcessor()
        resources.asFile.listFiles()
            ?.filter { it.extension == mdExtension }
            ?.forEach {
                processor.process(
                    packageName = packageName,
                    input = it.toPath(),
                    output = rootOutputDirectory.toPath(),
                )
            }
    }
}
