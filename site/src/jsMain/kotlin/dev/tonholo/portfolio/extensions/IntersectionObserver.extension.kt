package dev.tonholo.portfolio.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import kotlinx.browser.document
import org.w3c.dom.Element

private const val REVEAL_ATTR = "data-reveal"
private const val REVEAL_CLASS = "in"
private const val REVEAL_THRESHOLD = 0.1

/**
 * Typed wrapper around the browser-global `IntersectionObserver` class.
 *
 * Declared as `external class IntersectionObserver` so the Kotlin/JS
 * compiler maps direct instantiation (`IntersectionObserver(...)`) to
 * `new IntersectionObserver(...)` in the generated JS without needing a
 * `@JsModule` or inline `js("...")` invocation.
 */
external class IntersectionObserver(
    callback: (entries: Array<IntersectionObserverEntry>, observer: IntersectionObserver) -> Unit,
    options: IntersectionObserverOptions = definedExternally,
) {
    fun observe(target: Element)
    fun unobserve(target: Element)
    fun disconnect()
}

external interface IntersectionObserverEntry {
    val isIntersecting: Boolean
    val target: Element
}

external interface IntersectionObserverOptions {
    var threshold: Double
}

/**
 * Installs a single [IntersectionObserver] that adds the `.in` class to
 * every element carrying `data-reveal` once it crosses the 10% visibility
 * threshold, matching the handoff prototype's reveal pattern. Each element
 * is unobserved after firing so the class stays sticky.
 */
@Composable
fun InstallRevealObserver() {
    DisposableEffect(Unit) {
        val options = createRevealOptions(threshold = REVEAL_THRESHOLD)
        val observer = IntersectionObserver(
            callback = { entries, self ->
                entries.forEach { entry ->
                    if (entry.isIntersecting) {
                        entry.target.classList.add(REVEAL_CLASS)
                        self.unobserve(entry.target)
                    }
                }
            },
            options = options,
        )

        val nodes = document.querySelectorAll("[$REVEAL_ATTR]")
        for (index in 0 until nodes.length) {
            val node = nodes.item(index)
            if (node is Element) {
                observer.observe(node)
            }
        }

        onDispose { observer.disconnect() }
    }
}

private fun createRevealOptions(threshold: Double): IntersectionObserverOptions {
    val options = js("({})").unsafeCast<IntersectionObserverOptions>()
    options.threshold = threshold
    return options
}
