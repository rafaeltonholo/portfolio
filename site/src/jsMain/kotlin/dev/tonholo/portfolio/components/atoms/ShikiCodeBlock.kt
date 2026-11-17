package dev.tonholo.portfolio.components.atoms

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.css.userSelect
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.justifyContent
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.selectors.descendants
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.components.atoms.shiki.CodeToHastOptions
import dev.tonholo.portfolio.components.atoms.shiki.DefaultTheme
import dev.tonholo.portfolio.components.atoms.shiki.Shiki
import dev.tonholo.portfolio.components.atoms.shiki.ShikiTheme
import dev.tonholo.portfolio.components.atoms.shiki.ThemeOptions
import dev.tonholo.portfolio.components.atoms.shiki.createCodeToHastOptions
import dev.tonholo.portfolio.components.atoms.shiki.shikiTheme
import dev.tonholo.portfolio.extensions.unescapeHtmlEntities
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.PortfolioTokens
import dev.tonholo.portfolio.theme.color.Paper
import dev.tonholo.portfolio.theme.layout.SiteDimensions
import dev.tonholo.portfolio.theme.layout.unit.Dp
import dev.tonholo.portfolio.theme.layout.unit.dp
import dev.tonholo.portfolio.theme.typography.FontFamilies
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.CSSMediaQuery
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.StylePropertyValue
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.flexDirection
import org.jetbrains.compose.web.css.flexGrow
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.opacity
import org.jetbrains.compose.web.css.overflowX
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Code
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Pre
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Style
import org.jetbrains.compose.web.dom.Text
import kotlin.js.Json
import kotlin.js.json

private const val COPY_RESET_DELAY_MS: Long = 2000L

private const val UNFOCUSED_OPACITY: Float = 0.45f
private const val FOCUSED_OPACITY: Float = 1.0f
private const val CODE_LINE_HEIGHT: Double = 1.65

private const val LINE_NUMBER_WIDTH: String = "2ch"
private const val LINE_NUMBER_GAP: String = "1.5em"
private const val LINE_NUMBER_OPACITY: Float = 0.5f
private const val CODE_PADDING_LEFT: String = "0.1rem"
private const val GUTTER_END: String = "calc($CODE_PADDING_LEFT + $LINE_NUMBER_WIDTH + 0.75em)"
private const val GUTTER_TINT: String = "color-mix(in srgb, currentcolor 7%, transparent)"
private const val GUTTER_RULE_COLOR: String = "color-mix(in srgb, currentcolor 18%, transparent)"
private const val GUTTER_BACKGROUND: String =
    "linear-gradient(to right, $GUTTER_TINT $GUTTER_END, $GUTTER_RULE_COLOR 0, " +
        "$GUTTER_RULE_COLOR calc($GUTTER_END + 1px), transparent 0)"
private const val LINE_GUTTER_WIDTH: String = "calc($LINE_NUMBER_WIDTH + $LINE_NUMBER_GAP)"

private val CODE_FONT_SIZE: Dp = 13.dp
private val CARD_BORDER_RADIUS: Dp = 6.dp
private val CARD_BORDER_WIDTH: Dp = 1.dp
private val HEADER_PADDING_Y: Dp = 6.dp
private val HEADER_PADDING_X: Dp = 14.dp
private val HEADER_FONT_SIZE: Dp = 11.dp
private const val HEADER_LETTER_SPACING_REM: Double = 0.06
private val COARSE_POINTER = CSSMediaQuery.MediaFeature(name = "pointer", value = StylePropertyValue(value = "coarse"))
private val FALLBACK_PADDING_Y: Dp = 16.dp
private val FALLBACK_PADDING_X: Dp = 20.dp

/**
 * Darker stand-ins for the One Light token colours that fall below a 4.5:1 contrast ratio on the theme's
 * `#FAFAFA` background.
 */
private val ONE_LIGHT_READABLE_COLORS: Json = json(
    "#e45649" to "#d72f20",
    "#50a14f" to "#3f7f3e",
    "#4078f2" to "#2867f0",
    "#c18401" to "#986801",
    "#0184bc" to "#0179ad",
    "#a0a1a7" to "#707179",
)

/**
 * Lighter stand-ins for the One Dark Pro token colours (dark red and comment grey) that fall below a 4.5:1
 * contrast ratio on the dark code block background.
 */
private val ONE_DARK_READABLE_COLORS: Json = json(
    "#be5046" to "#da6a60",
    "#5c6370" to "#8a909c",
)

/**
 * One Light paints text its grammar can't parse, such as a `...` line in JSON, in white, which is invisible on
 * the theme's background. Shiki's colour replacements only cover hex colours, so the token is repainted in
 * the generated HTML.
 */
private const val ONE_LIGHT_UNPARSED_TOKEN = "<span style=\"color:white"
private const val READABLE_UNPARSED_TOKEN = "<span style=\"color:#707179"

/**
 * Global stylesheet governing Shiki code block typography, line focusing, highlighting, and diffs.
 */
object ShikiStylesheet : StyleSheet() {
    init {
        "pre.shiki" style {
            margin(0.px)
            padding(1.cssRem, 1.25.cssRem)
            property("padding-left", CODE_PADDING_LEFT)
            property("background-image", GUTTER_BACKGROUND)
            overflowX("auto")
            backgroundColor("transparent".unsafeCast<CSSColorValue>())
        }
        "pre.shiki code" style {
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Column)
            flexGrow(1)
            property("white-space", "pre-wrap")
            property("overflow-wrap", "anywhere")
        }
        "pre.shiki code .line:empty" style {
            height(1.5.cssRem)
        }
        "pre.shiki.has-focused code .line:not(.focused)" style {
            opacity(UNFOCUSED_OPACITY)
            property("filter", "blur(1.5px)")
            property("transition", "opacity 0.3s, filter 0.3s")
        }
        "pre.shiki.has-focused:hover code .line:not(.focused)" style {
            opacity(FOCUSED_OPACITY)
            property("filter", "none")
        }
        "pre.shiki code .highlighted" style {
            backgroundColor("color-mix(in srgb, currentcolor 14%, transparent)".unsafeCast<CSSColorValue>())
            borderRadius(2.px)
        }
        "pre.shiki code .diff.add" style {
            backgroundColor("color-mix(in srgb, #22c55e 16%, transparent)".unsafeCast<CSSColorValue>())
        }
        "pre.shiki code .diff.remove" style {
            backgroundColor("color-mix(in srgb, #ef4444 16%, transparent)".unsafeCast<CSSColorValue>())
        }
        "pre.shiki code .line" style {
            property("padding-left", LINE_GUTTER_WIDTH)
            property("text-indent", "calc(-1 * $LINE_GUTTER_WIDTH)")
        }
        "pre.shiki .line-number" style {
            display(DisplayStyle.InlineBlock)
            property("width", LINE_NUMBER_WIDTH)
            property("margin-right", LINE_NUMBER_GAP)
            property("text-align", "right")
            property("text-indent", "0")
            userSelect(UserSelect.None)
            opacity(LINE_NUMBER_OPACITY)
        }
    }
}

/**
 * Base [CssStyle] for the code fence container card.
 */
val ShikiCodeBlockContainerStyle = CssStyle {
    base {
        Modifier
            .fillMaxWidth()
            .margin(top = 1.5.cssRem, bottom = 1.5.cssRem)
            .borderRadius(CARD_BORDER_RADIUS)
            .border(width = CARD_BORDER_WIDTH, style = LineStyle.Solid, color = colorMode.resolveBorderColor())
            .backgroundColor(colorMode.resolveCodeBlockBg())
            .overflow(Overflow.Hidden)
            .fontFamily(*FontFamilies.MONO)
            .fontSize(CODE_FONT_SIZE)
            .lineHeight(CODE_LINE_HEIGHT)
    }
    descendants("pre", "code") {
        Modifier.fontFamily(*FontFamilies.MONO)
    }
}

/**
 * Base [CssStyle] for the code fence header bar.
 */
val ShikiCodeBlockHeaderStyle = CssStyle {
    base {
        Modifier
            .fillMaxWidth()
            .display(DisplayStyle.Flex)
            .alignItems(AlignItems.Center)
            .justifyContent(JustifyContent.SpaceBetween)
            .padding(
                top = HEADER_PADDING_Y,
                bottom = HEADER_PADDING_Y,
                left = HEADER_PADDING_X,
                right = HEADER_PADDING_X,
            )
            .border(width = CARD_BORDER_WIDTH, style = LineStyle.Solid, color = colorMode.resolveBorderColor())
            .backgroundColor(colorMode.resolveHeaderBg())
    }
    cssRule(mediaQuery = COARSE_POINTER) {
        Modifier.padding(top = 0.dp, bottom = 0.dp, left = HEADER_PADDING_X, right = 0.dp)
    }
}

/**
 * Base [CssStyle] for the copy button in the code fence header. On touch screens it grows to a full-size tap
 * target.
 */
val ShikiCopyButtonStyle = CssStyle {
    base {
        Modifier
            .border(width = 0.dp, style = LineStyle.None, color = Colors.Transparent)
            .backgroundColor(Colors.Transparent)
            .padding(0.dp)
            .fontFamily(*FontFamilies.MONO)
            .fontSize(HEADER_FONT_SIZE)
            .letterSpacing(HEADER_LETTER_SPACING_REM.cssRem)
            .cursor(Cursor.Pointer)
    }
    cssRule(mediaQuery = COARSE_POINTER) {
        Modifier
            .minHeight(SiteDimensions.layout.minimumTouchSize)
            .padding(topBottom = 0.dp, leftRight = HEADER_PADDING_X)
    }
}

/**
 * Header toolbar for code fence displaying the language label and copy action.
 *
 * @param language code language identifier.
 * @param unescapedCode raw unescaped code string copied to clipboard.
 */
@Composable
private fun ShikiCodeBlockHeader(language: String, unescapedCode: String) {
    val tokens: PortfolioTokens = LocalPortfolioTokens.current
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    var hasCopied: Boolean by remember { mutableStateOf(false) }

    Div(attrs = ShikiCodeBlockHeaderStyle.toModifier().toAttrs()) {
        Span(
            attrs = Modifier
                .fontSize(HEADER_FONT_SIZE)
                .color(tokens.muted.muted)
                .letterSpacing(HEADER_LETTER_SPACING_REM.cssRem)
                .toAttrs(),
        ) {
            Text(language.ifBlank { "text" }.uppercase())
        }

        Button(
            attrs = ShikiCopyButtonStyle
                .toModifier()
                .color(if (hasCopied) tokens.accent.accent else tokens.muted.muted)
                .onClick {
                    window.navigator.clipboard.writeText(unescapedCode)
                    hasCopied = true
                    coroutineScope.launch {
                        delay(COPY_RESET_DELAY_MS)
                        hasCopied = false
                    }
                }
                .toAttrs(),
        ) {
            Text(if (hasCopied) "COPIED!" else "COPY")
        }
    }
}

/**
 * Code body container displaying highlighted HTML or raw monospace fallback.
 *
 * @param parsedHtml pre-rendered HTML from Shiki.
 * @param unescapedCode raw code text shown when Shiki output is not yet ready.
 */
@Composable
private fun ShikiCodeBlockContent(parsedHtml: String, unescapedCode: String) {
    val tokens: PortfolioTokens = LocalPortfolioTokens.current

    Div(
        attrs = Modifier
            .fillMaxWidth()
            .overflow(Overflow.Auto)
            .toAttrs(),
    ) {
        if (parsedHtml.isBlank()) {
            Pre(
                attrs = Modifier
                    .margin(top = 0.dp, bottom = 0.dp, left = 0.dp, right = 0.dp)
                    .padding(
                        top = FALLBACK_PADDING_Y,
                        bottom = FALLBACK_PADDING_Y,
                        left = FALLBACK_PADDING_X,
                        right = FALLBACK_PADDING_X,
                    )
                    .fontFamily(*FontFamilies.MONO)
                    .color(tokens.ink.ink)
                    .toAttrs(),
            ) {
                Code {
                    Text(unescapedCode)
                }
            }
        } else {
            Div(attrs = Modifier.fillMaxWidth().toAttrs()) {
                DisposableEffect(parsedHtml) {
                    scopeElement.innerHTML = parsedHtml
                    onDispose { }
                }
            }
        }
    }
}

/**
 * Syntax-highlighted code block atom driven by Shiki.
 *
 * @param code the raw code string to render.
 * @param modifier optional [Modifier] chained onto the container.
 * @param language the code block language identifier (e.g., "kotlin", "bash").
 */
@Composable
fun ShikiCodeBlock(code: String, modifier: Modifier = Modifier, language: String = "text") {
    val colorMode: ColorMode = ColorMode.current
    var parsedHtml: String by remember { mutableStateOf("") }
    val unescapedCode: String = remember(code) { code.unescapeHtmlEntities().trimEnd() }

    val themeOptions: ThemeOptions = remember(colorMode) {
        shikiTheme {
            light = ShikiTheme.OneLight
            dark = ShikiTheme.OneDarkPro
            activeTheme(if (colorMode.isDark) DefaultTheme.Dark else DefaultTheme.Light)
        }
    }

    LaunchedEffect(unescapedCode, language, themeOptions) {
        val shikiClient: Shiki = Shiki.load()
        val options: CodeToHastOptions = createCodeToHastOptions {
            lang = language.ifBlank { "text" }
            themes = themeOptions.themes
            defaultColor = themeOptions.defaultColor
            colorReplacements = json(
                ShikiTheme.OneLight.themeName to ONE_LIGHT_READABLE_COLORS,
                ShikiTheme.OneDarkPro.themeName to ONE_DARK_READABLE_COLORS,
            )
            transformers = shikiClient.defaultTransformers.toTypedArray()
        }
        parsedHtml = shikiClient
            .codeToHtml(code = unescapedCode, options = options)
            .replace(oldValue = ONE_LIGHT_UNPARSED_TOKEN, newValue = READABLE_UNPARSED_TOKEN)
    }

    Style(cssRules = ShikiStylesheet.cssRules)

    Div(attrs = ShikiCodeBlockContainerStyle.toModifier().then(modifier).toAttrs()) {
        ShikiCodeBlockHeader(language = language, unescapedCode = unescapedCode)
        ShikiCodeBlockContent(parsedHtml = parsedHtml, unescapedCode = unescapedCode)
    }
}

private fun ColorMode.resolveBorderColor(): Color =
    if (isDark) Paper.dark.paper3 else Paper.light.paper3

private fun ColorMode.resolveHeaderBg(): Color =
    if (isDark) Paper.dark.paper2 else Paper.light.paper3

private fun ColorMode.resolveCodeBlockBg(): Color =
    if (isDark) Paper.dark.paper else Paper.light.paper2
