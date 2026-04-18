package dev.tonholo.portfolio.theme.color

import com.varabyte.kobweb.compose.ui.graphics.Color

// Archived alternate accent palettes - not wired to any UI toggle in v1.
// Preserved per PRD section 20 for future toggling.
// All pairs pass WCAG AA for UI text (4.5:1).

private const val LIGHT_PURPLE = 0x5B3AD9
private const val LIGHT_PURPLE_SOFT = 0xD6C8FC
private const val LIGHT_PURPLE_INK = 0xF5F1FF
private const val DARK_PURPLE = 0x9D83FF
private const val DARK_PURPLE_SOFT = 0x2A1F52
private const val DARK_PURPLE_INK = 0x0A0420

private const val LIGHT_AMBER = 0x9D6612
private const val LIGHT_AMBER_SOFT = 0xF2D9A8
private const val LIGHT_AMBER_INK = 0xFFF8EE
private const val DARK_AMBER = 0xE8A33D
private const val DARK_AMBER_SOFT = 0x4A2E0A
private const val DARK_AMBER_INK = 0x140C02

private const val LIGHT_CRIMSON = 0xB02A34
private const val LIGHT_CRIMSON_SOFT = 0xF4C5C9
private const val LIGHT_CRIMSON_INK = 0xFFF4F4
private const val DARK_CRIMSON = 0xFF6670
private const val DARK_CRIMSON_SOFT = 0x4F1318
private const val DARK_CRIMSON_INK = 0x160305

object AccentAlternates {
    val purple = AccentSwatchPair(
        light = AccentSwatch(
            accent = Color.rgb(LIGHT_PURPLE),
            accentSoft = Color.rgb(LIGHT_PURPLE_SOFT),
            accentInk = Color.rgb(LIGHT_PURPLE_INK),
        ),
        dark = AccentSwatch(
            accent = Color.rgb(DARK_PURPLE),
            accentSoft = Color.rgb(DARK_PURPLE_SOFT),
            accentInk = Color.rgb(DARK_PURPLE_INK),
        ),
    )

    val amber = AccentSwatchPair(
        light = AccentSwatch(
            accent = Color.rgb(LIGHT_AMBER),
            accentSoft = Color.rgb(LIGHT_AMBER_SOFT),
            accentInk = Color.rgb(LIGHT_AMBER_INK),
        ),
        dark = AccentSwatch(
            accent = Color.rgb(DARK_AMBER),
            accentSoft = Color.rgb(DARK_AMBER_SOFT),
            accentInk = Color.rgb(DARK_AMBER_INK),
        ),
    )

    val crimson = AccentSwatchPair(
        light = AccentSwatch(
            accent = Color.rgb(LIGHT_CRIMSON),
            accentSoft = Color.rgb(LIGHT_CRIMSON_SOFT),
            accentInk = Color.rgb(LIGHT_CRIMSON_INK),
        ),
        dark = AccentSwatch(
            accent = Color.rgb(DARK_CRIMSON),
            accentSoft = Color.rgb(DARK_CRIMSON_SOFT),
            accentInk = Color.rgb(DARK_CRIMSON_INK),
        ),
    )
}
