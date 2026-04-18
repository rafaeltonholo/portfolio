package dev.tonholo.portfolio.components.organisms.projects

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.components.atoms.Dot
import dev.tonholo.portfolio.components.atoms.DotVariant
import dev.tonholo.portfolio.components.atoms.MetaLabel
import dev.tonholo.portfolio.components.atoms.Text
import dev.tonholo.portfolio.components.atoms.TextSlot
import dev.tonholo.portfolio.domain.model.Project
import dev.tonholo.portfolio.domain.model.ProjectKind
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.typography.FontFamilies
import dev.tonholo.portfolio.theme.typography.Typography
import dev.tonholo.portfolio.theme.typography.apply
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Text as ComposeText

// Handoff `.project-hero`: two-column split with a 64px gap, items aligned to
// the end (baseline) so the meta + title and info grid share a bottom line.
// Below 900px (`Breakpoint.LG` in Silk) the grid collapses to a single column
// with a 32px gap, matching the `@media (max-width: 900px)` rule.
private const val HERO_GRID_GAP_DESKTOP_REM = 4.0 // 64px
private const val HERO_GRID_GAP_MOBILE_REM = 2.0 // 32px
private const val HERO_GRID_COL_FR = 1.0
private const val HERO_MARGIN_BOTTOM_REM = 4.0 // 64px

// Meta label row sits 12px above the display `h1`, matching the handoff inline
// `style={{marginBottom:12}}` on the top `.meta` element.
private const val META_MARGIN_BOTTOM_REM = 0.75 // 12px

// Handoff `.project-info`: mono family at 13px, 2-column grid, 24px row gap,
// 32px column gap.
private const val INFO_FONT_SIZE_REM = 0.8125 // 13px
private const val INFO_ROW_GAP_REM = 1.5 // 24px
private const val INFO_COL_GAP_REM = 2.0 // 32px
private const val INFO_COL_FR = 1.0

// Handoff `.project-info .label`: 11px mono uppercase, 0.08em tracking; we
// route that typography through [MetaLabel], which supplies family/size via
// [Typography.metaSmall]. Labels sit 6px above each value per the handoff rule.
private const val INFO_LABEL_MARGIN_BOTTOM_REM = 0.375 // 6px

// Handoff `Status` value: `● shipped to production`. Unicode bullet is replaced
// with a [Dot] atom. 8px gap keeps the swatch clear of the label copy, mirroring
// the Nav status badge spacing.
private const val STATUS_ROW_GAP_REM = 0.5 // 8px

// Copy. Hardcoded for v1 like the rest of Phase 2; i18n lifts these into
// Compose Resources later.
private const val META_COMMERCIAL = "commercial project"
private const val META_OPEN_SOURCE = "open source"
private const val META_SEPARATOR = " \u00B7 "
private const val META_PREFIX = "// "
private const val LABEL_ROLE = "Role"
private const val LABEL_TIMELINE = "Timeline"
private const val LABEL_STACK = "Stack"
private const val LABEL_STATUS = "Status"
private const val STATUS_TEXT = "shipped to production"
private const val STACK_SEPARATOR = ", "

/**
 * Project detail hero organism. Maps to `.project-hero` in the handoff CSS
 * (`ProjectPage` JSX, `styles.css` lines 1179-1208): a two-column split with
 * a mono meta eyebrow (`// {kind} · {year}`) above the display `h1` on the
 * left, and a `.project-info` 2x2 grid of Role / Timeline / Stack / Status
 * label-value pairs on the right.
 *
 * The `Status` value is rendered as a pulsing [Dot] followed by the copy
 * `"shipped to production"` tinted with the active accent token.
 *
 * Below [Breakpoint.LG] the hero grid collapses to a single column, matching
 * the handoff's `@media (max-width: 900px)` rule.
 *
 * @param project the [Project] whose case-study metadata is rendered.
 * @param modifier optional [Modifier] chained after the base styles.
 */
@Composable
fun ProjectDetailHero(project: Project, modifier: Modifier = Modifier) {
    val finalModifier = ProjectDetailHeroStyle
        .toModifier()
        .then(modifier)

    Div(attrs = finalModifier.toAttrs()) {
        ProjectDetailHeroCopyColumn(project = project)
        ProjectDetailHeroInfoGrid(project = project)
    }
}

/**
 * Left column of the project hero: mono meta label (`// {kind} · {year}`) over
 * the display `h1` project name.
 */
@Composable
private fun ProjectDetailHeroCopyColumn(project: Project) {
    val tokens = LocalPortfolioTokens.current

    Div(attrs = Modifier.toAttrs()) {
        MetaLabel(
            text = projectMetaText(project = project),
            modifier = Modifier
                .display(DisplayStyle.Block)
                .margin(bottom = META_MARGIN_BOTTOM_REM.cssRem),
        )
        H1(
            attrs = Modifier
                .apply(Typography.h1)
                .color(tokens.ink.ink)
                .margin(topBottom = 0.cssRem)
                .toAttrs(),
        ) {
            ComposeText(project.name)
        }
    }
}

/**
 * Right column of the project hero: the `.project-info` grid rendering the four
 * Role / Timeline / Stack / Status pairs. Each pair uses a mono uppercase label
 * sitting above a body value; the Status pair composes a pulsing [Dot] with
 * the `"shipped to production"` copy, tinted with the active accent.
 */
@Composable
private fun ProjectDetailHeroInfoGrid(project: Project) {
    Div(attrs = ProjectDetailHeroInfoStyle.toModifier().toAttrs()) {
        ProjectDetailHeroInfoCell(label = LABEL_ROLE, value = project.role)
        ProjectDetailHeroInfoCell(label = LABEL_TIMELINE, value = project.year)
        ProjectDetailHeroInfoCell(
            label = LABEL_STACK,
            value = project.stack.joinToString(separator = STACK_SEPARATOR),
        )
        ProjectDetailHeroStatusCell()
    }
}

/**
 * Generic Role / Timeline / Stack cell: label on top, body value below. Values
 * inherit the surrounding mono family and font-size (13px) from [ProjectDetailHeroInfoStyle];
 * label color comes from the [MetaLabel] atom and value color is set to `--ink`
 * via [Text] with the default body slot.
 */
@Composable
private fun ProjectDetailHeroInfoCell(label: String, value: String) {
    val tokens = LocalPortfolioTokens.current

    Div(attrs = Modifier.toAttrs()) {
        MetaLabel(
            text = label,
            modifier = Modifier
                .display(DisplayStyle.Block)
                .margin(bottom = INFO_LABEL_MARGIN_BOTTOM_REM.cssRem),
        )
        Text(
            text = value,
            slot = TextSlot.BodySmall,
            color = tokens.ink.ink,
        )
    }
}

/**
 * Dedicated Status cell: the label row then a row combining a pulsing [Dot]
 * with the `"shipped to production"` copy. The whole value row adopts the
 * accent color so the dot and text visually pair, matching
 * `style={{color:'var(--accent)'}}` on the JSX `.val`.
 */
@Composable
private fun ProjectDetailHeroStatusCell() {
    val tokens = LocalPortfolioTokens.current

    Div(attrs = Modifier.toAttrs()) {
        MetaLabel(
            text = LABEL_STATUS,
            modifier = Modifier
                .display(DisplayStyle.Block)
                .margin(bottom = INFO_LABEL_MARGIN_BOTTOM_REM.cssRem),
        )
        Div(
            attrs = ProjectDetailHeroStatusRowStyle
                .toModifier()
                .color(tokens.accent.accent)
                .toAttrs(),
        ) {
            Dot(variant = DotVariant.Pulsing)
            Text(
                text = STATUS_TEXT,
                slot = TextSlot.BodySmall,
                color = tokens.accent.accent,
            )
        }
    }
}

/** Builds the eyebrow copy: `"// commercial project · 2021-2022"`. */
private fun projectMetaText(project: Project): String {
    val kindLabel = when (project.kind) {
        ProjectKind.Commercial -> META_COMMERCIAL
        ProjectKind.OpenSource -> META_OPEN_SOURCE
    }
    return META_PREFIX + kindLabel + META_SEPARATOR + project.year
}

// ---------------------------------------------------------------------------
// Styles
// ---------------------------------------------------------------------------

/**
 * Base `CssStyle` for the two-column hero grid. Ports `.project-hero` from the
 * handoff: single column with a 32px gap below [Breakpoint.LG]; from `LG`
 * upwards switches to `1fr 1fr` with a 64px gap and aligns items to the end so
 * the eyebrow + title and the info grid share a bottom line. 64px bottom margin
 * separates the hero from the cover image / first section below.
 */
val ProjectDetailHeroStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Grid)
            .gridTemplateColumns { size(HERO_GRID_COL_FR.fr) }
            .gap(HERO_GRID_GAP_MOBILE_REM.cssRem)
            .alignItems(AlignItems.Start)
            .margin(bottom = HERO_MARGIN_BOTTOM_REM.cssRem)
    }
    Breakpoint.LG {
        Modifier
            .gridTemplateColumns {
                size(HERO_GRID_COL_FR.fr)
                size(HERO_GRID_COL_FR.fr)
            }
            .gap(HERO_GRID_GAP_DESKTOP_REM.cssRem)
            .alignItems(AlignItems.End)
    }
}

/**
 * Base `CssStyle` for the `.project-info` grid. Two equal columns, 24px row
 * gap and 32px column gap, mono family at 13px so the values inherit a tidy
 * monospaced baseline. Label typography/color is owned by [MetaLabel]; the
 * value color is set inline by the cell composable.
 */
val ProjectDetailHeroInfoStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Grid)
            .gridTemplateColumns {
                size(INFO_COL_FR.fr)
                size(INFO_COL_FR.fr)
            }
            .gap(rowGap = INFO_ROW_GAP_REM.cssRem, columnGap = INFO_COL_GAP_REM.cssRem)
            .fontFamily(FontFamilies.MONO)
            .fontSize(INFO_FONT_SIZE_REM.cssRem)
    }
}

/**
 * Base `CssStyle` for the Status value row: `LegacyInlineFlex` so the [Dot] and
 * the `"shipped to production"` text sit on the same baseline with an 8px gap.
 * The accent color is applied inline so it follows the active theme.
 */
val ProjectDetailHeroStatusRowStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.LegacyInlineFlex)
            .alignItems(AlignItems.Center)
            .gap(STATUS_ROW_GAP_REM.cssRem)
    }
}
