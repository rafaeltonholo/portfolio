package dev.tonholo.portfolio.core.ui.theme

import androidx.compose.runtime.Stable
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors

val White = Colors.White
val LimeGreen100 = Color.argb(0xFFF0F8EC)
val LimeGreen90 = Color.argb(0xFF9DD49E)
val LimeGreen50 = Color.argb(0xFF36693D)
val LimeGreen0 = Color.argb(0xFF013913)
val Neutral100 = Color.argb(0xFFFEFEFE)
val Neutral90 = Color.argb(0xFFF0F0F0)
val Neutral80 = Color.argb(0xFFE3E2E2)
val Neutral30 = Color.argb(0xFF3F3F3F)
val Neutral10 = Color.argb(0xFF1A1A1F)
val Neutral0 = Color.argb(0xFF0B0F0B)

/**
 * Copies the existing color, changing only the provided values.
 */
@Stable
fun Color.copy(
    red: Float = if (this is Color.Rgb) this.redf else toRgb().redf,
    green: Float = if (this is Color.Rgb) greenf else toRgb().greenf,
    blue: Float = if (this is Color.Rgb) bluef else toRgb().bluef,
    alpha: Float = if (this is Color.Rgb) alphaf else toRgb().alphaf,
) = Color.rgba(red, green, blue, alpha)
