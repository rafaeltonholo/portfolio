package dev.tonholo.portfolio.components.molecules

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.dom.GenericTag
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.ariaLabel
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.flexDirection
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridAutoColumns
import com.varabyte.kobweb.compose.ui.modifiers.gridAutoFlow
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.right
import com.varabyte.kobweb.compose.ui.modifiers.role
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.modifiers.top
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.overlay.Overlay
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.selectors.descendants
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.components.atoms.PhosphorIcon
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.theme.color.Ink
import dev.tonholo.portfolio.theme.color.Muted
import dev.tonholo.portfolio.theme.color.Paper
import dev.tonholo.portfolio.theme.layout.unit.dp
import kotlinx.browser.window
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.GridAutoFlow
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent

private const val FIGURE_MARGIN_EM = 1.75
private const val IMAGE_MAX_HEIGHT_VH = 70
private val IMAGE_BORDER_RADIUS = 6.dp
private val IMAGE_BORDER_WIDTH = 1.dp
private const val CAPTION_FONT_SIZE_EM = 0.85
private const val CAPTION_LINE_HEIGHT = 1.5
private const val CAPTION_GAP_EM = 0.6
private const val ROW_GAP_REM = 1.25
private const val ZOOM_PADDING_REM = 1.5
private const val CLOSE_BUTTON_INSET_REM = 1.0
private const val CLOSE_ICON_REM = 1.5
private const val ESCAPE_KEY = "Escape"
private const val ZOOM_BACKDROP_ALPHA = 0.88f
private const val ZOOM_Z_INDEX = 100

/**
 * An article image with an optional caption. Tapping or clicking it opens the full-size image on top of the
 * page; Escape, the close button or a click on the backdrop closes it.
 *
 * @param src the image URL.
 * @param alt the text alternative describing the image.
 * @param caption visible text under the image, or null for none.
 * @param modifier optional [Modifier] applied to the `<figure>`.
 */
@Composable
fun ArticleFigure(
    src: String,
    alt: String,
    caption: String?,
    modifier: Modifier = Modifier,
) {
    var zoomed by remember { mutableStateOf(false) }
    val openLabel = LocalStrings.current.articleBody.imageOpen
    GenericTag(name = "figure", attrs = ArticleFigureStyle.toModifier().then(modifier).toAttrs()) {
        Button(
            attrs = FigureButtonStyle
                .toModifier()
                .ariaLabel("$openLabel: $alt")
                .onClick { zoomed = true }
                .toAttrs(),
        ) {
            Img(src = src, alt = alt, attrs = FigureImageStyle.toModifier().toAttrs())
        }
        caption?.let {
            GenericTag(name = "figcaption", attrs = FigureCaptionStyle.toModifier().toAttrs()) { Text(it) }
        }
    }
    if (zoomed) {
        ZoomedImage(src = src, alt = alt, onClose = { zoomed = false })
    }
}

@Composable
private fun ZoomedImage(src: String, alt: String, onClose: () -> Unit) {
    val closeLabel = LocalStrings.current.articleBody.imageClose
    val currentOnClose by rememberUpdatedState(onClose)
    DisposableEffect(Unit) {
        val onKeyDown: (Event) -> Unit = { event ->
            if ((event as? KeyboardEvent)?.key == ESCAPE_KEY) currentOnClose()
        }
        window.addEventListener("keydown", onKeyDown)
        onDispose { window.removeEventListener("keydown", onKeyDown) }
    }
    Overlay(modifier = ZoomBackdropStyle.toModifier().role("dialog").ariaLabel(alt).onClick { onClose() }) {
        Box(modifier = Modifier.fillMaxSize().padding(ZOOM_PADDING_REM.cssRem), contentAlignment = Alignment.Center) {
            Img(src = src, alt = alt, attrs = ZoomedImageStyle.toModifier().toAttrs())
        }
        Button(
            attrs = ZoomCloseButtonStyle
                .toModifier()
                .ariaLabel(closeLabel)
                .onClick { event ->
                    event.stopPropagation()
                    onClose()
                }
                .toAttrs {
                    ref { element ->
                        (element as HTMLElement).focus()
                        onDispose { }
                    }
                },
        ) {
            PhosphorIcon(name = "x", size = CLOSE_ICON_REM.cssRem)
        }
    }
}

/**
 * Lays out article figures side by side on wider screens and stacks them on phones. Use it in markdown as
 * a `{{{ ArticleFigureRow` block with one image per paragraph.
 *
 * @param modifier optional [Modifier] applied to the row.
 * @param content the figures.
 */
@Composable
fun ArticleFigureRow(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    GenericTag(name = "div", attrs = ArticleFigureRowStyle.toModifier().then(modifier).toAttrs()) {
        content()
    }
}

/** A `<figure>` block in the article column. */
val ArticleFigureStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Flex)
            .alignItems(AlignItems.Center)
            .margin(topBottom = FIGURE_MARGIN_EM.em, leftRight = 0.em)
            .flexDirection(FlexDirection.Column)
            .gap(CAPTION_GAP_EM.em)
    }
}

/** Resets the button wrapping a figure image so it looks like the image itself. */
val FigureButtonStyle = CssStyle {
    base {
        Modifier
            .padding(0.cssRem)
            .border(width = 0.dp, style = LineStyle.None, color = Colors.Transparent)
            .backgroundColor(Colors.Transparent)
            .cursor(Cursor.ZoomIn)
            .maxWidth(100.percent)
    }
}

/** The image inside a figure: fits the column, capped in height so tall screenshots stay on screen. */
val FigureImageStyle = CssStyle {
    base {
        val border = if (colorMode == ColorMode.DARK) Paper.dark.paper3 else Paper.light.paper3
        Modifier
            .display(DisplayStyle.Block)
            .maxWidth(100.percent)
            .maxHeight(IMAGE_MAX_HEIGHT_VH.vh)
            .height(auto)
            .objectFit(ObjectFit.Contain)
            .borderRadius(IMAGE_BORDER_RADIUS)
            .border(width = IMAGE_BORDER_WIDTH, style = LineStyle.Solid, color = border)
    }
}

/** The figure caption: small, muted and centred under the image. */
val FigureCaptionStyle = CssStyle {
    base {
        val muted = if (colorMode == ColorMode.DARK) Muted.dark else Muted.light
        Modifier
            .fontSize(CAPTION_FONT_SIZE_EM.em)
            .lineHeight(CAPTION_LINE_HEIGHT)
            .color(muted.muted)
            .textAlign(TextAlign.Center)
    }
}

/** One column on phones, equal columns side by side from the medium breakpoint up. */
val ArticleFigureRowStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Grid)
            .gridTemplateColumns { minmax(0.cssRem, 1.fr) }
            .gap(ROW_GAP_REM.cssRem)
            .alignItems(AlignItems.Start)
            .margin(topBottom = FIGURE_MARGIN_EM.em, leftRight = 0.em)
    }
    descendants("figure") {
        Modifier.margin(0.em)
    }
    Breakpoint.MD {
        Modifier
            .gridAutoFlow(GridAutoFlow.Column)
            .gridAutoColumns { minmax(0.cssRem, 1.fr) }
    }
}

/** The full-size image shown in the zoom overlay. */
val ZoomedImageStyle = CssStyle {
    base {
        Modifier
            .maxWidth(100.percent)
            .maxHeight(100.percent)
            .objectFit(ObjectFit.Contain)
            .borderRadius(IMAGE_BORDER_RADIUS)
    }
}

/** A dark backdrop in both colour modes, so screenshots stand out from the page behind them. */
val ZoomBackdropStyle = CssStyle {
    base {
        Modifier
            .backgroundColor(Color.rgba(r = 0, g = 0, b = 0, a = ZOOM_BACKDROP_ALPHA))
            .zIndex(ZOOM_Z_INDEX)
    }
}

/** The close button in the top corner of the zoom overlay: light on the dark backdrop. */
val ZoomCloseButtonStyle = CssStyle {
    base {
        Modifier
            .color(Ink.light.ink)
            .position(Position.Fixed)
            .top(CLOSE_BUTTON_INSET_REM.cssRem)
            .right(CLOSE_BUTTON_INSET_REM.cssRem)
            .padding(0.5.cssRem)
            .border(width = 0.dp, style = LineStyle.None, color = Colors.Transparent)
            .borderRadius(50.percent)
            .backgroundColor(Paper.light.paper)
            .cursor(Cursor.Pointer)
    }
}
