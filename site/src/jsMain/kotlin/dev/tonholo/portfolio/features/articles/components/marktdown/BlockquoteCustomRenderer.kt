package dev.tonholo.portfolio.features.articles.components.marktdown

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.borderLeft
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.addVariant
import com.varabyte.kobweb.silk.components.style.toAttrs
import dev.tonholo.marktdown.domain.content.TextElement
import dev.tonholo.marktdown.domain.renderer.MarktdownElementScope
import dev.tonholo.marktdown.domain.renderer.MarktdownRenderer
import dev.tonholo.portfolio.core.extensions.margin
import dev.tonholo.portfolio.core.extensions.padding
import dev.tonholo.portfolio.core.ui.theme.colorScheme
import dev.tonholo.portfolio.core.ui.unit.dp
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.dom.Blockquote

val CustomBlockquoteStyles by ComponentStyle {
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

val CustomBlockquoteNoteVariant by CustomBlockquoteStyles.addVariant {
    base {
        Modifier
            .borderLeft {
                color(colorScheme.primary)
            }
    }
}

val CustomBlockquoteWarningVariant by CustomBlockquoteStyles.addVariant {
    base {
        Modifier
            .borderLeft {
                color(colorScheme.primary)
            }
    }
}

val CustomBlockquoteCautionVariant by CustomBlockquoteStyles.addVariant {
    base {
        Modifier
            .borderLeft {
                color(colorScheme.error)
            }
    }
}

val CustomBlockquoteImportantVariant by CustomBlockquoteStyles.addVariant {
    base {
        Modifier
            .borderLeft {
                color(colorScheme.primary)
            }
    }
}

val CustomBlockquoteTipVariant by CustomBlockquoteStyles.addVariant {
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
        attrs = CustomBlockquoteStyles.toAttrs(variant),
    ) {
        drawContent()
    }
}
