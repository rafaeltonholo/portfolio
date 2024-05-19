package dev.tonholo.portfolio.core.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import com.varabyte.kobweb.silk.components.style.breakpoint.BreakpointValues
import com.varabyte.kobweb.silk.components.style.breakpoint.ResponsiveValues
import dev.tonholo.portfolio.core.ui.theme.Theme
import kotlinx.browser.window
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.w3c.dom.events.EventListener

private const val RESIZE_DEBOUNCE_DELAY = 250

@Composable
fun <T> responsiveStateOf(values: ResponsiveValues<T>, throttleDelay: Int = RESIZE_DEBOUNCE_DELAY): State<T> {
    val breakpoints = Theme.breakpoints
    val initialValue = remember(values) { values.current(window.innerWidth, breakpoints) }
    return produceState(initialValue) {
        var throttled = false
        val listener = EventListener {
            if (!throttled) {
                value = values.current(window.innerWidth, breakpoints)
                throttled = true
                window.setTimeout({ throttled = false }, throttleDelay)
            }
        }
        window.addEventListener("resize", listener, true)
        awaitDispose {
            window.removeEventListener("resize", listener, true)
        }
    }
}

fun <T> ResponsiveValues<T>.current(windowWidth: Int, breakpoints: BreakpointValues<CSSSizeValue<CSSUnit.rem>>): T {
    return when (windowWidth.toFloat()) {
        in breakpoints.xl.toPx().value..Float.MAX_VALUE -> xl
        in breakpoints.lg.toPx().value..breakpoints.xl.toPx().value -> lg
        in breakpoints.md.toPx().value..breakpoints.lg.toPx().value -> md
        else -> base
    }
}

fun <T> ResponsiveValues(base: T, sm: T = base, md: T = sm, lg: T = md, xl: T = lg) =
    ResponsiveValues(base, sm, md, lg, xl)
