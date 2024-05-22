package dev.tonholo.portfolio.features.home.layouts

import androidx.compose.runtime.Composable
import cafe.adriel.lyricist.LanguageTag
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.TransitionProperty
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.core.extensions.padding
import dev.tonholo.portfolio.core.foundation.layout.Scaffold
import dev.tonholo.portfolio.core.sections.AppBar
import dev.tonholo.portfolio.core.sections.Footer
import dev.tonholo.portfolio.core.ui.unit.dp
import dev.tonholo.portfolio.features.home.sections.RecentProjectsSection
import dev.tonholo.portfolio.features.home.sections.WelcomeSection
import dev.tonholo.portfolio.features.home.sections.WhatIDoSection
import dev.tonholo.portfolio.resources.pages.Home
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.s

val HomeContentStyle = CssStyle {
    base {
        Modifier.transition(
            CSSTransition(
                property = TransitionProperty.All,
                duration = 0.4.s,
                timingFunction = AnimationTimingFunction.Ease,
                delay = 0.s,
            )
        )
    }
}

@Composable
fun HomeContent(
    home: Home,
    selectedLanguage: LanguageTag,
    modifier: Modifier = Modifier,
    onLocaleChange: (LanguageTag) -> Unit,
    onHomeClick: () -> Unit = {},
    onAboutClick: () -> Unit = {},
    onArticleClick: () -> Unit = {},
    onResumeClick: () -> Unit = {},
) {
    Scaffold(
        modifier = HomeContentStyle.toModifier() then modifier,
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
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            WelcomeSection(
                welcomeSection = home.welcome,
            )
            WhatIDoSection(
                about = home.about,
                modifier = Modifier
                    .fillMaxWidth()
                    .margin { top(80.dp) },
            )
            RecentProjectsSection(
                recentProjects = home.recentProjects,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
