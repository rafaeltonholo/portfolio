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
    implementation("org.commonmark:commonmark:0.24.0")
}
