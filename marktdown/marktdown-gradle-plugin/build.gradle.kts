import dev.tonholo.marktdown.config.MarktdownConfig

plugins {
    `kotlin-dsl`
    org.jetbrains.kotlin.jvm
//    `java-gradle-plugin`
    dev.tonholo.publishing
}

gradlePlugin {
    plugins {
        create("marktdown") {
            id = MarktdownConfig.GROUP
            implementationClass = "${MarktdownConfig.GROUP}.gradle.plugin.MarktdownPlugin"
            displayName = MarktdownConfig.DISPLAY_NAME
            description = MarktdownConfig.DESCRIPTION
        }
    }
}

dependencies {
    // Get access to Kotlin multiplatform source sets
    implementation(kotlin("gradle-plugin"))
    compileOnly(gradleApi())
    implementation(libs.org.jetbrains.kotlin.gradle.plugin)
    implementation(projects.marktdownProcessor)
}
