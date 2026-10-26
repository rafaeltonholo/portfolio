---
layout: .components.layouts.ProjectMarkdownLayout
slug: thunderbird
title: Thunderbird for Android - Compose state machines, Thundermail onboarding, and privacy-first architecture
description: "Modernizing a 15-year-old open-source email client: bridging legacy Android views to Jetpack Compose state machines, shipping Thundermail desktop-to-mobile onboarding, and driving privacy and RFC standards at Mozilla."
---

{{{ .components.organisms.projects.ProjectSection ("/ context", "Bringing modern Mozilla standards to a legendary open-source client.")

Thunderbird for Android represents the next generation of open-source mobile email, built on the battle-tested foundation of K-9 Mail alongside Mozilla. With over 15 years of continuous development and millions of active users, the application presented a distinct engineering challenge: **modernizing a massive legacy codebase while preserving rock-solid email reliability**.

As the project prepared for its official Mozilla release, several critical needs emerged:

**1. Architectural Modernization:** The core message list and reader were tied to legacy Android Views and complex mutable state. The team needed a safe, incremental strategy to transition these central user flows to **Jetpack Compose** and **Unidirectional Data Flow (UDF)** without risking regressions across diverse IMAP, POP3, and Exchange providers.

**2. Seamless Ecosystem Onboarding:** Launching Mozilla's new hosted email service, **Thundermail**, required an intuitive onboarding experience and friction-free account migration from Thunderbird Desktop to mobile via QR code scanning.

**3. Privacy and Standards Leadership:** As a Mozilla project, user privacy and protocol correctness are non-negotiable. The app needed hardened keyboard privacy and formal RFCs to standardize feature flagging, notification delivery, and RFC MIME email handling.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ legacy to compose", "Bridging legacy Android Views to a Compose state machine.")

The heart of any email client is the message list and message reader. In K-9 Mail, `MessageListFragment` and `MessageContainerView` were deeply intertwined with legacy Android Views, direct database cursors, and fragmented event handling. A complete rewrite would have been high risk and stalled ongoing feature delivery.

To solve this, I designed and implemented a **bidirectional bridge connecting the legacy loading mechanisms to a modern Jetpack Compose state machine**.

We encapsulated message list actions—such as active/focused message tracking, selection modes (`selectedMessages`, Select All, Deselect All), and scroll-to-message side effects—into a reactive unidirectional state machine. This allowed Compose UI items to drive the user interface while seamlessly synchronizing with the underlying background services and database observers.

Additionally, I implemented **Unified Folder support** within this new state architecture. The state machine dynamically resolves account indicator badges and sorting criteria per folder, guaranteeing consistent behavior whether viewing a single folder or a aggregated inbox spanning multiple accounts.

In the message reader, I converted legacy Java data structures (`AttachmentViewInfo.java` to Kotlin) and introduced `AttachmentUiItem` mappers. This enabled us to integrate **Compose-rendered attachment components directly into the legacy `MessageContainerView`**, paving the way for a smooth, screen-by-screen transition to declarative UI.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ thundermail sync", "Delivering desktop-to-mobile onboarding and QR migration.")

To support Mozilla's strategic launch of **Thundermail**, I spearheaded the end-to-end mobile onboarding experience.

I authored the formal RFC for the **Thundermail Add Account interim screen**, establishing the UX flow that guides new and existing users between standard IMAP configurations and Thundermail sign-in. To maintain brand consistency, I developed custom design system components, including `ThundermailButtonPanel` and auto-resizing brand typography (`UsingBrandTypography`, `TextDisplayMediumAutoResize`) optimized for diverse Android screen sizes.

A major barrier in mobile email adoption is configuring complex server hostnames, ports, and encryption protocols. To eliminate this friction, I implemented **cross-platform QR code migration** leveraging `QrCodeScannerActivityContract` and `K9MigrationManager`. Users can export their accounts from Thunderbird Desktop and scan the QR code on Android, transferring their settings instantly.

I also hardened the OAuth authentication pipeline, addressing browser lifecycle edge cases and providing graceful error handling when external browsers are unavailable or authorization is interrupted.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ privacy & standards", "Privacy-by-design and technical RFC leadership.")

Reflecting Mozilla's core commitment to digital privacy, I implemented **Keyboard Incognito Mode** across the application.

Software keyboards often collect keystroke telemetries to train personalized predictive language models. By enforcing `EditorInfo.IME_FLAG_NO_PERSONALIZED_LEARNING` across message searches, composition fields, and credentials, the app prevents third-party keyboards from recording sensitive communication, draft contents, or passwords.

Beyond feature implementation, I authored several project-wide **Architectural RFCs**:

- **RFC #11223 (Declarative Feature Flag Architecture):** Designed a type-safe, compile-time catalog for feature flags across the multi-module project, replacing scattered flag definitions with an extensible architecture.
- **RFC #11180 (Inline Images in Plain-Text Messages):** Solved a historic email interoperability issue where inline attachments inside plain-text messages caused rendering artifacts across different mail clients, resolving the issue in strict adherence to RFC MIME standards while preserving existing `cid:` behavior.
- **Notification & Delivery Reliability Specification:** Formulated detailed architectural proposals for background sync and push delivery reliability in Doze mode.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ outcomes", "Outcomes and impact.")

{{{ .components.organisms.projects.ProjectOutcomes ("Compose", "core message list & reader bridged", "QR sync", "desktop-to-mobile onboarding", "private", "keyboard incognito mode") }}}

The Compose bridge architecture proved that a 15-year-old codebase can be modernised incrementally **without regressions or feature freezes**. The message list now benefits from reactive Compose state management while remaining compatible with legacy database loaders.

The Thundermail integration and QR code migration drastically reduced onboarding friction, giving Thunderbird users a seamless bridge between desktop and mobile.

Simultaneously, the privacy hardening and architectural RFCs reinforced Thunderbird's position as a privacy-respecting, standards-compliant email client for the open web.

}}}
