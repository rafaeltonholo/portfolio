package dev.tonholo.a2uipacking.ui.packing

import dev.tonholo.a2ui.express.ExpressCatalog
import dev.tonholo.a2ui.express.ExpressCompiler
import dev.tonholo.a2uipacking.recordedExpressResponse
import java.io.File
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TestTimeSource
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

private const val GEMMA = "/models/gemma-4-E2B-it.litertlm"
private const val QWEN = "/models/Qwen3-0.6B.litertlm"

@OptIn(ExperimentalCoroutinesApi::class)
class PackingViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val timeSource = TestTimeSource()
    private val catalog = ExpressCatalog.fromSchema(File("src/main/assets/a2ui/basic_catalog_v0_9.json").readText())

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun viewModel(
        models: List<String> = listOf(GEMMA, QWEN),
        generator: ResponseGenerator = ResponseGenerator { _, _ -> recordedExpressResponse },
    ) = PackingViewModel(
        compiler = ExpressCompiler(catalog),
        signatures = "CheckBox(label, value)",
        generator = generator,
        modelPaths = models,
        timeSource = timeSource,
    )

    @Test
    fun `given model files - when the screen opens - then the first model is selected and generating is allowed`() {
        val state = viewModel().uiState.value

        assertEquals(GEMMA, state.selectedModel)
        assertEquals(GenerationPhase.Idle, state.phase)
        assertTrue(state.canGenerate)
    }

    @Test
    fun `given no model files - when the screen opens - then nothing is selected and generating is not allowed`() {
        val state = viewModel(models = emptyList()).uiState.value

        assertNull(state.selectedModel)
        assertFalse(state.canGenerate)
    }

    @Test
    fun `given a blank request - when checking the state - then generating is not allowed`() {
        val viewModel = viewModel()

        viewModel.updateRequest("   ")

        assertFalse(viewModel.uiState.value.canGenerate)
    }

    @Test
    fun `given a model that writes valid express - when generating - then the checklist is ready with how long it took`() = runTest(dispatcher) {
        var prompt = ""
        val viewModel = viewModel(
            generator = ResponseGenerator { _, received ->
                prompt = received
                timeSource += 12.seconds
                recordedExpressResponse
            },
        )
        viewModel.updateRequest("Pack for a rainy weekend")

        viewModel.generate()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(GenerationPhase.Ready(elapsed = 12.seconds), state.phase)
        assertEquals(3, state.checklist?.messages?.size)
        assertTrue(prompt.contains("Pack for a rainy weekend"))
    }

    @Test
    fun `given a slow model - when generating - then the phase is generating and generating again is not allowed`() = runTest(dispatcher) {
        val response = CompletableDeferred<String>()
        val viewModel = viewModel(generator = ResponseGenerator { _, _ -> response.await() })

        viewModel.generate()
        advanceUntilIdle()

        assertEquals(GenerationPhase.Generating, viewModel.uiState.value.phase)
        assertFalse(viewModel.uiState.value.canGenerate)
        response.complete(recordedExpressResponse)
    }

    @Test
    fun `given a model that writes broken express - when generating - then it fails with the compiler reason and keeps the old checklist`() = runTest(dispatcher) {
        val viewModel = viewModel(generator = ResponseGenerator { _, _ -> "<a2ui>\nroot = Column(\n</a2ui>" })
        viewModel.loadExample()
        val example = viewModel.uiState.value.checklist

        viewModel.generate()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.phase is GenerationPhase.Failed)
        assertTrue((state.phase as GenerationPhase.Failed).reason.startsWith("Line "))
        assertEquals(example, state.checklist)
    }

    @Test
    fun `given a model that crashes - when generating - then it fails with the error message`() = runTest(dispatcher) {
        val viewModel = viewModel(generator = ResponseGenerator { _, _ -> error("Model file not found") })

        viewModel.generate()
        advanceUntilIdle()

        assertEquals(GenerationPhase.Failed(reason = "Model file not found"), viewModel.uiState.value.phase)
    }

    @Test
    fun `given the example was loaded - when loading it again - then a new checklist replaces it`() {
        val viewModel = viewModel()

        viewModel.loadExample()
        val first = viewModel.uiState.value.checklist
        viewModel.loadExample()
        val second = viewModel.uiState.value.checklist

        assertEquals(GenerationPhase.Ready(elapsed = null), viewModel.uiState.value.phase)
        assertEquals(first?.messages, second?.messages)
        assertNotEquals(first?.id, second?.id)
    }

    @Test
    fun `given two models - when selecting the second - then it becomes the selected model`() {
        val viewModel = viewModel()

        viewModel.selectModel(QWEN)

        assertEquals(QWEN, viewModel.uiState.value.selectedModel)
    }
}
