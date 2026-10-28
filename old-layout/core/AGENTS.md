# core - Shared UI Library

This module is the shared Kobweb library. It holds UI primitives reused across
features, plus JS-only bindings for Firebase and analytics. `:site` and
`:resources` depend on it. Nothing in `core` may depend on `:site` or
`:resources`.

Read [../.ai/guidelines.md](../.ai/guidelines.md) first.

## Source Structure

```
core/src/commonMain/kotlin/dev/tonholo/portfolio/core/
├── ui/                                # Design system primitives
│   ├── theme/                         # Colors, typography, spacing tokens
│   ├── text/                          # Text styles, rich text helpers
│   └── unit/                          # Dimensions, conversions
├── foundation/
│   └── layout/                        # Row/Column/Box-style layout helpers
├── components/                        # Reusable composables (buttons, cards, ...)
├── collections/                       # Collection utilities
├── extensions/                        # Misc Kotlin extension functions
└── feature/                           # Common feature-level abstractions

core/src/jsMain/kotlin/dev/tonholo/portfolio/core/
├── ui/                                # JS-specific design-system extensions
├── foundation/
│   ├── layout/                        # DOM-backed layout primitives
│   └── text/                          # Compose HTML text implementations
├── components/                        # Browser-only variants of common components
├── extensions/                        # JS-only extensions (DOM, kotlin-js)
├── binding/
│   └── firebase/                      # Firebase JS SDK wrapper
└── analytics/
    └── events/                        # Analytics event definitions
```

## Build Configuration

`core/build.gradle.kts` is special:

- Uses the `com.github.gmazzo.buildconfig` plugin to generate
  `dev.tonholo.portfolio.config.BuildConfig` from `local.properties`. The
  generated fields (`FIREBASE_API_KEY`, `FIREBASE_AUTH_DOMAIN`,
  `FIREBASE_PROJECT_ID`, `FIREBASE_STORAGE_BUCKET`,
  `FIREBASE_MESSAGING_SENDER_ID`, `FIREBASE_APP_ID`,
  `FIREBASE_MEASUREMENT_ID`) are consumed by `core/binding/firebase`.
- Pulls in the Firebase JS SDK via `npm("firebase", "10.12.0")` in `jsMain`.
- Adds `-Xexpect-actual-classes` to the compiler arguments so expect/actual
  classes are allowed.

## Conventions

- **No framework dependencies in `commonMain`.** Keep Kobweb/Silk imports in
  `jsMain`. `commonMain` may depend on `compose.ui` but not on
  `compose.html.core` or Kobweb artifacts.
- **No browser-only APIs in `commonMain`.** DOM, `window`, and the Firebase
  JS SDK stay in `jsMain`.
- **Public `CssStyle`.** Any `CssStyle` or `CssStyle` variant must be `public`
  (not `private`) so Kobweb KSP can register it.
- **Prefer native `Modifier` functions** over `styleModifier` wrappers. See
  `.ai/guidelines.md#kobweb-specifics`.
- **Firebase config is build-time.** Never log or expose `BuildConfig.*`
  Firebase values at runtime beyond what Firebase requires.

## Common Commands

```bash
./gradlew :core:build
./gradlew :core:compileCommonMainKotlinMetadata
./gradlew :core:generateBuildConfig
```
