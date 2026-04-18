# site - Kobweb Application

CRITICAL: MUST read and follow [`AGENTS.md`](AGENTS.md) in this directory for
module-specific architecture, key files, and conventions.

Single active Kobweb module. Produces the static bundle uploaded to GitHub
Pages via the Actions workflow. No `docs/` directory is committed; no Lyricist;
no Marktdown included build; no `:core` or `:resources` modules.

```bash
cd site && kobweb run                  # Dev server
./gradlew :site:build                  # Full build
./gradlew :site:kspKotlinJs            # Regenerate KSP + Compose Resources output
./gradlew :site:detektJsMainSourceSet :site:detektJsTestSourceSet  # Lint
./gradlew :site:jsTest                 # Run tests
```
