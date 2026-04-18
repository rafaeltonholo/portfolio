package dev.tonholo.portfolio.theme.typography

import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextTransform
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit

/**
 * Immutable description of a single typography slot.
 *
 * Sizes are encoded in `cssRem` and letter-spacing in `em` to keep the type
 * scale zoomable, per the Phase 2 "no px" rule. Line-heights stay unitless
 * doubles to match the handoff CSS exactly.
 *
 * @param family one of the stacks on [FontFamilies].
 * @param size font size in `rem` units.
 * @param weight Kobweb [FontWeight] matching the handoff weight value.
 * @param lineHeight unitless line-height multiplier.
 * @param letterSpacing optional tracking in `em`; `null` when omitted.
 * @param transform optional [TextTransform]; `null` when not applied.
 */
data class TextStyle(
    val family: String,
    val size: CSSSizeValue<CSSUnit.rem>,
    val weight: FontWeight,
    val lineHeight: Double,
    val letterSpacing: CSSSizeValue<CSSUnit.em>? = null,
    val transform: TextTransform? = null,
)
