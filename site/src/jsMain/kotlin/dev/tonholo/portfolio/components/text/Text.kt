package dev.tonholo.portfolio.components.text

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.dom.ElementRefScope
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.ComponentVariant
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import dev.tonholo.portfolio.ui.theme.Theme
import dev.tonholo.portfolio.ui.theme.colorScheme
import dev.tonholo.portfolio.ui.theme.typography.LocalTypography
import dev.tonholo.portfolio.ui.theme.typography.TextStyle
import dev.tonholo.portfolio.ui.theme.typography.toModifier
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLSpanElement

val BaseTextStyle by ComponentStyle {
    base {
        Modifier.color(colorScheme.onBackground)
    }
}

@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle? = null,
    variant: ComponentVariant? = null,
    ref: ElementRefScope<HTMLSpanElement>? = null,
) {
    val textModifier =
        BaseTextStyle.toModifier() then (style?.toModifier() ?: LocalTypography.current.bodyMedium.toModifier())

    when (style) {
        Theme.typography.titleLarge -> H1(
            attrs = textModifier.then(modifier).toAttrs(),
        ) {
            Text(value = text)
        }

        Theme.typography.titleMedium -> H2(
            attrs = textModifier.then(modifier).toAttrs(),
        ) {
            Text(value = text)
        }

        Theme.typography.titleSmall -> H3(
            attrs = textModifier.then(modifier).toAttrs(),
        ) {
            Text(value = text)
        }

        Theme.typography.labelMedium -> P(
            attrs = textModifier.then(modifier).toAttrs(),
        ) {
            Text(value = text)
        }

        else -> SpanText(
            text = text,
            modifier = textModifier then modifier,
            variant = variant,
            ref = ref
        )
    }
}
