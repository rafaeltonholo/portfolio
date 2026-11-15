package dev.tonholo.a2uipacking.ui.packing

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.tonholo.a2uipacking.R
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay

private val suggestions = listOf(
    "Pack for a rainy weekend in Halifax",
    "Pack for a ski weekend",
    "Pack for a beach day",
    "Pack for a two-day business trip",
)

@Composable
fun ModelPicker(
    modelPaths: List<String>,
    selectedModel: String?,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("On-device model", style = MaterialTheme.typography.labelLarge)
        if (modelPaths.isEmpty()) {
            Text(
                "No .litertlm model in app storage yet. The README shows how to copy one.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            modelPaths.forEach { path ->
                FilterChip(
                    selected = path == selectedModel,
                    onClick = { onSelect(path) },
                    label = { Text(path.modelName) },
                    leadingIcon = { Icon(painterResource(R.drawable.ic_memory), contentDescription = null) },
                )
            }
        }
    }
}

@Composable
fun GenerationStatus(phase: GenerationPhase, modelName: String?, modifier: Modifier = Modifier) {
    when (phase) {
        GenerationPhase.Idle -> Unit
        GenerationPhase.Generating -> GeneratingCard(modelName = modelName, modifier = modifier)
        is GenerationPhase.Ready -> phase.elapsed?.let { elapsed ->
            AssistChip(
                onClick = {},
                label = { Text("Written on this phone in ${elapsed.inWholeSeconds} s") },
                leadingIcon = { Icon(painterResource(R.drawable.ic_auto_awesome), contentDescription = null) },
                modifier = modifier,
            )
        }
        is GenerationPhase.Failed -> Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
            ),
        ) {
            Text(phase.reason, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun GeneratingCard(modelName: String?, modifier: Modifier = Modifier) {
    var seconds by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1.seconds)
            seconds++
        }
    }
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                "${modelName ?: "The model"} is writing your checklist on this phone... $seconds s",
                style = MaterialTheme.typography.bodyMedium,
            )
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun ChecklistCard(checklist: Checklist, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
    ) {
        ChecklistSurface(checklist = checklist, modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp))
    }
}

@Composable
fun EmptyChecklist(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(
            painterResource(R.drawable.ic_luggage),
            contentDescription = null,
            modifier = Modifier.size(56.dp),
            tint = MaterialTheme.colorScheme.primary,
        )
        Text("Where are you going?", style = MaterialTheme.typography.titleLarge)
        Text(
            "A local model writes the checklist in A2UI Express, and the app renders it as native Compose.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun RequestBar(
    request: String,
    canGenerate: Boolean,
    onRequestChange: (String) -> Unit,
    onGenerate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            suggestions.forEach { suggestion ->
                AssistChip(onClick = { onRequestChange(suggestion) }, label = { Text(suggestion.removePrefix("Pack for a ")) })
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = request,
                onValueChange = onRequestChange,
                label = { Text("Packing request") },
                singleLine = true,
                modifier = Modifier.weight(1f),
            )
            FilledIconButton(onClick = onGenerate, enabled = canGenerate, modifier = Modifier.size(56.dp)) {
                Icon(painterResource(R.drawable.ic_send), contentDescription = "Generate")
            }
        }
    }
}
