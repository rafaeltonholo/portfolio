package dev.tonholo.portfolio.components.atoms

import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.whiteSpace
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.style.CssStyle
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.px

/**
 * Visually hides content while keeping it in the accessibility tree so screen
 * readers still announce it. Use on semantic markers (e.g. a section
 * `<h2>` whose visual heading is supplied by a different element) that need
 * to exist for the document outline but must not show up in the design.
 *
 * Ports the standard `.sr-only` / `.visually-hidden` recipe: a 1x1 square
 * clipped and positioned off-flow. Kept public so Kobweb KSP registers it.
 */
val VisuallyHiddenStyle = CssStyle {
    base {
        Modifier
            .position(Position.Absolute)
            .width(1.px)
            .height(1.px)
            .padding(0.px)
            .margin(leftRight = (-1).px, topBottom = 0.px)
            .overflow(Overflow.Hidden)
            .border(width = 0.px, style = LineStyle.None)
            .whiteSpace(WhiteSpace.NoWrap)
            .styleModifier {
                property("clip", "rect(0, 0, 0, 0)")
                property("clip-path", "inset(50%)")
            }
    }
}
