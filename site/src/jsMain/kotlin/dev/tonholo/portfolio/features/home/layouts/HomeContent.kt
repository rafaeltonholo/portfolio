package dev.tonholo.portfolio.features.home.layouts

import androidx.compose.runtime.Composable
import cafe.adriel.lyricist.LanguageTag
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.TransitionProperty
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.minWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import dev.tonholo.portfolio.breakpoints
import dev.tonholo.portfolio.components.AdaptiveLayout
import dev.tonholo.portfolio.components.text.Text
import dev.tonholo.portfolio.features.home.components.LanguageChanger
import dev.tonholo.portfolio.features.home.sections.AppBar
import dev.tonholo.portfolio.features.home.sections.Summary
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.css.vw

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

    Breakpoint.MD {
        Modifier
            .height(90.vh)
            .padding(topBottom = 5.vh, leftRight = 3.vw)
    }
}

val ListPanel by ComponentStyle {
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
            .width(100.percent)
            .margin { top(gap) }
            .transition(
                CSSTransition(
                    property = TransitionProperty.All,
                    duration = 0.4.s,
                    timingFunction = AnimationTimingFunction.Ease,
                    delay = 0.s,
                )
            )
    }
    Breakpoint.MD { Modifier.margin { left(gap) } }
    Breakpoint.LG { Modifier.margin { left(gap) } }
    Breakpoint.XL { Modifier.margin { left(gap) } }
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    onLocaleChange: (LanguageTag) -> Unit,
) {
    Column(modifier = HomeContentStyle.toModifier() then modifier) {
        LanguageChanger(onLocaleChange = onLocaleChange)
        AppBar()
        AdaptiveLayout(
            listPanel = {
                Column(modifier = ListPanel.toModifier()) {
                    Summary()
                }
            },
            detailPanel = {
                Column(modifier = DetailPanel.toModifier()) {
                    Text("That is a test.")
                }
            },
        )
    }
}
