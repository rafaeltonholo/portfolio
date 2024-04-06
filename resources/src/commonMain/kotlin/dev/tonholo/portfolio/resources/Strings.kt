package dev.tonholo.portfolio.resources

import cafe.adriel.lyricist.LyricistStrings

data class Strings(
    val simple: String,
)

@LyricistStrings(languageTag = "en", default = true)
val EnStrings = Strings(
    simple = "Simple test",
)

@LyricistStrings(languageTag = "pt")
val PtStrings = Strings(
    simple = "Simples test",
)
