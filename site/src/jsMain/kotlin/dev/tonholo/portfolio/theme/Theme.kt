package dev.tonholo.portfolio.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.varabyte.kobweb.compose.css.StyleVariable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.setVariable
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.theme.color.AccentMint
import dev.tonholo.portfolio.theme.color.Ink
import dev.tonholo.portfolio.theme.color.Muted
import dev.tonholo.portfolio.theme.color.Paper
import dev.tonholo.portfolio.theme.color.Syntax
import dev.tonholo.portfolio.theme.layout.Density
import dev.tonholo.portfolio.theme.layout.SpacingScale
import dev.tonholo.portfolio.theme.layout.SpacingVars
import dev.tonholo.portfolio.theme.typography.Typography
import kotlinx.browser.document
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLElement

private const val DATA_THEME_ATTR = "data-theme"
private const val DATA_ACCENT_ATTR = "data-accent"
private const val DATA_THEME_LIGHT = "light"
private const val DATA_THEME_DARK = "dark"
private const val DATA_ACCENT_MINT = "mint"

/**
 * Root theme provider for the portfolio.
 *
 * - Resolves [PortfolioTokens] from the current Silk [ColorMode].
 * - Mirrors the theme state onto `<html>` as `data-theme="light|dark"` and
 *   `data-accent="mint"` so CSS selectors and the boot-time script in
 *   `index.html` stay consistent with the composition.
 * - Writes the active [SpacingScale] onto [SpacingVars] via a wrapper `<div>`
 *   so density-aware `CssStyle` blocks (e.g. `PageSectionShellStyle`) pick
 *   up the right value without reading [LocalPortfolioTokens] at call sites.
 */
@Composable
fun PortfolioTheme(content: @Composable () -> Unit) {
    val colorMode by ColorMode.currentState
    val isDark = colorMode.isDark

    val tokens = remember(isDark) { tokensFor(isDark = isDark) }

    DisposableEffect(isDark) {
        val root = document.documentElement as? HTMLElement
        root?.setAttribute(
            DATA_THEME_ATTR,
            if (isDark) DATA_THEME_DARK else DATA_THEME_LIGHT,
        )
        root?.setAttribute(DATA_ACCENT_ATTR, DATA_ACCENT_MINT)
        onDispose { }
    }

    val spacingModifier = Modifier
        .setVariable(SpacingVars.PadY, tokens.spacing.padY)
        .setVariable(SpacingVars.PadYSmall, tokens.spacing.padYSmall)

    CompositionLocalProvider(LocalPortfolioTokens provides tokens) {
        Div(attrs = spacingModifier.toAttrs()) {
            content()
        }
    }
}

/**
 * Resolves the full [PortfolioTokens] bundle for the active color mode. Kept
 * as a non-composable helper so [PortfolioTheme] stays under the detekt
 * cognitive-complexity limit as the token surface grows.
 */
private fun tokensFor(isDark: Boolean): PortfolioTokens = PortfolioTokens(
    ink = if (isDark) Ink.dark else Ink.light,
    paper = if (isDark) Paper.dark else Paper.light,
    muted = if (isDark) Muted.dark else Muted.light,
    accent = if (isDark) AccentMint.dark else AccentMint.light,
    syntax = if (isDark) Syntax.dark else Syntax.light,
    spacing = SpacingScale.from(density = Density.Compact),
    typography = Typography,
)

object PortfolioThemeVars {
    val FocusRing by StyleVariable<Color>(prefix = "base-theme")
}
