---
title: Compose stability, without the cult
date: apr 10, 2026
description: Why most of what you read about @Stable is wrong, and what actually matters when you're profiling recompositions in real apps.
tags:
  - compose
  - performance
readTime: 9 min
status: draft
---

I spent a week last month trying to figure out why a list was re-composing on every scroll tick. Not every frame - just most of them. Enough that you could feel it. Enough that QA opened a ticket.

## The problem

In Compose, everything is a function. That's the gift. The trap is that functions re-run easily, and nothing tells you, by default, which functions are running how often. The recomposition layer is deeply hidden, and most of the advice online is either (a) cargo-culted or (b) last year's.

```kotlin
// lazy-column.kt
LazyColumn {
  items(state.items, key = { it.id }) { item ->
    val enriched = remember(item.id) { enrich(item) }
    ItemRow(enriched)
  }
}
```

This looks fine. This *is* fine, until `enrich` starts doing more than it lets on. And then, suddenly, it isn't.

## What @Stable really means

It means: I promise, to the best of my ability, that if two instances of this class are `.equals()`, then their observable properties will be `.equals()` too. It is a promise - not an assertion. The compiler cannot verify it. You, the human, are responsible.

## Profiling, for real

Android Studio has a Compose recomposition layer inspector. Most people don't know it exists. Enable it, scroll the UI, watch which composables pulse yellow, repeatedly. That's your list.

## A workflow that scales

Profile first, annotate second. Adding `@Stable` everywhere is cargo-culting. Adding it where the profiler showed you a problem is engineering.

## Conclusion

There is no cult here. There are tools, there are measurements, and there is the slow, boring work of making your code a little less chatty over time. That's the whole game.
