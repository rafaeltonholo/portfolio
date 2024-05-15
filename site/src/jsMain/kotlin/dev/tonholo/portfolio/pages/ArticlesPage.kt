package dev.tonholo.portfolio.pages

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.BorderCollapse
import com.varabyte.kobweb.compose.css.OverflowWrap
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignSelf
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderBottom
import com.varabyte.kobweb.compose.ui.modifiers.borderCollapse
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flexDirection
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.overflowWrap
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.navigation.Route
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.toModifier
import dev.tonholo.portfolio.KmpMigratrionPart1En
import dev.tonholo.portfolio.core.components.button.TextButton
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.extensions.margin
import dev.tonholo.portfolio.core.extensions.padding
import dev.tonholo.portfolio.core.foundation.layout.Scaffold
import dev.tonholo.portfolio.core.router.About
import dev.tonholo.portfolio.core.router.AppRoutes
import dev.tonholo.portfolio.core.router.Home
import dev.tonholo.portfolio.core.router.Resume
import dev.tonholo.portfolio.core.sections.AppBar
import dev.tonholo.portfolio.core.sections.Footer
import dev.tonholo.portfolio.core.ui.theme.LocalLyricist
import dev.tonholo.portfolio.core.ui.theme.color.copy
import dev.tonholo.portfolio.core.ui.theme.colorScheme
import dev.tonholo.portfolio.core.ui.theme.typography
import dev.tonholo.portfolio.core.ui.theme.typography.toModifier
import dev.tonholo.portfolio.core.ui.unit.dp
import dev.tonholo.portfolio.features.articles.components.ArticleHeader
import dev.tonholo.portfolio.locale.Locale
import dev.tonholo.portfolio.locale.localStorageKey
import dev.tonholo.portfolio.renderer.Marktdown
import kotlinx.browser.localStorage
import kotlinx.browser.window
import org.jetbrains.compose.web.css.AlignSelf
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.LineStyle
import org.w3c.dom.SMOOTH
import org.w3c.dom.ScrollBehavior
import org.w3c.dom.ScrollToOptions

val ArticlePageStyles by ComponentStyle {
    base {
        Modifier.fillMaxSize()
    }
    cssRule("article") {
        typography.bodyLarge
            .toModifier()
            .fillMaxWidth()
            .display(DisplayStyle.Flex)
            .flexDirection(FlexDirection.Column)
            .gap(16.dp)
    }
    val olUlModifier = {
        Modifier.padding(start = 32.dp)
    }
    cssRule("article ul", olUlModifier)
    cssRule("article ol", olUlModifier)

    val h1h2Modifier = {
        Modifier
            .borderBottom {
                width(1.dp)
                color(colorScheme.outline)
                style(LineStyle.Solid)
            }
            .padding(bottom = 16.dp)
    }

    cssRule("article h1", h1h2Modifier)
    cssRule("article h2", h1h2Modifier)

    cssRule("article :not(pre) > code") {
        Modifier
            .padding(horizontal = 4.dp, vertical = 2.dp)
            .borderRadius(4.dp)
            .lineHeight(32.dp)
            .backgroundColor(colorScheme.outline.copy(alpha = 0.9f))
            .overflowWrap(OverflowWrap.Anywhere)
    }

    cssRule("article a") {
        Modifier.color(colorScheme.primary)
    }

    cssRule("article a:hover") {
        Modifier.color(colorScheme.onBackground)
    }
    cssRule("article table") {
        Modifier.borderCollapse(BorderCollapse.Collapse)
    }
    cssRule("article th") {
        Modifier
            .border(1.dp, LineStyle.Solid, colorScheme.outline)
            .padding(4.dp)
    }
    cssRule("article td") {
        Modifier
            .border(1.dp, LineStyle.Solid, colorScheme.outline)
            .padding(4.dp)
    }
    cssRule("article tr:nth-child(2n)") {
        Modifier
            .backgroundColor(colorScheme.surface)
    }
}

@Page(AppRoutes.Articles.ROUTE)
@Composable
fun ArticlesPage() {
    val context = rememberPageContext()
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
        },
        bottomBar = {
            Footer(
                modifier = Modifier.fillMaxWidth(),
            )
        },
        modifier = ArticlePageStyles.toModifier()
    ) {
        val document = KmpMigratrionPart1En
        document.metadata?.let { metadata ->
            ArticleHeader(
                title = metadata.title,
                description = metadata.description.orEmpty(),
                tags = metadata.tags.map { it.value },
                authors = metadata.authors,
                postedDate = metadata.publishedDateTime,
                updatedDate = metadata.lastUpdateDateTime,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Marktdown(document = document)
        TextButton(
            onClick = {
                window.scrollTo(
                    options = ScrollToOptions(
                        top = 0.0,
                        behavior = ScrollBehavior.SMOOTH,
                    ),
                )
            },
            modifier = Modifier
                .alignSelf(AlignSelf.Center)
                .margin(vertical = 16.dp)
        ) {
            Text(text = lyricist.strings.scrollToTop)
        }
    }
}
