package dev.tonholo.a2uipacking

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SurfaceCheckTest {
    private fun messages(vararg json: String): List<JsonObject> = json.map { Json.parseToJsonElement(it).jsonObject }

    private val validComponents =
        """{"updateComponents":{"surfaceId":"packing","components":[{"id":"root","component":"Column","children":["title","item0"]},{"id":"title","component":"Text","text":"Ski weekend","variant":"h2"},{"id":"item0","component":"CheckBox","label":{"path":"/items/0/name"},"value":{"path":"/items/0/packed"}}]}}"""
    private val validData = """{"updateDataModel":{"surfaceId":"packing","path":"/items","value":[{"name":"Gloves","packed":false}]}}"""

    @Test
    fun `given a flat surface with bound data - when checking it - then it has no problem and counts the items`() {
        val verdict = checkSurface(messages(validComponents, validData))

        assertNull(verdict.problem)
        assertEquals(1, verdict.itemCount)
        assertFalse(verdict.copiedExample)
    }

    @Test
    fun `given children nested inside the parent - when checking it - then the nesting is reported`() {
        val nested = """{"updateComponents":{"surfaceId":"packing","components":[{"id":"root","component":"Column","children":[{"id":"title","component":"Text","text":"Hi"}]}]}}"""

        val verdict = checkSurface(messages(nested))

        assertEquals("malformed component: children must be ids, not nested components", verdict.problem)
    }

    @Test
    fun `given a child id that is never defined - when checking it - then the missing child is reported`() {
        val missing = """{"updateComponents":{"surfaceId":"packing","components":[{"id":"root","component":"Column","children":["title"]}]}}"""

        assertEquals("missing child title", checkSurface(messages(missing)).problem)
    }

    @Test
    fun `given a binding to data that was never sent - when checking it - then the unbound path is reported`() {
        assertEquals("unbound path /items/0/name", checkSurface(messages(validComponents)).problem)
    }

    @Test
    fun `given a checkbox without a value - when checking it - then it is reported`() {
        val noValue = """{"updateComponents":{"surfaceId":"packing","components":[{"id":"root","component":"CheckBox","label":"Towel"}]}}"""

        assertEquals("CheckBox without label or value", checkSurface(messages(noValue)).problem)
    }

    @Test
    fun `given the example title from the prompt - when checking it - then it is flagged as copied`() {
        val copied = validComponents.replace("Ski weekend", "Board game night")

        assertTrue(checkSurface(messages(copied, validData)).copiedExample)
    }

    @Test
    fun `given only the example items from the prompt - when checking it - then it is flagged as copied`() {
        val copiedData = """{"updateDataModel":{"surfaceId":"packing","path":"/items","value":[{"name":"Dice","packed":false}]}}"""

        assertTrue(checkSurface(messages(validComponents, copiedData)).copiedExample)
    }
}
