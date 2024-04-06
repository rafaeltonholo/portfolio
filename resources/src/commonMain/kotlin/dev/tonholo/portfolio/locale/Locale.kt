package dev.tonholo.portfolio.locale

import androidx.compose.ui.text.intl.Locale

object Locale {
    val current: String
        get() = Locale.current.toLanguageTag()
}
