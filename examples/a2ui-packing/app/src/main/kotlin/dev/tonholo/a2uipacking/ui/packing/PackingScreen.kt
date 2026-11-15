package dev.tonholo.a2uipacking.ui.packing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Callbacks for [PackingScreen], so the screen stays a plain function of [PackingUiState]. */
data class PackingActions(
    val onRequestChange: (String) -> Unit,
    val onSelectModel: (String) -> Unit,
    val onGenerate: () -> Unit,
    val onLoadExample: () -> Unit,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PackingScreen(state: PackingUiState, actions: PackingActions, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Packing assistant") },
                actions = { TextButton(onClick = actions.onLoadExample) { Text("Load example") } },
            )
        },
        bottomBar = {
            Surface(color = MaterialTheme.colorScheme.surfaceContainer) {
                RequestBar(
                    request = state.request,
                    canGenerate = state.canGenerate,
                    onRequestChange = actions.onRequestChange,
                    onGenerate = actions.onGenerate,
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.navigationBars.union(WindowInsets.ime))
                        .padding(16.dp),
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            ModelPicker(
                modelPaths = state.modelPaths,
                selectedModel = state.selectedModel,
                onSelect = actions.onSelectModel,
            )
            GenerationStatus(phase = state.phase, modelName = state.selectedModel?.modelName)
            val checklist = state.checklist
            if (checklist == null) {
                EmptyChecklist(modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp))
            } else {
                ChecklistCard(checklist = checklist, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}
