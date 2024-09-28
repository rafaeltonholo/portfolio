package dev.tonholo.portfolio.resources.pages

data class Pages(
    val home: Home,
    val about: AboutPage,
    val resume: ResumePage,
    val article: ArticlePage,
    val project: ProjectPage,
) {
    companion object {
        val En = Pages(
            home = Home.En,
            about = AboutPage.En,
            resume = ResumePage.En,
            article = ArticlePage.En,
            project = ProjectPage.En,
        )
        val PtBr = Pages(
            home = Home.PtBr,
            about = AboutPage.PtBr,
            resume = ResumePage.PtBr,
            article = ArticlePage.PtBr,
            project = ProjectPage.PtBR,
        )
    }
}

interface Page {
    val title: String
}
