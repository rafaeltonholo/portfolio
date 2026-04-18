package dev.tonholo.portfolio.components.atoms

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.typography.Typography
import dev.tonholo.portfolio.theme.typography.apply
import org.jetbrains.compose.web.dom.Span
import dev.tonholo.portfolio.theme.typography.TextStyle as PortfolioTypeStyle
import org.jetbrains.compose.web.dom.Text as ComposeText

/**
 * Generic text node that applies a [TextSlot]'s typography to a `<span>` and
 * optionally overrides color. Variants are expressed via [slot] rather than
 * subclassing.
 *
 * @param text the string to render.
 * @param modifier extra [Modifier] chained after the slot's base styles.
 * @param slot typography slot from [TextSlot]; defaults to [TextSlot.Body].
 * @param color optional override for the text color; when `null`, the element
 *   inherits the surrounding color.
 */
@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier,
    slot: TextSlot = TextSlot.Body,
    color: Color? = null,
) {
    // LocalPortfolioTokens is read to ensure the composable recomposes on theme
    // changes even when no token field is directly consumed here.
    LocalPortfolioTokens.current

    var finalModifier = PortfolioTextStyle
        .toModifier()
        .apply(slot.style)
    if (color != null) {
        finalModifier = finalModifier.color(color)
    }
    finalModifier = finalModifier.then(modifier)

    Span(attrs = finalModifier.toAttrs()) {
        ComposeText(text)
    }
}

/**
 * Curated set of typographic slots this atom can render. Each slot maps to
 * one [PortfolioTypeStyle] on [Typography]; callers pick the slot by name
 * rather than subclassing.
 */
enum class TextSlot(val style: PortfolioTypeStyle) {
    Hero(style = Typography.hero),
    H1(style = Typography.h1),
    H2(style = Typography.h2),
    H3Display(style = Typography.h3Display),
    BodyLarge(style = Typography.bodyLarge),
    Body(style = Typography.body),
    BodySmall(style = Typography.bodySmall),
    SerifLead(style = Typography.serifLead),
    SerifBody(style = Typography.serifBody),
    Code(style = Typography.code),
}

/**
 * Base `CssStyle` for [Text]. Intentionally empty so callers can freely chain
 * additional [Modifier]s (spacing, alignment, etc.) without fighting inherited
 * declarations. Exposed publicly so Kobweb KSP picks it up.
 */
val PortfolioTextStyle = CssStyle {}
