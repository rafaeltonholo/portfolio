package dev.tonholo.portfolio.core.ui.theme.color

import androidx.compose.runtime.Stable
import com.varabyte.kobweb.compose.ui.graphics.Color

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
