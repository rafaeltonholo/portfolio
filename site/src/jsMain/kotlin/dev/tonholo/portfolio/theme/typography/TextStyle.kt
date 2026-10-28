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
    val family: Array<String>,
    val size: CSSSizeValue<CSSUnit.rem>,
    val weight: FontWeight,
    val lineHeight: Double,
    val letterSpacing: CSSSizeValue<CSSUnit.em>? = null,
    val transform: TextTransform? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TextStyle) return false
        if (!family.contentEquals(other.family)) return false
        if (size != other.size) return false
        if (weight != other.weight) return false
        if (lineHeight != other.lineHeight) return false
        if (letterSpacing != other.letterSpacing) return false
        if (transform != other.transform) return false
        return true
    }

    override fun hashCode(): Int {
        var result = family.contentHashCode()
        result = 31 * result + size.hashCode()
        result = 31 * result + weight.hashCode()
        result = 31 * result + lineHeight.hashCode()
        result = 31 * result + (letterSpacing?.hashCode() ?: 0)
        result = 31 * result + (transform?.hashCode() ?: 0)
        return result
    }
}
