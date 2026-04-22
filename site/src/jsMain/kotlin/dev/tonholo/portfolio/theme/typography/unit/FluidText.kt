package dev.tonholo.portfolio.theme.typography.unit

import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit

/**
 * Emits a CSS `clamp(minRem, calc(preferredVw + biasRem), maxRem)` tuned for
 * fluid display typography. The result is typed as `CSSSizeValue<CSSUnit.rem>`
 * via `unsafeCast` so it can flow into the shared `TextStyle.size` slot even
 * though the intermediate preferred term mixes `vw` and `rem`.
 *
 * This is the `calc` exception documented in `CLAUDE.md` for mixed-unit
 * expressions; the preferred term is intentionally expressed in the
 * stylesheet rather than substituted by `Modifier.styleModifier`.
 *
 * @param minRem lower bound; what small phones get.
 * @param preferredVw viewport-driven coefficient (percentage of viewport
 *   width contributing to the fluid mid-range).
 * @param biasRem static rem bias added to the viewport term so the minimum
 *   fluid value never collapses below a readable floor.
 * @param maxRem upper bound; what large desktops get.
 */
fun fluidRem(
    minRem: Double,
    preferredVw: Double,
    biasRem: Double,
    maxRem: Double,
): CSSSizeValue<CSSUnit.rem> {
    val expression = "clamp(${minRem}rem, calc(${preferredVw}vw + ${biasRem}rem), ${maxRem}rem)"
    return expression.unsafeCast<CSSSizeValue<CSSUnit.rem>>()
}
