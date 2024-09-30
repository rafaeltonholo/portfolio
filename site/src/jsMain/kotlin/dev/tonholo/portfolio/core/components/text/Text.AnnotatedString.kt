package dev.tonholo.portfolio.core.components.text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.ColumnDefaults
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontStyle
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.navigation.Link
import dev.tonholo.portfolio.core.components.button.TextButton
import dev.tonholo.portfolio.core.ui.text.AnnotatedString
import dev.tonholo.portfolio.core.ui.text.LinkAnnotation
import dev.tonholo.portfolio.core.ui.text.ParagraphStyle
import dev.tonholo.portfolio.core.ui.text.SpanStyle
import dev.tonholo.portfolio.core.ui.text.SubtitleStyle
import dev.tonholo.portfolio.core.ui.text.TitleStyle
import dev.tonholo.portfolio.core.ui.text.spanStyle
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.theme.color.Unspecified
import dev.tonholo.portfolio.core.ui.unit.TextUnit

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
        key(text.text) {
            val styles = remember(text) { text.calculatedNestedStyles() }
            SideEffect {
                console.log("styles", styles)
            }
            styles.forEach { (parentRange, nestedRanges) ->
                key(parentRange) {
                    when {
                        nestedRanges.isEmpty() -> RangeRenderer(text = text, range = parentRange)
                        else -> NestedRangeRenderer(parentRange, nestedRanges, text)
                    }
                }
            }
        }
    }
}

@Composable
private fun NestedRangeRenderer(
    parentRange: AnnotatedString.Range<out AnnotatedString.Annotation>,
    nestedRanges: List<AnnotatedString.Range<out AnnotatedString.Annotation>>,
    text: AnnotatedString
) {
    when (parentRange.item) {
        is ParagraphStyle -> Paragraph {
            var startRange by remember { mutableIntStateOf(parentRange.start) }
            for (nestedRange in nestedRanges) {
                when {
                    nestedRange.start > startRange -> {
                        Text(
                            text = text.subSequence(startRange, nestedRange.start).text,
                            disablePreWrap = true,
                        )
                        startRange = nestedRange.end
                        RangeRenderer(text, nestedRange)
                    }

                    nestedRange.start == startRange -> {
                        RangeRenderer(text, nestedRange)
                        startRange = nestedRange.end
                    }
                }
            }
            if (startRange < parentRange.end) {
                Text(
                    text = text.subSequence(startRange, parentRange.end).text,
                    disablePreWrap = true,
                )
            }
        }

        else -> Unit
    }
}

@Composable
private fun RangeRenderer(
    text: AnnotatedString,
    range: AnnotatedString.Range<out AnnotatedString.Annotation>,
) {
    key(range) {
        val annotatedText = remember(text, range) { text.subSequence(range.start, range.end) }
        when (val annotation = range.item) {
            is ParagraphStyle -> Paragraph(text = annotatedText.text)
            is SpanStyle -> Text(
                text = annotatedText.text,
                modifier = Modifier.spanStyle(annotation),
                disablePreWrap = true,
            )

            is TitleStyle -> Title(
                text = annotatedText.text,
                style = annotation,
            )

            is SubtitleStyle -> Text(
                text = annotatedText.text,
                style = Theme.typography.titleSmall,
                disablePreWrap = true,
            )

            is LinkAnnotation.Url -> Link(
                path = annotation.url,
                text = annotatedText.text,
                modifier = Modifier.thenIf(annotation.styles?.style != null) {
                    Modifier.spanStyle(requireNotNull(annotation.styles?.style))
                }
            )

            is LinkAnnotation.Clickable -> TextButton(
                onClick = { annotation.linkInteractionListener?.onClick(annotation) }
            ) {
                Text(text = annotatedText.text)
            }

            is LinkAnnotation -> Text(text = annotatedText.text)
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
