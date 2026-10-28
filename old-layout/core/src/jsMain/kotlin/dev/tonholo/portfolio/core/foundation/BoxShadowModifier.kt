package dev.tonholo.portfolio.core.foundation

import com.varabyte.kobweb.compose.css.CSSLengthNumericValue
import com.varabyte.kobweb.compose.css.boxShadow
import com.varabyte.kobweb.compose.ui.styleModifier
import dev.tonholo.portfolio.core.ui.theme.Elevation
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.px

@DslMarker
annotation class BoxShadowDsl

interface BoxShadow {
    var offsetX: CSSLengthNumericValue
    var offsetY: CSSLengthNumericValue
    var blurRadius: CSSLengthNumericValue?
    var spreadRadius: CSSLengthNumericValue?
    var color: CSSColorValue?
    var inset: Boolean
}

private class BoxShadowImpl(
    override var offsetX: CSSLengthNumericValue = 0.px,
    override var offsetY: CSSLengthNumericValue = 0.px,
    override var blurRadius: CSSLengthNumericValue? = null,
    override var spreadRadius: CSSLengthNumericValue? = null,
    override var color: CSSColorValue? = null,
    override var inset: Boolean = false,
) : BoxShadow

class BoxShadowScope {
    internal val shadows = mutableListOf<BoxShadow>()

    @BoxShadowDsl
    fun shadow(builder: BoxShadow.() -> Unit) {
        shadows += BoxShadowImpl().apply(builder)
    }

    fun elevation(elevation: Elevation) {
        elevation.shadows.forEach { shadow ->
            shadow {
                offsetX = shadow.offsetX
                offsetY = shadow.offsetY
                blurRadius = shadow.blurRadius
                spreadRadius = shadow.spreadRadius
                color = shadow.color
                inset = shadow.inset
            }
        }
    }
}

fun List<BoxShadow>.toBoxShadowString(): String = joinToString { shadow ->
    with(shadow) {
        buildString {
            if (inset) {
                append("inset")
                append(' ')
            }
            append(offsetX)
            append(' ')
            append(offsetY)

            if (blurRadius != null) {
                append(' ')
                append(blurRadius)
            }

            if (spreadRadius != null) {
                if (blurRadius == null) {
                    append(' ')
                    append('0')
                }
                append(' ')
                append(spreadRadius)
            }

            if (color != null) {
                append(' ')
                append(color)
            }
        }
    }
}

@BoxShadowDsl
fun shadow(builder: BoxShadow.() -> Unit): BoxShadow =
    BoxShadowImpl().apply(builder)

fun Modifier.elevation(elevation: Elevation): Modifier = boxShadow {
    elevation(elevation)
}

fun Modifier.boxShadow(block: BoxShadowScope.() -> Unit): Modifier = styleModifier {
    val scope = BoxShadowScope().apply(block)
    boxShadow(value = scope.shadows.toBoxShadowString())
}
