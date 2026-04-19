package dev.tonholo.portfolio.components.template

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.components.molecules.SectionHead
import dev.tonholo.portfolio.theme.layout.PageSectionShellStyle
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
 * Shared layout wrapper for every numbered home-page section (`// 00` - `// 03`).
 * Renders the `<section>` element with [PageSectionShellStyle] (shell gutters
 * + density-aware `--pad-y` vertical rhythm), a [SectionHead] carrying the
 * ordinal marker, and a content slot.
 *
 * Using `padding` rather than `margin` means adjacent sections always get the
 * full `padY + padY` combined padding between content boxes; padding never
 * collapses, so the rhythm is deterministic regardless of the content above
 * or below and the Density setting drives both halves consistently.
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
    val sectionModifier = PageSectionShellStyle
        .toModifier()
        .then(modifier)

    Section(attrs = sectionModifier.toAttrs()) {
        SectionHead(number = sectionMarker(ordinal = number), title = title, trailing = trailing)
        content()
    }
}
