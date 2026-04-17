# Portfolio - AI Agent Guidelines

These guidelines MUST be followed at all times when working in this repository.

## Repository Overview

Personal portfolio site for Rafael Tonholo. Kotlin Multiplatform web app built
on [Kobweb](https://github.com/varabyte/kobweb), targeting JS (browser) only.
Content is authored in Markdown and rendered via the Compose-based `marktdown`
toolchain. UI strings are localized through Lyricist (`en`, `pt-BR`). The
production artifact is a static export under `docs/`, served by GitHub Pages
on the domain pinned by `docs/CNAME`.

## Project Structure

```
.
├── AGENTS.md                          # Root repository agent context
├── CLAUDE.md                          # Claude Code entry point
├── .ai/
│   └── guidelines.md                  # AI agent guidelines (you are here)
├── build.gradle.kts                   # Root Gradle build
├── settings.gradle.kts                # Root settings (defines included builds)
├── gradle/
│   └── libs.versions.toml             # Shared version catalog
├── site/                              # Kobweb application
│   ├── .kobweb/conf.yaml              # Dev/prod server config, siteRoot
│   └── src/jsMain/kotlin/...          # Pages, features, site-scoped core
├── core/                              # Shared UI library (Kobweb library)
│   └── src/{commonMain,jsMain}/...    # UI primitives, Firebase bindings
├── resources/                         # i18n + Markdown models
│   ├── markdown/                      # Source Markdown articles
│   └── src/commonMain/kotlin/...      # Lyricist string registries
├── kotlin-wrapper-shiki/              # Git submodule, includeBuild
├── marktdown/                         # Included build (MD -> Compose)
└── docs/                              # Committed static export (GH Pages)
```

## Build Commands

### Dev server

```bash
# Must run from the site module
cd site && kobweb run                  # http://localhost:8080, Q to stop
```

### Building

```bash
./gradlew build                        # Build all modules + included builds
./gradlew :site:build                  # App only
./gradlew :core:build                  # Shared UI library
./gradlew :resources:build             # i18n + markdown models
./gradlew -p marktdown build           # Included build
./gradlew -p kotlin-wrapper-shiki build # Included build (submodule)
```

### Static export (production)

```bash
cd site && kobweb export --layout static
# Writes into ../docs per site/.kobweb/conf.yaml (siteRoot: "../docs")
```

### KSP regeneration

```bash
./gradlew :resources:kspCommonMainKotlinMetadata   # Lyricist + Marktdown models
./gradlew :site:kspKotlinJs                        # Kobweb pages + Marktdown renderers
```

### Testing

No test source sets exist yet. Adding one requires wiring the Kotlin test plugin
in the target module before `./gradlew allTests` does anything.

## Code Style and Quality

- **Kotlin code style**: Official (`kotlin.code.style=official` in
  `gradle.properties`).
- **Max line length**: 120 characters (`.editorconfig`).
- No Detekt or ktlint configuration currently wired; rely on the IntelliJ
  formatter plus these guidelines.

### Mandatory Kotlin rules

- **No `!!` operator**. If the compiler says a value can be null, handle it
  (`?.`, `?: return`, `checkNotNull`, `requireNotNull`, or redesign).
- **No wildcard imports**. Always use explicit single imports.
- **Avoid full qualifiers**. Add imports (with alias on collision) rather than
  writing `some.pkg.core.Color` inline.
- **Prefer named arguments** for anything beyond obvious positional calls.
  Helps readability and avoids magic-number detekt issues if linting is added
  later.
- **Use `$$` raw-string prefix** for literal `${...}` strings in Kotlin build
  scripts (Kotlin 2.3+ syntax).
- **No em dashes (`-`)** anywhere. Use regular dashes (-), commas, periods, or
  semicolons.

### Design principles

- SOLID, KISS, DRY, YAGNI, composition over inheritance.
- Keep functions short (< 20 lines when practical), parameters few (<= 3-4),
  nesting shallow (<= 2-3 levels).
- Use early returns. Avoid magic numbers/strings.
- Prefer constructor injection; depend on abstractions.
- Keep domain models free of framework dependencies.
- One top-level class per file unless they clearly belong together.

### Shared contracts across modules

- Never duplicate data structures or constants across modules. If a class or
  constant appears in two places, refactor into a shared module (`core` or
  `resources`) immediately.
- Single source of truth: every piece of data/logic has exactly one
  authoritative definition; everything else imports it.
- When one module writes a file that another reads, use a shared serializable
  data class (`kotlinx.serialization`) instead of manual JSON string handling.

## Kobweb specifics

- **Prefer native `Modifier` functions** over `styleModifier { property(...) }`.
  Common examples: `Modifier.display(DisplayStyle.Flex)`,
  `Modifier.borderCollapse(BorderCollapse.Collapse)`,
  `Modifier.tableLayout(TableLayout.Fixed)`,
  `Modifier.scrollMargin(top = ...)`,
  `Modifier.maxHeight(calc { ... })`,
  `Modifier.aspectRatio(w, h)`,
  `Modifier.role("tabpanel")`. Only fall back to `styleModifier` after
  confirming no native equivalent exists (e.g., `repeating-conic-gradient`).
- **`CssStyle` must be public.** Kobweb's KSP processor registers styles at
  compile time; private styles are silently dropped.
- **`addExtraRoute(...)`** in `site/build.gradle.kts` is the registry for any
  route Kobweb cannot auto-discover (localized article slugs, project detail
  pages). Missing entries mean missing files in the static export.
- **Verify visually in the browser.** Use the `mcp__claude-in-chrome__*` tools
  after any UI change: navigate, inspect, screenshot or GIF-record
  multi-step flows, and compare against the reference before declaring work
  done. Compilation success does not imply visual correctness.

## Commit Conventions

Use [Conventional Commits](https://www.conventionalcommits.org/):

```
<type>(<scope>): <subject>
```

**Types**: `feat`, `fix`, `docs`, `refactor`, `perf`, `test`, `chore`

**Scopes** (optional): `site`, `core`, `resources`, `marktdown`, `shiki`,
`build`, `ci`, `docs`, etc.

**Rules**:

- Subject under ~72 characters, imperative mood.
- Small, logical commits. Every commit should compile and ideally not break
  the site.
- Do NOT include a `Co-Authored-By` trailer.
- Reference GitHub issues when applicable (`Closes #123`).

**Examples**:

- `feat(site): add resume page hero section`
- `fix(core): avoid ssr crash when window is undefined`
- `chore(build): bump kobweb to 0.20.0`

## Branch Naming

```
feat/<short-topic>
fix/<short-topic>
docs/<short-topic>
chore/<short-topic>
```

## Testing Guidelines

- TDD workflow is required when adding new behavior. Write the failing test
  first, then the minimal code to make it pass, then refactor.
- Tests follow the AAA pattern with `given ... when ... then ...` naming.
  Example: `given userSpaceOnUse radial gradient with translate scale transform,
  when toBrush is called, then center and radius are transformed`.
- Body structured as clear Arrange / Act / Assert sections.
- No test source sets are wired up yet. The first test added to a module must
  also wire `kotlin("test")` (or the appropriate test plugin) in that module's
  `build.gradle.kts`.

## Module-Specific Guidelines

Before modifying code in a module, read its `AGENTS.md`:

| Module      | Docs                                          |
|-------------|-----------------------------------------------|
| `site`      | [site/AGENTS.md](../site/AGENTS.md)           |
| `core`      | [core/AGENTS.md](../core/AGENTS.md)           |
| `resources` | [resources/AGENTS.md](../resources/AGENTS.md) |

Included builds (`marktdown/`, `kotlin-wrapper-shiki/`) are standalone
repositories with their own Gradle wrapper and README. Treat them as
third-party projects vendored via `includeBuild`.

## Key Architectural Decisions

- **Kotlin Multiplatform, JS-only.** All modules declare a `js { browser() }`
  target. Keep shared logic in `commonMain`; browser/DOM/Firebase code in
  `jsMain`. No JVM or native targets are configured.
- **Typesafe project accessors** enabled via `TYPESAFE_PROJECT_ACCESSORS` in
  `settings.gradle.kts`. Use `projects.core` / `projects.resources` instead of
  `project(":core")`.
- **Version catalog** at `gradle/libs.versions.toml` is the single source of
  truth for dependency versions. Check it before adding new dependencies; only
  add libraries that support KMP JS.
- **Split Marktdown pipeline.** `resources` generates data models from
  `resources/markdown/*.md` (`renderers.disableGeneration()`); `site` generates
  the renderers from those models
  (`renderers.modelsContainerProject = projects.resources`,
  `models.srcDir("../resources/build/generated/marktdown/...")`). Changing
  either side alone will break the other; update both build files together
  when upgrading the plugin.
- **Included builds replace vendored sources.**
    - `kotlin-wrapper-shiki/` is a git submodule that publishes the
      `dev.tonholo.kotlin.wrapper.shiki:*` artifacts. Local changes are picked
      up automatically.
    - `marktdown/` is a sibling Gradle project exposing the
      `dev.tonholo.marktdown` Gradle plugin plus `marktdown-core` /
      `marktdown-processor` artifacts.
- **Static site deploy via `docs/`.** `site/.kobweb/conf.yaml` sets
  `siteRoot: "../docs"`. The committed `docs/` tree is a build artifact; always
  regenerate with `kobweb export --layout static` rather than hand-editing.
  Preserve `docs/CNAME`.

## Multiplatform Constraints

- **Source set isolation.** Keep shared logic in `commonMain`. Do not introduce
  JVM-specific APIs (`java.io`, `java.nio`, `javax.*`) into common code, even
  though the only current target is JS.
- **Dependency compatibility.** Verify any new library supports Kotlin
  Multiplatform JS before adding it.
- **Browser-only APIs** (DOM, `window`, Firebase JS SDK) must stay in `jsMain`.
- **Compose runtime** flows through the JetBrains Compose plugin; do not mix
  in Jetpack Compose AndroidX artifacts.

## Local Configuration

`core/build.gradle.kts` reads `local.properties` at configuration time via the
`buildconfig` plugin. Create the file at the repo root with your Firebase
values before running Gradle:

```properties
firebase.apiKey=...
firebase.authDomain=...
firebase.projectId=...
firebase.storageBucket=...
firebase.messagingSenderId=...
firebase.appId=...
firebase.measurementId=...
```

The file is git-ignored. Missing properties fail configuration.

## Prerequisites

- JDK 17+.
- Git with submodule support (`git submodule update --init --recursive` to
  hydrate `kotlin-wrapper-shiki/`).
- Kobweb CLI installed (see https://kobweb.varabyte.com) for `kobweb run` and
  `kobweb export`.
- IntelliJ IDEA (or Android Studio) with Kotlin plugin.
