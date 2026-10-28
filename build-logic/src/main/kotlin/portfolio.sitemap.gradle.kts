import dev.tonholo.portfolio.buildlogic.SlugUtils
import dev.tonholo.portfolio.buildlogic.splitLocalizedSlug
import dev.tonholo.portfolio.buildlogic.tasks.GenerateSitemapTask
import java.io.File
import java.util.Properties

val resolvedAppPropertiesFile: File = rootProject.rootDir.resolve("app.properties")
val appProperties = Properties().apply {
    if (resolvedAppPropertiesFile.exists()) {
        resolvedAppPropertiesFile.inputStream().use { load(it) }
    }
}

val projectSlugs: List<String> = listOf("svg-compose")
val localizedPathSegments: List<String> = SlugUtils.LOCALIZED_PATH_SEGMENTS

val articlesMarkdownDir: File = layout.projectDirectory.dir("src/jsMain/resources/markdown/articles").asFile
val articleSlugs: List<String> = SlugUtils.findMarkdownSlugs(articlesMarkdownDir)

val projectsMarkdownDir: File = layout.projectDirectory.dir("src/jsMain/resources/markdown/projects").asFile
val projectMarkdownSlugs: List<String> = SlugUtils.findMarkdownSlugs(projectsMarkdownDir)

val dynamicProjectUrls: List<String> = projectSlugs.flatMap { slug: String ->
    listOf("/projects/$slug") + localizedPathSegments.map { lang: String -> "/projects/$lang/$slug" }
}
val markdownProjectUrls: List<String> = projectMarkdownSlugs.map { kebabSlug: String ->
    val (canonical: String, lang: String?) = kebabSlug.splitLocalizedSlug()
    if (lang == null) "/projects/$canonical" else "/projects/$lang/$canonical"
}
val articleUrls: List<String> = articleSlugs.map { kebabSlug: String ->
    val (canonical: String, lang: String?) = kebabSlug.splitLocalizedSlug()
    if (lang == null) "/articles/$canonical" else "/articles/$lang/$canonical"
}
val allExtraSlugs: List<String> = dynamicProjectUrls + markdownProjectUrls + articleUrls

val generateSitemap = tasks.register<GenerateSitemapTask>("generateSitemap") {
    description = "Generates sitemap.xml for all static and dynamic routes."
    this.pagesDir.set(layout.projectDirectory.dir("src/jsMain/kotlin/dev/tonholo/portfolio/pages"))
    this.appPropertiesFile.set(resolvedAppPropertiesFile)
    this.lastModified.set(appProperties.getProperty("LAST_MODIFIED").orEmpty())
    this.extraSlugs.set(allExtraSlugs)
    this.outputFile.set(layout.projectDirectory.file("src/jsMain/resources/public/sitemap.xml"))
}

tasks.configureEach {
    if (name == "jsProcessResources") {
        dependsOn(generateSitemap)
    }
}
