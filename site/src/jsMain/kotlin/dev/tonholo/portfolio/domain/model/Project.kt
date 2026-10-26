package dev.tonholo.portfolio.domain.model

/**
 * A card on the "Selected work" grid and the subject of its own case-study
 * detail page.
 */
data class Project(
    val slug: String,
    val index: String,
    val name: String,
    val kind: ProjectKind,
    val role: String,
    val year: String,
    val description: String,
    val stack: List<String>,
    val stats: List<ProjectStat>,
    val externalCta: String,
    val coverImage: String? = null,
)

enum class ProjectKind { Commercial, OpenSource }

data class ProjectStat(val value: String, val label: String)
