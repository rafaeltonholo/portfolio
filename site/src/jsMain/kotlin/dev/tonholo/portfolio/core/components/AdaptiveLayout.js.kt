package dev.tonholo.portfolio.core.components

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.AlignItems
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.flexDirection
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection

val AdaptiveLayoutStyles by ComponentStyle {
    base {
        Modifier
            .display(DisplayStyle.Flex)
            .flexDirection(FlexDirection.Column)
            .alignItems(AlignItems.Center)
    }

    Breakpoint.MD {
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
    // The Box Composable, differently from the Android implementation
    // will act like a Row or a Column, depending on the breakpoint.
    Box(
        modifier = adaptiveLayoutModifier then modifier,
    ) {
        listPanel()
        detailPanel()
    }
}
