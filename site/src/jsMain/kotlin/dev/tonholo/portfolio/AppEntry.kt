package dev.tonholo.portfolio

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.core.App
import dev.tonholo.portfolio.ui.theme.Theme

@App
@Composable
fun AppEntry(content: @Composable () -> Unit) {
    Theme {
        content()
    }
}
