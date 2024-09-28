package dev.tonholo.portfolio.resources

data class Project(
    val title: String,
    val description: String,
    val src: String?,
    val playStoreSrc: String? = null,
)

enum class ProjectType {
    OpenSource,
    Commercial
}
