package dev.tonholo.portfolio.theme

import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.init.registerStyleBase
import com.varabyte.kobweb.silk.style.breakpoint.BreakpointSizes
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.theme.color.Ink
import dev.tonholo.portfolio.theme.color.Paper
import org.jetbrains.compose.web.css.CSSMediaQuery
import org.jetbrains.compose.web.css.StylePropertyValue
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

// Focus ring hue per color mode. Light uses the darkened mint that passes
// 4.5:1 on paper; dark uses the bright mint swatch that reads cleanly on ink.
private const val FOCUS_RING_LIGHT = "#2F7A45"
private const val FOCUS_RING_DARK = "#7DD198"

// Single !important transition/animation override. !important is necessary
// here because every component style defines its own `transition(...)` and
// would otherwise win the cascade.
private const val REDUCED_MOTION_DURATION = "0.01ms !important"
private const val REDUCED_MOTION_ITERATION = "1 !important"
private const val REDUCED_MOTION_SCROLL = "auto !important"

/**
 * Global a11y layer applied once at Silk init.
 *
 * Two cross-cutting concerns that cannot live in a single per-component style:
 *
 * 1. **Keyboard focus visibility (WCAG 2.4.7).** Draws a 2px ring on
 *    `:focus-visible` using a CSS custom property so the color flips with the
 *    active theme. `:focus` without `:focus-visible` gets its outline reset so
 *    mouse clicks on anchors and buttons do not draw a ring (the old
 *    default-browser look).
 *
 * 2. **Reduced motion (WCAG 2.3.3 + respect the OS preference).** When the
 *    user prefers reduced motion, every element's transition and animation
 *    duration collapses to 0.01ms. `!important` is needed because every
 *    interactive style defines its own `transition(...)` and would otherwise
 *    override the media-query rule.
 */
@InitSilk
fun initAccessibilityLayer(ctx: InitSilkContext) {
    ctx.stylesheet.registerStyleBase("html.${ColorMode.LIGHT.cssClass}") {
        Modifier.styleModifier { property("--focus-ring", FOCUS_RING_LIGHT) }
    }
    ctx.stylesheet.registerStyleBase("html.${ColorMode.DARK.cssClass}") {
        Modifier.styleModifier { property("--focus-ring", FOCUS_RING_DARK) }
    }
    ctx.stylesheet.registerStyleBase(":focus") {
        Modifier.styleModifier { property("outline", "none") }
    }
    ctx.stylesheet.registerStyleBase(":focus-visible") {
        Modifier.styleModifier {
            property("outline", "2px solid var(--focus-ring)")
            property("outline-offset", "2px")
            property("border-radius", "2px")
        }
    }
    ctx.stylesheet.registerStyle("*, *::before, *::after") {
        cssRule(
            mediaQuery = CSSMediaQuery.MediaFeature(
                name = "prefers-reduced-motion",
                value = StylePropertyValue(value = "reduce"),
            ),
        ) {
            Modifier.styleModifier {
                property("animation-duration", REDUCED_MOTION_DURATION)
                property("animation-iteration-count", REDUCED_MOTION_ITERATION)
                property("transition-duration", REDUCED_MOTION_DURATION)
                property("scroll-behavior", REDUCED_MOTION_SCROLL)
            }
        }
    }
}
