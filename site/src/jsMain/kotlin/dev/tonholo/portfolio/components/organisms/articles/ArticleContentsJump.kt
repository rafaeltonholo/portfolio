package dev.tonholo.portfolio.components.organisms.articles

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.OverscrollBehavior
import com.varabyte.kobweb.compose.css.PointerEvents
import com.varabyte.kobweb.compose.css.TextTransform
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.css.Visibility
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.ariaLabel
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderBottom
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.bottom
import com.varabyte.kobweb.compose.ui.modifiers.boxShadow
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.flexDirection
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateRows
import com.varabyte.kobweb.compose.ui.modifiers.justifyContent
import com.varabyte.kobweb.compose.ui.modifiers.left
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.overscrollBehavior
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.pointerEvents
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.right
import com.varabyte.kobweb.compose.ui.modifiers.textTransform
import com.varabyte.kobweb.compose.ui.modifiers.top
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.visibility
import com.varabyte.kobweb.compose.ui.modifiers.whiteSpace
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.selectors.descendants
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.components.atoms.PhosphorIcon
import dev.tonholo.portfolio.locale.LocalStrings
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.color.Ink
import dev.tonholo.portfolio.theme.color.Paper
import dev.tonholo.portfolio.theme.layout.SiteDimensions
import dev.tonholo.portfolio.theme.typography.FontFamilies
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.AnimationTimingFunction
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent

private const val NAV_BAR_HEIGHT_PX = 80.0
private const val ESCAPE_KEY = "Escape"
private const val PANEL_Z_INDEX = 30
private const val BACKDROP_Z_INDEX = 29

private const val FONT_SIZE_PX = 11.0
private const val LETTER_SPACING_EM = 0.08
private const val MONO_CHARACTER_ADVANCE_EM = 0.6
private const val BORDER_PX = 1.0
private const val PADDING_X_PX = 14.0
private const val ICON_PX = 16.0
private const val ICON_GAP_PX = 8.0
private const val ICON_REM = 1.0

private const val INSET_PX = 16.0
private const val PANEL_MAX_WIDTH_REM = 22.0
private const val PANEL_LIST_MAX_HEIGHT_VH = 60
private const val PANEL_LIST_PADDING_Y_PX = 6.0
private const val PANEL_RADIUS_PX = 6.0
private const val SHADOW_OFFSET_Y_PX = 8.0
private const val SHADOW_BLUR_PX = 24.0
private const val SHADOW_ALPHA = 0.22f

private const val MORPH_MS = 350
private val MORPH_EASING = AnimationTimingFunction.cubicBezier(x1 = 0.2, y1 = 0.0, x2 = 0.0, y2 = 1.0)

/**
 * The width of the closed contents button, in px. The surface animates between this width and the open
 * panel's, and a CSS transition needs a length on both ends, so the width is worked out from the label:
 *
 * `2 * border + 2 * padding + icon + gap + letters * fontSize * (monoAdvance + letterSpacing)`
 *
 * It holds because the label is set in a monospaced face, where every letter advances by the same amount.
 *
 * @param label the button's visible label.
 */
fun contentsButtonWidthPx(label: String): Double {
    val chrome = 2 * BORDER_PX + 2 * PADDING_X_PX + ICON_PX + ICON_GAP_PX
    return chrome + label.length * FONT_SIZE_PX * (MONO_CHARACTER_ADVANCE_EM + LETTER_SPACING_EM)
}

/**
 * A floating contents button for small screens. It appears once the article's own contents have scrolled out
 * of view, and tapping it grows the button into the list of sections, in place, so the reader keeps their
 * spot. Picking a section, tapping outside or pressing Escape shrinks it back. It is hidden where the contents
 * sit beside the article.
 *
 * @param contents the table-of-contents entries.
 * @param activeId the id of the entry the reader is currently on.
 * @param contentsId the element id of the article's own contents block.
 * @param modifier optional [Modifier] applied to the floating surface.
 */
@Composable
fun ArticleContentsJump(
    contents: List<TocEntry>,
    activeId: String,
    contentsId: String,
    modifier: Modifier = Modifier,
) {
    val isPastContents = rememberIsScrolledPast(elementId = contentsId)
    var isOpen by remember { mutableStateOf(false) }
    LaunchedEffect(isPastContents) {
        if (!isPastContents) isOpen = false
    }
    if (isPastContents) {
        if (isOpen) {
            CloseOnEscape(onClose = { isOpen = false })
            Div(attrs = ContentsJumpBackdropStyle.toModifier().onClick { isOpen = false }.toAttrs())
        }
        Div(attrs = ContentsJumpAnchorStyle.toModifier().toAttrs()) {
            ContentsJumpSurface(
                contents = contents,
                activeId = activeId,
                isOpen = isOpen,
                onToggle = { isOpen = !isOpen },
                onNavigate = { isOpen = false },
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun ContentsJumpSurface(
    contents: List<TocEntry>,
    activeId: String,
    isOpen: Boolean,
    onToggle: () -> Unit,
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current.articlesSidebar
    val tokens = LocalPortfolioTokens.current
    val sections: List<TocSection> = remember(contents) { buildTocSections(contents) }
    val width = if (isOpen) Modifier.width(100.percent) else Modifier.width(contentsButtonWidthPx(strings.contents).px)
    Div(attrs = ContentsJumpSurfaceStyle.toModifier().then(width).then(modifier).toAttrs()) {
        val reveal = if (isOpen) ContentsJumpRevealOpenStyle.toModifier() else Modifier
        Div(attrs = ContentsJumpRevealStyle.toModifier().then(reveal).toAttrs()) {
            Div(attrs = ContentsJumpClipStyle.toModifier().toAttrs()) {
                Div(attrs = ContentsJumpListStyle.toModifier().toAttrs()) {
                    TocSectionList(
                        sections = sections,
                        activeId = activeId,
                        expandedSectionId = null,
                        colors = TocColors(muted = tokens.muted.muted, active = tokens.ink.ink),
                        onNavigate = onNavigate,
                    )
                }
            }
        }
        Button(
            attrs = ContentsJumpToggleStyle
                .toModifier()
                .ariaLabel(if (isOpen) strings.closeContents else strings.openContents)
                .onClick { onToggle() }
                .toAttrs(),
        ) {
            PhosphorIcon(name = "list-bullets", size = ICON_REM.cssRem)
            Text(strings.contents)
            PhosphorIcon(
                name = "x",
                modifier = ContentsJumpCloseIconStyle
                    .toModifier()
                    .then(if (isOpen) ContentsJumpCloseIconOpenStyle.toModifier() else Modifier),
                size = ICON_REM.cssRem,
            )
        }
    }
}

@Composable
private fun CloseOnEscape(onClose: () -> Unit) {
    val currentOnClose by rememberUpdatedState(onClose)
    DisposableEffect(Unit) {
        val onKeyDown: (Event) -> Unit = { event ->
            if ((event as? KeyboardEvent)?.key == ESCAPE_KEY) currentOnClose()
        }
        window.addEventListener("keydown", onKeyDown)
        onDispose { window.removeEventListener("keydown", onKeyDown) }
    }
}

@Composable
private fun rememberIsScrolledPast(elementId: String): Boolean {
    var isPast by remember(elementId) { mutableStateOf(false) }
    DisposableEffect(elementId) {
        var frame: Int? = null
        val onScroll: (Event) -> Unit = {
            if (frame == null) {
                frame = window.requestAnimationFrame {
                    frame = null
                    val bottom = document.getElementById(elementId)?.getBoundingClientRect()?.bottom
                    isPast = bottom != null && bottom < NAV_BAR_HEIGHT_PX
                }
            }
        }
        window.addEventListener("scroll", onScroll)
        onDispose {
            frame?.let { window.cancelAnimationFrame(it) }
            window.removeEventListener("scroll", onScroll)
        }
    }
    return isPast
}

/** Pins the floating surface to the bottom corner on small screens without blocking taps around it. */
val ContentsJumpAnchorStyle = CssStyle {
    base {
        Modifier
            .position(Position.Fixed)
            .left(INSET_PX.px)
            .right(INSET_PX.px)
            .bottom(INSET_PX.px)
            .zIndex(PANEL_Z_INDEX)
            .display(DisplayStyle.Flex)
            .justifyContent(JustifyContent.FlexEnd)
            .pointerEvents(PointerEvents.None)
    }
    Breakpoint.LG {
        Modifier.display(DisplayStyle.None)
    }
}

/** An invisible layer behind the open panel that closes it when tapped. */
val ContentsJumpBackdropStyle = CssStyle {
    base {
        Modifier
            .position(Position.Fixed)
            .top(0.px)
            .left(0.px)
            .right(0.px)
            .bottom(0.px)
            .zIndex(BACKDROP_Z_INDEX)
    }
    Breakpoint.LG {
        Modifier.display(DisplayStyle.None)
    }
}

/** The one surface that is the button when closed and the panel when open; its width animates between both. */
val ContentsJumpSurfaceStyle = CssStyle {
    base {
        val isDark = colorMode == ColorMode.DARK
        val paper = if (isDark) Paper.dark else Paper.light
        Modifier
            .display(DisplayStyle.Flex)
            .flexDirection(FlexDirection.Column)
            .maxWidth(PANEL_MAX_WIDTH_REM.cssRem)
            .overflow(Overflow.Hidden)
            .pointerEvents(PointerEvents.Auto)
            .border(width = BORDER_PX.px, style = LineStyle.Solid, color = paper.paper3)
            .borderRadius(PANEL_RADIUS_PX.px)
            .backgroundColor(paper.paper2)
            .boxShadow(
                offsetY = SHADOW_OFFSET_Y_PX.px,
                blurRadius = SHADOW_BLUR_PX.px,
                color = Color.rgba(r = 0, g = 0, b = 0, a = SHADOW_ALPHA),
            )
            .fontFamily(*FontFamilies.MONO)
            .fontSize(FONT_SIZE_PX.px)
            .transition(Transition.of(property = "width", duration = MORPH_MS.ms, timingFunction = MORPH_EASING))
    }
}

/** Wraps the list so its height can animate from nothing to its full size; hidden from focus while closed. */
val ContentsJumpRevealStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Grid)
            .gridTemplateRows { size(0.fr) }
            .opacity(0)
            .visibility(Visibility.Hidden)
            .transition(
                Transition.of(property = "grid-template-rows", duration = MORPH_MS.ms, timingFunction = MORPH_EASING),
                Transition.of(property = "opacity", duration = MORPH_MS.ms, timingFunction = MORPH_EASING),
                Transition.of(property = "visibility", duration = 0.ms, delay = MORPH_MS.ms),
            )
    }
}

/** The open state of [ContentsJumpRevealStyle]. */
val ContentsJumpRevealOpenStyle = CssStyle {
    base {
        Modifier
            .gridTemplateRows { size(1.fr) }
            .opacity(1)
            .visibility(Visibility.Visible)
            .transition(
                Transition.of(property = "grid-template-rows", duration = MORPH_MS.ms, timingFunction = MORPH_EASING),
                Transition.of(property = "opacity", duration = MORPH_MS.ms, timingFunction = MORPH_EASING),
                Transition.of(property = "visibility", duration = 0.ms),
            )
    }
}

/** Clips the list to the animated row height, so nothing shows while the surface is closed. */
val ContentsJumpClipStyle = CssStyle {
    base {
        Modifier
            .minHeight(0.px)
            .overflow(Overflow.Hidden)
    }
}

/** The scrolling list of sections inside the open panel. */
val ContentsJumpListStyle = CssStyle {
    base {
        val border = if (colorMode == ColorMode.DARK) Paper.dark.paper3 else Paper.light.paper3
        Modifier
            .maxHeight(PANEL_LIST_MAX_HEIGHT_VH.vh)
            .overflow(overflowX = Overflow.Hidden, overflowY = Overflow.Auto)
            .overscrollBehavior(OverscrollBehavior.Contain)
            .padding(topBottom = PANEL_LIST_PADDING_Y_PX.px, leftRight = PADDING_X_PX.px)
            .borderBottom(width = BORDER_PX.px, style = LineStyle.Solid, color = border)
    }
    descendants("ul") {
        Modifier.gap(0.px)
    }
    descendants("a") {
        Modifier.width(100.percent)
    }
}

/** The row that is the whole button when closed and the panel's footer when open. */
val ContentsJumpToggleStyle = CssStyle {
    base {
        val ink = if (colorMode == ColorMode.DARK) Ink.dark else Ink.light
        Modifier
            .position(Position.Relative)
            .display(DisplayStyle.Flex)
            .alignItems(AlignItems.Center)
            .gap(ICON_GAP_PX.px)
            .width(100.percent)
            .minHeight(SiteDimensions.layout.minimumTouchSize)
            .padding(topBottom = 0.px, leftRight = PADDING_X_PX.px)
            .border(width = 0.px, style = LineStyle.None, color = Colors.Transparent)
            .backgroundColor(Colors.Transparent)
            .color(ink.ink)
            .fontFamily(*FontFamilies.MONO)
            .fontSize(FONT_SIZE_PX.px)
            .letterSpacing(LETTER_SPACING_EM.em)
            .textTransform(TextTransform.Uppercase)
            .whiteSpace(WhiteSpace.NoWrap)
            .cursor(Cursor.Pointer)
    }
}

/** The close mark at the far end of the toggle row: out of the layout, and only visible while open. */
val ContentsJumpCloseIconStyle = CssStyle {
    base {
        Modifier
            .position(Position.Absolute)
            .right(PADDING_X_PX.px)
            .opacity(0)
            .transition(Transition.of(property = "opacity", duration = MORPH_MS.ms, timingFunction = MORPH_EASING))
    }
}

/** The open state of [ContentsJumpCloseIconStyle]. */
val ContentsJumpCloseIconOpenStyle = CssStyle {
    base {
        Modifier.opacity(1)
    }
}
