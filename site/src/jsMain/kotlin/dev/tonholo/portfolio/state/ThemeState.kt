package dev.tonholo.portfolio.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.analytics.UmamiAnalytics
import dev.tonholo.portfolio.extensions.readLocalStorage
import dev.tonholo.portfolio.extensions.writeLocalStorage
import kotlinx.browser.window

private const val THEME_KEY = "rt_theme"
private const val DARK_VALUE = "dark"
private const val LIGHT_VALUE = "light"

/**
 * Resolves the initial [ColorMode] from `localStorage` under [THEME_KEY], falling
 * back to the browser's `prefers-color-scheme: dark` media query if unset.
 */
fun resolveInitialColorMode(): ColorMode {
    val storedValue: String? = readLocalStorage(key = THEME_KEY)
    if (storedValue != null) {
        return when (storedValue) {
            DARK_VALUE -> ColorMode.DARK
            LIGHT_VALUE -> ColorMode.LIGHT
            else -> ColorMode.LIGHT
        }
    }
    val prefersDark: Boolean = runCatching {
        window.matchMedia("(prefers-color-scheme: dark)").matches
    }.getOrDefault(false)
    return if (prefersDark) ColorMode.DARK else ColorMode.LIGHT
}

/**
 * Reactive holder for the active [ColorMode], persisted to `localStorage`
 * under the `rt_theme` key. Mirrors the handoff prototype's theme toggle.
 *
 * `current` is declared with the `by mutableStateOf` property delegate so
 * the Compose compiler plugin emits a tracked state read at every call
 * site. The previous backing-field + custom getter shape was eligible for
 * Kotlin/JS to optimize the read path in a way that bypassed Compose's
 * invalidation tracking, which left consumers (Nav icon, Silk bridge)
 * staring at a stale [ColorMode] after `.toggle()`.
 */
class ThemeState(initial: ColorMode) {
    var current: ColorMode by mutableStateOf(initial)
        private set

    /**
     * Flips between light and dark, writing the new value to `localStorage`.
     */
    fun toggle() {
        val next = if (current.isDark) ColorMode.LIGHT else ColorMode.DARK
        current = next
        writeLocalStorage(key = THEME_KEY, value = if (next.isDark) DARK_VALUE else LIGHT_VALUE)
        UmamiAnalytics.trackThemeChange(theme = next.name)
    }
}

/**
 * Ambient [ThemeState]. Provided at the root by [dev.tonholo.portfolio.PortfolioApp].
 */
val LocalThemeState = compositionLocalOf<ThemeState> {
    error("ThemeState not provided")
}

/**
 * Creates a remembered [ThemeState] seeded from [resolveInitialColorMode].
 */
@Composable
fun rememberThemeState(): ThemeState {
    val initial: ColorMode = resolveInitialColorMode()
    return remember { ThemeState(initial = initial) }
}
