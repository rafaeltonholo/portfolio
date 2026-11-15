package dev.tonholo.a2ui.express

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

private val structuralKeys = setOf("component", "id")

/**
 * The component signatures an Express response may use, read from an A2UI catalog schema.
 *
 * The same signatures drive both the prompt the model reads ([promptSignatures]) and the way the compiler maps
 * positional arguments back to property names, so the two can't drift apart.
 */
class ExpressCatalog internal constructor(
    val catalogId: String,
    private val components: Map<String, ComponentSignature>,
) {
    internal fun signature(component: String): ComponentSignature? = components[component]

    /**
     * Renders one line per component, such as `CheckBox(label, value)`, for the model's prompt.
     *
     * @throws IllegalArgumentException if a component isn't in the catalog.
     */
    fun promptSignatures(components: List<String>): String = components.joinToString(separator = "\n") { name ->
        requireNotNull(signature(name)) { "$name is not in the catalog" }.render()
    }

    companion object {
        /** Reads the `catalogId` and the components of an A2UI v0.9 catalog JSON schema. */
        fun fromSchema(schema: String): ExpressCatalog {
            val root = Json.parseToJsonElement(schema).jsonObject
            val components = root.getValue("components").jsonObject.mapValues { (name, definition) ->
                definition.jsonObject.toSignature(name)
            }
            return ExpressCatalog(
                catalogId = root.getValue("catalogId").jsonPrimitive.content,
                components = components,
            )
        }
    }
}

internal data class ComponentSignature(val name: String, val parameters: List<Parameter>) {
    fun render(): String = "$name(${parameters.joinToString(separator = ", ") { it.render() }})"
}

internal data class Parameter(val name: String, val required: Boolean, val allowedValues: List<String>) {
    fun render(): String = buildString {
        append(name)
        if (!required) append('?')
        if (allowedValues.isNotEmpty()) append(allowedValues.joinToString(separator = "|", prefix = ": "))
    }
}

private fun JsonObject.toSignature(name: String): ComponentSignature {
    val parts = listOf(this) + (this["allOf"]?.jsonArray?.map { it.jsonObject }.orEmpty())
    val required = parts.flatMap { part -> part["required"]?.jsonArray?.map { it.jsonPrimitive.content }.orEmpty() }
    val parameters = parts
        .flatMap { part -> part["properties"]?.jsonObject?.entries.orEmpty() }
        .filter { (property, _) -> property !in structuralKeys }
        .map { (property, definition) ->
            Parameter(
                name = property,
                required = property in required,
                allowedValues = definition.jsonObject["enum"]?.jsonArray?.map { it.jsonPrimitive.content }.orEmpty(),
            )
        }
    return ComponentSignature(name, parameters)
}
