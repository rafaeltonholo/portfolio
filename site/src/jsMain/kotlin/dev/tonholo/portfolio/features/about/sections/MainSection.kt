package dev.tonholo.portfolio.features.about.sections

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.toModifier
import dev.tonholo.portfolio.core.components.AdaptiveLayout
import dev.tonholo.portfolio.core.components.button.LinkButton
import dev.tonholo.portfolio.core.components.text.Paragraph
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.unit.dp
import dev.tonholo.portfolio.resources.pages.MainContent

val MainSectionStyles by ComponentStyle {
    base {
        Modifier.gap(80.dp)
    }
}
val MainDetailsStyles by ComponentStyle {
    base {
        Modifier.gap(24.dp)
    }
}

@Composable
fun MainSection(
    mainContent: MainContent,
    modifier: Modifier = Modifier,
) {
    AdaptiveLayout(
        modifier = MainSectionStyles.toModifier() then modifier,
        listPanel = {
            Image(
                src = "https://secure.gravatar.com/avatar/9c32dd678349834ba86b53dcbc4612b7?size=456",
                modifier = Modifier
                    .size(456.dp)
                    .borderRadius {
                        topLeft(200.dp)
                        topRight(200.dp)
                    }
            )
        },
        detailPanel = {
            Column(
                modifier = MainDetailsStyles.toModifier(),
            ) {
                Text(
                    text = mainContent.title,
                    style = Theme.typography.displaySmall,
                )
                mainContent.description.forEach { description ->
                    Paragraph(
                        text = description,
                        style = Theme.typography.titleMedium
                            .copy(color = Theme.colorScheme.onBackgroundVariant),
                    )
                }
                LinkButton(
                    path = "",
                    color = Theme.colorScheme.primary,
                ) {
                    Text(
                        text = mainContent.viewMyResume,
                        style = Theme.typography.labelLarge,
                    )
                }
            }
        }
    )
}
