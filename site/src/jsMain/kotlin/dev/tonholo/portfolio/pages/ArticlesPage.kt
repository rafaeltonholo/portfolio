package dev.tonholo.portfolio.pages

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.functions.blur
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignSelf
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.filter
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.navigation.Route
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.kotlin.wrapper.highlightjs.compose.html.component.CodeBlockScope
import dev.tonholo.kotlin.wrapper.shiki.compose.html.component.ShikiCodeBlock
import dev.tonholo.kotlin.wrapper.shiki.core.themes.DefaultTheme
import dev.tonholo.kotlin.wrapper.shiki.core.themes.ShikiTheme
import dev.tonholo.kotlin.wrapper.shiki.core.themes.shikiTheme
import dev.tonholo.kotlin.wrapper.shiki.core.transformers.notation.transformerNotationFocus
import dev.tonholo.kotlin.wrapper.shiki.core.transformers.notation.transformerNotationHighlight
import dev.tonholo.marktdown.domain.content.CodeFence
import dev.tonholo.marktdown.domain.content.TextElement
import dev.tonholo.marktdown.domain.renderer.MarktdownElementScope
import dev.tonholo.marktdown.domain.renderer.MarktdownRenderer
import dev.tonholo.portfolio.KmpMigratrionPart1En
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.foundation.layout.Scaffold
import dev.tonholo.portfolio.core.router.About
import dev.tonholo.portfolio.core.router.AppRoutes
import dev.tonholo.portfolio.core.router.Home
import dev.tonholo.portfolio.core.router.Resume
import dev.tonholo.portfolio.core.sections.AppBar
import dev.tonholo.portfolio.core.ui.theme.LocalLyricist
import dev.tonholo.portfolio.core.ui.unit.dp
import dev.tonholo.portfolio.locale.Locale
import dev.tonholo.portfolio.locale.localStorageKey
import dev.tonholo.portfolio.renderer.Marktdown
import dev.tonholo.portfolio.renderer.MarktdownElement
import kotlinx.browser.localStorage
import org.jetbrains.compose.web.css.AlignSelf
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.dom.I
import org.jetbrains.compose.web.dom.Span

@Page(AppRoutes.Articles.ROUTE)
@Composable
fun ArticlesPage() {
    val context = rememberPageContext()
//    val colorMode = ColorMode.current
    val lyricist = LocalLyricist.current

    Scaffold(
        topBar = {
            AppBar(
                selectedLanguage = lyricist.languageTag,
                onLocaleChange = { languageTag ->
                    lyricist.languageTag = languageTag
                    localStorage.setItem(Locale.localStorageKey, languageTag)
                },
                onHomeClick = {
                    context.router.navigateTo(Route.Home)
                },
                onAboutClick = {
                    context.router.navigateTo(Route.About)
                },
                onResumeClick = {
                    context.router.navigateTo(Route.Resume)
                },
            )
        }
    ) {
        Column {
            Marktdown(document = KmpMigratrionPart1En)
        }
    }
}

@Composable
fun CodeBlockScope.BottomBar(
    link: String? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        link?.let {
            Link(
                path = link,
                modifier = Modifier.weight(1),
            )
        } ?: Span(attrs = Modifier.weight(1).toAttrs())
        languageName?.let { languageName ->
            Text(
                text = languageName,
                modifier = Modifier.alignSelf(AlignSelf.FlexEnd)
            )
        }
    }
}

val ShikiStyles by ComponentStyle {
    base { Modifier }

    cssRule("pre.has-focused code .line:not(.focused)") {
        Modifier
            .opacity(0.7f)
            .filter(blur(2.dp))
            .transition(
                CSSTransition(property = "filter", duration = 0.35.s),
                CSSTransition(property = "opacity", duration = 0.35.s),
            )
    }
    cssRule("pre.has-focused:hover code .line:not(.focused)") {
        Modifier
            .opacity(1.0f)
            .filter(blur(0.dp))
    }

    cssRule("code .highlighted") {
        Modifier
            .display(DisplayStyle.InlineBlock)
            .fillMaxWidth()
            .transition(CSSTransition(property = "background-color", duration = 0.5.s))
            .backgroundColor(
                "color-mix(in srgb, currentcolor 20%, transparent)".unsafeCast<CSSColorValue>(),
            )
    }
}

@Composable
@MarktdownRenderer.Custom(type = CodeFence::class)
fun MarktdownElementScope<CodeFence>.CodeFenceCustomRender() {
    val colorMode = ColorMode.current
    ShikiCodeBlock(
        code = element.code,
        language = element.language ?: "text",
        themeOptions = shikiTheme {
            light = ShikiTheme.OneLight
            dark = ShikiTheme.OneDarkPro
            activeTheme(
                if (colorMode == ColorMode.DARK) DefaultTheme.Dark
                else DefaultTheme.Light
            )
        },
        transformers = listOf(
            transformerNotationFocus(),
            transformerNotationHighlight(),
        ),
        paddingStart = 16.dp,
        paddingEnd = 16.dp,
        paddingTop = 16.dp,
        paddingBottom = 16.dp,
        blur = 2.dp,
        borderRadius = 8.dp,
    )
//    val colorMode = ColorMode.current
//    var code by remember { mutableStateOf("") }
//    LaunchedEffect(colorMode) {
//        Shiki.initialize()
//        val unescapedCode = DOMParser().parseFromString(element.code, "text/html")
//            .documentElement
//            ?.textContent
//            .orEmpty()
//        code = Shiki.instance.codeToHtml(
//            unescapedCode,
//            options = CodeToHastOptions {
//                lang = element.language ?: "text"
//                themes = ThemeInput {
//                    light = "github-light"
//                    dark = "github-dark"
//                }
//                transformers = arrayOf(
//                    transformerNotationFocus(),
//                    transformerNotationHighlight(),
//                )
//                defaultColor = if (colorMode.isDark) "dark" else "light"
//            },
//        )
//    }
//
//    Div(
//        attrs = ShikiStyles.toAttrs {
//            classes(element.language ?: "plain-text")
//        }
//    ) {
//        DisposableEffect(code) {
//            scopeElement.innerHTML = code
//            onDispose { }
//        }
//    }

//    val colorMode = ColorMode.current
//    CodeBlock(
//        code = element.code,
//        isDarkMode = colorMode.isDark,
//        supportedLanguage = element.language?.toSupportedLanguage(),
//        style = SupportedStyle.AndroidStudio,
//        bottomContent = {
//            BottomBar()
//        }
//    )
}

@Composable
@MarktdownRenderer.Custom(type = TextElement.EmphasisText::class)
fun MarktdownElementScope<TextElement.EmphasisText>.EmphasisTextCustomRender() {
    I {
        element.children.forEach {
            MarktdownElement(it)
        }
    }
}
