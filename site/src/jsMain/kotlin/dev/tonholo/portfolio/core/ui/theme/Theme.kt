package dev.tonholo.portfolio.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.lyricist.Lyricist
import cafe.adriel.lyricist.ProvideStrings
import cafe.adriel.lyricist.rememberStrings
import com.varabyte.kobweb.compose.css.BoxSizing
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.css.UserSelect
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
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.silk.components.layout.Surface
import com.varabyte.kobweb.silk.components.style.ComponentModifier
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.common.SmoothColorStyle
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.components.style.vars.color.BackgroundColorVar
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.init.registerStyleBase
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.core.foundation.shadow
import dev.tonholo.portfolio.core.ui.theme.color.copy
import dev.tonholo.portfolio.core.ui.theme.color.from
import dev.tonholo.portfolio.core.ui.theme.typography.toModifier
import dev.tonholo.portfolio.core.ui.unit.DefaultFontSize
import dev.tonholo.portfolio.locale.Locale
import dev.tonholo.portfolio.locale.localStorageKey
import dev.tonholo.portfolio.resources.Strings
import kotlinx.browser.localStorage
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh

private const val COLOR_MODE_KEY = "portfolio:colorMode"

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

val Theme.icons: IconScheme
    @Composable
    @ReadOnlyComposable
    get() = LocalIconScheme.current

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

val ComponentModifier.icons
    get() = if (colorMode == ColorMode.DARK) {
        IconScheme.Dark
    } else {
        IconScheme.Light
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
        registerStyleBase("img") {
            Modifier.userSelect(UserSelect.None)
        }
    }
}

@InitSilk
fun initTheme(context: InitSilkContext) = with(context) {
    theme.palettes.light.from(LightColorScheme)
    theme.palettes.dark.from(DarkColorScheme)
}

val MainStyle by ComponentStyle(extraModifiers = { SmoothColorStyle.toModifier() }) {
    base {
        Modifier
            .minHeight(100.vh)
            .scrollBehavior(ScrollBehavior.Smooth)
    }
    cssRule("*::selection") {
        Modifier.backgroundColor(colorScheme.primary.copy(alpha = 0.2f))
    }
}

@Composable
fun Theme(
    content: @Composable () -> Unit,
) {
    Theme(
        themedColorScheme = LightColorScheme to DarkColorScheme,
        typography = Typography,
        themedElevations = ElevationsLight to ElevationsDark,
    ) { colorMode ->
        val lyricist = rememberStrings(
            currentLanguageTag = localStorage.getItem(Locale.localStorageKey) ?: Locale.DEFAULT,
        )
        CompositionLocalProvider(
            LocalLyricist provides lyricist,
            LocalIconScheme provides if (colorMode == ColorMode.LIGHT) IconScheme.Light else IconScheme.Dark,
        ) {
            ProvideStrings(lyricist) {
                Surface(
                    MainStyle.toModifier(),
                ) {
                    content()
                }
            }
        }
    }
}

val LocalLyricist = staticCompositionLocalOf<Lyricist<Strings>> { error("Lyricist not provided") }
