package dev.tonholo.portfolio.components.organisms.projects

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.components.molecules.StatBlock
import dev.tonholo.portfolio.components.molecules.StatSize
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.dom.Div

// Handoff `.outcomes`: 3-column grid with a 1px gap whose background is
// `--paper-3` (light) / `--paper-2` (dark); each cell paints its own paper
// surface so the gutter reads as a hairline separator. Vertical margin is
// 32px above and below the grid. Below 820px (`Breakpoint.LG` in Silk) the
// grid collapses to a single column per the `@media (max-width: 820px)` rule.
private const val OUTCOMES_GAP_REM = 0.0625 // 1px at 1rem=16px
private const val OUTCOMES_MARGIN_Y_REM = 2.0 // 32px
private const val OUTCOMES_COLUMN_FR = 1.0
private const val OUTCOMES_DESKTOP_COLUMNS = 3

/**
 * Numeric pair used by [OutcomesGrid] to render a single outcome cell. Mirrors
 * the `{ n, l }` JSX shape inside `.outcomes` in the handoff (`ProjectPage`,
 * lines 155-168). The [value] is the display number (e.g. `"-73%"`, `"0 → 8"`)
 * and [label] is the caption copy rendered below.
 */
data class ProjectOutcome(val value: String, val label: String)

/**
 * Project detail organism: the numeric outcomes block. Ports `.outcomes` and
 * `.outcome-cell` in the handoff CSS into a 3-column grid of [StatBlock]
 * molecules configured with [StatSize.Large].
 *
 * The grid's 1px gap is backed by the `--paper-3` (light) or `--paper-2`
 * (dark) token, so the gutter reads as a hairline separator between cells
 * while each [StatBlock] paints its own paper surface. Below [Breakpoint.LG]
 * (820px in the handoff media query) the grid collapses to a single column.
 *
 * @param outcomes the list of [ProjectOutcome] data to render as cells.
 * @param modifier optional [Modifier] chained after the base grid styles.
 */
@Composable
fun OutcomesGrid(outcomes: List<ProjectOutcome>, modifier: Modifier = Modifier) {
    val tokens = LocalPortfolioTokens.current
    val colorMode = ColorMode.current
    val gridBackground = when (colorMode) {
        ColorMode.LIGHT -> tokens.paper.paper3
        ColorMode.DARK -> tokens.paper.paper2
    }

    val finalModifier = OutcomesGridStyle
        .toModifier()
        .backgroundColor(gridBackground)
        .then(modifier)

    Div(attrs = finalModifier.toAttrs()) {
        outcomes.forEach { outcome ->
            StatBlock(
                value = outcome.value,
                label = outcome.label,
                size = StatSize.Large,
                modifier = Modifier.backgroundColor(tokens.paper.paper),
            )
        }
    }
}

/**
 * Base `CssStyle` for the outcomes grid. Ports `.outcomes` from the handoff:
 * a single-column grid with a 1px gap and 32px vertical margin at the base
 * (mobile-first); promotes to 3 equal columns from [Breakpoint.LG] upwards to
 * match the `@media (max-width: 820px)` rule in `styles.css`. Background
 * color is applied inline by the composable so the hairline separator flips
 * with the active theme.
 */
val OutcomesGridStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Grid)
            .gridTemplateColumns { size(OUTCOMES_COLUMN_FR.fr) }
            .gap(OUTCOMES_GAP_REM.cssRem)
            .margin(topBottom = OUTCOMES_MARGIN_Y_REM.cssRem)
    }
    Breakpoint.LG {
        Modifier.gridTemplateColumns {
            repeat(OUTCOMES_DESKTOP_COLUMNS) { size(OUTCOMES_COLUMN_FR.fr) }
        }
    }
}
