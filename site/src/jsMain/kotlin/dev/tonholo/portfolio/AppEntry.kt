package dev.tonholo.portfolio

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import com.varabyte.kobweb.core.App
import com.varabyte.kobweb.silk.SilkApp
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.analytics.UmamiInjector
import dev.tonholo.portfolio.extensions.InstallRevealObserver
import dev.tonholo.portfolio.state.LocalLocaleState
import dev.tonholo.portfolio.state.LocalThemeState
import dev.tonholo.portfolio.state.rememberLocaleState
import dev.tonholo.portfolio.state.rememberThemeState
import dev.tonholo.portfolio.theme.PortfolioTheme

@App
@Composable
fun PortfolioApp(content: @Composable () -> Unit) {
    SilkApp {
        LaunchedEffect(Unit) {
            UmamiInjector.inject()
        }
        val themeState = rememberThemeState()
        val localeState = rememberLocaleState()

        // Silk remains the source of truth for which color-scoped styles render.
        // ThemeState persists the user's choice; this effect mirrors the chosen
        // mode into Silk's MutableState so PortfolioTheme and all Silk styles
        // recompose when the user toggles the theme.
        val silkColorMode = ColorMode.currentState
        LaunchedEffect(themeState.current) {
            silkColorMode.value = themeState.current
        }

        CompositionLocalProvider(
            LocalThemeState provides themeState,
            LocalLocaleState provides localeState,
        ) {
            PortfolioTheme {
                InstallRevealObserver()
                content()
            }
        }
    }
}
