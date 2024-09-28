package dev.tonholo.portfolio.features.projects

import androidx.compose.runtime.Composable
import cafe.adriel.lyricist.LanguageTag
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.autoLength
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderBottom
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.core.components.AdaptiveLayout
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.extensions.padding
import dev.tonholo.portfolio.core.foundation.layout.Scaffold
import dev.tonholo.portfolio.core.sections.AppBar
import dev.tonholo.portfolio.core.sections.Footer
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.theme.colorScheme
import dev.tonholo.portfolio.core.ui.theme.typography
import dev.tonholo.portfolio.core.ui.theme.typography.toModifier
import dev.tonholo.portfolio.core.ui.unit.dp
import dev.tonholo.portfolio.resources.Project
import dev.tonholo.portfolio.resources.pages.ProjectPage
import kotlinx.datetime.DatePeriod
import org.jetbrains.compose.web.css.LineStyle

val ProjectContentPageStyle = CssStyle {
    base {
        Modifier.fillMaxSize()
    }
}

val ProjectContainerStyle = CssStyle {
    base {
        Modifier
            .gap(32.dp)
            .fillMaxSize()
    }
}

val ProjectContainerHeadlineStyle = CssStyle {
    base {
        Modifier
            .fillMaxWidth()
            .backgroundColor(colorScheme.surfaceVariant)
            .padding(24.dp)
            .borderRadius(8.dp)
    }
    cssRule("h1") {
        Modifier
            .textAlign(TextAlign.Center)
            .fillMaxWidth()
    }
}

val ProjectContainerContentStyle = CssStyle {
    base {
        Modifier
            .fillMaxWidth()
            .gap(40.dp)
    }
}
val ProjectContainerContentPanelStyle = CssStyle {
    base {
        Modifier
            .fillMaxWidth()
            .margin { bottom(autoLength) }
    }
}

val ProjectContainerContentTitleStyle = CssStyle {
    base {
        typography.headlineMedium
            .toModifier()
            .fillMaxWidth()
            .padding(vertical = 24.dp)
            .borderBottom {
                width(1.dp)
                style(LineStyle.Solid)
            }
    }
}

@Composable
fun ProjectContent(
    strings: ProjectPage,
    project: Project,
    selectedLanguage: LanguageTag,
    modifier: Modifier = Modifier,
    onLocaleChange: (LanguageTag) -> Unit,
    onHomeClick: () -> Unit = {},
    onAboutClick: () -> Unit = {},
    onArticleClick: () -> Unit = {},
    onResumeClick: () -> Unit = {},
) {
    Scaffold(
        modifier = ProjectContentPageStyle.toModifier() then modifier,
        topBar = {
            AppBar(
                selectedLanguage = selectedLanguage,
                onLocaleChange = onLocaleChange,
                onHomeClick = onHomeClick,
                onAboutClick = onAboutClick,
                onArticleClick = onArticleClick,
                onResumeClick = onResumeClick,
            )
        },
        bottomBar = {
            Footer(
                modifier = Modifier.fillMaxWidth(),
            )
        },
        pageTitle = "${strings.title} - ${project.title}",
    ) { paddingValues ->
        Column(
            modifier = ProjectContainerStyle.toModifier()
                .padding(paddingValues),
        ) {
            Row(
                modifier = ProjectContainerHeadlineStyle.toModifier(),
            ) {
                Text(
                    text = project.title,
                    style = Theme.typography.displayLarge,
                )
            }
            AdaptiveLayout(
                modifier = ProjectContainerContentStyle.toModifier(),
                listPanel = {
                    ProjectBackground(
                        strings = strings,
                        content = project.description,
                        modifier = ProjectContainerContentPanelStyle.toModifier(),
                    )
                },
                detailPanel = {
                    ProjectInfo(
                        strings = strings,
                        project = project,
                        modifier = ProjectContainerContentPanelStyle.toModifier(),
                    )
                }
            )
        }
    }
}

@Composable
private fun ProjectBackground(
    strings: ProjectPage,
    content: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = strings.projectBackground,
            style = Theme.typography.headlineMedium,
            modifier = ProjectContainerContentTitleStyle.toModifier(),
        )
        Text(text = content)
    }
}

@Composable
private fun ProjectInfo(
    strings: ProjectPage,
    project: Project,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = strings.info,
            style = Theme.typography.headlineMedium,
            modifier = ProjectContainerContentTitleStyle.toModifier(),
        )
        Text(
            text = strings.role,
            style = Theme.typography.headlineSmall,
        )
        Text(text = project.role)
        project.timeline?.let { timeline ->
            Text(
                text = strings.timeline,
                style = Theme.typography.headlineSmall,
            )
            Text(text = timeline.toHumanString())
        }
    }
}

private fun DatePeriod.toHumanString(): String = buildList {
    if (years > 0) {
        add("$years years")
    }
    if (months > 0) {
        add("$months months")
    }
    if (days > 0) {
        add("$days days")
    }
}.joinToString()
