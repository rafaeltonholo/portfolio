package dev.tonholo.portfolio.resources.pages

data class Pages(
    val home: Home,
    val about: AboutPage,
    val resume: ResumePage,
    val article: ArticlePage,
) {
    companion object {
        val En = Pages(
            home = Home.En,
            about = AboutPage.En,
            resume = ResumePage.En,
            article = ArticlePage.En,
        )
        val PtBr = Pages(
            home = Home.PtBr,
            about = AboutPage.PtBr,
            resume = ResumePage.PtBr,
            article = ArticlePage.PtBr,
        )
    }
}

interface Page
