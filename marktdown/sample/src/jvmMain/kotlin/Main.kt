import dev.tonholo.marktdown.processor.visitor.MarktdownRenderer
import java.io.BufferedReader
import java.io.InputStreamReader
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser
import org.yaml.snakeyaml.Yaml

fun main() {
//    val content = File(ClassLoader.getSystemResource("test.md").toURI())
//    println("content = $content")
//    val output = File("resources").toPath()
//    MarktdownProcessor().process(
//        packageName = "dev.tonholo.marktdown.sample",
//        input = content.toPath(),
//        output = output
//    )
    val content = buildString {
        ClassLoader.getSystemResourceAsStream("test.md")?.use {
            val streamReader = InputStreamReader(it)
            BufferedReader(streamReader).useLines { lines ->
                lines.forEach { line ->
                    appendLine(line)
                }
            }
        }
    }

    val regex = Regex("^-{3,}([\\n\\w: -/]+)-{3,}")
//    val matchResult = regex.matchAt(content, index = 0)
    val matchResult = regex
        .findAll(content, startIndex = 0)
        .map { it.groupValues[1].trim() }
        .toList()

    val frontMatterMetadata = if (matchResult.isNotEmpty()) {
        val frontMatter = matchResult.first()
        println("front matter: $frontMatter")
        val yaml = Yaml()
        val map = yaml.load<Map<String, Any>>(frontMatter)
        println(map)
        map
    } else {
        println("no front matter.")
        null
    }

    val noFrontMatterContent = regex.replace(content, "").trimStart()
    val flavour = GFMFlavourDescriptor()
    val node = MarkdownParser(flavour)
        .buildMarkdownTreeFromString(noFrontMatterContent)

//    val html = HtmlGenerator(noFrontMatterContent, node, flavour).generateHtml()
//    println()
//    println("---- html ----")
//    println(html)
//    println("---- end html ----")
//    println()

    val parsed = MarktdownRenderer(
        content = noFrontMatterContent,
        packageName = "dev.tonholo.marktdown.sample",
        fileName = "Test.kt",
        frontMatterMetadata = frontMatterMetadata,
    ).render(node)
    println()
    println(parsed)
//    output.useLines { println(it.joinToString("\n")) }
    println("Finished")
}
