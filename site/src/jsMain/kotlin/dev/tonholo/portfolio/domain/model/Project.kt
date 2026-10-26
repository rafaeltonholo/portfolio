package dev.tonholo.portfolio.domain.model

/**
 * A card on the "Selected work" grid and the subject of its own case-study
 * detail page.
 *
 * @property slug Unique identifier for routes.
 * @property index Two-digit index string (e.g. "01").
 * @property name Display title of the project.
 * @property kinds List of categories this project belongs to (e.g. Commercial, OpenSource).
 * @property role Role or title held on the project.
 * @property year Active time range.
 * @property description Project summary copy.
 * @property stack Technology badges.
 * @property stats Key impact metrics.
 * @property externalCta CTA button label.
 * @property coverImage Optional cover/hero image asset path.
 */
data class Project(
    val slug: String,
    val index: String,
    val name: String,
    val kinds: List<ProjectKind>,
    val role: String,
    val year: String,
    val description: String,
    val stack: List<String>,
    val stats: List<ProjectStat>,
    val externalCta: String,
    val coverImage: String? = null,
) {
    /**
     * Backward-compatible primary kind accessor.
     */
    val kind: ProjectKind
        get() = kinds.first()

    constructor(
        slug: String,
        index: String,
        name: String,
        kind: ProjectKind,
        role: String,
        year: String,
        description: String,
        stack: List<String>,
        stats: List<ProjectStat>,
        externalCta: String,
        coverImage: String? = null,
    ) : this(
        slug = slug,
        index = index,
        name = name,
        kinds = listOf(kind),
        role = role,
        year = year,
        description = description,
        stack = stack,
        stats = stats,
        externalCta = externalCta,
        coverImage = coverImage,
    )
}

enum class ProjectKind { Commercial, OpenSource }

data class ProjectStat(val value: String, val label: String)
