package dev.tonholo.portfolio.extensions

import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.padding
import org.jetbrains.compose.web.css.px

fun Modifier.padding(
    vertical: CSSLengthOrPercentageNumericValue = 0.px,
    horizontal: CSSLengthOrPercentageNumericValue = 0.px,
) = this then Modifier.padding(topBottom = vertical, leftRight = horizontal)
