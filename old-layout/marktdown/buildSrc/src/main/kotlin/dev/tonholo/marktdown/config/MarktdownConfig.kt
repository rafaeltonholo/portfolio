package dev.tonholo.marktdown.config

import java.net.URI
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

public object MarktdownConfig {
    public const val GROUP: String = "dev.tonholo.marktdown"
    public const val VERSION: String = "1.0.0"
    public const val DISPLAY_NAME: String = "MarKTdown"
    public const val DESCRIPTION: String = "TODO"
    public val javaTarget: JvmTarget = JvmTarget.JVM_1_8
}

public val Project.mavenLocalRepositoryUri: URI
    get() =
        uri(rootProject.layout.buildDirectory.dir("localMaven"))
