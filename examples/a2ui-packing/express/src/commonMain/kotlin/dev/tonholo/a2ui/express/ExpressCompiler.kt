package dev.tonholo.a2ui.express

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

private const val OPEN_TAG = "<a2ui>"
private const val CLOSE_TAG = "</a2ui>"
private const val FENCE = "```"

/**
 * Compiles a model response written in a subset of A2UI Express into A2UI wire messages.
 *
 * Supported: `name = Component(...)` with positional, keyword and `_`-skipped arguments, inline nesting, strings,
 * numbers, booleans, null, lists, maps, `$/path` bindings, `$/path = value` data assignments and
 * `Event(name, context?)` actions. Templates, checks, functions, `surface(...)` and multiple catalogs are not.
 *
 * @param catalog the components the response may use.
 * @param version the A2UI protocol version written to every message.
 */
class ExpressCompiler(
    private val catalog: ExpressCatalog,
    private val version: String = "v0.9.1",
) {
    /**
     * Compiles the first `<a2ui>` block in [response] into the messages that create [surfaceId]:
     * `createSurface`, then `updateDataModel` when the block assigns data, then `updateComponents`.
     *
     * Data goes first because, with AndroidX A2UI Compose `1.0.0-alpha02` collecting messages on a background
     * thread, bound components sent right before their data sometimes stayed as placeholders, and sending the data
     * first avoided it in every run tried. The protocol allows either order.
     *
     * @throws ExpressException if the block is missing, malformed or uses something outside the catalog.
     */
    fun compile(response: String, surfaceId: String): List<JsonObject> {
        val statements = Parser(Lexer(extractBlock(response)).tokens()).statements()
        val components = ComponentFlattener(catalog, statements.filterIsInstance<Statement.Component>()).flatten()
        val dataModel = DataModelBuilder().apply {
            statements.filterIsInstance<Statement.Data>().forEach(::assign)
        }.build()
        return buildList {
            add(message("createSurface", "surfaceId" to surfaceId.json(), "catalogId" to catalog.catalogId.json()))
            if (dataModel != null) {
                add(message("updateDataModel", "surfaceId" to surfaceId.json(), "path" to "/".json(), "value" to dataModel))
            }
            add(message("updateComponents", "surfaceId" to surfaceId.json(), "components" to JsonArray(components)))
        }
    }

    private fun message(type: String, vararg fields: Pair<String, JsonElement>): JsonObject =
        JsonObject(mapOf("version" to version.json(), type to JsonObject(mapOf(*fields))))
}

private fun String.json(): JsonPrimitive = JsonPrimitive(this)

private fun extractBlock(response: String): String {
    val start = response.indexOf(OPEN_TAG)
    if (start < 0) throw ExpressException(line = null, reason = "No <a2ui> block found")
    val contentStart = start + OPEN_TAG.length
    val end = response.indexOf(CLOSE_TAG, contentStart).takeIf { it >= 0 } ?: response.length
    return response.substring(contentStart, end)
        .removePrefix("\n")
        .lines()
        .joinToString(separator = "\n") { line -> if (line.trimStart().startsWith(FENCE)) "" else line }
}
