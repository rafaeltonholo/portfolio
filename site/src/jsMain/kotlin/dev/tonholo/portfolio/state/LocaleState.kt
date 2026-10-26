package dev.tonholo.portfolio.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.tonholo.portfolio.analytics.UmamiAnalytics
import dev.tonholo.portfolio.extensions.readLocalStorage
import dev.tonholo.portfolio.extensions.writeLocalStorage
import dev.tonholo.portfolio.locale.Locale
import kotlinx.browser.document
import org.w3c.dom.HTMLElement

private const val LOCALE_KEY = "rt_lang"

/**
 * Reactive holder for the active [Locale], persisted to `localStorage`
 * under the `rt_lang` key and mirrored onto `<html lang>`.
 *
 * See [ThemeState] for the rationale behind the `by mutableStateOf`
 * property-delegate shape over a backing-field + custom getter.
 */
class LocaleState(initial: Locale) {
    var current: Locale by mutableStateOf(initial)
        private set

    /**
     * Switches to [next], writing the BCP-47 tag to `localStorage` and
     * updating `document.documentElement.lang` to keep the browser's
     * language hint in sync.
     */
    fun set(next: Locale) {
        val previous = current
        current = next
        writeLocalStorage(key = LOCALE_KEY, value = next.tag)
        (document.documentElement as? HTMLElement)?.lang = next.tag
        if (previous != next) {
            UmamiAnalytics.trackLanguageChange(previousLanguage = previous.tag, newLanguage = next.tag)
        }
    }
}

/**
 * Ambient [LocaleState]. Provided at the root by
 * [dev.tonholo.portfolio.PortfolioApp].
 */
val LocalLocaleState = compositionLocalOf<LocaleState> {
    error("LocaleState not provided")
}

/**
 * Creates a remembered [LocaleState] seeded from `localStorage["rt_lang"]`.
 * Defaults to [Locale.EnCa] when nothing is stored.
 */
@Composable
fun rememberLocaleState(): LocaleState {
    val initial = Locale.fromTagOrDefault(tag = readLocalStorage(key = LOCALE_KEY))
    return remember { LocaleState(initial = initial) }
}
