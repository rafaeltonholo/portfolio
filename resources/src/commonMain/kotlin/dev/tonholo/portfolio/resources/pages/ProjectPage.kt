package dev.tonholo.portfolio.resources.pages

data class ProjectPage(
    val title: String,
    val projectBackground: String,
    val info: String,
    val role: String,
    val client: String,
    val timeline: String,
    val stack: String,
) {
    companion object {
        val En = ProjectPage(
            title = "Project",
            projectBackground = "Project background",
            info = "Info",
            role = "Role",
            client = "Client",
            timeline = "Timeline",
            stack = "Stack",
        )
        val PtBR = ProjectPage(
            title = "Projeto",
            projectBackground = "Sobre o projeto",
            info = "Info",
            role = "Posição",
            client = "Cliente",
            timeline = "Duração",
            stack = "Stack",
        )
    }
}
