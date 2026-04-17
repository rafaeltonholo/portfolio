# site - Kobweb Application

This module is the Kobweb application. It owns `@Page` routes, feature
screens, the site-scoped `core/` utilities (router, sections, site-only
components), and the `locale/` bridge to the Lyricist registry exposed by
`:resources`. The static export produced by `kobweb export --layout static`
lands in `../docs` per `.kobweb/conf.yaml`.

Read [../.ai/guidelines.md](../.ai/guidelines.md) first.

## Source Structure

```
site/src/jsMain/kotlin/dev/tonholo/portfolio/
├── pages/                             # @Page entry points
│   ├── Index.kt                       # "/"
│   ├── AboutPage.kt
│   ├── ArticlesPage.kt
│   ├── ProjectPage.kt
│   └── ResumePage.kt
├── features/                          # Feature screens pages delegate to
│   ├── home/
│   ├── about/
│   ├── articles/
│   ├── projects/
│   └── resume/
├── core/                              # Site-scoped helpers
│   ├── components/                    # Site-only composables
│   ├── router/                        # Route helpers + locale routing
│   ├── sections/                      # Reusable page sections (hero, footer, etc.)
│   └── ui/                            # Site-specific UI glue
└── locale/                            # Lyricist Strings resolver wrapper

site/src/jsMain/resources/public/      # Static assets copied into the export
└── public/{images,icons,fonts,docs,.well-known}/
```

## Key Conventions

- **Pages stay thin.** `pages/*.kt` is a `@Page` composable that locates the
  matching feature screen and passes it parameters. Real implementation lives
  under `features/<name>/`.
- **Site vs shared UI.** Site-only composables (things tied to a specific
  section or layout) live in `site/core/`. Truly reusable primitives belong in
  `:core`. Never duplicate.
- **Localization bridge.** Look strings up through `locale/`, which wraps the
  generated Lyricist `Strings` registry from `:resources`. Do not hard-code
  UI copy.
- **Register non-discoverable routes.** Locale-prefixed article pages and
  project detail pages must be added to `kobweb { app { export {
  addExtraRoute(...) } } }` in `build.gradle.kts`. Forgetting this leaves them
  out of the static export.
- **Head additions** (fonts, meta tags) go inside `kobweb { app { index {
  head.add { ... } } } }` in `build.gradle.kts` alongside the existing
  `/fonts/faces.css` link.
- **Prefer native Kobweb `Modifier` APIs** over `styleModifier`. See
  `.ai/guidelines.md#kobweb-specifics`.

## Common Commands

```bash
cd site && kobweb run                          # Dev server, live reload
cd site && kobweb export --layout static       # Static bundle into ../docs
./gradlew :site:build
./gradlew :site:kspKotlinJs                    # Regenerate KSP output
```

## Visual Verification

After any UI change:

1. Start `kobweb run`.
2. Use `mcp__claude-in-chrome__*` tools to navigate to the affected route,
   compare against the reference design, and fix every difference (spacing,
   color, font, layout, interactivity) before presenting the work.
3. For multi-step interactions, record a GIF via
   `mcp__claude-in-chrome__gif_creator`.
