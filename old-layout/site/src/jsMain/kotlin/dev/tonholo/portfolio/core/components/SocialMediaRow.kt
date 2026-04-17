package dev.tonholo.portfolio.core.components

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.CSSLengthNumericValue
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.setVariable
import com.varabyte.kobweb.silk.components.forms.ButtonVars
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.core.analytics.LocalAnalyticsManager
import dev.tonholo.portfolio.core.analytics.events.AnalyticEvent
import dev.tonholo.portfolio.core.ui.theme.Icon
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.theme.icons
import dev.tonholo.portfolio.core.ui.unit.dp
import org.jetbrains.compose.web.css.keywords.auto
import org.w3c.dom.url.URL

val SocialMediaRowStyle = CssStyle {
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
    val analytics = LocalAnalyticsManager.current
    Link(
        path = path,
        modifier = modifier
            .setVariable(ButtonVars.Height, auto.unsafeCast<CSSLengthNumericValue>())
            .setVariable(ButtonVars.BackgroundDefaultColor, Colors.Transparent)
            .setVariable(ButtonVars.BackgroundHoverColor, Colors.Transparent)
            .setVariable(ButtonVars.FontSize, "unset".unsafeCast<CSSLengthNumericValue>())
            .setVariable(ButtonVars.PaddingHorizontal, "unset".unsafeCast<CSSLengthNumericValue>())
            .onClick {
                analytics.track(AnalyticEvent.SocialMediaView(URL(path).hostname))
            },
    ) {
        Image(src = icon())
    }
}
