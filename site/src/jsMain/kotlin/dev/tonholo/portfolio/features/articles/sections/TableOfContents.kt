package dev.tonholo.portfolio.features.articles.sections

import androidx.compose.runtime.Composable
import cafe.adriel.lyricist.LocalStrings
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.ListStyleType
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.flexDirection
import com.varabyte.kobweb.compose.ui.modifiers.listStyle
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.minWidth
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.navigation.OpenLinkStrategy
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.style.toAttrs
import com.varabyte.kobweb.silk.components.style.toModifier
import com.varabyte.kobweb.silk.theme.breakpoint.toMinWidthQuery
import dev.tonholo.marktdown.domain.MarktdownTableOfContentItem
import dev.tonholo.portfolio.core.collections.ImmutableList
import dev.tonholo.portfolio.core.collections.emptyImmutableList
import dev.tonholo.portfolio.core.collections.toImmutable
import dev.tonholo.portfolio.core.components.Details
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.extensions.padding
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.theme.colorScheme
import dev.tonholo.portfolio.core.ui.theme.typography
import dev.tonholo.portfolio.core.ui.theme.typography.toModifier
import dev.tonholo.portfolio.core.ui.unit.dp
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.s
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Nav
import org.jetbrains.compose.web.dom.Ul

data class TableOfContentItem(
    val name: String,
    val url: String,
    val children: ImmutableList<TableOfContentItem> = emptyImmutableList(),
)

fun MarktdownTableOfContentItem.toTableOfContentItem(): TableOfContentItem {
    return TableOfContentItem(
        name = title,
        url = "#${id}",
        children = children.map { it.toTableOfContentItem() }.toImmutable(),
    )
}

val TableOfContentsStyle by ComponentStyle {
    base {
        Modifier
            .display(DisplayStyle.Flex)
            .flexDirection(FlexDirection.Column)
            .backgroundColor(colorScheme.surfaceVariant)
            .color(colorScheme.onSurfaceVariant)
            .borderRadius(8.dp)
            .padding(24.dp)
            .transition(
                CSSTransition(
                    property = "min-width",
                    duration = 0.4.s,
                    timingFunction = AnimationTimingFunction.Ease,
                ),
                CSSTransition(
                    property = "height",
                    duration = 500.s,
                    timingFunction = AnimationTimingFunction.Ease,
                ),
            )
    }

    cssRule("[open] > summary") {
        Modifier.margin(bottom = 24.dp)
    }

    cssRule(Breakpoint.LG.toMinWidthQuery(), ":not([open])") {
        Modifier.minWidth(243.dp)
    }
}

val TableOfContentsNavStyle by ComponentStyle {
    cssRule("ul") {
        Modifier.listStyle(type = ListStyleType.None)
    }
    cssRule("ul > li > a") {
        typography.titleMedium
            .copy(color = colorScheme.onSurfaceVariant)
            .toModifier()
    }
    cssRule("ul > li > a:link") {
        Modifier.color(colorScheme.onSurfaceVariant)
    }
    cssRule("ul > li > a:visited") {
        Modifier.color(colorScheme.onSurfaceVariant)
    }
    cssRule("ul > li > a:hover") {
        Modifier.color(colorScheme.primary)
    }
    cssRule("ul > li > ul") {
        Modifier.margin(left = 12.dp)
    }
}

@Composable
fun TableOfContents(
    items: ImmutableList<TableOfContentItem>,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current
    Details(
        isOpened = true,
        summary = {
            Text(
                text = strings.pages.article.tableOfContents,
                style = Theme.typography.titleLarge,
                modifier = Modifier.display(DisplayStyle.Inline)
            )
        },
        modifier = TableOfContentsStyle.toModifier().then(modifier),
    ) {
        Nav(attrs = TableOfContentsNavStyle.toAttrs()) {
            Ul {
                for (item in items) {
                    TableOfContentItem(item)
                }
            }
        }
    }
}

@Composable
private fun TableOfContentItem(
    item: TableOfContentItem,
    modifier: Modifier = Modifier,
) {
    Li(attrs = modifier.toAttrs()) {
        Link(
            path = item.url,
            openInternalLinksStrategy = OpenLinkStrategy.IN_PLACE,
        ) {
            Text(
                text = item.name,
            )
        }
        if (item.children.isNotEmpty()) {
            Ul {
                for (child in item.children) {
                    TableOfContentItem(child)
                }
            }
        }
    }
}
