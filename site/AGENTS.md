# site - Kobweb Application

This is the single active Gradle module. It owns `@Page` routes, feature
screens, Atomic Design components, theme tokens, i18n strings (hand-rolled
`Strings` sealed class), analytics wiring, and custom Gradle tasks
(`generateSitemap`, `generateBuildAnalytics`). The static export is produced
by `kobweb export` and uploaded to GitHub Pages via the Actions workflow - no
`docs/` directory is committed.

Read [../.ai/guidelines.md](../.ai/guidelines.md) first.

## Source Structure

```
site/src/jsMain/kotlin/dev/tonholo/portfolio/
├── pages/                             # @Page entry points (one per route)
├── features/                          # Feature screens pages delegate to
├── components/
│   ├── atoms/                         # Smallest reusable UI units
│   │                                  #   PhosphorIcon, Arrow, Caret, Placeholder, ...
│   ├── molecules/                     # Composed atoms (StatBlock, SectionHead, ...)
│   ├── organisms/                     # Larger page sections built from molecules
│   └── layouts/                       # Page-level layout shells
├── theme/                             # Design tokens: colors, typography, density, breakpoints
├── state/                             # UI state holders
├── domain/                            # Pure domain models (no framework deps)
├── locale/                            # Hand-rolled i18n via Strings sealed class
│                                      #   One subclass per locale (en, pt-BR)
├── navigation/                        # Route constants and locale routing helpers
├── extensions/                        # Kotlin extension utilities
└── analytics/                         # BuildAnalytics (generated) + UmamiInjector

site/src/jsMain/resources/public/      # Static assets copied into the export
└── public/{images,icons,fonts,docs,.well-known}/

site/src/jsTest/kotlin/                # kotlin-test + Power-Assert tests
```

Generated sources (not committed):
- `build/generated/analytics/kotlin/` - `BuildAnalytics` object from `generateBuildAnalytics`
- `src/jsMain/resources/public/sitemap.xml` - written by `generateSitemap`

## Shipped Routes

| Route | Registration |
|---|---|
| `/` | auto-discovered |
| `/about` | auto-discovered |
| `/projects` | auto-discovered |
| `/projects/{slug}` | `projectSlugs` list in `build.gradle.kts` |
| `/articles` | auto-discovered |
| `/articles/{slug}` | `articleSlugs` list in `build.gradle.kts` |
| `/resume` | auto-discovered |

## Key Conventions

- **Pages stay thin.** `pages/*.kt` is a `@Page` composable that delegates to
  the matching feature screen. Real implementation lives under `features/<name>/`.
- **i18n via hand-rolled `Strings` sealed class.** Add or update locale entries
  in `locale/`. No Compose Multiplatform Resources; no `Res.string.*` accessor.
  The Compose MP plugin was removed due to Skiko runtime incompatibility with
  Kobweb Compose HTML.
- **Icons via Phosphor.** Use `PhosphorIcon` composable (`components/atoms/`).
  The icon set loads from `@phosphor-icons/web@2.1.2` CDN. No unicode glyphs.
- **Register non-discoverable routes.** Dynamic slug routes must be added to
  `kobweb { app { export { addExtraRoute(...) } } }` in `build.gradle.kts` via
  the `projectSlugs` / `articleSlugs` lists. Forgetting this leaves them out of
  the static export.
- **Head additions** (fonts, meta tags, favicons) go inside `kobweb { app {
  index { head.add { ... } } } }` in `build.gradle.kts`.
- **Prefer native Kobweb `Modifier` APIs** over `styleModifier`. See
  [`.ai/guidelines.md`](../.ai/guidelines.md).
- **Detekt source-set tasks are the real lint gates.**
  `:site:detektJsMainSourceSet` and `:site:detektJsTestSourceSet`. The top-level
  `:site:detekt` reports NO-SOURCE and should not be relied on.

## Common Commands

```bash
cd site && kobweb run                             # Dev server, live reload
./gradlew :site:build                             # Full build
./gradlew :site:kspKotlinJs                       # Regenerate KSP output
./gradlew :site:detektJsMainSourceSet :site:detektJsTestSourceSet  # Lint
./gradlew :site:jsTest                            # Run tests
./gradlew :site:generateSitemap                   # Write sitemap.xml
./gradlew :site:generateBuildAnalytics            # Regenerate BuildAnalytics source
```

## Visual Verification

After any UI change:

1. Start `kobweb run`.
2. Use `mcp__claude-in-chrome__*` tools to navigate to the affected route,
   compare against the reference design, and fix every difference (spacing,
   color, font, layout, interactivity) before presenting the work.
3. For multi-step interactions, record a GIF via
   `mcp__claude-in-chrome__gif_creator`.
