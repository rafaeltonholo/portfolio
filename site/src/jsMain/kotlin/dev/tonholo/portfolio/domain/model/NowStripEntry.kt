package dev.tonholo.portfolio.domain.model

/**
 * A column entry in the "Currently" strip on the home page (working on,
 * writing, off the clock).
 */
data class NowStripEntry(val label: String, val title: String, val body: String)
