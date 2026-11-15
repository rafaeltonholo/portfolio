package dev.tonholo.portfolio.extensions

import kotlinx.browser.document
import kotlinx.browser.window

/**
 * Smoothly scrolls the viewport to the element with the given DOM `id`. Runs a
 * single `getElementById` lookup against the document so it is safe to call
 * from a composable's `onClick` without capturing the DOM node at composition
 * time. If no matching element is found the call is a no-op, matching
 * `scrollIntoView`'s own resilience to missing anchors.
 *
 * @param id the target element's `id` attribute, without the leading `#`.
 */
fun scrollToElementId(id: String) {
    val element = document.getElementById(id) ?: return
    element.asDynamic().scrollIntoView(js("({ behavior: 'smooth', block: 'start' })"))
}

/**
 * Jumps to an in-page anchor the way a plain `#id` link would: records the hash in the address bar and scrolls
 * to the element. Kobweb's router intercepts same-page hash links, so in-page links call this from `onClick`
 * after preventing the default navigation.
 *
 * @param id the target element's `id` attribute, without the leading `#`.
 */
fun jumpToAnchor(id: String) {
    window.history.pushState(null, "", "#$id")
    scrollToElementId(id)
}

/**
 * Opens a URL in a new browser tab. Thin wrapper around `window.open` that
 * sets the `noopener,noreferrer` window features so the opened page cannot
 * reach back into the opener via `window.opener`.
 *
 * @param url the absolute or relative URL to open.
 */
fun openInNewTab(url: String) {
    window.open(url = url, target = "_blank", features = "noopener,noreferrer")
}
