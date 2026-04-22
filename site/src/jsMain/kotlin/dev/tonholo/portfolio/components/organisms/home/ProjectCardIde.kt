package dev.tonholo.portfolio.components.organisms.home

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextTransform
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.borderTop
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.flexWrap
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.justifyContent
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textTransform
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.translateY
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.navigation.UncoloredLinkVariant
import com.varabyte.kobweb.silk.components.navigation.UndecoratedLinkVariant
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.selectors.hover
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.components.atoms.ArrowStyle
import dev.tonholo.portfolio.components.atoms.MetaSeparator
import dev.tonholo.portfolio.components.atoms.PhosphorIcon
import dev.tonholo.portfolio.components.molecules.StatBlock
import dev.tonholo.portfolio.components.molecules.StatSize
import dev.tonholo.portfolio.domain.model.Project
import dev.tonholo.portfolio.domain.model.ProjectKind
import dev.tonholo.portfolio.extensions.toKebabCase
import dev.tonholo.portfolio.navigation.Routes
import dev.tonholo.portfolio.state.LocalLocaleState
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.color.Paper
import dev.tonholo.portfolio.theme.layout.unit.dp
import dev.tonholo.portfolio.theme.typography.FontFamilies
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexWrap
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text as ComposeText

// Head spec: `.proj-head { padding: 14px 24px; background: var(--paper-2); ... }`.
private val HEAD_PADDING_Y = 14.dp
private val HEAD_PADDING_X = 24.dp
private val HEAD_GAP = 12.dp
private val HEAD_FONT_SIZE = 12.dp

// Tag spec: `.proj-head .tag { padding: 3px 8px; border-radius: 2px; font-size: 10px; }`.
private val TAG_PADDING_Y = 3.dp
private val TAG_PADDING_X = 8.dp
private val TAG_BORDER_RADIUS = 2.dp
private val TAG_FONT_SIZE = 10.dp

// Body spec: `.proj-body { padding: 32px 24px; }`.
private val BODY_PADDING_Y = 32.dp
private val BODY_PADDING_X = 24.dp

// Title spec: `.proj-title { font-size: 32px; font-weight: 500; letter-spacing: -0.02em; margin-bottom: 10px; }`.
private val TITLE_FONT_SIZE = 32.dp
private const val TITLE_LETTER_SPACING_EM = -0.02
private val TITLE_MARGIN_BOTTOM = 10.dp

// Description spec: `.proj-desc { font-size: 14px; line-height: 1.55; max-width: 48ch; margin-bottom: 24px; }`.
private val DESC_FONT_SIZE = 14.dp
private const val DESC_LINE_HEIGHT = 1.55
private const val DESC_MAX_WIDTH_CH = 48.0
private val DESC_MARGIN_BOTTOM = 24.dp

// Stats grid spec: `.proj-stats { display: flex; gap: 32px; margin-bottom: 20px; }`.
// We ship a 3-column grid per the task spec to keep the stat cells aligned.
private val STATS_GAP = 32.dp
private val STATS_MARGIN_BOTTOM = 20.dp
private const val STATS_COLUMNS_FR = 1
private const val STATS_COLUMN_COUNT = 3

// Foot spec: `.proj-foot { padding-top: 20px; border-top: 1px solid var(--paper-3); font-size: 12px; }`.
private val FOOT_PADDING_TOP = 20.dp
private val FOOT_BORDER_WIDTH = 1.dp
private val FOOT_FONT_SIZE = 12.dp

// Hover motion spec: `.proj-card:hover .arrow { transform: translateX(4px); }` is owned by the
// `Arrow` atom; the card additionally lifts by -2px per the task spec.
private const val CARD_HOVER_LIFT_PX = -2
private val CARD_HOVER_LIFT = CARD_HOVER_LIFT_PX.dp
private const val CARD_TRANSITION_DURATION_S = 0.2

private const val KIND_COMMERCIAL_LABEL = "COMMERCIAL"
private const val KIND_OPEN_SOURCE_LABEL = "OPEN SOURCE"
private const val FILENAME_PREFIX = "- "
private const val FILENAME_EXTENSION = ".kt"
private const val CTA_LABEL = "read case study"

private const val ARROW_ICON_NAME = "arrow-right"
private val ARROW_GAP = 6.dp

// Inline gap between stack tokens and the centered separator dots in the
// card footer.
private val STACK_ROW_GAP = 6.dp

/**
 * Home "Selected work" card rendered in the IDE-block style. Ports the
 * `ProjectCard` JSX branch keyed on `style === 'ide'` (handoff `home.jsx`
 * lines 100-125): a paper card with a paper-2 filename header (kind tag,
 * `project.kt`-style filename, year) followed by a body containing a display
 * title, a body paragraph, a 3-column stats grid, and a footer that pairs a
 * mono stack line with a mono `read case study ->` CTA.
 *
 * The whole card is a real `<a href>` anchor via Kobweb's [Link] pointing at
 * the project's detail page, so middle-click / cmd-click / copy-link-address
 * work the way a browser user expects.
 *
 * @param project the [Project] to render.
 * @param modifier optional [Modifier] chained after the base styles.
 */
@Composable
fun ProjectCardIde(project: Project, modifier: Modifier = Modifier) {
    val tokens = LocalPortfolioTokens.current
    val locale = LocalLocaleState.current.current

    val cardModifier = ProjectCardIdeStyle
        .toModifier()
        .backgroundColor(tokens.paper.paper)
        .then(modifier)

    Link(
        path = Routes.project(slug = project.slug, locale = locale),
        modifier = cardModifier,
        variant = UndecoratedLinkVariant then UncoloredLinkVariant,
    ) {
        ProjectCardHead(project = project)
        ProjectCardBody(project = project)
    }
}

@Composable
private fun ProjectCardHead(project: Project) {
    val tokens = LocalPortfolioTokens.current
    val kindLabel = when (project.kind) {
        ProjectKind.Commercial -> KIND_COMMERCIAL_LABEL
        ProjectKind.OpenSource -> KIND_OPEN_SOURCE_LABEL
    }
    val filename = "${FILENAME_PREFIX}${project.name.toKebabCase()}${FILENAME_EXTENSION}"

    Div(
        attrs = ProjectCardHeadStyle
            .toModifier()
            .backgroundColor(tokens.paper.paper2)
            .color(tokens.muted.muted)
            .toAttrs(),
    ) {
        Span(
            attrs = ProjectCardTagStyle
                .toModifier()
                .backgroundColor(tokens.accent.accentSoft)
                .color(tokens.accent.accentInk)
                .toAttrs(),
        ) {
            ComposeText(kindLabel)
        }
        Span { ComposeText(filename) }
        Span { ComposeText(project.year) }
    }
}

@Composable
private fun ProjectCardBody(project: Project) {
    val tokens = LocalPortfolioTokens.current

    Div(attrs = ProjectCardBodyStyle.toModifier().toAttrs()) {
        Div(
            attrs = ProjectCardTitleStyle
                .toModifier()
                .color(tokens.ink.ink)
                .toAttrs(),
        ) {
            ComposeText(project.name)
        }
        Div(
            attrs = ProjectCardDescStyle
                .toModifier()
                .color(tokens.ink.ink3)
                .toAttrs(),
        ) {
            ComposeText(project.description)
        }
        Div(attrs = ProjectCardStatsStyle.toModifier().toAttrs()) {
            project.stats.forEach { stat ->
                StatBlock(
                    value = stat.value,
                    label = stat.label,
                    size = StatSize.Small,
                )
            }
        }
        ProjectCardFoot(project = project)
    }
}

@Composable
private fun ProjectCardFoot(project: Project) {
    val tokens = LocalPortfolioTokens.current

    Div(
        attrs = ProjectCardFootStyle
            .toModifier()
            .borderTop(
                width = FOOT_BORDER_WIDTH,
                style = LineStyle.Solid,
                color = tokens.paper.paper3,
            )
            .toAttrs(),
    ) {
        Span(
            attrs = Modifier
                .color(tokens.muted.muted)
                .display(DisplayStyle.LegacyInlineFlex)
                .alignItems(AlignItems.Center)
                .gap(STACK_ROW_GAP)
                .toAttrs(),
        ) {
            project.stack.forEachIndexed { index, item ->
                if (index > 0) {
                    MetaSeparator()
                }
                Span(attrs = Modifier.toAttrs()) { ComposeText(item) }
            }
        }
        Span(
            attrs = ProjectCardCtaStyle
                .toModifier()
                .color(tokens.accent.accent)
                .toAttrs(),
        ) {
            ComposeText(CTA_LABEL)
            PhosphorIcon(
                name = ARROW_ICON_NAME,
                modifier = ArrowStyle
                    .toModifier()
                    .margin(left = ARROW_GAP),
            )
        }
    }
}

/**
 * Base `CssStyle` for the IDE-block project card. Ports `.proj-card` scoped by
 * `[data-cards="ide"]`: no padding at the card level (head and body own their
 * own spacing), pointer cursor, and a transform + background transition so the
 * hover state can lift the card and flip the background. The base background
 * (`--paper`) is applied inline by the composable so it stays token-aware.
 */
val ProjectCardIdeStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Block)
            .cursor(Cursor.Pointer)
            .transition(
                Transition.group(
                    properties = listOf("background-color", "transform"),
                    duration = CARD_TRANSITION_DURATION_S.s,
                ),
            )
    }
    hover {
        val paper = if (colorMode == ColorMode.DARK) Paper.dark else Paper.light
        Modifier
            .backgroundColor(paper.paper2)
            .translateY(CARD_HOVER_LIFT)
    }
}

/**
 * Base `CssStyle` for the card head. Ports `.proj-head`: mono 12px row with
 * padding 14px/24px and a 12px column gap. Background and text color are
 * applied inline by the composable so they resolve against the current theme
 * tokens.
 */
val ProjectCardHeadStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Flex)
            .alignItems(AlignItems.Center)
            .justifyContent(JustifyContent.SpaceBetween)
            .flexWrap(FlexWrap.Wrap)
            .gap(HEAD_GAP)
            .padding(
                topBottom = HEAD_PADDING_Y,
                leftRight = HEAD_PADDING_X,
            )
            .fontFamily(*FontFamilies.MONO)
            .fontSize(HEAD_FONT_SIZE)
    }
}

/**
 * Base `CssStyle` for the kind tag inside the card head. Ports
 * `.proj-head .tag`: a 10px uppercase mono pill with 2px radius and 3px/8px
 * padding. Background and foreground colors are applied inline so the accent
 * soft / accent ink pairing resolves per theme.
 */
val ProjectCardTagStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.LegacyInlineFlex)
            .alignItems(AlignItems.Center)
            .padding(
                topBottom = TAG_PADDING_Y,
                leftRight = TAG_PADDING_X,
            )
            .borderRadius(TAG_BORDER_RADIUS)
            .fontSize(TAG_FONT_SIZE)
            .fontWeight(FontWeight.SemiBold)
    }
}

/**
 * Base `CssStyle` for the card body. Ports `.proj-body`: 32px / 24px padding.
 */
val ProjectCardBodyStyle = CssStyle {
    base {
        Modifier
            .padding(
                topBottom = BODY_PADDING_Y,
                leftRight = BODY_PADDING_X,
            )
    }
}

/**
 * Base `CssStyle` for the card title. Ports `.proj-title` under
 * `[data-cards="ide"]`: 32px display font, medium weight, tightened tracking,
 * 10px bottom margin.
 */
val ProjectCardTitleStyle = CssStyle {
    base {
        Modifier
            .fontFamily(*FontFamilies.DISPLAY)
            .fontSize(TITLE_FONT_SIZE)
            .fontWeight(FontWeight.Medium)
            .letterSpacing(TITLE_LETTER_SPACING_EM.em)
            .margin(bottom = TITLE_MARGIN_BOTTOM)
    }
}

/**
 * Base `CssStyle` for the card description. Ports `.proj-desc` under
 * `[data-cards="ide"]`: 14px body copy with a 48ch soft cap and a 24px bottom
 * margin before the stats grid.
 */
val ProjectCardDescStyle = CssStyle {
    base {
        Modifier
            .fontFamily(*FontFamilies.BODY)
            .fontSize(DESC_FONT_SIZE)
            .lineHeight(DESC_LINE_HEIGHT)
            .maxWidth(DESC_MAX_WIDTH_CH.unsafeCast<CSSSizeValue<CSSUnit.ch>>())
            .margin(bottom = DESC_MARGIN_BOTTOM)
    }
}

/**
 * Base `CssStyle` for the stats row. Ports `.proj-stats`: a 3-column grid with
 * 32px gaps and a 20px bottom margin before the foot rule. Using a grid (vs.
 * the handoff's flex row) keeps stat cells aligned when individual values span
 * different lengths.
 */
val ProjectCardStatsStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Grid)
            .gridTemplateColumns {
                repeat(STATS_COLUMN_COUNT) { size(STATS_COLUMNS_FR.fr) }
            }
            .gap(STATS_GAP)
            .margin(bottom = STATS_MARGIN_BOTTOM)
    }
}

/**
 * Base `CssStyle` for the card foot. Ports `.proj-foot` under
 * `[data-cards="ide"]`: flex row with a 20px top padding and a hairline top
 * rule. The border color is applied inline by the composable so it resolves
 * per theme.
 */
val ProjectCardFootStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Flex)
            .alignItems(AlignItems.Center)
            .justifyContent(JustifyContent.SpaceBetween)
            .flexWrap(FlexWrap.Wrap)
            .gap(HEAD_GAP)
            .padding(top = FOOT_PADDING_TOP)
            .fontFamily(*FontFamilies.MONO)
            .fontSize(FOOT_FONT_SIZE)
    }
}

/**
 * Base `CssStyle` for the `read case study ->` CTA inside the card foot.
 * Mono font, semi-bold, inline flex so the Phosphor arrow sits alongside the
 * label. The surrounding `.proj-foot` already forces mono typography; the
 * `TextTransform.None` here is an explicit guard so nested utility classes
 * cannot uppercase the copy.
 */
val ProjectCardCtaStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.LegacyInlineFlex)
            .alignItems(AlignItems.Center)
            .fontWeight(FontWeight.SemiBold)
            .textTransform(TextTransform.None)
    }
}
