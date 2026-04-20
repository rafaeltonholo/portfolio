package dev.tonholo.portfolio.components.organisms.home

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.ListStyleType
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.flexDirection
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.listStyle
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.components.atoms.MetaLabel
import dev.tonholo.portfolio.components.atoms.MetaLabelVariant
import dev.tonholo.portfolio.components.atoms.MetaSeparator
import dev.tonholo.portfolio.components.template.NumberedSection
import dev.tonholo.portfolio.domain.model.UseCategory
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.state.LocalLocaleState
import dev.tonholo.portfolio.state.StaticContent
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.layout.unit.dp
import dev.tonholo.portfolio.theme.typography.FontFamilies
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Ul
import org.jetbrains.compose.web.dom.Text as ComposeText

// Ordinal for the Uses section: renders as `// 03`.
private const val SECTION_ORDINAL = 3

// Grid: 4 equal columns, 32px gap (handoff line 239). Collapses to 2 cols
// below LG (820px) and 1 col below MD (720px).
private val GRID_GAP = 32.dp
private const val GRID_COL_FR = 1

// Category column: meta label + mono list.
private val CATEGORY_LABEL_MARGIN_BOTTOM = 12.dp

// Mono list: 8px row gap, 13px font, handoff line 248.
private val LIST_GAP = 8.dp
private val LIST_FONT_SIZE = 13.dp
private const val LIST_LINE_HEIGHT = 1.5

// Inline gap between the bullet dot and the item text.
private val LIST_ITEM_GAP = 8.dp

// Slash prefix before category name, per handoff line 247: `/editor`, `/hardware`, ...
private const val CATEGORY_NAME_PREFIX = "/"

/**
 * Home page organism. Ports the `Uses` section from the handoff
 * `home.jsx` (lines 231-255): a four-column grid of categorized tool lists
 * (editor / hardware / desk / cli) introduced by a [SectionHead] labelled
 * `// 03 Uses` with a trailing `full list ->` accent link.
 *
 * Collapses to two columns below [Breakpoint.LG] and one column below
 * [Breakpoint.MD], matching the handoff media queries on `.uses-grid`
 * style equivalents elsewhere in the handoff CSS.
 *
 * Reads directly from [StaticContent.USES]; this organism owns no state
 * or external callbacks.
 *
 * @param modifier optional [Modifier] chained after the base section styles.
 */
@Composable
fun UsesGrid(modifier: Modifier = Modifier) {
    val locale = LocalLocaleState.current.current
    val categories = StaticContent.usesFor(locale = locale)
    val strings = LocalStrings.current.uses

    NumberedSection(
        number = SECTION_ORDINAL,
        title = strings.sectionTitle,
        modifier = modifier,
        trailing = {
            MetaLabel(
                text = strings.sectionTrailingLabel,
                variant = MetaLabelVariant.Accent,
            )
        },
    ) {
        Div(attrs = UsesGridStyle.toModifier().toAttrs()) {
            categories.forEach { category ->
                UsesColumn(category = category)
            }
        }
    }
}

/**
 * One category column inside [UsesGrid]. Renders a mono meta label with the
 * category name prefixed by `/`, then a marker-less `<ul>` of items each
 * prefixed by a middle dot. Font size is 13px (0.8125rem) per the handoff
 * inline styles on the unordered list.
 */
@Composable
private fun UsesColumn(category: UseCategory) {
    val tokens = LocalPortfolioTokens.current

    Div(attrs = Modifier.toAttrs()) {
        MetaLabel(
            text = "$CATEGORY_NAME_PREFIX${category.name}",
            modifier = Modifier.margin(bottom = CATEGORY_LABEL_MARGIN_BOTTOM),
        )
        Ul(
            attrs = UsesListStyle
                .toModifier()
                .color(tokens.ink.ink2)
                .toAttrs(),
        ) {
            category.items.forEach { item ->
                Li(
                    attrs = Modifier
                        .display(DisplayStyle.Flex)
                        .alignItems(AlignItems.Center)
                        .gap(LIST_ITEM_GAP)
                        .toAttrs(),
                ) {
                    MetaSeparator()
                    ComposeText(item)
                }
            }
        }
    }
}

/**
 * Base `CssStyle` for the four-column uses grid. Handoff line 239 sets a
 * 4-col grid at 32px gap; the two media queries collapse to 2 cols below
 * [Breakpoint.LG] (820px) and 1 col below [Breakpoint.MD] (720px). Since
 * Silk's breakpoint blocks are `min-width`, the base rule renders the
 * mobile case (1 column), the `MD` block upgrades to 2 columns, and the
 * `LG` block restores the 4-column layout.
 */
val UsesGridStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Grid)
            .gridTemplateColumns {
                size(GRID_COL_FR.fr)
            }
            .gap(GRID_GAP)
    }
    Breakpoint.MD {
        Modifier.gridTemplateColumns {
            size(GRID_COL_FR.fr)
            size(GRID_COL_FR.fr)
        }
    }
    Breakpoint.LG {
        Modifier.gridTemplateColumns {
            size(GRID_COL_FR.fr)
            size(GRID_COL_FR.fr)
            size(GRID_COL_FR.fr)
            size(GRID_COL_FR.fr)
        }
    }
}

/**
 * Base `CssStyle` for the mono item list inside a category column. Ports
 * the inline `<ul>` style on handoff line 248: no marker, vertical flex
 * stack, 8px gap, 13px mono font. Color is applied inline by the column
 * composable so it stays token-aware.
 */
val UsesListStyle = CssStyle {
    base {
        Modifier
            .listStyle(type = ListStyleType.None)
            .display(DisplayStyle.Flex)
            .flexDirection(FlexDirection.Column)
            .gap(LIST_GAP)
            .margin(topBottom = 0.cssRem)
            .padding(left = 0.cssRem)
            .fontFamily(*FontFamilies.MONO)
            .fontSize(LIST_FONT_SIZE)
            .lineHeight(LIST_LINE_HEIGHT)
    }
}
