package dev.tonholo.portfolio.features.articles.components.marktdown

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.dom.svg.Svg
import dev.tonholo.marktdown.domain.content.ImageElement
import dev.tonholo.marktdown.domain.renderer.MarktdownElementScope
import dev.tonholo.marktdown.domain.renderer.MarktdownRenderer
import org.jetbrains.compose.web.dom.Img

@Composable
@MarktdownRenderer.Custom(type = ImageElement::class)
fun MarktdownElementScope<ImageElement>.CustomImageElement() {
    if (element.link.value.endsWith(".svg")) {
        Svg(
            attrs = {
                attr("data-src", element.link.value)
            },
            content = {},
        )
    } else {
        Img(
            src = element.link.value,
            alt = element.alt.orEmpty(),
        )
    }
}
