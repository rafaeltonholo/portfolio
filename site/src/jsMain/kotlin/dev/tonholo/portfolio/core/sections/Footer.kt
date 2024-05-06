package dev.tonholo.portfolio.core.sections

import androidx.compose.runtime.Composable
import cafe.adriel.lyricist.LocalStrings
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignSelf
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.components.layout.HorizontalDivider
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.toModifier
import dev.tonholo.portfolio.core.components.Logo
import dev.tonholo.portfolio.core.components.SocialMediaRow
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.extensions.padding
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.unit.dp
import org.jetbrains.compose.web.css.AlignSelf

val FooterStyle by ComponentStyle {
    base {
        Modifier
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
        Text(
            text = strings.footer.copyright,
            style = Theme.typography.titleMedium,
            modifier = Modifier
                .color(Theme.colorScheme.onBackground)
                .padding {
                    top(20.dp)
                }
                .alignSelf(AlignSelf.End),
        )
    }
}
