package dev.tonholo.portfolio.core.components.layout

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.StyleVariable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.borderTop
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.setVariable
import com.varabyte.kobweb.silk.components.layout.HorizontalDivider
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.unit.Dp
import dev.tonholo.portfolio.core.ui.unit.dp
import org.jetbrains.compose.web.css.LineStyle

object HorizontalDividerVars {
    val Style by StyleVariable(DividerDefaults.Style)
    val Thickness by StyleVariable(DividerDefaults.Thickness)
    val Color by StyleVariable<Color>()
    val Radius by StyleVariable(0.dp)
}

val HorizontalDividerStyle = CssStyle {
    base {
        Modifier
            .fillMaxWidth()
            .borderTop(
                HorizontalDividerVars.Thickness.value(),
                HorizontalDividerVars.Style.value(),
                HorizontalDividerVars.Color.value(),
            )
            .borderRadius(HorizontalDividerVars.Radius.value())
    }
}

@Composable
fun HorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = DividerDefaults.Thickness,
    color: Color = DividerDefaults.Color,
    radius: Dp = DividerDefaults.Radius,
    style: LineStyle = DividerDefaults.Style,
) {
    HorizontalDivider(
        modifier = HorizontalDividerStyle.toModifier()
            .then(modifier)
            .setVariable(HorizontalDividerVars.Thickness, thickness)
            .setVariable(HorizontalDividerVars.Color, color)
            .setVariable(HorizontalDividerVars.Radius, radius)
            .setVariable(HorizontalDividerVars.Style, style),
    )
}

/** Default values for [HorizontalDivider] */
object DividerDefaults {
    /** Default thickness of a divider. */
    val Thickness: Dp = 1.dp

    /** Default color of a divider. */
    val Color: Color @Composable get() = Theme.colorScheme.outline

    val Style: LineStyle get() = LineStyle.Solid
    val Radius: Dp get() = 0.dp
}
