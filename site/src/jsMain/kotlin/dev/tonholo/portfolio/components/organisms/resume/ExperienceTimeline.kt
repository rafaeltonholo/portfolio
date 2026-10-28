package dev.tonholo.portfolio.components.organisms.resume

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.flexDirection
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.components.atoms.MetaLabel
import dev.tonholo.portfolio.components.molecules.SectionHead
import dev.tonholo.portfolio.components.molecules.TimelineRow
import dev.tonholo.portfolio.components.template.sectionMarker
import dev.tonholo.portfolio.domain.model.Experience
import dev.tonholo.portfolio.locale.LocalStrings
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.dom.Div

// Ordinal for the Experience timeline; rendered as `// 01` via [sectionMarker].
private const val SECTION_ORDINAL = 1

/**
 * Stacked list of resume timeline rows. Ports the `<div class="timeline">`
 * block in `pages2.jsx` (lines 15-47) plus its preceding `.section-head`:
 * renders a [SectionHead] (`// 01 - Experience`, trailing `most recent first`
 * meta) followed by one [TimelineRow] per entry in [experiences]. The last
 * row is flagged so it paints the closing hairline border to terminate the
 * list (matches `.timeline-row.last`).
 *
 * The organism keeps zero direct layout styling on the `<div class="timeline">`
 * wrapper beyond the handoff's `display: flex; flex-direction: column;`.
 *
 * @param experiences list of [Experience] rows to render, top-down (the
 *   handoff presents them most-recent-first; ordering is owned by the caller).
 * @param modifier optional [Modifier] chained after the base styles.
 */
@Composable
fun ExperienceTimeline(experiences: List<Experience>, modifier: Modifier = Modifier) {
    val strings = LocalStrings.current.experience

    val finalModifier = ExperienceTimelineStyle
        .toModifier()
        .then(modifier)

    Div(attrs = finalModifier.toAttrs()) {
        SectionHead(
            number = sectionMarker(ordinal = SECTION_ORDINAL),
            title = strings.sectionTitle,
            trailing = { MetaLabel(text = strings.sectionTrailing) },
        )
        Div(attrs = ExperienceTimelineListStyle.toModifier().toAttrs()) {
            experiences.forEachIndexed { index, experience ->
                TimelineRow(
                    experience = experience,
                    last = index == experiences.lastIndex,
                )
            }
        }
    }
}

/**
 * Base `CssStyle` for the outer `<div>` wrapper. Kept empty so vertical
 * rhythm is owned by the section head's bottom margin and the [TimelineRow]
 * padding; callers can chain top padding via the [Modifier] argument.
 */
val ExperienceTimelineStyle = CssStyle {}

/**
 * Base `CssStyle` for the `.timeline` container. Ports `styles.css` line 1129:
 * a vertical flex stack so each [TimelineRow] paints its own 1px top border
 * without collapsing against its neighbour.
 */
val ExperienceTimelineListStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Flex)
            .flexDirection(FlexDirection.Column)
    }
}
