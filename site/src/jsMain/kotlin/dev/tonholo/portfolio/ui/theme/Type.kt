package dev.tonholo.portfolio.ui.theme

import com.varabyte.kobweb.compose.css.FontWeight
import dev.tonholo.portfolio.ui.theme.typography.TextStyle
import dev.tonholo.portfolio.ui.theme.typography.Typography
import org.jetbrains.compose.web.css.cssRem

val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = Eczar,
        fontWeight = FontWeight.Medium,
        fontSize = 2.5.cssRem,
    ),
    labelMedium = TextStyle(
        fontSize = 1.cssRem,
    ),
    labelSmall = TextStyle(
        fontSize = 0.8.cssRem,
    ),
)
