package dev.tonholo.portfolio.components.template

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.components.layouts.SectionContainerStyle
import dev.tonholo.portfolio.components.layouts.SectionContentStyle
import dev.tonholo.portfolio.components.molecules.SectionHead
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Section

// Mono marker format used by every numbered section (`// 00`, `// 01`, ...).
// Centralised so organisms only supply the ordinal integer; drift between
// `"// 1"` and `"// 01"` styles becomes impossible.
private const val MARKER_FORMAT_WIDTH = 2
private const val MARKER_PREFIX = "// "

/**
 * Formats an ordinal as the mono `// NN` marker used by [SectionHead] callers
 * across the site (home numbered sections, resume timeline, about tabs).
 * Callers that wrap their section in [NumberedSection] do not need this
 * helper - they pass the raw ordinal. Callers that render their own section
 * chrome but still want the zero-padded marker (e.g. `ExperienceTimeline`,
 * which lives inside an already-shelled page) can use this helper directly.
 */
fun sectionMarker(ordinal: Int): String =
    MARKER_PREFIX + ordinal.toString().padStart(MARKER_FORMAT_WIDTH, padChar = '0')

/**
 * Shared layout wrapper for every numbered home-page section (`// 00` - `// 01`).
 * Renders a semantic `<section>` with the standard
 * [SectionContainerStyle] rhythm (vertical [SiteDimensions.padding.section]
 * and the mobile-first horizontal gutter ramp via [SiteDimensions.size]) and
 * the inner [SectionContentStyle] cap so content stays under
 * [SiteDimensions.layout.contentMaxWidth]. The `<section>` body holds a
 * [SectionHead] with the ordinal marker followed by the content slot.
 *
 * Using `padding` rather than `margin` means adjacent sections always get the
 * full `padY + padY` combined padding between content boxes; padding never
 * collapses so the rhythm is deterministic regardless of what lives above or
 * below.
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
    Section(
        attrs = SectionContainerStyle
            .toModifier()
            .then(modifier)
            .toAttrs(),
    ) {
        Div(attrs = SectionContentStyle.toModifier().toAttrs()) {
            SectionHead(number = sectionMarker(ordinal = number), title = title, trailing = trailing)
            content()
        }
    }
}
