plugins {
    `kotlin-dsl`
}

kotlin {
    explicitApi()
    compilerOptions.freeCompilerArgs.add("-Xcontext-receivers")
}

dependencies {
    implementation(libs.org.jetbrains.kotlin.gradle.plugin)
    implementation(libs.com.vanniktech.gradle.maven.publish.plugin)
}

