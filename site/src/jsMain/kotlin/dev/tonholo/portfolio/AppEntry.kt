package dev.tonholo.portfolio

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.varabyte.kobweb.core.App
import com.varabyte.kobweb.silk.SilkApp
import dev.tonholo.portfolio.analytics.UmamiInjector

@App
@Composable
fun PortfolioApp(content: @Composable () -> Unit) {
    SilkApp {
        LaunchedEffect(Unit) {
            UmamiInjector.inject()
        }
        content()
    }
}
