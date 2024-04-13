package dev.tonholo.portfolio.components

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.toModifier
import dev.tonholo.portfolio.components.text.Text
import dev.tonholo.portfolio.extensions.padding
import dev.tonholo.portfolio.ui.theme.Theme
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px

val TextTagStyle by ComponentStyle {
    base {
        Modifier
            .display(DisplayStyle.InlineBlock)
            .padding(vertical = 0.15.em, horizontal = 0.3.em)
            .borderRadius(3.px)
            .border {
                width(1.px)
                style(LineStyle.Solid)
            }
    }
}

@Composable
fun TextTag(
    text: String,
    modifier: Modifier = Modifier,
    color: Color? = null,
) {
    Box(
        modifier = TextTagStyle.toModifier() then modifier,
    ) {
        Text(
            text = text,
            style = Theme.typography.labelSmall,
            modifier = if (color != null) Modifier.color(color) else Modifier,
        )
    }
}
