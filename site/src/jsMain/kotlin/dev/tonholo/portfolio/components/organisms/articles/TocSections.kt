package dev.tonholo.portfolio.components.organisms.articles

/** Heading level of a top-level article section. */
const val SECTION_LEVEL = 2

/**
 * A top-level article section in the table of contents, with the subsections under it.
 *
 * @param entry the section's own heading.
 * @param subsections the deeper headings that follow it, in order.
 */
data class TocSection(val entry: TocEntry, val subsections: List<TocEntry>)

/**
 * Groups a flat, in-order heading list into sections: each heading deeper than [SECTION_LEVEL] goes under the
 * closest section before it. A deeper heading with no section before it becomes a section of its own.
 */
fun buildTocSections(entries: List<TocEntry>): List<TocSection> {
    val sections = mutableListOf<TocSection>()
    entries.forEach { entry ->
        val current = sections.lastOrNull()
        if (entry.level > SECTION_LEVEL && current != null) {
            sections[sections.lastIndex] = current.copy(subsections = current.subsections + entry)
        } else {
            sections += TocSection(entry = entry, subsections = emptyList())
        }
    }
    return sections
}

/** Returns the id of the section that contains [activeId], whether it's the section itself or a subsection. */
fun List<TocSection>.activeSectionId(activeId: String): String? =
    firstOrNull { section -> section.entry.id == activeId || section.subsections.any { it.id == activeId } }
        ?.entry
        ?.id
