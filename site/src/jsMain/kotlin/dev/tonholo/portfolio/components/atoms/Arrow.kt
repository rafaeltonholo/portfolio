package dev.tonholo.portfolio.components.atoms

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text as ComposeText

private const val ARROW_GLYPH = "\u2192" // →
private const val ARROW_TRANSITION_DURATION_S = 0.2

/**
 * Inline rightward arrow glyph (`→`). Maps to the `.arrow` span used across
 * the handoff card footers and CTA labels. The base style sets up a smooth
 * transform transition so parent selectors (e.g., `.proj-card:hover .arrow`)
 * can animate a 4px translate with no extra setup.
 *
 * @param modifier optional [Modifier] chained after the base styles.
 * @param color optional color override; defaults to `inherit`.
 */
@Composable
fun Arrow(modifier: Modifier = Modifier, color: Color? = null) {
    // Read tokens so the arrow recomposes on theme changes even though it
    // inherits color from its parent by default.
    LocalPortfolioTokens.current

    var finalModifier = ArrowStyle.toModifier()
    if (color != null) {
        finalModifier = finalModifier.color(color)
    }
    finalModifier = finalModifier.then(modifier)

    Span(attrs = finalModifier.toAttrs { attr("aria-hidden", "true") }) {
        ComposeText(ARROW_GLYPH)
    }
}

/**
 * Base `CssStyle` for [Arrow]. Declares the inline-block display and the
 * `transition: transform` handshake so ancestor hover selectors can animate
 * the glyph horizontally (matching `.proj-card:hover .arrow`).
 */
val ArrowStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.InlineBlock)
            .transition(
                Transition.of(
                    property = "transform",
                    duration = ARROW_TRANSITION_DURATION_S.s,
                ),
            )
    }
}
