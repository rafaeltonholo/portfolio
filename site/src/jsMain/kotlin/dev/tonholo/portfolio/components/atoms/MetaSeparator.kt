package dev.tonholo.portfolio.components.atoms

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.flexShrink
import com.varabyte.kobweb.compose.ui.modifiers.setVariable
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import org.jetbrains.compose.web.css.cssRem

private const val META_DOT_SIZE_REM = 0.1875 // 3px at 1rem=16px

/**
 * Tiny static [Dot] sized to read as inline punctuation. Used as a separator
 * between meta segments or as a list bullet, replacing the prior unicode
 * middle-dot glyphs that fed string-concatenation paths.
 *
 * @param modifier optional [Modifier] chained after the base sizing.
 * @param color optional override; defaults to the active muted token.
 */
@Composable
fun MetaSeparator(modifier: Modifier = Modifier, color: Color? = null) {
    val tokens = LocalPortfolioTokens.current
    Dot(
        variant = DotVariant.Static,
        color = color ?: tokens.muted.muted,
        modifier = Modifier
            .setVariable(DotVars.Size, META_DOT_SIZE_REM.cssRem)
            .flexShrink(0)
            .then(modifier),
    )
}
