package dev.tonholo.portfolio.theme.typography

import com.varabyte.kobweb.compose.css.CSSLengthNumericValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.letterSpacing
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.textTransform

/**
 * Applies every property of [style] to the receiver [Modifier] using native
 * Kobweb modifiers. Unset optional properties (letter-spacing, transform)
 * are skipped so they do not override inherited values.
 */
fun Modifier.apply(style: TextStyle): Modifier {
    var modifier = this
        .fontFamily(style.family)
        .fontSize(style.size)
        .fontWeight(style.weight)
        .lineHeight(style.lineHeight)

    val letterSpacing = style.letterSpacing
    if (letterSpacing != null) {
        modifier = modifier.letterSpacing(letterSpacing.unsafeCast<CSSLengthNumericValue>())
    }

    val transform = style.transform
    if (transform != null) {
        modifier = modifier.textTransform(transform)
    }

    return modifier
}
