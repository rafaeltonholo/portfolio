package dev.tonholo.portfolio.features.articles.components.marktdown

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.tonholo.kotlin.wrapper.shiki.compose.html.component.ShikiCodeBlock
import dev.tonholo.kotlin.wrapper.shiki.core.themes.DefaultTheme
import dev.tonholo.kotlin.wrapper.shiki.core.themes.ShikiTheme
import dev.tonholo.kotlin.wrapper.shiki.core.themes.shikiTheme
import dev.tonholo.kotlin.wrapper.shiki.core.transformers.notation.transformerNotationFocus
import dev.tonholo.kotlin.wrapper.shiki.core.transformers.notation.transformerNotationHighlight
import dev.tonholo.kotlin.wrapper.shiki.core.transformers.notation.transformerNotationWordHighlight
import dev.tonholo.marktdown.domain.content.CodeFence
import dev.tonholo.marktdown.domain.renderer.MarktdownElementScope
import dev.tonholo.marktdown.domain.renderer.MarktdownRenderer
import dev.tonholo.portfolio.core.ui.unit.dp

@Composable
@MarktdownRenderer.Custom(type = CodeFence::class)
fun MarktdownElementScope<CodeFence>.CodeFenceCustomRender() {
    val colorMode = ColorMode.current
    ShikiCodeBlock(
        code = element.code,
        language = element.language ?: "text",
        themeOptions = shikiTheme {
            light = ShikiTheme.OneLight
            dark = ShikiTheme.OneDarkPro
            activeTheme(
                if (colorMode == ColorMode.DARK) DefaultTheme.Dark
                else DefaultTheme.Light
            )
        },
        transformers = listOf(
            transformerNotationFocus(),
            transformerNotationHighlight(),
            transformerNotationWordHighlight(),
        ),
        paddingStart = 16.dp,
        paddingEnd = 16.dp,
        paddingTop = 16.dp,
        paddingBottom = 16.dp,
        blur = 2.dp,
        borderRadius = 8.dp,
    )
}
