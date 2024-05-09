package dev.tonholo.portfolio.features.home.sections

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.toModifier
import dev.tonholo.portfolio.core.components.SocialMediaRow
import dev.tonholo.portfolio.core.components.text.Paragraph
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.extensions.padding
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.unit.dp
import dev.tonholo.portfolio.resources.pages.WelcomeSection
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div

val WelcomeSectionStyle by ComponentStyle {
    base {
        Modifier
            .fillMaxSize()
            .padding(end = 24.dp)
    }
}
val WelcomeSectionDescriptionStyle by ComponentStyle {
    base {
        Modifier.margin { top(16.dp) }
    }
}

@Composable
fun WelcomeSection(
    welcomeSection: WelcomeSection,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
    ) {
        Column(
            modifier = WelcomeSectionStyle.toModifier(),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = welcomeSection.title,
                    style = Theme.typography.displayMedium,
                )
                Column(
                    modifier = WelcomeSectionDescriptionStyle.toModifier(),
                ) {
                    welcomeSection.description.forEach { paragraph ->

                    }
                }
                Paragraph(
                    text = welcomeSection.description,
                    style = Theme.typography.titleLarge,
                )
            }
            SocialMediaRow()
        }
        Div(
            attrs = {
                style {
                    width(552.dp)
                    height(414.dp)
                }
            }
        ) {
            Image(
                src = "images/home-me.jpg",
                width = 552,
                height = 414,
                modifier = Modifier
                    .borderRadius {
                        topLeft(200.dp)
                        bottomLeft(200.dp)
                    }
                    .objectFit(ObjectFit.Cover)
            )
        }
    }
}
