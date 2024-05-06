package dev.tonholo.portfolio.core.extensions

import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import dev.tonholo.portfolio.core.foundation.layout.PaddingValues
import dev.tonholo.portfolio.core.foundation.layout.calculateBottomPadding
import dev.tonholo.portfolio.core.foundation.layout.calculateLeftPadding
import dev.tonholo.portfolio.core.foundation.layout.calculateRightPadding
import dev.tonholo.portfolio.core.foundation.layout.calculateTopPadding
import dev.tonholo.portfolio.core.ui.unit.dp
import org.jetbrains.compose.web.css.px

fun Modifier.padding(
    vertical: CSSLengthOrPercentageNumericValue = 0.dp,
    horizontal: CSSLengthOrPercentageNumericValue = 0.dp,
) = this then Modifier.padding(
    paddingValues = PaddingValues(
        start = horizontal,
        top = vertical,
        end = horizontal,
        bottom = vertical,
    ),
)

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

fun Modifier.margin(
    vertical: CSSLengthOrPercentageNumericValue = 0.dp,
    horizontal: CSSLengthOrPercentageNumericValue = 0.dp,
) = this then Modifier.margin {
    if (vertical != 0.dp) {
        top(vertical)
        bottom(vertical)
    }

    if (horizontal != 0.dp) {
        left(horizontal)
        right(horizontal)
    }
}
