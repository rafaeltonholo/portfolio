package dev.tonholo.portfolio.theme.layout.unit

import dev.tonholo.portfolio.theme.typography.unit.DEFAULT_FONT_SIZE
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.cssRem

value class Dp(private val dp: CSSSizeValue<CSSUnit.rem>) : CSSSizeValue<CSSUnit.rem> by dp {
    override fun toString(): String = dp.toString()

    companion object {
        val Unspecified: Dp = "unset".unsafeCast<Dp>()
    }
}

val Int.dp: Dp
    get() = Dp((this.toFloat() / DEFAULT_FONT_SIZE).cssRem)
val Double.dp: Dp
    get() = Dp((this / DEFAULT_FONT_SIZE).cssRem)
val Float.dp: Dp
    get() = Dp((this / DEFAULT_FONT_SIZE).cssRem)
