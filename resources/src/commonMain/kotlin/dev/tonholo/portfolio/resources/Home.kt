package dev.tonholo.portfolio.resources

data class Home(
    val welcome: WelcomeSection,
    val about: AboutSection,
    val recentProjects: RecentProjectsSection,
) : Page {
    companion object {
        val En = Home(
            welcome = WelcomeSection.En,
            about = AboutSection.En,
            recentProjects = RecentProjectsSection.En,
        )
        val PtBr = Home(
            welcome = WelcomeSection.PtBr,
            about = AboutSection.PtBr,
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
            description = buildString {
                append("I’ve been coding and building products since 2010, and now I’m an android developer trying to.....")
            }
        )
        val PtBr = WelcomeSection(
            title = "Opa, eu sou Rafael!",
            description = buildString {
                append("Sou um programador Android desde 2010, e agora estou buscando.....")
            }
        )
    }
}

data class AboutSection(
    val title: String,
    val description: String,
) : Section {
    companion object {
        val En = AboutSection(
            title = "What I do",
            description = buildString {
                append("I code, write and bake, but mostly code using ....")
            },
        )
        val PtBr = AboutSection(
            title = "O que faço",
            description = buildString {
                append("Eu programo, escrevo e cozinho, mas principalmente programo usando ....")
            }
        )
    }
}

data class RecentProjectsSection(
    val title: String,
    val projects: List<Project>,
) : Section {
    companion object {
        val En = RecentProjectsSection(
            title = "My recent projects",
            projects = listOf(
                Project(
                    title = "SVG to Compose",
                    description = buildString {
                        append("A command-line tool to convert SVG ")
                        append("or an Android Vector Drawable (AVG) to Android Jetpack Compose Icons.")
                    },
                    src = "https://github.com/rafaeltonholo/svg-to-compose",
                ),
                Project(
                    title = "SVG to Compose",
                    description = buildString {
                        append("A command-line tool to convert SVG ")
                        append("or an Android Vector Drawable (AVG) to Android Jetpack Compose Icons.")
                    },
                    src = "https://github.com/rafaeltonholo/svg-to-compose",
                ),
                Project(
                    title = "SVG to Compose",
                    description = buildString {
                        append("A command-line tool to convert SVG ")
                        append("or an Android Vector Drawable (AVG) to Android Jetpack Compose Icons.")
                    },
                    src = "https://github.com/rafaeltonholo/svg-to-compose",
                ),
                Project(
                    title = "SVG to Compose",
                    description = buildString {
                        append("A command-line tool to convert SVG ")
                        append("or an Android Vector Drawable (AVG) to Android Jetpack Compose Icons.")
                    },
                    src = "https://github.com/rafaeltonholo/svg-to-compose",
                ),
                Project(
                    title = "SVG to Compose",
                    description = buildString {
                        append("A command-line tool to convert SVG ")
                        append("or an Android Vector Drawable (AVG) to Android Jetpack Compose Icons.")
                    },
                    src = "https://github.com/rafaeltonholo/svg-to-compose",
                ),
                Project(
                    title = "SVG to Compose",
                    description = buildString {
                        append("A command-line tool to convert SVG ")
                        append("or an Android Vector Drawable (AVG) to Android Jetpack Compose Icons.")
                    },
                    src = "https://github.com/rafaeltonholo/svg-to-compose",
                ),
                Project(
                    title = "SVG to Compose",
                    description = buildString {
                        append("A command-line tool to convert SVG ")
                        append("or an Android Vector Drawable (AVG) to Android Jetpack Compose Icons.")
                    },
                    src = "https://github.com/rafaeltonholo/svg-to-compose",
                ),
            )
        )

        val PtBr = RecentProjectsSection(
            title = "Meus projetos recentes",
            projects = listOf(
                Project(
                    title = "SVG to Compose",
                    description = buildString {
                        append("Uma ferramenta command-line para converter SVG ")
                        append("ou um Android Vector Drawable (AVG) para Android Jetpack Compose Icons.")
                    },
                    src = "https://github.com/rafaeltonholo/svg-to-compose",
                )
            )
        )
    }
}
