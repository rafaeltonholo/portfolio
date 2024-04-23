package dev.tonholo.marktdown.processor.commonMark

import dev.tonholo.marktdown.processor.visitor.commonMark.MarktdownRenderer
import java.nio.file.Path
import org.commonmark.ext.front.matter.YamlFrontMatterExtension
import org.commonmark.ext.gfm.tables.TablesExtension
import org.commonmark.ext.image.attributes.ImageAttributesExtension
import org.commonmark.ext.task.list.items.TaskListItemsExtension
import org.commonmark.parser.Parser
import kotlin.io.path.nameWithoutExtension
import kotlin.io.path.useLines

class MarktdownProcessor {
    fun process(packageName: String, input: Path, output: Path) {
        input.useLines { lines ->
            val content = buildString {
                for (line in lines) {
                    appendLine(line)
                }
            }

            process(
                packageName = packageName,
                fileName = input.nameWithoutExtension,
                content = content,
                output = output,
            )
        }
    }

    private fun process(
        packageName: String,
        fileName: String,
        content: String,
        output: Path,
    ) {
        val parser = Parser
            .builder()
            .extensions(
                listOf(
                    ImageAttributesExtension.create(),
                    TablesExtension.create(),
                    TaskListItemsExtension.create(),
                    YamlFrontMatterExtension.create(),
                )
            )
            .build()
        val document = parser.parse(content)
        val renderer = MarktdownRenderer(packageName, fileName)
        renderer.render(node = document, output)
    }
}
