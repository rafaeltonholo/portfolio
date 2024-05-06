package dev.tonholo.portfolio.features.home.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import cafe.adriel.lyricist.LocalStrings
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.ListStyleType
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.flexWrap
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.listStyle
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.whiteSpace
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.toModifier
import dev.tonholo.portfolio.core.components.TextTag
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.resources.Description
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.theme.colorScheme
import dev.tonholo.portfolio.core.ui.theme.typography
import dev.tonholo.portfolio.core.ui.theme.typography.toModifier
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexWrap
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Ul

val HistoryCardStyle by ComponentStyle {
    base {
        typography.bodyMedium.toModifier()
            .color(colorScheme.onSurface)
    }
}

@Composable
fun HistoryCard(
    company: String,
    description: Description,
    title: String,
    starting: LocalDate,
    ending: LocalDate?,
    modifier: Modifier = Modifier,
    technologies: List<String>? = null, // use immutable list instead.
) {
    val strings = LocalStrings.current
    val historyCardModifier = HistoryCardStyle.toModifier() then modifier
    Column(modifier = historyCardModifier) {
        PeriodTag(
            starting = starting,
            ending = ending,
        )
        Text(
            text = title,
            style = Theme.typography.headlineSmall,
            modifier = Modifier
                .margin { top(0.5.em) }
                .color(Theme.colorScheme.onBackground),
        )
        Text(
            text = company,
            style = Theme.typography.titleMedium,
            modifier = Modifier
                .padding {
                    top(0.2.em)
                }
                .color(Theme.colorScheme.onSurface),
        )

        Description(description)

        technologies?.let { technologies ->
            Row(
                modifier = Modifier
                    .display(DisplayStyle.Flex)
                    .flexWrap(FlexWrap.Wrap)
                    .alignItems(AlignItems.Center)
                    .padding { top(0.5.em) },
            ) {
                Text(
                    text = "",
                    style = Theme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.margin {
                        right(0.5.em)
                        bottom(0.5.em)
                    }
                )
                for (technology in technologies) {
                    val borderColor = Theme.colorScheme.secondary
                    TextTag(
                        text = technology,
                        modifier = Modifier
                            .margin {
                                right(0.8.em)
                                bottom(0.5.em)
                            }
                            .border {
                                color(borderColor)
                            },
                        color = Theme.colorScheme.primary,
                    )
                }
            }
        }
    }
}

@Composable
private fun Description(
    description: Description,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = description.summary,
            style = Theme.typography.bodyMedium,
            modifier = Modifier
                .padding { top(0.5.em) }
                .lineHeight(1.5.em)
                .whiteSpace(WhiteSpace.PreLine),
        )
        Ul(
            attrs = Modifier
                .listStyle(ListStyleType.Disc)
                .margin {
                    top(1.em)
                    left(2.em)
                }
                .toAttrs(),
        ) {
            for ((index, bulletPoint) in description.bulletPoints.withIndex()) {
                key(bulletPoint) {
                    Li(
                        attrs = if (index == 0) {
                            Modifier
                        } else {
                            Modifier.margin { top(0.5.em) }
                        }.toAttrs(),
                    ) {
                        Text(
                            text = bulletPoint,
                            style = Theme.typography.bodyMedium.copy(
                                lineHeight = 1.2.em,
                            ),
                        )
                    }
                }
            }
        }
    }
}
