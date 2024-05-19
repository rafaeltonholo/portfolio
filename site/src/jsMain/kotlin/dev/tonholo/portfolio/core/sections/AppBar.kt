package dev.tonholo.portfolio.core.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.lyricist.LanguageTag
import cafe.adriel.lyricist.LocalStrings
import com.varabyte.kobweb.compose.css.AlignItems
import com.varabyte.kobweb.compose.css.CSSLengthNumericValue
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.ColumnScope
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.alignSelf
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flexDirection
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.justifyContent
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.setVariable
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.navigation.Route
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.forms.ButtonVars
import com.varabyte.kobweb.silk.components.icons.mdi.MdiClose
import com.varabyte.kobweb.silk.components.icons.mdi.MdiDarkMode
import com.varabyte.kobweb.silk.components.icons.mdi.MdiLightMode
import com.varabyte.kobweb.silk.components.icons.mdi.MdiMenu
import com.varabyte.kobweb.silk.components.overlay.Overlay
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.base
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
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
import dev.tonholo.portfolio.core.ui.theme.color.copy
import dev.tonholo.portfolio.core.ui.theme.colorScheme
import dev.tonholo.portfolio.core.ui.theme.elevations
import dev.tonholo.portfolio.core.ui.theme.typography
import dev.tonholo.portfolio.core.ui.theme.typography.toModifier
import dev.tonholo.portfolio.core.ui.unit.TextUnit
import dev.tonholo.portfolio.core.ui.unit.dp
import dev.tonholo.portfolio.core.ui.unit.sp
import dev.tonholo.portfolio.features.home.components.LanguageChanger
import dev.tonholo.portfolio.locale.Locales
import org.jetbrains.compose.web.css.AlignSelf
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.dom.Nav

val AppBarStyles by ComponentStyle {
    base {
        Modifier
            .fillMaxWidth()
            .background(colorScheme.surface)
            .borderRadius(8.dp)
            .padding(16.dp)
            .elevation(elevations.level1)
            .display(DisplayStyle.Flex)
            .flexDirection(FlexDirection.Row)
            .justifyContent(JustifyContent.SpaceBetween)
            .alignItems(AlignItems.Center)
    }
}

val AppBarNavMenuStyles by ComponentStyle {
    base {
        Modifier.display(DisplayStyle.None)
    }

    Breakpoint.MD {
        Modifier
            .display(DisplayStyle.Flex)
            .gap(24.dp)
    }
}

val AppBarButtonStyles by ComponentStyle {
    base {
        typography.headlineLarge.toModifier()
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .background(Color.Unspecified)
            .borderRadius(100.dp)
    }

    Breakpoint.MD {
        typography.labelLarge.toModifier()
            .padding(horizontal = 10.dp, vertical = 12.dp)
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

val AppBarActionButtonsStyle by ComponentStyle {
    base {
        Modifier.display(DisplayStyle.None)
    }
    Breakpoint.MD {
        Modifier
            .display(DisplayStyle.Flex)
            .gap(10.dp)
    }
}

val AppBarMenuButtonStyle by ComponentStyle {
    base {
        Modifier.display(DisplayStyle.Inline)
    }
    Breakpoint.MD {
        Modifier.display(DisplayStyle.None)
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
    val context = rememberPageContext()
    val currentRoute = context.route
    var isMenuOpen by remember { mutableStateOf(false) }
    Nav(
        attrs = AppBarStyles.toModifier().then(modifier).toAttrs(),
    ) {
        Logo(modifier = Modifier.padding(left = 8.dp))
        val navMenuContent = remember(strings) {
            movableContentOf {
                AppBarButton(
                    text = strings.navBar.home,
                    onClick = onHomeClick,
                    isSelected = currentRoute.path == Route.Home,
                )
                AppBarButton(
                    text = strings.navBar.about,
                    onClick = onAboutClick,
                    isSelected = currentRoute.path == Route.About,
                )
                AppBarButton(
                    text = strings.navBar.articles,
                    onClick = onArticleClick,
                    isSelected = currentRoute.path.startsWith("/${Locales.EN.lowercase()}${Route.Articles}") ||
                        currentRoute.path.startsWith("/${Locales.PT_BR.lowercase()}${Route.Articles}") ||
                        currentRoute.path.startsWith(Route.Articles),
                )
                AppBarButton(
                    text = strings.navBar.resume,
                    onClick = onResumeClick,
                    isSelected = currentRoute.path == Route.Resume,
                )
            }
        }

        Row(
            modifier = AppBarNavMenuStyles.toModifier(),
        ) {
            navMenuContent()
        }
        Row(
            modifier = AppBarActionButtonsStyle.toModifier(),
        ) {
            ColorModeButton(
                modifier = Modifier.alignSelf(AlignSelf.FlexEnd),
            )
            LanguageChanger(
                selected = selectedLanguage,
                onLocaleChange = onLocaleChange,
            )
        }

        IconButton(
            onClick = { isMenuOpen = true },
            modifier = AppBarMenuButtonStyle.toModifier(),
        ) {
            MdiMenu()
        }

        if (isMenuOpen) {
            Overlay {
                AppNavigationDialog(
                    selectedLanguage = selectedLanguage,
                    onLocaleChange = onLocaleChange,
                    onDismiss = { isMenuOpen = false },
                ) {
                    navMenuContent()
                }
            }
        }
    }
}

val AppNavigationDialogStyle by ComponentStyle.base {
    Modifier
        .fillMaxSize()
        .backgroundColor(colorScheme.surface)
        .padding(24.dp)
}

@Composable
private fun AppNavigationDialog(
    selectedLanguage: LanguageTag,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    onLocaleChange: (LanguageTag) -> Unit = {},
    content: @Composable (ColumnScope.() -> Unit),
) {
    Column(
        modifier = AppNavigationDialogStyle.toModifier() then modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .alignSelf(AlignSelf.End)
                .height(56.dp),
        ) {
            MdiClose(
                modifier = Modifier.fontSize(48.sp),
            )
        }
        Spacer()
        content()
        Spacer()
        ColorModeButton(iconSize = 32.sp)
        LanguageChanger(
            selected = selectedLanguage,
            onLocaleChange = onLocaleChange,
            modifier = Modifier.margin {
                top(16.dp)
                left((-29).dp)
            }
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
    iconSize: TextUnit = 24.sp,
    modifier: Modifier = Modifier,
) {
    var colorMode by ColorMode.currentState
    IconButton(
        onClick = { colorMode = colorMode.opposite },
        modifier = modifier,
    ) {
        val iconSizeModifier = Modifier.fontSize(iconSize)
        if (colorMode.isLight) {
            MdiDarkMode(modifier = iconSizeModifier)
        } else {
            MdiLightMode(modifier = iconSizeModifier)
        }
    }
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
