package dev.tonholo.portfolio.theme.typography

import com.varabyte.kobweb.compose.css.OverflowWrap
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.overflowWrap
import com.varabyte.kobweb.compose.ui.modifiers.padding
import dev.tonholo.portfolio.theme.layout.unit.Dp
import dev.tonholo.portfolio.theme.layout.unit.dp

internal val INLINE_CODE_PADDING_HORIZONTAL: Dp = 4.dp
internal val INLINE_CODE_PADDING_VERTICAL: Dp = 2.dp
internal val INLINE_CODE_BORDER_RADIUS: Dp = 4.dp
internal val INLINE_CODE_LINE_HEIGHT: Dp = 32.dp
internal val INLINE_CODE_BG_DARK: Color = Color.rgba(r = 0x40, g = 0x40, b = 0x40, a = 0.9f)
internal val INLINE_CODE_BG_LIGHT: Color = Color.rgba(r = 0xDC, g = 0xDC, b = 0xDC, a = 0.9f)

/**
 * Applies the inline code pill styling from `@old-layout`: JetBrains Mono font,
 * 4px horizontal and 2px vertical padding, 4px corner radius, 32px line height,
 * theme-aware background, and anywhere overflow wrap.
 *
 * @param isDark true when dark theme is active.
 * @return styled [Modifier].
 */
fun Modifier.applyInlineCodeStyle(isDark: Boolean): Modifier {
    val backgroundColor: Color = if (isDark) INLINE_CODE_BG_DARK else INLINE_CODE_BG_LIGHT
    return this
        .fontFamily(*FontFamilies.MONO)
        .padding(
            topBottom = INLINE_CODE_PADDING_VERTICAL,
            leftRight = INLINE_CODE_PADDING_HORIZONTAL,
        )
        .borderRadius(INLINE_CODE_BORDER_RADIUS)
        .lineHeight(INLINE_CODE_LINE_HEIGHT)
        .backgroundColor(backgroundColor)
        .overflowWrap(OverflowWrap.Anywhere)
}
