---
title: Composing SVGs at build time
date: feb 02, 2026
description: How svg-to-compose works, and why generating code is still the most under-appreciated Kotlin trick.
tags:
  - compose
  - tooling
readTime: 6 min
---

Most people treat code generation as a workaround. I treat it as one of the first tools I reach for, because it turns an ongoing maintenance burden into a one-time design decision.

## The problem with runtime SVG parsing

You can absolutely parse SVG at runtime, render it to a bitmap, and show it. You will pay for that decision on every screen, every scroll, and every configuration change. Designers ship a lot of icons. The app has to render a lot of icons. Parsing is a pure tax.

## What svg-to-compose does

It reads SVG or Android Vector Drawable files, walks the path commands, and emits idiomatic Jetpack Compose `ImageVector` declarations. The runtime cost is exactly one `@Composable` call per icon. There is no parser in the production binary, no surprise dependency, and no per-theme re-interpretation of the same path data.

## Why this pattern matters

When you generate code, you turn a class of bugs into a class of diffs. A broken icon shows up in code review, not in a tester's bug report. That is the real win, and it is a pattern that scales far past SVG.
