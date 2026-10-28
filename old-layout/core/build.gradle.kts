import java.util.*

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.compose)
    alias(libs.plugins.kobweb.library)
    alias(libs.plugins.com.github.gmazzo.buildconfig)
}

kotlin {
    js {
        useEsModules()
        browser()
    }

    targets.all {
        compilations.all {
            compilerOptions.configure {
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.ui)
        }
        jsMain.dependencies {
            implementation(compose.html.core)
            implementation(libs.kobweb.core)
            implementation(libs.kobweb.silk)
            implementation(npm(name = "firebase", version = "10.12.0"))
        }
    }
}


val localConfig = project.rootProject.file("local.properties")
val properties = Properties().apply {
    load(localConfig.reader())
}

buildConfig {
    packageName(group.toString().plus(".config"))
    useKotlinOutput()
    buildConfigField(
        name = "FIREBASE_API_KEY",
        value = properties.getProperty("firebase.apiKey"),
    )
    buildConfigField(
        name = "FIREBASE_AUTH_DOMAIN",
        value = properties.getProperty("firebase.authDomain"),
    )
    buildConfigField(
        name = "FIREBASE_PROJECT_ID",
        value = properties.getProperty("firebase.projectId"),
    )
    buildConfigField(
        name = "FIREBASE_STORAGE_BUCKET",
        value = properties.getProperty("firebase.storageBucket"),
    )
    buildConfigField(
        name = "FIREBASE_MESSAGING_SENDER_ID",
        value = properties.getProperty("firebase.messagingSenderId"),
    )
    buildConfigField(
        name = "FIREBASE_APP_ID",
        value = properties.getProperty("firebase.appId"),
    )
    buildConfigField(
        name = "FIREBASE_MEASUREMENT_ID",
        value = properties.getProperty("firebase.measurementId"),
    )
}
