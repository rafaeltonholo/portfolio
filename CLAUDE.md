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
# Dev server (from the site directory)
cd site && kobweb run                  # http://localhost:8080, Q to stop

# Build
./gradlew :site:build                  # Only active module

# Lint (use source-set tasks; top-level detekt reports NO-SOURCE)
./gradlew :site:detektJsMainSourceSet :site:detektJsTestSourceSet

# Tests
./gradlew :site:jsTest

# KSP regen (Compose Resources + Kobweb KSP)
./gradlew :site:kspKotlinJs

# Sitemap
./gradlew :site:generateSitemap        # Writes site/src/jsMain/resources/public/sitemap.xml
```

## Key Constraints

- **Kotlin Multiplatform, JS target only.** Shared code in `commonMain`,
  browser/DOM in `jsMain`. No JVM or native targets.
- **Single `:site` module.** The old `:core`, `:resources`, `kotlin-wrapper-shiki`,
  and `marktdown` modules are deleted. Their source is in `old-layout/` for
  reference only - do not import from there.
- **i18n via Compose Multiplatform Resources.** Strings are in
  `site/src/jsMain/composeResources/values/strings.xml` (en) and
  `values-pt-rBR/strings.xml` (pt-BR). Access via the generated `Res.string.*`
  accessor (`dev.tonholo.portfolio.resources` package). No Lyricist.
- **No `docs/` output committed.** Deployment uses the native GitHub Actions GH
  Pages artifact uploaded from `site/.kobweb/site/`. No `CNAME` file in the repo.
- **Register extra routes in `site/build.gradle.kts`** via
  `kobweb { app { export { addExtraRoute(...) } } }` for any localized slug
  or project detail page Kobweb cannot auto-discover.
- **`app.properties` drives build metadata and analytics.** Keys: `VERSION`,
  `LAST_MODIFIED`, `UMAMI_SCRIPT_URL`, `UMAMI_WEBSITE_ID`.
- **Verify UI changes visually** with the `mcp__claude-in-chrome__*` tools
  before declaring work done. Compilation success != visual correctness.
- **Conventional Commits**, no `Co-Authored-By` trailer, small logical
  commits. No em dashes anywhere. See `.ai/guidelines.md`.

## Phase 2 Redesign

The Atomic Design UI rebuild (7 routes, design tokens, etc.) is planned but has
NOT started. See [`.ai/plans/PRD.md`](.ai/plans/PRD.md) and
[`.ai/plans/phase2-redesign-plan.md`](.ai/plans/phase2-redesign-plan.md).

## Module CLAUDE.md Files

- [site/CLAUDE.md](site/CLAUDE.md)
