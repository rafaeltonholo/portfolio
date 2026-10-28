plugins {
    `kotlin-dsl`
}

group = "dev.tonholo.portfolio.buildlogic"

dependencies {
    implementation(libs.plugins.kotlin.multiplatform)
    implementation(libs.plugins.compose.compiler)
    implementation(libs.plugins.kobweb.application)
    implementation(libs.plugins.kobwebx.markdown)
    implementation(libs.plugins.detekt)
    implementation("org.commonmark:commonmark:0.24.0")
}
