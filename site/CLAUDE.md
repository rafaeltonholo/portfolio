# site - Kobweb Application

CRITICAL: MUST read and follow [`AGENTS.md`](AGENTS.md) in this directory for
module-specific architecture, key files, and conventions.

Kobweb app that owns routes, features, and the locale wrapper. Produces the
static bundle under `../docs` via `kobweb export --layout static`.

```bash
cd site && kobweb run                  # Dev server
cd site && kobweb export --layout static
./gradlew :site:build
./gradlew :site:kspKotlinJs            # Regenerate Kobweb + Marktdown renderers
```
