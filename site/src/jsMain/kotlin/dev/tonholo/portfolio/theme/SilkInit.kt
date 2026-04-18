package dev.tonholo.portfolio.theme

import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.style.breakpoint.BreakpointSizes
import org.jetbrains.compose.web.css.cssRem

private const val SM_REM = 30.0
private const val MD_REM = 45.0
private const val LG_REM = 51.25
private const val XL_REM = 56.25

/**
 * Configures Silk breakpoints to match the Phase 2 design handoff.
 *
 * | Slot | cssRem | px     | Handoff label |
 * |------|--------|--------|---------------|
 * | SM   | 30     | 480    | small phones  |
 * | MD   | 45     | 720    | "Mobile" cutoff (shell gutter flips to 48px) |
 * | LG   | 51.25  | 820    | "Tablet"      |
 * | XL   | 56.25  | 900    | "TabletLg"    |
 *
 * XXL is left at its default by omitting it; [BreakpointSizes] falls back to XL.
 */
@InitSilk
fun initBreakpoints(ctx: InitSilkContext) {
    ctx.theme.breakpoints = BreakpointSizes(
        sm = SM_REM.cssRem,
        md = MD_REM.cssRem,
        lg = LG_REM.cssRem,
        xl = XL_REM.cssRem,
    )
}
