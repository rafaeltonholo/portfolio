package dev.tonholo.portfolio.theme.layout

import com.varabyte.kobweb.compose.css.StyleVariable
import com.varabyte.kobweb.compose.css.functions.calc
import dev.tonholo.portfolio.theme.layout.PaddingTokens.section
import dev.tonholo.portfolio.theme.layout.unit.Dp
import dev.tonholo.portfolio.theme.layout.unit.dp
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.cssRem

private const val SIZE_PREFIX = "dimension-size"

/**
 * CSS custom properties that back the site's spacing scale. Values are
 * registered at `:root` per breakpoint in
 * [dev.tonholo.portfolio.theme.initLayoutTokens]; consumers read them via
 * [SizeTokens] (or directly via `SiteSizeVar.X.value()`) so a breakpoint flip
 * propagates to every consumer through CSS without recomposition.
 *
 * **IMPORTANT**: Do not call directly. Use [SiteDimensions.size] for normal
 * consumption.
 */
object SiteSizeVar {
    val Xsm by StyleVariable<CSSSizeValue<CSSUnit.rem>>(SIZE_PREFIX)
    val Sm by StyleVariable<CSSSizeValue<CSSUnit.rem>>(SIZE_PREFIX)
    val Md by StyleVariable<CSSSizeValue<CSSUnit.rem>>(SIZE_PREFIX)
    val Lg by StyleVariable<CSSSizeValue<CSSUnit.rem>>(SIZE_PREFIX)
    val Xl by StyleVariable<CSSSizeValue<CSSUnit.rem>>(SIZE_PREFIX)
    val Xxl by StyleVariable<CSSSizeValue<CSSUnit.rem>>(SIZE_PREFIX)
}

/** Raw scale tokens. Resolves to the active breakpoint's [SiteSizeVar] value. */
object SizeTokens {
    val Xsm get() = SiteSizeVar.Xsm.value()
    val Sm get() = SiteSizeVar.Sm.value()
    val Md get() = SiteSizeVar.Md.value()
    val Lg get() = SiteSizeVar.Lg.value()
    val Xl get() = SiteSizeVar.Xl.value()
    val Xxl get() = SiteSizeVar.Xxl.value()
}

/**
 * Semantic padding tokens. Compose on top of [SiteSizeVar] via `calc` so they
 * automatically scale with breakpoint changes to the underlying CSS variable.
 */
object PaddingTokens {
    /** Default section vertical padding (handoff `.section`). */
    val section get() = calc { SiteSizeVar.Xxl.value() + 1.cssRem }

    /** Hero section top padding. Bottom of a hero typically uses [section]. */
    val heroTop get() = calc { SiteSizeVar.Xxl.value() + 5.cssRem }

    /** Page top padding for content pages without a hero. */
    val pageTop get() = calc { SiteSizeVar.Xxl.value() + 2.cssRem }

    /** Footer vertical padding. */
    val footerVertical get() = calc { SiteSizeVar.Xxl.value() + 2.cssRem }
}

/** Semantic margin tokens. */
object MarginTokens {
    val body get() = calc { SiteSizeVar.Xxl.value() + 2.cssRem }
}

/** Fixed layout tokens that do not vary with breakpoint. */
object LayoutTokens {
    /** Maximum width for page-level content containers (80rem = 1280px). */
    val contentMaxWidth: CSSSizeValue<CSSUnit.rem> get() = 80.cssRem
    val button: ButtonSizes = ButtonSizes
    val minimumTouchSize = 44.dp
}

object ButtonSizes {
    val icon: IconButtonSizes = IconButtonSizes
}

object IconButtonSizes {
    val Small: Dp get() = 12.dp
    val Medium: Dp get() = 16.dp
    val Large: Dp get() = 24.dp
}

/**
 * Single facade exposing all layout tokens. Call sites read e.g.
 * `SiteDimensions.padding.section` or `SiteDimensions.size.Xl`.
 */
object SiteDimensions {
    val size: SizeTokens = SizeTokens
    val padding: PaddingTokens = PaddingTokens
    val margin: MarginTokens = MarginTokens
    val layout: LayoutTokens = LayoutTokens
}
