---
title: Why I stopped fearing Gradle
date: mar 28, 2026
description: A short letter to everyone who groans when they see build.gradle.kts. The tooling got good. The docs did not.
tags:
  - gradle
  - build
readTime: 7 min
---

Gradle has spent the last decade picking up a reputation that was fair in 2015, dated by 2020, and lazy by 2024. Most of the friction my coworkers report is older than the toolchain they're actually using.

## The tooling is further ahead than you think

Version catalogs, typesafe project accessors, configuration caching, and the Kotlin DSL have quietly turned Gradle from a scripting hazard into something that looks a lot like a real programming environment. The error messages are clearer, the build scans are routine, and `./gradlew build --configuration-cache` finishes in noticeably less time than it did a year ago.

The one thing that hasn't caught up is the docs. The official reference still assumes you read the previous page, the blog posts are pinned to whatever major version was current when they were written, and the Stack Overflow answers are almost uniformly out of date. The only reliable way to learn modern Gradle is to read other people's modern `build.gradle.kts`.

## What actually changed my mind

I stopped treating Gradle as a pile of plugins held together by superstition. A convention plugin, applied from `buildSrc`, is a Kotlin file. It runs. You can debug it. You can extract helpers. The build logic becomes something you can maintain instead of something you patch around.

That shift is less about Gradle and more about accepting it as code. Once you're there, the tooling's pace of improvement stops feeling hostile; it starts feeling welcome.
