package dev.tonholo.portfolio.features.articles.components

import androidx.compose.runtime.Composable
import cafe.adriel.lyricist.LocalStrings
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.core.components.button.PrimaryButton
import dev.tonholo.portfolio.core.components.text.Paragraph
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.theme.colorScheme
import dev.tonholo.portfolio.core.ui.unit.dp
import org.jetbrains.compose.web.css.LineStyle

val ArticleCardStyle = CssStyle {
    base {
        Modifier
            .fillMaxWidth()
            .backgroundColor(colorScheme.surfaceVariant)
            .color(colorScheme.onSurfaceVariant)
            .border {
                width(1.dp)
                style(LineStyle.Solid)
                color(colorScheme.outline)
            }
            .borderRadius(8.dp)
    }

    Breakpoint.MD {
        Modifier
            .maxWidth(435.dp)
    }

    Breakpoint.LG {
        Modifier
            .maxWidth(552.dp)
    }
}

val ArticleCardContentStyle = CssStyle {
    base {
        Modifier
            .padding(24.dp)
            .gap(16.dp)
    }
}

@Composable
fun ArticleCard(
    title: String,
    shortDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    thumbnail: String? = null,
) {
    val strings = LocalStrings.current
    Column(
        modifier = ArticleCardStyle.toModifier() then modifier,
    ) {
        thumbnail?.let {
            Image(
                src = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .objectFit(ObjectFit.Cover)
                    .borderRadius {
                        topLeft(8.dp)
                        topRight(8.dp)
                    },
            )
        }
        Column(modifier = ArticleCardContentStyle.toModifier()) {
            Text(
                text = title,
                style = Theme.typography.headlineSmall,
            )
            Paragraph(
                text = shortDescription,
                style = Theme.typography.bodyLarge,
            )
            PrimaryButton(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = strings.pages.article.readMore,
                    style = Theme.typography.labelLarge,
                )
            }
        }
    }
}
