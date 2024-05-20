package dev.tonholo.portfolio.core.foundation.layout

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.StyleVariable
import com.varabyte.kobweb.compose.dom.ElementRefScope
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.RowScope
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.setVariable
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.core.foundation.ExtendedArrangement
import dev.tonholo.portfolio.core.ui.unit.Dp
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.fr
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

val FlowRowStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Grid)
            .then(
                FlowRowVars.maxItemsInEachRow.value().let { value ->
                    Modifier.gridTemplateColumns {
                        repeat(count = value) {
                            size(1.fr)
                        }
                    }
                }
            )
            .gap(
                rowGap = FlowRowVars.verticalSpaceBetween.value(),
                columnGap = FlowRowVars.horizontalSpaceBetween.value(),
            )
    }
}

@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: ExtendedArrangement.Horizontal = FlowRowDefaults.HorizontalArrangement,
    verticalArrangement: ExtendedArrangement.Vertical = FlowRowDefaults.VerticalAlignment,
    ref: ElementRefScope<HTMLElement>? = null,
    maxItemsInEachRow: Int? = null,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = FlowRowStyle.toModifier() then modifier
            .setVariable(FlowRowVars.horizontalSpaceBetween, horizontalArrangement.spacing)
            .setVariable(FlowRowVars.verticalSpaceBetween, verticalArrangement.spacing)
            // TODO: figure out when no items were provided.
            .setVariable(FlowRowVars.maxItemsInEachRow, maxItemsInEachRow ?: -1),
        horizontalArrangement = horizontalArrangement.horizontalArrangement,
        ref = ref,
        content = content,
    )
}
