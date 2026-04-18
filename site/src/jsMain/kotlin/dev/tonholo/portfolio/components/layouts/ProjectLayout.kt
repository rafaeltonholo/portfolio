package dev.tonholo.portfolio.components.layouts

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.extendedBy
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.components.organisms.projects.PrevNextNav
import dev.tonholo.portfolio.theme.layout.ShellStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.dom.Div

// Matches the handoff `--pad-y` (80px) applied to `.project-detail` so the
// project chrome lines up with the other detail pages.
private const val PROJECT_PADDING_Y_REM = 5.0

/**
 * Layout for project detail pages. Delegates the chrome (top nav, footer,
 * language toast) to [PageLayout] and wraps the page body in the shared
 * [ShellStyle] with the handoff `--pad-y` vertical padding. A [PrevNextNav]
 * strip is rendered after the content so the sticky footer sits within the
 * same capped container as the article body.
 *
 * When [nextProjectName] is `null` the `next: {project}` slot in
 * [PrevNextNav] is hidden, matching the behaviour for the final project in
 * the series.
 *
 * @param nextProjectName the human-readable name of the next project, or
 *   `null` to hide the right slot of [PrevNextNav].
 * @param onBack invoked when the back link is clicked.
 * @param onNext invoked when the next link is clicked. Only attached when
 *   [nextProjectName] is non-null.
 * @param modifier optional [Modifier] applied to the wrapper `<div>`.
 * @param content the project body rendered inside the shell.
 */
@Composable
fun ProjectLayout(
    nextProjectName: String?,
    onBack: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    PageLayout {
        Div(attrs = ProjectLayoutStyle.toModifier().then(modifier).toAttrs()) {
            content()
            PrevNextNav(
                nextProjectName = nextProjectName,
                onBack = onBack,
                onNext = onNext,
            )
        }
    }
}

/**
 * Base `CssStyle` for [ProjectLayout]. Extends [ShellStyle] so the project
 * wrapper inherits the max-width cap and horizontal gutters, then layers on
 * the 5rem vertical padding called out by `.project-detail`.
 */
val ProjectLayoutStyle = ShellStyle.extendedBy {
    base {
        Modifier.padding(topBottom = PROJECT_PADDING_Y_REM.cssRem)
    }
}
