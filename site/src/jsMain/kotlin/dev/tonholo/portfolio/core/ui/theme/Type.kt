package dev.tonholo.portfolio.core.ui.theme

import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.LineHeight
import dev.tonholo.portfolio.core.ui.theme.typography.TextStyle
import dev.tonholo.portfolio.core.ui.theme.typography.TextUnit
import dev.tonholo.portfolio.core.ui.theme.typography.Typography
import org.jetbrains.compose.web.css.em

val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = Eczar,
        fontWeight = FontWeight.Medium,
        fontSize = 2.85.em,
        lineHeight = LineHeight.Normal.unsafeCast<TextUnit>(),
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 1.71.em,
        lineHeight = LineHeight.Normal.unsafeCast<TextUnit>(),
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 1.33.em,
        lineHeight = LineHeight.Normal.unsafeCast<TextUnit>(),
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 1.14.em,
        lineHeight = LineHeight.Normal.unsafeCast<TextUnit>(),
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 0.94.em,
        lineHeight = LineHeight.Normal.unsafeCast<TextUnit>(),
    ),
    titleSmall = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 0.76.em,
        lineHeight = LineHeight.Normal.unsafeCast<TextUnit>(),
    ),
    labelMedium = TextStyle(
        fontSize = 1.em,
        lineHeight = LineHeight.Normal.unsafeCast<TextUnit>(),
    ),
    labelSmall = TextStyle(
        fontSize = 0.8.em,
        lineHeight = LineHeight.Normal.unsafeCast<TextUnit>(),
    ),
)
