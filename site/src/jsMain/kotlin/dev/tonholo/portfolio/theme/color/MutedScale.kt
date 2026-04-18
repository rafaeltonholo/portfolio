package dev.tonholo.portfolio.theme.color

import com.varabyte.kobweb.compose.ui.graphics.Color

data class MutedScale(val muted: Color, val muted2: Color)

private const val LIGHT_MUTED = 0x6B675E
private const val LIGHT_MUTED_2 = 0x8A857A

private const val DARK_MUTED = 0x8A857A
private const val DARK_MUTED_2 = 0x6B675E

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
