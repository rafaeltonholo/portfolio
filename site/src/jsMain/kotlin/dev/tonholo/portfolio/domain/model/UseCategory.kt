package dev.tonholo.portfolio.domain.model

/**
 * A category in the /uses grid on the home page (editor, hardware, desk, cli).
 */
data class UseCategory(val name: String, val items: List<String>)
