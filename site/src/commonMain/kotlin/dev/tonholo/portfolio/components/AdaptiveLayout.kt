package dev.tonholo.portfolio.components

import androidx.compose.runtime.Composable
import dev.tonholo.portfolio.foundation.Modifier

@Composable
expect fun AdaptiveLayout(
    modifier: Modifier = Modifier,
    listPanel: @Composable () -> Unit,
    detailPanel: @Composable () -> Unit,
)
