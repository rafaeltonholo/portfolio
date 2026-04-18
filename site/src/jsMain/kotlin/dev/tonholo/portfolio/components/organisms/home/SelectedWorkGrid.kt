package dev.tonholo.portfolio.components.organisms.home

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
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
import dev.tonholo.portfolio.components.molecules.SectionHead
import dev.tonholo.portfolio.domain.model.Project
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.layout.ShellStyle
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Section

// Section head copy per handoff `home.jsx` lines 300-306: `// 00` + `Selected
// work` + a meta chip summarising the list length and span.
private const val SECTION_NUMBER = "// 00"
private const val SECTION_TITLE = "Selected work"
private const val META_PROJECTS_LABEL = "projects"
private const val META_YEAR_SPAN = "2021 - present"
private const val META_SEPARATOR = " \u00B7 " // " · " middle dot

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
 * Renders a [SectionHead] (`// 00 - Selected work`, trailing meta chip
 * summarising the project count and year span) followed by a 2-column grid of
 * [ProjectCardIde] entries. The 1px grid gap is backed by the `--paper-3`
 * token (light) or `--paper-2` (dark) so the gutter reads as a hairline
 * separator between cards. Below [Breakpoint.LG] the grid collapses to a
 * single column.
 *
 * @param projects the list of [Project]s to render as cards.
 * @param onProjectClick invoked with the [Project.slug] when a card is
 *   activated; route to the matching detail page.
 * @param modifier optional [Modifier] chained after the base section styles.
 */
@Composable
fun SelectedWorkGrid(
    projects: List<Project>,
    onProjectClick: (slug: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tokens = LocalPortfolioTokens.current
    val colorMode = ColorMode.current
    val gridBackground = when (colorMode) {
        ColorMode.LIGHT -> tokens.paper.paper3
        ColorMode.DARK -> tokens.paper.paper2
    }

    val sectionModifier = SelectedWorkGridStyle
        .toModifier()
        .then(modifier)

    Section(attrs = sectionModifier.toAttrs()) {
        Div(attrs = ShellStyle.toModifier().toAttrs()) {
            SectionHead(
                number = SECTION_NUMBER,
                title = SECTION_TITLE,
                trailing = {
                    MetaLabel(
                        text = "${projects.size} ${META_PROJECTS_LABEL}${META_SEPARATOR}${META_YEAR_SPAN}",
                    )
                },
            )
            Div(
                attrs = SelectedWorkProjectsGridStyle
                    .toModifier()
                    .backgroundColor(gridBackground)
                    .toAttrs(),
            ) {
                projects.forEach { project ->
                    ProjectCardIde(
                        project = project,
                        onClick = onProjectClick,
                    )
                }
            }
        }
    }
}

/**
 * Base `CssStyle` for the outer `<section>`. Kept empty so the inner
 * [ShellStyle] owns horizontal gutters; callers can chain vertical padding via
 * the [Modifier] argument.
 */
val SelectedWorkGridStyle = CssStyle {}

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
