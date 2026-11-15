plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.android.kotlin.multiplatform.library)
  alias(libs.plugins.kotlin.serialization)
}

kotlin {
  jvmToolchain(17)
  jvm()
  android {
    namespace = "dev.tonholo.a2ui.express"
    compileSdk = 37
    minSdk = 24
  }

  sourceSets {
    commonMain.dependencies {
      api(libs.kotlinx.serialization.json)
    }
    commonTest.dependencies {
      implementation(libs.kotlin.test)
    }
  }
}
