package dev.tonholo.portfolio.buildlogic

import com.varabyte.kobweb.gradle.core.extensions.KobwebBlock
import com.varabyte.kobwebx.gradle.markdown.MarkdownBlock
import com.varabyte.kobwebx.gradle.markdown.tasks.ConvertMarkdownTask
import com.varabyte.kobwebx.gradle.markdown.tasks.ProcessMarkdownTask
import dev.tonholo.portfolio.buildlogic.markdown.MarkdownArticlesProcessor
import dev.tonholo.portfolio.buildlogic.markdown.MarkdownHandlers
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import java.io.File

/**
 * Convention plugin for Kobweb markdown processing, article generation,
 * and custom markdown AST handlers.
 */
class PortfolioMarkdownPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        plugins.apply("com.varabyte.kobwebx.markdown")

        val kobwebBlock: KobwebBlock = extensions.getByType<KobwebBlock>()
        val markdownBlock: MarkdownBlock = kobwebBlock.extensions.getByType<MarkdownBlock>()

        markdownBlock.defaultLayout.set("dev.tonholo.portfolio.components.layouts.ArticleMarkdownLayout")
        markdownBlock.process.set { entries ->
            val markdownResourceDir: File = layout.projectDirectory.dir("src/jsMain/resources/markdown").asFile
            val content: String = MarkdownArticlesProcessor.generateArticlesObject(entries, markdownResourceDir)
            generateKotlin(
                filePath = "dev/tonholo/portfolio/generated/articles/GeneratedArticles.kt",
                content = content,
            )
        }

        tasks.withType<ProcessMarkdownTask>().configureEach {
            notCompatibleWithConfigurationCache("Kobweb markdown processing task")
        }

        tasks.withType<ConvertMarkdownTask>().configureEach {
            notCompatibleWithConfigurationCache("Kobweb markdown conversion task")
            markdownHandlers.code.set { codeBlock -> MarkdownHandlers.formatCode(codeBlock) }
            markdownHandlers.a.set { link -> MarkdownHandlers.formatLink(link) }
            markdownHandlers.blockquote.set { blockQuote -> MarkdownHandlers.formatBlockquote(blockQuote) }
            doLast {
                val genDir: File = getGenDir().get().asFile
                MarkdownHandlers.sanitizeGeneratedSources(genDir)
            }
        }
    }
}
