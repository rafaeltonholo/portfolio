import dev.tonholo.marktdown.config.mavenLocalRepositoryUri

plugins {
    id("com.vanniktech.maven.publish")
}


publishing {
    repositories {
        maven {
            name = "testMaven"
            url = rootProject.mavenLocalRepositoryUri
        }
    }
}
