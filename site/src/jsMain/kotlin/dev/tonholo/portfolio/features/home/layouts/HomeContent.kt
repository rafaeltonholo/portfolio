package dev.tonholo.portfolio.features.home.layouts

import androidx.compose.runtime.Composable
import cafe.adriel.lyricist.LanguageTag
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.TransitionProperty
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.minWidth
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import dev.tonholo.portfolio.breakpoints
import dev.tonholo.portfolio.core.components.AdaptiveLayout
import dev.tonholo.portfolio.core.components.Scaffold
import dev.tonholo.portfolio.core.extensions.padding
import dev.tonholo.portfolio.core.sections.AppBar
import dev.tonholo.portfolio.core.ui.theme.colorScheme
import dev.tonholo.portfolio.features.home.sections.Experiences
import dev.tonholo.portfolio.features.home.sections.Summary
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s

val HomeContentStyle by ComponentStyle {
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

val ListPanel by ComponentStyle {
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
    Breakpoint.MD {
        val breakpointValue = breakpoints.md.width.value
        Modifier
            .maxWidth((breakpointValue / 2).px)
            .minWidth((breakpointValue / 4).px)
    }
    Breakpoint.LG {
        Modifier
            .maxWidth(400.px)
            .minWidth(200.px)
    }
    Breakpoint.XL {
        val breakpointValue = breakpoints.xl.width.value
        Modifier
            .maxWidth((breakpointValue / 2).px)
            .minWidth((breakpointValue / 4).px)
    }
}

val DetailPanel by ComponentStyle {
    val gap = 1.em
    base {
        Modifier
            .padding(vertical = 2.em, horizontal = 1.5.em)
            .margin { top(gap) }
            .transition(
                CSSTransition(
                    property = TransitionProperty.All,
                    duration = 0.4.s,
                    timingFunction = AnimationTimingFunction.Ease,
                    delay = 0.s,
                )
            )
            .background(color = colorScheme.surface)
            .fillMaxWidth()
    }
    Breakpoint.MD {
        Modifier.margin {
            top(0.px)
            left(gap)
        }
    }
    Breakpoint.LG {
        Modifier.margin {
            top(0.px)
            left(gap)
        }
    }
    Breakpoint.XL {
        Modifier.margin {
            top(0.px)
            left(gap)
        }
    }
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    onLocaleChange: (LanguageTag) -> Unit,
    onHomeClick: () -> Unit = {},
    onArticleClick: () -> Unit = {},
    onResumeClick: () -> Unit = {},
) {
    Scaffold(
        modifier = HomeContentStyle.toModifier() then modifier,
        topBar = {
            AppBar(
                onLocaleChange = onLocaleChange,
                onHomeClick = onHomeClick,
                onArticleClick = onArticleClick,
                onResumeClick = onResumeClick,
            )
        }
    ) { paddingValues ->
        AdaptiveLayout(
            listPanel = {
                Column(modifier = ListPanel.toModifier()) {
                    Summary()
                }
            },
            detailPanel = {
                Column(modifier = DetailPanel.toModifier()) {
                    Experiences()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
        )
    }
}
