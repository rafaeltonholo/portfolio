package dev.tonholo.a2ui.express

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

internal fun JsonElement.jsonObjectAt(key: String): JsonObject = jsonObject.getValue(key).jsonObject

internal fun JsonElement.messageType(): String = jsonObject.keys.first { it != "version" }

internal fun JsonArray.message(type: String): JsonObject = first { it.messageType() == type }.jsonObjectAt(type)

internal fun JsonArray.components(): JsonArray = message("updateComponents").getValue("components").jsonArray
