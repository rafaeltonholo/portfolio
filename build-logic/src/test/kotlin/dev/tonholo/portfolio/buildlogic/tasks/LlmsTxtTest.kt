package dev.tonholo.portfolio.buildlogic.tasks

import kotlin.test.Test
import kotlin.test.assertEquals

class LlmsTxtTest {
    @Test
    fun `given articles and projects - when rendering llms txt - then it follows the llms txt layout`() {
        val text = GenerateLlmsTxtTask.renderLlmsTxt(
            articles = listOf(SiteLink(title = "Git time travel", url = "https://rafael.tonholo.dev/articles/git", description = "Recover a repo.")),
            projects = listOf(SiteLink(title = "Thunderbird", url = "https://rafael.tonholo.dev/projects/thunderbird", description = "")),
        )

        assertEquals(
            expected = """
                |# Rafael Tonholo
                |
                |> ${GenerateLlmsTxtTask.SUMMARY}
                |
                |## Articles
                |
                |- [Git time travel](https://rafael.tonholo.dev/articles/git): Recover a repo.
                |
                |## Projects
                |
                |- [Thunderbird](https://rafael.tonholo.dev/projects/thunderbird)
                |
                |## Pages
                |
                |- [About](https://rafael.tonholo.dev/about)
                |- [Resume](https://rafael.tonholo.dev/resume)
                |
            """.trimMargin(),
            actual = text,
        )
    }
}
