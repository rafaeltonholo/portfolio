package dev.tonholo.portfolio.components.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.rememberPageContext
import dev.tonholo.portfolio.components.organisms.chrome.Footer
import dev.tonholo.portfolio.components.organisms.chrome.LanguageToast
import dev.tonholo.portfolio.components.organisms.chrome.Nav
import dev.tonholo.portfolio.locale.Locale
import dev.tonholo.portfolio.state.LocalLocaleState
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.dom.TagElement
import org.w3c.dom.HTMLElement

// `LanguageToast` auto-dismiss window after the user switches to pt-BR.
// Matches the 3.6s timer from the handoff spec.
private const val LANGUAGE_TOAST_DISMISS_DELAY_MS = 3600L

/**
 * Base layout shared by every page. Renders the site chrome (top [Nav] and
 * bottom [Footer]), wraps the caller's page body in a semantic `<main>`
 * element, and emits the transient [LanguageToast] when the active locale
 * flips to [Locale.PtBr].
 *
 * Routing state (current path, navigation callback) is read from the active
 * [rememberPageContext]. The callback delegates to `Router.tryRoutingTo`, so
 * any [Routes][dev.tonholo.portfolio.navigation.Routes] constant passed by
 * the chrome will resolve against registered Kobweb pages.
 *
 * The toast visibility is local state: when [LocalLocaleState] transitions to
 * [Locale.PtBr] a [LaunchedEffect] flips `visible` to `true`, then hides it
 * again after [LANGUAGE_TOAST_DISMISS_DELAY_MS]. The effect keys on the
 * current [Locale] so re-selecting pt-BR restarts the timer.
 *
 * @param modifier optional [Modifier] applied to the `<main>` element.
 * @param content the page body rendered inside `<main>`.
 */
@Composable
fun PageLayout(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val pageContext = rememberPageContext()
    val currentRoute = pageContext.route.path
    val localeState = LocalLocaleState.current

    var toastVisible by remember { mutableStateOf(value = false) }
    LaunchedEffect(localeState.current) {
        if (localeState.current == Locale.PtBr) {
            toastVisible = true
            delay(timeMillis = LANGUAGE_TOAST_DISMISS_DELAY_MS)
            toastVisible = false
        }
    }

    Nav(
        currentRoute = currentRoute,
        onNavigate = { route -> pageContext.router.tryRoutingTo(pathQueryAndFragment = route) },
    )
    TagElement<HTMLElement>(tagName = "main", applyAttrs = modifier.toAttrs()) {
        content()
    }
    Footer(
        onNavigate = { route -> pageContext.router.tryRoutingTo(pathQueryAndFragment = route) },
    )
    LanguageToast(visible = toastVisible)
}
