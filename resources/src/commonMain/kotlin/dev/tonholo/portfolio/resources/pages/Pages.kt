package dev.tonholo.portfolio.resources.pages

data class Pages(
    val home: Home,
    val about: AboutPage,
    val resume: ResumePage,
    val article: ArticlePage,
)

interface Page
