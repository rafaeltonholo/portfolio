package dev.tonholo.portfolio.components.organisms.about

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontStyle
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontStyle
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.graphics.ImageDecoding
import com.varabyte.kobweb.silk.components.graphics.ImageFetchPriority
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.components.atoms.MetaLabel
import dev.tonholo.portfolio.components.atoms.MetaLabelVariant
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.layout.unit.dp
import dev.tonholo.portfolio.theme.typography.FontFamilies
import dev.tonholo.portfolio.theme.typography.Typography
import dev.tonholo.portfolio.theme.typography.apply
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.ch
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
private val GRID_GAP = 64.dp
private const val GRID_WIDE_FR = 1.2
private const val GRID_NARROW_FR = 1.0
private const val GRID_COL_FR = 1.0

// Handoff inline styles on the copy column.
private val META_MARGIN_BOTTOM = 20.dp
private val TITLE_MARGIN_BOTTOM = 28.dp

// Handoff lede paragraph: body sans (Geist), 20px size, 1.55 line-height,
// 48ch cap, `ink-2` color. The earlier build used `FontFamilies.SERIF` which
// drifted from the handoff's Geist intro treatment.
private val LEDE_SIZE = 20.dp
private const val LEDE_LINE_HEIGHT = 1.55
private const val LEDE_MAX_WIDTH_CH = 48.0

// Mono eyebrow label is a technical literal (file path) and stays in English.
private const val META_TEXT = "// about.me"

// Portrait asset matches the old layout's About image; served from
// site/src/jsMain/resources/public/images so Kobweb exposes it at /images/writing.jpg.
private const val PORTRAIT_SRC = "/images/writing.jpg"

// 4:5 ratio reserved for the portrait slot (width:height).
private const val PORTRAIT_ASPECT_W = 4
private const val PORTRAIT_ASPECT_H = 5

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
        AboutHeaderPortrait()
    }
}

/**
 * Right column: editorial portrait constrained to the 4:5 frame that the
 * handoff reserved for the about placeholder. The outer `Div` holds the
 * aspect ratio so the grid column keeps a predictable height, while the
 * inner `Image` fills the frame with `object-fit: cover` to avoid
 * distortion across breakpoints.
 */
@Composable
private fun AboutHeaderPortrait() {
    val strings = LocalStrings.current.about

    Div(
        attrs = AboutHeaderPortraitFrameStyle
            .toModifier()
            .aspectRatio(width = PORTRAIT_ASPECT_W, height = PORTRAIT_ASPECT_H)
            .toAttrs(),
    ) {
        Image(
            src = PORTRAIT_SRC,
            description = strings.portraitAlt,
            modifier = AboutHeaderPortraitImageStyle.toModifier(),
            decoding = ImageDecoding.Async,
            fetchPriority = ImageFetchPriority.High,
        )
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
    val strings = LocalStrings.current.about

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
                .apply(Typography.h1)
                .color(tokens.ink.ink)
                .margin(top = 0.cssRem, bottom = TITLE_MARGIN_BOTTOM)
                .toAttrs(),
        ) {
            ComposeText(strings.titlePrefix)
            Em(
                attrs = AboutHeaderTitleEmphasisStyle
                    .toModifier()
                    .color(tokens.accent.accent)
                    .toAttrs(),
            ) {
                ComposeText(strings.titleEmphasis)
            }
        }
        P(
            attrs = AboutHeaderLedeStyle
                .toModifier()
                .color(tokens.ink.ink2)
                .toAttrs(),
        ) {
            ComposeText(strings.lede)
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
            .gap(GRID_GAP)
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
 * Base `CssStyle` for the `<em>` inside the `h1`. Ports the handoff rule
 * `.about-hdr h1 em { font-style: normal; color: var(--accent); }`: keep the
 * inherited mono display family, override the browser default italic, and
 * let the composable paint the accent color inline so it follows the theme.
 */
val AboutHeaderTitleEmphasisStyle = CssStyle {
    base {
        Modifier.fontStyle(FontStyle.Normal)
    }
}

/**
 * Outer wrapper around the portrait image. Keeps the 4:5 frame and hides any
 * pixel overflow from the covered image so the grid column holds its shape.
 */
val AboutHeaderPortraitFrameStyle = CssStyle {
    base {
        Modifier
            .fillMaxWidth()
            .display(DisplayStyle.Block)
    }
}

/**
 * Image inside [AboutHeaderPortraitFrameStyle]: fills the 4:5 frame and uses
 * `object-fit: cover` so the photo crops instead of stretching when the
 * column width changes.
 */
val AboutHeaderPortraitImageStyle = CssStyle {
    base {
        Modifier
            .fillMaxSize()
            .objectFit(ObjectFit.Cover)
    }
}

/**
 * Base `CssStyle` for the lede paragraph. Ports the inline styles on the
 * handoff `<p>`: body sans (Geist) family, 20px size, 1.55 line-height, 48ch
 * cap. Color is applied inline by the composable from the `ink-2` token.
 */
val AboutHeaderLedeStyle = CssStyle {
    base {
        Modifier
            .fontFamily(*FontFamilies.BODY)
            .fontSize(LEDE_SIZE)
            .lineHeight(LEDE_LINE_HEIGHT)
            .margin(topBottom = 0.cssRem)
            .maxWidth(LEDE_MAX_WIDTH_CH.ch)
    }
}
