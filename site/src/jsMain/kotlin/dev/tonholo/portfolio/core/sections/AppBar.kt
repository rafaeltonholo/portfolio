package dev.tonholo.portfolio.core.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import cafe.adriel.lyricist.LanguageTag
import cafe.adriel.lyricist.LocalStrings
import com.varabyte.kobweb.browser.dom.ElementTarget
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignSelf
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flex
import com.varabyte.kobweb.compose.ui.modifiers.flexDirection
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.setVariable
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.forms.ButtonVars
import com.varabyte.kobweb.silk.components.icons.MoonIcon
import com.varabyte.kobweb.silk.components.icons.SunIcon
import com.varabyte.kobweb.silk.components.overlay.PopupPlacement
import com.varabyte.kobweb.silk.components.overlay.Tooltip
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.base
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.extensions.padding
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.theme.colorScheme
import dev.tonholo.portfolio.features.home.components.LanguageChanger
import kotlinx.browser.window
import org.jetbrains.compose.web.css.AlignSelf
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

val AppBarStyles by ComponentStyle {
    base {
        Modifier.fillMaxWidth()
    }
}

val AppBarNavRowStyles by ComponentStyle {
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
    modifier: Modifier = Modifier,
    onLocaleChange: (LanguageTag) -> Unit = {},
    onHomeClick: () -> Unit = {},
    onArticleClick: () -> Unit = {},
    onResumeClick: () -> Unit = {},
) {
    val strings = LocalStrings.current
    Column(modifier = AppBarStyles.toModifier() then modifier) {
        LanguageChanger(onLocaleChange = onLocaleChange)
        Row(
            modifier = AppBarNavRowStyles.toModifier(),
        ) {
            AppBarButton(
                text = strings.navBar.home,
                onClick = onHomeClick,
                isSelected = window.location.pathname == "/",
            )
            AppBarButton(
                text = strings.navBar.articles,
                onClick = onArticleClick,
                isSelected = window.location.pathname == "article",
            )
            AppBarButton(
                text = strings.navBar.resume,
                onClick = onResumeClick,
                isSelected = window.location.pathname == "/resume",
            )
            Box(
                modifier = Modifier
                    .flex(1)
                    .display(DisplayStyle.Flex)
                    .flexDirection(FlexDirection.Column),
            ) {
                ColorModeButton(
                    modifier = Modifier.alignSelf(AlignSelf.FlexEnd),
                )
            }
        }
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

@Composable
private fun ColorModeButton(
    modifier: Modifier = Modifier,
) {
    var colorMode by ColorMode.currentState
    IconButton(
        onClick = { colorMode = colorMode.opposite },
        modifier = modifier,
    ) {
        if (colorMode.isLight) MoonIcon() else SunIcon()
    }
    Tooltip(ElementTarget.PreviousSibling, "Toggle color mode", placement = PopupPlacement.BottomRight)
}

@Composable
private fun IconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Button(
        onClick = { onClick() },
        modifier
            .padding(0.px)
            .borderRadius(50.percent)
            .setVariable(ButtonVars.FontSize, 1.em), // Make button icon size relative to parent container font size
    ) {
        content()
    }
}
