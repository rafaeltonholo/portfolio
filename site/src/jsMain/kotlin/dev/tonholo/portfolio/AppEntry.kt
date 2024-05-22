package dev.tonholo.portfolio

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.core.App
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.style.CssStyleScopeBase
import com.varabyte.kobweb.silk.style.breakpoint.BreakpointSizes
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.unit.dp

private val Breakpoints = BreakpointSizes(
    sm = 320.dp,
    md = 768.dp,
    lg = 1024.dp,
    xl = 1300.dp,
)

val CssStyleScopeBase.breakpoints get() = Breakpoints

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
