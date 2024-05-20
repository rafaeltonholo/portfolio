package dev.tonholo.portfolio.features.about.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.navigation.Route
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toModifier
import dev.tonholo.portfolio.core.components.AdaptiveLayout
import dev.tonholo.portfolio.core.components.button.LinkButton
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.extensions.ResponsiveValues
import dev.tonholo.portfolio.core.extensions.responsiveStateOf
import dev.tonholo.portfolio.core.router.Resume
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.unit.dp
import dev.tonholo.portfolio.resources.pages.MainContent
import org.jetbrains.compose.web.css.AlignItems

val MainSectionStyles by ComponentStyle {
    base {
        Modifier
            .fillMaxWidth()
            .gap(40.dp)
            .alignItems(AlignItems.Center)
    }
    Breakpoint.LG {
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
            val size by responsiveStateOf(ResponsiveValues(372, 456))
            Column(
                modifier = Modifier.size(size.dp),
            ) {
                Image(
                    src = "images/writing.jpg",
                    width = size,
                    height = size,
                    modifier = Modifier
                        .borderRadius {
                            topLeft(200.dp)
                            topRight(200.dp)
                        }
                        .objectFit(ObjectFit.Cover)
                )
            }
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
                    Text(
                        text = description,
                        style = Theme.typography.titleMedium
                            .copy(color = Theme.colorScheme.onBackgroundVariant),
                    )
                }
                LinkButton(
                    path = Route.Resume,
                    color = Theme.colorScheme.primary,
                ) {
                    Text(
                        text = mainContent.viewMyResume,
                        style = Theme.typography.labelLarge,
                    )
                }
            }
        },
    )
}
