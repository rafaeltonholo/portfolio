import dev.tonholo.marktdown.config.MarktdownConfig
import dev.tonholo.marktdown.config.mavenLocalRepositoryUri
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

allprojects {
    repositories {
        google()
        mavenCentral()
    }
    group = MarktdownConfig.GROUP
    version = MarktdownConfig.VERSION

    tasks.withType<JavaCompile> {
        sourceCompatibility = MarktdownConfig.javaTarget.toString()
        targetCompatibility = MarktdownConfig.javaTarget.toString()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = MarktdownConfig.javaTarget.toString()
    }

    tasks.withType<ProcessResources> {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}

subprojects {
    if (name == "sample") {
        repositories {
            google()
            mavenCentral()
            maven {
                name = "testMaven"
                url = rootProject.mavenLocalRepositoryUri
            }
        }
    }
}

tasks.create("publishAllToMavenLocal") {
    group = "publishing"
    description = "Publish all subprojects to maven local"
    doLast {
        println("After ${project.name}:publishToLocalMaven")
    }
    subprojects {
        if (name != "sample") {
            tasks.withType<PublishToMavenRepository> {
                doLast {
                    println("After ${project.name}:publishAllPublicationsToTestMavenRepository")
                }
                this@create.dependsOn(this)
            }
        }
    }
}

tasks.withType<Delete> {
    val rootTask = this
    subprojects {
        tasks.withType<Delete> {
            rootTask.dependsOn(this)
        }
    }
}

tasks.wrapper {
    gradleVersion = libs.versions.gradle.get()
    distributionType = Wrapper.DistributionType.ALL
}
