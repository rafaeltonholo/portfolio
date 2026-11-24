package dev.tonholo.portfolio.components.atoms

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.Element
import org.w3c.dom.events.Event

private val USER_SCROLL_EVENTS: List<String> = listOf("wheel", "touchstart", "keydown")

/**
 * Keeps the comments in view after the reader signs in with GitHub.
 *
 * Giscus brings the reader back with a full page load, and the article above the comments keeps growing while its
 * images, diagrams and code load, which pushes the comments out of view. Every time the page changes size, this
 * scrolls the comments back into view, until the reader scrolls on their own or
 * [GiscusConfig.SIGN_IN_SCROLL_WINDOW_MS] passes.
 */
@Composable
internal fun KeepCommentsInViewAfterSignIn() {
    val isSignInReturn: Boolean = remember { GiscusConfig.isSignInReturn(search = window.location.search) }
    DisposableEffect(isSignInReturn) {
        if (!isSignInReturn) return@DisposableEffect onDispose { }
        val startedAt: Double = window.performance.now()
        var readerScrolled = false
        val bringCommentsIntoView: () -> Unit = {
            val stillWaiting = !readerScrolled &&
                window.performance.now() - startedAt < GiscusConfig.SIGN_IN_SCROLL_WINDOW_MS
            if (stillWaiting) document.getElementById(GiscusConfig.CONTAINER_ID)?.scrollIntoView()
        }
        val onReaderScroll: (Event) -> Unit = { readerScrolled = true }
        val pageResizeObserver = ResizeObserver { _, _ -> bringCommentsIntoView() }
        document.body?.let { pageResizeObserver.observe(it) }
        USER_SCROLL_EVENTS.forEach { window.addEventListener(it, onReaderScroll) }
        window.requestAnimationFrame { bringCommentsIntoView() }
        onDispose {
            pageResizeObserver.disconnect()
            USER_SCROLL_EVENTS.forEach { window.removeEventListener(it, onReaderScroll) }
        }
    }
}

private external class ResizeObserver(
    callback: (entries: Array<dynamic>, observer: ResizeObserver) -> Unit,
) {
    fun observe(target: Element)
    fun disconnect()
}
