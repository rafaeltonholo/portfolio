package dev.tonholo.a2uipacking

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class JsonObjectsTest {
    @Test
    fun `given pretty printed objects inside a fence - when extracting - then each top level object is returned`() {
        val text = "Here you go:\n```json\n{\n  \"a\": {\"b\": 1}\n}\n{\"c\": \"}\"}\n```"

        val objects = jsonObjectsIn(text)

        assertEquals(listOf(Json.parseToJsonElement("""{"a":{"b":1}}"""), Json.parseToJsonElement("""{"c":"}"}""")), objects)
    }

    @Test
    fun `given an object that never closes - when extracting - then it is rejected`() {
        assertThrows(IllegalArgumentException::class.java) { jsonObjectsIn("{\"a\": [1, 2") }
    }

    @Test
    fun `given text without objects - when extracting - then it is rejected`() {
        assertThrows(IllegalArgumentException::class.java) { jsonObjectsIn("Sorry, I can't.") }
    }
}
