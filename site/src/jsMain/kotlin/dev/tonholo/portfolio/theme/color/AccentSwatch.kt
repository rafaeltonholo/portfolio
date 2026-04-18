package dev.tonholo.portfolio.theme.color

import com.varabyte.kobweb.compose.ui.graphics.Color

data class AccentSwatch(val accent: Color, val accentSoft: Color, val accentInk: Color)

data class AccentSwatchPair(val light: AccentSwatch, val dark: AccentSwatch)
