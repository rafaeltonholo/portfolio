package dev.tonholo.portfolio.extensions

import kotlinx.browser.window

/**
 * Safe `localStorage` accessors.
 *
 * JS privacy-mode sandboxes throw on touch, so we swallow and fall back to
 * no-op, matching how the handoff prototype wraps every read/write in
 * try/catch.
 */
fun readLocalStorage(key: String): String? = runCatching {
    window.localStorage.getItem(key)
}.getOrNull()

fun writeLocalStorage(key: String, value: String) {
    runCatching { window.localStorage.setItem(key, value) }
}
