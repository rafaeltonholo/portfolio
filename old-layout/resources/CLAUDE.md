# resources - i18n + Markdown Models

CRITICAL: MUST read and follow [`AGENTS.md`](AGENTS.md) in this directory for
module-specific architecture, key files, and conventions.

Kobweb library that owns the Lyricist string registry and the Marktdown model
generation for Markdown articles under `resources/markdown/`. Depends on
`:core`. Consumed by `:site`.

```bash
./gradlew :resources:build
./gradlew :resources:kspCommonMainKotlinMetadata   # Regenerate Lyricist + Marktdown models
```
