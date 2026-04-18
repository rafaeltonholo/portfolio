## Repository Overview

- **Primary language**: Kotlin (Kotlin Multiplatform, JS target).
- **Build system**: Gradle (Kotlin DSL) with typesafe project accessors.
- **Framework**: [Kobweb](https://github.com/varabyte/kobweb) (Silk UI, Compose HTML).
- **Repository structure**: Single `:site` Gradle module (Phase 1 scaffold). The old
  `:core`, `:resources`, `kotlin-wrapper-shiki`, and `marktdown` artifacts live under
  `old-layout/` for reference only and are NOT wired into any build.
- **Deployment**: GitHub Actions workflow (`.github/workflows/export-and-deploy-site.yml`)
  runs `kobweb export` and uploads the native GH Pages artifact from
  `site/.kobweb/site/`. No `docs/` directory is committed. No `CNAME` file in the repo.

> CRITICAL:
> All agents MUST follow [`.ai/guidelines.md`](.ai/guidelines.md) at all times
> for code style, architecture, commit conventions, and testing rules.

## Module Map

| Module | Description                                                  | AGENTS.md                        |
|--------|--------------------------------------------------------------|----------------------------------|
| `site` | Single Kobweb application. Pages, features, i18n, analytics. | [site/AGENTS.md](site/AGENTS.md) |

Source under `old-layout/` is reference only - do not import or depend on it.

## Phase 2 Redesign

Phase 2 core is landed. See [`.ai/plans/phase2-redesign-plan.md`](.ai/plans/phase2-redesign-plan.md)
for future polish items.

### Shipped routes

| Route | Notes |
|---|---|
| `/` | Home / hero |
| `/about` | About page |
| `/projects` | Project list |
| `/projects/{slug}` | Project detail - slugs registered via `projectSlugs` in `site/build.gradle.kts` |
| `/articles` | Article list |
| `/articles/{slug}` | Article detail - slugs registered via `articleSlugs` in `site/build.gradle.kts` |
| `/resume` | Resume / CV |

### Package structure (Atomic Design)

```
site/src/jsMain/kotlin/dev/tonholo/portfolio/
├── components/
│   ├── atoms/         # Smallest reusable UI units (PhosphorIcon, Arrow, Caret, ...)
│   ├── molecules/     # Composed atoms (StatBlock, SectionHead, ...)
│   ├── organisms/     # Larger sections built from molecules
│   └── layouts/       # Page-level layout shells
├── theme/             # Design tokens (colors, typography, spacing)
├── state/             # UI state holders
├── domain/            # Domain models (pure, no framework deps)
├── locale/            # Hand-rolled i18n via Strings sealed class (see note below)
├── navigation/        # Route helpers and locale routing
└── extensions/        # Kotlin extension utilities
```

### i18n approach

i18n uses a hand-rolled `Strings` sealed class under `locale/`, NOT Compose Multiplatform
Resources. The Compose MP plugin was dropped because it pulled the Skiko runtime, which is
broken under Kobweb Compose HTML. The `composeResources/` directory and `Res.string.*`
accessor are no longer in use.

### Icon stack

Phosphor Icons via `@phosphor-icons/web@2.1.2` CDN, wrapped by
`components/atoms/PhosphorIcon.kt`. No unicode glyphs.

### Known follow-up items

- Long-form article and project prose is still inline; markdown migration is pending.
- `pt-BR` translations cover nav, footer, LanguageToast, and hero CTAs only; all other
  copy falls back to English.
- `old-layout/` is still present; cleanup commit is pending.
- Favicon entries in `site/build.gradle.kts` `index.head.add` reference `/logo192.png`
  and `/logo512.png` (both now shipped).

## Verification Workflow

After making changes, perform an **empirical validation**:

1. **Lint**: `./gradlew :site:detektJsMainSourceSet :site:detektJsTestSourceSet`
   (the top-level `:site:detekt` reports NO-SOURCE and is a no-op; use the
   source-set-specific tasks).
2. **Build**: `./gradlew :site:build`.
3. **Run dev server**: from `site/`, `kobweb run`, and open http://localhost:8080.
4. **Visual check**: use the `mcp__claude-in-chrome__*` tools to navigate to the
   affected routes. For multi-step UI flows, record a GIF via
   `mcp__claude-in-chrome__gif_creator`. Zoom into each changed element before
   declaring the work done.
5. **Tests**: `./gradlew :site:jsTest` runs the `kotlin-test` + Power-Assert suite
   (`StringExtensionTest`, `UmamiInjectorTest`, `ResAccessorTest`).

## Key Workflows

### Add a new page or feature

1. Add the route's `@Page` composable under
   `site/src/jsMain/kotlin/dev/tonholo/portfolio/pages/`.
2. Implement the screen under `features/<name>/` in the same module.
3. Add localized strings in the `locale/` package via the hand-rolled `Strings`
   sealed class (one entry per supported locale).
4. If the route is not auto-discovered (locale-prefixed, dynamic segment),
   register it via `kobweb { app { export { addExtraRoute(...) } } }` in
   `site/build.gradle.kts`. Dynamic slug lists use `projectSlugs` / `articleSlugs`
   defined there.
5. Re-run the dev server and verify visually in Chrome.

### Update localized strings

- Strings are managed through the hand-rolled `Strings` sealed class under
  `locale/`. Add or update entries for each supported locale (en, pt-BR).
- No Compose Multiplatform Resources or `Res.string.*` accessor; the Compose MP
  plugin was removed due to Skiko runtime incompatibility with Kobweb Compose HTML.

### Update analytics / build metadata

- `app.properties` keys: `VERSION`, `LAST_MODIFIED`, `UMAMI_SCRIPT_URL`,
  `UMAMI_WEBSITE_ID`. Consumed at configuration time by `site/build.gradle.kts`.
- `generateBuildAnalytics` task produces
  `dev.tonholo.portfolio.analytics.BuildAnalytics` under
  `build/generated/analytics/kotlin/`.
- `UmamiInjector` appends the analytics `<script>` to `document.head` at app
  init; it skips `localhost`, `127.0.0.1`, and empty hostnames automatically.

## Recommended Tools & MCP

- **`mcp__claude-in-chrome__*`**: mandatory for visual verification of UI
  changes. Supports navigation, screenshots, console and network inspection,
  and GIF recording.
- **Context7 MCP** (`mcp__plugin_context7_context7__*`): preferred over web
  search for Kobweb, Compose HTML, and Kotlin documentation.
