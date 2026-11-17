package dev.tonholo.portfolio.components.atoms.shiki

import kotlin.js.Json
import kotlin.js.Promise

/**
 * Common AST transformer interface recognized by Shiki's pipeline.
 */
external interface ShikiTransformer {
    var name: String?
}

/**
 * Options passed to Shiki's code-to-HTML parser.
 */
external interface CodeToHastOptions : ThemeOptions {
    var lang: String
    var transformers: Array<ShikiTransformer>

    /** Token colour overrides, keyed by theme name and then by the lowercase hex colour to replace. */
    var colorReplacements: Json
}

/**
 * Low-level JavaScript interface exposed by Shiki's module exports.
 */
external interface ShikiApi {
    @JsName("codeToHtml")
    fun codeToHtmlPromise(code: String, options: CodeToHastOptions): Promise<String>
}

/**
 * Creates a typed [CodeToHastOptions] JavaScript object.
 *
 * @param builder configuration lambda populating options.
 * @return the configured [CodeToHastOptions] instance.
 */
fun createCodeToHastOptions(builder: CodeToHastOptions.() -> Unit): CodeToHastOptions {
    val options: CodeToHastOptions = js("({})").unsafeCast<CodeToHastOptions>()
    options.builder()
    return options
}
