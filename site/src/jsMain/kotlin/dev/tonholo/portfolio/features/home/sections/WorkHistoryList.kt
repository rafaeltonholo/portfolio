package dev.tonholo.portfolio.features.home.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import com.varabyte.kobweb.compose.css.ListStyleType
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.borderBottom
import com.varabyte.kobweb.compose.ui.modifiers.listStyle
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.firstChild
import com.varabyte.kobweb.silk.components.style.lastChild
import com.varabyte.kobweb.silk.components.style.not
import com.varabyte.kobweb.silk.components.style.toAttrs
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.features.home.components.HistoryCard
import dev.tonholo.portfolio.resources.HomePage
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.theme.colorScheme
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Ul

val WorkHistoryListItemStyle by ComponentStyle {
    base {
        Modifier
            .listStyle(ListStyleType.None)
            .margin {
                top(1.em)
            }
            .padding {
                bottom(1.em)
            }
    }

    firstChild {
        Modifier.margin { top(0.px) }
    }

    not(lastChild).invoke {
        Modifier.borderBottom {
            width(1.px)
            color(colorScheme.outline)
            style(LineStyle.Solid)
        }
    }

    lastChild {
        Modifier.padding { bottom(0.px) }
    }
}

@Composable
fun WorkHistoryList(
    title: String,
    experiences: List<HomePage.HistorySection.Experience>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = title,
            style = Theme.typography.headlineMedium,
        )
        Ul(attrs = Modifier.padding { top(1.em) }.toAttrs()) {
            for (experience in experiences) {
                key(experience.key) {
                    Li(attrs = WorkHistoryListItemStyle.toAttrs()) {
                        HistoryCard(
                            company = experience.name,
                            description = experience.description,
                            title = experience.title,
                            starting = requireNotNull(experience.starting),
                            ending = experience.ending,
                            technologies = experience.technologiesUsed,
                        )
                    }
                }
            }
        }
    }
}
