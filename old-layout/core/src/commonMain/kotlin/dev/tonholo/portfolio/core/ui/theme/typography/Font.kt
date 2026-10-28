package dev.tonholo.portfolio.core.ui.theme.typography

import androidx.compose.runtime.Stable
import com.varabyte.kobweb.compose.css.FontStyle
import com.varabyte.kobweb.compose.css.FontWeight

class Font(
    val names: Array<out String>,
    val weight: FontWeight = FontWeight.Normal,
    val style: FontStyle = FontStyle.Normal,
) {
    override fun toString(): String {
        return "Font(names=${names.contentToString()}, weight=$weight, style=$style)"
    }
}

class FontFamily(
    val fonts: Array<out Font>,
) {
    override fun toString(): String {
        return "FontFamily(fonts=${fonts.contentToString()})"
    }
}

@Stable
fun Font(
    vararg names: String,
    weight: FontWeight = FontWeight.Normal,
    style: FontStyle = FontStyle.Normal,
): Font = Font(names = names, weight = weight, style = style)

@Stable
fun FontFamily(vararg fonts: Font): FontFamily = FontFamily(fonts = fonts)

fun FontWeight.isEqualTo(other: FontWeight, default: FontWeight? = null): Boolean =
    toString() == other.toString() || toString() == default?.toString()

fun FontStyle.isEqualTo(other: FontStyle): Boolean =
    toString() == other.toString()

val FontWeight.Companion.Regular
    get() = Normal
