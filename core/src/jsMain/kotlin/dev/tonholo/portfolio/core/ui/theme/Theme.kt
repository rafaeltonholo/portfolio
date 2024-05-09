package dev.tonholo.portfolio.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.varabyte.kobweb.core.KobwebApp
import com.varabyte.kobweb.silk.init.setSilkWidgetVariables
import com.varabyte.kobweb.silk.prepareSilkFoundation
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.core.ui.theme.color.ColorScheme
import dev.tonholo.portfolio.core.ui.theme.color.LocalColorScheme
import dev.tonholo.portfolio.core.ui.theme.typography.LocalTypography
import dev.tonholo.portfolio.core.ui.theme.typography.Typography
import kotlinx.browser.document
import kotlinx.browser.localStorage

private const val COLOR_MODE_KEY = "portfolio:colorMode"

data class ThemedValue<T>(
    val light: T,
    val dark: T,
)

@Composable
fun Theme(
    themedColorScheme: ThemedValue<ColorScheme>,
    typography: Typography,
    themedElevations: ThemedValue<Elevations>,
    content: @Composable (colorMode: ColorMode) -> Unit,
) {
    KobwebApp {
        prepareSilkFoundation {
            val colorMode by ColorMode.currentState
            LaunchedEffect(colorMode) {
                localStorage.setItem(COLOR_MODE_KEY, colorMode.name)
            }

            val (colorScheme, elevations) = when (colorMode) {
                ColorMode.LIGHT -> themedColorScheme.light to themedElevations.light
                ColorMode.DARK -> themedColorScheme.dark to themedElevations.dark
            }

            InitSilkWidgetVariables()

            CompositionLocalProvider(
                LocalColorScheme provides colorScheme,
                LocalTypography provides typography,
                LocalElevations provides elevations,
            ) {
                content(colorMode)
            }
        }
    }
}

@Composable
private fun InitSilkWidgetVariables() {
    val root = remember { document.body }
    root?.setSilkWidgetVariables()
}

object Theme {
    val colorScheme: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalColorScheme.current

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current

    val elevations: Elevations
        @Composable
        @ReadOnlyComposable
        get() = LocalElevations.current
}
