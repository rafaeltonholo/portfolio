package dev.tonholo.portfolio.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.lyricist.Lyricist
import cafe.adriel.lyricist.ProvideStrings
import cafe.adriel.lyricist.rememberStrings
import com.varabyte.kobweb.compose.css.BoxSizing
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.boxSizing
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.outline
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.components.forms.ButtonStyle
import com.varabyte.kobweb.silk.components.layout.Surface
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.init.registerStyleBase
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.CssStyleScopeBase
import com.varabyte.kobweb.silk.style.breakpoint.BreakpointValues
import com.varabyte.kobweb.silk.style.common.SmoothColorStyle
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.style.vars.color.BackgroundColorVar
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.modifyStyle
import dev.tonholo.portfolio.core.foundation.shadow
import dev.tonholo.portfolio.core.ui.theme.color.copy
import dev.tonholo.portfolio.core.ui.theme.color.from
import dev.tonholo.portfolio.core.ui.theme.typography.toModifier
import dev.tonholo.portfolio.core.ui.unit.DefaultFontSize
import dev.tonholo.portfolio.core.ui.unit.dp
import dev.tonholo.portfolio.locale.Locale
import dev.tonholo.portfolio.locale.localStorageKey
import dev.tonholo.portfolio.resources.Strings
import kotlinx.browser.document
import kotlinx.browser.localStorage
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh
import org.w3c.dom.HTMLMetaElement
import org.w3c.dom.asList

private val ElevationsLight = Elevations(
    level1 = Elevation(
        shadow {
            offsetX = 0.dp
            offsetY = 1.dp
            blurRadius = 3.dp
            spreadRadius = 1.dp
            color = Colors.Black.copy(alpha = 0.15f)
        },
        shadow {
            offsetX = 0.dp
            offsetY = 1.dp
            blurRadius = 2.dp
            spreadRadius = 0.dp
            color = Colors.Black.copy(alpha = 0.3f)
        }
    ),
)

private val ElevationsDark = Elevations(
    level1 = Elevation(
        shadow {
            offsetX = 0.dp
            offsetY = 4.dp
            blurRadius = 4.dp
            spreadRadius = 0.dp
            color = Colors.Black.copy(alpha = 0.30f)
        },
        shadow {
            offsetX = 0.dp
            offsetY = 8.dp
            blurRadius = 12.dp
            spreadRadius = 6.dp
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
val CssStyleScopeBase.colorScheme
    get() = if (colorMode == ColorMode.DARK) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

val CssStyleScopeBase.elevations
    get() = if (colorMode == ColorMode.DARK) {
        ElevationsDark
    } else {
        ElevationsLight
    }

val CssStyleScopeBase.icons
    get() = if (colorMode == ColorMode.DARK) {
        IconScheme.Dark
    } else {
        IconScheme.Light
    }

/**
 * Workaround to enable [CssStyleScopeBase]
 * to use [Theme.typography].
 */
val CssStyleScopeBase.typography
    get() = Typography

@InitSilk
fun initSiteStyles(context: InitSilkContext) {
    context.stylesheet.apply {
        registerStyle("body:has(.silk-overlay.$FULL_SCREEN_MENU_CLASSNAME)") {
            base {
                Modifier.overflow(Overflow.Hidden)
            }
        }

        layer("reset") {
            registerStyleBase("*") {
                Modifier
                    .margin(0.dp)
                    .padding(0.dp)
                    .outline(0.dp)
                    .boxSizing(BoxSizing.BorderBox)
            }
        }

        layer("base") {
            registerStyleBase(":root") {
                Modifier.fontSize(DefaultFontSize.px)
            }
            registerStyleBase("body") {
                Typography.bodyLarge
                    .copy(lineHeight = null)
                    .toModifier()
                    .fillMaxSize()
                    .backgroundColor(BackgroundColorVar.value())
            }
            registerStyleBase("#root") {
                Modifier.maxWidth(1440.dp)
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

    context.theme.apply {
        modifyStyle(ButtonStyle) {
            Modifier
                .backgroundColor(Colors.Transparent)
                .cursor(Cursor.Pointer)
                .styleModifier {
                    property(propertyName = "border", value = "none")
                }
        }
    }
}

@InitSilk
fun initTheme(context: InitSilkContext) = with(context) {
    theme.palettes.light.from(LightColorScheme)
    theme.palettes.dark.from(DarkColorScheme)
}

val MainStyle = CssStyle(extraModifier = { SmoothColorStyle.toModifier() }) {
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
    breakpoints: BreakpointValues<CSSSizeValue<CSSUnit.rem>>,
    content: @Composable () -> Unit,
) {
    Theme(
        themedColorScheme = LightColorScheme to DarkColorScheme,
        typography = Typography,
        themedElevations = ElevationsLight to ElevationsDark,
        breakpoints = breakpoints,
    ) { colorMode ->
        val lyricist = rememberStrings(
            currentLanguageTag = localStorage.getItem(Locale.localStorageKey) ?: Locale.DEFAULT,
        )

        LaunchedEffect(colorMode) {
            document.head?.let { head ->
                head
                    .querySelectorAll("meta[name='theme-color']")
                    .asList()
                    .forEach { document.head?.removeChild(it) }
                head.append(
                    (document.createElement("meta") as HTMLMetaElement).apply {
                        name = "theme-color"
                        this.content = if(colorMode.isLight) {
                            LightColorScheme.background.toString()
                        } else {
                            DarkColorScheme.background.toString()
                        }
                    }
                )
            }
        }

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
