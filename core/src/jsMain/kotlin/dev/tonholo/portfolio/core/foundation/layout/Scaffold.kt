package dev.tonholo.portfolio.core.foundation.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.css.TransitionProperty
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.core.extensions.ResponsiveValues
import dev.tonholo.portfolio.core.extensions.padding
import dev.tonholo.portfolio.core.extensions.responsiveStateOf
import dev.tonholo.portfolio.core.ui.unit.dp
import kotlinx.browser.document
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.s

val ScaffoldStyle = CssStyle {
    base {
        Modifier
            .fillMaxSize()
            .transition(
                Transition.of(
                    property = TransitionProperty.All,
                    duration = 0.4.s,
                    timingFunction = AnimationTimingFunction.Ease,
                    delay = 0.s,
                )
            )
            .padding(vertical = 20.dp, horizontal = 20.dp)
    }

    Breakpoint.LG {
        Modifier
            .padding(vertical = 40.dp, horizontal = 156.dp)
    }
    cssRule("> *") {
        Modifier.transition(
            Transition.of(
                property = "padding",
                duration = 0.4.s,
                timingFunction = AnimationTimingFunction.Ease,
                delay = 0.s,
            ),
        )
    }
}

@Composable
fun Scaffold(
    modifier: Modifier = Modifier,
    topBar: (@Composable () -> Unit)? = null,
    bottomBar: (@Composable () -> Unit)? = null,
    pageTitle: String? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    LaunchedEffect(pageTitle) {
        if (!pageTitle.isNullOrBlank()){
            document.title = pageTitle
        }
    }

    Column(
        modifier = ScaffoldStyle.toModifier() then modifier,
    ) {
        val padding by responsiveStateOf(ResponsiveValues(base = 40, lg = 80))

        val paddingValues = remember(topBar, bottomBar, padding) {
            PaddingValues(
                top = if (topBar != null) padding.dp else 0.dp,
                bottom = if (bottomBar != null) padding.dp else 0.dp,
            )
        }
        topBar?.invoke()
        content(paddingValues)
        bottomBar?.invoke()
    }
}
