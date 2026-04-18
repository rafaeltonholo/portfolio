package dev.tonholo.portfolio.theme

import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.style.breakpoint.BreakpointSizes
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.theme.color.Ink
import dev.tonholo.portfolio.theme.color.Paper
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

/**
 * Paints the body with the active paper token and ink text color so the page
 * background flips with the color mode. Handoff `body` uses `--paper` fill
 * and `--ink` text; without this registration the body falls through to the
 * browser default (white), which reads as broken once the user toggles to
 * dark mode or lands on a dark-theme default.
 */
@InitSilk
fun initPageBackground(ctx: InitSilkContext) {
    ctx.stylesheet.registerStyle("html.${ColorMode.LIGHT.cssClass} body") {
        base {
            Modifier
                .backgroundColor(Paper.light.paper)
                .color(Ink.light.ink)
        }
    }
    ctx.stylesheet.registerStyle("html.${ColorMode.DARK.cssClass} body") {
        base {
            Modifier
                .backgroundColor(Paper.dark.paper)
                .color(Ink.dark.ink)
        }
    }
}

private val ColorMode.cssClass: String
    get() = when (this) {
        ColorMode.LIGHT -> "silk-light"
        ColorMode.DARK -> "silk-dark"
    }
