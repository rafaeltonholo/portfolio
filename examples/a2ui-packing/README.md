# A2UI Packing

A small Android sample where a local LiteRT-LM model writes a packing checklist in A2UI Express, a Kotlin compiler turns it into A2UI v0.9.1 messages, and AndroidX A2UI Compose renders it.

It has two modules:

- `express` compiles a subset of [A2UI Express](https://github.com/a2ui-project/a2ui/blob/main/specification/proposals/express/a2ui_express.md) into A2UI wire messages. It's Kotlin Multiplatform common code with JVM and Android targets, and its only dependency is kotlinx.serialization.
- `app` loads the model, builds the prompt from the catalog, compiles the response and renders the surface. `PackingViewModel` owns the request and the generation state, and `ChecklistSurface` hosts the AndroidX renderer.

The sample renders generated UI. It doesn't run the action round trip.

## Build and test

Use JDK 17 or newer and the Gradle wrapper, which is pinned to 9.9.1.

```shell
./gradlew :express:jvmTest :app:testDebugUnitTest :app:assembleDebug
```

`PackingSurfaceTest` checks that a recorded Express response renders its title and bound checkboxes. Run it on one device:

```shell
adb -s <device-serial> shell am instrument -w -e class dev.tonholo.a2uipacking.PackingSurfaceTest dev.tonholo.a2uipacking.test/androidx.test.runner.AndroidJUnitRunner
```

## Run with a local model

Download a model supported by LiteRT-LM, such as [`Qwen3-0.6B.litertlm`](https://huggingface.co/litert-community/Qwen3-0.6B/blob/main/Qwen3-0.6B.litertlm) or [`gemma-4-E2B-it.litertlm`](https://huggingface.co/litert-community/gemma-4-E2B-it-litert-lm/blob/main/gemma-4-E2B-it.litertlm). Review the model card and its terms before use.

Install the app, then copy the model to app-private storage:

```shell
adb -s <device-serial> install -r app/build/outputs/apk/debug/app-debug.apk
adb -s <device-serial> push gemma-4-E2B-it.litertlm /data/local/tmp/
adb -s <device-serial> shell run-as dev.tonholo.a2uipacking mkdir -p files
adb -s <device-serial> shell run-as dev.tonholo.a2uipacking cp /data/local/tmp/gemma-4-E2B-it.litertlm files/
adb -s <device-serial> shell rm /data/local/tmp/gemma-4-E2B-it.litertlm
```

The app lists every `.litertlm` file in its storage as a model chip, so open it, pick the model, and tap a suggestion or type a request. The model isn't bundled in the APK, and reinstalling with `install -r` keeps it, while uninstalling deletes it.

## Benchmark JSON against Express

`FormatBenchmarkTest` asks one model for the same five checklists twice in each format, checks every response, and appends one JSON line per run to `files/benchmark-<model>.jsonl` in app storage:

```shell
adb -s <device-serial> install -r app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk
adb -s <device-serial> shell am instrument -w -e model gemma-4-E2B-it.litertlm -e class dev.tonholo.a2uipacking.FormatBenchmarkTest dev.tonholo.a2uipacking.test/androidx.test.runner.AndroidJUnitRunner
adb -s <device-serial> shell run-as dev.tonholo.a2uipacking cat files/benchmark-gemma-4-E2B-it.jsonl
```

## Probe the renderer race

`RendererRaceProbe` sends the recorded checklist in two batches on a real clock and holds the screen for nine seconds. Read the result with `android layout` while it runs. It has no assertions, because the race it probes is intermittent:

```shell
adb -s <device-serial> shell am instrument -w -e order components-first -e collector default -e gap 0 -e class dev.tonholo.a2uipacking.RendererRaceProbe dev.tonholo.a2uipacking.test/androidx.test.runner.AndroidJUnitRunner
```

The runs behind the article are in `benchmark-results/renderer-race.txt`.

## Versions checked on November 13, 2026

| Library | Version |
| --- | --- |
| AndroidX A2UI Compose | `1.0.0-alpha02` |
| LiteRT-LM | `0.19.1` |
| kotlinx.serialization | `1.13.0` |
| Kotlin | `2.5.0-RC` |
| Android Gradle Plugin | `9.6.0-alpha03` |
| Gradle | `9.9.1` |

Check the [AndroidX A2UI release notes](https://developer.android.com/jetpack/androidx/releases/a2ui-compose) and [LiteRT-LM releases](https://github.com/google-ai-edge/LiteRT-LM/releases) for newer versions.

## Attribution

`app/src/main/assets/a2ui/basic_catalog_v0_9.json` is the [A2UI v0.9 basic catalog](https://a2ui.org/specification/v0_9_1/catalogs/basic/catalog.json) from the [A2UI project](https://github.com/a2ui-project/a2ui), licensed under the Apache License 2.0.

The icons in `app/src/main/res/drawable/ic_*.xml`, except the launcher icons, are [Material Symbols](https://github.com/google/material-design-icons) by Google, licensed under the Apache License 2.0.
