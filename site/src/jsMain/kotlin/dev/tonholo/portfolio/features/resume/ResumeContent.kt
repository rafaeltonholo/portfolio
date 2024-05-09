package dev.tonholo.portfolio.features.resume

import androidx.compose.runtime.Composable
import cafe.adriel.lyricist.LanguageTag
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.TransitionProperty
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flex
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.toModifier
import dev.tonholo.portfolio.core.components.AdaptiveLayout
import dev.tonholo.portfolio.core.components.SocialMediaRow
import dev.tonholo.portfolio.core.components.button.PrimaryButton
import dev.tonholo.portfolio.core.foundation.layout.Scaffold
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.extensions.padding
import dev.tonholo.portfolio.core.sections.AppBar
import dev.tonholo.portfolio.core.sections.Footer
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.unit.dp
import dev.tonholo.portfolio.features.resume.sections.EducationSection
import dev.tonholo.portfolio.features.resume.sections.ExperienceSection
import dev.tonholo.portfolio.features.resume.sections.SkillsSection
import dev.tonholo.portfolio.resources.pages.ResumePage
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.s

val ResumeContentStyles by ComponentStyle {
    base {
        Modifier
            .transition(
                CSSTransition(
                    property = TransitionProperty.All,
                    duration = 0.4.s,
                    timingFunction = AnimationTimingFunction.Ease,
                    delay = 0.s,
                )
            )
    }
}

val ResumeContainerStyles by ComponentStyle {
    base {
        Modifier
            .gap(32.dp)
            .fillMaxSize()
    }
}

val ResumeAdaptiveContainerStyles by ComponentStyle {
    base {
        Modifier
            .fillMaxWidth()
            .gap(120.dp)
    }
}

@Composable
fun ResumeContent(
    resume: ResumePage,
    selectedLanguage: LanguageTag,
    modifier: Modifier = Modifier,
    onLocaleChange: (LanguageTag) -> Unit,
    onHomeClick: () -> Unit = {},
    onAboutClick: () -> Unit = {},
    onArticleClick: () -> Unit = {},
) {
    Scaffold(
        modifier = ResumeContentStyles.toModifier() then modifier,
        topBar = {
            AppBar(
                selectedLanguage = selectedLanguage,
                onLocaleChange = onLocaleChange,
                onHomeClick = onHomeClick,
                onAboutClick = onAboutClick,
                onArticleClick = onArticleClick,
            )
        },
        bottomBar = {
            Footer(
                modifier = Modifier.fillMaxWidth(),
            )
        }
    ) { paddingValues ->
        Column(
            modifier = ResumeContainerStyles.toModifier()
                .padding(paddingValues),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = resume.title,
                    style = Theme.typography.displaySmall,
                )
                PrimaryButton(
                    onClick = {
                        println("testing")
                    },
                    enabled = true,
                ) {
                    Text(
                        text = resume.viewResumePdf,
                        style = Theme.typography.labelLarge,
                    )
                }
            }
            SocialMediaRow()
            AdaptiveLayout(
                listPanel = {
                    ExperienceSection(
                        section = resume.experience,
                        modifier = Modifier.flex(1),
                    )
                },
                detailPanel = {
                    Column(
                        modifier = Modifier
                            .flex(1)
                            .gap(56.dp),

                    ) {
                        EducationSection(
                            section = resume.educationSection,
                        )
                        SkillsSection(
                            section = resume.skillSection,
                        )
                    }
                },
                modifier = ResumeAdaptiveContainerStyles.toModifier(),
            )
        }
    }
}

