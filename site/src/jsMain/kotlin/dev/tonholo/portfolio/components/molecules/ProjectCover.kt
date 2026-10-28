package dev.tonholo.portfolio.components.molecules

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.graphics.ImageDecoding
import com.varabyte.kobweb.silk.components.graphics.ImageFetchPriority
import dev.tonholo.portfolio.components.atoms.AspectRatio
import dev.tonholo.portfolio.components.atoms.Placeholder
import dev.tonholo.portfolio.theme.layout.unit.dp
import org.jetbrains.compose.web.dom.Div

private val COVER_RADIUS = 4.dp
private val COVER_MARGIN_BOTTOM = 64.dp
private const val ASPECT_RATIO_WIDTH = 16
private const val ASPECT_RATIO_HEIGHT = 9

/**
 * Cover / hero media component for a project detail page.
 * Renders a full-width 16:9 covered image if [coverImage] is present;
 * otherwise falls back to a branded [Placeholder] frame.
 *
 * @param coverImage URL or local asset path of the cover image.
 * @param projectName Name of the project, used as image alt text.
 * @param placeholderLabel Label text displayed in the fallback placeholder.
 * @param modifier Optional modifier applied to the root element.
 */
@Composable
fun ProjectCover(
    coverImage: String?,
    projectName: String,
    placeholderLabel: String,
    modifier: Modifier = Modifier,
) {
    if (coverImage != null) {
        val rootModifier: Modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(width = ASPECT_RATIO_WIDTH, height = ASPECT_RATIO_HEIGHT)
            .borderRadius(COVER_RADIUS)
            .overflow(Overflow.Hidden)
            .margin(bottom = COVER_MARGIN_BOTTOM)
            .then(modifier)
        Div(attrs = rootModifier.toAttrs()) {
            val imageModifier: Modifier = Modifier
                .fillMaxSize()
                .objectFit(ObjectFit.Cover)
            Image(
                src = coverImage,
                description = projectName,
                modifier = imageModifier,
                decoding = ImageDecoding.Async,
                fetchPriority = ImageFetchPriority.High,
            )
        }
    } else {
        val placeholderModifier: Modifier = Modifier
            .borderRadius(COVER_RADIUS)
            .margin(bottom = COVER_MARGIN_BOTTOM)
            .then(modifier)
        Placeholder(
            aspectRatio = AspectRatio.Hero16x9,
            label = placeholderLabel,
            modifier = placeholderModifier,
        )
    }
}
