package dev.tonholo.portfolio.domain.model

/**
 * A bucket of skills grouped by category; drives the two chip columns in the
 * resume skills section.
 */
data class SkillBucket(val category: SkillCategory, val skills: List<String>)

enum class SkillCategory { PrimaryStack, AlsoComfortable }
