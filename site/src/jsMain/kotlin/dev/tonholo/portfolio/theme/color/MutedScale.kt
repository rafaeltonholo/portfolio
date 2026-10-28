package dev.tonholo.portfolio.theme.color

import com.varabyte.kobweb.compose.ui.graphics.Color

data class MutedScale(val muted: Color, val muted2: Color)

// Calibrated warm muted scale passing WCAG AA (≥4.5:1) on respective paper surfaces.
private const val LIGHT_MUTED = 0x5C5850
private const val LIGHT_MUTED_2 = 0x6E6960

private const val DARK_MUTED = 0x9E998E
private const val DARK_MUTED_2 = 0x8E8A80

object Muted {
    val light = MutedScale(
        muted = Color.rgb(LIGHT_MUTED),
        muted2 = Color.rgb(LIGHT_MUTED_2),
    )
    val dark = MutedScale(
        muted = Color.rgb(DARK_MUTED),
        muted2 = Color.rgb(DARK_MUTED_2),
    )
}
