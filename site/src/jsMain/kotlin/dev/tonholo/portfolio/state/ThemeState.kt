package dev.tonholo.portfolio.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.extensions.readLocalStorage
import dev.tonholo.portfolio.extensions.writeLocalStorage

private const val THEME_KEY = "rt_theme"
private const val DARK_VALUE = "dark"
private const val LIGHT_VALUE = "light"

/**
 * Reactive holder for the active [ColorMode], persisted to `localStorage`
 * under the `rt_theme` key. Mirrors the handoff prototype's theme toggle.
 */
class ThemeState(initial: ColorMode) {
    private val state: MutableState<ColorMode> = mutableStateOf(initial)

    val current: ColorMode get() = state.value

    /**
     * Flips between light and dark, writing the new value to `localStorage`.
     */
    fun toggle() {
        val next = if (state.value.isDark) ColorMode.LIGHT else ColorMode.DARK
        state.value = next
        writeLocalStorage(key = THEME_KEY, value = if (next.isDark) DARK_VALUE else LIGHT_VALUE)
    }
}

/**
 * Ambient [ThemeState]. Provided at the root by [dev.tonholo.portfolio.PortfolioApp].
 */
val LocalThemeState = compositionLocalOf<ThemeState> {
    error("ThemeState not provided")
}

/**
 * Creates a remembered [ThemeState] seeded from `localStorage["rt_theme"]`.
 * Defaults to [ColorMode.LIGHT] when nothing is stored.
 */
@Composable
fun rememberThemeState(): ThemeState {
    val initial = when (readLocalStorage(key = THEME_KEY)) {
        DARK_VALUE -> ColorMode.DARK
        LIGHT_VALUE -> ColorMode.LIGHT
        else -> ColorMode.LIGHT
    }
    return remember { ThemeState(initial = initial) }
}
