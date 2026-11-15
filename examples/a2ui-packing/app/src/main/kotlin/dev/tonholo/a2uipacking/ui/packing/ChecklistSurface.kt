package dev.tonholo.a2uipacking.ui.packing

import androidx.a2ui.compose.runtime.A2uiMessageParser
import androidx.a2ui.compose.ui.A2uiMessageProcessor
import androidx.a2ui.compose.ui.catalog.A2uiBasicCatalogV1
import androidx.a2ui.model.processor.processInput
import androidx.a2ui.model.protocol.A2uiExecutionContext
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.a2ui.A2uiSurface
import androidx.compose.material3.a2ui.catalog.MaterialA2uiBasicCatalogV1Defaults
import androidx.compose.material3.a2ui.catalog.materialA2uiBasicCatalogV1
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.tonholo.a2uipacking.PACKING_SURFACE_ID
import dev.tonholo.a2uipacking.deleteSurfaceMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** Renders [checklist] with the AndroidX A2UI renderer, replacing the previous checklist when a new one arrives. */
@Composable
fun ChecklistSurface(checklist: Checklist, modifier: Modifier = Modifier) {
    val processor = remember { A2uiMessageProcessor(catalogs = listOf(packingCatalog())) }
    val parser = remember { A2uiMessageParser() }
    val surfaces by processor.activeSurfaces.collectAsStateWithLifecycle()

    LaunchedEffect(processor) { withContext(Dispatchers.Default) { processor.collectMessages() } }
    LaunchedEffect(checklist.id) {
        if (processor.activeSurfaces.value.isNotEmpty()) {
            processor.processInput(parser, deleteSurfaceMessage(PACKING_SURFACE_ID))
        }
        checklist.messages.forEach { processor.processInput(parser, it.toString()) }
    }

    Column(modifier = modifier) {
        surfaces.forEach { surface -> A2uiSurface(surfaceModel = surface) }
    }
}

/** The Material 3 basic catalog with media and URL handling turned off, since packing lists don't use them. */
fun packingCatalog() = materialA2uiBasicCatalogV1(
    image = MaterialA2uiBasicCatalogV1Defaults.image { _, _, _, _, _ -> },
    video = MaterialA2uiBasicCatalogV1Defaults.video { _, _, _ -> },
    audioPlayer = MaterialA2uiBasicCatalogV1Defaults.audioPlayer { _, _, _, _ -> },
    functions = object : A2uiBasicCatalogV1.Functions {
        override fun openUrl(url: String, executionContext: A2uiExecutionContext) = Unit
    },
)
