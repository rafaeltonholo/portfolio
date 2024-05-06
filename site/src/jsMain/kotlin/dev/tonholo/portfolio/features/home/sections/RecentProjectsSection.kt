package dev.tonholo.portfolio.features.home.sections

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.flex
import com.varabyte.kobweb.compose.ui.modifiers.flexGrow
import com.varabyte.kobweb.compose.ui.modifiers.flexShrink
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.toModifier
import dev.tonholo.portfolio.core.components.layout.ExtendedArrangement
import dev.tonholo.portfolio.core.components.layout.FlowRow
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.extensions.padding
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.unit.dp
import dev.tonholo.portfolio.features.home.components.ProjectCard
import dev.tonholo.portfolio.resources.RecentProjectsSection

val RecentProjectsSectionStyles by ComponentStyle {
    base {
        Modifier
            .gap(40.dp)
            .padding(vertical = 40.dp)
    }
}

val RecentProjectsCardStyles by ComponentStyle {
    base {
        Modifier
            .flexGrow(0.5f)
            .flexShrink(1f)
    }
}

@Composable
fun RecentProjectsSection(
    recentProjects: RecentProjectsSection,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = RecentProjectsSectionStyles.toModifier() then modifier,
    ) {
        Text(
            text = recentProjects.title,
            style = Theme.typography.displaySmall,
        )

        FlowRow(
            horizontalArrangement = ExtendedArrangement.spacedBy(24.dp, alignment = Alignment.Start),
            verticalArrangement = ExtendedArrangement.spacedBy(40.dp, alignment = Alignment.Top),
            maxItemsInEachRow = 2,
        ) {
            recentProjects.projects.forEach { project ->
                ProjectCard(
                    name = project.title,
                    description = project.description,
                    src = project.src,
                    modifier = RecentProjectsCardStyles.toModifier(),
                )
            }
        }
    }
}
