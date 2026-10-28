package dev.tonholo.portfolio.theme

import androidx.compose.runtime.compositionLocalOf
import dev.tonholo.portfolio.theme.color.AccentSwatch
import dev.tonholo.portfolio.theme.color.InkScale
import dev.tonholo.portfolio.theme.color.MutedScale
import dev.tonholo.portfolio.theme.color.PaperScale
import dev.tonholo.portfolio.theme.color.SyntaxScale
import dev.tonholo.portfolio.theme.typography.Typography

/**
 * Aggregated design tokens resolved for the active color mode.
 *
 * Provided through [LocalPortfolioTokens] by [PortfolioTheme]; consumers read
 * the current snapshot via `LocalPortfolioTokens.current` inside composable
 * code. Layout spacing tokens are NOT bundled here; consume them via
 * [dev.tonholo.portfolio.theme.layout.SiteDimensions], whose CSS variables
 * are registered at `:root` per breakpoint.
 */
data class PortfolioTokens(
    val ink: InkScale,
    val paper: PaperScale,
    val muted: MutedScale,
    val accent: AccentSwatch,
    val syntax: SyntaxScale,
    val typography: Typography,
)

val LocalPortfolioTokens = compositionLocalOf<PortfolioTokens> {
    error("PortfolioTokens not provided. Wrap your content in PortfolioTheme { }.")
}
