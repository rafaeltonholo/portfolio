package dev.tonholo.portfolio.components.atoms.mermaid

import kotlinx.browser.document
import kotlinx.coroutines.await
import org.w3c.dom.HTMLScriptElement
import org.w3c.dom.events.Event
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.js.Promise

/**
 * External bridge to the Mermaid module attached to the global scope by the ESM loader.
 */
private external interface MermaidApi {
    fun initialize(config: MermaidConfig)
    fun render(id: String, text: String): Promise<MermaidRenderResult>
}

private external interface MermaidRenderResult {
    val svg: String
}

private external interface MermaidConfig {
    var startOnLoad: Boolean
    var securityLevel: String
    var theme: String
    var fontFamily: String
}

/**
 * Mermaid built-in themes the site switches between.
 *
 * @property id the theme name Mermaid expects.
 */
enum class MermaidTheme(val id: String) {
    Light(id = "neutral"),
    Dark(id = "dark"),
}

/**
 * Singleton client that turns Mermaid diagram definitions into SVG markup.
 */
class Mermaid private constructor(private val api: MermaidApi) {
    private var renderCount = 0

    /**
     * Renders [code] to an SVG string with [theme] applied.
     *
     * @throws Throwable when Mermaid can't parse [code].
     */
    suspend fun render(code: String, theme: MermaidTheme, fontFamily: String): String {
        api.initialize(config = createConfig(theme = theme, fontFamily = fontFamily))
        renderCount++
        return api.render(id = "$RENDER_ID_PREFIX$renderCount", text = code).await().svg
    }

    companion object {
        private const val MERMAID_SCRIPT_ID: String = "portfolio-mermaid-loader"
        private const val MERMAID_READY_EVENT: String = "portfolio-mermaid-ready"
        private const val MERMAID_VERSION: String = "12.3.0"
        private const val RENDER_ID_PREFIX: String = "portfolio-mermaid-"

        private var instance: Mermaid? = null

        /**
         * Resolves the singleton [Mermaid] instance, injecting the ESM loader script on first use.
         */
        suspend fun load(): Mermaid = suspendCoroutine { continuation ->
            val existing: Mermaid? = instance
            if (existing != null) {
                continuation.resume(existing)
                return@suspendCoroutine
            }
            val onReady: (Event) -> Unit = {
                val api: MermaidApi? = js("globalThis.__portfolioMermaid").unsafeCast<MermaidApi?>()
                if (api != null && instance == null) {
                    instance = Mermaid(api = api)
                }
                instance?.let { continuation.resume(it) }
            }
            document.addEventListener(MERMAID_READY_EVENT, callback = onReady, false)
            if (document.querySelector("script[id='$MERMAID_SCRIPT_ID']") == null) {
                document.body?.appendChild(createLoaderScript())
            }
        }

        private fun createLoaderScript(): HTMLScriptElement =
            (document.createElement("script") as HTMLScriptElement).apply {
                id = MERMAID_SCRIPT_ID
                type = "module"
                textContent = """
                    |import mermaid from 'https://cdn.jsdelivr.net/npm/mermaid@$MERMAID_VERSION/dist/mermaid.esm.min.mjs';
                    |globalThis.__portfolioMermaid = mermaid;
                    |document.dispatchEvent(new Event('$MERMAID_READY_EVENT'));
                """.trimMargin()
            }

        private fun createConfig(theme: MermaidTheme, fontFamily: String): MermaidConfig =
            js("({})").unsafeCast<MermaidConfig>().apply {
                startOnLoad = false
                securityLevel = "strict"
                this.theme = theme.id
                this.fontFamily = fontFamily
            }
    }
}
