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
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.boxSizing
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
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
import dev.tonholo.portfolio.core.foundation.shadow
import dev.tonholo.portfolio.core.ui.theme.color.ColorScheme
import dev.tonholo.portfolio.core.ui.theme.color.LocalColorScheme
import dev.tonholo.portfolio.core.ui.theme.color.from
import dev.tonholo.portfolio.core.ui.theme.typography.LocalTypography
import dev.tonholo.portfolio.core.ui.theme.typography.Typography
import dev.tonholo.portfolio.core.ui.theme.typography.toModifier
import dev.tonholo.portfolio.core.ui.unit.DefaultFontSize
import dev.tonholo.portfolio.locale.Locale
import dev.tonholo.portfolio.locale.localStorageKey
import dev.tonholo.portfolio.resources.Strings
import kotlinx.browser.document
import kotlinx.browser.localStorage
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh

private const val COLOR_MODE_KEY = "portfolio:colorMode"

private val DarkColorScheme = ColorScheme(
    primary = LimeGreen90,
    onPrimary = LimeGreen0,
    background = Neutral0,
    onBackground = Neutral100,
    surface = Neutral10,
    onSurface = Neutral90,
    onSurfaceVariant = Neutral80,
)

private val LightColorScheme = ColorScheme(
    primary = LimeGreen50,
    onPrimary = White,
    background = Neutral100,
    onBackground = Neutral10,
    surface = LimeGreen100,
    onSurface = Neutral10,
    onSurfaceVariant = Neutral30,
)

private val ElevationsLight = Elevations(
    level1 = Elevation(
        shadow {
            offsetX = 0.px
            offsetY = 1.px
            blurRadius = 3.px
            spreadRadius = 1.px
            color = Colors.Black.copy(alpha = 0.15f)
        },
        shadow {
            offsetX = 0.px
            offsetY = 1.px
            blurRadius = 2.px
            spreadRadius = 0.px
            color = Colors.Black.copy(alpha = 0.3f)
        }
    ),
)

private val ElevationsDark = Elevations(
    level1 = Elevation(
        shadow {
            offsetX = 0.px
            offsetY = 4.px
            blurRadius = 4.px
            spreadRadius = 0.px
            color = Colors.Black.copy(alpha = 0.30f)
        },
        shadow {
            offsetX = 0.px
            offsetY = 8.px
            blurRadius = 12.px
            spreadRadius = 6.px
            color = Colors.Black.copy(alpha = 0.15f)
        }
    ),
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

    val elevations: Elevations
        @Composable
        @ReadOnlyComposable
        get() = LocalElevations.current
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

val ComponentModifier.elevations
    get() = if (colorMode == ColorMode.DARK) {
        ElevationsDark
    } else {
        ElevationsLight
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
        registerStyleBase(":root") {
            Modifier.fontSize(DefaultFontSize.px)
        }
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
        registerStyleBase("#root") {
            Modifier.maxWidth(1440.px)
                .margin(
                    topBottom = 0.unsafeCast<CSSLengthOrPercentageNumericValue>(),
                    leftRight = auto.unsafeCast<CSSLengthOrPercentageNumericValue>(),
                )
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

            val (colorScheme, elevations) = when (colorMode) {
                ColorMode.LIGHT -> LightColorScheme to ElevationsLight
                ColorMode.DARK -> DarkColorScheme to ElevationsDark
            }

            InitSilkWidgetVariables()

            val lyricist = rememberStrings(
                currentLanguageTag = localStorage.getItem(Locale.localStorageKey) ?: Locale.DEFAULT,
            )
            CompositionLocalProvider(
                LocalColorScheme provides colorScheme,
                LocalLyricist provides lyricist,
                LocalTypography provides Typography,
                LocalElevations provides elevations,
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
