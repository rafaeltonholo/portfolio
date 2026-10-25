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
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
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
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.components.atoms.shiki.CodeToHastOptions
import dev.tonholo.portfolio.components.atoms.shiki.DefaultTheme
import dev.tonholo.portfolio.components.atoms.shiki.Shiki
import dev.tonholo.portfolio.components.atoms.shiki.ShikiTheme
import dev.tonholo.portfolio.components.atoms.shiki.ThemeOptions
import dev.tonholo.portfolio.components.atoms.shiki.createCodeToHastOptions
import dev.tonholo.portfolio.components.atoms.shiki.shikiTheme
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.PortfolioTokens
import dev.tonholo.portfolio.theme.layout.unit.Dp
import dev.tonholo.portfolio.theme.layout.unit.dp
import dev.tonholo.portfolio.theme.typography.FontFamilies
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.LineStyle
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

private const val COPY_RESET_DELAY_MS: Long = 2000L

private const val DARK_BORDER_HEX: Int = 0x27272A
private const val LIGHT_BORDER_HEX: Int = 0xE4E4E7
private const val DARK_HEADER_BG_HEX: Int = 0x18181B
private const val LIGHT_HEADER_BG_HEX: Int = 0xF4F4F5
private const val DARK_BODY_BG_HEX: Int = 0x0F0F11
private const val LIGHT_BODY_BG_HEX: Int = 0xFAFAFA

private const val UNFOCUSED_OPACITY: Float = 0.45f
private const val FOCUSED_OPACITY: Float = 1.0f
private const val CODE_LINE_HEIGHT: Double = 1.65

private val CODE_FONT_SIZE: Dp = 13.dp
private val CARD_BORDER_RADIUS: Dp = 6.dp
private val CARD_BORDER_WIDTH: Dp = 1.dp
private val HEADER_PADDING_Y: Dp = 6.dp
private val HEADER_PADDING_X: Dp = 14.dp
private val FALLBACK_PADDING_Y: Dp = 16.dp
private val FALLBACK_PADDING_X: Dp = 20.dp

/**
 * Global stylesheet governing Shiki code block typography, line focusing, highlighting, and diffs.
 */
object ShikiStylesheet : StyleSheet() {
    init {
        "pre.shiki" style {
            margin(0.px)
            padding(1.cssRem, 1.25.cssRem)
            overflowX("auto")
            backgroundColor("transparent".unsafeCast<CSSColorValue>())
        }
        "pre.shiki code" style {
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Column)
            flexGrow(1)
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
                .fontSize(11.dp)
                .color(tokens.muted.muted)
                .letterSpacing(0.06.cssRem)
                .toAttrs(),
        ) {
            Text(language.ifBlank { "text" }.uppercase())
        }

        Button(
            attrs = Modifier
                .border(width = 0.dp, style = LineStyle.None, color = Color.rgb(0, 0, 0))
                .backgroundColor(Color.rgba(0, 0, 0, 0f))
                .color(if (hasCopied) tokens.accent.accent else tokens.muted.muted)
                .fontSize(11.dp)
                .cursor(Cursor.Pointer)
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
            transformers = shikiClient.defaultTransformers.toTypedArray()
        }
        parsedHtml = shikiClient.codeToHtml(code = unescapedCode, options = options)
    }

    Style(cssRules = ShikiStylesheet.cssRules)

    Div(attrs = ShikiCodeBlockContainerStyle.toModifier().then(modifier).toAttrs()) {
        ShikiCodeBlockHeader(language = language, unescapedCode = unescapedCode)
        ShikiCodeBlockContent(parsedHtml = parsedHtml, unescapedCode = unescapedCode)
    }
}

/**
 * Replaces HTML encoded entity sequences with literal characters.
 *
 * @return clean unescaped string.
 */
private fun String.unescapeHtmlEntities(): String = replace("&amp;", "&")
    .replace("&lt;", "<")
    .replace("&gt;", ">")
    .replace("&quot;", "\"")
    .replace("&apos;", "'")
    .replace("&#39;", "'")
    .replace("&#34;", "\"")
    .replace("&#96;", "`")
    .replace("&#123;", "{")
    .replace("&#125;", "}")
    .replace("&#91;", "[")
    .replace("&#93;", "]")
    .replace("&#40;", "(")
    .replace("&#41;", ")")

private fun ColorMode.resolveBorderColor(): Color =
    if (isDark) Color.rgb(DARK_BORDER_HEX) else Color.rgb(LIGHT_BORDER_HEX)

private fun ColorMode.resolveHeaderBg(): Color =
    if (isDark) Color.rgb(DARK_HEADER_BG_HEX) else Color.rgb(LIGHT_HEADER_BG_HEX)

private fun ColorMode.resolveCodeBlockBg(): Color =
    if (isDark) Color.rgb(DARK_BODY_BG_HEX) else Color.rgb(LIGHT_BODY_BG_HEX)
