package dev.tonholo.portfolio.core.components.chip

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.varabyte.kobweb.compose.css.StyleVariable
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.RowScope
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.setVariable
import com.varabyte.kobweb.silk.components.layout.Surface
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.core.extensions.padding
import dev.tonholo.portfolio.core.foundation.BorderStroke
import dev.tonholo.portfolio.core.foundation.BorderStrokeVars
import dev.tonholo.portfolio.core.foundation.layout.PaddingValues
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.theme.color.copy
import dev.tonholo.portfolio.core.ui.unit.Dp
import dev.tonholo.portfolio.core.ui.unit.dp
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.LineStyle

val ChipStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.InlineBlock)
            .borderRadius(BorderStrokeVars.BorderRadius.value())
            .border {
                width(BorderStrokeVars.BorderWidth.value())
                style(BorderStrokeVars.BorderStyle.value())
                color(BorderStrokeVars.BorderColor.value())
            }
    }
}

val ChipContentContainerStyle = CssStyle {
    base {
        Modifier
            .color(ChipDefaults.Vars.ContentColor.value())
            .backgroundColor(ChipDefaults.Vars.ContainerColor.value())
    }
}

@Composable
fun Chip(
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    colors: ChipColors = ChipDefaults.colors(),
    border: BorderStroke? = ChipDefaults.border(selected, enabled),
    contentPadding: PaddingValues = ChipDefaults.ContentPadding,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,
) {
    Surface(
        modifier = ChipStyle.toModifier() then modifier
            .then(border?.toModifier() ?: Modifier)
            .then(colors.toModifier())
            .onClick {
                if (enabled) {
                    onClick()
                }
            },
    ) {
        Row(
            modifier = ChipContentContainerStyle.toModifier() then Modifier
                .padding(contentPadding),
        ) {
            if (leadingIcon != null) {
                leadingIcon()
            }
            content()
            if (trailingIcon != null) {
                trailingIcon()
            }
        }
    }
}

object ChipDefaults {
    object Vars {
        val ContainerColor by StyleVariable<Color>("chip")
        val ContentColor by StyleVariable<Color>("chip")
        val DisabledContainerColor by StyleVariable<Color>("chip")
        val DisabledContentColor by StyleVariable<Color>("chip")
    }

    val ContentPadding = PaddingValues(
        vertical = 6.dp,
        horizontal = 16.dp,
    )

    @Composable
    fun colors(
        containerColor: Color = Colors.Transparent,
        contentColor: Color = Theme.colorScheme.outline,
        disabledContainerColor: Color = Colors.Transparent,
        disabledContentColor: Color = Theme.colorScheme.outline,
    ): ChipColors = ChipColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor,
    )

    @Composable
    fun border(
        selected: Boolean,
        enabled: Boolean,
        borderColor: Color = Theme.colorScheme.outline,
        selectedBorderColor: Color = Colors.Transparent,
        disabledBorderColor: Color = Theme.colorScheme.onSurface.copy(alpha = 0.12f),
        disabledSelectedBorderColor: Color = Colors.Transparent,
        borderWidth: Dp = 1.dp,
        selectedBorderWidth: Dp = 1.dp,
        style: LineStyle = LineStyle.Solid,
        radius: Dp = 8.dp,
    ): BorderStroke {
        val color = if (enabled) {
            if (selected) selectedBorderColor else borderColor
        } else {
            if (selected) disabledSelectedBorderColor else disabledBorderColor
        }
        return BorderStroke(
            width = if (selected) selectedBorderWidth else borderWidth,
            color = color,
            style = style,
            radius = radius,
        )
    }
}

@Immutable
@ConsistentCopyVisibility
data class ChipColors internal constructor(
    private val containerColor: Color,
    private val contentColor: Color,
    private val disabledContainerColor: Color,
    private val disabledContentColor: Color,
) {
    fun toModifier(): Modifier = Modifier
        .setVariable(ChipDefaults.Vars.ContainerColor, containerColor)
        .setVariable(ChipDefaults.Vars.ContentColor, contentColor)
        .setVariable(ChipDefaults.Vars.DisabledContainerColor, disabledContainerColor)
        .setVariable(ChipDefaults.Vars.DisabledContentColor, disabledContentColor)
}
