package dev.tonholo.portfolio.core.foundation.layout

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.StyleVariable
import com.varabyte.kobweb.compose.dom.ElementRefScope
import com.varabyte.kobweb.compose.dom.registerRefScope
import com.varabyte.kobweb.compose.style.toClassNames
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.classNames
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.flexWrap
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.setVariable
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.ComponentKind
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.addVariant
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.core.foundation.ExtendedArrangement
import dev.tonholo.portfolio.core.ui.unit.Dp
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexWrap
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLElement

object FlowRowVars {
    val horizontalSpaceBetween by StyleVariable<Dp>()
    val verticalSpaceBetween by StyleVariable<Dp>()
    val maxItemsInEachRow by StyleVariable<Int>()
}

object FlowRowDefaults {
    val HorizontalArrangement: ExtendedArrangement.Horizontal = ExtendedArrangement.Start
    val VerticalAlignment: ExtendedArrangement.Vertical = ExtendedArrangement.Top
}

sealed interface FlowRowKind : ComponentKind

val FlowRowStyle = CssStyle<FlowRowKind> {
    base {
        Modifier
            .display(DisplayStyle.Flex)
            .flexWrap(FlexWrap.Wrap)
            .gap(
                rowGap = FlowRowVars.verticalSpaceBetween.value(),
                columnGap = FlowRowVars.horizontalSpaceBetween.value(),
            )
    }
}

val FlowRowGridKind = FlowRowStyle.addVariant {
    base {
        Modifier
            .display(DisplayStyle.Grid)
            .gridTemplateColumns {
                repeat(count = FlowRowVars.maxItemsInEachRow.value()) {
                    size(1.fr)
                }
            }
    }
}

@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: ExtendedArrangement.Horizontal = FlowRowDefaults.HorizontalArrangement,
    verticalArrangement: ExtendedArrangement.Vertical = FlowRowDefaults.VerticalAlignment,
    ref: ElementRefScope<HTMLElement>? = null,
    maxItemsInEachRow: Int? = null,
    content: @Composable () -> Unit
) {
    Div(
        attrs = FlowRowStyle
            .toModifier(
                FlowRowGridKind.takeIf { maxItemsInEachRow != null },
            )
            .then(modifier)
            .setVariable(FlowRowVars.horizontalSpaceBetween, horizontalArrangement.spacing)
            .setVariable(FlowRowVars.verticalSpaceBetween, verticalArrangement.spacing)
            // TODO: figure out when no items were provided.
            .setVariable(FlowRowVars.maxItemsInEachRow, maxItemsInEachRow ?: -1)
            .toAttrs {
                classes(
                    "kobweb-row",
                    *verticalArrangement.verticalArrangement.toClassNames(),
                    *horizontalArrangement.horizontalArrangement.toClassNames(),
                )
            },
    ) {
        registerRefScope(ref)
        content()
    }
}
