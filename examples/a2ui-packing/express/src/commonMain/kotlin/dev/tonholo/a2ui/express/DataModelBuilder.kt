package dev.tonholo.a2ui.express

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

private const val MAX_LIST_INDEX = 10_000

/**
 * Folds `$/path = value` assignments, in order, into the surface's initial data model. Later assignments can
 * refine earlier ones, such as `$/items/0/packed = true` after `$/items = [...]`.
 */
internal class DataModelBuilder {
    private val root = mutableMapOf<String, Any?>()

    fun assign(statement: Statement.Data) {
        val segments = statement.path.trim('/').split('/').filter { it.isNotEmpty() }
        if (segments.isEmpty()) throw ExpressException(statement.line, "Assign data to a path such as $/items")
        val value = statement.value.toData()
        val parent = segments.dropLast(1).foldIndexed(root as Any) { index, container, segment ->
            child(container, segment, nextSegment = segments[index + 1], line = statement.line)
        }
        set(parent, segments.last(), value, statement.line)
    }

    fun build(): JsonObject? = root.takeIf { it.isNotEmpty() }?.let { toJson(it) as JsonObject }

    private fun child(container: Any, segment: String, nextSegment: String, line: Int): Any {
        val existing = get(container, segment, line)
        if (existing is MutableMap<*, *> || existing is MutableList<*>) return existing
        val created: Any = if (nextSegment.toListIndex() != null) mutableListOf<Any?>() else mutableMapOf<String, Any?>()
        set(container, segment, created, line)
        return created
    }

    private fun get(container: Any, segment: String, line: Int): Any? = when (container) {
        is MutableMap<*, *> -> container[segment]
        is MutableList<*> -> container.getOrNull(listIndex(segment, line))
        else -> throw ExpressException(line, "Can't assign inside a value")
    }

    @Suppress("UNCHECKED_CAST")
    private fun set(container: Any, segment: String, value: Any?, line: Int) {
        when (container) {
            is MutableMap<*, *> -> (container as MutableMap<String, Any?>)[segment] = value
            is MutableList<*> -> (container as MutableList<Any?>).setPadded(listIndex(segment, line), value)
            else -> throw ExpressException(line, "Can't assign inside a value")
        }
    }

    private fun listIndex(segment: String, line: Int): Int =
        segment.toListIndex() ?: throw ExpressException(line, "$segment is not a list index")
}

private fun String.toListIndex(): Int? = toIntOrNull()?.takeIf { it in 0..MAX_LIST_INDEX }

private fun MutableList<Any?>.setPadded(index: Int, value: Any?) {
    while (size <= index) add(null)
    this[index] = value
}

private fun Expression.toData(): Any? = when (this) {
    is Expression.Text -> JsonPrimitive(value)
    is Expression.Number -> toJson()
    is Expression.Bool -> JsonPrimitive(value)
    is Expression.Null -> null
    is Expression.ListOf -> items.mapTo(mutableListOf()) { it.toData() }
    is Expression.MapOf -> entries.associateTo(mutableMapOf()) { (key, value) -> key to value.toData() }
    else -> throw ExpressException(line, "Data values must be strings, numbers, booleans, null, lists or maps")
}

private fun toJson(value: Any?): JsonElement = when (value) {
    null -> JsonNull
    is JsonElement -> value
    is Map<*, *> -> JsonObject(value.entries.associate { (key, item) -> key.toString() to toJson(item) })
    is List<*> -> JsonArray(value.map(::toJson))
    else -> error("Unexpected data value $value")
}
