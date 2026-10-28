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
        val targetTheme: String = if (colorMode.isDark) GiscusConfig.THEME_DARK else GiscusConfig.THEME_LIGHT
        syncGiscusWidget(targetTheme = targetTheme, term = term)
        onDispose { }
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
    if (!GiscusConfig.shouldInject(search = window.location.search)) return
    val existingIframe: HTMLIFrameElement? = document
        .querySelector("iframe.giscus-frame")
        .unsafeCast<HTMLIFrameElement?>()
    if (existingIframe != null) {
        if (GiscusConfig.isContaminatedIframe(src = existingIframe.src)) {
            existingIframe.remove()
        } else if (existingIframe.contentWindow != null) {
            postGiscusThemeMessage(iframe = existingIframe, theme = targetTheme, term = term)
            return
        }
    }
    val container: HTMLElement? = document.querySelector(".giscus").unsafeCast<HTMLElement?>()
    if (container != null) {
        injectGiscusScript(container = container, term = term, targetTheme = targetTheme)
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
        src = GiscusConfig.SCRIPT_SRC
        setAttribute("data-repo", GiscusConfig.REPO)
        setAttribute("data-repo-id", GiscusConfig.REPO_ID)
        setAttribute("data-category", GiscusConfig.CATEGORY)
        setAttribute("data-category-id", GiscusConfig.CATEGORY_ID)
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
    iframe.contentWindow?.postMessage(message, GiscusConfig.ORIGIN)
}

/**
 * Ensures an `og:title` meta tag exists in `<head>` populated with [term], as expected by Giscus.
 *
 * @param term the title string to set in the OpenGraph meta tag.
 */
private fun ensureOgTitleMeta(term: String) {
    if (term.isBlank()) return
    val head: HTMLMetaElement? = document.head.unsafeCast<HTMLMetaElement?>()
    if (head == null) return
    val existing: HTMLMetaElement? = document.querySelector("meta[property='og:title']").unsafeCast<HTMLMetaElement?>()
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
