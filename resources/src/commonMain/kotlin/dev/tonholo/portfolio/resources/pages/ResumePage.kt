package dev.tonholo.portfolio.resources.pages

import dev.tonholo.portfolio.locale.Locales
import dev.tonholo.portfolio.resources.Education
import dev.tonholo.portfolio.resources.workExperience.Experience

data class ResumePage(
    val title: String,
    val viewResumePdf: String,
    val experience: ExperienceSection,
    val educationSection: EducationSection,
    val skillSection: SkillSection,
) : Page {
    companion object {
        val En = ResumePage(
            title = "My background",
            viewResumePdf = "View resume PDF",
            experience = ExperienceSection.En,
            educationSection = EducationSection.En,
            skillSection = SkillSection.En,
        )
        val PtBr = ResumePage(
            title = "Meu currículo",
            viewResumePdf = "Ver currículo PDF",
            experience = ExperienceSection.PtBr,
            educationSection = EducationSection.PtBr,
            skillSection = SkillSection.PtBr,
        )
    }
}

data class ExperienceSection(
    val title: String,
    val experiences: List<Experience>,
) : Section {
    companion object {
        val En = ExperienceSection(
            title = "Experience",
            experiences = requireNotNull(Experience.Experiences[Locales.EN]),
        )
        val PtBr = ExperienceSection(
            title = "Experiência",
            experiences = requireNotNull(Experience.Experiences[Locales.PT_BR]),
        )
    }
}

data class EducationSection(
    val title: String,
    val educations: List<Education>,
) : Section {
    companion object {
        val En = EducationSection(
            title = "Education",
            educations = requireNotNull(Education.Educations[Locales.EN]),
        )
        val PtBr = EducationSection(
            title = "Educação",
            educations = requireNotNull(Education.Educations[Locales.PT_BR]),
        )
    }
}

data class SkillSection(
    val title: String,
    val skills: List<Skill>,
) : Section {
    companion object {
        val En = SkillSection(
            title = "Skills",
            skills = listOf(
                Skill(name = "Android"),
                Skill(name = "Kotlin"),
                Skill(name = "KMP"),
                Skill(name = "Hilt"),
                Skill(name = "Jetpack Compose"),
                Skill(name = "Play Feature Delivery"),
                Skill(name = "git"),
                Skill(name = "Java"),
                Skill(name = "Dagger2"),
                Skill(name = "Hilt"),
                Skill(name = "Java"),
            ),
        )
        val PtBr = SkillSection(
            title = "Habilidades",
            skills = En.skills,
        )
    }

    data class Skill(
        val name: String,
    )
}
