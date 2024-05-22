package dev.tonholo.portfolio.features.about

import androidx.compose.runtime.Composable
import cafe.adriel.lyricist.LanguageTag
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.TransitionProperty
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.core.components.layout.HorizontalDivider
import dev.tonholo.portfolio.core.extensions.margin
import dev.tonholo.portfolio.core.extensions.padding
import dev.tonholo.portfolio.core.foundation.layout.Scaffold
import dev.tonholo.portfolio.core.sections.AppBar
import dev.tonholo.portfolio.core.sections.Footer
import dev.tonholo.portfolio.core.ui.unit.dp
import dev.tonholo.portfolio.feature.FeatureFlag
import dev.tonholo.portfolio.features.about.sections.MainSection
import dev.tonholo.portfolio.features.about.sections.MoreAboutMeSection
import dev.tonholo.portfolio.resources.pages.AboutPage
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.s

val AboutContentStyle = CssStyle {
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
fun AboutContent(
    about: AboutPage,
    selectedLanguage: LanguageTag,
    modifier: Modifier = Modifier,
    onLocaleChange: (LanguageTag) -> Unit,
    onHomeClick: () -> Unit = {},
    onArticleClick: () -> Unit = {},
    onResumeClick: () -> Unit = {},
) {
    Scaffold(
        modifier = AboutContentStyle.toModifier() then modifier,
        topBar = {
            AppBar(
                selectedLanguage = selectedLanguage,
                onLocaleChange = onLocaleChange,
                onHomeClick = onHomeClick,
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
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            MainSection(
                mainContent = about.main,
            )
            HorizontalDivider(modifier = Modifier.margin(vertical = 80.dp))
            if (FeatureFlag.MoreAboutMe.enabled) {
                MoreAboutMeSection(
                    moreAboutMe = about.moreAboutMe,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
