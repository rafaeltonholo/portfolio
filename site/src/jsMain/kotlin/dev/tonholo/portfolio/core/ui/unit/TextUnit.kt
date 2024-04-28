package dev.tonholo.portfolio.core.ui.unit

import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.cssRem

val Int.sp: CSSSizeValue<CSSUnit.rem>
    get() = (this.toFloat() / 16).cssRem
val Double.sp: CSSSizeValue<CSSUnit.rem>
    get() = (this / 16).cssRem
val Float.sp: CSSSizeValue<CSSUnit.rem>
    get() = (this / 16).cssRem
