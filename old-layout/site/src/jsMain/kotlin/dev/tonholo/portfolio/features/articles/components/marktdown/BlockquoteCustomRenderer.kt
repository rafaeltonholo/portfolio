package dev.tonholo.portfolio.features.articles.components.marktdown

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.borderLeft
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.ComponentKind
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.addVariant
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.marktdown.domain.content.TextElement
import dev.tonholo.marktdown.domain.renderer.MarktdownElementScope
import dev.tonholo.marktdown.domain.renderer.MarktdownRenderer
import dev.tonholo.portfolio.core.extensions.margin
import dev.tonholo.portfolio.core.extensions.padding
import dev.tonholo.portfolio.core.ui.theme.colorScheme
import dev.tonholo.portfolio.core.ui.unit.dp
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.dom.Blockquote

sealed interface CustomBlockquoteKind : ComponentKind

val CustomBlockquoteStyle = CssStyle<CustomBlockquoteKind> {
    base {
        Modifier
            .margin(horizontal = 16.dp)
            .padding(16.dp)
            .borderLeft {
                width(5.dp)
                color(colorScheme.outline)
                style(LineStyle.Solid)
            }
    }
}

val CustomBlockquoteNoteVariant = CustomBlockquoteStyle.addVariant {
    base {
        Modifier
            .borderLeft {
                color(colorScheme.primary)
            }
    }
}

val CustomBlockquoteWarningVariant = CustomBlockquoteStyle.addVariant {
    base {
        Modifier
            .borderLeft {
                color(colorScheme.primary)
            }
    }
}

val CustomBlockquoteCautionVariant = CustomBlockquoteStyle.addVariant {
    base {
        Modifier
            .borderLeft {
                color(colorScheme.error)
            }
    }
}

val CustomBlockquoteImportantVariant = CustomBlockquoteStyle.addVariant {
    base {
        Modifier
            .borderLeft {
                color(colorScheme.primary)
            }
    }
}

val CustomBlockquoteTipVariant = CustomBlockquoteStyle.addVariant {
    base {
        Modifier
            .borderLeft {
                color(colorScheme.primary)
            }
    }
}

@Composable
@MarktdownRenderer.Custom(type = TextElement.Blockquote::class)
fun MarktdownElementScope<TextElement.Blockquote>.CustomBlockquote() {
    val variant = when (element.type) {
        TextElement.Blockquote.Type.NOTE -> CustomBlockquoteNoteVariant
        TextElement.Blockquote.Type.WARNING -> CustomBlockquoteWarningVariant
        TextElement.Blockquote.Type.CAUTION -> CustomBlockquoteCautionVariant
        TextElement.Blockquote.Type.IMPORTANT -> CustomBlockquoteImportantVariant
        TextElement.Blockquote.Type.TIP -> CustomBlockquoteTipVariant
        null -> null
    }
    Blockquote(
        attrs = CustomBlockquoteStyle.toModifier(variant).toAttrs(),
    ) {
        drawContent()
    }
}
