package dev.tonholo.portfolio.core.ui.theme.color

import com.varabyte.kobweb.compose.ui.graphics.Color

val Color.Companion.Unspecified: Color
    get() = "unset".unsafeCast<Color>()
