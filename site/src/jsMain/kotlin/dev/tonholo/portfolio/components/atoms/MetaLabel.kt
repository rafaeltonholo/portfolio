package dev.tonholo.portfolio.components.atoms

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.toModifier
import dev.tonholo.portfolio.theme.LocalPortfolioTokens
import dev.tonholo.portfolio.theme.PortfolioTokens
import dev.tonholo.portfolio.theme.typography.Typography
import dev.tonholo.portfolio.theme.typography.apply
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text as ComposeText

/**
 * Small uppercase mono label used across the site (section numbers, hero meta,
 * project kind). Maps to `.meta` and `.meta-accent` in the handoff CSS.
 *
 * @param text label copy; rendered in the mono font with uppercase letterforms.
 * @param modifier optional [Modifier] chained after the base styles.
 * @param variant [MetaLabelVariant.Default] uses `--muted`; [MetaLabelVariant.Accent]
 *   uses `--accent`.
 */
@Composable
fun MetaLabel(
    text: String,
    modifier: Modifier = Modifier,
    variant: MetaLabelVariant = MetaLabelVariant.Default,
) {
    val tokens = LocalPortfolioTokens.current
    val color = variant.color(tokens = tokens)

    val finalModifier = MetaLabelStyle
        .toModifier()
        .apply(Typography.metaSmall)
        .color(color)
        .then(modifier)

    Span(attrs = finalModifier.toAttrs()) {
        ComposeText(text)
    }
}

/**
 * Variant axis for [MetaLabel]. Kept small on purpose; new color overrides
 * belong in the base text, not in new variants.
 */
enum class MetaLabelVariant {
    Default,
    Accent,
    ;

    /** Resolves the label color for this variant from the active [tokens]. */
    fun color(tokens: PortfolioTokens): Color = when (this) {
        Default -> tokens.muted.muted
        Accent -> tokens.accent.accent
    }
}

/**
 * Base `CssStyle` for [MetaLabel]. Empty at the base level; typography and
 * color are applied inline by the composable via token-aware modifiers.
 */
val MetaLabelStyle = CssStyle {}
