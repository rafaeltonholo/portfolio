package dev.tonholo.portfolio.resources

import cafe.adriel.lyricist.LyricistStrings
import dev.tonholo.portfolio.locale.Locales

data class Strings(
    val simple: String,
)

@LyricistStrings(languageTag = Locales.EN, default = true)
val EnStrings = Strings(
    simple = "Simple test",
)

@LyricistStrings(languageTag = Locales.PT_BR)
val PtStrings = Strings(
    simple = "Simples test",
)
