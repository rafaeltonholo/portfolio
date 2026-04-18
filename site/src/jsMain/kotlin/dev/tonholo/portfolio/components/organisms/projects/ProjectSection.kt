package dev.tonholo.portfolio.components.organisms.projects

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.TextTransform
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.alignSelf
import com.varabyte.kobweb.compose.ui.modifiers.borderTop
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.textTransform
import com.varabyte.kobweb.compose.ui.modifiers.top
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.PortfolioTokens
import dev.tonholo.portfolio.theme.typography.FontFamilies
import dev.tonholo.portfolio.theme.typography.Typography
import dev.tonholo.portfolio.theme.typography.apply
import org.jetbrains.compose.web.css.AlignSelf
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Text as ComposeText

// Handoff `.project-section`: two-column grid with a 220px left column and a
// fluid right column (`1fr`), 64px gap, 48px top/bottom padding, and a 1px
// `--paper-3` (light) / `--paper-2` (dark) top border. Below 900px
// (`Breakpoint.LG`) collapses to a single column with a 16px gap.
private const val SECTION_LABEL_COL_REM = 13.75 // 220px at 1rem=16px
private const val SECTION_CONTENT_COL_FR = 1.0
private const val SECTION_GAP_DESKTOP_REM = 4.0 // 64px
private const val SECTION_GAP_MOBILE_REM = 1.0 // 16px
private const val SECTION_PADDING_Y_REM = 3.0 // 48px
private const val SECTION_BORDER_WIDTH_REM = 0.0625 // 1px

// Handoff `.project-section .lbl`: mono 11px uppercase with 0.08em tracking,
// muted color, sticky with `top: 100px` so it trails the nav chrome while the
// content scrolls past.
private const val LABEL_FONT_SIZE_REM = 0.6875 // 11px
private const val LABEL_LETTER_SPACING_EM = 0.08
private const val LABEL_STICKY_TOP_REM = 6.25 // 100px

// Handoff `.project-section .content`: serif family at 19px / 1.6, `--ink-2`
// color, capped at 60ch per the handoff rule. The spec asks for a 48ch cap on
// the paragraph body inside the slot; the outer wrapper still matches the
// handoff's 60ch to keep headings aligned with neighbouring sections.
private const val CONTENT_BODY_SIZE_REM = 1.1875 // 19px
private const val CONTENT_BODY_LINE_HEIGHT = 1.6
private const val CONTENT_MAX_WIDTH_CH = 60.0

// Handoff `.project-section .content h2`: 36px display, weight 500, -0.02em
// tracking, 1.05 line-height, 24px bottom margin.
private const val CONTENT_H2_SIZE_REM = 2.25 // 36px
private const val CONTENT_H2_LINE_HEIGHT = 1.05
private const val CONTENT_H2_LETTER_SPACING_EM = -0.02
private const val CONTENT_H2_MARGIN_BOTTOM_REM = 1.5 // 24px

// Spec: paragraph body (content slot) caps at 48ch; the wrapper owns the 60ch
// ceiling so a caller that renders non-paragraph content (e.g., the outcomes
// grid) can still span the full column.
private const val CONTENT_PARAGRAPH_MAX_WIDTH_CH = 48.0

/**
 * Reusable two-column section used across the project detail page. Maps to
 * `.project-section` in the handoff CSS (`styles.css` lines 1219-1251): a
 * `220px 1fr` grid with a mono uppercase `.lbl` sticky in the left column and
 * a display `h2` + content slot on the right.
 *
 * Content paragraphs authored through the slot should sit inside
 * [ProjectSectionParagraphStyle] so they inherit the 48ch max-width cap per
 * the Phase 2 redesign spec; the outer `.content` column keeps the handoff's
 * 60ch ceiling so non-paragraph content (for instance the [OutcomesGrid]
 * slotted inside the `/ outcomes` section) can stretch to the column edge.
 *
 * Below [Breakpoint.LG] the grid collapses to a single column with a 16px gap,
 * matching the `@media (max-width: 900px)` rule in the handoff.
 *
 * @param label the mono marker copy, e.g. `"/ context"`.
 * @param title the `h2` display heading, e.g.
 *   `"Pinterest is 9 years of code, and it shows."`.
 * @param modifier optional [Modifier] chained after the base styles.
 * @param content composable slot for the section body. Typical usage renders
 *   one or more [ProjectSectionParagraph] composables, optionally interleaved
 *   with other organisms such as [OutcomesGrid].
 */
@Composable
fun ProjectSection(
    label: String,
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val tokens = LocalPortfolioTokens.current
    val borderColor = projectSectionBorderColor(tokens = tokens)

    val finalModifier = ProjectSectionStyle
        .toModifier()
        .borderTop(
            width = SECTION_BORDER_WIDTH_REM.cssRem,
            style = LineStyle.Solid,
            color = borderColor,
        )
        .then(modifier)

    Div(attrs = finalModifier.toAttrs()) {
        Div(
            attrs = ProjectSectionLabelStyle
                .toModifier()
                .color(tokens.muted.muted)
                .toAttrs(),
        ) {
            ComposeText(label)
        }
        Div(
            attrs = ProjectSectionContentStyle
                .toModifier()
                .color(tokens.ink.ink2)
                .toAttrs(),
        ) {
            H2(
                attrs = ProjectSectionTitleStyle
                    .toModifier()
                    .color(tokens.ink.ink)
                    .toAttrs(),
            ) {
                ComposeText(title)
            }
            content()
        }
    }
}

/**
 * Resolves the top-border color shared by `.project-section`. Light theme
 * uses `paper-3`, dark theme uses `paper-2` to match the
 * `[data-theme="dark"] .project-section` rule.
 */
@Composable
private fun projectSectionBorderColor(tokens: PortfolioTokens): Color {
    val colorMode = ColorMode.current
    return when (colorMode) {
        ColorMode.LIGHT -> tokens.paper.paper3
        ColorMode.DARK -> tokens.paper.paper2
    }
}

// ---------------------------------------------------------------------------
// Styles
// ---------------------------------------------------------------------------

/**
 * Base `CssStyle` for the outer section grid. Ports `.project-section` from
 * the handoff: single column with a 16px gap below [Breakpoint.LG]; from `LG`
 * up switches to a `220px 1fr` grid with a 64px gap and 48px top/bottom
 * padding. The top border is applied inline by the composable so it can flip
 * per theme.
 */
val ProjectSectionStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Grid)
            .gridTemplateColumns { size(SECTION_CONTENT_COL_FR.fr) }
            .gap(SECTION_GAP_MOBILE_REM.cssRem)
            .padding(topBottom = SECTION_PADDING_Y_REM.cssRem)
    }
    Breakpoint.LG {
        Modifier
            .gridTemplateColumns {
                size(SECTION_LABEL_COL_REM.cssRem)
                size(SECTION_CONTENT_COL_FR.fr)
            }
            .gap(SECTION_GAP_DESKTOP_REM.cssRem)
    }
}

/**
 * Base `CssStyle` for `.project-section .lbl`. Mono 11px uppercase with
 * +0.08em tracking; sticky with `top: 100px` so the label trails the nav
 * chrome while the content column scrolls. Color is applied inline by the
 * composable so it tracks the active theme.
 */
val ProjectSectionLabelStyle = CssStyle {
    base {
        Modifier
            .fontFamily(*FontFamilies.MONO)
            .fontSize(LABEL_FONT_SIZE_REM.cssRem)
            .letterSpacing(LABEL_LETTER_SPACING_EM.em)
            .textTransform(TextTransform.Uppercase)
            .position(Position.Sticky)
            .top(LABEL_STICKY_TOP_REM.cssRem)
            .alignSelf(AlignSelf.Start)
    }
}

/**
 * Base `CssStyle` for `.project-section .content`. Serif 19px / 1.6 body,
 * capped at 60ch. Color is applied inline by the composable.
 */
val ProjectSectionContentStyle = CssStyle {
    base {
        Modifier
            .fontFamily(*FontFamilies.SERIF)
            .fontSize(CONTENT_BODY_SIZE_REM.cssRem)
            .lineHeight(CONTENT_BODY_LINE_HEIGHT)
            .maxWidth(CONTENT_MAX_WIDTH_CH.unsafeCast<CSSSizeValue<CSSUnit.ch>>())
    }
}

/**
 * Base `CssStyle` for the section `<h2>`. Ports the handoff `.content h2`:
 * 36px display with tight tracking, near-unity line height, 24px bottom
 * margin, no top margin so the border-top padding sets the rhythm. Color is
 * applied inline by the composable.
 */
val ProjectSectionTitleStyle = CssStyle {
    base {
        Modifier
            .apply(Typography.h2)
            .fontSize(CONTENT_H2_SIZE_REM.cssRem)
            .lineHeight(CONTENT_H2_LINE_HEIGHT)
            .letterSpacing(CONTENT_H2_LETTER_SPACING_EM.em)
            .margin(top = 0.cssRem, bottom = CONTENT_H2_MARGIN_BOTTOM_REM.cssRem)
    }
}

/**
 * Shared paragraph style for copy rendered inside [ProjectSection]'s content
 * slot. Caps the paragraph body at 48ch (per the Phase 2 spec) while the
 * outer content column keeps the handoff's 60ch ceiling for non-paragraph
 * content. Inherits serif typography from [ProjectSectionContentStyle] so
 * callers just need to chain the style onto their `<p>`.
 */
val ProjectSectionParagraphStyle = CssStyle {
    base {
        Modifier
            .margin(topBottom = 0.cssRem)
            .maxWidth(CONTENT_PARAGRAPH_MAX_WIDTH_CH.unsafeCast<CSSSizeValue<CSSUnit.ch>>())
    }
}
