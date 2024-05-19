package dev.tonholo.portfolio.resources.pages

import dev.tonholo.portfolio.resources.MonthNamesEn
import dev.tonholo.portfolio.resources.MonthNamesPtBr
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

data class ArticlePage(
    val posted: (date: LocalDateTime) -> String,
    val updated: (date: LocalDateTime) -> String,
    val coAuthors: (names: List<String>) -> String,
    val tableOfContents: String,
    val title: String,
    val readMore: String,
) {
    companion object {
        val En = ArticlePage(
            posted = { date -> "Posted on ${date.toFormattedString(MonthNamesEn)}" },
            updated = { date -> "Updated on ${date.toFormattedString(MonthNamesEn)}" },
            coAuthors = { names ->
                "co: ${names.fastJoinToString(lastSeparator = ", and ")}"
            },
            tableOfContents = "Table of Contents",
            title = "Articles",
            readMore = "Read More",
        )
        val PtBr = ArticlePage(
            posted = { date ->
                "Publicado em ${date.toFormattedString(MonthNamesPtBr)}"
            },
            updated = { date ->
                "Atualizado em ${date.toFormattedString(MonthNamesPtBr)}"
            },
            coAuthors = { names ->
                "co: ${names.fastJoinToString(lastSeparator = " e ")}"
            },
            tableOfContents = "Conteúdo",
            title = "Artigos",
            readMore = "Leia Mais",
        )
    }
}

private fun LocalDateTime.toFormattedString(monthNames: MonthNames) =
    format(
        LocalDateTime.Format {
            monthName(monthNames)
            char(',')
            char(' ')
            dayOfMonth()
            char(' ')
            year()
        },
    )

private fun List<String>.fastJoinToString(
    separator: String = ", ",
    lastSeparator: String = separator,
) = buildString {
    var count = 0
    for (i in this@fastJoinToString.indices) {
        val element = this@fastJoinToString[i]
        count++
        when {
            count == size -> append(lastSeparator)
            count > 1 -> append(separator)
        }
        append(element)
    }
}
