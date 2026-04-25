package dev.tonholo.portfolio.components.organisms.home

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.BoxDecorationBreak
import com.varabyte.kobweb.compose.css.OverflowWrap
import com.varabyte.kobweb.compose.css.functions.min
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
import com.varabyte.kobweb.compose.ui.modifiers.overflowWrap
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
import dev.tonholo.portfolio.components.layouts.HeroSection
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.layout.unit.dp
import dev.tonholo.portfolio.theme.typography.Typography
import dev.tonholo.portfolio.theme.typography.apply
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.dom.Br
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text as ComposeText

// Handoff `.hero-a`: two-column grid, 64px gap. Tracks self-collapse to one
// column when the container can no longer fit two of [HERO_GRID_MIN_COL_WIDTH]
// side-by-side; the gap shrinks to 32px below [Breakpoint.LG] so stacked
// content keeps a tighter rhythm on narrow phones.
private val HERO_GRID_GAP_DESKTOP = 64.dp
private val HERO_GRID_GAP_MOBILE = 32.dp

// Minimum width before each grid track is allowed; below this the `auto-fit`
// grid collapses to a single column. Treat this as a layout contract for the
// hero ("don't render the side-by-side split until each side has substantial
// editorial breathing room"), not a content-coupled measurement; the
// `min(100%, ...)` wrapper at the call site keeps narrow mobile containers
// from overflowing this floor.
private val HERO_GRID_MIN_COL_WIDTH = 32.cssRem

// Handoff inline styles on the meta label row and the CTA row.
private val META_MARGIN_BOTTOM = 20.dp
private val TITLE_MARGIN_BOTTOM = 32.dp
private val SUBTITLE_MARGIN_BOTTOM = 36.dp
private const val SUBTITLE_MAX_WIDTH_CH = 44.0
private val CTA_ROW_GAP = 12.dp

// Handoff `.hero-a__title .under`: accent-soft background + 8px horizontal
// padding (vertical padding is 0 to keep the handoff's tight cap-height halo).
private val UNDER_PADDING_X = 8.dp

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
 * @param onCurrentlyBuildingClick invoked when the "Currently Building" CTA is pressed.
 * @param onWritingClick invoked when the "Writing" CTA is pressed.
 * @param onHireClick invoked when the "Hire Me" CTA is pressed.
 */
@Composable
fun HeroEditorial(
    modifier: Modifier = Modifier,
    onFeaturedWorkClick: () -> Unit = {},
    onDownloadCvClick: () -> Unit = {},
    onCurrentlyBuildingClick: () -> Unit = {},
    onWritingClick: () -> Unit = {},
    onHireClick: () -> Unit = {},
) {
    HeroSection(id = "hero", modifier = modifier) {
        Div(attrs = HeroEditorialGridStyle.toModifier().toAttrs()) {
            HeroCopyColumn(
                onFeaturedWorkClick = onFeaturedWorkClick,
                onDownloadCvClick = onDownloadCvClick,
            )
            KotlinCodeSnippet(
                onCurrentlyBuildingClick = onCurrentlyBuildingClick,
                onWritingClick = onWritingClick,
                onHireClick = onHireClick,
            )
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
                .margin(bottom = META_MARGIN_BOTTOM),
        )
        H1(
            attrs = Modifier
                .apply(Typography.hero)
                .color(tokens.ink.ink)
                .overflowWrap(OverflowWrap.BreakWord)
                .margin(top = 0.cssRem, bottom = TITLE_MARGIN_BOTTOM)
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
 * Base `CssStyle` for the split grid. Uses `repeat(auto-fit, minmax(...))` so
 * the layout itself decides when there's room for two columns based on its
 * container width, instead of relying on a viewport breakpoint that drifts as
 * copy or font-size changes. The handoff layout - editorial title on the
 * left, code snippet on the right - falls out of the grid math: at any
 * container wide enough for two `HERO_GRID_MIN_COL_WIDTH` tracks plus the
 * gap, two columns appear; below that, the snippet stacks below the title.
 * The gap still uses [Breakpoint.LG] so stacked rows on phones stay tighter
 * than the desktop column rhythm.
 */
val HeroEditorialGridStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Grid)
            .gridTemplateColumns {
                repeat(autoFit) {
                    minmax(min(100.percent, HERO_GRID_MIN_COL_WIDTH), 1.fr)
                }
            }
            .gap(HERO_GRID_GAP_MOBILE)
            .alignItems(AlignItems.Start)
    }
    Breakpoint.LG {
        Modifier.gap(HERO_GRID_GAP_DESKTOP)
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
            .padding(topBottom = 0.cssRem, leftRight = UNDER_PADDING_X)
            .boxDecorationBreak(BoxDecorationBreak.Clone)
            .overflowWrap(OverflowWrap.Anywhere)
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
            .margin(top = 0.cssRem, bottom = SUBTITLE_MARGIN_BOTTOM)
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
            .gap(CTA_ROW_GAP)
    }
}
