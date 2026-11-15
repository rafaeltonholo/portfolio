package dev.tonholo.portfolio.components.organisms.articles

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TocSectionsTest {
    private val anatomy = TocEntry(id = "anatomy", label = "Anatomy", level = 2)
    private val createSurface = TocEntry(id = "createsurface", label = "createSurface", level = 3)
    private val updateDataModel = TocEntry(id = "updatedatamodel", label = "updateDataModel", level = 3)
    private val flatList = TocEntry(id = "flat-list", label = "Why a flat list?", level = 2)

    @Test
    fun `given h2 and h3 entries, when building sections, then each h3 is nested under the h2 before it`() {
        // Arrange
        val entries = listOf(anatomy, createSurface, updateDataModel, flatList)
        // Act
        val sections = buildTocSections(entries)
        // Assert
        assertEquals(
            listOf(
                TocSection(entry = anatomy, subsections = listOf(createSurface, updateDataModel)),
                TocSection(entry = flatList, subsections = emptyList()),
            ),
            sections,
        )
    }

    @Test
    fun `given an h3 before any h2, when building sections, then it becomes its own section`() {
        // Arrange
        val entries = listOf(createSurface, anatomy)
        // Act
        val sections = buildTocSections(entries)
        // Assert
        assertEquals(
            listOf(
                TocSection(entry = createSurface, subsections = emptyList()),
                TocSection(entry = anatomy, subsections = emptyList()),
            ),
            sections,
        )
    }

    @Test
    fun `given the active heading is an h3, when resolving the active section, then its parent h2 is active`() {
        // Arrange
        val sections = buildTocSections(listOf(anatomy, createSurface, flatList))
        // Act
        val activeSection = sections.activeSectionId(activeId = "createsurface")
        // Assert
        assertEquals("anatomy", activeSection)
    }

    @Test
    fun `given an unknown active id, when resolving the active section, then there is none`() {
        // Arrange
        val sections = buildTocSections(listOf(anatomy, flatList))
        // Act
        val activeSection = sections.activeSectionId(activeId = "missing")
        // Assert
        assertNull(activeSection)
    }
}
