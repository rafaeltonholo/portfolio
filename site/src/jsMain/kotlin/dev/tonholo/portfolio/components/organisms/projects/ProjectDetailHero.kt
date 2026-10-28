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
import dev.tonholo.portfolio.components.atoms.MetaSeparator
import dev.tonholo.portfolio.components.atoms.Text
import dev.tonholo.portfolio.components.atoms.TextSlot
import dev.tonholo.portfolio.domain.model.Project
import dev.tonholo.portfolio.domain.model.ProjectKind
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.locale.Strings
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.layout.unit.dp
import dev.tonholo.portfolio.theme.typography.FontFamilies
import dev.tonholo.portfolio.theme.typography.Typography
import dev.tonholo.portfolio.theme.typography.apply
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Text as ComposeText

// Handoff `.project-hero`: two-column split with a 64px gap, items aligned to
// the end (baseline) so the meta + title and info grid share a bottom line.
// Below 900px (`Breakpoint.LG` in Silk) the grid collapses to a single column
// with a 32px gap, matching the `@media (max-width: 900px)` rule.
private val HERO_GRID_GAP_DESKTOP = 64.dp
private val HERO_GRID_GAP_MOBILE = 32.dp
private const val HERO_GRID_COL_FR = 1.0
private val HERO_MARGIN_BOTTOM = 64.dp

// Meta label row sits 12px above the display `h1`, matching the handoff inline
// `style={{marginBottom:12}}` on the top `.meta` element.
private val META_MARGIN_BOTTOM = 12.dp

// Handoff `.project-info`: mono family at 13px, 2-column grid, 24px row gap,
// 32px column gap.
private val INFO_FONT_SIZE = 13.dp
private val INFO_ROW_GAP = 24.dp
private val INFO_COL_GAP = 32.dp
private const val INFO_COL_FR = 1.0

// Handoff `.project-info .label`: 11px mono uppercase, 0.08em tracking; we
// route that typography through [MetaLabel], which supplies family/size via
// [Typography.metaSmall]. Labels sit 6px above each value per the handoff rule.
private val INFO_LABEL_MARGIN_BOTTOM = 6.dp

// Handoff `Status` value: `● shipped to production`. Unicode bullet is replaced
// with a [Dot] atom. 8px gap keeps the swatch clear of the label copy, mirroring
// the Nav status badge spacing.
private val STATUS_ROW_GAP = 8.dp

private const val META_PREFIX = "// "
private const val STACK_SEPARATOR = ", "

// Inline gap between the eyebrow segments and the centered separator dot.
private val META_ROW_GAP = 6.dp

/**
 * Project detail hero organism. Maps to `.project-hero` in the handoff CSS
 * (`ProjectPage` JSX, `styles.css` lines 1179-1208): a two-column split with
 * a mono meta eyebrow (`// {kind}` then a [MetaSeparator] dot then `{year}`) above the display `h1` on the
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
    val strings = LocalStrings.current.projectDetail

    val finalModifier = ProjectDetailHeroStyle
        .toModifier()
        .then(modifier)

    Div(attrs = finalModifier.toAttrs()) {
        ProjectDetailHeroCopyColumn(project = project, strings = strings)
        ProjectDetailHeroInfoGrid(project = project, strings = strings)
    }
}

/**
 * Left column of the project hero: mono long-form eyebrow (`// {kind}` then
 * a [MetaSeparator] dot then `{year}`) over the display `h1` project name.
 *
 * The eyebrow uses [TextSlot.MetaPlain] rather than [MetaLabel] because
 * the full phrase runs 31-35 characters; ALL-CAPS at that length defeats
 * word-shape recognition and reads as shouty body copy. Short-label meta
 * (`ROLE`, `STATUS`, etc.) still uses [MetaLabel] - see
 * [ProjectDetailHeroInfoCell] below.
 */
@Composable
private fun ProjectDetailHeroCopyColumn(project: Project, strings: Strings.ProjectDetail) {
    val tokens = LocalPortfolioTokens.current
    val kindLabel = projectKindLabel(project = project, strings = strings)

    Div(attrs = Modifier.toAttrs()) {
        Div(
            attrs = Modifier
                .display(DisplayStyle.LegacyInlineFlex)
                .alignItems(AlignItems.Center)
                .gap(META_ROW_GAP)
                .margin(bottom = META_MARGIN_BOTTOM)
                .toAttrs(),
        ) {
            Text(
                text = "$META_PREFIX$kindLabel",
                slot = TextSlot.MetaPlain,
                color = tokens.muted.muted,
            )
            MetaSeparator()
            Text(
                text = project.year,
                slot = TextSlot.MetaPlain,
                color = tokens.muted.muted,
            )
        }
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
 * the shipped-status copy tinted with the active accent.
 */
@Composable
private fun ProjectDetailHeroInfoGrid(project: Project, strings: Strings.ProjectDetail) {
    Div(attrs = ProjectDetailHeroInfoStyle.toModifier().toAttrs()) {
        ProjectDetailHeroInfoCell(label = strings.labelRole, value = project.role)
        ProjectDetailHeroInfoCell(label = strings.labelTimeline, value = project.year)
        ProjectDetailHeroInfoCell(
            label = strings.labelStack,
            value = project.stack.joinToString(separator = STACK_SEPARATOR),
        )
        ProjectDetailHeroStatusCell(strings = strings)
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
                .margin(bottom = INFO_LABEL_MARGIN_BOTTOM),
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
 * with the localized shipped-status copy. The whole value row adopts the
 * accent color so the dot and text visually pair, matching
 * `style={{color:'var(--accent)'}}` on the JSX `.val`.
 */
@Composable
private fun ProjectDetailHeroStatusCell(strings: Strings.ProjectDetail) {
    val tokens = LocalPortfolioTokens.current

    Div(attrs = Modifier.toAttrs()) {
        MetaLabel(
            text = strings.labelStatus,
            modifier = Modifier
                .display(DisplayStyle.Block)
                .margin(bottom = INFO_LABEL_MARGIN_BOTTOM),
        )
        Div(
            attrs = ProjectDetailHeroStatusRowStyle
                .toModifier()
                .color(tokens.accent.accent)
                .toAttrs(),
        ) {
            Dot(variant = DotVariant.Pulsing)
            Text(
                text = strings.statusShipped,
                slot = TextSlot.BodySmall,
                color = tokens.accent.accent,
            )
        }
    }
}

/** Resolves the localized kind label rendered in the eyebrow. */
private fun projectKindLabel(project: Project, strings: Strings.ProjectDetail): String =
    project.kinds.joinToString(separator = " / ") { kind ->
        when (kind) {
            ProjectKind.Commercial -> strings.commercialKind
            ProjectKind.OpenSource -> strings.openSourceKind
        }
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
            .gridTemplateColumns { minmax(0.px, HERO_GRID_COL_FR.fr) }
            .gap(HERO_GRID_GAP_MOBILE)
            .alignItems(AlignItems.Start)
            .margin(bottom = HERO_MARGIN_BOTTOM)
    }
    Breakpoint.LG {
        Modifier
            .gridTemplateColumns {
                minmax(0.px, HERO_GRID_COL_FR.fr)
                minmax(0.px, HERO_GRID_COL_FR.fr)
            }
            .gap(HERO_GRID_GAP_DESKTOP)
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
            .gap(rowGap = INFO_ROW_GAP, columnGap = INFO_COL_GAP)
            .fontFamily(*FontFamilies.MONO)
            .fontSize(INFO_FONT_SIZE)
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
            .gap(STATUS_ROW_GAP)
    }
}
