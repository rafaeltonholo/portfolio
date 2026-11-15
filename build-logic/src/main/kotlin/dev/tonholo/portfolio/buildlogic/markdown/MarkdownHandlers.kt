package dev.tonholo.portfolio.buildlogic.markdown

import org.commonmark.node.BlockQuote
import org.commonmark.node.Code
import org.commonmark.node.FencedCodeBlock
import org.commonmark.node.Image
import org.commonmark.node.Link
import org.commonmark.node.Node
import org.commonmark.node.Paragraph
import org.commonmark.node.SoftLineBreak
import org.commonmark.node.Text
import java.io.File

/**
 * Handlers and post-conversion utilities for Kobweb markdown compilation.
 */
object MarkdownHandlers {
    private const val MERMAID_LANGUAGE = "mermaid"
    private const val MOLECULES = "dev.tonholo.portfolio.components.molecules"
    private const val BLOCK_CONTAINER = "org.jetbrains.compose.web.dom.Div"
    private val DEFAULT_HEADING_REGEX: Regex = Regex("""^org\.jetbrains\.compose\.web\.dom\.H([1-6])\(attrs = \{ id\("([^"]*)"\) \}\)$""")
    private val DEFAULT_TEXT_REGEX: Regex = Regex("""^org\.jetbrains\.compose\.web\.dom\.Text\((".*")\)$""", RegexOption.DOT_MATCHES_ALL)
    private val FOOTNOTE_REFERENCE_REGEX: Regex = Regex("""\[\^[^\]\s]+\]""")
    private val FOOTNOTE_DEFINITION_REGEX: Regex = Regex("""^\[\^([^\]\s]+)\]:\s*""")
    private val FOOTNOTE_AS_LINK_REGEX: Regex =
        Regex("""ArticleLink\(href = "[^"]*"\) \{\s*org\.jetbrains\.compose\.web\.dom\.Text\("\^([^"\s]+)"\)""")
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
        if (lang == MERMAID_LANGUAGE) {
            return "dev.tonholo.portfolio.components.atoms.MermaidDiagram(code = \"\"\"$escaped\"\"\")"
        }
        return "dev.tonholo.portfolio.components.atoms.ShikiCodeBlock(code = \"\"\"$escaped\"\"\", language = \"$lang\")"
    }

    fun formatLink(link: Link): String {
        val dest: String = link.destination
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("$", "\${'$'}")
        return "dev.tonholo.portfolio.components.atoms.ArticleLink(href = \"$dest\")"
    }

    /**
     * Rewrites Kobweb's default heading output into an `ArticleHeading`, keeping Kobweb's generated id so
     * existing section links keep working. Headings without an id keep the default output.
     */
    fun formatHeading(defaultCode: String): String {
        val match: MatchResult = DEFAULT_HEADING_REGEX.find(defaultCode) ?: return defaultCode
        val (level: String, id: String) = match.destructured
        return "$MOLECULES.ArticleHeading(level = $level, id = \"$id\")"
    }

    /**
     * Wraps Kobweb's default text output in `FootnoteText` when it contains a `[^id]` reference, reusing
     * Kobweb's escaped string literal.
     */
    fun formatText(defaultCode: String): String {
        val literal: String = DEFAULT_TEXT_REGEX.find(defaultCode)?.groupValues?.get(1) ?: return defaultCode
        if (!FOOTNOTE_REFERENCE_REGEX.containsMatchIn(literal)) return defaultCode
        return "$MOLECULES.FootnoteText(text = $literal)"
    }

    /**
     * Renders a `[^id]: ...` paragraph as a `FootnoteDefinition`, removing the marker from its first text node,
     * and an image-only paragraph as a block container so its figure isn't nested inside a `<p>`.
     */
    fun formatParagraph(paragraph: Paragraph, defaultCode: String): String {
        val firstText: Text? = paragraph.firstChild as? Text
        val definition: MatchResult? = firstText?.let { FOOTNOTE_DEFINITION_REGEX.find(it.literal) }
        if (firstText != null && definition != null) {
            firstText.literal = firstText.literal.substring(definition.range.last + 1)
            return "$MOLECULES.FootnoteDefinition(id = \"${definition.groupValues[1].escapeForKotlinString()}\")"
        }
        if (paragraph.containsOnlyImages()) return BLOCK_CONTAINER
        return defaultCode
    }

    /** Renders an image as an `ArticleFigure`, using the markdown image title as its caption. */
    fun formatImage(image: Image): String {
        val alt: String = image.plainText().escapeForKotlinString()
        image.detachChildren()
        val src: String = image.destination.escapeForKotlinString()
        val caption: String = image.title?.takeIf { it.isNotBlank() }?.let { "\"${it.escapeForKotlinString()}\"" } ?: "null"
        return "$MOLECULES.ArticleFigure(src = \"$src\", alt = \"$alt\", caption = $caption)"
    }

    /**
     * Finds footnote references that CommonMark turned into links because their definition was read as a link
     * reference definition (for example `[^a]: https://example.org`). Returns the affected footnote ids.
     */
    fun findFootnoteProblems(generatedCode: String): List<String> =
        FOOTNOTE_AS_LINK_REGEX.findAll(generatedCode).map { it.groupValues[1] }.toList()

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

    private fun Paragraph.containsOnlyImages(): Boolean {
        var child: Node? = firstChild
        var hasImage = false
        while (child != null) {
            when {
                child is Image -> hasImage = true
                child is SoftLineBreak -> Unit
                child is Text && child.literal.isBlank() -> Unit
                else -> return false
            }
            child = child.next
        }
        return hasImage
    }

    private fun Node.detachChildren() {
        while (firstChild != null) firstChild.unlink()
    }

    private fun Node.plainText(): String = buildString {
        var child: Node? = firstChild
        while (child != null) {
            when (child) {
                is Text -> append(child.literal)
                is Code -> append(child.literal)
                else -> append(child.plainText())
            }
            child = child.next
        }
    }

    private fun String.escapeForKotlinString(): String =
        replace("\\", "\\\\").replace("\"", "\\\"").replace("$", "\${'$'}")
}
