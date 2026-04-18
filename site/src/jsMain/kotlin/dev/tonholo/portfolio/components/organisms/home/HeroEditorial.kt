package dev.tonholo.portfolio.components.organisms.home

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.BoxDecorationBreak
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.boxDecorationBreak
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.components.atoms.Button
import dev.tonholo.portfolio.components.atoms.ButtonVariant
import dev.tonholo.portfolio.components.atoms.MetaLabel
import dev.tonholo.portfolio.components.atoms.MetaLabelVariant
import dev.tonholo.portfolio.components.atoms.PhosphorIcon
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.layout.ShellStyle
import dev.tonholo.portfolio.theme.typography.Typography
import dev.tonholo.portfolio.theme.typography.apply
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.dom.Br
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text as ComposeText

// ---------------------------------------------------------------------------
// Section chrome. Handoff `.hero`:
//   padding: calc(var(--pad-y) * 1.2) 0 var(--pad-y);
// --pad-y compact density = 80px * 0.72 = ~57.6px -> rem values below match
// the design-token spacing (6rem top, 5rem bottom) for the pinned density.
// ---------------------------------------------------------------------------
private const val HERO_PADDING_TOP_REM = 6.0
private const val HERO_PADDING_BOTTOM_REM = 5.0

// Handoff `.hero-a`: two-column grid, 64px gap. Collapses to a single column
// below 900px (Silk's `Breakpoint.LG`) with a 32px gap.
private const val HERO_GRID_GAP_DESKTOP_REM = 4.0 // 64px
private const val HERO_GRID_GAP_MOBILE_REM = 2.0 // 32px
private const val HERO_GRID_COL_FR = 1

// Handoff inline styles on the meta label row and the CTA row.
private const val META_MARGIN_BOTTOM_REM = 1.25 // 20px
private const val TITLE_MARGIN_BOTTOM_REM = 2.0 // 32px
private const val SUBTITLE_MARGIN_BOTTOM_REM = 2.25 // 36px
private const val SUBTITLE_MAX_WIDTH_CH = 44.0
private const val CTA_ROW_GAP_REM = 0.75 // 12px

// Handoff `.hero-a__title .under`: accent-soft background + 8px horizontal
// padding (vertical padding is 0 to keep the handoff's tight cap-height halo).
private const val UNDER_PADDING_X_REM = 0.5 // 8px

// Mono meta eyebrow is a technical literal (file name) and stays untranslated.
private const val META_TEXT = "// signature.kt"

/**
 * Home hero organism, variant `a` from the handoff. Maps to `.hero` + `.hero-a`
 * in `styles.css`: a two-column split layout with editorial copy on the left
 * and the [KotlinCodeSnippet] on the right.
 *
 * The whole section sits inside [ShellStyle] so it shares the 80rem content
 * cap and gutter with the rest of the page. Below [Breakpoint.LG] (900px) the
 * grid collapses to a single column matching the handoff media query.
 *
 * @param modifier optional [Modifier] chained after the base styles.
 * @param onFeaturedWorkClick invoked when the primary CTA is pressed. Defaults
 *   to a no-op so the organism can be previewed without wiring.
 * @param onDownloadCvClick invoked when the secondary CTA is pressed. Defaults
 *   to a no-op for the same reason.
 */
@Composable
fun HeroEditorial(
    modifier: Modifier = Modifier,
    onFeaturedWorkClick: () -> Unit = {},
    onDownloadCvClick: () -> Unit = {},
) {
    val finalModifier = HeroEditorialSectionStyle
        .toModifier()
        .then(modifier)

    Div(attrs = finalModifier.toAttrs()) {
        Div(attrs = ShellStyle.toModifier().toAttrs()) {
            Div(attrs = HeroEditorialGridStyle.toModifier().toAttrs()) {
                HeroCopyColumn(
                    onFeaturedWorkClick = onFeaturedWorkClick,
                    onDownloadCvClick = onDownloadCvClick,
                )
                KotlinCodeSnippet()
            }
        }
    }
}

/**
 * Left column: meta label, display title with a highlighted `engineer` span,
 * body subtitle, and the two CTAs.
 */
@Composable
private fun HeroCopyColumn(onFeaturedWorkClick: () -> Unit, onDownloadCvClick: () -> Unit) {
    val tokens = LocalPortfolioTokens.current
    val strings = LocalStrings.current.hero

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
                .apply(Typography.hero)
                .color(tokens.ink.ink)
                .margin(top = 0.cssRem, bottom = TITLE_MARGIN_BOTTOM_REM.cssRem)
                .toAttrs(),
        ) {
            ComposeText(strings.titleLine1)
            Br()
            Span(
                attrs = HeroEditorialUnderStyle
                    .toModifier()
                    .backgroundColor(tokens.accent.accentSoft)
                    .toAttrs(),
            ) {
                ComposeText(strings.titleLine2)
            }
            Br()
            ComposeText(strings.titleLine3)
        }
        P(
            attrs = HeroEditorialSubtitleStyle
                .toModifier()
                .color(tokens.ink.ink2)
                .toAttrs(),
        ) {
            ComposeText(strings.subtitle)
        }
        Div(attrs = HeroEditorialCtaRowStyle.toModifier().toAttrs()) {
            Button(
                text = strings.ctaPrimary,
                variant = ButtonVariant.Accent,
                onClick = onFeaturedWorkClick,
                trailing = { PhosphorIcon(name = "arrow-right") },
            )
            Button(
                text = strings.ctaSecondary,
                variant = ButtonVariant.Ghost,
                onClick = onDownloadCvClick,
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Styles
// ---------------------------------------------------------------------------

/**
 * Base `CssStyle` for the outer `<section>` container. Ports `.hero` padding
 * from the handoff (top padding is 1.2x the bottom padding to give the copy
 * more breathing room above the fold).
 */
val HeroEditorialSectionStyle = CssStyle {
    base {
        Modifier.padding(
            top = HERO_PADDING_TOP_REM.cssRem,
            bottom = HERO_PADDING_BOTTOM_REM.cssRem,
        )
    }
}

/**
 * Base `CssStyle` for the split grid. Mobile is a single column with a tight
 * 32px gap; from [Breakpoint.LG] (900px) upwards the grid becomes two equal
 * columns separated by 64px, matching `.hero-a` in the handoff CSS.
 */
val HeroEditorialGridStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Grid)
            .gridTemplateColumns { size(HERO_GRID_COL_FR.fr) }
            .gap(HERO_GRID_GAP_MOBILE_REM.cssRem)
            .alignItems(AlignItems.Start)
    }
    Breakpoint.LG {
        Modifier
            .gridTemplateColumns {
                size(HERO_GRID_COL_FR.fr)
                size(HERO_GRID_COL_FR.fr)
            }
            .gap(HERO_GRID_GAP_DESKTOP_REM.cssRem)
    }
}

/**
 * Base `CssStyle` for the highlighted `engineer` span inside the hero title.
 * Ports `.hero-a__title .under`: accent-soft background with 0.5rem horizontal
 * padding and `box-decoration-break: clone` so the highlight wraps cleanly if
 * the word ever breaks across lines. Background colour is applied inline from
 * the active accent swatch.
 */
val HeroEditorialUnderStyle = CssStyle {
    base {
        Modifier
            .padding(topBottom = 0.cssRem, leftRight = UNDER_PADDING_X_REM.cssRem)
            .boxDecorationBreak(BoxDecorationBreak.Clone)
    }
}

/**
 * Base `CssStyle` for the hero subtitle. Ports `.hero-a__subtitle`: 17px
 * `bodyLarge` typography, 44ch max-width, 36px bottom margin. Colour is
 * applied inline from the `ink-2` token.
 */
val HeroEditorialSubtitleStyle = CssStyle {
    base {
        Modifier
            .apply(Typography.bodyLarge)
            .maxWidth(SUBTITLE_MAX_WIDTH_CH.unsafeCast<CSSSizeValue<CSSUnit.ch>>())
            .margin(top = 0.cssRem, bottom = SUBTITLE_MARGIN_BOTTOM_REM.cssRem)
    }
}

/**
 * Base `CssStyle` for the CTA row. Matches the handoff's inline
 * `style={{display:'flex', gap:12}}` on the button pair.
 */
val HeroEditorialCtaRowStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Flex)
            .gap(CTA_ROW_GAP_REM.cssRem)
    }
}
