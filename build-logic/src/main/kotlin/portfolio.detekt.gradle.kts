import dev.detekt.gradle.extensions.DetektExtension

plugins {
    id("dev.detekt")
}

extensions.configure<DetektExtension> {
    config.setFrom(rootProject.file("config/detekt.yml"))
    buildUponDefaultConfig = true
    allRules = false
}

tasks.matching { it.name.startsWith("detekt") }.configureEach {
    if (this is org.gradle.api.tasks.SourceTask) {
        exclude { element ->
            element.file.absolutePath.contains("/build/generated/")
        }
    }
}
