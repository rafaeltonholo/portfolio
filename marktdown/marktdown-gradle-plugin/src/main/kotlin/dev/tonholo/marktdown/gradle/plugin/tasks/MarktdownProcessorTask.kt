package dev.tonholo.marktdown.gradle.plugin.tasks

import dev.tonholo.marktdown.gradle.plugin.domain.asLogger
import dev.tonholo.marktdown.gradle.plugin.dsl.MarktdownExtension
import dev.tonholo.marktdown.gradle.plugin.dsl.with
import dev.tonholo.marktdown.gradle.plugin.internal.source.ModelsSourceConfiguration
import dev.tonholo.marktdown.processor.MarktdownProcessor
import java.io.File
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.work.Incremental
import org.gradle.work.InputChanges
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

/**
 * Task for processing Markdown files.
 */
abstract class MarktdownProcessorTask : DefaultTask() {
    companion object {
        const val TASK_NAME = "processMarkdownFiles"
    }

    /**
     * The root output directory for the generated markdown models files.
     *
     * This is used to set the source directories for the generated Kotlin
     * files into the project source set.
     */
    @get:OutputDirectory
    val rootOutputDirectory: File
        get() = project.layout.buildDirectory
            .dir("generated/marktdown/kotlin/commonMain")
            .get()
            .asFile

    /**
     * The output directory for the generated files.
     */
    @get:Internal
    val outputDirectory
        get() = File(
            "${rootOutputDirectory.absolutePath}/${packageName.replace(".", "/")}",
        )

    /**
     * The package name for the generated markdown models files.
     */
    @get:Input
    lateinit var packageName: String

    /**
     * The source configuration for the generated markdown models files.
     */
    @get:Nested
    lateinit var models: ModelsSourceConfiguration

    @get:Incremental
    @get:InputDirectory
    val modelsDir: DirectoryProperty = project.objects.directoryProperty()

    init {
        group = "MarKTdown"
        description = "Process markdown files"
    }

    /**
     * Processes the Markdown files.
     */
    @TaskAction
    fun processMarkdownFiles(inputChanges: InputChanges) {
        println(
            if (inputChanges.isIncremental) "Executing incrementally"
            else "Executing non-incrementally"
        )
        logger.debug("Cleaning up generated files")
        rootOutputDirectory.parentFile.deleteRecursively()
        logger.debug("Creating output directory for generated files")
        logger.debug("output = {}", outputDirectory.absolutePath)
        outputDirectory.mkdirs()
        val mdExtension = "md"

        val processor = MarktdownProcessor(logger.asLogger())
        models.origin?.asFile?.listFiles()
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

/**
 * Registers the Markdown processor task for a project.
 *
 * @param extension The Markdown extension.
 * @return The task provider for the Markdown processor task.
 */
context(Project)
fun TaskContainer.registerMarktdownProcessorTask(
    extension: MarktdownExtension,
): TaskProvider<MarktdownProcessorTask>? = extensions
    .takeIf { extension.models.disabledGeneration.not() }
    ?.getByType(KotlinMultiplatformExtension::class.java)
    ?.let { kmpExtension ->
        val commonTarget = kmpExtension.targets
            .first { it.platformType == KotlinPlatformType.common }

        val commonMainSourceSet = commonTarget.compilations
            .first { it.platformType == KotlinPlatformType.common }
            .defaultSourceSet

        logger.debug("models.origin = {}", extension.models.origin)

        val task = tasks.register<MarktdownProcessorTask>(
            MarktdownProcessorTask.TASK_NAME,
        ) {
            with(extension)
            finalizedBy(tasks.withType<MarktdownRendererTask>())
        }

        tasks.withType<KotlinCommonCompile>().configureEach {
            dependsOn(task)
        }

        val outputDir = task.map { it.rootOutputDirectory }
        commonMainSourceSet.kotlin.srcDirs(outputDir)
        task
    }
