# site - Kobweb Application

This is the single active Gradle module. It owns `@Page` routes, feature
screens, site-scoped utilities, i18n strings (Compose Multiplatform Resources),
analytics wiring, and custom Gradle tasks (`generateSitemap`,
`generateBuildAnalytics`). The static export is produced by `kobweb export`
and uploaded to GitHub Pages via the Actions workflow - no `docs/` directory
is committed.

Read [../.ai/guidelines.md](../.ai/guidelines.md) first.

## Source Structure

```
site/src/jsMain/kotlin/dev/tonholo/portfolio/
├── pages/                             # @Page entry points
│   └── Index.kt                       # "/"
├── features/                          # Feature screens pages delegate to
├── core/                              # Site-scoped helpers
│   ├── components/                    # Site-only composables
│   ├── router/                        # Route helpers + locale routing
│   ├── sections/                      # Reusable page sections (hero, footer, etc.)
│   └── ui/                            # Site-specific UI glue
└── analytics/                         # BuildAnalytics (generated) + UmamiInjector

site/src/jsMain/composeResources/      # Compose Multiplatform Resources
├── values/strings.xml                 # Default (en) localized strings
└── values-pt-rBR/strings.xml          # Portuguese (pt-BR) strings

site/src/jsMain/resources/public/      # Static assets copied into the export
└── public/{images,icons,fonts,docs,.well-known}/

site/src/jsTest/kotlin/                # kotlin-test + Power-Assert tests
```

Generated sources (not committed):
- `build/generated/analytics/kotlin/` - `BuildAnalytics` object from `generateBuildAnalytics`
- `src/jsMain/resources/public/sitemap.xml` - written by `generateSitemap`

## Key Conventions

- **Pages stay thin.** `pages/*.kt` is a `@Page` composable that delegates to
  the matching feature screen. Real implementation lives under `features/<name>/`.
- **i18n via Compose Resources.** Strings are accessed through the generated
  `Res.string.*` accessor (package `dev.tonholo.portfolio.resources`). No Lyricist.
  Edit `composeResources/values/strings.xml` and the `values-pt-rBR/` counterpart.
- **Register non-discoverable routes.** Locale-prefixed article pages and
  project detail pages must be added to `kobweb { app { export {
  addExtraRoute(...) } } }` in `build.gradle.kts`. Forgetting this leaves them
  out of the static export.
- **Head additions** (fonts, meta tags) go inside `kobweb { app { index {
  head.add { ... } } } }` in `build.gradle.kts`.
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
