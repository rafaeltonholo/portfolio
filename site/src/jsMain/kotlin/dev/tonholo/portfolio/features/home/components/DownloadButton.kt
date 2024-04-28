package dev.tonholo.portfolio.features.home.components

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.css.TextTransform
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.modifiers.textTransform
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.icons.mdi.MdiDownload
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.toModifier
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.extensions.padding
import dev.tonholo.portfolio.core.ui.theme.colorScheme
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px

val DownloadButtonStyle by ComponentStyle {
    base {
        Modifier
            .textTransform(TextTransform.Uppercase)
            .padding(vertical = 0.5.em, horizontal = 1.em)
            .background(colorScheme.secondary)
            .color(colorScheme.onSecondary)
            .textDecorationLine(TextDecorationLine.None)
            .border {
                width(1.px)
                color(colorScheme.outline)
                style(LineStyle.Solid)
            }
            .borderRadius(2.em)
            .display(DisplayStyle.Flex)
            .alignItems(AlignItems.Center)
    }
}

@Composable
fun DownloadButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        modifier = DownloadButtonStyle.toModifier() then modifier,
        onClick = { onClick() },
    ) {
        Text(
            text = "Download CV",
            modifier = Modifier.padding(right = 0.5.em),
        )
        MdiDownload()
    }
}
