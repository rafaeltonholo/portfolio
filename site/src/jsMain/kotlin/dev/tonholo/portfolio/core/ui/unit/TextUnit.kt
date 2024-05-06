package dev.tonholo.portfolio.core.ui.unit

import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.cssRem

const val DefaultFontSize = 16

val Int.sp: TextUnit
    get() = TextUnit((this.toFloat() / DefaultFontSize).cssRem)

val Double.sp: TextUnit
    get() = TextUnit((this / DefaultFontSize).cssRem)

val Float.sp: TextUnit
    get() = TextUnit((this / DefaultFontSize).cssRem)

val Int.dp: Dp
    get() = Dp((this.toFloat() / DefaultFontSize).cssRem)
val Double.dp: Dp
    get() = Dp((this / DefaultFontSize).cssRem)
val Float.dp: Dp
    get() = Dp((this / DefaultFontSize).cssRem)


value class Dp(private val dp: CSSSizeValue<CSSUnit.rem>) : CSSSizeValue<CSSUnit.rem> by dp {
    override fun toString(): String = dp.toString()

    companion object {
        val Unspecified: Dp = "unset".unsafeCast<Dp>()
    }
}

value class TextUnit(private val sp: CSSSizeValue<CSSUnit.rem>) : CSSSizeValue<CSSUnit.rem> by sp {
    override fun toString(): String = sp.toString()

    companion object {
        val Unspecified: Dp = "unset".unsafeCast<Dp>()
    }
}
