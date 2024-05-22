package dev.tonholo.portfolio.core.sections

import androidx.compose.runtime.Composable
import cafe.adriel.lyricist.LocalStrings
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.components.layout.HorizontalDivider
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.selectors.hover
import com.varabyte.kobweb.silk.style.selectors.visited
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.core.components.Logo
import dev.tonholo.portfolio.core.components.SocialMediaRow
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.extensions.padding
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.theme.colorScheme
import dev.tonholo.portfolio.core.ui.unit.dp

val FooterStyle = CssStyle {
    base {
        Modifier
    }
}

val FooterCopyrightRowStyle = CssStyle {
    base {
        Modifier
            .fillMaxWidth()
            .padding {
                top(20.dp)
            }
    }
}

val FooterDesignedByStyle = CssStyle {
    base {
        Modifier.padding(start = 4.dp)
    }

    hover {
        Modifier.color(colorScheme.primary)
    }
    visited {
        Modifier.color(colorScheme.primary)
    }
}

@Composable
fun Footer(
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current
    Column(
        modifier = FooterStyle.toModifier() then modifier,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 40.dp),
        ) {
            Logo()
            SocialMediaRow()
        }
        HorizontalDivider(modifier = Modifier.fillMaxWidth())
        Row(
            modifier = FooterCopyrightRowStyle.toModifier()
        ) {
            Text(
                text = strings.footer.builtWith,
                style = Theme.typography.titleMedium
                    .copy(color = Theme.colorScheme.onBackground),
            )
            Spacer()
            Text(
                text = strings.footer.copyright,
                style = Theme.typography.titleMedium
                    .copy(color = Theme.colorScheme.onBackground),
            )
            Link(
                path = "https://www.agbicalho.com",
                modifier = FooterDesignedByStyle.toModifier(),
            ) {
                Text(
                    text = strings.footer.designedBy,
                    style = Theme.typography.titleMedium
                        .copy(color = Theme.colorScheme.onBackground),
                )
            }
        }
    }
}
