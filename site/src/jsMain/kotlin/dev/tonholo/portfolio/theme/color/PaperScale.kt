package dev.tonholo.portfolio.theme.color

import com.varabyte.kobweb.compose.ui.graphics.Color

data class PaperScale(val paper: Color, val paper2: Color, val paper3: Color)

private const val LIGHT_PAPER = 0xF7F5EF
private const val LIGHT_PAPER_2 = 0xEFECE3
private const val LIGHT_PAPER_3 = 0xE5E1D4

private const val DARK_PAPER = 0x15130F
private const val DARK_PAPER_2 = 0x1D1B16
private const val DARK_PAPER_3 = 0x25231D

object Paper {
    val light = PaperScale(
        paper = Color.rgb(LIGHT_PAPER),
        paper2 = Color.rgb(LIGHT_PAPER_2),
        paper3 = Color.rgb(LIGHT_PAPER_3),
    )
    val dark = PaperScale(
        paper = Color.rgb(DARK_PAPER),
        paper2 = Color.rgb(DARK_PAPER_2),
        paper3 = Color.rgb(DARK_PAPER_3),
    )
}
