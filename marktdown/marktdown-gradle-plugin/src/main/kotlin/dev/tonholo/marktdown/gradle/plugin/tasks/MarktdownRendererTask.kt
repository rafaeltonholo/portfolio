package dev.tonholo.marktdown.gradle.plugin.tasks

import dev.tonholo.marktdown.domain.content.MarktdownElement
import dev.tonholo.marktdown.gradle.plugin.MarktdownExtension
import dev.tonholo.marktdown.gradle.plugin.apply
import dev.tonholo.marktdown.processor.renderer.compose.html.MarktdownElementRendererCreator
import java.nio.file.Path
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskContainer
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteRecursively
import kotlin.io.path.useDirectoryEntries
import kotlin.io.path.useLines
import kotlin.reflect.KClass

abstract class MarktdownRendererTask : DefaultTask() {
    @get:OutputDirectory
    val rootOutputDirectory: Path
        get() = project.layout.buildDirectory
            .dir("generated/marktdown/kotlin/jsMain")
            .get()
            .asFile
            .toPath()

    @get:Input
    lateinit var packageName: String

    init {
        group = "MarKTdown"
        description = "Generate MarktdownElement renderer files"
    }

    @OptIn(ExperimentalPathApi::class)
    @TaskAction
    fun generateRenderers() {
        val pkg = packageName.replace(".", "/")
        val modelsPath = project.layout.buildDirectory
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
            packageName = packageName,
            elementsToRender = elementsToRender.toSet(),
        ).create(output = rootOutputDirectory)
    }
}

context(Project)
fun TaskContainer.registerMarktdownRendererTask(
    extension: MarktdownExtension,
) {
    val kmpExtension = extensions.getByType(KotlinMultiplatformExtension::class.java)
    kmpExtension
        .targets
        .firstOrNull { it.platformType == KotlinPlatformType.js }
        ?.compilations
        ?.first { it.platformType == KotlinPlatformType.js }
        ?.defaultSourceSet
        ?.let { sourceSet ->
            val task = register<MarktdownRendererTask>(
                "generateJsMainMarktdownElementsRender",
            ) {
                apply(extension)
                dependsOn(tasks.withType<MarktdownProcessorTask>())
            }
            val outputDir = task.map { it.rootOutputDirectory }
            sourceSet.kotlin.srcDirs(outputDir)
        }
}
