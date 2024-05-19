package dev.tonholo.portfolio.features.articles.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.lyricist.LocalStrings
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.flexDirection
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.toModifier
import dev.tonholo.marktdown.domain.Author
import dev.tonholo.portfolio.core.collections.ImmutableList
import dev.tonholo.portfolio.core.components.chip.Chip
import dev.tonholo.portfolio.core.components.chip.ChipDefaults
import dev.tonholo.portfolio.core.components.layout.HorizontalDivider
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.extensions.padding
import dev.tonholo.portfolio.core.foundation.layout.PaddingValues
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.theme.color.copy
import dev.tonholo.portfolio.core.ui.unit.dp
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.dom.Header

val ArticleHeaderStyles by ComponentStyle {
    base {
        Modifier
            .display(DisplayStyle.Flex)
            .flexDirection(FlexDirection.Column)
            .gap(8.dp)
            .padding(vertical = 16.dp)
    }
}
val ArticleHeaderAuthorCardStyles by ComponentStyle {
    base {
        Modifier.padding(vertical = 8.dp)
    }
}
val ArticleHeaderTagsStyles by ComponentStyle {
    base {
        Modifier
            .gap(4.dp)
            .padding(top = 4.dp, bottom = 12.dp)
    }
}

@Composable
fun ArticleHeader(
    title: String,
    authors: ImmutableList<Author>,
    postedDate: LocalDateTime,
    tags: ImmutableList<String>,
    modifier: Modifier = Modifier,
    description: String? = null,
    updatedDate: LocalDateTime? = null,
) {
    Header(
        attrs = ArticleHeaderStyles.toModifier().then(modifier).toAttrs(),
    ) {
        Text(
            text = title,
            style = Theme.typography.headlineLarge,
        )
        description?.let {
            Text(
                text = description,
                style = Theme.typography.titleLarge.copy(
                    color = Theme.colorScheme.onBackgroundVariant.copy(alpha = 0.75f),
                ),
            )
        }
        AuthorCard(
            authors = authors,
            postedDate = postedDate,
            updatedDate = updatedDate,
            modifier = ArticleHeaderAuthorCardStyles.toModifier(),
        )
        Row(
            modifier = ArticleHeaderTagsStyles.toModifier(),
        ) {
            tags.forEach { tag ->
                Chip(
                    selected = false,
                    colors = ChipDefaults.colors(
                        contentColor = Theme.colorScheme.onBackgroundVariant,
                    ),
                    contentPadding = PaddingValues(
                        vertical = 3.dp,
                        horizontal = 8.dp,
                    )
                ) {
                    Text(
                        text = tag,
                        style = Theme.typography.labelSmall,
                    )
                }
            }
        }
        HorizontalDivider()
    }
}

@Composable
fun AuthorCard(
    authors: ImmutableList<Author>,
    postedDate: LocalDateTime,
    modifier: Modifier = Modifier,
    updatedDate: LocalDateTime? = null,
) {
    val strings = LocalStrings.current
    val mainAuthor = remember(authors) {
        authors.first()
    }
    val coAuthors = remember(authors) {
        authors.drop(1).map { it.name }
    }
    Row(
        modifier = modifier.gap(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        mainAuthor.avatar?.value?.let { src ->
            Image(
                src = src,
                width = 50,
                height = 50,
                modifier = Modifier.borderRadius(100.percent),
            )
        }
        Column {
            Text(
                text = mainAuthor.name,
                style = Theme.typography.titleMedium,
            )
            if (coAuthors.isNotEmpty()) {
                Text(
                    text = strings.pages.article.coAuthors(coAuthors),
                    style = Theme.typography.titleSmall
                        .copy(
                            color = Theme.colorScheme.onBackground.copy(alpha = 0.65f),
                        ),
                )
            }
            Column {
                Text(
                    text = buildString {
                        append(strings.pages.article.posted(postedDate))

                        updatedDate?.let {
                            if (it != postedDate) {
                                append(", ")
                                append(strings.pages.article.updated(it))
                            }
                        }
                    },
                    style = Theme.typography.labelLarge
                        .copy(
                            color = Theme.colorScheme.onBackground.copy(alpha = 0.65f),
                        ),
                )
            }
        }
    }
}
