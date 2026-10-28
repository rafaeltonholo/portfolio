package dev.tonholo.portfolio.resources

data class Description(
    val summary: String,
    val bulletPoints: List<String> = emptyList(),
) {
    companion object {
        operator fun invoke(
            summary: String,
            vararg bulletPoints: String,
        ): Description = Description(
            summary = summary,
            bulletPoints = bulletPoints.toList(),
        )
    }
}
