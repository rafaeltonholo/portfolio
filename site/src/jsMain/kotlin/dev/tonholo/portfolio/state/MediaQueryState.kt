package dev.tonholo.portfolio.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.browser.window
import org.w3c.dom.events.Event

/**
 * Tracks whether the CSS media [query] matches, and updates when it starts or stops matching, for example when the
 * window is resized.
 *
 * @param query a CSS media query, such as `(min-width: 80rem)`.
 */
@Composable
fun rememberMediaQueryMatch(query: String): Boolean {
    val mediaQueryList = remember(query) { window.matchMedia(query) }
    var matches by remember(query) { mutableStateOf(mediaQueryList.matches) }
    DisposableEffect(mediaQueryList) {
        val onChange: (Event) -> Unit = { matches = mediaQueryList.matches }
        mediaQueryList.addEventListener("change", onChange)
        onDispose { mediaQueryList.removeEventListener("change", onChange) }
    }
    return matches
}
