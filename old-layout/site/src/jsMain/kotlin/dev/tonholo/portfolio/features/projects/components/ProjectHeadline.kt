package dev.tonholo.portfolio.features.projects.components

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.color
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.resources.Project

@Composable
fun ProjectHeadline(
    project: Project,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .then(project.customHeadline?.backgroundColor?.let { Modifier.backgroundColor(it) } ?: Modifier)
            .then(project.customHeadline?.textColor?.let { Modifier.color(it) } ?: Modifier),
    ) {
        Text(
            text = project.customHeadline?.headline ?: project.title,
            style = Theme.typography.displayLarge,
        )
    }
}
