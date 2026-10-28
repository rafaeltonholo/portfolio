---
layout: .components.layouts.ProjectMarkdownLayout
slug: bleacher-report
title: Bleacher Report - Centralized ads and Jetpack Compose adoption
description: "Two initiatives at Bleacher Report: a centralized Ad Manager that eliminated ad-injection crashes and moved slot config to the backend, and the introduction of Jetpack Compose to prepare the team for the next-generation app."
---

{{{ .components.organisms.projects.ProjectSection ("/ context", "Two critical needs for a sports-media platform at scale.")

Bleacher Report, a leading platform for sports and culture content, serves millions of fans with the latest news, scores, and community interactions. As the app expanded, two critical needs emerged:

**1. Enhancing Ad Integration:** The existing ad system was fragmented and unstable, leading to crashes and limiting the ad team's ability to manage ad content dynamically. There was an urgent need to create a more reliable and flexible ad integration system to boost monetization without degrading user experience.

**2. Adopting Modern UI Development:** With the upcoming development of a new app using **Jetpack Compose**, there was a emergent need to integrate Compose into the current app. This would prepare the development team for the transition and allow for the gradual adoption of modern UI practices.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ ad integration", "Centralizing a fragmented ad stack.")

They wanted to improve how advertisements were integrated within its community features. As the app expanded, it became crucial to streamline ad placement to enhance user experience and maximize monetization opportunities.

The existing ad system faced several issues. There were three different methods for injecting ads into the app, leading to inconsistencies and maintenance challenges. Ads were dynamically inserted during scrolling, which occasionally caused crashes due to concurrent modification exceptions. Additionally, any changes or additions to ad slots would require publishing a new version of the app, limiting the ad team's flexibility to respond swiftly to market demands.

As a Senior Android Engineer, I led the initiative to overhaul the ad integration process. We developed a **centralized Ad Manager** that consolidated all ad creation and injection mechanisms into a single system. This unified approach served as the **single source of truth for ad operations**, simplifying maintenance and reducing the potential for errors.

To enhance flexibility, we created an Ad Schema that allowed the Ad Manager to **request the expected ads** for each screen directly **from the backend**. This change enabled the ad team to dynamically add or modify ad slots without requiring app updates.

Additionally, we restructured the ad injection process by **pre-computing** and inserting ads into the content list before rendering it on the screen. This proactive method prevented concurrent modification exceptions, **eliminating the crashes** users were experiencing.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ compose adoption", "Championing Jetpack Compose ahead of the next-gen app.")

Anticipating the shift toward modern **UI development** in Android, Bleacher Report planned to adopt **Jetpack Compose** - Google's declarative UI toolkit - for its next-generation app. Preparing the existing development team and codebase for this transition was essential to ensure a smooth and efficient rollout.

The current app was built using the traditional **Android View system**. Introducing Jetpack Compose presented challenges, as the development team needed practical experience with the new toolkit to contribute effectively to the upcoming app. Additionally, ongoing **feature development** for the existing app had to continue without disruption.

Taking the initiative, I led the integration of **Jetpack Compose** into the current application. We established the foundational infrastructure required to support Compose, configuring project dependencies and ensuring compatibility with existing components.

To demonstrate Compose's capabilities and encourage adoption, we developed a new feature for the **community redesign** using Jetpack Compose. This feature showcased how Compose could coexist with legacy UI elements within the app.

I also facilitated **training and knowledge sharing** among team members. Mentoring other developers and providing resources helped the team build proficiency with Compose, preparing them for the upcoming development of the new app.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ outcomes", "Outcomes and impact.")

{{{ .components.organisms.projects.ProjectOutcomes ("stable", "crashes from ad injection eliminated", "dynamic", "ad slots configurable from the backend", "ready", "team prepared for the next-gen Compose app") }}}

The new centralized ad management system significantly **improved the app's stability and user experience**. Crashes caused by ad injection were eliminated, resulting in a smoother browsing experience for users.

The ad team gained the **ability to manage ad content dynamically**, adding or changing ad slots on demand **without the need for new app releases**. This flexibility allowed for **quicker responses to market opportunities** and **increased potential for revenue generation** via ads.

Integrating Jetpack Compose into the existing app acted as a catalyst for the team's adoption of the new technology. Developers gained **hands-on experience**, reducing the learning curve for future projects. The new feature enriched the app's functionality and showcased the benefits of **modern UI development** practices. When development of the new app began, the team was already familiar with Compose, allowing for a more **efficient and effective** development process.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ learnings", "Key learnings.")

This project underscored the importance of simplifying complex systems through a single source of truth. By creating a single point of control for ad management, **we reduced complexity and improved reliability** of the app. It also highlighted the value of **backend-driven configurations**, which provide **greater flexibility and agility in managing app content** without the overhead of frequent app updates.

On the Compose side, this project emphasized the value of early adoption of new technologies and **strategic planning**. Integrating Jetpack Compose into the existing app minimized risks associated with transitioning to a new UI framework. It also highlighted the importance of **leadership** in guiding teams through technological changes and fostering an environment of **continuous learning** and **innovation**.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ reflections", "Overall reflections.")

Through these projects at Bleacher Report, I was able to drive significant improvements in both the **technical infrastructure** and the **development team's capabilities**. By enhancing ad integration, we improved **app stability** and **ad monetization flexibility**. By pioneering Jetpack Compose adoption, we prepared the team for future technological shifts, ensuring that Bleacher Report remains at the forefront of **modern Android development**.

These experiences reinforced the importance of **proactive problem-solving, strategic planning**, and effective **leadership**. They also highlighted the value of **continuous learning** and **adaptability** in the ever-evolving field of **mobile app development**.

}}}
