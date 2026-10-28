package dev.tonholo.portfolio.core.foundation.layout

import androidx.compose.runtime.Stable
import dev.tonholo.portfolio.core.ui.unit.LayoutDirection

@Stable
interface PaddingValues

/**
 * The padding to be applied along the left edge inside a box.
 */
@Stable
expect fun <T> PaddingValues.calculateLeftPadding(layoutDirection: LayoutDirection): T

/**
 * The padding to be applied along the top edge inside a box.
 */
@Stable
expect fun <T> PaddingValues.calculateTopPadding(): T

/**
 * The padding to be applied along the right edge inside a box.
 */
@Stable
expect fun <T> PaddingValues.calculateRightPadding(layoutDirection: LayoutDirection): T

/**
 * The padding to be applied along the bottom edge inside a box.
 */
@Stable
expect fun <T> PaddingValues.calculateBottomPadding(): T

