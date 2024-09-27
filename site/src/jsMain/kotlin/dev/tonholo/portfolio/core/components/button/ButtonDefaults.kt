package dev.tonholo.portfolio.core.components.button

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.StyleVariable
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.css.VerticalAlign
import com.varabyte.kobweb.compose.css.boxShadow
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.setVariable
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.compose.ui.modifiers.verticalAlign
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.components.forms.ButtonVars
import com.varabyte.kobweb.silk.style.ComponentKind
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.CssStyleVariant
import com.varabyte.kobweb.silk.style.selectors.active
import com.varabyte.kobweb.silk.style.selectors.focus
import com.varabyte.kobweb.silk.style.selectors.hover
import dev.tonholo.portfolio.core.components.button.ButtonDefaults.Vars
import dev.tonholo.portfolio.core.foundation.layout.PaddingValues
import dev.tonholo.portfolio.core.foundation.toBoxShadowString
import dev.tonholo.portfolio.core.ui.theme.Elevation
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.theme.color.Unspecified
import dev.tonholo.portfolio.core.ui.theme.color.copy
import dev.tonholo.portfolio.core.ui.unit.Dp
import dev.tonholo.portfolio.core.ui.unit.dp

typealias ButtonVariant = CssStyleVariant<ButtonKind>

sealed interface ButtonKind : ComponentKind

val ButtonStyle = CssStyle<ButtonKind> {
    base {
        Modifier
            .backgroundColor(Vars.ContainerColor.value())
            .color(Vars.ContentColor.value())
            .styleModifier {
                boxShadow(Vars.DefaultElevation.value())
            }
            .verticalAlign(VerticalAlign.Middle)
            .borderRadius(Vars.BorderRadius.value())
            .border { width(0.dp) }
            .cursor(Vars.Cursor.value())
            .userSelect(UserSelect.None) // No selecting text within buttons
            .transition(Transition.of("background-color", duration = ButtonVars.ColorTransitionDuration.value()))
    }

    hover {
        Modifier
            .styleModifier {
                boxShadow(Vars.HoveredElevation.value())
            }
    }

    focus {
        Modifier
            .styleModifier {
                boxShadow(Vars.FocusedElevation.value())
            }
    }

    active {
        Modifier
            .styleModifier {
                boxShadow(Vars.PressedElevation.value())
            }
    }

    cssRule(":disabled") {
        Modifier
            .backgroundColor(Vars.DisabledContainerColor.value())
            .color(Vars.DisabledContentColor.value())
            .styleModifier {
                boxShadow(Vars.DisabledElevation.value())
            }
    }
}

object ButtonDefaults {
    object Vars {
        val ContainerColor by StyleVariable<Color>("button")
        val ContentColor by StyleVariable<Color>("button")
        val DisabledContainerColor by StyleVariable<Color>("button")
        val DisabledContentColor by StyleVariable<Color>("button")
        val DefaultElevation by StyleVariable<String>("button")
        val PressedElevation by StyleVariable<String>("button")
        val FocusedElevation by StyleVariable<String>("button")
        val HoveredElevation by StyleVariable<String>("button")
        val DisabledElevation by StyleVariable<String>("button")
        val BorderRadius by StyleVariable<Dp>("button")
        val Cursor by StyleVariable<Cursor>("button")
    }

    val ContentPadding = PaddingValues(
        start = 24.dp,
        top = 8.dp,
        end = 24.dp,
        bottom = 8.dp
    )

    val TextButtonContentPadding = PaddingValues(
        vertical = 10.dp,
        horizontal = 24.dp,
    )

    val BorderRadius = 100.dp

    @Composable
    fun buttonColors(
        containerColor: Color = Color.Unspecified,
        contentColor: Color = Color.Unspecified,
        disabledContainerColor: Color = Color.Unspecified,
        disabledContentColor: Color = Color.Unspecified,
    ): ButtonColors = ButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor,
    )

    @Composable
    fun textButtonColors(
        contentColor: Color = Theme.colorScheme.primary,
        disabledContentColor: Color = Theme.colorScheme.onSurface.copy(alpha = 0.5f),
        hoveredContainerColor: Color = Theme.colorScheme.primary.copy(alpha = 0.1f),
    ): ButtonColors = ButtonColors(
        containerColor = hoveredContainerColor,
        contentColor = contentColor,
        disabledContainerColor = Colors.Transparent,
        disabledContentColor = disabledContentColor,
    )

    @Composable
    fun buttonElevation(
        defaultElevation: Elevation = Theme.elevations.level0,
        pressedElevation: Elevation = Theme.elevations.level0,
        focusedElevation: Elevation = Theme.elevations.level0,
        hoveredElevation: Elevation = Theme.elevations.level1,
        disabledElevation: Elevation = Theme.elevations.level0,
    ): ButtonElevation = ButtonElevation(
        defaultElevation = defaultElevation,
        pressedElevation = pressedElevation,
        focusedElevation = focusedElevation,
        hoveredElevation = hoveredElevation,
        disabledElevation = disabledElevation,
    )
}

@Immutable
data class ButtonColors internal constructor(
    private val containerColor: Color,
    private val contentColor: Color,
    private val disabledContainerColor: Color,
    private val disabledContentColor: Color,
) {
    fun toModifier(): Modifier = Modifier
        .setVariable(Vars.ContainerColor, containerColor)
        .setVariable(Vars.ContentColor, contentColor)
        .setVariable(Vars.DisabledContainerColor, disabledContainerColor)
        .setVariable(Vars.DisabledContentColor, disabledContentColor)
}

data class ButtonElevation internal constructor(
    private val defaultElevation: Elevation,
    private val pressedElevation: Elevation,
    private val focusedElevation: Elevation,
    private val hoveredElevation: Elevation,
    private val disabledElevation: Elevation,
) {
    fun toModifier(): Modifier = Modifier
        .setVariable(Vars.DefaultElevation, defaultElevation.shadows.toBoxShadowString())
        .setVariable(Vars.PressedElevation, pressedElevation.shadows.toBoxShadowString())
        .setVariable(Vars.FocusedElevation, focusedElevation.shadows.toBoxShadowString())
        .setVariable(Vars.HoveredElevation, hoveredElevation.shadows.toBoxShadowString())
        .setVariable(Vars.DisabledElevation, disabledElevation.shadows.toBoxShadowString())
}


