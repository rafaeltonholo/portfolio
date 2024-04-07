package dev.tonholo.portfolio.components.text

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.dom.ElementRefScope
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.silk.components.style.ComponentVariant
import com.varabyte.kobweb.silk.components.text.SpanText
import dev.tonholo.portfolio.ui.theme.typography.LocalTypography
import dev.tonholo.portfolio.ui.theme.typography.TextStyle
import dev.tonholo.portfolio.ui.theme.typography.toModifier
import org.w3c.dom.HTMLSpanElement

@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle? = null,
    variant: ComponentVariant? = null,
    ref: ElementRefScope<HTMLSpanElement>? = null,
) {
    val textModifier = style?.toModifier() ?: LocalTypography.current.bodyMedium.toModifier()
    SpanText(
        text = text,
        modifier = textModifier then modifier,
        variant = variant,
        ref = ref
    )
}
