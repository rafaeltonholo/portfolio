@file:Suppress("UNCHECKED_CAST")

package dev.tonholo.portfolio.core.foundation.layout

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.varabyte.kobweb.compose.css.StyleVariable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.setVariable
import dev.tonholo.portfolio.core.ui.unit.Dp
import dev.tonholo.portfolio.core.ui.unit.LayoutDirection
import dev.tonholo.portfolio.core.ui.unit.dp

/**
 * The padding to be applied along the left edge inside a box.
 */
@Stable
actual fun <T> PaddingValues.calculateLeftPadding(layoutDirection: LayoutDirection): T {
    require(this is PaddingValuesImpl)
    return start as T
}

/**
 * The padding to be applied along the top edge inside a box.
 */
@Stable
actual fun <T> PaddingValues.calculateTopPadding(): T {
    require(this is PaddingValuesImpl)
    return top as T
}

/**
 * The padding to be applied along the right edge inside a box.
 */
@Stable
actual fun <T> PaddingValues.calculateRightPadding(layoutDirection: LayoutDirection): T {
    require(this is PaddingValuesImpl)
    return end as T
}

/**
 * The padding to be applied along the bottom edge inside a box.
 */
@Stable
actual fun <T> PaddingValues.calculateBottomPadding(): T {
    require(this is PaddingValuesImpl)
    return bottom as T
}

/**
 * The padding to be applied along the left edge inside a box.
 */
fun PaddingValues.calculateLeftPadding(): Dp =
    calculateLeftPadding(LayoutDirection.DEFAULT)

/**
 * The padding to be applied along the top edge inside a box.
 */
fun PaddingValues.calculateTopPadding(): Dp =
    calculateTopPadding<Dp>()

/**
 * The padding to be applied along the right edge inside a box.
 */
fun PaddingValues.calculateRightPadding(): Dp =
    calculateRightPadding(LayoutDirection.DEFAULT)

/**
 * The padding to be applied along the bottom edge inside a box.
 */
fun PaddingValues.calculateBottomPadding(): Dp =
    calculateBottomPadding<Dp>()

@Immutable
private data class PaddingValuesImpl(
    @Stable
    val start: Dp = 0.dp,
    @Stable
    val top: Dp = 0.dp,
    @Stable
    val end: Dp = 0.dp,
    @Stable
    val bottom: Dp = 0.dp,
) : PaddingValues

@Stable
fun PaddingValues(
    start: Dp = 0.dp,
    top: Dp = 0.dp,
    end: Dp = 0.dp,
    bottom: Dp = 0.dp
): PaddingValues = PaddingValuesImpl(start, top, end, bottom)

@Stable
fun PaddingValues(
    vertical: Dp = 0.dp,
    horizontal: Dp = 0.dp,
): PaddingValues = PaddingValuesImpl(horizontal, vertical, horizontal, vertical)
