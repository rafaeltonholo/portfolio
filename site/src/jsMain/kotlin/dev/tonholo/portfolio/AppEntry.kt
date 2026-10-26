package dev.tonholo.portfolio

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.varabyte.kobweb.core.App
import com.varabyte.kobweb.silk.SilkApp
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.analytics.AnalyticsManager
import dev.tonholo.portfolio.analytics.LocalAnalyticsManager
import dev.tonholo.portfolio.extensions.InstallRevealObserver
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.locale.stringsFor
import dev.tonholo.portfolio.state.LocalLocaleState
import dev.tonholo.portfolio.state.LocalThemeState
import dev.tonholo.portfolio.state.rememberLocaleState
import dev.tonholo.portfolio.state.rememberThemeState
import dev.tonholo.portfolio.theme.PortfolioTheme

@App
@Composable
fun PortfolioApp(content: @Composable () -> Unit) {
    SilkApp {
        val analytics = remember { AnalyticsManager() }
        val themeState = rememberThemeState()
        val localeState = rememberLocaleState()
        val silkColorMode = ColorMode.currentState
        if (silkColorMode.value != themeState.current) {
            silkColorMode.value = themeState.current
        }
        LaunchedEffect(themeState.current) {
            silkColorMode.value = themeState.current
        }
        val strings = stringsFor(locale = localeState.current)
        CompositionLocalProvider(
            LocalThemeState provides themeState,
            LocalLocaleState provides localeState,
            LocalStrings provides strings,
            LocalAnalyticsManager provides analytics,
        ) {
            PortfolioTheme {
                InstallRevealObserver()
                content()
            }
        }
    }
}
