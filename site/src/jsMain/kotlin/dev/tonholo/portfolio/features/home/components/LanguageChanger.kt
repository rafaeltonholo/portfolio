package dev.tonholo.portfolio.features.home.components

import androidx.compose.runtime.Composable
import cafe.adriel.lyricist.LanguageTag
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.TextTransform
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.setVariable
import com.varabyte.kobweb.compose.ui.modifiers.textTransform
import com.varabyte.kobweb.silk.components.layout.DividerVars
import com.varabyte.kobweb.silk.components.layout.VerticalDivider
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.hover
import com.varabyte.kobweb.silk.components.style.toModifier
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.theme.colorScheme
import dev.tonholo.portfolio.core.ui.theme.typography
import dev.tonholo.portfolio.core.ui.theme.typography.toModifier
import dev.tonholo.portfolio.core.ui.unit.dp
import dev.tonholo.portfolio.locale.Locales
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.LineStyle

val LanguageChanger by ComponentStyle {
    base {
        typography.labelLarge.toModifier() then Modifier
            .border(style = LineStyle.None)
            .background(color = Color.transparent)
            .color(colorScheme.onSurface)
            .padding(10.dp)
            .cursor(Cursor.Pointer)
            .textTransform(TextTransform.Uppercase)
    }

    hover {
        Modifier
            .color(colorScheme.secondary)
    }
}

@Composable
fun LanguageChanger(
    modifier: Modifier = Modifier,
    onLocaleChange: (LanguageTag) -> Unit,
) {
    Row(
        modifier = modifier.gap(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = Locales.PT_BR,
            modifier = LanguageChanger
                .toModifier()
                .onClick { onLocaleChange(Locales.PT_BR) },
        )
        VerticalDivider(
            modifier = Modifier
                .setVariable(DividerVars.Length, 32.dp)
                .setVariable(DividerVars.Color, Theme.colorScheme.onSurface),
        )
        Text(
            text = Locales.EN,
            modifier = LanguageChanger
                .toModifier()
                .onClick { onLocaleChange(Locales.EN) },
        )
    }
}
