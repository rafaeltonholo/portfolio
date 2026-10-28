package dev.tonholo.portfolio.domain.model

/**
 * A single row on the resume timeline.
 */
data class Experience(
    val date: String,
    val duration: String,
    val role: String,
    val company: String,
    val body: String,
    val bullets: List<String>,
)
