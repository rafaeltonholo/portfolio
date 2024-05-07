package dev.tonholo.portfolio.core.foundation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.varabyte.kobweb.compose.css.StyleVariable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.setVariable
import dev.tonholo.portfolio.core.ui.unit.Dp
import dev.tonholo.portfolio.core.ui.unit.dp
import org.jetbrains.compose.web.css.LineStyle

@Stable
@Immutable
data class BorderStroke(
    val width: Dp,
    val color: Color,
    val style: LineStyle = LineStyle.Solid,
    val radius: Dp = 0.dp,
) {
    fun toModifier(): Modifier = Modifier
        .setVariable(BorderStrokeVars.BorderWidth, width)
        .setVariable(BorderStrokeVars.BorderColor, color)
        .setVariable(BorderStrokeVars.BorderStyle, style)
        .setVariable(BorderStrokeVars.BorderRadius, radius)
}

object BorderStrokeVars {
    val BorderWidth by StyleVariable<Dp>()
    val BorderColor by StyleVariable<Color>()
    val BorderStyle by StyleVariable<LineStyle>()
    val BorderRadius by StyleVariable<Dp>()
}
