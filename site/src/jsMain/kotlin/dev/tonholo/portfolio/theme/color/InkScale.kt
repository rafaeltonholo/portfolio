package dev.tonholo.portfolio.theme.color

import com.varabyte.kobweb.compose.ui.graphics.Color

data class InkScale(val ink: Color, val ink2: Color, val ink3: Color, val ink4: Color)

private const val LIGHT_INK = 0x141311
private const val LIGHT_INK_2 = 0x1D1B17
private const val LIGHT_INK_3 = 0x2A2824
private const val LIGHT_INK_4 = 0x3A3833

private const val DARK_INK = 0xF2EFE6
private const val DARK_INK_2 = 0xE4E0D3
private const val DARK_INK_3 = 0xB8B3A5
private const val DARK_INK_4 = 0x7E7A70

object Ink {
    val light = InkScale(
        ink = Color.rgb(LIGHT_INK),
        ink2 = Color.rgb(LIGHT_INK_2),
        ink3 = Color.rgb(LIGHT_INK_3),
        ink4 = Color.rgb(LIGHT_INK_4),
    )
    val dark = InkScale(
        ink = Color.rgb(DARK_INK),
        ink2 = Color.rgb(DARK_INK_2),
        ink3 = Color.rgb(DARK_INK_3),
        ink4 = Color.rgb(DARK_INK_4),
    )
}
