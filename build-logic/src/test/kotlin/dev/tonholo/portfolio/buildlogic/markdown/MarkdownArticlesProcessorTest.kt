package dev.tonholo.portfolio.buildlogic.markdown

import kotlin.test.Test
import dev.tonholo.portfolio.buildlogic.markdown.MarkdownArticlesProcessor.AuthorRow
import dev.tonholo.portfolio.buildlogic.markdown.MarkdownArticlesProcessor.ReviewerRow
import kotlin.test.assertEquals

class MarkdownArticlesProcessorTest {
    @Test
    fun `given an english display date - when converting to iso - then the calendar date is returned`() {
        assertEquals(expected = "2026-10-25", actual = MarkdownArticlesProcessor.toIsoDate("oct 25, 2026"))
    }

    @Test
    fun `given an iso timestamp - when converting to iso - then only the date is kept`() {
        assertEquals(expected = "2024-05-19", actual = MarkdownArticlesProcessor.toIsoDate("2024-05-19T17:16:34-03:00"))
    }

    @Test
    fun `given an unparseable date - when converting to iso - then it is blank instead of a placeholder`() {
        assertEquals(expected = "", actual = MarkdownArticlesProcessor.toIsoDate("someday"))
    }

    @Test
    fun `given no reviewers key - when extracting reviewers - then the list is empty`() {
        val frontMatter: List<String> = frontMatterOf(
            """
            title: An article
            date: nov 17, 2026
            """,
        )

        val reviewers: List<ReviewerRow> = MarkdownArticlesProcessor.extractReviewers(frontMatter)

        assertEquals(expected = emptyList(), actual = reviewers)
    }

    @Test
    fun `given reviewers with names and links - when extracting reviewers - then each reviewer keeps its link`() {
        val frontMatter: List<String> = frontMatterOf(
            """
            reviewers:
                -   name: Jane Doe
                    link: https://github.com/jane
                - name: 'John Roe'
                  link: "https://www.linkedin.com/in/john"
            """,
        )

        val reviewers: List<ReviewerRow> = MarkdownArticlesProcessor.extractReviewers(frontMatter)

        assertEquals(
            expected = listOf(
                ReviewerRow(name = "Jane Doe", link = "https://github.com/jane"),
                ReviewerRow(name = "John Roe", link = "https://www.linkedin.com/in/john"),
            ),
            actual = reviewers,
        )
    }

    @Test
    fun `given a reviewer without a link - when extracting reviewers - then the link is blank`() {
        val frontMatter: List<String> = frontMatterOf(
            """
            reviewers:
                - name: Jane Doe
            """,
        )

        val reviewers: List<ReviewerRow> = MarkdownArticlesProcessor.extractReviewers(frontMatter)

        assertEquals(expected = listOf(ReviewerRow(name = "Jane Doe", link = "")), actual = reviewers)
    }

    @Test
    fun `given a reviewer without a name - when extracting reviewers - then the reviewer is skipped`() {
        val frontMatter: List<String> = frontMatterOf(
            """
            reviewers:
                - link: https://github.com/nobody
                - name: Jane Doe
            """,
        )

        val reviewers: List<ReviewerRow> = MarkdownArticlesProcessor.extractReviewers(frontMatter)

        assertEquals(expected = listOf(ReviewerRow(name = "Jane Doe", link = "")), actual = reviewers)
    }

    @Test
    fun `given authors followed by reviewers - when extracting both - then neither list takes entries from the other`() {
        val frontMatter: List<String> = frontMatterOf(
            """
            authors:
                -   name: rafael
                    avatar: https://rafael.tonholo.dev/images/writing.jpg
            reviewers:
                -   name: Jane Doe
                    link: https://github.com/jane
            tags:
                - android
            """,
        )

        val authors: List<AuthorRow> = MarkdownArticlesProcessor.extractAuthors(frontMatter)
        val reviewers: List<ReviewerRow> = MarkdownArticlesProcessor.extractReviewers(frontMatter)

        assertEquals(
            expected = listOf(AuthorRow(name = "rafael", avatar = "https://rafael.tonholo.dev/images/writing.jpg")),
            actual = authors,
        )
        assertEquals(
            expected = listOf(ReviewerRow(name = "Jane Doe", link = "https://github.com/jane")),
            actual = reviewers,
        )
    }

    private fun frontMatterOf(yaml: String): List<String> = yaml.trimIndent().lines()

    @Test
    fun `given a route override - when extracting the path slug - then it is the last route segment`() {
        val frontMatter: List<String> = frontMatterOf(
            """
            routeOverride: /articles/pt-br/como-o-a2ui-funciona
            slug: how-a2ui-works
            """,
        )

        val pathSlug: String = MarkdownArticlesProcessor.extractPathSlug(frontMatter, fallback = "how-a2ui-works")

        assertEquals(expected = "como-o-a2ui-funciona", actual = pathSlug)
    }

    @Test
    fun `given no route override - when extracting the path slug - then the fallback slug is used`() {
        val frontMatter: List<String> = frontMatterOf(
            """
            slug: git-command-line
            """,
        )

        val pathSlug: String = MarkdownArticlesProcessor.extractPathSlug(frontMatter, fallback = "git-command-line")

        assertEquals(expected = "git-command-line", actual = pathSlug)
    }
}
