# Portfolio

Personal site of Rafael Tonholo, Android engineer based in Halifax, Nova Scotia.
Live at [rafael.tonholo.dev](https://rafael.tonholo.dev).

Built with [Kobweb](https://github.com/varabyte/kobweb) (Silk UI on Compose HTML).
Kotlin Multiplatform, JS target only. Everything is exported as a static site.

## Features

- Pages: home, about, projects, project detail, articles, article detail, resume, and a themed 404.
- Two languages: English and Brazilian Portuguese (`pt-BR`).
- Light and dark modes that follow the OS.
- Articles and project case studies written in Markdown.
- Privacy-friendly analytics with [Umami](https://umami.is) (skipped on localhost).
- Icons from [Phosphor Icons](https://phosphoricons.com); fonts are JetBrains Mono, Geist, and Fraunces.

## Tech stack

| Area      | Choice                                                              |
|-----------|---------------------------------------------------------------------|
| Language  | Kotlin 2.4.21                                                       |
| Framework | Kobweb 0.25.1 (Silk, Compose HTML 1.11.1)                           |
| Markdown  | kobwebx-markdown                                                    |
| Build     | Gradle (Kotlin DSL), version catalog in `gradle/libs.versions.toml` |
| Lint      | detekt 2.0.0-alpha.6 with ktlint wrapper and Compose rules          |
| Tests     | `kotlin-test` with Power-Assert                                     |
| Hosting   | GitHub Pages                                                        |

## Requirements

- JDK 21
- [Kobweb CLI](https://github.com/varabyte/kobweb-cli) 0.9.23 (the version CI uses)

## Getting started

Start the dev server from the `site` folder:

```bash
cd site
kobweb run
```

Open [http://localhost:8080](http://localhost:8080). Press `Q` in the terminal to stop it.
The site reloads when you change code.

## Common commands

```bash
./gradlew :site:build                                              # Build
./gradlew :site:detektJsMainSourceSet :site:detektJsTestSourceSet  # Lint
./gradlew :site:jsTest                                             # Tests
./gradlew :site:generateSitemap                                    # Write sitemap.xml
```

The top-level `:site:detekt` task has no sources and does nothing. Use the source-set tasks above.

## Project layout

```
.
├── app.properties        # VERSION, LAST_MODIFIED, UMAMI_SCRIPT_URL, UMAMI_WEBSITE_ID
├── build-logic/          # Gradle convention code
├── config/detekt.yml     # Lint rules
├── site/                 # The Kobweb app (single Gradle module)
│   └── src/
│       ├── jsMain/kotlin/dev/tonholo/portfolio/
│       │   ├── analytics/    # Umami injector and analytics manager
│       │   ├── components/   # atoms, molecules, organisms, layouts
│       │   ├── domain/       # Pure domain models
│       │   ├── extensions/   # Kotlin extension helpers
│       │   ├── locale/       # Hand-rolled i18n (Strings sealed class: en, pt-BR)
│       │   ├── navigation/   # Routes and locale routing
│       │   ├── pages/        # @Page composables
│       │   ├── state/        # UI state holders
│       │   └── theme/        # Design tokens
│       ├── jsMain/resources/
│       │   ├── markdown/     # articles/ and projects/ content
│       │   └── public/       # Images, icons, favicons
│       └── jsTest/           # Tests
└── old-layout/           # Previous implementation, reference only
```

## Routes

| Route              | Page           |
|--------------------|----------------|
| `/`                | Home           |
| `/about`           | About          |
| `/projects`        | Project list   |
| `/projects/{slug}` | Project detail |
| `/articles`        | Article list   |
| `/articles/{slug}` | Article detail |
| `/resume`          | Resume         |

Localized pages live under a language segment, for example `/articles/pt-br/{slug}`.
Routes that Kobweb cannot find on its own (localized slugs and detail pages) are registered
in `site/build.gradle.kts` with `addExtraRoute`.

## Content

- Articles: add a Markdown file to `site/src/jsMain/resources/markdown/articles/`.
  Name the Portuguese version with a `PtBr` suffix, for example `HelloWorldPtBr.md`.
- Project case studies: same idea under `markdown/projects/`.
- Slugs are picked up from the file names at build time.
- Projects without a case study (such as `svg-compose`) are listed in `projectSlugs` in `site/build.gradle.kts`.

## Deployment

Pushing to `main` runs `.github/workflows/export-and-deploy-site.yml`. It:

1. Runs lint and tests.
2. Exports the site with `kobweb export`.
3. Uploads `site/.kobweb/site/` as the GitHub Pages artifact.

No `docs/` folder or `CNAME` file is committed.

To try a production export locally:

```bash
cd site
kobweb export
kobweb run --env prod
```

## Contributing and AI agents

- Code style, architecture, testing, and commit rules are in [`.ai/guidelines.md`](.ai/guidelines.md).
- Repository overview and verification steps are in [`AGENTS.md`](AGENTS.md).
- Commits follow Conventional Commits.
