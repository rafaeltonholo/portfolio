package dev.tonholo.portfolio.theme

import androidx.compose.runtime.compositionLocalOf
import dev.tonholo.portfolio.theme.color.AccentSwatch
import dev.tonholo.portfolio.theme.color.InkScale
import dev.tonholo.portfolio.theme.color.MutedScale
import dev.tonholo.portfolio.theme.color.PaperScale
import dev.tonholo.portfolio.theme.layout.SpacingScale
import dev.tonholo.portfolio.theme.typography.Typography

/**
 * Aggregated design tokens resolved for the active color mode and density.
 *
 * Provided through [LocalPortfolioTokens] by [PortfolioTheme]; consumers read
 * the current snapshot via `LocalPortfolioTokens.current` inside composable
 * code.
 */
data class PortfolioTokens(
    val ink: InkScale,
    val paper: PaperScale,
    val muted: MutedScale,
    val accent: AccentSwatch,
    val spacing: SpacingScale,
    val typography: Typography,
)

val LocalPortfolioTokens = compositionLocalOf<PortfolioTokens> {
    error("PortfolioTokens not provided. Wrap your content in PortfolioTheme { }.")
}
