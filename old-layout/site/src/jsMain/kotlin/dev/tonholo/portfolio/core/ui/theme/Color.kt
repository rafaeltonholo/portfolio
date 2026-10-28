package dev.tonholo.portfolio.core.ui.theme

import com.varabyte.kobweb.compose.ui.graphics.Color
import dev.tonholo.portfolio.core.ui.theme.color.ColorScheme

val LimeGreen100 = Color.argb(0xFFF0F8EC)
val LimeGreen90 = Color.argb(0xFF9DD49E)
val LimeGreen50 = Color.argb(0xFF36693D)
val LimeGreen0 = Color.argb(0xFF013913)
val Neutral100 = Color.argb(0xFFFFFFFF)
val Neutral90 = Color.argb(0xFFFEFEFE)
val Neutral80 = Color.argb(0xFFF8F8F8)
val Neutral70 = Color.argb(0xFFF0F0F0)
val Neutral60 = Color.argb(0xFFE3E2E2)
val Neutral40 = Color.argb(0xFFDCDCDC)
val Neutral30 = Color.argb(0xFF404040)
val Neutral20 = Color.argb(0xFF3F3F3F)
val Neutral10 = Color.argb(0xFF1A1A1F)
val Neutral5 = Color.argb(0xFF1A1A1A)
val Neutral0 = Color.argb(0xFF0B0F0B)

internal val DarkColorScheme = ColorScheme(
    primary = LimeGreen90,
    onPrimary = LimeGreen0,
    background = Neutral0,
    onBackground = Neutral70,
    onBackgroundVariant = Neutral60,
    surface = Neutral5,
    surfaceVariant = Neutral10,
    onSurface = Neutral70,
    onSurfaceVariant = Neutral70,
    outline = Neutral30,
)

internal val LightColorScheme = ColorScheme(
    primary = LimeGreen50,
    onPrimary = Neutral100,
    background = Neutral90,
    onBackground = Neutral5,
    onBackgroundVariant = Neutral20,
    surface = LimeGreen100,
    surfaceVariant = Neutral80,
    onSurface = Neutral5,
    onSurfaceVariant = Neutral5,
    outline = Neutral40,
)
