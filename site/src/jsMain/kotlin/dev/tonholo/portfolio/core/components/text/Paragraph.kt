package dev.tonholo.portfolio.core.components.text

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.toModifier
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.theme.typography
import dev.tonholo.portfolio.core.ui.theme.typography.TextStyle
import dev.tonholo.portfolio.core.ui.theme.typography.toModifier
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

val ParagraphStyle by ComponentStyle {
    base {
        typography.bodyLarge.toModifier()
    }
}

@Composable
fun Paragraph(
    text: String,
    style: TextStyle? = null,
    modifier: Modifier = Modifier,
) {
    val paragraphModifier = ParagraphStyle.toModifier().thenIf(style != null) {
        style?.toModifier(Theme.typography.bodyLarge) ?: Modifier
    }
    P(attrs = (paragraphModifier then modifier).toAttrs()) {
        Text(value = text)
    }
}
