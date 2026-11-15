package dev.tonholo.a2uipacking

import androidx.a2ui.compose.runtime.A2uiMessageParser
import androidx.a2ui.compose.ui.A2uiMessageProcessor
import androidx.a2ui.model.processor.processInput
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.a2ui.A2uiSurface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import dev.tonholo.a2ui.express.ExpressCatalog
import dev.tonholo.a2ui.express.ExpressCompiler
import dev.tonholo.a2uipacking.ui.packing.packingCatalog
import dev.tonholo.a2uipacking.ui.theme.A2UIPackingTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.junit.Test

/**
 * Sends the recorded checklist to the AndroidX renderer in two batches on a real clock and holds the screen for
 * nine seconds, so the result can be read with `android layout` or a screenshot. It has no assertions because the
 * race it probes is intermittent.
 *
 * Arguments: `-e order components-first|data-first`, `-e collector main|default`, and `-e gap <milliseconds>`.
 */
class RendererRaceProbe {
    private val arguments = InstrumentationRegistry.getArguments()
    private val componentsFirst = arguments.getString("order") == "components-first"
    private val collectOnMain = arguments.getString("collector") == "main"
    private val gapMillis = arguments.getString("gap")?.toLong() ?: 1_000L

    @Test
    fun givenTwoMessageBatches_whenSentOnARealClock_thenTheScreenIsHeldForInspection() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val compiler = ExpressCompiler(
            ExpressCatalog.fromSchema(context.assets.open(CATALOG_ASSET).use { it.reader().readText() }),
        )
        val messages = compiler.compile(response = recordedExpressResponse, surfaceId = PACKING_SURFACE_ID)
        val create = messages.filter { "createSurface" in it }
        val data = messages.filter { "updateDataModel" in it }
        val components = messages.filter { "updateComponents" in it }
        val (first, second) = if (componentsFirst) create + components to data else create + data to components
        val processor = A2uiMessageProcessor(catalogs = listOf(packingCatalog()))
        val parser = A2uiMessageParser()

        ActivityScenario.launch(ComponentActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                activity.setContent {
                    A2UIPackingTheme {
                        val surfaces by processor.activeSurfaces.collectAsState()
                        LaunchedEffect(processor) {
                            if (collectOnMain) {
                                processor.collectMessages()
                            } else {
                                withContext(Dispatchers.Default) { processor.collectMessages() }
                            }
                        }
                        LaunchedEffect(Unit) {
                            first.forEach { processor.processInput(parser, it.toString()) }
                            delay(gapMillis)
                            second.forEach { processor.processInput(parser, it.toString()) }
                        }
                        Surface {
                            Card(
                                modifier = Modifier.safeDrawingPadding().padding(16.dp).fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                                ),
                            ) {
                                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                                    surfaces.forEach { A2uiSurface(surfaceModel = it) }
                                }
                            }
                        }
                    }
                }
            }
            Thread.sleep(9_000)
        }
    }
}
