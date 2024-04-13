package dev.tonholo.portfolio.features.home.sections

import androidx.compose.runtime.Composable
import cafe.adriel.lyricist.LocalStrings
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.base
import com.varabyte.kobweb.silk.components.style.toModifier
import dev.tonholo.portfolio.components.text.Text
import dev.tonholo.portfolio.extensions.padding
import dev.tonholo.portfolio.ui.theme.Theme
import dev.tonholo.portfolio.ui.theme.colorScheme
import kotlinx.browser.window
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px

val AppBarStyles by ComponentStyle {
    base {
        Modifier
            .margin { top(0.2.em) }
            .fillMaxWidth()
            .background(colorScheme.surface)
            .padding(vertical = 0.7.em, horizontal = 1.em)
            .borderRadius {
                topLeft(8.px)
                topRight(8.px)
            }
            .margin { bottom(1.em) }
            .gap(1.em)
    }
}

val AppBarButtonStyles by ComponentStyle.base {
    Modifier
        .padding(0.5.em)
        .background(colorScheme.background)
}

@Composable
fun AppBar(
    modifier: Modifier = Modifier
) {
    val strings = LocalStrings.current
    Row(
        modifier = AppBarStyles.toModifier() then modifier,
    ) {
        AppBarButton(
            text = strings.navBar.home,
            onClick = {
                window.location.href = "/"
            },
            isSelected = window.location.pathname == "/",
        )
        AppBarButton(
            text = strings.navBar.articles,
            onClick = {
                window.location.href = "/article"
            },
            isSelected = window.location.pathname == "article",
        )
        AppBarButton(
            text = strings.navBar.resume,
            onClick = {
                window.location.href = "/resume"
            },
            isSelected = window.location.pathname == "/resume",
        )
    }
}

@Composable
private fun AppBarButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
) {
    Button(
        onClick = { onClick() },
        modifier = AppBarButtonStyles.toModifier() then modifier
            .thenIf(isSelected) {
                Modifier.color(Theme.colorScheme.primary)
            },
    ) {
        Text(
            text = text,
        )
    }
}
