package dev.tonholo.marktdown.gradle.plugin.tasks

import dev.tonholo.marktdown.domain.content.MarktdownElement
import dev.tonholo.marktdown.gradle.plugin.dsl.MarktdownExtension
import dev.tonholo.marktdown.gradle.plugin.dsl.apply
import dev.tonholo.marktdown.processor.renderer.MarktdownElementRendererCreator
import dev.tonholo.marktdown.processor.renderer.compose.html.ComposeHtmlRendererGenerator
import java.nio.file.Path
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteRecursively
import kotlin.io.path.useDirectoryEntries
import kotlin.io.path.useLines
import kotlin.reflect.KClass

/**
 * Task for generating MarkdownElement renderer files.
 */
abstract class MarktdownRendererTask : DefaultTask() {
    /**
     * The root output directory for the generated renderers files.
     *
     * This is used to set the source directories for the generated Kotlin
     * files into the project source set.
     *
     * TODO: consider accepting others kmp source sets instead of only jsMain.
     */
    @get:OutputDirectory
    val rootOutputDirectory: Path
        get() = project.layout.buildDirectory
            .dir("generated/marktdown/kotlin/jsMain")
            .get()
            .asFile
            .toPath()

    /**
     * The package name for the generated renderers files.
     */
    @get:Input
    lateinit var packageName: String

    /**
     * The path to the models project.
     *
     * If not set, the project's build directory is used.
     * If markdown models generation is disabled, this property is required
     * and not null.
     */
    @get:Internal
    var modelsOriginPath: Path? = null

    /**
     * The project containing the Markdown models.
     *
     * This is used to create a dependency of this task on the models' generation task.
     */
    // TODO: Figure out a better way to set a dependency to the models project
    @Internal
    lateinit var modelsContainerProject: Project

    init {
        group = "MarKTdown"
        description = "Generate MarktdownElement renderer files"
    }

    /**
     * Generates the MarkdownElement renderers.
     */
    @OptIn(ExperimentalPathApi::class)
    @TaskAction
    fun generateRenderers() {
        val pkg = packageName.replace(".", "/")
        val modelsPath = modelsOriginPath?.let { Path("$it/$pkg") }
            ?: project.layout.buildDirectory
                .dir("generated/marktdown/kotlin/commonMain/$pkg")
                .get()
                .asFile
                .toPath()

        val basePath = Path("${rootOutputDirectory}/${pkg}")
        val rendererPath = Path(
            "${basePath}/renderer",
        )
        rendererPath.deleteRecursively()
        rendererPath.createDirectories()
        val elementsToRender = mutableSetOf<KClass<out MarktdownElement>>()
        modelsPath.useDirectoryEntries("*.kt") { paths ->
            for (path in paths) {
                println("path = $path")
                path.useLines { lines ->
                    lines
                        .filter {
                            it.startsWith("import ") ||
                                MarktdownElementRendererCreator.marktdownElements.keys.any { key ->
                                    key in it
                                }
                        }
                        .map {
                            if (it.startsWith("import ")) {
                                it.removePrefix("import ")
                            } else {
                                MarktdownElementRendererCreator.marktdownElements.keys.first { key ->
                                    key in it
                                }
                            }
                        }
                        .mapNotNull { MarktdownElementRendererCreator.marktdownElements[it] }
                        .toList()
                }.let(elementsToRender::addAll)
            }
        }

        MarktdownElementRendererCreator(
            rendererGenerator = ComposeHtmlRendererGenerator(
                packageName = packageName,
                elementsToRender = elementsToRender.toSet(),
            ),
        ).create(output = rootOutputDirectory)
    }
}

/**
 * Registers the Markdown renderer task for a project.
 *
 * @param extension The Markdown extension.
 * @return The task provider for the Markdown renderer task.
 */
context(Project)
fun TaskContainer.registerMarktdownRendererTask(
    extension: MarktdownExtension,
): TaskProvider<MarktdownRendererTask>? =
    extensions.getByType(KotlinMultiplatformExtension::class.java)
        .takeIf { extension.renderers.disabledGeneration.not() }
        ?.targets
        ?.firstOrNull { it.platformType == KotlinPlatformType.js }
        ?.compilations
        ?.first { it.platformType == KotlinPlatformType.js }
        ?.defaultSourceSet
        ?.let { sourceSet ->
            val task = register<MarktdownRendererTask>(
                "generateJsMainMarktdownElementsRender",
            ) {
                apply(extension)

                val modelsProject = rootProject.allprojects.first {
                    it.name == modelsContainerProject.name
                }
                val processorTask = modelsProject.tasks.findByName(MarktdownProcessorTask.TASK_NAME)
                processorTask?.let {
                    dependsOn(it)
                }

                tasks.findByName("kspJs")?.mustRunAfter(this)
            }
            tasks.withType<KotlinJsCompile>().configureEach {
                dependsOn(task)
            }
            val outputDir = task.map { it.rootOutputDirectory }
            sourceSet.kotlin.srcDirs(outputDir)
            task
        }