package dev.tonholo.portfolio.core.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.varabyte.kobweb.compose.css.AlignItems
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.flexDirection
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import dev.tonholo.portfolio.core.extensions.ResponsiveValues
import dev.tonholo.portfolio.core.extensions.responsiveStateOf
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection

val AdaptiveLayoutStyles by ComponentStyle {
    base {
        Modifier
            .display(DisplayStyle.Flex)
            .flexDirection(FlexDirection.Column)
            .alignItems(AlignItems.Center)
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
    val adaptiveLayoutModifier = AdaptiveLayoutStyles.toModifier()
    val responsiveAlignment by responsiveStateOf(ResponsiveValues(base = Alignment.Center, lg = Alignment.TopStart))
    // The Box Composable, differently from the Android implementation
    // will act like a Row or a Column, depending on the breakpoint.
    Box(
        modifier = adaptiveLayoutModifier then modifier,
        contentAlignment = responsiveAlignment,
    ) {
        listPanel()
        detailPanel()
    }
}
