@file:Suppress("UNCHECKED_CAST")

package dev.tonholo.portfolio.core.foundation.layout

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import dev.tonholo.portfolio.core.ui.unit.LayoutDirection
import org.jetbrains.compose.web.css.px

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
fun PaddingValues.calculateLeftPadding(): CSSLengthOrPercentageNumericValue =
    calculateLeftPadding(LayoutDirection.DEFAULT)

/**
 * The padding to be applied along the top edge inside a box.
 */
fun PaddingValues.calculateTopPadding(): CSSLengthOrPercentageNumericValue =
    calculateTopPadding<CSSLengthOrPercentageNumericValue>()

/**
 * The padding to be applied along the right edge inside a box.
 */
fun PaddingValues.calculateRightPadding(): CSSLengthOrPercentageNumericValue =
    calculateRightPadding(LayoutDirection.DEFAULT)

/**
 * The padding to be applied along the bottom edge inside a box.
 */
fun PaddingValues.calculateBottomPadding(): CSSLengthOrPercentageNumericValue =
    calculateBottomPadding<CSSLengthOrPercentageNumericValue>()

@Immutable
private data class PaddingValuesImpl(
    @Stable
    val start: CSSLengthOrPercentageNumericValue = 0.px,
    @Stable
    val top: CSSLengthOrPercentageNumericValue = 0.px,
    @Stable
    val end: CSSLengthOrPercentageNumericValue = 0.px,
    @Stable
    val bottom: CSSLengthOrPercentageNumericValue = 0.px,
) : PaddingValues

@Stable
fun PaddingValues(
    start: CSSLengthOrPercentageNumericValue = 0.px,
    top: CSSLengthOrPercentageNumericValue = 0.px,
    end: CSSLengthOrPercentageNumericValue = 0.px,
    bottom: CSSLengthOrPercentageNumericValue = 0.px
): PaddingValues = PaddingValuesImpl(start, top, end, bottom)
