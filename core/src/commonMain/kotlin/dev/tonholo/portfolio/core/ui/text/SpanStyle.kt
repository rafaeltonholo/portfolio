package dev.tonholo.portfolio.core.ui.text

import androidx.compose.ui.text.font.FontSynthesis
import com.varabyte.kobweb.compose.css.FontStyle
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.ui.Modifier
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
import dev.tonholo.portfolio.core.ui.text.AnnotatedString.Builder
import dev.tonholo.portfolio.core.ui.text.AnnotatedString.Style
import dev.tonholo.portfolio.core.ui.theme.color.Unspecified
import dev.tonholo.portfolio.core.ui.theme.typography.Font
import dev.tonholo.portfolio.core.ui.unit.TextUnit

data class SpanStyle(
    val fontSize: TextUnit = TextUnit.Unspecified,
    val fontWeight: FontWeight? = null,
    val fontStyle: FontStyle? = null,
    val fontSynthesis: FontSynthesis? = null,
    val fontFamily: Font? = null,
    val fontFeatureSettings: String? = null,
    val letterSpacing: TextUnit = TextUnit.Unspecified,
    val background: Color = Color.Unspecified,
    val textDecoration: TextDecorationLine? = null,
) : Style

fun Builder.bold(text: String) {
    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
        append(text)
    }
}

fun Modifier.spanStyle(style: SpanStyle): Modifier = this then Modifier
    .then(
        style
            .fontSize
            .takeIf { it != TextUnit.Unspecified }
            ?.let { Modifier.fontSize(it) } ?: Modifier,
    )
    .then(style.fontWeight?.let { Modifier.fontWeight(it) } ?: Modifier)
    .then(style.fontStyle?.let { Modifier.fontStyle(it) } ?: Modifier)
//    .then(style.fontSynthesis?.let { Modifier.fontSynthesis(it) } ?: Modifier)
    .then(style.fontFamily?.let { Modifier.fontFamily(it.names.joinToString()) } ?: Modifier)
    .then(
        style
            .fontFeatureSettings
            ?.let {
                Modifier.styleModifier {
                    property("font-feature-settings", it)
                }
            }
            ?: Modifier,
    )
    .then(
        style
            .letterSpacing
            .takeIf { it != TextUnit.Unspecified }
            ?.let { Modifier.letterSpacing(it) } ?: Modifier,
    )
    .then(
        style
            .background
            .takeIf { it != Color.Unspecified }
            ?.let { Modifier.background(it) } ?: Modifier,
    )
    .then(style.textDecoration?.let { Modifier.textDecorationLine(it) } ?: Modifier)
