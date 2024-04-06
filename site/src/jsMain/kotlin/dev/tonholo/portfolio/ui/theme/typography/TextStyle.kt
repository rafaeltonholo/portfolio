package dev.tonholo.portfolio.ui.theme.typography

import com.varabyte.kobweb.compose.css.FontStyle
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.LineHeight
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.css.TextShadow
import com.varabyte.kobweb.compose.ui.graphics.Color

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
