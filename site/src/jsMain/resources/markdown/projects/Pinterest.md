---
layout: .components.layouts.ProjectMarkdownLayout
slug: pinterest
title: Pinterest - Modularizing a nine-year-old codebase
description: Incremental modularization and a centralized dynamic-feature dependency manager, delivered without a freeze, across a codebase with millions of users and nine years of history.
---

{{{ .components.organisms.projects.ProjectSection ("/ context", "Pinterest is 9 years of code, and it shows.")

By the time I joined, the Android codebase had grown into thousands of files, dozens of teams, and the kind of build times that made developers dread clean installs. A clean build averaged 30 minutes on a high-end machine with an i9 and 64GB of memory. An incremental build, 15.

That cost compounds. Every engineer on the team was losing real hours every week, and the delivery cadence suffered. Meanwhile, the product team wanted features delivered on-demand via Google Play Feature Delivery, which added another layer of complexity to an already intricate graph.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ problem", "The module graph had become the org chart.")

Every team owned their modules. Every module had subtly different conventions. Dynamic features didn't know how to find their dependencies without elaborate reflection-based lookups, which crashed at runtime whenever a feature wasn't ready yet. New work couldn't land without shuffling modules that had nothing to do with it, and that made the whole graph fragile.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ approach", "Smaller pieces, clearer contracts.")

We started with open communication. Product engineers, platform engineers, and the release team in the same channel, aligned on the plan so modularization never blocked feature work. That collaboration was the single most important thing we did.

The first technical job was boring on purpose: take inventory. We identified classes and resources with the fewest dependencies, extracted them into new modules, and only then walked up the graph toward the tightly coupled components. Small steps, continuous landing, no freeze.

Then we built a centralized dynamic-feature dependency manager. A small runtime library that exposed stable Kotlin interfaces for every feature module, handled the install and load lifecycle, and let consumers depend on contracts instead of reflection. Teams stopped owning their own discovery logic. Runtime crashes from missing features went away.

We also spent real time deleting things. Unused dependencies, dead code, and module-to-module edges that shouldn't have existed. Fewer edges, fewer unnecessary rebuilds, fewer surprises.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ outcomes", "What changed, with numbers.")

{{{ .components.organisms.projects.ProjectOutcomes ("-30%", "reduction in average build time", "stable", "dynamic features, no reflection crashes", "scales", "architecture ready for next 9 years") }}}

More important than the numbers: the dynamic-feature manager stopped being the thing engineers dreaded touching. Features got merged on the first review. The platform became a platform again.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ learnings", "A few things I took away.")

**Boring is good.** The version that won wasn't the clever one. It was the one that did the minimum possible, in the most obvious way, and left no room for teams to reinvent. Type-safe interfaces beat reflection every time.

**Incremental is everything.** We couldn't rip the graph apart and rebuild it; too much else depended on it. Every change had to ship, on its own, without breaking the hundreds of modules downstream. That constraint shaped the whole design.

**Talk to the people who live in it.** Some of the most useful feedback came from engineers who had worked in the codebase for years and had a long list of papercuts. They knew where the bodies were buried, and most of our best decisions came from those conversations.

}}}
