package dev.tonholo.portfolio.components.atoms.shiki

/**
 * Options configuring active dual-themes (light and dark) for Shiki.
 */
external interface ThemeOptions {
    var themes: ThemeInput
    var defaultColor: String
}

/**
 * Maps light and dark color schemes to their respective Shiki theme identifiers.
 */
external interface ThemeInput {
    var light: String
    var dark: String
}

/**
 * Active color mode discriminator used as the default color in Shiki rendering.
 */
enum class DefaultTheme {
    Light,
    Dark,
}

/**
 * Builder creating a typed [ThemeOptions] descriptor.
 */
class ThemeBuilder {
    var light: ShikiTheme? = null
    var dark: ShikiTheme? = null
    var default: DefaultTheme = DefaultTheme.Dark
        private set

    /**
     * Sets the active fallback color mode theme.
     *
     * @param theme the target default theme variant.
     */
    fun activeTheme(theme: DefaultTheme) {
        default = theme
    }

    /**
     * Assembles the configured [ThemeOptions] instance.
     *
     * @return the compiled [ThemeOptions] JS object.
     */
    fun build(): ThemeOptions {
        val lightTheme: ShikiTheme = checkNotNull(light ?: dark) { "At least one theme must be configured." }
        val darkTheme: ShikiTheme = checkNotNull(dark ?: light) { "At least one theme must be configured." }
        val themesInput: ThemeInput = js("({})").unsafeCast<ThemeInput>().apply {
            light = lightTheme.themeName
            dark = darkTheme.themeName
        }
        return js("({})").unsafeCast<ThemeOptions>().apply {
            themes = themesInput
            defaultColor = default.name.lowercase()
        }
    }
}

/**
 * DSL constructor building a [ThemeOptions] configuration.
 *
 * @param builder configuration lambda configuring light and dark themes.
 * @return the resolved [ThemeOptions].
 */
fun shikiTheme(builder: ThemeBuilder.() -> Unit): ThemeOptions =
    ThemeBuilder().apply(builder).build()
