package dev.tonholo.portfolio.ui.theme.typography

import com.varabyte.kobweb.compose.css.CSSLengthNumericValue
import com.varabyte.kobweb.compose.css.FontStyle
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.css.TextShadow
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.font
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontStyle
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.modifiers.textShadow

data class TextStyle(
    val color: Color? = null,
    val fontSize: TextUnit? = null,
    val fontWeight: FontWeight? = null,
    val fontStyle: FontStyle? = null,
    val fontFamily: FontFamily? = null,
    val letterSpacing: TextUnit? = null,
    val background: Color? = null,
    val textDecoration: TextDecorationLine? = null,
    val shadow: TextShadow? = null,
    val textAlign: TextAlign? = null,
    val lineHeight: TextUnit? = null,
) {
    companion object {
        val Default = TextStyle()
    }
}

fun TextStyle.toModifier(): Modifier = (color?.let(Modifier::color) ?: Modifier) then
    (fontFamilyModifier()) then
    (fontSize?.let(Modifier::fontSize) ?: Modifier) then
    (letterSpacing?.let { Modifier.letterSpacing(it.unsafeCast<CSSLengthNumericValue>()) } ?: Modifier) then
    (background?.let(Modifier::background) ?: Modifier) then
    (textDecoration?.let(Modifier::textDecorationLine) ?: Modifier) then
    (shadow?.let(Modifier::textShadow) ?: Modifier) then
    (textAlign?.let(Modifier::textAlign) ?: Modifier) then
    (lineHeight?.let(Modifier::lineHeight) ?: Modifier)

private fun TextStyle.fontFamilyModifier(): Modifier = if (fontFamily != null) {
    val fontStyle = fontStyle ?: FontStyle.Normal
    val fontWeight = fontWeight ?: FontWeight.Normal
    val font = fontFamily.fonts.firstOrNull {
        it.weight.isEqualTo(fontWeight) && it.style.isEqualTo(fontStyle)
    }

    if (font != null) {
        Modifier.font {
            family(values = font.names)
            weight(font.weight)
            style(font.style)
        }
    } else {
        Modifier
    }
} else {
    (fontWeight?.let(Modifier::fontWeight) ?: Modifier) then
        (fontStyle?.let(Modifier::fontStyle) ?: Modifier)
}
