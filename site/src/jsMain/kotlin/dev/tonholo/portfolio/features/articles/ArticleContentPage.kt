package dev.tonholo.portfolio.features.articles

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.lyricist.LocalStrings
import com.varabyte.kobweb.compose.css.BorderCollapse
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.Height
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.OverflowWrap
import com.varabyte.kobweb.compose.css.TransitionTimingFunction
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
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.minWidth
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflowWrap
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.top
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.navigation.Route
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toAttrs
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.core.analytics.LocalAnalyticsManager
import dev.tonholo.portfolio.core.analytics.events.AnalyticEvent
import dev.tonholo.portfolio.core.collections.toImmutable
import dev.tonholo.portfolio.core.components.button.TextButton
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.extensions.margin
import dev.tonholo.portfolio.core.extensions.padding
import dev.tonholo.portfolio.core.foundation.layout.Scaffold
import dev.tonholo.portfolio.core.router.About
import dev.tonholo.portfolio.core.router.AppRoutes
import dev.tonholo.portfolio.core.router.Article
import dev.tonholo.portfolio.core.router.Articles
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
import dev.tonholo.portfolio.feature.FeatureFlag
import dev.tonholo.portfolio.features.articles.components.ArticleHeader
import dev.tonholo.portfolio.features.articles.sections.TableOfContents
import dev.tonholo.portfolio.features.articles.sections.toTableOfContentItem
import dev.tonholo.portfolio.locale.Locale
import dev.tonholo.portfolio.locale.localStorageKey
import dev.tonholo.portfolio.renderer.Marktdown
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.css.AlignSelf
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Section
import org.w3c.dom.HTMLScriptElement
import org.w3c.dom.SMOOTH
import org.w3c.dom.ScrollBehavior
import org.w3c.dom.ScrollToOptions
import kotlin.js.Date
import kotlin.js.json
import kotlin.time.Duration.Companion.seconds

val ArticleContentPageStyle = CssStyle {
    base {
        Modifier.fillMaxSize()
    }
}

val ArticleContentPageContentStyle = CssStyle {
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

val ArticleContentStyle = CssStyle {
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
    cssRule(" [class*=lang-] pre.shiki code span.line > span.highlighted-word") {
        val color = "color-mix(in srgb, currentcolor 20%, transparent".unsafeCast<CSSColorValue>()
        Modifier
            .border {
                width(1.dp)
                style(LineStyle.Solid)
                color(color)
            }
            .borderRadius(2.dp)
            .padding(vertical = 1.dp, horizontal = 3.dp)
            .margin(vertical = (-1).dp, horizontal = (-3).dp)
            .backgroundColor(color)
    }

    cssRule("iframe#brasil-zoomed") {
        Modifier
            .fillMaxWidth()
            .height(500.dp)
    }
}

val ArticlePageTableOfContentStyle = CssStyle {
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

@Composable
@Page(AppRoutes.ArticleContent.ROUTE)
fun ArticleContentPage() {
    val lyricist = LocalLyricist.current
    val analytics = LocalAnalyticsManager.current
    LaunchedEffect(Unit) {
        analytics.track(AnalyticEvent.PageView(language = lyricist.languageTag))
    }
    val context = rememberPageContext()
    val articleKey = context.route.params.getValue(AppRoutes.ArticleContent.ARTICLE_KEY_PARAM)
    val strings = LocalStrings.current
    val article = strings.articles[articleKey]
    val lang = context.route.params.getValue(AppRoutes.ArticleContent.LANG_PARAM)

    LaunchedEffect(lang) {
        if (lyricist.languageTag != lang) {
            lyricist.languageTag = lang
            localStorage.setItem(Locale.localStorageKey, lang)
        }
    }

    if (article == null) {
        return
    }

    Scaffold(
        topBar = {
            AppBar(
                selectedLanguage = lyricist.languageTag,
                onLocaleChange = { languageTag ->
                    lyricist.languageTag = languageTag
                    localStorage.setItem(Locale.localStorageKey, languageTag)
                    window.history.replaceState(
                        null,
                        document.title,
                        Route.Article(languageTag = languageTag, key = articleKey),
                    )
                },
                onHomeClick = {
                    context.router.navigateTo(Route.Home)
                },
                onArticleClick = {
                    context.router.navigateTo(Route.Articles)
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
        pageTitle = "${article.metadata?.title ?: strings.pages.article.title} | Rafael Tonholo"
    ) { paddingValues ->
        Column(modifier = ArticleContentPageStyle.toModifier().padding(paddingValues)) {
            val tableOfContents = remember(article) {
                article.tableOfContent.items.map {
                    it.toTableOfContentItem()
                }.toImmutable()
            }

            article.metadata?.let { metadata ->
                ArticleHeader(
                    title = metadata.title,
                    description = metadata.summary,
                    tags = metadata.tags.map { it.value }.toImmutable(),
                    authors = metadata.authors.toImmutable(),
                    postedDate = metadata.publishedDateTime,
                    updatedDate = metadata.lastUpdateDateTime,
                    modifier = Modifier.fillMaxWidth(),
                )

                metadata.postThumbnail?.let { thumbnail ->
                    Image(
                        src = thumbnail.value,
                        modifier = Modifier
                            .fillMaxWidth()
                            .objectFit(ObjectFit.Cover)
                            .borderRadius(16.dp)
                            .margin { bottom(16.dp) },
                    )
                }
            }
            Section(
                attrs = ArticleContentPageContentStyle.toAttrs(),
            ) {
                if (tableOfContents.size > 1) {
                    TableOfContents(
                        items = tableOfContents,
                        modifier = ArticlePageTableOfContentStyle.toModifier(),
                    )
                }

                Marktdown(
                    document = article,
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

            if (FeatureFlag.Comments.enabled) {
                CommentSection(
                    articleKey = articleKey,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

val CommentSectionStyle = CssStyle {
    base {
        Modifier
            .margin { top(32.dp) }
            .backgroundColor(colorScheme.surface)
            .borderRadius(8.dp)
            .transition(
                CSSTransition(
                    property = "opacity",
                    duration = 0.3.s,
                    timingFunction = TransitionTimingFunction.EaseOut,
                    delay = 0.s,
                ),
                CSSTransition(
                    property = "transform",
                    duration = 0.3.s,
                    timingFunction = TransitionTimingFunction.EaseOut,
                    delay = 0.s,
                ),
                CSSTransition(
                    property = "box-shadow",
                    duration = 0.3.s,
                    timingFunction = TransitionTimingFunction.EaseInOut,
                    delay = 0.s,
                ),
            )
            .styleModifier {
                property(propertyName = "transform-origin", value = "center top")
            }
    }
    cssRule("#graphcomment > iframe .gc-body") {
        Modifier.backgroundColor(colorScheme.background)
    }
    cssRule("> iframe") {
        Modifier
            .padding(16.dp)
            .styleModifier {
                property("color-scheme", "normal")
            }
    }
}

@Composable
fun CommentSection(
    articleKey: String,
    modifier: Modifier = Modifier,
) {
//    val scriptId = "graphcomment-script"
//    DisposableEffect(Unit) {
//        val isGcScriptPending = document.getElementById(scriptId) == null
//        console.log("isGcScriptPending", isGcScriptPending)
//
//        val gcScript = if (isGcScriptPending) {
//            (document.createElement("script") as HTMLScriptElement).apply {
//                id = scriptId
//                type = "text/javascript"
//                async = true
//                src = "https://integration.graphcomment.com/gc_graphlogin.js?${Date.now()}"
//
//                onload = {
//                    val semioParams = json(
//                        "graphcommentId" to "rtonholo-portfolio",
//                        "behaviour" to json(
//                            "uid" to articleKey,
//                        ),
//                    ).also { console.log("params: ", it) }
//                    graphLogin(
//                        semioParams,
//                    )
//                }
//
//                document.body?.appendChild(this)
//            }
//        } else null
//
//        onDispose {
//            gcScript?.let { document.removeChild(it) }
//        }
//    }
//
//    Div(
//        attrs = CommentSectionStyles.toModifier().then(modifier).toAttrs {
//            id("graphcomment")
//        },
//    ) { }

    val scriptId = "disqus-script"
    DisposableEffect(Unit) {
        val isDisqusScriptPending = document.getElementById(scriptId) == null
        console.log("isGcScriptPending", isDisqusScriptPending)

        val gcScript = if (isDisqusScriptPending) {
            (document.createElement("script") as HTMLScriptElement).apply {
                id = scriptId
                type = "text/javascript"
                src = "https://rtonholo-portfolio.disqus.com/embed.js"
                setAttribute("data-timestamp", Date.now().toString())

                window.asDynamic().disqus_config = fun() {
                    val thiz = js("this")
                    thiz.page.url = window.location.href
                    thiz.page.identifier = articleKey
                }

                document.body?.appendChild(this)
            }
        } else null

        onDispose {
            gcScript?.let { document.removeChild(it) }
        }
    }

    val colorMode = ColorMode.current
    var opacity by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while (window.asDynamic().DISQUS == null) {
            delay(1.seconds)
        }
        opacity = 1
    }

    LaunchedEffect(colorMode) {
        window.asDynamic().DISQUS?.reset(
            json("reload" to true)
        )
    }

    Div(
        attrs = CommentSectionStyle.toModifier()
            .then(modifier)
            .opacity(opacity)
            .toAttrs {
                id("disqus_thread")
            },
    ) { }
}

@JsName("__semio__gc_graphlogin")
external fun graphLogin(params: dynamic) {
    definedExternally
}
