package dev.tonholo.portfolio.buildlogic.markdown

import org.commonmark.node.BlockQuote
import org.commonmark.node.FencedCodeBlock
import org.commonmark.node.Link
import org.commonmark.node.Paragraph
import org.commonmark.node.Text
import java.io.File

/**
 * Handlers and post-conversion utilities for Kobweb markdown compilation.
 */
object MarkdownHandlers {
    private val CALLOUT_REGEX: Regex =
        """\[!(NOTE|TIP|IMPORTANT|WARNING|CAUTION|QUOTE)(?:\s+"([^"]*)")?\]""".toRegex(RegexOption.IGNORE_CASE)
    private val LOCALIZED_ARTICLE_REGEX: Regex =
        Regex("""@Page\("/articles/(.+)-pt-br"\)""")

    fun formatCode(codeBlock: FencedCodeBlock): String {
        val rawInfo: String = codeBlock.info.trim()
        val lang: String = rawInfo.split(Regex("\\s+")).firstOrNull()?.ifBlank { "text" } ?: "text"
        val escaped: String = codeBlock.literal
            .replace("$", "\${'$'}")
            .replace("\"\"\"", "\${\"\\\"\\\"\\\"\"}")
        return "dev.tonholo.portfolio.components.atoms.ShikiCodeBlock(code = \"\"\"$escaped\"\"\", language = \"$lang\")"
    }

    fun formatLink(link: Link): String {
        val dest: String = link.destination
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("$", "\${'$'}")
        return "dev.tonholo.portfolio.components.atoms.ArticleLink(href = \"$dest\")"
    }

    fun formatBlockquote(blockQuote: BlockQuote): String {
        val firstParagraph: Paragraph? = blockQuote.firstChild as? Paragraph
        val firstText: Text? = firstParagraph?.firstChild as? Text
        val match: MatchResult? = firstText?.let { CALLOUT_REGEX.find(it.literal) }
        return if (match != null) {
            val typeStr: String = match.groupValues[1].uppercase()
            val customTitle: String? = match.groupValues[2].takeIf { it.isNotBlank() }
            val remaining: String = firstText.literal.substringAfter(match.value).trimStart()
            if (remaining.isEmpty()) {
                firstText.unlink()
                if (firstParagraph.firstChild == null) {
                    firstParagraph.unlink()
                }
            } else {
                firstText.literal = remaining
            }
            val titleParam: String = if (customTitle != null) ", title = \"$customTitle\"" else ""
            "dev.tonholo.portfolio.components.molecules.Callout(type = dev.tonholo.portfolio.components.molecules.CalloutType.$typeStr$titleParam)"
        } else {
            "dev.tonholo.portfolio.components.molecules.Callout(type = dev.tonholo.portfolio.components.molecules.CalloutType.QUOTE)"
        }
    }

    fun sanitizeGeneratedSources(genDir: File) {
        if (!genDir.exists()) return
        val builderStart = "FrontMatterElement.Builder {"
        genDir.walkTopDown().filter { it.isFile && it.extension == "kt" }.forEach { file: File ->
            var text: String = file.readText()
            val startIndex: Int = text.indexOf(builderStart)
            if (startIndex != -1) {
                val endIndex: Int = text.indexOf("})", startIndex)
                if (endIndex != -1) {
                    val prefix: String = text.substring(0, startIndex + builderStart.length)
                    val builderContent: String = text.substring(startIndex + builderStart.length, endIndex)
                    val suffix: String = text.substring(endIndex)
                    val fixedBuilder: String = buildString {
                        var inString = false
                        var isEscaped = false
                        for (i: Int in builderContent.indices) {
                            val c: Char = builderContent[i]
                            if (isEscaped) {
                                append(c)
                                isEscaped = false
                            } else if (c == '\\') {
                                append(c)
                                isEscaped = true
                            } else if (c == '"') {
                                append(c)
                                inString = !inString
                            } else if (inString && (c == '\n' || c == '\r')) {
                                if (c == '\n') append("\\n")
                            } else {
                                append(c)
                            }
                        }
                    }
                    text = prefix + fixedBuilder + suffix
                }
            }
            if (LOCALIZED_ARTICLE_REGEX.containsMatchIn(text)) {
                text = LOCALIZED_ARTICLE_REGEX.replace(text) { matchResult ->
                    val slug: String = matchResult.groupValues[1]
                    """@Page("/articles/pt-br/$slug")"""
                }
            }
            file.writeText(text)
        }
    }
}
