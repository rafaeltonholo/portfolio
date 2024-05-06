package dev.tonholo.portfolio.resources

data class Pages(
    val home: Home,
)

sealed interface Page
