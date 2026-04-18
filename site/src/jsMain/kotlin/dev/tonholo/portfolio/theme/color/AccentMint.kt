package dev.tonholo.portfolio.theme.color

import com.varabyte.kobweb.compose.ui.graphics.Color

private const val LIGHT_ACCENT = 0x3D8A54
private const val LIGHT_ACCENT_SOFT = 0xC8E5CF
private const val LIGHT_ACCENT_INK = 0xF2F7F4

private const val DARK_ACCENT = 0x7DD198
private const val DARK_ACCENT_SOFT = 0x1D4028
private const val DARK_ACCENT_INK = 0x051A0C

object AccentMint {
    val light = AccentSwatch(
        accent = Color.rgb(LIGHT_ACCENT),
        accentSoft = Color.rgb(LIGHT_ACCENT_SOFT),
        accentInk = Color.rgb(LIGHT_ACCENT_INK),
    )
    val dark = AccentSwatch(
        accent = Color.rgb(DARK_ACCENT),
        accentSoft = Color.rgb(DARK_ACCENT_SOFT),
        accentInk = Color.rgb(DARK_ACCENT_INK),
    )
}
