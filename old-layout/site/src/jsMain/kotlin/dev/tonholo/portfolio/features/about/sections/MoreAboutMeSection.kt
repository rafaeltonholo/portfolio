package dev.tonholo.portfolio.features.about.sections

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.transform
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.theme.colorScheme
import dev.tonholo.portfolio.core.ui.unit.dp
import dev.tonholo.portfolio.resources.pages.MoreAboutMe
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.dom.Section

val MoreAboutMeSectionStyle = CssStyle {
    base {
        Modifier.gap(40.dp)
    }
}

val MoreAboutMeHighlightSectionStyle = CssStyle {
    base {
        Modifier
            .fillMaxWidth()
            .display(DisplayStyle.Grid)
            .gridTemplateColumns {
                repeat(count = 2) {
                    size(1.fr)
                }
            }
            .gap(rowGap = 80.dp, columnGap = 46.dp)
    }
}


val MoreAboutMeHighlightCardStyle = CssStyle {
    base {
        Modifier
            .borderRadius(8.dp)
            .background(colorScheme.surfaceVariant)
            .color(color = colorScheme.onSurfaceVariant)
    }
    cssRule(":nth-child(even)") {
        Modifier.transform {
            translateY(120.dp)
        }
    }
}

@Composable
fun MoreAboutMeSection(
    moreAboutMe: MoreAboutMe,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = MoreAboutMeSectionStyle.toModifier() then modifier,
    ) {
        Text(
            text = moreAboutMe.title,
            style = Theme.typography.displaySmall,
        )

        Section(
            attrs = MoreAboutMeHighlightSectionStyle
                .toModifier()
                .thenIf(moreAboutMe.highlights.size % 2 == 0) {
                    Modifier.padding { bottom(120.dp) }
                }
                .toAttrs(),
        ) {
            moreAboutMe.highlights.forEach { highlight ->
                HighlightCard(highlight)
            }
        }
    }
}

@Composable
private fun HighlightCard(
    highlight: MoreAboutMe.Highlight,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = MoreAboutMeHighlightCardStyle.toModifier() then modifier,
    ) {
        Image(
            src = highlight.image,
            height = 342,
            modifier = Modifier.borderRadius {
                topLeft(8.dp)
                topRight(8.dp)
            },

            )
        Column(
            modifier = Modifier
                .padding(24.dp)
                .gap(16.dp)
        ) {
            Text(
                text = highlight.title,
                style = Theme.typography.headlineLarge,
            )
            Text(
                text = highlight.description,
                style = Theme.typography.titleMedium,
            )
        }
    }
}
