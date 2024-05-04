package dev.tonholo.marktdown.processor

import dev.tonholo.marktdown.processor.visitor.MarktdownRenderer
import java.nio.file.Path
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser
import org.yaml.snakeyaml.Yaml
import kotlin.io.path.nameWithoutExtension
import kotlin.io.path.useLines
import kotlin.math.log

class MarktdownProcessor(
    private val logger: Logger,
) {
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
        logger.debug(
            "process() called with: packageName = {}, fileName = {}, content = {}, output = {}",
            packageName,
            fileName,
            content,
            output,
        )
        val regex = Regex("^-{3,}([\\n\\w: -/]+)-{3,}")
        val matchResult = regex
            .findAll(content, startIndex = 0)
            .map { it.groupValues[1].trim() }
            .toList()

        val frontMatterMetadata = if (matchResult.isNotEmpty()) {
            val frontMatter = matchResult.first()
            logger.debug("front matter: {}", frontMatter)
            val yaml = Yaml()
            val map = yaml.load<Map<String, Any>>(frontMatter)
            logger.debug("frontMatter map: {}", frontMatter)
            map
        } else {
            logger.debug("no front matter.")
            null
        }

        val noFrontMatterContent = regex.replace(content, "").trimStart()
        val flavour = GFMFlavourDescriptor()
        val document = MarkdownParser(flavour)
            .buildMarkdownTreeFromString(noFrontMatterContent)

        val renderer = MarktdownRenderer(
            logger = logger,
            packageName = packageName,
            fileName = fileName,
            content = noFrontMatterContent,
            frontMatterMetadata = frontMatterMetadata,
        )

        renderer.render(node = document, output)
    }
}
