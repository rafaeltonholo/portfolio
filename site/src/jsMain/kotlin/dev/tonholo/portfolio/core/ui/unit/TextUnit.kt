package dev.tonholo.portfolio.core.ui.unit

import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.cssRem

const val DefaultFontSize = 16

val Int.sp: CSSSizeValue<CSSUnit.rem>
    get() = (this.toFloat() / DefaultFontSize).cssRem

val Double.sp: CSSSizeValue<CSSUnit.rem>
    get() = (this / DefaultFontSize).cssRem

val Float.sp: CSSSizeValue<CSSUnit.rem>
    get() = (this / DefaultFontSize).cssRem

val Int.dp: CSSSizeValue<CSSUnit.rem>
    get() = (this.toFloat() / DefaultFontSize).cssRem
val Double.dp: CSSSizeValue<CSSUnit.rem>
    get() = (this / DefaultFontSize).cssRem
val Float.dp: CSSSizeValue<CSSUnit.rem>
    get() = (this / DefaultFontSize).cssRem
