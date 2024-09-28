// https://docs.gradle.org/current/userguide/declaring_dependencies.html#sec:type-safe-project-accessors
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://us-central1-maven.pkg.dev/varabyte-repos/public")
        maven {
            name = "marktdownLocalMaven"
            url = uri("marktdown/build/localMaven")
        }
    }
}

rootProject.name = "portfolio"

include(
    ":site",
    ":resources",
    ":core",
)
includeBuild("kotlin-wrapper-highlightjs")
includeBuild("kotlin-wrapper-shiki")
includeBuild("marktdown")
includeBuild("showcase")
