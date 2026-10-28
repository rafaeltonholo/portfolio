package dev.tonholo.portfolio.features.articles.components.marktdown

import androidx.compose.runtime.Composable
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.marktdown.domain.content.TextElement
import dev.tonholo.marktdown.domain.renderer.MarktdownElementScope
import dev.tonholo.marktdown.domain.renderer.MarktdownRenderer
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.theme.color.copy
import dev.tonholo.portfolio.core.ui.theme.colorScheme
import dev.tonholo.portfolio.core.ui.theme.typography.toModifier
import dev.tonholo.portfolio.core.ui.unit.dp
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.H4
import org.jetbrains.compose.web.dom.H5
import org.jetbrains.compose.web.dom.H6

val AnchorTokenStyle = CssStyle {
    base {
        Modifier
            .opacity(0f)
            .color(colorScheme.onBackground.copy(alpha = 0.4f))
            .margin {
                left(8.dp)
            }
            .transition(
                Transition.of(
                    property = "opacity",
                    duration = 0.3.s,
                    timingFunction = AnimationTimingFunction.EaseInOut,
                ),
            )
            .cursor(Cursor.Pointer)
    }
}

val ArticleTitleStyle = CssStyle {
    cssRule(":hover .anchor-token") {
        Modifier.opacity(1f)
    }
}

@Composable
@MarktdownRenderer.Custom(type = TextElement.Title::class)
fun MarktdownElementScope<TextElement.Title>.TitleCustomRender() {
    val scope = rememberCoroutineScope()
    val anchor = remember(element) {
        movableContentOf {
            SpanText(
                text = "#",
                modifier = AnchorTokenStyle
                    .toModifier()
                    .onClick {
                        scope.launch {
                            window.navigator.clipboard.writeText(
                                "${window.location.href}#${element.id?.value}",
                            ).await()
                            // TODO: Change for a Toast21
                            window.alert("Copied to clipboard")
                        }
                    }
            )
        }
    }

    when (element.style) {
        TextElement.Title.Style.H1 -> H1(
            attrs = ArticleTitleStyle.toModifier()
                .then(Theme.typography.headlineLarge.toModifier())
                .toAttrs {
                    element.id?.value?.let {
                        id(it)
                    }
                },
        ) {
            drawContent()
            anchor()
        }

        TextElement.Title.Style.H2 -> H2(
            attrs = ArticleTitleStyle.toModifier()
                .then(Theme.typography.headlineMedium.toModifier())
                .toAttrs {
                    element.id?.value?.let {
                        id(it)
                    }
                },
        ) {
            drawContent()
            anchor()
        }

        TextElement.Title.Style.H3 -> H3(
            attrs = ArticleTitleStyle.toModifier()
                .then(Theme.typography.headlineSmall.toModifier())
                .toAttrs {
                    element.id?.value?.let {
                        id(it)
                    }
                },
        ) {
            drawContent()
            anchor()
        }

        TextElement.Title.Style.H4 -> H4(
            attrs = ArticleTitleStyle.toModifier()
                .then(Theme.typography.titleLarge.toModifier())
                .toAttrs {
                    element.id?.value?.let {
                        id(it)
                    }
                },
        ) {
            drawContent()
            anchor()
        }

        TextElement.Title.Style.H5 -> H5(
            attrs = ArticleTitleStyle.toModifier()
                .then(Theme.typography.titleMedium.toModifier())
                .toAttrs {
                    element.id?.value?.let {
                        id(it)
                    }
                },
        ) {
            drawContent()
            anchor()
        }

        TextElement.Title.Style.H6 -> H6(
            attrs = ArticleTitleStyle.toModifier()
                .then(Theme.typography.titleSmall.toModifier())
                .toAttrs {
                    element.id?.value?.let {
                        id(it)
                    }
                },
        ) {
            drawContent()
            anchor()
        }
    }
}
