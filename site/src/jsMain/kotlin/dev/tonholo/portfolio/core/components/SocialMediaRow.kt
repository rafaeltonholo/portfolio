package dev.tonholo.portfolio.core.components

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.CSSLengthNumericValue
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.setVariable
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.forms.ButtonVars
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.toModifier
import dev.tonholo.portfolio.core.ui.theme.Icon
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.theme.icons
import dev.tonholo.portfolio.core.ui.unit.dp
import org.jetbrains.compose.web.css.keywords.auto

val SocialMediaRowStyle by ComponentStyle {
    base {
        Modifier.gap(24.dp)
    }
}

@Composable
fun SocialMediaRow(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = SocialMediaRowStyle.toModifier() then modifier,
    ) {
        SocialMediaButton(
            path = "https://www.linkedin.com/in/rafaeltonholo/",
            icon = Theme.icons.LinkedIn,
        )
        SocialMediaButton(
            path = "https://github.com/rafaeltonholo",
            icon = Theme.icons.Github,
        )
        SocialMediaButton(
            path = "https://twitter.com/rafaeltonholo",
            icon = Theme.icons.Twitter,
        )
    }
}

@Composable
private fun SocialMediaButton(
    path: String,
    icon: Icon.Asset,
    modifier: Modifier = Modifier,
) {
    Link(
        path = path,
        modifier = modifier
            .setVariable(ButtonVars.Height, auto.unsafeCast<CSSLengthNumericValue>())
            .setVariable(ButtonVars.BackgroundDefaultColor, Colors.Transparent)
            .setVariable(ButtonVars.BackgroundHoverColor, Colors.Transparent)
            .setVariable(ButtonVars.FontSize, "unset".unsafeCast<CSSLengthNumericValue>())
            .setVariable(ButtonVars.PaddingHorizontal, "unset".unsafeCast<CSSLengthNumericValue>()),
    ) {
        Image(src = icon())
    }
}
