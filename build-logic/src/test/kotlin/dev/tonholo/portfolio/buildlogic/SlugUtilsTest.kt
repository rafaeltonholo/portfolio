package dev.tonholo.portfolio.buildlogic

import java.io.File
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals

class SlugUtilsTest {
    @Test
    fun `given markdown articles with and without a route override - when finding routes - then the override wins`() {
        val directory: File = Files.createTempDirectory("articles").toFile()
        File(directory, "HowA2uiWorks.md").writeText("---\ntitle: How A2UI works\n---\nBody")
        File(directory, "HowA2uiWorksPtBr.md").writeText(
            "---\nrouteOverride: /articles/pt-br/como-o-a2ui-funciona\nslug: how-a2ui-works\n---\nCorpo",
        )
        File(directory, "GitCommandLinePtBr.md").writeText("---\nslug: git-command-line\n---\nCorpo")

        val routes: List<String> = SlugUtils.findMarkdownRoutes(directory = directory, section = "articles")

        assertEquals(
            expected = listOf(
                "/articles/pt-br/git-command-line",
                "/articles/how-a2ui-works",
                "/articles/pt-br/como-o-a2ui-funciona",
            ),
            actual = routes,
        )
    }
}
