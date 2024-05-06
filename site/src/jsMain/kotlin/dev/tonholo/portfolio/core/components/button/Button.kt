package dev.tonholo.portfolio.core.components.button

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.dom.ElementRefScope
import com.varabyte.kobweb.compose.dom.registerRefScope
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.RowScope
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.setVariable
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.style.toModifier
import dev.tonholo.portfolio.core.extensions.padding
import dev.tonholo.portfolio.core.foundation.layout.PaddingValues
import dev.tonholo.portfolio.core.ui.unit.Dp
import org.jetbrains.compose.web.attributes.ButtonType
import org.jetbrains.compose.web.attributes.type
import org.w3c.dom.HTMLButtonElement
import org.jetbrains.compose.web.dom.Button as ComposeButton

@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    borderRadius: Dp = ButtonDefaults.BorderRadius,
    ref: ElementRefScope<HTMLButtonElement>? = null,
    content: @Composable RowScope.() -> Unit
) {
    ComposeButton(
        attrs = ButtonStyles
            .toModifier()
            .onClick { event ->
                onClick()
                event.stopPropagation()
            }
            .setVariable(ButtonDefaults.Vars.BorderRadius, borderRadius)
            .setVariable(ButtonDefaults.Vars.Cursor, if (enabled) Cursor.Pointer else Cursor.NotAllowed)
            .then(colors.toModifier())
            .then(elevation?.toModifier() ?: Modifier)
            .then(modifier)
            .toAttrs {
                if (!enabled) {
                    attr("disabled", enabled.not().toString())
                }
                type(ButtonType.Button)
            }
    ) {
        registerRefScope(ref)

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(contentPadding),
            content = content,
        )
    }
}
