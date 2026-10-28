plugins {
    `kotlin-dsl`
}

group = "dev.tonholo.portfolio.buildlogic"

fun Provider<PluginDependency>.toPluginCoordinate(): Provider<String> =
    map { "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}" }

dependencies {
    implementation(libs.plugins.kotlin.multiplatform.toPluginCoordinate())
    implementation(libs.plugins.compose.compiler.toPluginCoordinate())
    implementation(libs.plugins.kobweb.application.toPluginCoordinate())
    implementation(libs.plugins.kobwebx.markdown.toPluginCoordinate())
    implementation(libs.plugins.detekt.toPluginCoordinate())
    implementation("dev.detekt:detekt-gradle-plugin:2.0.0-alpha.6")
    implementation("com.varabyte.kobwebx:kobwebx-frontmatter:0.25.1")
    implementation("com.varabyte.kobwebx.gradle:markdown:0.25.1")
    implementation("org.commonmark:commonmark:0.31.0")
}

gradlePlugin {
    plugins {
        register("portfolioMarkdown") {
            id = "portfolio.markdown"
            implementationClass = "dev.tonholo.portfolio.buildlogic.PortfolioMarkdownPlugin"
        }
    }
}
