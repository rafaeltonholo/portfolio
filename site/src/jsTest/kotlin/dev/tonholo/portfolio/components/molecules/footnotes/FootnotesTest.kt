package dev.tonholo.portfolio.components.molecules.footnotes

import kotlin.test.Test
import kotlin.test.assertEquals

class FootnotesTest {
    @Test
    fun `given text with two references, when splitting it, then text and references alternate in order`() {
        // Arrange
        val text = "the spec puts it[^spec]: and[^a2] more"
        // Act
        val segments = splitFootnoteReferences(text)
        // Assert
        assertEquals(
            listOf(
                FootnoteSegment.Text("the spec puts it"),
                FootnoteSegment.Reference("spec"),
                FootnoteSegment.Text(": and"),
                FootnoteSegment.Reference("a2"),
                FootnoteSegment.Text(" more"),
            ),
            segments,
        )
    }

    @Test
    fun `given text without references, when splitting it, then it is a single text segment`() {
        // Arrange
        val text = "plain [link] text"
        // Act
        val segments = splitFootnoteReferences(text)
        // Assert
        assertEquals(listOf(FootnoteSegment.Text("plain [link] text")), segments)
    }

    @Test
    fun `given footnotes referenced in some order, when numbering them, then numbers follow first appearance`() {
        // Arrange
        val registry = FootnoteRegistry()
        // Act
        val first = registry.registerReference(id = "spec")
        val second = registry.registerReference(id = "formats")
        val repeat = registry.registerReference(id = "spec")
        // Assert
        assertEquals(FootnoteReferenceAnchor(number = 1, anchorId = "fnref-spec", targetId = "fn-spec"), first)
        assertEquals(FootnoteReferenceAnchor(number = 2, anchorId = "fnref-formats", targetId = "fn-formats"), second)
        assertEquals(FootnoteReferenceAnchor(number = 1, anchorId = "fnref-spec-2", targetId = "fn-spec"), repeat)
    }

    @Test
    fun `given a definition, when resolving it, then it points back to the first reference`() {
        // Arrange
        val registry = FootnoteRegistry()
        registry.registerReference(id = "a")
        registry.registerReference(id = "b")
        // Act
        val definition = registry.definition(id = "b")
        // Assert
        assertEquals(FootnoteDefinitionAnchor(number = 2, anchorId = "fn-b", backTargetId = "fnref-b"), definition)
    }

    @Test
    fun `given a definition never referenced, when resolving it, then it gets the next number and no back link`() {
        // Arrange
        val registry = FootnoteRegistry()
        registry.registerReference(id = "a")
        // Act
        val definition = registry.definition(id = "unused")
        // Assert
        assertEquals(FootnoteDefinitionAnchor(number = 2, anchorId = "fn-unused", backTargetId = null), definition)
    }
}
