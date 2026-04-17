# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

# Portfolio - Claude Code Context

CRITICAL: MUST read and follow [`.ai/guidelines.md`](.ai/guidelines.md) at all
times. It contains code style, architecture, testing, and commit conventions
for this repository.

Read [`AGENTS.md`](AGENTS.md) for the repository overview, module map, and
verification workflow.

## Quick Reference

```bash
# Dev server (from the site module)
cd site && kobweb run                  # http://localhost:8080, Q to stop

# Production static export into ../docs (GitHub Pages source)
cd site && kobweb export --layout static

# Build
./gradlew build                        # Everything
./gradlew :site:build                  # App only
./gradlew :core:build                  # Shared UI library
./gradlew :resources:build             # i18n + markdown models

# KSP regen (Lyricist strings, Marktdown models/renderers)
./gradlew :resources:kspCommonMainKotlinMetadata
./gradlew :site:kspKotlinJs

# Included builds
./gradlew -p marktdown build
./gradlew -p kotlin-wrapper-shiki build
```

## Key Constraints

- **Kotlin Multiplatform, JS target only.** Shared code in `commonMain`,
  browser/DOM/Firebase in `jsMain`. No JVM or native targets.
- **Static export flow.** `site/.kobweb/conf.yaml` points `siteRoot` at
  `../docs`; the committed `docs/` tree is an artifact. Regenerate via
  `kobweb export --layout static`; preserve `docs/CNAME`.
- **Register extra routes in `site/build.gradle.kts`** via
  `kobweb { app { export { addExtraRoute(...) } } }` for any localized slug
  or project detail page Kobweb cannot auto-discover.
- **Split Marktdown pipeline** across `resources` (model generation) and
  `site` (renderer generation). Upgrade both build files together.
- **Firebase config required for `:core`.** `local.properties` at the repo
  root supplies the `firebase.*` keys consumed by `buildConfig`.
- **Verify UI changes visually** with the `mcp__claude-in-chrome__*` tools
  before declaring work done. Compilation success != visual correctness.
- **Conventional Commits**, no `Co-Authored-By` trailer, small logical
  commits. No em dashes anywhere. See `.ai/guidelines.md`.

## Module CLAUDE.md Files

Each Gradle module has its own `CLAUDE.md` referencing the module-specific
`AGENTS.md`:

- [site/CLAUDE.md](site/CLAUDE.md)
- [core/CLAUDE.md](core/CLAUDE.md)
- [resources/CLAUDE.md](resources/CLAUDE.md)
