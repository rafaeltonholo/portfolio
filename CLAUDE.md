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
- **i18n via hand-rolled `Strings` sealed class** under `locale/`. No Compose
  Multiplatform Resources, no `Res.string.*` accessor, no Lyricist. The Compose MP
  plugin was dropped because it pulled the Skiko runtime that is broken under
  Kobweb Compose HTML.
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

Phase 2 core is landed; see [`.ai/plans/phase2-redesign-plan.md`](.ai/plans/phase2-redesign-plan.md)
for future polish items. Shipped routes: `/`, `/about`, `/projects`, `/projects/{slug}`,
`/articles`, `/articles/{slug}`, `/resume`. Dynamic slug lists live in
`site/build.gradle.kts` as `projectSlugs` / `articleSlugs`.

Atomic Design package layout: `components/{atoms,molecules,organisms,layouts}`,
`theme/`, `state/`, `domain/`, `locale/`, `navigation/`, `extensions/`.

Icons use Phosphor Icons via `@phosphor-icons/web@2.1.2` CDN, wrapped by
`components/atoms/PhosphorIcon.kt`.

## Module CLAUDE.md Files

- [site/CLAUDE.md](site/CLAUDE.md)
