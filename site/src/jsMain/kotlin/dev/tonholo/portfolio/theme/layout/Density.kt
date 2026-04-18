package dev.tonholo.portfolio.theme.layout

import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.px

private const val PAD_Y_BASE = 80.0
private const val PAD_Y_SMALL_BASE = 40.0
private const val GAP_BASE = 24.0

enum class Density(val multiplier: Double) {
    Compact(multiplier = 0.72),
    Normal(multiplier = 1.0),
    Spacious(multiplier = 1.25),
}

data class SpacingScale(
    val padY: CSSSizeValue<CSSUnit.px>,
    val padYSmall: CSSSizeValue<CSSUnit.px>,
    val gap: CSSSizeValue<CSSUnit.px>,
) {
    companion object {
        fun from(density: Density) = SpacingScale(
            padY = (PAD_Y_BASE * density.multiplier).toInt().px,
            padYSmall = (PAD_Y_SMALL_BASE * density.multiplier).toInt().px,
            gap = (GAP_BASE * density.multiplier).toInt().px,
        )
    }
}
