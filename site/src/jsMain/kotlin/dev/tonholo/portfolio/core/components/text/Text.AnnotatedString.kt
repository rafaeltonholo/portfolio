package dev.tonholo.portfolio.core.components.text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.ColumnDefaults
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import dev.tonholo.portfolio.core.ui.text.AnnotatedString
import dev.tonholo.portfolio.core.ui.text.ParagraphStyle
import dev.tonholo.portfolio.core.ui.text.SpanStyle
import dev.tonholo.portfolio.core.ui.text.SubtitleStyle
import dev.tonholo.portfolio.core.ui.text.TitleStyle
import dev.tonholo.portfolio.core.ui.theme.Theme

@Composable
fun Text(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = ColumnDefaults.VerticalArrangement,
    horizontalAlignment: Alignment.Horizontal = ColumnDefaults.HorizontalAlignment,
) {
    Column(
        modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
    ) {
        key(text) {
            text.styles.forEach { range ->
                key(range) {
                    val annotatedText = text.subSequence(range.start, range.end)
                    when (val style = range.item) {
                        is ParagraphStyle -> Paragraph(text = annotatedText.text)
                        is SpanStyle -> Text(text = annotatedText.text)
                        is TitleStyle -> Title(
                            text = annotatedText.text,
                            style = style,
                        )

                        is SubtitleStyle -> Text(
                            text = annotatedText.text,
                            style = Theme.typography.titleSmall,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Title(
    text: String,
    style: TitleStyle,
) {
    Text(
        text = text,
        style = when (style.level) {
            TitleStyle.Level.H1 -> Theme.typography.displayLarge
            TitleStyle.Level.H2 -> Theme.typography.displayMedium
            TitleStyle.Level.H3 -> Theme.typography.displaySmall
            TitleStyle.Level.H4 -> Theme.typography.headlineLarge
            TitleStyle.Level.H5 -> Theme.typography.headlineMedium
            TitleStyle.Level.H6 -> Theme.typography.headlineSmall
        },
    )
}
