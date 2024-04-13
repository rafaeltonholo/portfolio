package dev.tonholo.portfolio.features.home.sections

import androidx.compose.runtime.Composable
import cafe.adriel.lyricist.LocalStrings
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.lightened
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.flexDirection
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.toModifier
import dev.tonholo.portfolio.core.components.Divider
import dev.tonholo.portfolio.core.components.text.Paragraph
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.features.home.components.DownloadButton
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.theme.typography
import dev.tonholo.portfolio.core.ui.theme.typography.toModifier
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

val ProfileImageStyle by ComponentStyle {
    base {
        typography
            .headlineLarge.copy(
                fontSize = 2.3.em,
            )
            .toModifier()

    }
}

@Composable
fun Summary(
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current
    Column(
        modifier = modifier
            .display(DisplayStyle.Flex)
            .flexDirection(FlexDirection.Column)
            .alignItems(AlignItems.Center)
            .background(Theme.colorScheme.surface)
            .padding(topBottom = 3.em, leftRight = 1.em),
    ) {
        Image(
            src = "https://secure.gravatar.com/avatar/9c32dd678349834ba86b53dcbc4612b7?size=150",
            description = "Rafael's profile image",
            modifier = Modifier
                .borderRadius(100.percent)
                .height(150.px)
        )
        Text(
            text = "Rafael R. Tonholo",
            style = Theme.typography.headlineLarge,
            modifier = ProfileImageStyle.toModifier(),
        )
        Text(
            text = strings.screens.home.info.jobTitle,
            style = Theme.typography.labelMedium,
            modifier = Modifier.color(Theme.colorScheme.primary),
        )
        Divider()
        strings.screens.home.info.about.forEachIndexed { index, paragraph ->
            Paragraph(
                text = paragraph,
                modifier = Modifier
                    .textAlign(TextAlign.Justify)
                    .then(
                        if (index == strings.screens.home.info.about.lastIndex) {
                            Modifier
                        } else {
                            Modifier.padding(bottom = 0.5.em)
                        }
                    )
            )
        }
        Divider()
        Link(
            path = "mailto:rafael@tonholo.dev",
            text = "rafael@tonholo.dev",
            modifier = Modifier
                .padding(leftRight = 0.5.em)
                .color(Theme.colorScheme.background.lightened(0.5f))
        )
        DownloadButton(
            modifier = Modifier.margin(top = 0.5.em),
        ) {

        }
    }
}
