---
title: The honest cost of KMP
date: mar 12, 2026
description: I shipped a KMP feature to production last quarter. Here's what it gave me, and here's what it took.
tags:
  - kmp
  - kotlin
readTime: 12 min
---

Kotlin Multiplatform is a good trade, not a free one. A quarter on from shipping my first KMP feature, I'm still enthusiastic about it, but I want to tell the truth about what the trade involved.

## What it gave me

One domain layer. One set of tests. One place where the business rules live, reviewed once, by humans who understood the problem. That is the pitch, and the pitch is accurate. The day we caught a subtle rounding bug by writing the repair once and deploying it to both apps, I stopped wondering whether KMP was worth it.

## What it took

It took up-front investment in the build. Expect-actual declarations, platform source sets, and a mental map for where each piece of code is allowed to live. It took patience when a new library's multiplatform artifact lagged behind its JVM one. And it took a conversation with the team about where the boundary lived, which is the kind of debate that a monorepo settles through pattern-matching but KMP forces into the open.

## When I'd do it again

When the domain logic is non-trivial, yes. When the domain logic is a thin CRUD veneer, no. The feature has to be worth the ceremony. If you can't articulate what you're sharing and why, the ceremony is a net cost.
