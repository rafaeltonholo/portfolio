package dev.tonholo.portfolio.features.home.components

import androidx.compose.runtime.Composable
import cafe.adriel.lyricist.LanguageTag
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.StyleVariable
import com.varabyte.kobweb.compose.css.TextTransform
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.setVariable
import com.varabyte.kobweb.compose.ui.modifiers.textTransform
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.layout.DividerVars
import com.varabyte.kobweb.silk.components.layout.VerticalDivider
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.selectors.hover
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.core.analytics.LocalAnalyticsManager
import dev.tonholo.portfolio.core.analytics.events.AnalyticEvent
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.ui.theme.Theme
import dev.tonholo.portfolio.core.ui.theme.colorScheme
import dev.tonholo.portfolio.core.ui.theme.typography
import dev.tonholo.portfolio.core.ui.theme.typography.toModifier
import dev.tonholo.portfolio.core.ui.unit.dp
import dev.tonholo.portfolio.locale.Locales
import org.jetbrains.compose.web.css.LineStyle

object LanguageChangerVars {
    val Color by StyleVariable<Color>()
}

val LanguageChangerStyle = CssStyle {
    base {
        typography.titleMedium.toModifier()
            .border(style = LineStyle.None)
            .background(color = Colors.Transparent)
            .color(LanguageChangerVars.Color.value(colorScheme.onSurface))
            .padding(10.dp)
            .cursor(Cursor.Pointer)
            .textTransform(TextTransform.Uppercase)
    }

    Breakpoint.MD {
        typography.labelLarge.toModifier()
    }

    hover {
        Modifier
            .color(colorScheme.secondary)
    }
}

@Composable
fun LanguageChanger(
    selected: LanguageTag,
    modifier: Modifier = Modifier,
    onLocaleChange: (LanguageTag) -> Unit,
) {
    val selectedModifier = Modifier.setVariable(
        variable = LanguageChangerVars.Color,
        value = Theme.colorScheme.primary,
    )
    val analytics = LocalAnalyticsManager.current
    Row(
        modifier = modifier.gap(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = Locales.PT_BR,
            modifier = LanguageChangerStyle
                .toModifier()
                .onClick {
                    analytics.track(
                        event = AnalyticEvent.LanguageChange(
                            previousLanguage = selected,
                            language = Locales.PT_BR,
                        )
                    )
                    onLocaleChange(Locales.PT_BR)
                }
                .thenIf(selected == Locales.PT_BR, selectedModifier),
        )
        VerticalDivider(
            modifier = Modifier
                .setVariable(DividerVars.Length, 32.dp)
                .setVariable(DividerVars.Color, Theme.colorScheme.onSurface),
        )
        Text(
            text = Locales.EN,
            modifier = LanguageChangerStyle
                .toModifier()
                .onClick {
                    analytics.track(
                        event = AnalyticEvent.LanguageChange(
                            previousLanguage = selected,
                            language = Locales.EN,
                        )
                    )
                    onLocaleChange(Locales.EN)
                }
                .thenIf(selected == Locales.EN, selectedModifier),
        )
    }
}
