package dev.tonholo.portfolio.components.template

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.components.molecules.SectionHead
import dev.tonholo.portfolio.theme.layout.PageSectionShellStyle
import org.jetbrains.compose.web.dom.Section

// Mono marker format used by every numbered home section (`// 00`, `// 01`,
// ...). Centralised so organisms only supply the ordinal integer; drift
// between `"// 1"` and `"// 01"` styles becomes impossible.
private const val MARKER_FORMAT_WIDTH = 2
private const val MARKER_PREFIX = "// "

/**
 * Shared layout wrapper for every numbered home-page section (`// 00` - `// 03`).
 * Renders the `<section>` element with [PageSectionShellStyle] (shell gutters
 * + 5rem vertical rhythm), a [SectionHead] carrying the ordinal marker, and
 * a content slot.
 *
 * Using `padding` rather than `margin` means adjacent sections always get the
 * full `5rem + 5rem = 10rem` combined padding between content boxes; padding
 * never collapses, so the rhythm is deterministic regardless of the content
 * above or below.
 *
 * @param number the section ordinal; formatted into the mono marker as
 *   `"// 00"` etc.
 * @param title the editorial title rendered as the section `<h2>`.
 * @param modifier optional [Modifier] chained after the base section style,
 *   typically used by the page to attach a scroll-target `id`.
 * @param trailing optional composable rendered in [SectionHead]'s trailing
 *   slot; use for meta chips like "most recent first" or `full list ->`.
 * @param content the section body, usually a grid of cards or columns.
 */
@Composable
fun NumberedSection(
    number: Int,
    title: String,
    modifier: Modifier = Modifier,
    trailing: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val marker = MARKER_PREFIX + number.toString().padStart(MARKER_FORMAT_WIDTH, padChar = '0')
    val sectionModifier = PageSectionShellStyle
        .toModifier()
        .then(modifier)

    Section(attrs = sectionModifier.toAttrs()) {
        SectionHead(number = marker, title = title, trailing = trailing)
        content()
    }
}
