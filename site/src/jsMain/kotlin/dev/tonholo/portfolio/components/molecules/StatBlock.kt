package dev.tonholo.portfolio.components.molecules

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextTransform
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textTransform
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.layout.unit.dp
import dev.tonholo.portfolio.theme.typography.FontFamilies
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text as ComposeText

// Small variant (`.proj-stat` inside an IDE card).
private val SMALL_NUM_SIZE = 24.dp
private val SMALL_LABEL_SIZE = 10.dp
private val SMALL_LABEL_MARGIN_TOP = 6.dp
private const val SMALL_LABEL_LETTER_SPACING_EM = 0.08

// Large variant (`.outcome-cell` in an outcomes grid).
private val LARGE_CELL_PADDING_Y = 24.dp
private val LARGE_CELL_PADDING_X = 20.dp
private val LARGE_NUM_SIZE = 44.dp
private val LARGE_NUM_MARGIN_BOTTOM = 8.dp
private const val LARGE_NUM_LETTER_SPACING_EM = -0.03
private val LARGE_LABEL_SIZE = 13.dp
private const val LARGE_LABEL_MAX_WIDTH_CH = 22.0
private const val LARGE_LABEL_LINE_HEIGHT = 1.45

private const val NUM_LINE_HEIGHT_UNITLESS = 1.0

/**
 * Size axis for [StatBlock]. [Small] powers the `.proj-stat` slot inside
 * an IDE-style project card; [Large] powers the `.outcome-cell` slot used
 * in the outcomes grid on project detail pages.
 */
enum class StatSize {
    Small,
    Large,
}

/**
 * Number + label stacked metric used in projects and outcomes grids. Maps to
 * `.proj-stat` (small) and `.outcome-cell` (large) in the handoff CSS.
 *
 * @param value the headline number, e.g. `"-65%"` or `"2M+"`.
 * @param label the caption rendered below, uppercase for the small variant
 *   and left-aligned body copy for the large variant.
 * @param size variant selector: [StatSize.Small] for in-card stats, [StatSize.Large]
 *   for outcomes cells.
 * @param modifier optional [Modifier] chained after the base styles.
 */
@Composable
fun StatBlock(
    value: String,
    label: String,
    size: StatSize,
    modifier: Modifier = Modifier,
) {
    val tokens = LocalPortfolioTokens.current

    val finalModifier = when (size) {
        StatSize.Small ->
            StatBlockSmallStyle.toModifier()

        StatSize.Large ->
            StatBlockLargeStyle
                .toModifier()
                .padding(
                    topBottom = LARGE_CELL_PADDING_Y,
                    leftRight = LARGE_CELL_PADDING_X,
                )
    }.then(modifier)

    Div(attrs = finalModifier.toAttrs()) {
        val numberModifier = when (size) {
            StatSize.Small ->
                Modifier
                    .display(DisplayStyle.Block)
                    .fontFamily(*FontFamilies.MONO)
                    .fontSize(SMALL_NUM_SIZE)
                    .fontWeight(FontWeight.SemiBold)
                    .lineHeight(NUM_LINE_HEIGHT_UNITLESS)
                    .color(tokens.accent.accent)

            StatSize.Large ->
                Modifier
                    .display(DisplayStyle.Block)
                    .fontFamily(*FontFamilies.DISPLAY)
                    .fontSize(LARGE_NUM_SIZE)
                    .fontWeight(FontWeight.Medium)
                    .lineHeight(NUM_LINE_HEIGHT_UNITLESS)
                    .letterSpacing(LARGE_NUM_LETTER_SPACING_EM.em)
                    .margin(bottom = LARGE_NUM_MARGIN_BOTTOM)
                    .color(tokens.accent.accent)
        }
        Span(attrs = numberModifier.toAttrs()) {
            ComposeText(value)
        }

        val labelModifier = when (size) {
            StatSize.Small ->
                Modifier
                    .display(DisplayStyle.Block)
                    .fontFamily(*FontFamilies.MONO)
                    .fontSize(SMALL_LABEL_SIZE)
                    .margin(top = SMALL_LABEL_MARGIN_TOP)
                    .letterSpacing(SMALL_LABEL_LETTER_SPACING_EM.em)
                    .textTransform(TextTransform.Uppercase)
                    .color(tokens.muted.muted)

            StatSize.Large ->
                Modifier
                    .display(DisplayStyle.Block)
                    .fontFamily(*FontFamilies.BODY)
                    .fontSize(LARGE_LABEL_SIZE)
                    .lineHeight(LARGE_LABEL_LINE_HEIGHT)
                    .maxWidth(LARGE_LABEL_MAX_WIDTH_CH.unsafeCast<CSSSizeValue<CSSUnit.ch>>())
                    .color(tokens.ink.ink2)
        }
        Span(attrs = labelModifier.toAttrs()) {
            ComposeText(label)
        }
    }
}

/**
 * Base `CssStyle` for the small [StatBlock] variant (`.proj-stat`). The
 * outer block uses the mono font; number and label colors are applied
 * inline by the composable so they stay token-aware.
 */
val StatBlockSmallStyle = CssStyle {
    base {
        Modifier.fontFamily(*FontFamilies.MONO)
    }
}

/**
 * Base `CssStyle` for the large [StatBlock] variant (`.outcome-cell`).
 * Renders a paper background; outcomes grid parents supply the 1px `gap`
 * that produces the hairline separators between cells.
 */
val StatBlockLargeStyle = CssStyle {
    base {
        Modifier
    }
}
