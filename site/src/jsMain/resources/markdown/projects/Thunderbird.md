---
layout: .components.layouts.ProjectMarkdownLayout
slug: thunderbird
coverImage: /images/projects/thunderbird/hero.png
title: Thunderbird for Android - a Compose message list, Thundermail sign-in, and a privacy compiler plugin
description: "What I built on Thunderbird for Android: a Compose and MVI message list bridged into the legacy screen, Thundermail sign-in from design to release, a declarative feature flag platform, and pii-safe, a Kotlin K2 compiler plugin that keeps personal data out of logs."
---

{{{ .components.organisms.projects.ProjectSection ("/ context", "An old, much-loved email app that still has to keep working.")

From April 2025 to October 2026 I worked on Thunderbird for Android, the open-source email app,
as a Senior Software Engineer, Mobile. Everything happens in the open, on the public
[thunderbird-android](https://github.com/thunderbird/thunderbird-android) repository. The same
codebase ships two apps, Thunderbird and K-9 Mail, and the app has more than a million monthly
active users.

That means you can't stop the world to modernize it. Every change has to land while the app keeps
shipping to beta and release, and it has to work for both apps.

During that time, a few problems took most of my time:

- The message list, the main screen of the app, was built on legacy Android Views, with its state
  spread all over the place. That caused bugs and inconsistencies, and replacing it in one go was
  not an option.
- Thunderbird was launching Thundermail, its own email service, and the Android app needed a way to
  sign in to it.
- Personal data could end up in logs, and the only protection was each developer remembering to
  hide it.
- Feature flags lived in six separate factory classes, one per app and build type, so it was easy to
  get them wrong.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ legacy to compose", "A new message list in Compose, plugged into the old screen.")

The new message list is fully written in Jetpack Compose and uses MVI. The hard part was not the UI,
it was getting there without a rewrite and without a feature freeze.

The idea of using a ViewModel-based state machine came out of a weekly tech meeting where the team
looked at the problem together. From there, I designed the whole message list state machine as a
diagram with every state and transition, presented it to the team, and then built it. The
`MessageListStateMachine` is a direct translation of that diagram.

To keep each screen's definition short, I built a generic `StateMachine` with a Kotlin DSL. Side
effects live in their own handlers, and the state machine keeps a history of its states, with diffs
and pretty-printed logs. When something goes wrong, you can read exactly how the screen got there.

Then I connected the new list to the legacy `MessageListFragment` through a two-way bridge, so the
Compose state machine could use the existing loading code and item actions. On top of that came
selection, active message tracking, scrolling, swipe actions, sorting, and unified folder support.
It was the biggest single effort of my time there, with about 69 merged pull requests.

The new message list is built and integrated, but it is still behind a feature flag that is off by
default. The work was paused a few times when priorities changed, first for Thundermail, then for
the feature flag work.

I did something similar in the message reader. To bring back the attachment list, I wrote a new one
in Compose and placed it inside the legacy `MessageContainerView`, converting
`AttachmentViewInfo` from Java to Kotlin along the way. That way, the piece is ready for when the
reader moves to Compose.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ thundermail", "Thundermail sign-in, from design to release.")

I built the Android onboarding for Thundermail: new onboarding screens, a "Sign in with Thundermail"
flow over OAuth, and a "Scan QR code" entry point. It works on both Thunderbird and K-9 Mail, so the
shared pieces live in a common module.

I also built some of the design system pieces it needed, like `ThundermailButtonPanel` and
`TextDisplayMediumAutoResize`, and handled the cases where sign-in goes wrong. If the user cancels,
or there is no browser to open the OAuth page, the app now shows a clear error instead of getting
stuck on the redirect screen.

After launch, I wrote RFC 0002 for a Thundermail Add Account interim screen and then built that screen.

Thundermail onboarding reached beta on May 8, 2026 and release on June 3, 2026, and it is on by
default today.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ privacy & standards", "Privacy checked by the compiler, and design docs before code.")

In an email app, data classes carry email addresses, folder names, and account details. Logging one
of them by accident can leak personal data, and hiding it by hand is easy to forget.

So I designed and built **pii-safe**, a Kotlin K2 compiler plugin
([#11532](https://github.com/thunderbird/thunderbird-android/pull/11532),
[#11533](https://github.com/thunderbird/thunderbird-android/pull/11533),
[#11534](https://github.com/thunderbird/thunderbird-android/pull/11534)). It works like this:

- Developers mark a class with `@PiiSafe.HasPii`, and its sensitive properties with
  `@PiiSafe.Mask` or `@PiiSafe.Hide`. A Gradle plugin (`PiiSafeGradleSubplugin`) applies the
  compiler plugin to the module.
- FIR checkers (`HasPiiPropagationChecker`, `HasPiiRequiresAnnotatedPropertyChecker`) fail the
  build when the marking is incomplete.
- An IR generator (`ToStringOverridePiiSafeBodyGenerator`) writes a safe `toString()` for the
  marked classes, masking or hiding the sensitive fields, with no reflection at runtime.

It is merged and in use. Four modules apply the plugin, and its annotations are used in the core
message, folder, and database migration code.

I also like to write things down before writing code. While I was there, I wrote 4 RFCs, 1 technical
design, and 1 architecture decision record:

- RFC 0002, the Thundermail Add Account interim screen.
- RFC 0003, rendering inline images in plain-text messages.
- RFC 0004 and Technical Design 0002, the new feature flag architecture. I then delivered it: one
  declarative catalog validated at build time, generated type-safe keys, a Gradle plugin, and a debug
  screen. It replaced the six factory classes, and the old code is gone.
- RFC 0006, remote feature flags, which I built next. They are switched off for now.
- ADR 0010, a project-wide Repository pattern, which is still marked as Proposed.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ outcomes", "Outcomes and impact.")

{{{ .components.organisms.projects.ProjectOutcomes ("Jun 2026", "Thundermail sign-in in release", "4 modules", "using pii-safe", "32-53%", "less pull request CI time") }}}

Thundermail sign-in went from design to the release version, and Android users can sign in to
Thundermail from the app today.

pii-safe turned privacy into something the build checks, instead of something each developer has to
remember. The feature flag catalog does the same for flags: mistakes now show up at build time.

I also split the pull request CI workflow into separate jobs and turned on Gradle caching, which cut
pull request CI time from 44-62 minutes to 29-31 minutes in side-by-side runs. Every contributor
gets that on every pull request.

The new message list is not in users' hands yet, but the hard part is done. It runs on a state
machine the team can read and debug, and it plugs into the legacy screen without a feature freeze.

}}}
