package dev.tonholo.portfolio.components.atoms.shiki

import kotlinx.browser.document
import kotlinx.coroutines.await
import org.w3c.dom.HTMLScriptElement
import org.w3c.dom.events.Event
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * External bridge to Shiki bundle attached to global scope by the ESM loader.
 */
private external interface ShikiBundle {
    val api: ShikiApi
    val transformers: Array<ShikiTransformer>
}

/**
 * Singleton client managing runtime initialization and code-to-HTML conversion via Shiki.
 *
 * @param api the initialized Shiki API implementation.
 * @param defaultTransformers standard AST transformers (focus, highlight, diff notation).
 */
class Shiki private constructor(
    private val api: ShikiApi,
    val defaultTransformers: List<ShikiTransformer>,
) {
    /**
     * Converts raw source code into syntax-highlighted HTML markup asynchronously.
     *
     * @param code raw code string to highlight.
     * @param options theme, language, and transformer options.
     * @return HTML string with highlighted spans and CSS classes.
     */
    suspend fun codeToHtml(code: String, options: CodeToHastOptions): String =
        api.codeToHtmlPromise(code = code, options = options).await()

    companion object {
        private const val SHIKI_SCRIPT_ID: String = "portfolio-shiki-loader"
        private const val SHIKI_READY_EVENT: String = "portfolio-shiki-ready"
        private const val SHIKI_VERSION: String = "1.20.0"

        private var instance: Shiki? = null

        /**
         * Resolves the singleton [Shiki] instance, dynamically injecting the ESM loader
         * script into the page DOM if not already present.
         *
         * @return the initialized [Shiki] instance.
         */
        suspend fun load(): Shiki = suspendCoroutine { continuation ->
            val existing: Shiki? = instance
            if (existing != null) {
                continuation.resume(existing)
                return@suspendCoroutine
            }
            val onReady: (Event) -> Unit = {
                val bundle: ShikiBundle? = js("globalThis.__portfolioShiki").unsafeCast<ShikiBundle?>()
                if (bundle != null && instance == null) {
                    instance = Shiki(
                        api = bundle.api,
                        defaultTransformers = bundle.transformers.toList(),
                    )
                }
                instance?.let { continuation.resume(it) }
            }
            document.addEventListener(SHIKI_READY_EVENT, callback = onReady, false)
            val isLoaderMissing: Boolean = document.querySelector("script[id='$SHIKI_SCRIPT_ID']") == null
            if (isLoaderMissing) {
                val script: HTMLScriptElement = (document.createElement("script") as HTMLScriptElement).apply {
                    id = SHIKI_SCRIPT_ID
                    type = "module"
                    textContent = """
                        |import * as shiki from 'https://esm.run/shiki@$SHIKI_VERSION';
                        |import {
                        |  transformerNotationFocus,
                        |  transformerNotationHighlight,
                        |  transformerNotationDiff,
                        |} from 'https://esm.run/@shikijs/transformers@$SHIKI_VERSION';
                        |
                        |globalThis.__portfolioShiki = {
                        |  api: shiki,
                        |  transformers: [
                        |    transformerNotationFocus(),
                        |    transformerNotationHighlight(),
                        |    transformerNotationDiff(),
                        |  ],
                        |};
                        |document.dispatchEvent(new Event('$SHIKI_READY_EVENT'));
                    """.trimMargin()
                }
                document.body?.appendChild(script)
            }
        }
    }
}
