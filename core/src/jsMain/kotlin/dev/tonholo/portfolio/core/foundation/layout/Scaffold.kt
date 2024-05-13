package dev.tonholo.portfolio.core.foundation.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.TransitionProperty
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import dev.tonholo.portfolio.core.extensions.padding
import dev.tonholo.portfolio.core.ui.unit.dp
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.s

val ScaffoldStyle by ComponentStyle {
    base {
        Modifier
            .fillMaxSize()
            .transition(
                CSSTransition(
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
}

@Composable
fun Scaffold(
    modifier: Modifier = Modifier,
    topBar: (@Composable () -> Unit)? = null,
    bottomBar: (@Composable () -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    Column(
        modifier = ScaffoldStyle.toModifier() then modifier,
    ) {
        val paddingValues = remember(topBar, bottomBar) {
            PaddingValues(
                top = if (topBar != null) 80.dp else 0.dp,
                bottom = if (bottomBar != null) 80.dp else 0.dp,
            )
        }
        topBar?.invoke()
        content(paddingValues)
        bottomBar?.invoke()
    }
}
