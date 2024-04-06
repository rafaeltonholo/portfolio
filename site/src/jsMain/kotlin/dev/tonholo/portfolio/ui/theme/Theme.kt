package dev.tonholo.portfolio.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import cafe.adriel.lyricist.ProvideStrings
import com.varabyte.kobweb.compose.css.BoxSizing
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.boxSizing
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.font
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.outline
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import com.varabyte.kobweb.silk.SilkApp
import com.varabyte.kobweb.silk.components.layout.Surface
import com.varabyte.kobweb.silk.components.style.common.SmoothColorStyle
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.init.registerStyleBase
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.ui.theme.color.ColorScheme
import dev.tonholo.portfolio.ui.theme.color.LocalColorScheme
import dev.tonholo.portfolio.ui.theme.color.from
import dev.tonholo.portfolio.ui.theme.typography.LocalTypography
import dev.tonholo.portfolio.ui.theme.typography.Typography
import kotlinx.browser.localStorage
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh

private const val COLOR_MODE_KEY = "portfolio:colorMode"

private val DarkColorScheme = ColorScheme(
    primary = AccentPrimary,
    onPrimary = Primary,
    secondary = AccentDark,
    onSecondary = TextPrimary,
    background = Primary,
    onBackground = TextPrimary,
    surface = Secondary,
    onSurface = TextSecondary,
    outline = AccentBorder,
    error = AccentOrange,
    onError = Primary,
)

// TODO: setup light mode color scheme
private val LightColorScheme = ColorScheme(
    primary = AccentPrimary,
    onPrimary = Primary,
    secondary = AccentDark,
    onSecondary = TextPrimary,
    background = Secondary,
    onBackground = TextPrimary,
    surface = Primary,
    onSurface = TextSecondary,
    outline = AccentBorder,
    error = AccentOrange,
    onError = Primary,
)

object Theme {
    val colorScheme: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalColorScheme.current

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
}

@InitSilk
fun initTheme(context: InitSilkContext) = with(context) {
    config.initialColorMode = localStorage
        .getItem(COLOR_MODE_KEY)
        ?.let(ColorMode::valueOf)
        ?: ColorMode.DARK

    theme.palettes.light.from(LightColorScheme)
    theme.palettes.dark.from(DarkColorScheme)

    stylesheet.apply {
        registerStyleBase("*") {
            Modifier
                .margin(0.px)
                .padding(0.px)
                .outline(0.px)
                .boxSizing(BoxSizing.BorderBox)
        }
        registerStyleBase("body") {
            Modifier
                .font {
                    family("Roboto Condensed", "sans-serif")
                    size(16.px)
                }
                .fillMaxSize()
        }
    }
}

@Composable
fun Theme(
    content: @Composable () -> Unit,
) {
    val colorMode = ColorMode.current
    LaunchedEffect(colorMode) {
        localStorage.setItem(COLOR_MODE_KEY, colorMode.name)
    }

    val colorScheme = if (colorMode == ColorMode.DARK) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    CompositionLocalProvider(
        LocalColorScheme provides colorScheme,
    ) {
        ProvideStrings {
            SilkApp {
                Surface(
                    SmoothColorStyle.toModifier()
                        .minHeight(100.vh)
                        .scrollBehavior(ScrollBehavior.Smooth)
                ) {
                    content()
                }
            }
        }
    }
}
