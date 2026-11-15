package dev.tonholo.a2uipacking

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.ai.edge.litertlm.ConversationConfig
import com.google.ai.edge.litertlm.Engine
import com.google.ai.edge.litertlm.EngineConfig
import com.google.ai.edge.litertlm.ExperimentalApi
import com.google.ai.edge.litertlm.ExperimentalFlags
import com.google.ai.edge.litertlm.SamplerConfig
import dev.tonholo.a2ui.express.ExpressCatalog
import dev.tonholo.a2ui.express.ExpressCompiler
import dev.tonholo.a2ui.express.ExpressException
import java.io.File
import kotlin.time.measureTimedValue
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Test
import org.junit.runner.RunWith

private const val REPEATS = 2
private const val MAX_OUTPUT_TOKENS = 512

private val requests = listOf(
    "Pack for a weekend beach trip",
    "Pack for a rainy hiking day",
    "Pack for a ski weekend",
    "Pack for a two-day business trip",
    "Pack for a picnic in the park",
)

/**
 * Generates the same checklists as plain JSON and as Express with one model, and appends one JSON line per run
 * to `files/benchmark-<model>.jsonl`. Pass the model file name with `-e model <file>.litertlm`.
 */
@RunWith(AndroidJUnit4::class)
class FormatBenchmarkTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val modelFile = InstrumentationRegistry.getArguments().getString("model") ?: "model.litertlm"
    private val catalog = ExpressCatalog.fromSchema(context.assets.open(CATALOG_ASSET).use { it.reader().readText() })
    private val compiler = ExpressCompiler(catalog)
    private val results = File(context.filesDir, "benchmark-${modelFile.substringBefore('.')}.jsonl")

    @OptIn(ExperimentalApi::class)
    @Test
    fun given_one_model_when_generating_json_and_express_then_each_run_is_recorded() = runBlocking {
        ExperimentalFlags.enableBenchmark = true
        val signatures = catalog.promptSignatures(packingComponents)
        Engine(EngineConfig(modelPath = File(context.filesDir, modelFile).absolutePath)).use { engine ->
            engine.initialize()
            repeat(REPEATS) { attempt ->
                requests.forEachIndexed { index, request ->
                    record(engine, Run("json", index, attempt, jsonPrompt(request)))
                    record(engine, Run("express", index, attempt, expressPrompt(request, signatures)))
                }
            }
        }
    }

    private data class Run(val format: String, val request: Int, val attempt: Int, val prompt: String)

    @OptIn(ExperimentalApi::class)
    private suspend fun record(engine: Engine, run: Run) {
        val config = ConversationConfig(
            samplerConfig = SamplerConfig(topK = 10, topP = 0.95, temperature = 0.2),
            maxOutputToken = MAX_OUTPUT_TOKENS,
        )
        engine.createConversation(config).use { conversation ->
            val (output, duration) = measureTimedValue {
                buildString { conversation.sendMessageAsync(run.prompt).collect { append(it) } }
            }
            val benchmark = conversation.getBenchmarkInfo()
            val verdict = verdictFor(run.format, output)
            val line = JsonObject(
                mapOf(
                    "model" to JsonPrimitive(modelFile),
                    "format" to JsonPrimitive(run.format),
                    "request" to JsonPrimitive(run.request),
                    "attempt" to JsonPrimitive(run.attempt),
                    "ms" to JsonPrimitive(duration.inWholeMilliseconds),
                    "ttftSeconds" to JsonPrimitive(benchmark.timeToFirstTokenInSecond),
                    "decodeTokens" to JsonPrimitive(benchmark.lastDecodeTokenCount),
                    "decodeTokensPerSecond" to JsonPrimitive(benchmark.lastDecodeTokensPerSecond),
                    "chars" to JsonPrimitive(output.length),
                    "problem" to JsonPrimitive(verdict.problem),
                    "items" to JsonPrimitive(verdict.itemCount),
                    "copiedExample" to JsonPrimitive(verdict.copiedExample),
                    "output" to JsonPrimitive(output),
                ),
            )
            results.appendText("$line\n")
        }
    }

    private fun verdictFor(format: String, output: String): SurfaceVerdict {
        val messages = try {
            if (format == "express") compiler.compile(output, PACKING_SURFACE_ID) else jsonObjectsIn(output)
        } catch (exception: ExpressException) {
            return SurfaceVerdict(problem = "express: ${exception.message}", itemCount = 0, copiedExample = false)
        } catch (exception: IllegalArgumentException) {
            return SurfaceVerdict(problem = "not JSON: ${exception.message?.take(120)}", itemCount = 0, copiedExample = false)
        }
        return checkSurface(messages)
    }
}
