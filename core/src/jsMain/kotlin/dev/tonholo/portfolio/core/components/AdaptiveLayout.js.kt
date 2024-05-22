package dev.tonholo.portfolio.core.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.flexDirection
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.core.extensions.ResponsiveValues
import dev.tonholo.portfolio.core.extensions.responsiveStateOf
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection

val AdaptiveLayoutStyles = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Flex)
            .flexDirection(FlexDirection.Column)
    }

    Breakpoint.LG {
        Modifier
            .flexDirection(FlexDirection.Row)
    }
}

@Composable
actual fun AdaptiveLayout(
    modifier: Modifier,
    listPanel: @Composable () -> Unit,
    detailPanel: @Composable () -> Unit,
) {
    val adaptiveLayoutModifier = AdaptiveLayoutStyles.toModifier() then modifier
    val useColumn by responsiveStateOf(ResponsiveValues(base = true, lg = false))
    val content = remember {
        movableContentOf {
            listPanel()
            detailPanel()
        }
    }
    if (useColumn) {
        Column(
            modifier = adaptiveLayoutModifier,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            content()
        }
    } else {
        Row(
            modifier = adaptiveLayoutModifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            content()
        }
    }
}
