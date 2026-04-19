package dev.tonholo.portfolio.theme.layout

import com.varabyte.kobweb.compose.css.CSSLengthNumericValue
import com.varabyte.kobweb.compose.css.StyleVariable
import org.jetbrains.compose.web.css.cssRem

// Fallbacks mirror [SpacingScale]'s Normal-density values. Styles that read
// these vars before `PortfolioTheme` has a chance to write them fall back to
// the Normal rhythm, so server-rendered HTML and theme-provider-less previews
// still lay out reasonably.
private const val PAD_Y_FALLBACK_REM = 5.0
private const val PAD_Y_SMALL_FALLBACK_REM = 2.5

/**
 * CSS custom properties mirroring [SpacingScale]. `PortfolioTheme` writes the
 * active density-scaled values onto these variables at its root; any
 * `CssStyle` that needs density-aware spacing reads them back via
 * `SpacingVars.PadY.value()` (or `PadYSmall.value()`), so a density change in
 * the tokens bundle flows to every consumer without touching call sites.
 *
 * Using a [StyleVariable] (rather than raw `var(--pad-y)` strings through
 * `styleModifier`) keeps the variable name under Kobweb's control and lets
 * consumers type-check the value they pass in.
 */
object SpacingVars {
    /** Default section vertical padding (`.section` in the handoff). */
    val PadY by StyleVariable<CSSLengthNumericValue>(
        defaultFallback = PAD_Y_FALLBACK_REM.cssRem,
    )

    /** Compressed padding used by secondary bands like the hiring CTA. */
    val PadYSmall by StyleVariable<CSSLengthNumericValue>(
        defaultFallback = PAD_Y_SMALL_FALLBACK_REM.cssRem,
    )
}
