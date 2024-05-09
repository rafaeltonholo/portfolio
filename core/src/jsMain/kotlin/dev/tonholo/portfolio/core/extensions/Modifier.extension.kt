package dev.tonholo.portfolio.core.extensions

import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import dev.tonholo.portfolio.core.foundation.layout.PaddingValues
import dev.tonholo.portfolio.core.foundation.layout.calculateBottomPadding
import dev.tonholo.portfolio.core.foundation.layout.calculateLeftPadding
import dev.tonholo.portfolio.core.foundation.layout.calculateRightPadding
import dev.tonholo.portfolio.core.foundation.layout.calculateTopPadding
import dev.tonholo.portfolio.core.ui.unit.Dp
import dev.tonholo.portfolio.core.ui.unit.dp

fun Modifier.padding(
    all: Dp = 0.dp,
) = this then Modifier.padding(
    paddingValues = PaddingValues(
        start = all,
        top = all,
        end = all,
        bottom = all,
    ),
)

fun Modifier.padding(
    start: Dp = 0.dp,
    top: Dp = 0.dp,
    end: Dp = 0.dp,
    bottom: Dp = 0.dp,
) = this then Modifier.padding(
    paddingValues = PaddingValues(
        start = start,
        top = top,
        end = end,
        bottom = bottom,
    ),
)

fun Modifier.padding(
    vertical: Dp = 0.dp,
    horizontal: Dp = 0.dp,
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
    if (start != 0.dp) {
        left(start)
    }
    val top = paddingValues.calculateTopPadding()
    if (top != 0.dp) {
        top(top)
    }
    val end = paddingValues.calculateRightPadding()
    if (end != 0.dp) {
        right(end)
    }
    val bottom = paddingValues.calculateBottomPadding()
    if (bottom != 0.dp) {
        bottom(bottom)
    }
}

fun Modifier.margin(
    vertical: Dp = 0.dp,
    horizontal: Dp = 0.dp,
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
