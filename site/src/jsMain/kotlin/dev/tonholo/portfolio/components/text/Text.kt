package dev.tonholo.portfolio.components.text

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.dom.ElementRefScope
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.ComponentVariant
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.text.SpanText
import dev.tonholo.portfolio.ui.theme.Theme
import dev.tonholo.portfolio.ui.theme.typography
import dev.tonholo.portfolio.ui.theme.typography.TextStyle
import dev.tonholo.portfolio.ui.theme.typography.toModifier
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.H4
import org.jetbrains.compose.web.dom.H5
import org.jetbrains.compose.web.dom.H6
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLSpanElement

val BaseTextStyle by ComponentStyle {
    base {
        typography.bodyLarge.toModifier()
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
        BaseTextStyle.toModifier() then (style?.toModifier() ?: Modifier)

    when (style) {
        Theme.typography.headlineLarge -> H1(
            attrs = textModifier.then(modifier).toAttrs(),
        ) {
            Text(value = text)
        }

        Theme.typography.headlineMedium -> H2(
            attrs = textModifier.then(modifier).toAttrs(),
        ) {
            Text(value = text)
        }

        Theme.typography.headlineSmall -> H3(
            attrs = textModifier.then(modifier).toAttrs(),
        ) {
            Text(value = text)
        }

        Theme.typography.titleLarge -> H4(
            attrs = textModifier.then(modifier).toAttrs(),
        ) {
            Text(value = text)
        }

        Theme.typography.titleMedium -> H5(
            attrs = textModifier.then(modifier).toAttrs(),
        ) {
            Text(value = text)
        }

        Theme.typography.titleSmall -> H6(
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
