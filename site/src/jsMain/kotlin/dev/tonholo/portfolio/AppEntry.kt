package dev.tonholo.portfolio

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.core.App
import com.varabyte.kobweb.silk.components.style.ComponentModifiers
import com.varabyte.kobweb.silk.components.style.breakpoint.BreakpointSizes
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.unit.dp

private val Breakpoints = BreakpointSizes(
    sm = 320.dp,
    md = 768.dp,
    lg = 1024.dp,
    xl = 1300.dp,
)

val ComponentModifiers.breakpoints get() = Breakpoints

@InitSilk
fun initializeBreakpoints(ctx: InitSilkContext) {
    ctx.theme.breakpoints = Breakpoints
}

@App
@Composable
fun AppEntry(content: @Composable () -> Unit) {
    Theme(Breakpoints) {
        content()
    }
}
