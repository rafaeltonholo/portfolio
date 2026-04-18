package dev.tonholo.portfolio.components.organisms.projects

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.components.molecules.NavLink
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.dom.Div

// Handoff `home.jsx` lines 269-281 render the filter as a tight `<div style={{display:'flex', gap:4}}>`
// row of `nav-link` buttons. 4px at 1rem=16px = 0.25rem.
private const val FILTER_BAR_GAP_REM = 0.25

// Labels mirror the lower-case `l` copy in the handoff filter list.
private const val LABEL_ALL = "all"
private const val LABEL_COMMERCIAL = "commercial"
private const val LABEL_OPEN_SOURCE = "open source"

/**
 * Filter axis for the Projects list page. Maps to the `{id}` values in the
 * handoff filter array (`all`, `commercial`, `oss`).
 */
enum class ProjectsFilter {
    All,
    Commercial,
    OpenSource,
}

/**
 * Horizontal filter bar rendered above the Projects grid. Ports the inline
 * `<div style={{display:'flex', gap:4}}>` row of `nav-link` buttons inside
 * `ProjectsPage` (handoff `home.jsx` lines 269-281): three toggles that select
 * the current project kind filter. The active filter is styled via
 * [NavLink]`(active = true)`, which owns the `> ` prefix and active
 * background/foreground pairing.
 *
 * @param current the currently selected filter; drives the active [NavLink].
 * @param onSelect invoked with the clicked filter; callers should hoist state
 *   up and re-render the grid against the new value.
 * @param modifier optional [Modifier] chained after the base styles.
 */
@Composable
fun ProjectsFilterBar(
    current: ProjectsFilter,
    onSelect: (ProjectsFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    val finalModifier = ProjectsFilterBarStyle
        .toModifier()
        .then(modifier)

    Div(attrs = finalModifier.toAttrs()) {
        ProjectsFilter.entries.forEach { filter ->
            NavLink(
                label = filter.label,
                active = filter == current,
                onClick = { onSelect(filter) },
            )
        }
    }
}

/**
 * Lower-case human label for a filter. Matches the `l` property on each handoff
 * filter entry.
 */
private val ProjectsFilter.label: String
    get() = when (this) {
        ProjectsFilter.All -> LABEL_ALL
        ProjectsFilter.Commercial -> LABEL_COMMERCIAL
        ProjectsFilter.OpenSource -> LABEL_OPEN_SOURCE
    }

/**
 * Base `CssStyle` for [ProjectsFilterBar]. Ports the inline flex row from the
 * handoff: a 4px gap between `NavLink` buttons. Kept public so Kobweb KSP can
 * register it.
 */
val ProjectsFilterBarStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Flex)
            .gap(FILTER_BAR_GAP_REM.cssRem)
    }
}
