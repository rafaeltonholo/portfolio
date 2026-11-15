package dev.tonholo.a2uipacking.ui.packing

import java.io.File
import kotlin.time.Duration
import kotlinx.serialization.json.JsonObject

data class PackingUiState(
    val modelPaths: List<String>,
    val selectedModel: String?,
    val request: String,
    val phase: GenerationPhase = GenerationPhase.Idle,
    val checklist: Checklist? = null,
) {
    val canGenerate: Boolean
        get() = selectedModel != null && request.isNotBlank() && phase != GenerationPhase.Generating
}

sealed interface GenerationPhase {
    data object Idle : GenerationPhase

    data object Generating : GenerationPhase

    /** @property elapsed how long the model took, or null when the checklist didn't come from a model. */
    data class Ready(val elapsed: Duration?) : GenerationPhase

    data class Failed(val reason: String) : GenerationPhase
}

/**
 * A2UI messages for one checklist. [id] changes on every new checklist, so the same messages can be shown again.
 */
data class Checklist(val id: Int, val messages: List<JsonObject>)

val String.modelName: String
    get() = File(this).nameWithoutExtension
