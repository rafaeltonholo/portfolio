package dev.tonholo.portfolio.core.ui.text

import androidx.compose.ui.text.font.FontSynthesis
import com.varabyte.kobweb.compose.css.FontStyle
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.ui.graphics.Color
import dev.tonholo.portfolio.core.ui.text.AnnotatedString.Builder
import dev.tonholo.portfolio.core.ui.text.AnnotatedString.Style
import dev.tonholo.portfolio.core.ui.theme.color.Unspecified
import dev.tonholo.portfolio.core.ui.theme.typography.Font
import dev.tonholo.portfolio.core.ui.unit.TextUnit

data class TitleStyle(
    val level: Level,
    val fontSize: TextUnit = TextUnit.Unspecified,
    val fontWeight: FontWeight? = null,
    val fontStyle: FontStyle? = null,
    val fontSynthesis: FontSynthesis? = null,
    val fontFamily: Font? = null,
    val fontFeatureSettings: String? = null,
    val letterSpacing: TextUnit = TextUnit.Unspecified,
    val background: Color = Color.Unspecified,
    val textDecoration: TextDecorationLine? = null,
) : Style {
    enum class Level {
        H1, H2, H3, H4, H5, H6
    }
}

data class SubtitleStyle(
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

fun Builder.title(
    text: String,
    level: TitleStyle.Level,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    fontStyle: FontStyle? = null,
    fontSynthesis: FontSynthesis? = null,
    fontFamily: Font? = null,
    fontFeatureSettings: String? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    background: Color = Color.Unspecified,
    textDecoration: TextDecorationLine? = null,
) {
    withStyle(
        style = TitleStyle(
            level = level,
            fontSize = fontSize,
            fontWeight = fontWeight,
            fontStyle = fontStyle,
            fontSynthesis = fontSynthesis,
            fontFamily = fontFamily,
            fontFeatureSettings = fontFeatureSettings,
            letterSpacing = letterSpacing,
            background = background,
            textDecoration = textDecoration,
        ),
    ) {
        append(text)
    }
}

fun Builder.subtitle(
    text: String,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    fontStyle: FontStyle? = null,
    fontSynthesis: FontSynthesis? = null,
    fontFamily: Font? = null,
    fontFeatureSettings: String? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    background: Color = Color.Unspecified,
    textDecoration: TextDecorationLine? = null,
) {
    withStyle(
        style = SubtitleStyle(
            fontSize = fontSize,
            fontWeight = fontWeight,
            fontStyle = fontStyle,
            fontSynthesis = fontSynthesis,
            fontFamily = fontFamily,
            fontFeatureSettings = fontFeatureSettings,
            letterSpacing = letterSpacing,
            background = background,
            textDecoration = textDecoration,
        ),
    ) {
        append(text)
    }
}
