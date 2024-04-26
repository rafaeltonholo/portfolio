package dev.tonholo.portfolio.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.lyricist.Lyricist
import cafe.adriel.lyricist.ProvideStrings
import cafe.adriel.lyricist.rememberStrings
import com.varabyte.kobweb.compose.css.BoxSizing
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.boxSizing
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.outline
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import com.varabyte.kobweb.core.KobwebApp
import com.varabyte.kobweb.silk.components.layout.Surface
import com.varabyte.kobweb.silk.components.style.ComponentModifier
import com.varabyte.kobweb.silk.components.style.common.SmoothColorStyle
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.style.vars.color.BackgroundColorVar
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.init.registerStyleBase
import com.varabyte.kobweb.silk.init.setSilkWidgetVariables
import com.varabyte.kobweb.silk.prepareSilkFoundation
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.core.ui.theme.color.ColorScheme
import dev.tonholo.portfolio.core.ui.theme.color.LocalColorScheme
import dev.tonholo.portfolio.core.ui.theme.color.from
import dev.tonholo.portfolio.core.ui.theme.typography.LocalTypography
import dev.tonholo.portfolio.core.ui.theme.typography.Typography
import dev.tonholo.portfolio.core.ui.theme.typography.toModifier
import dev.tonholo.portfolio.locale.Locale
import dev.tonholo.portfolio.locale.localStorageKey
import dev.tonholo.portfolio.resources.Strings
import kotlinx.browser.document
import kotlinx.browser.localStorage
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh

private const val COLOR_MODE_KEY = "portfolio:colorMode"

private val DarkColorScheme = ColorScheme(
    primary = AccentPrimary,
    onPrimary = Primary,
    secondary = AccentDark,
    onSecondary = TextPrimary,
    background = Secondary,
    onBackground = TextPrimary,
    surface = Primary,
    onSurface = TextSecondary,
    outline = AccentBorder,
    outlineVariant = AccentGray,
    error = AccentOrange,
    onError = Primary,
)

// TODO: setup light mode color scheme
private val LightColorScheme = ColorScheme(
    primary = AccentPrimary,
    onPrimary = Primary,
    secondary = AccentDark,
    onSecondary = TextPrimary,
    background = Color.argb(0xFFFFFFFF),
    onBackground = TextPrimary,
    surface = Primary,
    onSurface = TextSecondary,
    outline = AccentBorder,
    outlineVariant = AccentGray,
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

/**
 * Workaround to enable [com.varabyte.kobweb.silk.components.style.ComponentStyle]
 * to use [Theme.colorScheme].
 */
val ComponentModifier.colorScheme
    get() = if (colorMode == ColorMode.DARK) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

/**
 * Workaround to enable [com.varabyte.kobweb.silk.components.style.ComponentStyle]
 * to use [Theme.typography].
 */
val ComponentModifier.typography
    get() = Typography

@InitSilk
fun initColorMode(context: InitSilkContext) {
    context.config.initialColorMode = localStorage
        .getItem(COLOR_MODE_KEY)
        ?.let(ColorMode::valueOf)
        ?: ColorMode.DARK
}

@InitSilk
fun initSiteStyles(context: InitSilkContext) {
    context.stylesheet.apply {
        registerStyleBase("*") {
            Modifier
                .margin(0.px)
                .padding(0.px)
                .outline(0.px)
                .boxSizing(BoxSizing.BorderBox)
        }
        registerStyleBase("body") {
            Typography.bodyLarge
                .copy(lineHeight = null)
                .toModifier()
                .fillMaxSize()
                .backgroundColor(BackgroundColorVar.value())
        }
    }
}

@InitSilk
fun initTheme(context: InitSilkContext) = with(context) {
    theme.palettes.light.from(LightColorScheme)
    theme.palettes.dark.from(DarkColorScheme)
}

@Composable
fun Theme(
    content: @Composable () -> Unit,
) {
    KobwebApp {
        prepareSilkFoundation {
            val colorMode by ColorMode.currentState
            LaunchedEffect(colorMode) {
                localStorage.setItem(COLOR_MODE_KEY, colorMode.name)
            }

            val colorScheme = if (colorMode == ColorMode.DARK) {
                DarkColorScheme
            } else {
                LightColorScheme
            }
            InitSilkWidgetVariables()

            val lyricist = rememberStrings(
                currentLanguageTag = localStorage.getItem(Locale.localStorageKey) ?: Locale.DEFAULT,
            )
            CompositionLocalProvider(
                LocalColorScheme provides colorScheme,
                LocalLyricist provides lyricist,
                LocalTypography provides Typography,
            ) {
                ProvideStrings(lyricist) {
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
}

@Composable
private fun InitSilkWidgetVariables() {
    val root = remember { document.body }
    root?.setSilkWidgetVariables()
}

val LocalLyricist = staticCompositionLocalOf<Lyricist<Strings>> { error("Lyricist not provided") }
