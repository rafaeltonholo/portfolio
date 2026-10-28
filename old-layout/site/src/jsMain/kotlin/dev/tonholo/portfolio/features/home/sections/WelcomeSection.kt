package dev.tonholo.portfolio.features.home.sections

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flexDirection
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toAttrs
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.core.components.SocialMediaRow
import dev.tonholo.portfolio.core.components.text.Paragraph
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.unit.dp
import dev.tonholo.portfolio.resources.pages.WelcomeSection
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div

val WelcomeSectionStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Flex)
            .flexDirection(FlexDirection.ColumnReverse)
            .gap(16.dp)
    }
    Breakpoint.LG {
        Modifier
            .flexDirection(FlexDirection.Row)
            .gap(24.dp)
    }
}
val WelcomeSectionContainerStyle = CssStyle {
    base {
        Modifier
            .fillMaxSize()
            .gap(16.dp)
    }
}
val WelcomeSectionDescriptionStyle = CssStyle {
    base {
        Modifier.gap(16.dp)
    }
}
val WelcomeImageContainerStyle = CssStyle {
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

val WelcomeImageStyle = CssStyle {
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
    Div(
        attrs = WelcomeSectionStyle.toModifier().then(modifier).toAttrs(),
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
            Image(
                src = "images/home-me.jpg",
                width = 552,
                height = 414,
                modifier = WelcomeImageStyle.toModifier(),
            )
        }
    }
}
