package dev.tonholo.portfolio.components.molecules

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.ListStyleType
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.borderBottom
import com.varabyte.kobweb.compose.ui.modifiers.borderTop
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.content
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.flexDirection
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.left
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.listStyle
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.selectors.before
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.domain.model.Experience
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.typography.FontFamilies
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Ul
import org.jetbrains.compose.web.dom.Text as ComposeText

// Grid template. Handoff: `180px 60px 1fr 2fr` at 16px root = 11.25rem / 3.75rem / 1fr / 2fr.
private const val GRID_DATE_COL_REM = 11.25
private const val GRID_SPACER_COL_REM = 3.75
private const val GRID_ROLE_COL_FR = 1
private const val GRID_BODY_COL_FR = 2

// Outer row: 32px gap, 28px top/bottom padding, 1px hairline borders.
private const val ROW_GAP_REM = 2.0 // 32px
private const val ROW_PADDING_Y_REM = 1.75 // 28px
private const val ROW_BORDER_WIDTH_REM = 0.0625 // 1px at 1rem=16px

// Left column: date (12px mono, muted) over duration (11px mono, muted-2 prefixed with "· ").
private const val DATE_SIZE_REM = 0.75 // 12px
private const val DURATION_SIZE_REM = 0.6875 // 11px
private const val META_LINE_HEIGHT = 1.4

// Right column: role (18px display medium) + company subtitle (13px, muted).
private const val ROLE_SIZE_REM = 1.125 // 18px
private const val ROLE_LINE_HEIGHT = 1.2
private const val ROLE_LETTER_SPACING_EM = -0.01
private const val COMPANY_SIZE_REM = 0.8125 // 13px
private const val COMPANY_MARGIN_TOP_REM = 0.125 // 2px

// Body copy + bullets.
private const val BODY_SIZE_REM = 0.875 // 14px
private const val BODY_LINE_HEIGHT = 1.6
private const val BODY_MAX_WIDTH_CH = 68.0
private const val BULLETS_MARGIN_TOP_REM = 0.5 // 8px
private const val BULLETS_GAP_REM = 0.375 // 6px
private const val BULLET_PADDING_LEFT_REM = 1.125 // 18px

// Bullet glyph for `::before`. Handoff used an em dash; project rule requires
// regular dash, so we render a hyphen instead.
private const val BULLET_GLYPH = "-"

/**
 * Returns the hairline border color for a timeline row, matching the handoff:
 * light theme uses `--paper-3`; dark theme overrides to `--paper-2`.
 */
@Composable
private fun timelineBorderColor(): Color {
    val tokens = LocalPortfolioTokens.current
    val colorMode = ColorMode.current
    return when (colorMode) {
        ColorMode.LIGHT ->
            tokens.paper.paper3

        ColorMode.DARK ->
            tokens.paper.paper2
    }
}

/**
 * Single row in the Resume page timeline. Maps to `.timeline-row` in the
 * handoff CSS: a four-column grid combining a mono date/duration column,
 * a spacer column, a display-typography role column (with company
 * subtitle), and a body column carrying the description plus an optional
 * bullet list.
 *
 * When [last] is `true` the row also paints a bottom hairline so the
 * terminating entry closes the timeline (matches `.timeline-row.last`).
 *
 * @param experience the [Experience] model providing copy for every slot.
 * @param modifier optional [Modifier] chained after the base styles.
 * @param last set `true` for the final row so it receives the closing border.
 */
@Composable
fun TimelineRow(experience: Experience, modifier: Modifier = Modifier, last: Boolean = false) {
    val tokens = LocalPortfolioTokens.current
    val borderColor = timelineBorderColor()

    var finalModifier = TimelineRowStyle
        .toModifier()
        .borderTop(
            width = ROW_BORDER_WIDTH_REM.cssRem,
            style = LineStyle.Solid,
            color = borderColor,
        )
    if (last) {
        finalModifier = finalModifier.borderBottom(
            width = ROW_BORDER_WIDTH_REM.cssRem,
            style = LineStyle.Solid,
            color = borderColor,
        )
    }
    finalModifier = finalModifier.then(modifier)

    Div(attrs = finalModifier.toAttrs()) {
        // Column 1: date over duration (mono, muted).
        Div(attrs = Modifier.toAttrs()) {
            Span(
                attrs = Modifier
                    .display(DisplayStyle.Block)
                    .fontFamily(*FontFamilies.MONO)
                    .fontSize(DATE_SIZE_REM.cssRem)
                    .lineHeight(META_LINE_HEIGHT)
                    .color(tokens.muted.muted)
                    .toAttrs(),
            ) {
                ComposeText(experience.date)
            }
            Span(
                attrs = Modifier
                    .display(DisplayStyle.Block)
                    .fontFamily(*FontFamilies.MONO)
                    .fontSize(DURATION_SIZE_REM.cssRem)
                    .lineHeight(META_LINE_HEIGHT)
                    .color(tokens.muted.muted2)
                    .toAttrs(),
            ) {
                // Handoff prefixes the duration copy with a middle-dot + space.
                ComposeText("\u00B7 ${experience.duration}")
            }
        }

        // Column 2: empty spacer preserves the grid shape defined in the handoff.
        Div(attrs = Modifier.toAttrs())

        // Column 3: role (display medium) with company subtitle block.
        Div(
            attrs = Modifier
                .fontFamily(*FontFamilies.DISPLAY)
                .fontSize(ROLE_SIZE_REM.cssRem)
                .fontWeight(FontWeight.Medium)
                .lineHeight(ROLE_LINE_HEIGHT)
                .letterSpacing(ROLE_LETTER_SPACING_EM.em)
                .color(tokens.ink.ink)
                .toAttrs(),
        ) {
            ComposeText(experience.role)
            Span(
                attrs = Modifier
                    .display(DisplayStyle.Block)
                    .fontFamily(*FontFamilies.BODY)
                    .fontSize(COMPANY_SIZE_REM.cssRem)
                    .fontWeight(FontWeight.Normal)
                    .margin(top = COMPANY_MARGIN_TOP_REM.cssRem)
                    .color(tokens.muted.muted)
                    .toAttrs(),
            ) {
                ComposeText(experience.company)
            }
        }

        // Column 4: body copy + optional bullet list.
        Div(
            attrs = TimelineBodyStyle
                .toModifier()
                .fontFamily(*FontFamilies.BODY)
                .fontSize(BODY_SIZE_REM.cssRem)
                .lineHeight(BODY_LINE_HEIGHT)
                .maxWidth(BODY_MAX_WIDTH_CH.unsafeCast<CSSSizeValue<CSSUnit.ch>>())
                .color(tokens.ink.ink2)
                .toAttrs(),
        ) {
            ComposeText(experience.body)
            if (experience.bullets.isNotEmpty()) {
                Ul(attrs = TimelineBulletsStyle.toModifier().toAttrs()) {
                    experience.bullets.forEach { bullet ->
                        Li(
                            attrs = TimelineBulletItemStyle
                                .toModifier()
                                .color(tokens.accent.accent)
                                .toAttrs(),
                        ) {
                            // The Li's `::before` inherits the accent color set
                            // above; nested Span restores the body ink color for
                            // the bullet copy itself.
                            Span(attrs = Modifier.color(tokens.ink.ink2).toAttrs()) {
                                ComposeText(bullet)
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Base `CssStyle` for [TimelineRow]. Ports the handoff `.timeline-row`
 * layout: a four-column grid with generous row padding. Border colors are
 * applied inline by the composable so they can react to the active color
 * mode.
 */
val TimelineRowStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Grid)
            .gridTemplateColumns {
                size(GRID_DATE_COL_REM.cssRem)
                size(GRID_SPACER_COL_REM.cssRem)
                size(GRID_ROLE_COL_FR.fr)
                size(GRID_BODY_COL_FR.fr)
            }
            .gap(ROW_GAP_REM.cssRem)
            .padding(topBottom = ROW_PADDING_Y_REM.cssRem)
            .alignItems(AlignItems.Start)
    }
}

/**
 * Base `CssStyle` for the `.timeline-body` slot. Kept empty; typography and
 * max-width land inline via the composable so they can consume tokens.
 */
val TimelineBodyStyle = CssStyle {}

/**
 * `<ul>` container for the bullet list inside `.timeline-body`. Matches
 * the handoff: no marker, vertical flex stack, 8px top margin, 6px gap
 * between items, zero padding so alignment is owned by the items.
 */
val TimelineBulletsStyle = CssStyle {
    base {
        Modifier
            .listStyle(type = ListStyleType.None)
            .display(DisplayStyle.Flex)
            .flexDirection(FlexDirection.Column)
            .gap(BULLETS_GAP_REM.cssRem)
            .margin(top = BULLETS_MARGIN_TOP_REM.cssRem)
            .padding(left = 0.cssRem)
    }
}

/**
 * `<li>` style for timeline bullets. Reproduces `.timeline-body ul li` and
 * its `::before` em-dash marker: 18px left padding makes room for the
 * absolutely-positioned marker column; the marker inherits the accent
 * color set inline on the item element so it stays theme-aware.
 */
val TimelineBulletItemStyle = CssStyle {
    base {
        Modifier
            .position(Position.Relative)
            .padding(left = BULLET_PADDING_LEFT_REM.cssRem)
    }

    before {
        Modifier
            .content(BULLET_GLYPH)
            .position(Position.Absolute)
            .left(0.cssRem)
            .fontWeight(FontWeight.SemiBold)
    }
}
