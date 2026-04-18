package dev.tonholo.portfolio.components.layouts

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.extendedBy
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.theme.layout.ShellStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.dom.Div

// Mirrors the handoff `--pad-y` (80px) applied to `.article-detail` so the
// article body sits flush with the vertical rhythm used by `.resume` and
// `.project-detail` sections.
private const val ARTICLE_PADDING_Y_REM = 5.0

/**
 * Layout for long-form article detail pages. Delegates the chrome (top nav,
 * footer, language toast) to [PageLayout] and adds an article-specific
 * wrapper: the [ShellStyle] caps width at 80rem, and the inner `<div>`
 * receives an extra 5rem of top/bottom padding to match `.article-detail` in
 * the handoff CSS.
 *
 * @param modifier optional [Modifier] applied to the wrapper `<div>`.
 * @param content the article body rendered inside the shell.
 */
@Composable
fun ArticleLayout(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    PageLayout {
        Div(attrs = ArticleLayoutStyle.toModifier().then(modifier).toAttrs()) {
            content()
        }
    }
}

/**
 * Base `CssStyle` for [ArticleLayout]. Extends [ShellStyle] so the article
 * wrapper inherits the max-width cap and horizontal gutters, then layers on
 * the 5rem vertical padding called out by `.article-detail`.
 */
val ArticleLayoutStyle = ShellStyle.extendedBy {
    base {
        Modifier.padding(topBottom = ARTICLE_PADDING_Y_REM.cssRem)
    }
}
