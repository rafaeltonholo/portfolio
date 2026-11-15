package dev.tonholo.a2uipacking.ui.packing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.tonholo.a2ui.express.ExpressCompiler
import dev.tonholo.a2uipacking.PACKING_SURFACE_ID
import dev.tonholo.a2uipacking.expressPrompt
import dev.tonholo.a2uipacking.recordedExpressResponse
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.TimeSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val DEFAULT_REQUEST = "Pack for a rainy weekend in Halifax"

/** Writes a model response for a prompt, using the model file at `modelPath`. */
fun interface ResponseGenerator {
    suspend fun generate(modelPath: String, prompt: String): String
}

/**
 * Turns a packing request into a checklist: asks the selected model for Express, then compiles it to A2UI messages.
 *
 * @param signatures the component signatures the prompt allows, from `ExpressCatalog.promptSignatures`.
 * @param modelPaths the model files available on the device.
 */
class PackingViewModel(
    private val compiler: ExpressCompiler,
    private val signatures: String,
    private val generator: ResponseGenerator,
    modelPaths: List<String>,
    private val timeSource: TimeSource = TimeSource.Monotonic,
) : ViewModel() {
    private val state = MutableStateFlow(
        PackingUiState(modelPaths = modelPaths, selectedModel = modelPaths.firstOrNull(), request = DEFAULT_REQUEST),
    )
    val uiState: StateFlow<PackingUiState> = state.asStateFlow()

    private var nextChecklistId = 0

    fun updateRequest(request: String) = state.update { it.copy(request = request) }

    fun selectModel(modelPath: String) = state.update { it.copy(selectedModel = modelPath) }

    fun loadExample() = show(response = recordedExpressResponse, phase = GenerationPhase.Ready(elapsed = null))

    fun generate() {
        val current = state.value
        val modelPath = current.selectedModel ?: return
        if (!current.canGenerate) return
        state.update { it.copy(phase = GenerationPhase.Generating) }
        viewModelScope.launch {
            val started = timeSource.markNow()
            try {
                val response = generator.generate(
                    modelPath = modelPath,
                    prompt = expressPrompt(request = current.request, signatures = signatures),
                )
                show(response = response, phase = GenerationPhase.Ready(elapsed = started.elapsedNow()))
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                state.update { it.copy(phase = GenerationPhase.Failed(reason = exception.message ?: "Generation failed")) }
            }
        }
    }

    private fun show(response: String, phase: GenerationPhase) {
        val checklist = Checklist(
            id = nextChecklistId++,
            messages = compiler.compile(response = response, surfaceId = PACKING_SURFACE_ID),
        )
        state.update { it.copy(phase = phase, checklist = checklist) }
    }
}
