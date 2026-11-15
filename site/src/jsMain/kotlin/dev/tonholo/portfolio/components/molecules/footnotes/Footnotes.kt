package dev.tonholo.portfolio.components.molecules.footnotes

private val FOOTNOTE_REFERENCE_REGEX = Regex("""\[\^([^\]\s]+)\]""")
private const val REFERENCE_ID_PREFIX = "fnref-"
private const val DEFINITION_ID_PREFIX = "fn-"

/** A piece of prose text: either plain text or a `[^id]` footnote reference. */
sealed interface FootnoteSegment {
    data class Text(val value: String) : FootnoteSegment

    data class Reference(val id: String) : FootnoteSegment
}

/** Splits [text] around its `[^id]` footnote references, keeping their order. */
fun splitFootnoteReferences(text: String): List<FootnoteSegment> {
    val segments = mutableListOf<FootnoteSegment>()
    var cursor = 0
    FOOTNOTE_REFERENCE_REGEX.findAll(text).forEach { match ->
        if (match.range.first > cursor) segments += FootnoteSegment.Text(text.substring(cursor, match.range.first))
        segments += FootnoteSegment.Reference(match.groupValues[1])
        cursor = match.range.last + 1
    }
    if (cursor < text.length) segments += FootnoteSegment.Text(text.substring(cursor))
    return segments
}

/**
 * Where one footnote reference sits and where it points.
 *
 * @param number the footnote number shown to readers.
 * @param anchorId the reference's own element id, unique per occurrence.
 * @param targetId the id of the matching definition.
 */
data class FootnoteReferenceAnchor(val number: Int, val anchorId: String, val targetId: String)

/**
 * Where one footnote definition sits and where its back link points.
 *
 * @param number the footnote number shown to readers.
 * @param anchorId the definition's element id.
 * @param backTargetId the id of the first reference, or null when nothing references it.
 */
data class FootnoteDefinitionAnchor(
    val number: Int,
    val anchorId: String,
    val backTargetId: String?,
)

/**
 * Numbers footnotes in the order readers first meet them, and gives every reference its own anchor id so
 * definitions can link back to the first one.
 */
class FootnoteRegistry {
    private val numbers = mutableMapOf<String, Int>()
    private val referenceCounts = mutableMapOf<String, Int>()

    fun registerReference(id: String): FootnoteReferenceAnchor {
        val occurrence = (referenceCounts[id] ?: 0) + 1
        referenceCounts[id] = occurrence
        val suffix = if (occurrence == 1) "" else "-$occurrence"
        return FootnoteReferenceAnchor(
            number = numberOf(id),
            anchorId = "$REFERENCE_ID_PREFIX$id$suffix",
            targetId = "$DEFINITION_ID_PREFIX$id",
        )
    }

    fun definition(id: String): FootnoteDefinitionAnchor = FootnoteDefinitionAnchor(
        number = numberOf(id),
        anchorId = "$DEFINITION_ID_PREFIX$id",
        backTargetId = if (referenceCounts.containsKey(id)) "$REFERENCE_ID_PREFIX$id" else null,
    )

    private fun numberOf(id: String): Int = numbers.getOrPut(id) { numbers.size + 1 }
}
