package dev.tonholo.portfolio.core.components.button

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.RowScope
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.silk.components.style.addVariant
import com.varabyte.kobweb.silk.components.style.hover
import dev.tonholo.portfolio.core.foundation.layout.PaddingValues

val TextButtonVariant by ButtonStyles.addVariant {
    base {
        Modifier.backgroundColor(Colors.Transparent)
    }
    hover {
        Modifier.backgroundColor(
            ButtonDefaults.Vars.ContainerColor.value()
        )
    }
}

@Composable
fun TextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        elevation = null,
        contentPadding = contentPadding,
        variant = TextButtonVariant,
        content = content,
    )
}
