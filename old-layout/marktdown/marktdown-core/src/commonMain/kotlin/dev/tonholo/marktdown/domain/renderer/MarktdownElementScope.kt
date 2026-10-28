package dev.tonholo.marktdown.domain.renderer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.tonholo.marktdown.domain.content.MarktdownElement

sealed interface MarktdownElementScope<out T : MarktdownElement> {
    val element: T
    val drawContent: @Composable () -> Unit

    companion object {
        @Composable
        operator fun <T : MarktdownElement> invoke(
            element: T,
            drawContent: @Composable () -> Unit,
        ): MarktdownElementScope<T> = MarktdownElementScopeImpl(
            element = element,
            drawContent = remember { drawContent },
        )
    }
}

private class MarktdownElementScopeImpl<out T : MarktdownElement>(
    override val element: T,
    override val drawContent: @Composable () -> Unit,
) : MarktdownElementScope<T>
