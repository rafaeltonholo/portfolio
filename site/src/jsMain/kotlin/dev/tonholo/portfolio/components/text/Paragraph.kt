package dev.tonholo.portfolio.components.text

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.tonholo.portfolio.ui.theme.typography.LocalTypography
import dev.tonholo.portfolio.ui.theme.typography.TextStyle
import dev.tonholo.portfolio.ui.theme.typography.toModifier
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

@Composable
fun Paragraph(
    text: String,
    style: TextStyle? = null,
    modifier: Modifier = Modifier,
) {
    val paragraphModifier = (style?.toModifier() ?: LocalTypography.current.bodyMedium.toModifier())
    P(attrs = (paragraphModifier then modifier).toAttrs()) {
        Text(value = text)
    }
}
