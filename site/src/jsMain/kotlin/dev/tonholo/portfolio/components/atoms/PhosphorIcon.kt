package dev.tonholo.portfolio.components.atoms

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.classNames
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.dom.I

private const val DEFAULT_ICON_SIZE_REM = 1.0
private const val ICON_LINE_HEIGHT = 1.0

/**
 * Renders a [Phosphor icon](https://phosphoricons.com/) glyph by its canonical
 * name (without the `ph-` prefix) using the Phosphor Icons web font.
 *
 * Icons scale from the parent's font size, so pass [size] to grow or shrink
 * the glyph. The rendered element is a semantic `<i>` tag carrying the
 * required `ph` weight class and the `ph-<name>` specific class.
 *
 * @param name Phosphor icon name without the `ph-` prefix (e.g. `"arrow-right"`,
 *   `"arrow-up-right"`, `"sun"`, `"moon"`, `"github-logo"`).
 * @param modifier optional [Modifier] chained after the base styles.
 * @param weight stroke weight variant; defaults to [PhosphorWeight.Regular].
 * @param size glyph size expressed in `rem`; applied via `font-size`.
 */
@Composable
fun PhosphorIcon(
    name: String,
    modifier: Modifier = Modifier,
    weight: PhosphorWeight = PhosphorWeight.Regular,
    size: CSSSizeValue<CSSUnit.rem> = DEFAULT_ICON_SIZE_REM.cssRem,
) {
    I(
        attrs = Modifier
            .classNames(weight.cssClass, "ph-$name")
            .fontSize(size)
            .lineHeight(ICON_LINE_HEIGHT)
            .then(modifier)
            .toAttrs(),
    )
}

/**
 * Weight variants of the Phosphor Icons family. Only the weights whose
 * stylesheets are actually loaded by `site/build.gradle.kts` are exposed.
 * Adding a new variant here requires appending the matching
 * `@phosphor-icons/web/src/<weight>/style.css` link under the Kobweb head
 * config; otherwise the glyph renders blank. Thin, Light, Bold, and Duotone
 * stylesheets were dropped from the bundle since no call site used them.
 */
enum class PhosphorWeight(val cssClass: String) {
    Regular("ph"),
    Fill("ph-fill"),
}
