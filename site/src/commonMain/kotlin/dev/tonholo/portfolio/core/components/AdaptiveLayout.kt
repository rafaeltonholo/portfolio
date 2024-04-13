package dev.tonholo.portfolio.core.components

import androidx.compose.runtime.Composable
import dev.tonholo.portfolio.core.foundation.Modifier

@Composable
expect fun AdaptiveLayout(
    modifier: Modifier = Modifier,
    listPanel: @Composable () -> Unit,
    detailPanel: @Composable () -> Unit,
)
