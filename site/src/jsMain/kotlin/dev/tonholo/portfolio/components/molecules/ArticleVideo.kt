package dev.tonholo.portfolio.components.molecules

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.dom.GenericTag
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.theme.color.Paper
import dev.tonholo.portfolio.theme.layout.unit.dp
import kotlinx.browser.window
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Video
import org.w3c.dom.HTMLVideoElement

private const val VIDEO_MAX_HEIGHT_VH = 75
private const val REDUCED_MOTION_QUERY = "(prefers-reduced-motion: reduce)"
private val VIDEO_BORDER_RADIUS = 6.dp
private val VIDEO_BORDER_WIDTH = 1.dp

/**
 * A short silent clip in an article, such as a screen recording. It loops muted with playback controls, and
 * only starts on its own when the reader hasn't asked for reduced motion.
 *
 * @param src the video URL.
 * @param poster the image shown before the video plays.
 * @param modifier optional [Modifier] applied to the `<figure>`.
 * @param caption visible text under the video, or null for none.
 */
@Composable
fun ArticleVideo(
    src: String,
    poster: String,
    modifier: Modifier = Modifier,
    caption: String? = null,
) {
    GenericTag(name = "figure", attrs = ArticleFigureStyle.toModifier().then(modifier).toAttrs()) {
        Video(attrs = ArticleVideoStyle.toModifier().toAttrs()) {
            DisposableEffect(src, poster) {
                (scopeElement as HTMLVideoElement).configureAsSilentLoop(src = src, poster = poster)
                onDispose { }
            }
        }
        caption?.let {
            GenericTag(name = "figcaption", attrs = FigureCaptionStyle.toModifier().toAttrs()) { Text(it) }
        }
    }
}

private fun HTMLVideoElement.configureAsSilentLoop(src: String, poster: String) {
    muted = true
    loop = true
    controls = true
    asDynamic().playsInline = true
    this.poster = poster
    this.src = src
    if (!window.matchMedia(REDUCED_MOTION_QUERY).matches) {
        autoplay = true
        play().catch { }
    }
}

/** The video element: fits the column, capped in height so phone recordings stay on screen. */
val ArticleVideoStyle = CssStyle {
    base {
        val border = if (colorMode == ColorMode.DARK) Paper.dark.paper3 else Paper.light.paper3
        Modifier
            .display(DisplayStyle.Block)
            .maxWidth(100.percent)
            .maxHeight(VIDEO_MAX_HEIGHT_VH.vh)
            .objectFit(ObjectFit.Contain)
            .borderRadius(VIDEO_BORDER_RADIUS)
            .border(width = VIDEO_BORDER_WIDTH, style = LineStyle.Solid, color = border)
    }
}
