package dev.tonholo.portfolio.components.organisms.home

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.components.atoms.MetaLabel
import dev.tonholo.portfolio.components.atoms.MetaSeparator
import dev.tonholo.portfolio.components.template.NumberedSection
import dev.tonholo.portfolio.domain.model.Project
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span

// Ordinal for the Selected Work section: renders as `// 00`.
private const val SECTION_ORDINAL = 0
private const val META_YEAR_SPAN = "2021 - present"

// Inline gap between the trailing meta segments and the centered separator dot.
private const val TRAILING_META_GAP_REM = 0.375 // 6px

// Grid spec per handoff CSS: `.projects-grid { display: grid; gap: 1px;
// background: var(--paper-3); }`. The 1px gap doubles as a hairline separator
// because the background colour fills the gutter while each card paints its
// own `--paper` surface. `.projects-grid.two` promotes to two columns; below
// the handoff `820px` media query the grid collapses to a single column.
private const val GRID_GAP_REM = 0.0625 // 1px at 1rem=16px
private const val GRID_COLUMN_FR = 1

/**
 * Home organism: the "Selected work" section. Ports the `<section class="section
 * shell">` block in `home.jsx` (lines 300-311) and the `.projects-grid.two
 * [data-cards="ide"]` rules in the handoff CSS.
 *
 * Wraps the grid in a [NumberedSection] (`// 00 - Selected work`) with a
 * trailing meta chip summarising the project count and year span. Pages that
 * already carry their own editorial header (e.g. `/projects` with its "Work."
 * title) call [ProjectsGrid] directly and provide their own shell.
 *
 * @param projects the list of [Project]s to render as cards.
 * @param modifier optional [Modifier] chained after the base section styles.
 */
@Composable
fun SelectedWorkGrid(projects: List<Project>, modifier: Modifier = Modifier) {
    val strings = LocalStrings.current.selectedWork

    NumberedSection(
        number = SECTION_ORDINAL,
        title = strings.sectionTitle,
        modifier = modifier,
        trailing = {
            Span(
                attrs = Modifier
                    .display(DisplayStyle.LegacyInlineFlex)
                    .alignItems(AlignItems.Center)
                    .gap(TRAILING_META_GAP_REM.cssRem)
                    .toAttrs(),
            ) {
                MetaLabel(text = "${projects.size} ${strings.projectsSuffix}")
                MetaSeparator()
                MetaLabel(text = META_YEAR_SPAN)
            }
        },
    ) {
        ProjectsGrid(projects = projects)
    }
}

/**
 * Inner 2-column grid of [ProjectCardIde]s. Exposed so pages that wrap their
 * own chrome (e.g. `/projects`) can render the grid without the
 * [NumberedSection] head; the home organism embeds it under [SelectedWorkGrid].
 * The 1px grid gap is backed by the `--paper-3` token (light) or `--paper-2`
 * (dark) so the gutter reads as a hairline separator between cards. Below
 * [Breakpoint.LG] the grid collapses to a single column.
 */
@Composable
fun ProjectsGrid(projects: List<Project>) {
    val tokens = LocalPortfolioTokens.current
    val colorMode = ColorMode.current
    val gridBackground = when (colorMode) {
        ColorMode.LIGHT -> tokens.paper.paper3
        ColorMode.DARK -> tokens.paper.paper2
    }

    Div(
        attrs = SelectedWorkProjectsGridStyle
            .toModifier()
            .backgroundColor(gridBackground)
            .toAttrs(),
    ) {
        projects.forEach { project ->
            ProjectCardIde(project = project)
        }
    }
}

/**
 * Base `CssStyle` for the 2-column projects grid. Ports `.projects-grid.two`
 * from the handoff CSS: a `repeat(2, 1fr)` grid with a 1px gap that doubles as
 * a hairline separator (the parent's `--paper-3` / `--paper-2` background
 * fills the gutter). Below [Breakpoint.LG] the grid collapses to a single
 * column to match the `@media (max-width: 820px)` rule in the handoff.
 */
val SelectedWorkProjectsGridStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Grid)
            .gridTemplateColumns {
                size(GRID_COLUMN_FR.fr)
            }
            .gap(GRID_GAP_REM.cssRem)
    }
    Breakpoint.LG {
        Modifier.gridTemplateColumns {
            repeat(2) { size(GRID_COLUMN_FR.fr) }
        }
    }
}
