package dev.tonholo.portfolio.features.articles.components.marktdown

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.Visibility
import com.varabyte.kobweb.compose.css.visibility
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.marktdown.domain.content.CustomElement
import dev.tonholo.marktdown.domain.renderer.MarktdownElementScope
import dev.tonholo.marktdown.domain.renderer.MarktdownRenderer
import org.jetbrains.compose.web.css.CSSLengthValue
import org.jetbrains.compose.web.css.border
import org.jetbrains.compose.web.dom.Iframe

@Composable
@MarktdownRenderer.Custom(type = CustomElement::class)
fun MarktdownElementScope<CustomElement>.CustomElementRenderer() {
    when (element.name) {
        "Showcase" -> Showcase(
            preview = requireNotNull(element.attributes["preview"]),
            id = element.attributes["id"],
        )
        else -> Unit
    }
}

@Composable
fun Showcase(
    preview: String,
    modifier: Modifier = Modifier,
    id: String? = null,
) {
    val colorMode = ColorMode.current
    Iframe(
        attrs = modifier.toAttrs {
            id?.let(::id)
            attr("src", "/showcase/index.html?preview=$preview&dark=${colorMode.isDark}")
            style {
                border("none".unsafeCast<CSSLengthValue>())
                visibility(Visibility.Hidden)
            }
            attr("onload", "this.style.visibility = 'visible';")
        },
    )
}
