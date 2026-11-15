package dev.tonholo.a2ui.express

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

private const val ROOT = "root"
private const val EVENT = "Event"

/**
 * Walks the component tree from `root` and emits A2UI's flat adjacency list: every component becomes one entry,
 * and parents point to children by id.
 *
 * Components assigned to a variable keep the variable name as their id. Inline components get an id derived
 * from their parent and the slot they fill, such as `root_child_children_0`, matching the reference compilers.
 */
internal class ComponentFlattener(
    private val catalog: ExpressCatalog,
    statements: List<Statement.Component>,
) {
    private val variables = statements.associateBy { it.name }
    private val emitted = mutableSetOf<String>()
    private val inProgress = mutableSetOf<String>()
    private val components = mutableListOf<JsonObject>()

    fun flatten(): List<JsonObject> {
        val root = variables[ROOT] ?: throw ExpressException(line = null, reason = "No root component defined")
        if (!root.isComponent()) throw ExpressException(root.line, "root must be a component")
        emitVariable(root)
        return components
    }

    private fun Statement.Component.isComponent(): Boolean =
        (value as? Expression.Call)?.let { catalog.signature(it.name) } != null

    private fun emitVariable(statement: Statement.Component): JsonElement {
        if (statement.name in emitted) return JsonPrimitive(statement.name)
        inProgress += statement.name
        val property = if (statement.isComponent()) {
            emitComponent(id = statement.name, call = statement.value as Expression.Call)
            emitted += statement.name
            JsonPrimitive(statement.name)
        } else {
            toProperty(statement.value, slotId = statement.name)
        }
        inProgress -= statement.name
        return property
    }

    private fun emitComponent(id: String, call: Expression.Call) {
        val signature = catalog.signature(call.name) ?: throw ExpressException(call.line, "${call.name} is not in the catalog")
        val slot = components.size
        components += JsonObject(emptyMap())
        val properties = bindArguments(signature, call).mapValues { (parameter, value) ->
            toProperty(value, slotId = "${id}_$parameter")
        }
        components[slot] = buildJsonObject {
            put("id", JsonPrimitive(id))
            put("component", JsonPrimitive(call.name))
            properties.forEach { (name, value) -> put(name, value) }
        }
    }

    private fun toProperty(expression: Expression, slotId: String): JsonElement = when (expression) {
        is Expression.Text -> JsonPrimitive(expression.value)
        is Expression.Number -> expression.toJson()
        is Expression.Bool -> JsonPrimitive(expression.value)
        is Expression.Null -> JsonNull
        is Expression.Path -> JsonObject(mapOf("path" to JsonPrimitive(expression.path)))
        is Expression.ListOf -> JsonArray(expression.items.mapIndexed { index, item -> toProperty(item, "${slotId}_$index") })
        is Expression.MapOf -> JsonObject(expression.entries.associate { (key, value) -> key to toProperty(value, "${slotId}_$key") })
        is Expression.Reference -> resolve(expression)
        is Expression.Call -> callToProperty(expression, slotId)
        is Expression.Skip -> throw ExpressException(expression.line, "_ can only skip an argument")
    }

    private fun resolve(reference: Expression.Reference): JsonElement {
        if (reference.name in inProgress) throw ExpressException(reference.line, "${reference.name} refers back to itself")
        val statement = variables[reference.name] ?: throw ExpressException(reference.line, "${reference.name} is not defined")
        return emitVariable(statement)
    }

    private fun callToProperty(call: Expression.Call, slotId: String): JsonElement {
        if (call.name == EVENT) return event(call)
        emitComponent(id = slotId, call = call)
        return JsonPrimitive(slotId)
    }

    private fun event(call: Expression.Call): JsonElement {
        val arguments = bindArguments(eventSignature, call)
        val name = arguments.getValue("name") as? Expression.Text
            ?: throw ExpressException(call.line, "Event name must be a string")
        val context = arguments["context"]?.let { toProperty(it, slotId = "") } ?: JsonObject(emptyMap())
        return JsonObject(mapOf("event" to JsonObject(mapOf("name" to JsonPrimitive(name.value), "context" to context))))
    }
}

private val eventSignature = ComponentSignature(
    name = EVENT,
    parameters = listOf(
        Parameter(name = "name", required = true, allowedValues = emptyList()),
        Parameter(name = "context", required = false, allowedValues = emptyList()),
    ),
)
