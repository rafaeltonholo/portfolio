package dev.tonholo.portfolio.showcase.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val LimeGreen100 = Color(0xFFF0F8EC)
val LimeGreen90 = Color(0xFF9DD49E)
val LimeGreen50 = Color(0xFF36693D)
val LimeGreen0 = Color(0xFF013913)
val Neutral100 = Color(0xFFFFFFFF)
val Neutral90 = Color(0xFFFEFEFE)
val Neutral80 = Color(0xFFF8F8F8)
val Neutral70 = Color(0xFFF0F0F0)
val Neutral60 = Color(0xFFE3E2E2)
val Neutral40 = Color(0xFFDCDCDC)
val Neutral30 = Color(0xFF404040)
val Neutral20 = Color(0xFF3F3F3F)
val Neutral10 = Color(0xFF1A1A1F)
val Neutral5 = Color(0xFF1A1A1A)
val Neutral0 = Color(0xFF0B0F0B)

internal val DarkColorScheme = darkColorScheme(
    primary = LimeGreen90,
    onPrimary = LimeGreen0,
    background = Neutral0,
    onBackground = Neutral70,
    surface = Neutral5,
    surfaceVariant = Neutral10,
    onSurface = Neutral70,
    onSurfaceVariant = Neutral70,
    outline = Neutral30,
)

internal val LightColorScheme = lightColorScheme(
    primary = LimeGreen50,
    onPrimary = Neutral100,
    background = Neutral90,
    onBackground = Neutral5,
    surface = LimeGreen100,
    surfaceVariant = Neutral80,
    onSurface = Neutral5,
    onSurfaceVariant = Neutral5,
    outline = Neutral40,
)
