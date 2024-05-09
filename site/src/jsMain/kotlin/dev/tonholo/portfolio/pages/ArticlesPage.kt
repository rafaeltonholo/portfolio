package dev.tonholo.portfolio.pages

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignSelf
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.navigation.Route
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.kotlin.wrapper.highlightjs.compose.html.component.CodeBlock
import dev.tonholo.kotlin.wrapper.highlightjs.compose.html.component.CodeBlockScope
import dev.tonholo.kotlin.wrapper.highlightjs.core.language.toSupportedLanguage
import dev.tonholo.kotlin.wrapper.highlightjs.core.style.SupportedStyle
import dev.tonholo.marktdown.domain.content.CodeFence
import dev.tonholo.marktdown.domain.content.TextElement
import dev.tonholo.marktdown.domain.renderer.MarktdownElementScope
import dev.tonholo.marktdown.domain.renderer.MarktdownRenderer
import dev.tonholo.portfolio.KmpMigratrionPart1En
import dev.tonholo.portfolio.core.foundation.layout.Scaffold
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.router.About
import dev.tonholo.portfolio.core.router.AppRoutes
import dev.tonholo.portfolio.core.router.Home
import dev.tonholo.portfolio.core.router.Resume
import dev.tonholo.portfolio.core.sections.AppBar
import dev.tonholo.portfolio.core.ui.theme.LocalLyricist
import dev.tonholo.portfolio.locale.Locale
import dev.tonholo.portfolio.locale.localStorageKey
import dev.tonholo.portfolio.renderer.Marktdown
import dev.tonholo.portfolio.renderer.MarktdownElement
import kotlinx.browser.localStorage
import org.jetbrains.compose.web.css.AlignSelf
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

@Composable
@MarktdownRenderer.Custom(type = CodeFence::class)
fun MarktdownElementScope<CodeFence>.CodeFenceCustomRender() {
    val colorMode = ColorMode.current
    CodeBlock(
        code = element.code,
        isDarkMode = colorMode.isDark,
        supportedLanguage = element.language?.toSupportedLanguage(),
        style = SupportedStyle.AndroidStudio,
        bottomContent = {
            BottomBar()
        }
    )
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
