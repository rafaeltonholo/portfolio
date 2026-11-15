package dev.tonholo.a2uipacking

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

/**
 * Finds every top-level JSON object in [text], ignoring prose, fences and line breaks around them.
 *
 * @throws IllegalArgumentException if there's no object, one never closes, or one isn't valid JSON.
 */
fun jsonObjectsIn(text: String): List<JsonObject> {
    val objects = mutableListOf<JsonObject>()
    var position = text.indexOf('{')
    while (position >= 0) {
        val end = closingBrace(text, position)
        objects += Json.parseToJsonElement(text.substring(position, end + 1)).jsonObject
        position = text.indexOf('{', end + 1)
    }
    require(objects.isNotEmpty()) { "no JSON object found" }
    return objects
}

private fun closingBrace(text: String, start: Int): Int {
    var depth = 0
    var inString = false
    var escaped = false
    for (index in start until text.length) {
        val char = text[index]
        when {
            escaped -> escaped = false
            inString && char == '\\' -> escaped = true
            char == '"' -> inString = !inString
            inString -> Unit
            char == '{' -> depth++
            char == '}' -> if (--depth == 0) return index
        }
    }
    throw IllegalArgumentException("unclosed JSON object")
}
