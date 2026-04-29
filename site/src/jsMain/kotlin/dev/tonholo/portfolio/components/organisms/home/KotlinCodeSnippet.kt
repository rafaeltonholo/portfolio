package dev.tonholo.portfolio.components.organisms.home

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.PointerEvents
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.ariaHidden
import com.varabyte.kobweb.compose.ui.modifiers.ariaLabel
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.bottom
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.minWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onKeyDown
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.pointerEvents
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.right
import com.varabyte.kobweb.compose.ui.modifiers.role
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.modifiers.top
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.thenIfNotNull
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.color.Paper
import dev.tonholo.portfolio.theme.layout.unit.dp
import dev.tonholo.portfolio.theme.typography.FontFamilies
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text as ComposeText

// Handoff `.hero-a__code` block. Mono font, 15px / line-height 2, ink-2 body.
// Tokens: `.c-kw` accent + 600, `.c-var` ink + 500, `.c-str` ink-3, `.c-dim` muted.
private val CODE_FONT_SIZE = 15.dp
private const val CODE_LINE_HEIGHT = 2.0

// Non-breaking spaces that preserve the exact indentation shown in the handoff.
private const val INDENT_2 = "\u00A0\u00A0"
private const val INDENT_4 = "\u00A0\u00A0\u00A0\u00A0"

// Spacing between the declaration block and the "try one of the methods" tail.
private const val METHODS_TOP_MARGIN_EM = 1.0

// Accessible name announced for the whole snippet. Short and descriptive so
// assistive tech users know what the region contains before drilling into
// the per-line tokens.
private const val SNIPPET_ARIA_LABEL = "Kotlin Engineer code snippet"

// Keys that should activate a clickable `<div role="button">` per WAI-ARIA
// authoring practices. Space is included alongside Enter because native
// buttons trigger on both.
private const val KEY_ENTER = "Enter"
private const val KEY_SPACE = " "

// Width of the right-edge fade overlay shown below [Breakpoint.MD]. Sized so
// the gradient bleed is visible to the eye (roughly three mono characters at
// the snippet's 15px font-size) without eating too much of the last column.
private val FADE_WIDTH = 40.dp

/**
 * Home hero code snippet organism. Renders the syntax-coloured Kotlin block on
 * the right of [HeroEditorial], mirroring the `.hero-a__code` container in the
 * handoff CSS. Copy is hardcoded from the handoff JSX (lines 22-38 of
 * `hero.jsx`); only the [modifier] is configurable.
 *
 * Token colouring follows the `.c-*` rules in `styles.css`:
 * - `.c-dim`  -> muted comments
 * - `.c-kw`   -> keywords (accent, 600 weight)
 * - `.c-var`  -> variables (ink, 500 weight)
 * - `.c-str`  -> string literals (ink-3)
 *
 * @param modifier optional [Modifier] chained after the base styles.
 */
@Composable
fun KotlinCodeSnippet(
    onCurrentlyBuildingClick: () -> Unit = {},
    onWritingClick: () -> Unit = {},
    onHireClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val tokens = LocalPortfolioTokens.current

    val wrapperModifier = KotlinCodeSnippetWrapperStyle
        .toModifier()
        .role("region")
        .ariaLabel(SNIPPET_ARIA_LABEL)
        .then(modifier)

    Div(attrs = wrapperModifier.toAttrs()) {
        Div(
            attrs = KotlinCodeSnippetStyle
                .toModifier()
                .color(tokens.ink.ink2)
                .toAttrs(),
        ) {
            Line { Dim(text = "// 01") }
            Line {
                Kw(text = "val")
                Text(text = " ")
                Var(text = "rafael")
                Text(text = " = ")
                Kw(text = "Engineer")
                Text(text = "(")
            }
            Line {
                Text(text = "${INDENT_2}name = ")
                Str(text = "\"Rafael Tonholo\"")
                Text(text = ",")
            }
            Line {
                Text(text = "${INDENT_2}role = ")
                Str(text = "\"Senior Android\"")
                Text(text = ",")
            }
            Line {
                Text(text = "${INDENT_2}based = ")
                Str(text = "\"Halifax, Canada\"")
                Text(text = ",")
            }
            Line {
                Text(text = "${INDENT_2}stack = ")
                Kw(text = "listOf")
                Text(text = "(")
            }
            Line {
                Text(text = INDENT_4)
                Str(text = "\"Kotlin\"")
                Text(text = ", ")
                Str(text = "\"Compose\"")
                Text(text = ", ")
                Str(text = "\"KMP\"")
                Text(text = ",")
            }
            Line {
                Text(text = INDENT_4)
                Str(text = "\"Coroutines\"")
                Text(text = ", ")
                Str(text = "\"Hilt\"")
                Text(text = ", ")
                Str(text = "\"Jetpack\"")
            }
            Line { Text(text = "${INDENT_2}),") }
            Line {
                Text(text = "${INDENT_2}shipping = ")
                Kw(text = "true")
                Text(text = ",")
            }
            Line {
                Text(text = "${INDENT_2}coffee = ")
                Kw(text = "Infinity")
                Text(text = ",")
            }
            Line { Text(text = ")") }
            Line(topMarginEm = METHODS_TOP_MARGIN_EM) {
                Dim(text = "// try one of the methods ->")
            }
            Line(onClick = onCurrentlyBuildingClick) {
                Var(text = "rafael")
                Text(text = ".")
                Kw(text = "currentlyBuilding")
                Text(text = "()")
            }
            Line(onClick = onWritingClick) {
                Var(text = "rafael")
                Text(text = ".")
                Kw(text = "writing")
                Text(text = "()")
            }
            Line(onClick = onHireClick) {
                Var(text = "rafael")
                Text(text = ".")
                Kw(text = "hire")
                Text(text = "()")
            }
        }
        Div(
            attrs = KotlinCodeSnippetFadeStyle
                .toModifier()
                .ariaHidden(true)
                .toAttrs(),
        )
    }
}

/**
 * A single code line. Matches the handoff's `<div>` per-row structure. When
 * [onClick] is supplied the line becomes an ARIA button: it takes keyboard
 * focus via `tabindex="0"` and activates on both Enter and Space so
 * keyboard-only visitors can trigger the underlying navigation the same way
 * pointer users do. The handler on Space also calls `preventDefault()` to
 * suppress the browser's default page scroll.
 */
@Composable
private fun Line(
    topMarginEm: Double = 0.0,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Div(
        attrs = Modifier
            .thenIf(topMarginEm > 0.0) { Modifier.margin(top = topMarginEm.em) }
            .thenIfNotNull(onClick) { handler ->
                Modifier
                    .role("button")
                    .tabIndex(0)
                    .onClick { handler() }
                    .onKeyDown { event ->
                        if (event.key == KEY_ENTER || event.key == KEY_SPACE) {
                            event.preventDefault()
                            handler()
                        }
                    }
                    .textDecorationLine(TextDecorationLine.Underline)
                    .cursor(Cursor.Pointer)
            }
            .toAttrs(),
    ) {
        content()
    }
}

/** Plain text fragment, no token styling. */
@Composable
private fun Text(text: String) {
    ComposeText(text)
}

/** Keyword token. Maps to `.c-kw`: accent colour + 600 weight. */
@Composable
private fun Kw(text: String) {
    val tokens = LocalPortfolioTokens.current
    Span(
        attrs = KotlinCodeSnippetKwStyle
            .toModifier()
            .color(tokens.accent.accent)
            .toAttrs(),
    ) {
        ComposeText(text)
    }
}

/** Variable token. Maps to `.c-var`: ink colour + 500 weight. */
@Composable
private fun Var(text: String) {
    val tokens = LocalPortfolioTokens.current
    Span(
        attrs = KotlinCodeSnippetVarStyle
            .toModifier()
            .color(tokens.ink.ink)
            .toAttrs(),
    ) {
        ComposeText(text)
    }
}

/** String literal token. Maps to `.c-str`: ink-3 colour. */
@Composable
private fun Str(text: String) {
    val tokens = LocalPortfolioTokens.current
    Span(
        attrs = KotlinCodeSnippetStrStyle
            .toModifier()
            .color(tokens.ink.ink3)
            .toAttrs(),
    ) {
        ComposeText(text)
    }
}

/** Dim comment token. Maps to `.c-dim`: muted colour. */
@Composable
private fun Dim(text: String) {
    val tokens = LocalPortfolioTokens.current
    Span(
        attrs = KotlinCodeSnippetDimStyle
            .toModifier()
            .color(tokens.muted.muted)
            .toAttrs(),
    ) {
        ComposeText(text)
    }
}

/**
 * Base `CssStyle` for the wrapper that hosts the scroll container and the
 * mobile fade overlay. Kept as a positioning shell only so the scrollable
 * inner block keeps owning its own min-width / overflow contract.
 */
val KotlinCodeSnippetWrapperStyle = CssStyle {
    base {
        Modifier.position(Position.Relative)
    }
}

/**
 * Base `CssStyle` for [KotlinCodeSnippet]. Ports `.hero-a__code` from the
 * handoff CSS: mono font, 15px / line-height 2. Colour is applied inline on
 * the composable so it can flip with the active theme tokens.
 */
val KotlinCodeSnippetStyle = CssStyle {
    base {
        Modifier
            .fontFamily(*FontFamilies.MONO)
            .fontSize(CODE_FONT_SIZE)
            .lineHeight(CODE_LINE_HEIGHT)
            // Grid items default to min-width: min-content, which on mobile
            // lets the long non-wrapping code lines expand the hero grid past
            // the viewport. Drop the floor and allow horizontal scroll inside
            // the snippet so the page no longer overflows.
            .minWidth(0.px)
            .overflow {
                x(Overflow.Auto)
                y(Overflow.Visible)
            }
    }
}

/**
 * Right-edge fade overlay shown below [Breakpoint.MD]. Signals to mobile
 * visitors that the snippet scrolls horizontally. The gradient fades from
 * transparent into the active paper token so the vanishing edge reads as the
 * page bleeding off, not an extra chrome element. `pointerEvents: none` keeps
 * the method-call buttons underneath fully tappable, and the overlay hides
 * entirely from [Breakpoint.MD] upwards where the two-column hero grid keeps
 * the snippet from overflowing.
 */
val KotlinCodeSnippetFadeStyle = CssStyle {
    base {
        val isDark = colorMode == ColorMode.DARK
        val paperColor = if (isDark) Paper.dark.paper else Paper.light.paper
        Modifier
            .position(Position.Absolute)
            .top(0.px)
            .right(0.px)
            .bottom(0.px)
            .width(FADE_WIDTH)
            .pointerEvents(PointerEvents.None)
            .background {
                image(
                    linearGradient(dir = LinearGradient.Direction.ToRight) {
                        add(Colors.Transparent)
                        add(paperColor)
                    },
                )
            }
    }
    Breakpoint.MD {
        Modifier.display(DisplayStyle.None)
    }
}

/**
 * Base `CssStyle` for keyword tokens. Ports `.hero-a__code .c-kw`:
 * 600 weight. Colour is applied inline from the active accent token.
 */
val KotlinCodeSnippetKwStyle = CssStyle {
    base {
        Modifier.fontWeight(FontWeight.SemiBold)
    }
}

/**
 * Base `CssStyle` for variable tokens. Ports `.hero-a__code .c-var`:
 * 500 weight. Colour is applied inline from the active ink token.
 */
val KotlinCodeSnippetVarStyle = CssStyle {
    base {
        Modifier.fontWeight(FontWeight.Medium)
    }
}

/**
 * Base `CssStyle` for string literal tokens. Ports `.hero-a__code .c-str`:
 * inherits the default code weight; colour is applied inline from ink-3.
 */
val KotlinCodeSnippetStrStyle = CssStyle {}

/**
 * Base `CssStyle` for dim comment tokens. Ports `.hero-a__code .c-dim`:
 * inherits the default code weight; colour is applied inline from muted.
 */
val KotlinCodeSnippetDimStyle = CssStyle {}
