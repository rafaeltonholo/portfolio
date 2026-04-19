package dev.tonholo.portfolio.theme.color

import com.varabyte.kobweb.compose.ui.graphics.Color

// Light-mode accent is darkened from the handoff's `#3d8a54` so it passes
// WCAG AA (4.5:1) on the paper surface. The original value renders at ~4.2:1,
// which fails text contrast - nav active markers, meta labels, and CTA copy
// all read from this token.
private const val LIGHT_ACCENT = 0x2F7A45
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
