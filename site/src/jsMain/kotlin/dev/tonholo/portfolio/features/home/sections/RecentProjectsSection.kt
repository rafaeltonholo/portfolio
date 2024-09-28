package dev.tonholo.portfolio.features.home.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.extensions.ResponsiveValues
import dev.tonholo.portfolio.core.extensions.padding
import dev.tonholo.portfolio.core.extensions.responsiveStateOf
import dev.tonholo.portfolio.core.foundation.ExtendedArrangement
import dev.tonholo.portfolio.core.foundation.layout.FlowRow
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.unit.dp
import dev.tonholo.portfolio.features.home.components.ProjectCard
import dev.tonholo.portfolio.resources.Project
import dev.tonholo.portfolio.resources.ProjectType
import dev.tonholo.portfolio.resources.pages.RecentProjectsSection

val RecentProjectsSectionStyle = CssStyle {
    base {
        Modifier
            .gap(40.dp)
            .padding(vertical = 40.dp)
    }
}

@Composable
fun RecentProjectsSection(
    recentProjects: RecentProjectsSection,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = RecentProjectsSectionStyle.toModifier() then modifier,
    ) {
        Text(
            text = recentProjects.title,
            style = Theme.typography.displaySmall,
        )

        recentProjects.projects[ProjectType.Commercial]?.let { projects ->
            Projects(
                projectLabel = recentProjects.projectTypeLabels.getValue(ProjectType.Commercial),
                projects = projects,
            )
        }
        recentProjects.projects[ProjectType.OpenSource]?.let { projects ->
            Projects(
                projectLabel = recentProjects.projectTypeLabels.getValue(ProjectType.OpenSource),
                projects = projects,
            )
        }
    }
}

@Composable
private fun Projects(
    projectLabel: String,
    projects: List<Project>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        val maxItemsInEachRow by responsiveStateOf(ResponsiveValues(base = 1, lg = 2))
        Text(
            text = projectLabel,
            style = Theme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 24.dp),
        )
        FlowRow(
            horizontalArrangement = ExtendedArrangement.spacedBy(24.dp, alignment = Alignment.Start),
            verticalArrangement = ExtendedArrangement.spacedBy(40.dp, alignment = Alignment.Top),
            maxItemsInEachRow = maxItemsInEachRow,
        ) {
            projects.forEach { project ->
                ProjectCard(
                    name = project.title,
                    description = project.description,
                    src = project.src,
                    playStoreSrc = project.playStoreSrc,
                )
            }
        }
    }
}
