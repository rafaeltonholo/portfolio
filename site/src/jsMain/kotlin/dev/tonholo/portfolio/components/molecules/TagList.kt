package dev.tonholo.portfolio.components.molecules

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.flexWrap
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.components.atoms.Chip
import dev.tonholo.portfolio.theme.layout.unit.dp
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexWrap
import org.jetbrains.compose.web.dom.Div

private val TAG_LIST_GAP = 4.dp

/**
 * Horizontal wrapping row of [Chip] atoms. Maps to `.chip` usage inside
 * wrapping containers in the handoff CSS such as `.proj-meta`,
 * `.article-tags`, and `.resume-skills .row`.
 *
 * Each entry in [tags] is rendered as `"{prefix}{tag}"` inside a default
 * [Chip]. The row wraps to multiple lines as needed, with a small gap
 * between items.
 *
 * @param tags the raw tag values to render.
 * @param modifier optional [Modifier] chained after the base styles.
 * @param prefix optional prefix prepended to every tag (e.g. `"#"` for
 *   article tags). Defaults to an empty string.
 */
@Composable
fun TagList(tags: List<String>, modifier: Modifier = Modifier, prefix: String = "") {
    val finalModifier = TagListStyle
        .toModifier()
        .then(modifier)

    Div(attrs = finalModifier.toAttrs()) {
        tags.forEach { tag ->
            Chip(text = "$prefix$tag")
        }
    }
}

/**
 * Base `CssStyle` for [TagList]: flex row that wraps onto multiple lines
 * with a small gap between chips.
 */
val TagListStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Flex)
            .flexWrap(FlexWrap.Wrap)
            .gap(TAG_LIST_GAP)
    }
}
