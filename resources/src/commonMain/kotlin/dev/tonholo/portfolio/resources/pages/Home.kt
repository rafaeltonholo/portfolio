package dev.tonholo.portfolio.resources.pages

import androidx.compose.runtime.Immutable
import dev.tonholo.portfolio.core.ui.text.AnnotatedString
import dev.tonholo.portfolio.core.ui.text.annotatedString
import dev.tonholo.portfolio.core.ui.text.bold
import dev.tonholo.portfolio.resources.Project
import dev.tonholo.portfolio.resources.ProjectType
import dev.tonholo.portfolio.resources.Projects

data class Home(
    override val title: String,
    val welcome: WelcomeSection,
    val about: HomeAboutSection,
    val recentProjects: RecentProjectsSection,
) : Page {
    companion object {
        val En = Home(
            title = "Home | Rafael Tonholo",
            welcome = WelcomeSection.En,
            about = HomeAboutSection.En,
            recentProjects = RecentProjectsSection.En,
        )
        val PtBr = Home(
            title = "Início | Rafael Tonholo",
            welcome = WelcomeSection.PtBr,
            about = HomeAboutSection.PtBr,
            recentProjects = RecentProjectsSection.PtBr,
        )
    }
}

data class WelcomeSection(
    val title: String,
    val description: String,
) : Section {
    companion object {
        val En = WelcomeSection(
            title = "Hi there, I’m Rafael!",
            description = """
                   I'm a mobile developer who's been building things for Android since 2010.
                   Huge fan of learning new stuff (especially in the mobile world!), and lately 
                   I'm all about Jetpack Compose and Kotlin 💜
                """.trimIndent(),
        )
        val PtBr = WelcomeSection(
            title = "Opa, Rafael aqui!",
            description = """
                Sou um dev mobile mineiro que faz uns trem aí com Android desde 2010.
                Fanzaço de aprender coisas novas (especialmente no mundo mobile!), e recentemente 
                estou focado em Jetpack Compose e Kotlin 💜
            """.trimIndent(),
        )
    }
}

@Immutable
data class HomeAboutSection(
    val title: String,
    val description: String,
    val annotatedString: AnnotatedString? = null,
    val androidLogoContentDescription: String,
    val kotlinLogoContentDescription: String,
    val composeLogoContentDescription: String,
) : Section {
    companion object {
        val En = HomeAboutSection(
            title = "What I do",
            description = """
                    I create apps for Android and sometimes iOS using KMP, I love
                    to share what I know and, when I'm not programming, I like to 
                    venture into the kitchen and try to make something good to eat.
                    And lately I've working mostly with:
                """.trimIndent(),
            annotatedString = annotatedString {
                append("I create apps for ")
                bold("Android")
                append(" and sometimes iOS using ")
                bold("KMP")
                append(" and I love to share what I know and, when I'm not programming, I like to")
                append("venture into the kitchen")
                append(" and try to make something good to eat.")
                append(" And lately I've working mostly with:")
            },
            androidLogoContentDescription = "Android applications",
            kotlinLogoContentDescription = "Using Kotlin as main language",
            composeLogoContentDescription = "Using Jetpack Compose UI Framework",
        )
        val PtBr = HomeAboutSection(
            title = "O que faço",
            description = """
                    Eu crio aplicativos para Android e, às vezes, para iOS usando o KMP, 
                    adoro compartilhar o que sei e, quando não estou programando, gosto de me 
                    aventurar na cozinha tentando fazer um trem bão pra comer. 
                    Sim, eu sei fazer pão de queijo e é o melhor do mundo. 
                    E por último, últimamente tenho mexido basicamente com:
                """.trimIndent(),
            androidLogoContentDescription = "Applicações para Android",
            kotlinLogoContentDescription = "Usando Kotlin como linguagem principal",
            composeLogoContentDescription = "Usando Jetpack Compose UI Framework",
        )
    }
}

@Immutable
data class RecentProjectsSection(
    val title: String,
    val projectTypeLabels: Map<ProjectType, String>,
    val projects: Map<ProjectType, List<Project>>,
) : Section {
    companion object {
        val En = RecentProjectsSection(
            title = "Projects I worked on or contributed to",
            projectTypeLabels = mapOf(
                ProjectType.OpenSource to "Open Source projects",
                ProjectType.Commercial to "Commercial projects",
            ),
            projects = Projects.En,
        )

        val PtBr = RecentProjectsSection(
            title = "Projetos que trabalhei ou contribuí",
            projectTypeLabels = mapOf(
                ProjectType.OpenSource to "Projetos open source",
                ProjectType.Commercial to "Projetos comerciais",
            ),
            projects = Projects.PtBr,
        )
    }
}
