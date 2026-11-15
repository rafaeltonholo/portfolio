package dev.tonholo.a2uipacking

import org.junit.Assert.assertTrue
import org.junit.Test

class ModelPromptTest {
    private val signatures = "Column(children, justify?, align?)\nText(text, variant?)\nCheckBox(label, value)"

    @Test
    fun `given a request - when making the express prompt - then it lists the catalog signatures and asks for one a2ui block`() {
        val prompt = expressPrompt(request = "Pack for a weekend", signatures = signatures)

        assertTrue(prompt.contains("Pack for a weekend"))
        assertTrue(prompt.contains(signatures))
        assertTrue(prompt.contains("<a2ui>"))
        assertTrue(prompt.contains("/no_think"))
    }

    @Test
    fun `given a request - when making the json prompt - then it asks for v0_9_1 wire messages`() {
        val prompt = jsonPrompt(request = "Pack for a weekend")

        assertTrue(prompt.contains("Pack for a weekend"))
        assertTrue(prompt.contains("\"version\":\"v0.9.1\""))
        assertTrue(prompt.contains("updateComponents"))
        assertTrue(prompt.contains("updateDataModel"))
        assertTrue(prompt.contains("/no_think"))
    }

    @Test
    fun `given both prompts - when comparing their examples - then they describe the same checklist`() {
        val express = expressPrompt(request = "x", signatures = signatures)
        val json = jsonPrompt(request = "x")

        listOf("Board game night", "Dice", "Snacks").forEach { text ->
            assertTrue("express prompt is missing $text", express.contains(text))
            assertTrue("json prompt is missing $text", json.contains(text))
        }
    }
}
