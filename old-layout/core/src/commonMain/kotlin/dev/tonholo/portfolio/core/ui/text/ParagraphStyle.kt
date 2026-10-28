package dev.tonholo.portfolio.core.ui.text

import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.classNames
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.styleModifier
import dev.tonholo.portfolio.core.extensions.padding
import dev.tonholo.portfolio.core.foundation.layout.PaddingValues
import dev.tonholo.portfolio.core.ui.text.AnnotatedString.Builder
import dev.tonholo.portfolio.core.ui.text.AnnotatedString.Style
import dev.tonholo.portfolio.core.ui.unit.TextIndent
import dev.tonholo.portfolio.core.ui.unit.TextUnit

data class ParagraphStyle(
    val tag: String = "",
    val textAlign: TextAlign = TextAlign.Start,
    val lineHeight: TextUnit? = null,
    val textIndent: TextIndent? = null,
    val paddingValues: PaddingValues? = null,
) : Style

fun Builder.paragraph(
    text: String,
    tag: String = "",
    textAlign: TextAlign = TextAlign.Start,
    lineHeight: TextUnit? = null,
    textIndent: TextIndent = TextIndent(),
    paddingValues: PaddingValues? = null,
) {
    paragraph(tag, textAlign, lineHeight, textIndent, paddingValues) {
        append(text)
    }
}

fun Builder.paragraph(
    tag: String = "",
    textAlign: TextAlign = TextAlign.Start,
    lineHeight: TextUnit? = null,
    textIndent: TextIndent = TextIndent(),
    paddingValues: PaddingValues? = null,
    block: Builder.() -> Unit,
) {
    withStyle(style = ParagraphStyle(tag, textAlign, lineHeight, textIndent, paddingValues), block)
}

fun Modifier.paragraphStyle(style: ParagraphStyle): Modifier = this then Modifier
    .then(
        style
            .tag
            .takeIf { it.isNotBlank() }
            ?.let { Modifier.classNames(it) }
            ?: Modifier,
    )
    .textAlign(style.textAlign)
    .then(style.lineHeight?.let { Modifier.lineHeight(it) } ?: Modifier)
    .then(style.paddingValues?.let { Modifier.padding(it) } ?: Modifier)
    .then(
        style
            .textIndent
            ?.let {
                Modifier.styleModifier {
                    property("text-indent", it.firstLine)
                }
            }
            ?: Modifier,
    )
