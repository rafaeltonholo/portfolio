package dev.tonholo.portfolio.core.components.button

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.StyleVariable
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.lightened
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.setVariable
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.navigation.LinkStyle
import com.varabyte.kobweb.silk.components.style.addVariant
import com.varabyte.kobweb.silk.components.style.hover
import dev.tonholo.portfolio.core.extensions.padding
import dev.tonholo.portfolio.core.ui.theme.colorScheme
import dev.tonholo.portfolio.core.ui.theme.copy
import dev.tonholo.portfolio.core.ui.unit.dp
import org.jetbrains.compose.web.css.LineStyle

object LinkButtonVars {
    val Color by StyleVariable<Color>()
    val HoverColor by StyleVariable<Color>()
    val Background by StyleVariable<Color>()
}

val LinkButtonVariant by LinkStyle.addVariant {
    base {
        Modifier
            .border {
                width(1.dp)
                style(LineStyle.Solid)
                color(LinkButtonVars.Color.value())
            }
            .borderRadius(100.dp)
            .backgroundColor(LinkButtonVars.Background.value())
            .gap(8.dp)
            .color(LinkButtonVars.Color.value())
            .padding(vertical = 10.dp, horizontal = 24.dp)
    }

    hover {
        Modifier
            .setVariable(LinkButtonVars.Background, colorScheme.primary.copy(alpha = 0.1f))
            .textDecorationLine(TextDecorationLine.None)
            .color(LinkButtonVars.HoverColor.value())
            .border {
                color(LinkButtonVars.HoverColor.value())
            }
    }
}

@Composable
fun LinkButton(
    path: String,
    color: Color,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Link(
        path = path,
        modifier = modifier
            .setVariable(LinkButtonVars.Color, color)
            .setVariable(LinkButtonVars.HoverColor, color.lightened()),
        variant = LinkButtonVariant,
        content = content,
    )
}
