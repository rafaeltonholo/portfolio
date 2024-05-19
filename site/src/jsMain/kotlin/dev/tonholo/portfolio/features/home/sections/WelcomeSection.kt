package dev.tonholo.portfolio.features.home.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flexDirection
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toAttrs
import com.varabyte.kobweb.silk.components.style.toModifier
import dev.tonholo.portfolio.breakpoints
import dev.tonholo.portfolio.core.components.SocialMediaRow
import dev.tonholo.portfolio.core.components.text.Paragraph
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.extensions.padding
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.unit.dp
import dev.tonholo.portfolio.resources.pages.WelcomeSection
import kotlinx.browser.window
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div

val WelcomeSectionStyle by ComponentStyle {
    base {
        Modifier.flexDirection(FlexDirection.ColumnReverse)
    }
    Breakpoint.LG {
        Modifier.flexDirection(FlexDirection.Row)
    }
}
val WelcomeSectionContainerStyle by ComponentStyle {
    base {
        Modifier
            .fillMaxSize()
            .padding(end = 24.dp)
            .gap(16.dp)
    }
}
val WelcomeSectionDescriptionStyle by ComponentStyle {
    base {
        Modifier.gap(16.dp)
    }
}
val WelcomeImageContainerStyle by ComponentStyle {
    base {
        Modifier.fillMaxWidth()
    }

    Breakpoint.LG {
        Modifier.styleModifier {
            width(552.dp)
            height(414.dp)
        }
    }
}

val WelcomeImageStyle by ComponentStyle {
    base {
        Modifier
            .objectFit(ObjectFit.Cover)
            .borderRadius(12.dp)
            .width(100.percent)
    }
    Breakpoint.LG {
        Modifier
            .width(auto)
            .borderRadius {
                topLeft(200.dp)
                bottomLeft(200.dp)
                topRight(4.dp)
                bottomRight(4.dp)
            }
    }
}

@Composable
fun WelcomeSection(
    welcomeSection: WelcomeSection,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = WelcomeSectionStyle.toModifier() then modifier,
    ) {
        Column(
            modifier = WelcomeSectionContainerStyle.toModifier(),
        ) {
            Column(
                modifier = WelcomeSectionDescriptionStyle.toModifier()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = welcomeSection.title,
                    style = Theme.typography.displayMedium,
                )
                Paragraph(
                    text = welcomeSection.description,
                    style = Theme.typography.titleLarge,
                )
            }
            SocialMediaRow()
        }
        Div(
            attrs = WelcomeImageContainerStyle.toAttrs()
        ) {
            val breakpoints = Theme.breakpoints
            var innerWidth by remember { mutableStateOf(window.innerWidth) }
            val imgWidth by remember {
                derivedStateOf {
                    if (innerWidth >= breakpoints.lg.toPx().value) {
                        552
                    } else {
                        null
                    }
                }
            }
            val imgHeight by remember {
                derivedStateOf {
                    if (innerWidth >= breakpoints.lg.toPx().value) {
                        414
                    } else {
                        314
                    }
                }
            }

            DisposableEffect(Unit) {
                window.onresize = {
                    innerWidth = window.innerWidth
                    this
                }
                onDispose {
                    window.onresize = null
                }
            }

            Image(
                src = "images/home-me.jpg",
                width = imgWidth,
                height = imgHeight,
                modifier = WelcomeImageStyle.toModifier(),
            )
        }
    }
}
