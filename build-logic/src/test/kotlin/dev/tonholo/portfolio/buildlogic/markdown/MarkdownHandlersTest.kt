package dev.tonholo.portfolio.buildlogic.markdown

import org.commonmark.node.FencedCodeBlock
import org.commonmark.node.Image
import org.commonmark.node.Paragraph
import org.commonmark.node.Text
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MarkdownHandlersTest {
    private fun fencedBlock(info: String, literal: String): FencedCodeBlock =
        FencedCodeBlock().apply {
            this.info = info
            this.literal = literal
        }

    @Test
    fun `given a mermaid fenced block - when formatting code - then a mermaid diagram is rendered`() {
        val block = fencedBlock(info = "mermaid", literal = "flowchart LR\n    A --> B\n")

        val code = MarkdownHandlers.formatCode(block)

        assertEquals(
            expected = "dev.tonholo.portfolio.components.atoms.MermaidDiagram(code = \"\"\"flowchart LR\n    A --> B\n\"\"\")",
            actual = code,
        )
    }

    @Test
    fun `given a kotlin fenced block - when formatting code - then a highlighted code block is rendered`() {
        val block = fencedBlock(info = "kotlin", literal = "val a = 1\n")

        val code = MarkdownHandlers.formatCode(block)

        assertEquals(
            expected = "dev.tonholo.portfolio.components.atoms.ShikiCodeBlock(code = \"\"\"val a = 1\n\"\"\", language = \"kotlin\")",
            actual = code,
        )
    }

    @Test
    fun `given kobweb's default h2 output - when formatting a heading - then it renders an article heading with the same id`() {
        val code = MarkdownHandlers.formatHeading(
            defaultCode = "org.jetbrains.compose.web.dom.H2(attrs = { id(\"tl-dr\") })",
        )

        assertEquals(
            expected = "dev.tonholo.portfolio.components.molecules.ArticleHeading(level = 2, id = \"tl-dr\")",
            actual = code,
        )
    }

    @Test
    fun `given a heading without an id - when formatting a heading - then kobweb's default output is kept`() {
        val defaultCode = "org.jetbrains.compose.web.dom.H4"

        val code = MarkdownHandlers.formatHeading(defaultCode = defaultCode)

        assertEquals(expected = defaultCode, actual = code)
    }

    @Test
    fun `given text with a footnote reference - when formatting text - then it renders footnote-aware text`() {
        val code = MarkdownHandlers.formatText(
            defaultCode = "org.jetbrains.compose.web.dom.Text(\"puts it[^spec]:\")",
        )

        assertEquals(
            expected = "dev.tonholo.portfolio.components.molecules.FootnoteText(text = \"puts it[^spec]:\")",
            actual = code,
        )
    }

    @Test
    fun `given text without a footnote reference - when formatting text - then kobweb's default output is kept`() {
        val defaultCode = "org.jetbrains.compose.web.dom.Text(\"plain [link] text\")"

        val code = MarkdownHandlers.formatText(defaultCode = defaultCode)

        assertEquals(expected = defaultCode, actual = code)
    }

    @Test
    fun `given a paragraph starting with a footnote definition - when formatting it - then it renders a definition and drops the marker`() {
        val paragraph = Paragraph().apply { appendChild(Text("[^spec]: A2UI v0.9.1 protocol specification, ")) }

        val code = MarkdownHandlers.formatParagraph(paragraph = paragraph, defaultCode = "org.jetbrains.compose.web.dom.P")

        assertEquals(
            expected = "dev.tonholo.portfolio.components.molecules.FootnoteDefinition(id = \"spec\")",
            actual = code,
        )
        assertEquals(expected = "A2UI v0.9.1 protocol specification, ", actual = (paragraph.firstChild as Text).literal)
    }

    @Test
    fun `given a paragraph holding only an image - when formatting it - then it renders a block container instead of a p`() {
        val paragraph = Paragraph().apply { appendChild(Image("/a.png", "caption")) }

        val code = MarkdownHandlers.formatParagraph(paragraph = paragraph, defaultCode = "org.jetbrains.compose.web.dom.P")

        assertEquals(expected = "org.jetbrains.compose.web.dom.Div", actual = code)
    }

    @Test
    fun `given an ordinary paragraph - when formatting it - then kobweb's default output is kept`() {
        val paragraph = Paragraph().apply { appendChild(Text("Just prose.")) }

        val code = MarkdownHandlers.formatParagraph(paragraph = paragraph, defaultCode = "org.jetbrains.compose.web.dom.P")

        assertEquals(expected = "org.jetbrains.compose.web.dom.P", actual = code)
    }

    @Test
    fun `given an image with a title - when formatting it - then it renders a figure with the title as caption`() {
        val image = Image("/images/a.png", "Two checkboxes").apply { appendChild(Text("JSON response")) }

        val code = MarkdownHandlers.formatImage(image)

        assertEquals(
            expected = "dev.tonholo.portfolio.components.molecules.ArticleFigure(" +
                "src = \"/images/a.png\", alt = \"JSON response\", caption = \"Two checkboxes\")",
            actual = code,
        )
        assertNull(image.firstChild, "the alt text children must be detached so they aren't rendered as content")
    }

    @Test
    fun `given an image without a title - when formatting it - then it renders a figure with no caption`() {
        val image = Image("/images/a.png", null).apply { appendChild(Text("A \"quoted\" alt")) }

        val code = MarkdownHandlers.formatImage(image)

        assertEquals(
            expected = "dev.tonholo.portfolio.components.molecules.ArticleFigure(" +
                "src = \"/images/a.png\", alt = \"A \\\"quoted\\\" alt\", caption = null)",
            actual = code,
        )
    }

    @Test
    fun `given generated code where a footnote became a link - when checking footnotes - then the problem is reported`() {
        val generated = """
            |dev.tonholo.portfolio.components.atoms.ArticleLink(href = "https://example.org") {
            |    org.jetbrains.compose.web.dom.Text("^note")
            |}
        """.trimMargin()

        val problems = MarkdownHandlers.findFootnoteProblems(generated)

        assertEquals(expected = listOf("note"), actual = problems)
    }
}
