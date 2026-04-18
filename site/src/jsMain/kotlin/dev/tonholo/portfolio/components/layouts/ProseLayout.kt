package dev.tonholo.portfolio.components.layouts

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.dom.Div

// 64ch caps the reading measure at roughly 64 characters, the sweet spot used
// by `.prose` in the handoff for long-form copy inside article detail pages
// and the About tabs.
private const val PROSE_MAX_WIDTH_CH = 64.0

/**
 * Constrained reading-measure container used by article detail copy and the
 * About tabs. Caps the max-width at 64ch and centers horizontally via auto
 * horizontal margins so the surrounding shell keeps its gutter while the
 * prose column hits the handoff `.prose` width.
 *
 * @param modifier optional [Modifier] chained after the base [ProseLayoutStyle].
 * @param content the prose body rendered inside the container.
 */
@Composable
fun ProseLayout(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Div(attrs = ProseLayoutStyle.toModifier().then(modifier).toAttrs()) {
        content()
    }
}

/**
 * Base `CssStyle` for [ProseLayout]. Sets the 64ch reading measure and the
 * `margin: 0 auto` centering so the column sits inside any parent shell
 * without needing additional wrappers.
 */
val ProseLayoutStyle = CssStyle {
    base {
        Modifier
            .maxWidth(PROSE_MAX_WIDTH_CH.unsafeCast<CSSSizeValue<CSSUnit.ch>>())
            .margin(
                topBottom = 0.cssRem,
                leftRight = auto.unsafeCast<CSSLengthOrPercentageNumericValue>(),
            )
    }
}
