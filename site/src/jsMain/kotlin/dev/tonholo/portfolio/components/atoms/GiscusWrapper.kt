package dev.tonholo.portfolio.components.atoms

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.state.LocalThemeState
import dev.tonholo.portfolio.state.ThemeState
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLIFrameElement
import org.w3c.dom.HTMLMetaElement
import org.w3c.dom.HTMLScriptElement
import org.w3c.dom.MessageEvent
import org.w3c.dom.events.Event

private const val GISCUS_ORIGIN: String = "https://giscus.app"
private const val GISCUS_SCRIPT_SRC: String = "https://giscus.app/client.js"
private const val GISCUS_REPO: String = "rafaeltonholo/portfolio"
private const val GISCUS_REPO_ID: String = "R_kgDOL0CWLw"
private const val GISCUS_CATEGORY: String = "Q&A"
private const val GISCUS_CATEGORY_ID: String = "DIC_kwDOL0CWL84DJluC"
private const val GISCUS_THEME_DARK: String = "transparent_dark"
private const val GISCUS_THEME_LIGHT: String = "light"

/**
 * Embeds the Giscus GitHub Discussions comment widget for the specified discussion [term].
 *
 * @param term discussion title identifier (typically the article title).
 * @param modifier optional [Modifier] chained onto the wrapper.
 */
@Composable
fun GiscusWrapper(term: String, modifier: Modifier = Modifier) {
    val themeState: ThemeState = LocalThemeState.current
    val colorMode: ColorMode = themeState.current
    DisposableEffect(term, colorMode) {
        ensureOgTitleMeta(term = term)
        val targetTheme: String = if (colorMode.isDark) GISCUS_THEME_DARK else GISCUS_THEME_LIGHT
        syncGiscusWidget(targetTheme = targetTheme, term = term)
        val messageListener: (Event) -> Unit = createGiscusMessageListener(
            targetTheme = targetTheme,
            term = term,
        )
        window.addEventListener("message", messageListener)
        onDispose {
            window.removeEventListener("message", messageListener)
        }
    }
    Div(
        attrs = Modifier
            .fillMaxWidth()
            .then(modifier)
            .toAttrs {
                classes("giscus")
            },
    )
}

/**
 * Synchronizes the Giscus widget with the given [targetTheme] and [term], injecting the script if needed.
 *
 * @param targetTheme active Giscus theme name.
 * @param term discussion title identifier.
 */
private fun syncGiscusWidget(targetTheme: String, term: String) {
    val existingIframe: HTMLIFrameElement? = document
        .querySelector("iframe.giscus-frame")
        .unsafeCast<HTMLIFrameElement?>()
    if (existingIframe?.contentWindow != null) {
        postGiscusThemeMessage(iframe = existingIframe, theme = targetTheme, term = term)
    } else {
        val container: HTMLElement? = document.querySelector(".giscus").unsafeCast<HTMLElement?>()
        if (container != null) {
            injectGiscusScript(container = container, term = term, targetTheme = targetTheme)
        }
    }
}

/**
 * Creates an event listener that forwards the latest [targetTheme] and [term] to Giscus when ready.
 *
 * @param targetTheme active Giscus theme name.
 * @param term discussion title identifier.
 */
private fun createGiscusMessageListener(targetTheme: String, term: String): (Event) -> Unit =
    { event ->
        val messageEvent: MessageEvent = event.unsafeCast<MessageEvent>()
        if (messageEvent.origin == GISCUS_ORIGIN) {
            val iframe: HTMLIFrameElement? = document
                .querySelector("iframe.giscus-frame")
                .unsafeCast<HTMLIFrameElement?>()
            if (iframe?.contentWindow != null) {
                postGiscusThemeMessage(iframe = iframe, theme = targetTheme, term = term)
            }
        }
    }

/**
 * Injects the Giscus bootstrap `<script>` into [container] with [term] and [targetTheme].
 *
 * @param container target container element where the script tag should be injected.
 * @param term discussion title identifier.
 * @param targetTheme active Giscus theme name.
 */
private fun injectGiscusScript(container: HTMLElement, term: String, targetTheme: String) {
    if (container.querySelector("script") != null) return
    val script: HTMLScriptElement = (document.createElement("script") as HTMLScriptElement).apply {
        src = GISCUS_SCRIPT_SRC
        setAttribute("data-repo", GISCUS_REPO)
        setAttribute("data-repo-id", GISCUS_REPO_ID)
        setAttribute("data-category", GISCUS_CATEGORY)
        setAttribute("data-category-id", GISCUS_CATEGORY_ID)
        setAttribute("data-mapping", "og:title")
        setAttribute("data-term", term)
        setAttribute("data-strict", "1")
        setAttribute("data-reactions-enabled", "1")
        setAttribute("data-emit-metadata", "0")
        setAttribute("data-input-position", "bottom")
        setAttribute("data-theme", targetTheme)
        setAttribute("data-lang", "en")
        setAttribute("data-loading", "lazy")
        setAttribute("crossorigin", "anonymous")
        async = true
    }
    container.appendChild(script)
}

/**
 * Posts a configuration message to the Giscus [iframe] to update the active [theme] and [term].
 *
 * @param iframe target [HTMLIFrameElement] hosting the Giscus discussion.
 * @param theme the target Giscus theme identifier.
 * @param term discussion title identifier.
 */
@Suppress("UnusedParameter")
private fun postGiscusThemeMessage(iframe: HTMLIFrameElement, theme: String, term: String) {
    val message: dynamic = js("({ giscus: { setConfig: { theme: theme, term: term } } })")
    iframe.contentWindow?.postMessage(message, GISCUS_ORIGIN)
}

/**
 * Ensures an `og:title` meta tag exists in `<head>` populated with [term], as expected by Giscus.
 *
 * @param term the title string to set in the OpenGraph meta tag.
 */
private fun ensureOgTitleMeta(term: String) {
    if (term.isBlank()) return
    val head = document.head ?: return
    val existing = document.querySelector("meta[property='og:title']").unsafeCast<HTMLMetaElement?>()
    if (existing != null) {
        existing.content = term
    } else {
        val meta: HTMLMetaElement = (document.createElement("meta") as HTMLMetaElement).apply {
            setAttribute("property", "og:title")
            content = term
        }
        head.appendChild(meta)
    }
}
