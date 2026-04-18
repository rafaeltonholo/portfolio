package dev.tonholo.portfolio.components.organisms.resume

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.ListStyleType
import com.varabyte.kobweb.compose.css.TextTransform
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.borderTop
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.flexDirection
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.listStyle
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textTransform
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.components.molecules.TagList
import dev.tonholo.portfolio.domain.model.Education
import dev.tonholo.portfolio.domain.model.SkillBucket
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.typography.FontFamilies
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H4
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Ul
import org.jetbrains.compose.web.dom.Text as ComposeText

// Heading copy per handoff `pages2.jsx` lines 51, 57, 63.
private const val PRIMARY_STACK_HEADING = "// primary stack"
private const val ALSO_COMFORTABLE_HEADING = "// also comfortable"
private const val EDUCATION_HEADING = "// education"

// `.resume-skills` container: 3-column grid, 48px gap, 80px top margin, 48px
// top padding, 1px top hairline border. Below `@media (max-width: 820px)`
// collapses to a single column with a 32px gap; we use `Breakpoint.LG` to
// match the rest of the redesign.
private const val GRID_GAP_DESKTOP_REM = 3.0 // 48px
private const val GRID_GAP_MOBILE_REM = 2.0 // 32px
private const val GRID_MARGIN_TOP_REM = 5.0 // 80px
private const val GRID_PADDING_TOP_REM = 3.0 // 48px
private const val GRID_BORDER_WIDTH_REM = 0.0625 // 1px at 1rem=16px
private const val GRID_COLUMN_FR = 1

// `.skills-col h4`: mono, 11px / 600, uppercase with +0.08em tracking, ink-3,
// 16px bottom margin.
private const val HEADING_FONT_SIZE_REM = 0.6875 // 11px
private const val HEADING_LETTER_SPACING_EM = 0.08
private const val HEADING_MARGIN_BOTTOM_REM = 1.0 // 16px

// Education `<ul>` inline styles on `pages2.jsx` line 64: list-style none,
// flex column, 12px gap.
private const val EDUCATION_LIST_GAP_REM = 0.75 // 12px

// Education entry typography per `pages2.jsx` lines 66-67. Titles use the
// display family at 15px / 500 / ink. Subtitles use the mono family at 11px /
// muted.
private const val EDUCATION_TITLE_FONT_SIZE_REM = 0.9375 // 15px
private const val EDUCATION_TITLE_MARGIN_TOP_REM = 0.125 // 2px breathing room
private const val EDUCATION_SUBTITLE_FONT_SIZE_REM = 0.6875 // 11px
private const val EDUCATION_SUBTITLE_LINE_HEIGHT = 1.4

/**
 * Returns the hairline border color for the `.resume-skills` top rule. Light
 * theme uses `--paper-3`; dark theme overrides to `--paper-2`, matching
 * `styles.css` lines 1167-1169.
 */
@Composable
private fun skillsTopBorderColor(): Color {
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
 * Three-column grid that closes the Resume page. Ports the `.resume-skills`
 * block from `pages2.jsx` (lines 49-79) and `styles.css` (lines 1161-1175):
 * primary stack chips on the left, "also comfortable" chips in the middle,
 * and a stacked education list on the right. Below [Breakpoint.LG] the grid
 * collapses to a single column to match the handoff `@media (max-width:
 * 820px)` rule.
 *
 * @param primaryStack the primary-stack [SkillBucket] rendered in the first
 *   column.
 * @param alsoComfortable the secondary-stack [SkillBucket] rendered in the
 *   middle column.
 * @param education list of [Education] entries rendered in the third column
 *   as display-font titles over mono muted subtitles.
 * @param modifier optional [Modifier] chained after the base styles.
 */
@Composable
fun SkillsGrid(
    primaryStack: SkillBucket,
    alsoComfortable: SkillBucket,
    education: List<Education>,
    modifier: Modifier = Modifier,
) {
    val borderColor = skillsTopBorderColor()

    val finalModifier = SkillsGridStyle
        .toModifier()
        .borderTop(
            width = GRID_BORDER_WIDTH_REM.cssRem,
            style = LineStyle.Solid,
            color = borderColor,
        )
        .then(modifier)

    Div(attrs = finalModifier.toAttrs()) {
        SkillsColumn(heading = PRIMARY_STACK_HEADING) {
            TagList(tags = primaryStack.skills)
        }
        SkillsColumn(heading = ALSO_COMFORTABLE_HEADING) {
            TagList(tags = alsoComfortable.skills)
        }
        SkillsColumn(heading = EDUCATION_HEADING) {
            EducationList(entries = education)
        }
    }
}

/**
 * Single `.skills-col` cell. Renders the mono `h4` heading followed by the
 * caller-supplied body slot. Keeping the heading in one place enforces the
 * handoff's uppercase + tracking styling across all three columns.
 */
@Composable
private fun SkillsColumn(heading: String, content: @Composable () -> Unit) {
    val tokens = LocalPortfolioTokens.current

    Div(attrs = Modifier.toAttrs()) {
        H4(
            attrs = SkillsColumnHeadingStyle
                .toModifier()
                .color(tokens.ink.ink3)
                .toAttrs(),
        ) {
            ComposeText(heading)
        }
        content()
    }
}

/**
 * Stacked list of [Education] entries. Ports the inline `<ul>` in
 * `pages2.jsx` lines 64-77: list-style none, vertical flex stack with a 12px
 * gap between entries. Each entry renders a display-font title (degree) over
 * a mono muted subtitle combining the institution and year with a middle dot
 * separator.
 */
@Composable
private fun EducationList(entries: List<Education>) {
    val tokens = LocalPortfolioTokens.current

    Ul(attrs = SkillsEducationListStyle.toModifier().toAttrs()) {
        entries.forEach { entry ->
            Li(attrs = Modifier.toAttrs()) {
                Div(
                    attrs = SkillsEducationTitleStyle
                        .toModifier()
                        .color(tokens.ink.ink)
                        .toAttrs(),
                ) {
                    ComposeText(entry.degree)
                }
                Div(
                    attrs = SkillsEducationSubtitleStyle
                        .toModifier()
                        .color(tokens.muted.muted)
                        .toAttrs(),
                ) {
                    ComposeText("${entry.institution} \u00B7 ${entry.year}")
                }
            }
        }
    }
}

/**
 * Base `CssStyle` for [SkillsGrid]. Ports `.resume-skills` from the handoff:
 * a single-column grid on mobile with a 32px gap, promoted to three equal
 * columns with a 48px gap at [Breakpoint.LG] and above. The top margin, top
 * padding, and hairline top border terminate the timeline above and separate
 * the skills block. Border colour is applied inline by the composable so it
 * can react to the active color mode.
 */
val SkillsGridStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Grid)
            .gridTemplateColumns { size(GRID_COLUMN_FR.fr) }
            .gap(GRID_GAP_MOBILE_REM.cssRem)
            .margin(top = GRID_MARGIN_TOP_REM.cssRem)
            .padding(top = GRID_PADDING_TOP_REM.cssRem)
    }
    Breakpoint.LG {
        Modifier
            .gridTemplateColumns { repeat(count = 3) { size(GRID_COLUMN_FR.fr) } }
            .gap(GRID_GAP_DESKTOP_REM.cssRem)
    }
}

/**
 * Base `CssStyle` for the `.skills-col h4` heading. Ports `styles.css` lines
 * 1171-1174: mono family at 11px / 600, uppercase with +0.08em tracking, 16px
 * bottom margin. Colour is applied inline from the `ink-3` token.
 */
val SkillsColumnHeadingStyle = CssStyle {
    base {
        Modifier
            .fontFamily(FontFamilies.MONO)
            .fontSize(HEADING_FONT_SIZE_REM.cssRem)
            .fontWeight(FontWeight.SemiBold)
            .letterSpacing(HEADING_LETTER_SPACING_EM.em)
            .textTransform(TextTransform.Uppercase)
            .margin(top = 0.cssRem, bottom = HEADING_MARGIN_BOTTOM_REM.cssRem)
    }
}

/**
 * Base `CssStyle` for the education `<ul>` container. Ports the inline style
 * on `pages2.jsx` line 64: strips default list markers, vertical flex stack,
 * 12px gap between entries, zero padding so titles align with the column
 * heading.
 */
val SkillsEducationListStyle = CssStyle {
    base {
        Modifier
            .listStyle(type = ListStyleType.None)
            .display(DisplayStyle.Flex)
            .flexDirection(FlexDirection.Column)
            .gap(EDUCATION_LIST_GAP_REM.cssRem)
            .padding(0.cssRem)
            .margin(topBottom = 0.cssRem)
    }
}

/**
 * Base `CssStyle` for the education entry title. Ports the inline style on
 * `pages2.jsx` line 66: display family at 15px / 500. Colour is applied
 * inline from the `ink` token.
 */
val SkillsEducationTitleStyle = CssStyle {
    base {
        Modifier
            .fontFamily(FontFamilies.DISPLAY)
            .fontSize(EDUCATION_TITLE_FONT_SIZE_REM.cssRem)
            .fontWeight(FontWeight.Medium)
            .margin(top = EDUCATION_TITLE_MARGIN_TOP_REM.cssRem, bottom = 0.cssRem)
    }
}

/**
 * Base `CssStyle` for the education entry subtitle. Ports the inline style on
 * `pages2.jsx` line 67: mono family at 11px. Colour is applied inline from
 * the `muted` token.
 */
val SkillsEducationSubtitleStyle = CssStyle {
    base {
        Modifier
            .fontFamily(FontFamilies.MONO)
            .fontSize(EDUCATION_SUBTITLE_FONT_SIZE_REM.cssRem)
            .fontWeight(FontWeight.Normal)
            .lineHeight(EDUCATION_SUBTITLE_LINE_HEIGHT)
            .margin(topBottom = 0.cssRem)
    }
}
