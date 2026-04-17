package dev.tonholo.portfolio.locale

import androidx.compose.ui.text.intl.Locale
import cafe.adriel.lyricist.LanguageTag

object Locale {
    val current: String
        get() = Locale.current.toLanguageTag()
    const val DEFAULT = Locales.EN
}

object Locales {
    const val EN: LanguageTag = "en"
    const val PT_BR: LanguageTag = "pt-BR"
}
