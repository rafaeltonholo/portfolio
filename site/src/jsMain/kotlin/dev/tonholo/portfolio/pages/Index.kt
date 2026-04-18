package dev.tonholo.portfolio.pages

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.core.Page
import org.jetbrains.compose.web.dom.Text

@Page
@Composable
// Kobweb @Page route composables are never called from a parent composable, so no modifier is needed.
@Suppress("ModifierMissing")
fun HomePage() {
    Text("portfolio scaffold online")
}
