package dev.tonholo.portfolio.components.organisms.about

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontStyle
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontStyle
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.components.atoms.AspectRatio
import dev.tonholo.portfolio.components.atoms.MetaLabel
import dev.tonholo.portfolio.components.atoms.MetaLabelVariant
import dev.tonholo.portfolio.components.atoms.Placeholder
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.typography.FontFamilies
import dev.tonholo.portfolio.theme.typography.Typography
import dev.tonholo.portfolio.theme.typography.apply
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Em
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text as ComposeText

// Handoff `.about-hdr`: two-column grid, `1.2fr 1fr`, 64px gap, items aligned
// to the start. Collapses to a single column below 900px (Silk's
// `Breakpoint.LG`) matching the `@media (max-width: 900px)` rule.
private const val GRID_GAP_REM = 4.0 // 64px
private const val GRID_WIDE_FR = 1.2
private const val GRID_NARROW_FR = 1.0
private const val GRID_COL_FR = 1.0

// Handoff inline styles on the copy column.
private const val META_MARGIN_BOTTOM_REM = 1.25 // 20px
private const val TITLE_MARGIN_BOTTOM_REM = 1.75 // 28px

// Handoff lede paragraph: serif, 20px size, 1.55 line-height, 48ch cap,
// `ink-2` color.
private const val LEDE_SIZE_REM = 1.25 // 20px
private const val LEDE_LINE_HEIGHT = 1.55
private const val LEDE_MAX_WIDTH_CH = 48.0

// Hardcoded v1 copy. Lifts into Compose Resources during the i18n pass, same
// approach as every other Phase 2 organism.
private const val META_TEXT = "// about.me"
private const val TITLE_PREFIX = "The long way to "
private const val TITLE_EMPHASIS = "Kotlin."
private const val LEDE_TEXT =
    "Brazilian, Ottawa-based, Android-focused. Started programming at 12 on a " +
        "MU server for my cousins. Haven't stopped since."
private const val PORTRAIT_LABEL = "portrait - 4:5"

/**
 * About page header organism. Maps to `.about-hdr` in the handoff CSS: a
 * two-column layout with editorial copy on the left (mono meta label, display
 * `h1`, serif lede) and a tall 4:5 portrait placeholder on the right.
 *
 * Below [Breakpoint.LG] (900px) the grid collapses to a single column to match
 * the `@media (max-width: 900px) { .about-hdr { grid-template-columns: 1fr; } }`
 * rule in `styles.css`.
 *
 * @param modifier optional [Modifier] chained after the base styles.
 */
@Composable
fun AboutHeader(modifier: Modifier = Modifier) {
    val finalModifier = AboutHeaderStyle
        .toModifier()
        .then(modifier)

    Div(attrs = finalModifier.toAttrs()) {
        AboutHeaderCopyColumn()
        Placeholder(aspectRatio = AspectRatio.Portrait4x5, label = PORTRAIT_LABEL)
    }
}

/**
 * Left column: mono accent meta label, display `h1` with an accent-colored
 * serif italic span for the emphasized word, and the serif lede paragraph.
 * Mirrors the first `<div>` inside `.about-hdr` in the handoff JSX.
 */
@Composable
private fun AboutHeaderCopyColumn() {
    val tokens = LocalPortfolioTokens.current

    Div(attrs = Modifier.toAttrs()) {
        MetaLabel(
            text = META_TEXT,
            variant = MetaLabelVariant.Accent,
            modifier = Modifier
                .display(DisplayStyle.Block)
                .margin(bottom = META_MARGIN_BOTTOM_REM.cssRem),
        )
        H1(
            attrs = Modifier
                .apply(Typography.h1)
                .color(tokens.ink.ink)
                .margin(top = 0.cssRem, bottom = TITLE_MARGIN_BOTTOM_REM.cssRem)
                .toAttrs(),
        ) {
            ComposeText(TITLE_PREFIX)
            Em(
                attrs = AboutHeaderTitleEmphasisStyle
                    .toModifier()
                    .color(tokens.accent.accent)
                    .toAttrs(),
            ) {
                ComposeText(TITLE_EMPHASIS)
            }
        }
        P(
            attrs = AboutHeaderLedeStyle
                .toModifier()
                .color(tokens.ink.ink2)
                .toAttrs(),
        ) {
            ComposeText(LEDE_TEXT)
        }
    }
}

// ---------------------------------------------------------------------------
// Styles
// ---------------------------------------------------------------------------

/**
 * Base `CssStyle` for the outer two-column grid. Ports `.about-hdr` from the
 * handoff: single column at the base width (mobile-first); switches to the
 * `1.2fr 1fr` split at [Breakpoint.LG] with a 64px gap and items aligned to
 * the start so the tall portrait card does not stretch.
 */
val AboutHeaderStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Grid)
            .gridTemplateColumns { size(GRID_COL_FR.fr) }
            .gap(GRID_GAP_REM.cssRem)
            .alignItems(AlignItems.Start)
    }
    Breakpoint.LG {
        Modifier.gridTemplateColumns {
            size(GRID_WIDE_FR.fr)
            size(GRID_NARROW_FR.fr)
        }
    }
}

/**
 * Base `CssStyle` for the `<em>` inside the `h1`. Ports the handoff's
 * emphasized word styling: italic serif with the accent color applied inline
 * by the composable so it follows the active theme.
 */
val AboutHeaderTitleEmphasisStyle = CssStyle {
    base {
        Modifier
            .fontFamily(FontFamilies.SERIF)
            .fontStyle(FontStyle.Italic)
    }
}

/**
 * Base `CssStyle` for the lede paragraph. Ports the inline styles on the
 * handoff `<p>`: serif family, 20px size, 1.55 line-height, 48ch cap. Color
 * is applied inline by the composable from the `ink-2` token.
 */
val AboutHeaderLedeStyle = CssStyle {
    base {
        Modifier
            .fontFamily(FontFamilies.SERIF)
            .fontSize(LEDE_SIZE_REM.cssRem)
            .lineHeight(LEDE_LINE_HEIGHT)
            .margin(topBottom = 0.cssRem)
            .maxWidth(LEDE_MAX_WIDTH_CH.unsafeCast<CSSSizeValue<CSSUnit.ch>>())
    }
}
