package dev.tonholo.portfolio.theme.layout

import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.cssRem

private const val PAD_Y_BASE_REM = 5.0
private const val PAD_Y_SMALL_BASE_REM = 2.5
private const val GAP_BASE_REM = 1.5

enum class Density(val multiplier: Double) {
    Compact(multiplier = 0.72),
    Normal(multiplier = 1.0),
    Spacious(multiplier = 1.25),
}

data class SpacingScale(
    val padY: CSSSizeValue<CSSUnit.rem>,
    val padYSmall: CSSSizeValue<CSSUnit.rem>,
    val gap: CSSSizeValue<CSSUnit.rem>,
) {
    companion object {
        fun from(density: Density) = SpacingScale(
            padY = (PAD_Y_BASE_REM * density.multiplier).cssRem,
            padYSmall = (PAD_Y_SMALL_BASE_REM * density.multiplier).cssRem,
            gap = (GAP_BASE_REM * density.multiplier).cssRem,
        )
    }
}
