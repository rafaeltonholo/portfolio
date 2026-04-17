## Repository Overview

- **Primary language**: Kotlin (Kotlin Multiplatform, JS target).
- **Build system**: Gradle (Kotlin DSL) with typesafe project accessors.
- **Framework**: [Kobweb](https://github.com/varabyte/kobweb) (Silk UI, Compose
  HTML).
- **Repository structure**: Multi-module Gradle project plus two included
  builds.
- **Deployment**: Static export to `docs/`, served by GitHub Pages on the
  domain pinned in `docs/CNAME`.

> CRITICAL:
> All agents MUST follow [`.ai/guidelines.md`](.ai/guidelines.md) at all times
> for code style, architecture, commit conventions, and testing rules.

## Module-Specific Context

| Module                 | Description                                                                | AGENTS.md                                  |
|------------------------|----------------------------------------------------------------------------|--------------------------------------------|
| `site`                 | Kobweb application. Pages, features, site-scoped core and locale code.     | [site/AGENTS.md](site/AGENTS.md)           |
| `core`                 | Kobweb library. Cross-feature UI primitives and JS-only bindings.          | [core/AGENTS.md](core/AGENTS.md)           |
| `resources`            | Kobweb library. i18n registries (Lyricist) and Markdown model sources.     | [resources/AGENTS.md](resources/AGENTS.md) |
| `kotlin-wrapper-shiki` | Git submodule (Shiki syntax highlighter wrapper), wired as `includeBuild`. | See submodule's own README                 |
| `marktdown`            | Markdown -> Compose plugin + processor, wired as `includeBuild`.           | See `marktdown/README.md`                  |
| `docs`                 | Committed static export output. GH Pages serves from here.                 | -                                          |

## Verification Workflow

After making changes, perform an **empirical validation**:

1. **Build**: `./gradlew build` (or the narrower `:site:build` / `:core:build`
   / `:resources:build` while iterating).
2. **Run dev server**: from `site/`, `kobweb run`, and open
   http://localhost:8080.
3. **Visual check**: use the `mcp__claude-in-chrome__*` tools to navigate to
   the affected routes. For multi-step UI flows, record a GIF via
   `mcp__claude-in-chrome__gif_creator`. Zoom into each changed element on
   both the reference and the implementation before declaring the work done.
4. **Static export smoke**: `cd site && kobweb export --layout static`, then
   spot-check the generated files under `docs/` (especially any route added via
   `addExtraRoute(...)`).
5. **Tests**: run `./gradlew allTests` once the first test source set is
   wired. Until then, visual verification is mandatory for UI changes.

## Key Workflows

### Add a new page or feature

1. Add the route's `@Page` composable under
   `site/src/jsMain/kotlin/dev/tonholo/portfolio/pages/`.
2. Implement the screen under `features/<name>/` in the same module; share UI
   primitives via `:core`.
3. Add Lyricist strings in `resources/src/commonMain/kotlin/.../resources/`
   and pull them through `site/src/jsMain/kotlin/.../locale/`.
4. If the route is not auto-discovered (locale-prefixed, dynamic segment),
   register it via `kobweb { app { export { addExtraRoute(...) } } }` in
   `site/build.gradle.kts`.
5. Re-run the dev server and verify visually in Chrome.

### Add a new article

1. Drop the Markdown file into `resources/markdown/` following the existing
   naming convention (`<slug>-<locale>.md`).
2. Run `./gradlew :resources:kspCommonMainKotlinMetadata` to regenerate models,
   then `./gradlew :site:kspKotlinJs` to regenerate renderers.
3. Register any new localized slug via `addExtraRoute` in
   `site/build.gradle.kts` so it lands in the static export.

### Update localized strings

- Strings live in `resources/src/commonMain/kotlin/dev/tonholo/portfolio/resources/`
  grouped by feature (`pages/`, `projects/`, `workExperience/`).
- After editing, rerun `./gradlew :resources:kspCommonMainKotlinMetadata` so
  the Lyricist registry is regenerated; `site` picks up the change on the next
  build.

## Local Configuration

`core/build.gradle.kts` reads `local.properties` at configuration time for
Firebase keys. See
[.ai/guidelines.md#local-configuration](.ai/guidelines.md#local-configuration)
for the full list of keys; the file is git-ignored.

## Recommended Tools & MCP

- **`mcp__claude-in-chrome__*`**: mandatory for visual verification of UI
  changes. Supports navigation, screenshots, console and network inspection,
  and GIF recording.
- **Context7 MCP** (`mcp__plugin_context7_context7__*`): preferred over web
  search for Kobweb, Compose HTML, Kotlin, and Lyricist documentation.
- **`diff`**: handy when the static export changes to compare `docs/` output
  between builds.
