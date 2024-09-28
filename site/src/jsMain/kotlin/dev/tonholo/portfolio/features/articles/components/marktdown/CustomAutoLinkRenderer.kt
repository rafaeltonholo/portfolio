package dev.tonholo.portfolio.features.articles.components.marktdown

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.browser.http.HttpMethod
import com.varabyte.kobweb.browser.http.fetch
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.navigation.Link
import dev.tonholo.marktdown.domain.content.Link
import dev.tonholo.marktdown.domain.renderer.MarktdownElementScope
import dev.tonholo.marktdown.domain.renderer.MarktdownRenderer
import dev.tonholo.portfolio.core.components.text.Text
import dev.tonholo.portfolio.core.ui.unit.dp
import dev.tonholo.portfolio.features.articles.components.ArticleCard
import kotlinx.browser.window
import org.w3c.dom.parsing.DOMParser

external fun encodeURIComponent(value: String): String {
    definedExternally
}

@Composable
@MarktdownRenderer.Custom(type = Link.AutoLink::class)
fun MarktdownElementScope<Link.AutoLink>.CustomAutoLinkRenderer() {
    var metadata by remember { mutableStateOf<LinkPreview?>(null) }

    LaunchedEffect(element.link.value) {
        val response = window.fetch(
            method = HttpMethod.GET,
            resource = "https://corsproxy.io/?${encodeURIComponent(element.link.value)}",
        )
        val text = response.decodeToString()
        val parser = DOMParser()
        val document = parser.parseFromString(text, "text/html")
        val title = document.querySelector("meta[property='og:title'],meta[name='twitter:title']")
            ?.getAttribute("content")
            ?: document.head?.querySelector("title")?.textContent.orEmpty()
        val description = document.querySelector("meta[name=description]")
            ?.getAttribute("content")
            .orEmpty()
        val thumbnail = document.querySelector("meta[property='og:image']")
        metadata = LinkPreview(
            title = title,
            description = description,
            thumbnail = thumbnail?.getAttribute("content"),
        )
    }
    metadata?.let {
        ArticleCard(
            title = it.title,
            shortDescription = it.description,
            onClick = {
                window.open(element.link.value)
            },
            thumbnail = it.thumbnail,
        )
//        LinkPreviewCard(metadata = it)
    } ?: Link(element.link.value)
}

@Composable
fun LinkPreviewCard(
    metadata: LinkPreview,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .borderRadius(8.dp)
    ) {
        metadata.thumbnail?.let { thumb ->
            Image(src = thumb)
        }
        Text(text = metadata.title)
        Text(text = metadata.description)
    }
}

data class LinkPreview(
    val title: String,
    val description: String,
    val thumbnail: String? = null,
)
