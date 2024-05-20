package dev.tonholo.portfolio.feature

enum class FeatureFlag(
    val enabled: Boolean,
) {
    MoreAboutMe(enabled = false),
    Comments(enabled = false),
}
