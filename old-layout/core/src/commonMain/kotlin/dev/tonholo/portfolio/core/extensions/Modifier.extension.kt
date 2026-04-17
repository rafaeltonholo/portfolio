package dev.tonholo.portfolio.core.extensions

import com.varabyte.kobweb.compose.ui.Modifier
import dev.tonholo.portfolio.core.foundation.layout.PaddingValues

expect fun Modifier.padding(
    paddingValues: PaddingValues,
): Modifier
