package dev.tonholo.portfolio.resources

data class NavBar(
    val home: String,
    val about: String,
    val articles: String,
    val resume: String,
) {
    companion object {
        val En = NavBar(
            home = "Home",
            about = "About",
            articles = "Articles",
            resume = "Resume",
        )
        val PtBr = NavBar(
            home = "Início",
            about = "Sobre",
            articles = "Artigos",
            resume = "Currículo",
        )
    }
}
