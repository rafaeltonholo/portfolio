package dev.tonholo.portfolio.components.atoms

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.selectors.descendants
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.portfolio.components.atoms.mermaid.Mermaid
import dev.tonholo.portfolio.components.atoms.mermaid.MermaidTheme
import dev.tonholo.portfolio.extensions.unescapeHtmlEntities
import dev.tonholo.portfolio.theme.color.Paper
import dev.tonholo.portfolio.theme.layout.unit.dp
import dev.tonholo.portfolio.theme.typography.FontFamilies
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.keywords.auto
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.dom.Div
import kotlin.coroutines.cancellation.CancellationException

private val DIAGRAM_BORDER_RADIUS = 6.dp
private val DIAGRAM_BORDER_WIDTH = 1.dp
private const val DIAGRAM_PADDING_REM = 1.25
private const val DIAGRAM_MARGIN_REM = 1.5
private const val DIAGRAM_MIN_HEIGHT_REM = 8.0

/**
 * Renders a Mermaid diagram from its text definition, following the site's light or dark mode.
 *
 * Falls back to a highlighted code block when the definition can't be rendered.
 *
 * @param code the Mermaid definition, as written in a ` ```mermaid ` fence.
 * @param modifier optional [Modifier] applied to the diagram container.
 */
@Composable
fun MermaidDiagram(code: String, modifier: Modifier = Modifier) {
    val colorMode: ColorMode = ColorMode.current
    val definition: String = remember(code) { code.unescapeHtmlEntities().trimEnd() }
    var svg: String? by remember(definition, colorMode) { mutableStateOf(null) }
    var failed: Boolean by remember(definition) { mutableStateOf(false) }

    LaunchedEffect(definition, colorMode) {
        try {
            svg = Mermaid.load().render(
                code = definition,
                theme = if (colorMode.isDark) MermaidTheme.Dark else MermaidTheme.Light,
                fontFamily = FontFamilies.DISPLAY.toCssFontList(),
            )
        } catch (exception: CancellationException) {
            throw exception
        } catch (_: Throwable) {
            failed = true
        }
    }

    if (failed) {
        ShikiCodeBlock(code = code, modifier = modifier, language = "mermaid")
        return
    }
    Div(attrs = MermaidDiagramStyle.toModifier().then(modifier).toAttrs()) {
        DisposableEffect(svg) {
            scopeElement.innerHTML = svg.orEmpty()
            onDispose { }
        }
    }
}

private fun Array<String>.toCssFontList(): String =
    joinToString(separator = ", ") { name -> if (name.contains(' ')) "\"$name\"" else name }

/**
 * Card around a rendered Mermaid diagram. Wide diagrams scroll sideways instead of shrinking
 * past the point of being readable.
 */
val MermaidDiagramStyle = CssStyle {
    base {
        val isDark = colorMode.isDark
        Modifier
            .fillMaxWidth()
            .minHeight(DIAGRAM_MIN_HEIGHT_REM.cssRem)
            .margin(topBottom = DIAGRAM_MARGIN_REM.cssRem)
            .padding(DIAGRAM_PADDING_REM.cssRem)
            .borderRadius(DIAGRAM_BORDER_RADIUS)
            .border(
                width = DIAGRAM_BORDER_WIDTH,
                style = LineStyle.Solid,
                color = if (isDark) Paper.dark.paper3 else Paper.light.paper3,
            )
            .backgroundColor(if (isDark) Paper.dark.paper else Paper.light.paper2)
            .overflow(Overflow.Auto)
    }
    descendants("svg") {
        Modifier
            .display(DisplayStyle.Block)
            .margin(leftRight = auto.unsafeCast<CSSLengthOrPercentageNumericValue>())
            .maxWidth(100.percent)
            .height(auto)
    }
}
