package dev.tonholo.portfolio.core.ui.theme.color

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.silk.theme.colors.palette.MutablePalette
import com.varabyte.kobweb.silk.theme.colors.palette.background
import com.varabyte.kobweb.silk.theme.colors.palette.color

@Immutable
data class ColorScheme(
    val primary: Color = Color.Unspecified,
    val onPrimary: Color = Color.Unspecified,
    val primaryContainer: Color = Color.Unspecified,
    val onPrimaryContainer: Color = Color.Unspecified,
    val inversePrimary: Color = Color.Unspecified,
    val secondary: Color = Color.Unspecified,
    val onSecondary: Color = Color.Unspecified,
    val secondaryContainer: Color = Color.Unspecified,
    val onSecondaryContainer: Color = Color.Unspecified,
    val tertiary: Color = Color.Unspecified,
    val onTertiary: Color = Color.Unspecified,
    val tertiaryContainer: Color = Color.Unspecified,
    val onTertiaryContainer: Color = Color.Unspecified,
    val background: Color = Color.Unspecified,
    val onBackground: Color = Color.Unspecified,
    val surface: Color = Color.Unspecified,
    val onSurface: Color = Color.Unspecified,
    val surfaceVariant: Color = Color.Unspecified,
    val onSurfaceVariant: Color = Color.Unspecified,
    val surfaceTint: Color = Color.Unspecified,
    val inverseSurface: Color = Color.Unspecified,
    val inverseOnSurface: Color = Color.Unspecified,
    val error: Color = Color.Unspecified,
    val onError: Color = Color.Unspecified,
    val errorContainer: Color = Color.Unspecified,
    val onErrorContainer: Color = Color.Unspecified,
    val outline: Color = Color.Unspecified,
    val outlineVariant: Color = Color.Unspecified,
    val scrim: Color = Color.Unspecified,
    val surfaceBright: Color = Color.Unspecified,
    val surfaceDim: Color = Color.Unspecified,
    val surfaceContainer: Color = Color.Unspecified,
    val surfaceContainerHigh: Color = Color.Unspecified,
    val surfaceContainerHighest: Color = Color.Unspecified,
    val surfaceContainerLow: Color = Color.Unspecified,
    val surfaceContainerLowest: Color = Color.Unspecified,
)

fun MutablePalette.from(colorScheme: ColorScheme) {
    background = colorScheme.background
    color = colorScheme.onBackground
}

val Color.Companion.Unspecified: Color
    get() = "unset".unsafeCast<Color>()

internal val LocalColorScheme = staticCompositionLocalOf<ColorScheme> { error("not provided color scheme.") }

