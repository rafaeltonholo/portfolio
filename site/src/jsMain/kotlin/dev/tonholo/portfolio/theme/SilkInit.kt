package dev.tonholo.portfolio.theme

import com.varabyte.kobweb.compose.css.Animation
import com.varabyte.kobweb.compose.css.AnimationIterationCount
import com.varabyte.kobweb.compose.css.OutlineWidth
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.animation
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.outline
import com.varabyte.kobweb.compose.ui.modifiers.outlineOffset
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import com.varabyte.kobweb.compose.ui.modifiers.setVariable
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.init.registerStyleBase
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.breakpoint.BreakpointSizes
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.state.resolveInitialColorMode
import dev.tonholo.portfolio.theme.color.AccentMint
import dev.tonholo.portfolio.theme.color.Ink
import dev.tonholo.portfolio.theme.color.Paper
import dev.tonholo.portfolio.theme.layout.SiteSizeVar
import dev.tonholo.portfolio.theme.layout.unit.dp
import dev.tonholo.portfolio.theme.typography.FontFamilies
import dev.tonholo.portfolio.theme.typography.applyInlineCodeStyle
import dev.tonholo.portfolio.theme.typography.unit.DEFAULT_FONT_SIZE
import org.jetbrains.compose.web.css.CSSMediaQuery
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.StylePropertyValue
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.px

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
 * Anchors `1rem` to the [DEFAULT_FONT_SIZE] pixel value on the root element so
 * every `rem`-based token (including [dev.tonholo.portfolio.theme.layout.unit.Dp]
 * and [dev.tonholo.portfolio.theme.typography.unit.TextUnit]) converts to the
 * intended pixel values. Without this rule we inherit the user-agent default,
 * which users can override via browser zoom settings and shift the entire
 * layout off the handoff grid.
 */
@InitSilk
fun initRootFontSize(ctx: InitSilkContext) {
    ctx.stylesheet.registerStyleBase("html") {
        Modifier
            .fontFamily(*FontFamilies.BODY)
            .fontSize(DEFAULT_FONT_SIZE.px)
    }
}

/**
 * Initializes Silk's color mode from stored preferences or system theme so
 * the initial render matches the user's expected theme without a flash.
 */
@InitSilk
fun initColorMode(ctx: InitSilkContext) {
    ctx.config.initialColorMode = resolveInitialColorMode()
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
                .fontFamily(*FontFamilies.BODY)
                .backgroundColor(Paper.light.paper)
                .color(Ink.light.ink)
        }
    }
    ctx.stylesheet.registerStyle("html.${ColorMode.DARK.cssClass} body") {
        base {
            Modifier
                .fontFamily(*FontFamilies.BODY)
                .backgroundColor(Paper.dark.paper)
                .color(Ink.dark.ink)
        }
    }
}

/**
 * Global styling for inline `<code>` tags across the site, matching the `@old-layout` design:
 * JetBrains Mono font, 4px horizontal and 2px vertical padding, 4px corner radius,
 * 32px line height, and a subtle theme-aware background with `anywhere` overflow wrap.
 */
@InitSilk
fun initInlineCode(ctx: InitSilkContext) {
    ctx.stylesheet.registerStyle("html.${ColorMode.LIGHT.cssClass} :not(pre) > code") {
        base {
            Modifier.applyInlineCodeStyle(isDark = false)
        }
    }
    ctx.stylesheet.registerStyle("html.${ColorMode.DARK.cssClass} :not(pre) > code") {
        base {
            Modifier.applyInlineCodeStyle(isDark = true)
        }
    }
}

private val ColorMode.cssClass: String
    get() = when (this) {
        ColorMode.LIGHT -> "silk-light"
        ColorMode.DARK -> "silk-dark"
    }

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
        Modifier
            .setVariable(PortfolioThemeVars.FocusRing, AccentMint.light.accent)
    }
    ctx.stylesheet.registerStyleBase("html.${ColorMode.DARK.cssClass}") {
        Modifier
            .setVariable(PortfolioThemeVars.FocusRing, AccentMint.dark.accent)
    }
    ctx.stylesheet.registerStyleBase(":focus") {
        Modifier.outline(style = LineStyle.None)
    }
    ctx.stylesheet.registerStyleBase(":focus-visible") {
        Modifier
            .outline {
                width(OutlineWidth.of(2.dp))
                style(LineStyle.Solid)
                color(PortfolioThemeVars.FocusRing.value())
            }
            .outlineOffset(2.dp)
            .borderRadius(2.dp)
    }
    ctx.stylesheet.registerStyle("*, *::before, *::after") {
        cssRule(
            mediaQuery = CSSMediaQuery.MediaFeature(
                name = "prefers-reduced-motion",
                value = StylePropertyValue(value = "reduce"),
            ),
        ) {
            Modifier
                .animation(
                    Animation.of(
                        name = "reduced-motion",
                        duration = 0.01.ms,
                        iterationCount = AnimationIterationCount.of(1),
                    ),
                )
                .transition { duration(0.01.ms) }
                .scrollBehavior(ScrollBehavior.Auto)
        }
    }
}

private const val LAYOUT_TRANSITION_DURATION_MS = 300

private val LAYOUT_TRANSITION_PROPERTIES = listOf("padding", "margin", "gap", "max-width")

private val LayoutTransitions: List<Transition.Listable> =
    LAYOUT_TRANSITION_PROPERTIES.map { property ->
        Transition.of(
            property = property,
            duration = LAYOUT_TRANSITION_DURATION_MS.ms,
            timingFunction = TransitionTimingFunction.EaseOut,
        )
    }

/**
 * Registers the [SiteSizeVar] scale at `:root` for every active breakpoint
 * and adds a smooth transition for layout properties on `main, section, nav,
 * footer` so resizing the viewport across breakpoints does not jank.
 *
 * Smaller tokens (Xsm..Lg) stay constant across breakpoints; only `Xl` and
 * `Xxl` ramp upward, matching the svg-to-compose precedent. Adjust the
 * values below if rhythm needs tuning after the home migration lands.
 */
@InitSilk
fun initLayoutTokens(ctx: InitSilkContext) {
    ctx.stylesheet.registerStyle(":root") {
        base {
            Modifier
                .setVariable(SiteSizeVar.Xsm, 0.25.cssRem)
                .setVariable(SiteSizeVar.Sm, 0.5.cssRem)
                .setVariable(SiteSizeVar.Md, 0.75.cssRem)
                .setVariable(SiteSizeVar.Lg, 1.cssRem)
                .setVariable(SiteSizeVar.Xl, 1.5.cssRem)
                .setVariable(SiteSizeVar.Xxl, 2.cssRem)
        }
        Breakpoint.MD {
            Modifier
                .setVariable(SiteSizeVar.Xsm, 0.25.cssRem)
                .setVariable(SiteSizeVar.Sm, 0.5.cssRem)
                .setVariable(SiteSizeVar.Md, 0.75.cssRem)
                .setVariable(SiteSizeVar.Lg, 1.cssRem)
                .setVariable(SiteSizeVar.Xl, 1.5.cssRem)
                .setVariable(SiteSizeVar.Xxl, 3.cssRem)
        }
        Breakpoint.LG {
            Modifier
                .setVariable(SiteSizeVar.Xsm, 0.25.cssRem)
                .setVariable(SiteSizeVar.Sm, 0.5.cssRem)
                .setVariable(SiteSizeVar.Md, 0.75.cssRem)
                .setVariable(SiteSizeVar.Lg, 1.cssRem)
                .setVariable(SiteSizeVar.Xl, 2.cssRem)
                .setVariable(SiteSizeVar.Xxl, 3.cssRem)
        }
        Breakpoint.XL {
            Modifier
                .setVariable(SiteSizeVar.Xsm, 0.25.cssRem)
                .setVariable(SiteSizeVar.Sm, 0.5.cssRem)
                .setVariable(SiteSizeVar.Md, 0.75.cssRem)
                .setVariable(SiteSizeVar.Lg, 1.cssRem)
                .setVariable(SiteSizeVar.Xl, 2.cssRem)
                .setVariable(SiteSizeVar.Xxl, 4.cssRem)
        }
    }

    ctx.stylesheet.registerStyle("main, section, nav, footer") {
        cssRule(
            mediaQuery = CSSMediaQuery.MediaFeature(
                name = "prefers-reduced-motion",
                value = StylePropertyValue(value = "no-preference"),
            ),
        ) {
            Modifier.transition(LayoutTransitions)
        }
    }
}
