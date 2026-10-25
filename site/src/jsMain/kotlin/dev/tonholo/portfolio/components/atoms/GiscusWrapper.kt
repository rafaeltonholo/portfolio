package dev.tonholo.portfolio.components.atoms

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.Div

/**
 * Placeholder container where the Giscus comment widget injects its iframe.
 */
@Composable
fun GiscusWrapper() {
    Div(
        attrs = {
            classes("giscus", "giscus-frame")
        },
    )
}
