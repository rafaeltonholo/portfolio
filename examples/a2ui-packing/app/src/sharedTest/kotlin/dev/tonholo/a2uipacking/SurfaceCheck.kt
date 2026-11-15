package dev.tonholo.a2uipacking

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

private val exampleNames = setOf("Dice", "Snacks")
private const val EXAMPLE_TITLE = "Board game night"

/** What the benchmark learned about one generated surface. */
data class SurfaceVerdict(val problem: String?, val itemCount: Int, val copiedExample: Boolean)

/**
 * Checks the messages a packing response produced: a flat component list with a root, children that exist,
 * only the allowed components, checkboxes with a label and value, and bindings that point at real data.
 */
fun checkSurface(messages: List<JsonObject>): SurfaceVerdict {
    val components = messages.firstNotNullOfOrNull { it["updateComponents"]?.jsonObject?.get("components")?.jsonArray }
        ?: return SurfaceVerdict(problem = "no updateComponents", itemCount = 0, copiedExample = false)
    val dataModel = messages.firstNotNullOfOrNull { it["updateDataModel"]?.jsonObject }?.let(::dataRoot)
    val problem = runCatching { structureProblem(components.map { it.jsonObject }, dataModel) }
        .getOrElse { "malformed component: ${it.message}" }
    val names = itemNames(dataModel)
    val title = components.map { it.jsonObject }.firstOrNull { it.text("component") == "Text" }?.text("text")
    return SurfaceVerdict(
        problem = problem,
        itemCount = names.size,
        copiedExample = title == EXAMPLE_TITLE || (names.isNotEmpty() && exampleNames.containsAll(names)),
    )
}

private fun structureProblem(components: List<JsonObject>, dataModel: JsonElement?): String? {
    val ids = components.map { it.text("id") ?: return "component without id" }
    if (ids.size != ids.toSet().size) return "duplicate ids"
    if ("root" !in ids) return "no root"
    components.forEach { component ->
        when (val type = component.text("component")) {
            "Column" -> component.childIds().firstOrNull { it !in ids }?.let { return "missing child $it" }
            "Text" -> if (component["text"] == null) return "Text without text"
            "CheckBox" -> if (component["label"] == null || component["value"] == null) return "CheckBox without label or value"
            else -> return "unexpected component $type"
        }
        component.boundPaths().firstOrNull { resolve(dataModel, it) == null }?.let { return "unbound path $it" }
    }
    return null
}

private fun JsonObject.text(key: String): String? = (this[key] as? JsonPrimitive)?.takeIf { it.isString }?.content

private fun JsonObject.childIds(): List<String> {
    val children = this["children"] as? JsonArray ?: error("children must be a list of ids")
    return children.map { child -> (child as? JsonPrimitive)?.content ?: error("children must be ids, not nested components") }
}

private fun JsonObject.boundPaths(): List<String> = values.mapNotNull { value -> (value as? JsonObject)?.text("path") }

private fun dataRoot(update: JsonObject): JsonElement? {
    val path = update.text("path") ?: "/"
    val value = update["value"] ?: return null
    return path.trim('/').split('/').filter { it.isNotEmpty() }.foldRight(value) { segment, inner ->
        JsonObject(mapOf(segment to inner))
    }
}

private fun resolve(root: JsonElement?, path: String): JsonElement? =
    path.trim('/').split('/').fold(root) { node, segment ->
        when (node) {
            is JsonObject -> node[segment]
            is JsonArray -> segment.toIntOrNull()?.let(node::getOrNull)
            else -> null
        }
    }

private fun itemNames(dataModel: JsonElement?): List<String> =
    (resolve(dataModel, "/items") as? JsonArray).orEmpty().mapNotNull { item ->
        (item as? JsonObject)?.get("name")?.jsonPrimitive?.content
    }
