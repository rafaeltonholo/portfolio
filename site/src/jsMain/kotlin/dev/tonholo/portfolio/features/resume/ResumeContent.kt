package dev.tonholo.portfolio.features.resume

import androidx.compose.runtime.Composable
import cafe.adriel.lyricist.LanguageTag
import com.varabyte.kobweb.compose.css.Transition
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
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.core.analytics.LocalAnalyticsManager
import dev.tonholo.portfolio.core.analytics.events.AnalyticEvent
import dev.tonholo.portfolio.core.components.AdaptiveLayout
import dev.tonholo.portfolio.core.components.SocialMediaRow
import dev.tonholo.portfolio.core.components.button.PrimaryButton
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.extensions.padding
import dev.tonholo.portfolio.core.foundation.layout.Scaffold
import dev.tonholo.portfolio.core.sections.AppBar
import dev.tonholo.portfolio.core.sections.Footer
import dev.tonholo.portfolio.core.ui.theme.LocalLyricist
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.unit.dp
import dev.tonholo.portfolio.features.resume.sections.EducationSection
import dev.tonholo.portfolio.features.resume.sections.ExperienceSection
import dev.tonholo.portfolio.features.resume.sections.SkillsSection
import dev.tonholo.portfolio.resources.pages.ResumePage
import kotlinx.browser.window
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.s

val ResumeContentStyle = CssStyle {
    base {
        Modifier.transition(
            Transition.of(
                property = TransitionProperty.All,
                duration = 0.4.s,
                timingFunction = AnimationTimingFunction.Ease,
                delay = 0.s,
            )
        )
    }
}

val ResumeContainerStyle = CssStyle {
    base {
        Modifier
            .gap(32.dp)
            .fillMaxSize()
    }
}

val ResumeAdaptiveContainerStyle = CssStyle {
    base {
        Modifier
            .transition(
                Transition.of(
                    property = TransitionProperty.All,
                    duration = 0.4.s,
                    timingFunction = AnimationTimingFunction.Ease,
                    delay = 0.s,
                )
            )
            .fillMaxWidth()
            .gap(40.dp)
    }
    Breakpoint.MD {
        Modifier.gap(80.dp)
    }
    Breakpoint.LG {
        Modifier.gap(120.dp)
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
    val analytics = LocalAnalyticsManager.current
    val lyricist = LocalLyricist.current
    Scaffold(
        modifier = ResumeContentStyle.toModifier() then modifier,
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
        },
        pageTitle = resume.title,
    ) { paddingValues ->
        Column(
            modifier = ResumeContainerStyle.toModifier()
                .padding(paddingValues),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = resume.resumeTitle,
                    style = Theme.typography.displaySmall,
                )
                PrimaryButton(
                    onClick = {
                        analytics.track(
                            event = AnalyticEvent.ViewResumePdf(
                                language = lyricist.languageTag,
                            ),
                        )
                        window.open(
                            url = "https://drive.google.com/file/d/1u4G0lVEI45S-6wCSSI65N200UFqKCXnM/view?usp=sharing",
                            target = "_blank",
                        )
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
                        modifier = Modifier
                            .flex(1)
                            .fillMaxWidth(),
                    )
                },
                detailPanel = {
                    Column(
                        modifier = Modifier
                            .flex(1)
                            .gap(56.dp)
                            .fillMaxSize(),

                        ) {
                        EducationSection(
                            section = resume.educationSection,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        SkillsSection(
                            section = resume.skillSection,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                },
                modifier = ResumeAdaptiveContainerStyle.toModifier(),
            )
        }
    }
}

