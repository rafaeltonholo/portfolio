package dev.tonholo.portfolio.core.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import cafe.adriel.lyricist.LanguageTag
import cafe.adriel.lyricist.LocalStrings
import com.varabyte.kobweb.browser.dom.ElementTarget
import com.varabyte.kobweb.compose.css.CSSLengthNumericValue
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.alignSelf
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.setVariable
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.navigation.Route
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.forms.ButtonVars
import com.varabyte.kobweb.silk.components.icons.mdi.MdiDarkMode
import com.varabyte.kobweb.silk.components.icons.mdi.MdiLightMode
import com.varabyte.kobweb.silk.components.overlay.PopupPlacement
import com.varabyte.kobweb.silk.components.overlay.Tooltip
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.hover
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.core.components.Logo
import dev.tonholo.portfolio.core.extensions.padding
import dev.tonholo.portfolio.core.foundation.elevation
import dev.tonholo.portfolio.core.router.About
import dev.tonholo.portfolio.core.router.Articles
import dev.tonholo.portfolio.core.router.Home
import dev.tonholo.portfolio.core.router.Resume
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.theme.color.Unspecified
import dev.tonholo.portfolio.core.ui.theme.colorScheme
import dev.tonholo.portfolio.core.ui.theme.copy
import dev.tonholo.portfolio.core.ui.theme.elevations
import dev.tonholo.portfolio.core.ui.theme.typography
import dev.tonholo.portfolio.core.ui.theme.typography.toModifier
import dev.tonholo.portfolio.core.ui.unit.dp
import dev.tonholo.portfolio.features.home.components.LanguageChanger
import kotlinx.browser.window
import org.jetbrains.compose.web.css.AlignSelf
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.css.percent

val AppBarStyles by ComponentStyle {
    base {
        Modifier
            .fillMaxWidth()
            .background(colorScheme.surface)
            .borderRadius(8.dp)
            .padding(16.dp)
            .elevation(elevations.level1)
    }
}

val AppBarButtonStyles by ComponentStyle {
    base {
        typography.labelLarge.toModifier() then Modifier
            .padding(horizontal = 10.dp, vertical = 12.dp)
            .background(Color.Unspecified)
            .borderRadius(100.dp)
    }

    hover {
        Modifier
            .setVariable(
                variable = ButtonVars.BackgroundHoverColor,
                value = colorScheme.primary.copy(alpha = 0.1f),
            )
    }
}

val IconButtonStyles by ComponentStyle {
    base {
        Modifier
            .padding(0.25.em)
            .background(Color.Unspecified)
            .borderRadius(100.percent)
    }

    hover {
        Modifier
            .setVariable(
                variable = ButtonVars.BackgroundHoverColor,
                value = colorScheme.primary.copy(alpha = 0.1f),
            )
    }
}

@Composable
fun AppBar(
    selectedLanguage: LanguageTag,
    modifier: Modifier = Modifier,
    onLocaleChange: (LanguageTag) -> Unit = {},
    onHomeClick: () -> Unit = {},
    onAboutClick: () -> Unit = {},
    onArticleClick: () -> Unit = {},
    onResumeClick: () -> Unit = {},
) {
    val strings = LocalStrings.current
    Row(
        modifier = AppBarStyles.toModifier() then modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Logo(modifier = Modifier.padding(left = 8.dp))
        Row(
            modifier = Modifier.gap(24.dp)
        ) {
            AppBarButton(
                text = strings.navBar.home,
                onClick = onHomeClick,
                isSelected = window.location.pathname == Route.Home,
            )
            AppBarButton(
                text = strings.navBar.about,
                onClick = onAboutClick,
                isSelected = window.location.pathname == Route.About,
            )
            AppBarButton(
                text = strings.navBar.articles,
                onClick = onArticleClick,
                isSelected = window.location.pathname == Route.Articles,
            )
            AppBarButton(
                text = strings.navBar.resume,
                onClick = onResumeClick,
                isSelected = window.location.pathname == Route.Resume,
            )
        }
        Row(
            modifier = Modifier.gap(0.625.em)
        ) {
            ColorModeButton(
                modifier = Modifier.alignSelf(AlignSelf.FlexEnd),
            )
            LanguageChanger(
                selected = selectedLanguage,
                onLocaleChange = onLocaleChange,
            )
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
            .setVariable(
                variable = ButtonVars.Height,
                value = auto.unsafeCast<CSSLengthNumericValue>(),
            )
            .thenIf(isSelected) {
                Modifier.color(Theme.colorScheme.primary)
            },
    ) {
        org.jetbrains.compose.web.dom.Text(
            value = text,
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
        if (colorMode.isLight) MdiDarkMode() else MdiLightMode()
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
        IconButtonStyles.toModifier() then modifier
            .setVariable(ButtonVars.FontSize, 1.em), // Make button icon size relative to parent container font size
    ) {
        content()
    }
}
