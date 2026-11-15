package dev.tonholo.a2uipacking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.tonholo.a2ui.express.ExpressCatalog
import dev.tonholo.a2ui.express.ExpressCompiler
import dev.tonholo.a2uipacking.ui.packing.PackingActions
import dev.tonholo.a2uipacking.ui.packing.PackingScreen
import dev.tonholo.a2uipacking.ui.packing.PackingViewModel
import dev.tonholo.a2uipacking.ui.theme.A2UIPackingTheme

const val CATALOG_ASSET = "a2ui/basic_catalog_v0_9.json"
private const val MODEL_EXTENSION = "litertlm"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent { A2UIPackingTheme { PackingRoute() } }
    }
}

@Composable
private fun PackingRoute() {
    val context = LocalContext.current.applicationContext
    val viewModel = viewModel {
        val catalog = ExpressCatalog.fromSchema(context.assets.open(CATALOG_ASSET).use { it.reader().readText() })
        PackingViewModel(
            compiler = ExpressCompiler(catalog),
            signatures = catalog.promptSignatures(packingComponents),
            generator = ::generateResponse,
            modelPaths = context.filesDir.listFiles { file -> file.extension == MODEL_EXTENSION }
                .orEmpty()
                .map { it.absolutePath }
                .sorted(),
        )
    }
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val actions = remember(viewModel) {
        PackingActions(
            onRequestChange = viewModel::updateRequest,
            onSelectModel = viewModel::selectModel,
            onGenerate = viewModel::generate,
            onLoadExample = viewModel::loadExample,
        )
    }
    PackingScreen(state = state, actions = actions)
}
