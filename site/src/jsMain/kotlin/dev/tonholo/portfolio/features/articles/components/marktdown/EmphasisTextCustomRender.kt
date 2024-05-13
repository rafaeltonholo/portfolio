package dev.tonholo.portfolio.features.articles.components.marktdown

import androidx.compose.runtime.Composable
import dev.tonholo.marktdown.domain.content.TextElement
import dev.tonholo.marktdown.domain.renderer.MarktdownElementScope
import dev.tonholo.marktdown.domain.renderer.MarktdownRenderer
import dev.tonholo.portfolio.renderer.MarktdownElement
import org.jetbrains.compose.web.dom.I

@Composable
@MarktdownRenderer.Custom(type = TextElement.EmphasisText::class)
fun MarktdownElementScope<TextElement.EmphasisText>.EmphasisTextCustomRender() {
    I {
        element.children.forEach {
            MarktdownElement(it)
        }
    }
}
