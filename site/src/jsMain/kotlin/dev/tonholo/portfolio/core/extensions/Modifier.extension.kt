package dev.tonholo.portfolio.core.extensions

import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.padding
import dev.tonholo.portfolio.core.foundation.layout.PaddingValues
import dev.tonholo.portfolio.core.foundation.layout.calculateBottomPadding
import dev.tonholo.portfolio.core.foundation.layout.calculateLeftPadding
import dev.tonholo.portfolio.core.foundation.layout.calculateRightPadding
import dev.tonholo.portfolio.core.foundation.layout.calculateTopPadding
import org.jetbrains.compose.web.css.px

fun Modifier.padding(
    vertical: CSSLengthOrPercentageNumericValue = 0.px,
    horizontal: CSSLengthOrPercentageNumericValue = 0.px,
) = this then Modifier.padding(topBottom = vertical, leftRight = horizontal)

fun Modifier.padding(
    paddingValues: PaddingValues,
) = this then Modifier.padding {
    val start = paddingValues.calculateLeftPadding()
    if (start != 0.px) {
        left(start)
    }
    val top = paddingValues.calculateTopPadding()
    if (top != 0.px) {
        top(top)
    }
    val end = paddingValues.calculateRightPadding()
    if (end != 0.px) {
        right(end)
    }
    val bottom = paddingValues.calculateBottomPadding()
    if (bottom != 0.px) {
        bottom(bottom)
    }
}
