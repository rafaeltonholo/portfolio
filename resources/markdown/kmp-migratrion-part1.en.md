---
title: My document
authors:
  - name: Rafael
    links:
        - https://abc.com
        - https://github.com/rafaeltonholo
        - https://linkedin.com/in/rafaeltonholo
    avatar: http://localhost:8080/images/writing.jpg
  - name: Amanda
    links:
        - https://google.com
        - https://github.com/amanda
        - https://linkedin.com/in/amanda
  - name: Cacau
    avatar: https://google.com
  - name: Pingo
publishedDateTime: 2024-04-20T20:41:45
tags:
    - KMP
    - Android
    - iOS
---

[*That* **is** <sub>a</sub> `complex`, ~custom~, ^link^](https://www.google.com.br)

~~*That* **is** <sub>a</sub> `complex`, ==~custom~==, ^paragraph^~~

<https://www.google.com>

<mailto:rafael@tonholo.dev>

---

Header
---

Header2
===

* test
* bullet
* list^[1]^

  ![Tux, the Linux mascot](https://mdg.imgix.net/assets/images/tux.png?auto=format&fit=clip&q=40&w=100)


1. Testing
    - Nested
    - List
2. How it comes
    - When very depth
        1. List become
            - Deep
        2. Deep?
    - Not deep.
3. Shalow


Paragraph with hard line \
break

```
Is this a code block?
```

    Or is this a code block?
    Intersting that this is the code block
    and not the above.
    Does
        It
            Work
        With
    indentation?

~~~kotlin
interface ThatIsADifferentCodeFence {
    val what: String get() = "The hell?"
}
~~~

# Task Log

## Integration

### Android

Android was a quite nice surprise! I was expecting it to be easier than iOS to plug a native app into KMP, but not that easy!

With the help of Gradle composite, and thank you @frl-sh for introducing that concept to me, I was able to plug KMP in the native app within an hour, differently from iOS which took me almost a day; I could count the lack of knowledge on iOS ecosystem for that, but even with knowledge I don't think would be faster than android setup.

Here are the steps to integrate KMP within an Android native app:
1. In the root `settings.gradle.kts`, we include the `shubi-android` project by using gradle composite:
```kotlin
includeBuild("shubi-android")
```

2. Inside the `build.gradle.kts` from `shubi-shared` project, we need to make a small change that will enable us to use the shared library more easily inside the Android app:
```kotlin
// Same configuration of the KMP wizard project
plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.multiplatform)
}

//Here is the change we need to make in order to use the 
// shared library within the native app.
group = "com.redspace.shubi"
version = "0.0.1-alpha"

kotlin {
    // same setup of the KMP wizard project
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_11.toString()
            }
        }
    }

    // other setup kotlin setup
}

// Same configuration of the KMP wizard project
android {
    namespace = "com.redspace.shubi.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
```
3. Now, inside the `settings.gradle.kts` of the `shubi-android` app, we also include the root KMP project by using gradle composite, but changing a little:
```kotlin
includeBuild("../../kmp-app-migration") {
    dependencySubstitution {
        // This will instruct gradle to, every time we refer to a module
        // with this same notation, we substitute it by the project we have 
        // in this repository.
        substitute(module("com.redspace.shubi:shubi-shared"))
            .using(project(":shubi-shared"))
    }
}
```
4. And last, but not least, we add the dependency inside the module we are going to use the share code:
```kotlin
dependencies {
    implementation("com.redspace.shubi:shubi-shared:0.0.1-alpha")
}
```

This is a very nice approach because we can still work into the native app without actually loading the KMP mono repo if needed. We would just need to change the `settings.gradle.kts` of the `shubi-android` app to first look if the `shubi-android` is inside the mono repo and only include it via gradle composite if is the case. Otherwise, the app would need to look to the published dependency of `com.redspace.shubi:shubi-shared`, as it would be for an external library.

The `settings.gradle.kts` of the `shubi-android` app would look like this with the changes:
```kotlin
val rootMonoRepo = "../../kmp-app-migration"
val monoRepoFile = file(rootMonoRepo)
if (monoRepoFile.exists() && monoRepoFile.isDirectory()) {
    includeBuild("../../kmp-app-migration") {
        dependencySubstitution {
            // This will instruct Gradle to, every time we refer to a module
            // with this same notation, we substitute it with the project we have 
            // in this repository.
            substitute(module("com.redspace.shubi:shubi-shared"))
                .using(project(":shubi-shared"))
        }
    }
}
```

With this change, we decouple the native Android app.

#### Android usage
The usage of the shared code on Android is the same as if we were using a Kotlin library.

1. Import the library in the `build.gradle.kts` file dependency as already explained about
2. Import the class/function/property in the file you want to use the shared code
3. Use it!

Taking the "KMP Hello World" Greeting class as an example, here is a quick usage of it on `shubi-android` app directly on the MainActivity:

```kotlin
package com.redspace.shubi

import android.os.Bundle
// several other imports
import com.redspace.shubi.shared.Greeting
// other imports

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    // some properties
    override fun onCreate(savedInstanceState: Bundle?) {
        // some configurations
        super.onCreate(savedInstanceState)
        setContent { Content() }
        // other code
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Composable
    private fun Content() {
        // some compose stuff
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Magenta),
        ) {
            Text(
                // Here is the usage of the shared class!
                // Plain, simple, kotlin code.
                text = Greeting().greet(), // [!code focus]
                style = MaterialTheme.typography.headlineLarge,
            )
        }
    }
}
```

No strange performance change nor build time increase has been detected yet, but it is too soon to understand if any impact will happen, however, it is good to know that nothing changed.

### iOS

Integrating with the iOS code was easier than I was thinking. The base configuration of an iOS project on KMP is almost what we needed.

A few changes were needed since we have a modularized app on iOS (nice! didn't know that was possible too).

To integrate the iOS with KMP, the procedure is the following:

#### Shared module setup

In the `shubi-shared` module, we add the following target:

```kotlin
    val frameworkName = "ShubiShared"
    // 1.
    val xcf = XCFramework(frameworkName)
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = frameworkName
            isStatic = true
            // 2.
            xcf.add(this)
        }
    }
```

The setup is pretty much the same as the default KMP iOS setup, as you can see above, with only two differences:

1. A `XCFramework` configuration is required since we have a modularized app that uses Swift Package Manager. We need to set its name as the same name as the `ios.framework` generated by KMP (due to KMP limitations).
2. We need to add the `binaries.framework` reference to the `xcf` configuration.

This will enable the KMP shared code to be packaged inside a `xcframework` file which will be available for import via SwiftPM.

To generate the framework, though, we need to run the `assembleShubiSharedXCFramework` gradle task. We might add that task as a dependency of `embedAndSignAppleFrameworkForXcode`.

> [!NOTE]
> A `.framework` and `.xcframework` differ in the following:
>
> `.framework` was made to handle a single architecture/platform. You combine up to 2 architectures in it, by making a [FAT binary](https://en.wikipedia.org/wiki/Fat_binary), but it also comes with downsides of needing to strip the unused architecture at compile time and can't handle more than 2 architectures.
>
> `.xcframework` on the other hand, chooses the correct architecture instead of stripping. The `.xcframework` is essentially a collection of `.framework`’s.
>
> By calling the task `assembleShubiSharedXCFramework`, KMP builds separately all your targets and generates the `.framework` file and then merges them into a single `.xcframework`.
>
> Thank you @htdahms for this information!

> [!IMPORTANT]
> It is important to say that if your shared module has no code, KMP will not generate any `.framework` and consequently no `.xcframework`.
>
> For that reason, make sure you have the "KMP Hello world" `Greeting`/`Platform` class to be able to build the iOS app later.

#### iOS app project setup

In the `shubi-ios`, we need to change the following:

1. Change the `User Script Sandboxing` to `NO`. It can be changed at the project level.
2. The documentation asks to add the following path to `Framework Search Path > Search Path`: `$(SRCROOT)/../shubi-shared/build/xcode-frameworks/$(CONFIGURATION)/$(SDK_NAME)`, however, we didn't need that because of the way we use it. That will be required if we choose to use the KMP shared code in the root Shubi project.

It can be changed at the project level.

3. Under the `Build Phases`, add the following script **BEFORE** the `Compile Sources` phase:

```sh
cd "$SRCROOT/.."
./gradlew :shubi-shared:embedAndSignAppleFrameworkForXcode
```

That needs to be added per app, sadly. 4. Since the Shubi app has a custom build configuration other than the default (`Debug` and `Release`), the documentation says to add to the `User-Defined` section the `KOTLIN_FRAMEWORK_BUILD_TYPE` setting and set its value to `Debug` and `Release` accordingly with your build configuration settings.

In our case, I have chosen the following values for our configuration:

| Project build configuration | `KOTLIN_FRAMEWORK_BUILD_TYPE` value |
| --------------------------- | ----------------------------------- |
| `Debug`                     | `Debug`                             |
| `Release`                   | `Release`                           |
| `Release - Internal`        | `Release`                           |

It can be changed at the project level.

4. Inside the `Package.swift` file within the module we are using the shared code, we add the following:

```swift
#if DEBUG
    let buildType = "debug"
#else
    let buildType = "release"
#endif

let package = Package(
    name: "Details",
    defaultLocalization: "en",
    platforms: [ /* no changes */ ],
    products: [
        .library(
            name: "Details",
            targets: [
                "Details",
                "ShubiShared", // [!code highlight]
            ]
        ),
    ],
    dependencies: [ /* no changes */ ],
    targets: [
        .target(
            name: "Details",
            dependencies: [ /* no changes */ ]
        ),
        .binaryTarget(
            name: "ShubiShared",
            path: "../../../../shubi-shared/build/XCFrameworks/\(buildType)/ShubiShared.xcframework"
        ),
        .testTarget(/* no changes */),
    ]
)
```

That setup will be needed by the feature module.

> [!IMPORTANT]
> Since we are setting up the framework via Swift Package Manager, the `xcframework` should be built before trying to build the app. Need to investigate a way to workaround this.

#### iOS KMP Shared code usage
After setting up the `xcframework` dependency in the `Package.swift` file, we can directly use the code doing the following:
```swift

// First import the framework.
import ShubiShared

@Reducer
public struct Details {
    // lots of code

    public init() {}

    public var body: some ReducerOf<Self> {
        BindingReducer()
        Reduce { state, action in
            switch action {
            case .onAppear:
                // not related code
                
                // Greetings is kind of the Hello World of KMP.
                // Just used it to understand if the KMP was correctly 
                // plugged in the iOS app.
                // So we instantiate the class as it was a Swift class.
                let greetings = Greetings()

                analyticsClient.track(event: PageViewedAnalyticsEvent.content(
                    // and then use the .greet() method to print "Hello iOS!".
                    content: "\(state.currentContent.title) : \(greetings.greet())",
                    tab: state.fromTab,
                    contentType: contentType
                ))

                // lots of other code.
            }
        }
    }
}

```

With this procedure, we could understand how to plug in and integrate Kotlin code with Swift. This will enable us to later migrate a feature to the shared module (Analytics is a good candidate!).

No strange performance change has been detected yet, but it is too soon to understand if any impact will happen, but it is good to know that nothing changed.
