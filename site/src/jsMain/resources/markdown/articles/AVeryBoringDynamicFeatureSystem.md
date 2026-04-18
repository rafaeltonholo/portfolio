---
title: A very boring dynamic-feature system
date: feb 24, 2026
description: Boring is good. Boring is stable. Boring is what you want at 2am.
tags:
  - architecture
  - android
readTime: 11 min
---

The best compliment a dynamic-feature system can earn is the one nobody ever writes down: it didn't break. At two in the morning, boring is the highest virtue a build system can have.

## Why boring

Flashy module graphs, clever plugin DSLs, and aggressive Gradle features make great conference talks. They make terrible on-call incidents. When a feature module fails to resolve at midnight because someone renamed a transitive dependency, you want the system to be so plain that the problem is obvious, not so clever that you have to remember who invented the convention.

## What boring looks like

A shallow module graph, ideally no more than two levels deep outside the core layer. Feature modules that only know about the handful of APIs they depend on. A single plugin that applies the same Kotlin, Android, and lint configuration to every feature module, with zero optional behaviour. Dependencies declared through the version catalog, never through ad-hoc strings.

## What you lose

A little elegance, yes. A little code golf. You give up some of the fun of seeing a clever build script compress a hundred lines of configuration into ten. What you get back is a system that a coworker you haven't met yet can maintain for the next five years. That trade is almost always the right one.
