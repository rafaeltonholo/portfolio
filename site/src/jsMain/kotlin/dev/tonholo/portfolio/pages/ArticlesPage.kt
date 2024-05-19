package dev.tonholo.portfolio.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.varabyte.kobweb.compose.css.BorderCollapse
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.css.OverflowWrap
import com.varabyte.kobweb.compose.foundation.layout.Column
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
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.minWidth
import com.varabyte.kobweb.compose.ui.modifiers.overflowWrap
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.top
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.navigation.Route
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toAttrs
import com.varabyte.kobweb.silk.components.style.toModifier
import dev.tonholo.portfolio.KmpMigratrionPart1En
import dev.tonholo.portfolio.core.collections.toImmutable
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
import dev.tonholo.portfolio.features.articles.sections.TableOfContents
import dev.tonholo.portfolio.features.articles.sections.toTableOfContentItem
import dev.tonholo.portfolio.locale.Locale
import dev.tonholo.portfolio.locale.localStorageKey
import dev.tonholo.portfolio.renderer.Marktdown
import kotlinx.browser.localStorage
import kotlinx.browser.window
import org.jetbrains.compose.web.css.AlignSelf
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.dom.Section
import org.w3c.dom.SMOOTH
import org.w3c.dom.ScrollBehavior
import org.w3c.dom.ScrollToOptions

val ArticlePageStyles by ComponentStyle {
    base {
        Modifier.fillMaxSize()
    }
}

val ArticlesPageContentStyle by ComponentStyle {
    base {
        Modifier
            .fillMaxWidth()
            .display(DisplayStyle.Flex)
            .flexDirection(FlexDirection.Column)
            .gap(24.dp)
    }

    Breakpoint.LG {
        Modifier
            .flexDirection(FlexDirection.RowReverse)
    }
}

val ArticleContentStyle by ComponentStyle {
    base {
        typography.bodyLarge
            .toModifier()
            .display(DisplayStyle.Flex)
            .flexDirection(FlexDirection.Column)
            .gap(16.dp)
            .minWidth(0.dp)
    }
    val olUlModifier = {
        Modifier.padding(start = 32.dp)
    }
    cssRule("ul", olUlModifier)
    cssRule("ol", olUlModifier)

    val h1h2Modifier = {
        Modifier
            .borderBottom {
                width(1.dp)
                color(colorScheme.outline)
                style(LineStyle.Solid)
            }
            .padding(bottom = 16.dp)
    }

    cssRule("h1", h1h2Modifier)
    cssRule("h2", h1h2Modifier)

    cssRule(" :not(pre) > code") {
        Modifier
            .padding(horizontal = 4.dp, vertical = 2.dp)
            .borderRadius(4.dp)
            .lineHeight(32.dp)
            .backgroundColor(colorScheme.outline.copy(alpha = 0.9f))
            .overflowWrap(OverflowWrap.Anywhere)
    }

    cssRule("a") {
        Modifier.color(colorScheme.primary)
    }

    cssRule("a:hover") {
        Modifier.color(colorScheme.onBackground)
    }
    cssRule("table") {
        Modifier.borderCollapse(BorderCollapse.Collapse)
    }
    cssRule("th") {
        Modifier
            .border(1.dp, LineStyle.Solid, colorScheme.outline)
            .padding(4.dp)
    }
    cssRule("td") {
        Modifier
            .border(1.dp, LineStyle.Solid, colorScheme.outline)
            .padding(4.dp)
    }
    cssRule("tr:nth-child(2n)") {
        Modifier
            .backgroundColor(colorScheme.surface)
    }
}

val ArticlePageTableOfContentStyle by ComponentStyle {
    base {
        Modifier
    }
    Breakpoint.LG {
        Modifier
            .height(Height.FitContent)
            .position(Position.Sticky)
            .top(16.dp)
            .minWidth(300.dp)
    }
}

@Page(AppRoutes.Articles.ROUTE)
@Composable
fun ArticlesPage() {
    val context = rememberPageContext()
    val lyricist = LocalLyricist.current
    val document = KmpMigratrionPart1En
    val tableOfContents = remember(document) {
        document.tableOfContent.items.map {
            it.toTableOfContentItem()
        }.toImmutable()
    }

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
    ) { paddingValues ->
        Column(modifier = ArticlePageStyles.toModifier().padding(paddingValues)) {
            document.metadata?.let { metadata ->
                ArticleHeader(
                    title = metadata.title,
                    description = metadata.description,
                    tags = metadata.tags.map { it.value }.toImmutable(),
                    authors = metadata.authors.toImmutable(),
                    postedDate = metadata.publishedDateTime,
                    updatedDate = metadata.lastUpdateDateTime,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Section(
                attrs = ArticlesPageContentStyle.toAttrs(),
            ) {
                TableOfContents(
                    items = tableOfContents,
                    modifier = ArticlePageTableOfContentStyle.toModifier(),
                )

                Marktdown(
                    document = document,
                    injectMetaTags = true,
                    attrs = ArticleContentStyle.toAttrs(),
                )
            }
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
}
