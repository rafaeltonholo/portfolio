package dev.tonholo.portfolio.core.foundation.text

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.StyleVariable
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.dom.ElementRefScope
import com.varabyte.kobweb.compose.dom.registerRefScope
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.setVariable
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.navigation.Anchor
import com.varabyte.kobweb.navigation.OpenLinkStrategy
import com.varabyte.kobweb.navigation.UpdateHistoryMode
import com.varabyte.kobweb.silk.style.ComponentKind
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.CssStyleVariant
import com.varabyte.kobweb.silk.style.addVariant
import com.varabyte.kobweb.silk.style.selectors.hover
import com.varabyte.kobweb.silk.style.selectors.link
import com.varabyte.kobweb.silk.style.selectors.visited
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.style.vars.color.ColorVar
import dev.tonholo.portfolio.core.ui.theme.Theme
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLElement

object LinkTextVars {
    val DefaultColor by StyleVariable<CSSColorValue>()
    val VisitedColor by StyleVariable<CSSColorValue>()
}

sealed interface LinkTextKind : ComponentKind

/**
 * Style to use with [A] tags to give them app's colors.
 */
val LinkTextStyle = CssStyle<LinkTextKind> {
    base {
        Modifier.textDecorationLine(TextDecorationLine.None)
    }

    link {
        Modifier.color(LinkTextVars.DefaultColor.value())
    }
    visited {
        Modifier.color(LinkTextVars.VisitedColor.value())
    }
    hover {
        Modifier.textDecorationLine(TextDecorationLine.Underline)
    }
}

val UndecoratedLinkVariant = LinkTextStyle.addVariant {
    hover {
        Modifier.textDecorationLine(TextDecorationLine.None)
    }
}

val UncoloredLinkVariant = LinkTextStyle.addVariant {
    val colorModifier = Modifier.color(ColorVar.value())
    link { colorModifier }
    visited { colorModifier }
}

val AlwaysUnderlinedLinkVariant = LinkTextStyle.addVariant {
    base {
        Modifier.textDecorationLine(TextDecorationLine.Underline)
    }
}

@Composable
fun LinkText(
    path: String,
    text: String? = null,
    modifier: Modifier = Modifier,
    variant: CssStyleVariant<LinkTextKind>? = null,
    openInternalLinksStrategy: OpenLinkStrategy? = null,
    openExternalLinksStrategy: OpenLinkStrategy? = null,
    updateHistoryMode: UpdateHistoryMode? = null,
    autoPrefix: Boolean = true,
    ref: ElementRefScope<HTMLElement>? = null,
) {
    Anchor(
        href = path,
        attrs = LinkTextStyle
            .toModifier(variant)
            .then(modifier)
            // receive as parameter.
            .setVariable(LinkTextVars.DefaultColor, Theme.colorScheme.primary)
            .setVariable(LinkTextVars.VisitedColor, Theme.colorScheme.onBackgroundVariant)
            .toAttrs(),
        openInternalLinksStrategy,
        openExternalLinksStrategy,
        updateHistoryMode,
        autoPrefix
    ) {
        registerRefScope(ref)
        Text(value = text ?: path)
    }
}
