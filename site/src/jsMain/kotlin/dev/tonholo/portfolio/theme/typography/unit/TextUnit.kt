package dev.tonholo.portfolio.theme.typography.unit

import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.cssRem

const val DEFAULT_FONT_SIZE = 16

value class TextUnit(private val sp: CSSSizeValue<CSSUnit.rem>) : CSSSizeValue<CSSUnit.rem> by sp {
    override fun toString(): String = sp.toString()

    companion object {
        val Unspecified: TextUnit = "unset".unsafeCast<TextUnit>()
    }
}

val Int.sp: TextUnit
    get() = TextUnit((this.toFloat() / DEFAULT_FONT_SIZE).cssRem)

val Double.sp: TextUnit
    get() = TextUnit((this / DEFAULT_FONT_SIZE).cssRem)

val Float.sp: TextUnit
    get() = TextUnit((this / DEFAULT_FONT_SIZE).cssRem)
