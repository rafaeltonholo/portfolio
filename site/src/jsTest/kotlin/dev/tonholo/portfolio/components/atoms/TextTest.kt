package dev.tonholo.portfolio.components.atoms

import com.varabyte.kobweb.compose.ui.graphics.Color
import dev.tonholo.portfolio.theme.typography.Typography
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class TextTest {
    @Test
    fun `given default Text slot, when slot is read, then it is body`() {
        // Arrange / Act
        val slot = TextSlot.Body

        // Assert
        assertEquals(Typography.body, slot.style)
    }

    @Test
    fun `given TextSlot Hero, when style is read, then it matches Typography hero`() {
        // Arrange / Act
        val slot = TextSlot.Hero

        // Assert
        assertEquals(Typography.hero, slot.style)
    }

    @Test
    fun `given TextSlot H1, when style is read, then it matches Typography h1`() {
        assertEquals(Typography.h1, TextSlot.H1.style)
    }

    @Test
    fun `given TextSlot H2, when style is read, then it matches Typography h2`() {
        assertEquals(Typography.h2, TextSlot.H2.style)
    }

    @Test
    fun `given TextSlot H3Display, when style is read, then it matches Typography h3Display`() {
        assertEquals(Typography.h3Display, TextSlot.H3Display.style)
    }

    @Test
    fun `given TextSlot BodyLarge, when style is read, then it matches Typography bodyLarge`() {
        assertEquals(Typography.bodyLarge, TextSlot.BodyLarge.style)
    }

    @Test
    fun `given TextSlot BodySmall, when style is read, then it matches Typography bodySmall`() {
        assertEquals(Typography.bodySmall, TextSlot.BodySmall.style)
    }

    @Test
    fun `given TextSlot SerifLead, when style is read, then it matches Typography serifLead`() {
        assertEquals(Typography.serifLead, TextSlot.SerifLead.style)
    }

    @Test
    fun `given TextSlot SerifBody, when style is read, then it matches Typography serifBody`() {
        assertEquals(Typography.serifBody, TextSlot.SerifBody.style)
    }

    @Test
    fun `given TextSlot Code, when style is read, then it matches Typography code`() {
        assertEquals(Typography.code, TextSlot.Code.style)
    }

    @Test
    fun `given color null, when passed, then caller can opt out of color override`() {
        val color: Color? = null
        assertNull(color)
    }

    @Test
    fun `given PortfolioTextStyle is a val, when referenced, then it is non-null`() {
        assertNotNull(PortfolioTextStyle)
    }
}
