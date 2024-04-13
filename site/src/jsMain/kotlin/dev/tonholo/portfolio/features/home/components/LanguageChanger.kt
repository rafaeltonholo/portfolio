package dev.tonholo.portfolio.features.home.components

import androidx.compose.runtime.Composable
import cafe.adriel.lyricist.LanguageTag
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.TextTransform
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.content
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.left
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.textTransform
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.after
import com.varabyte.kobweb.silk.components.style.firstChild
import com.varabyte.kobweb.silk.components.style.hover
import com.varabyte.kobweb.silk.components.style.toModifier
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.locale.Locales
import dev.tonholo.portfolio.core.ui.theme.colorScheme
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.em

val LanguageChanger by ComponentStyle {
    base {
        Modifier
            .border(style = LineStyle.None)
            .background(color = Color.transparent)
            .color(colorScheme.primary)
            .padding(0.5.em)
            .fontSize(0.85.em)
            .cursor(Cursor.Pointer)
            .margin(left = 0.2.em)
            .textTransform(TextTransform.Uppercase)
    }

    hover {
        Modifier
            .color(colorScheme.secondary)
    }

    firstChild {
        Modifier.margin(right = 1.em)
    }
    (firstChild + after) {
        Modifier
            .content("|")
            .position(Position.Relative)
            .left(1.em)
    }
}

@Composable
fun LanguageChanger(
    modifier: Modifier = Modifier,
    onLocaleChange: (LanguageTag) -> Unit,
) {
    Row(modifier = modifier) {
        Text(
            text = Locales.EN,
            modifier = LanguageChanger
                .toModifier()
                .onClick { onLocaleChange(Locales.EN) },
        )
        Text(
            text = Locales.PT_BR,
            modifier = LanguageChanger
                .toModifier()
                .onClick { onLocaleChange(Locales.PT_BR) },
        )
    }
}
