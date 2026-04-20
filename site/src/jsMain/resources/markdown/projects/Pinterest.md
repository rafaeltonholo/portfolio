---
layout: .components.layouts.ProjectMarkdownLayout
slug: pinterest
title: Pinterest - Modularizing Pinterest's codebase
description: Adopting an incremental approach and a centralized dynamic-feature dependency manager, we modularized a 9-year-old codebase with millions of users without a freeze.
---

{{{ .components.organisms.projects.ProjectSection ("/ context", "Pinterest is 9 years of code, and it shows.")

Pinterest is a leading visual discovery engine, inspiring millions with ideas ranging from recipes to home and style inspiration.

As the application grew over nine years, it faced significant scalability challenges, particularly with build times reaching up to **30 minutes for a clean build and 15 minutes for incremental builds** on high-end machines.

Additionally, the need to implement on-demand installable features required leveraging the **Google Play Dynamic Feature Delivery** system.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ problem", "The challenges of a large and aging codebase.")

The primary issue we faced was the extensive build times. On high-end machines equipped with i9 processors and 64GB of memory, a clean build of the app could take up to 30 minutes, and even incremental builds were around 15 minutes. These prolonged build times impacted developer productivity and slowed down the release cycle. Additionally, there was a need to implement features that could be installed on demand, necessitating the integration of Google's Play Feature Delivery system, which added another layer of complexity to an already cumbersome codebase.

Modularizing such a large and aging codebase without disrupting ongoing feature development was a significant challenge. The app's tightly coupled components made decoupling and refactoring difficult. We also had to ensure that the transition didn't introduce new errors or affect the stability of the app. Moreover, integrating dynamic feature modules led to application crashes when required features weren't available at runtime, mainly due to the use of reflection in dependency management.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ actions", "Actions and implementation.")

We began by fostering open communication with all stakeholders, especially the product engineers, to ensure everyone was aligned and aware of the changes. This collaboration was crucial to minimize disruptions to ongoing development work.

Adopting an **incremental approach**, we started by identifying and extracting classes and resources with the fewest dependencies into separate modules. This strategy allowed us to modularize parts of the app without causing significant ripple effects throughout the codebase. As we progressed, we tackled more complex components, carefully refactoring code to decouple tightly coupled modules.

To address the application crashes caused by dynamic feature modules, we developed a **centralized dynamic-feature dependency manager**. This manager allowed features requiring dynamic modules to request them without using reflection. By leveraging Kotlin's type safety through interfaces, we eliminated many runtime errors related to not-yet-ready dynamic features or typos. This solution not only improved the application's reliability but also simplified the integration process for developers working on features that require other dynamic features.

We also focused on optimizing dependencies by identifying and removing unnecessary ones and cleaning up dead code. This effort reduced module interdependencies and prevented unnecessary builds of modules that certain features shouldn't rely on.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ outcomes", "Outcomes and impact.")

{{{ .components.organisms.projects.ProjectOutcomes ("30%", "reduction in build times", "manager", "centralized dynamic-feature dependency manager", "scalable", "improved scalability and maintainability") }}}

The modularization efforts led to a **30% reduction in build times**, significantly enhancing developer efficiency and productivity. Developers could now work more independently within their respective modules, reducing the overhead of dealing with the entire application during development.

The centralized dynamic-feature dependency manager improved the app's stability by ensuring that dynamic features were correctly loaded at runtime. This enhancement reduced crashes and provided a smoother user experience when features were installed on demand.

The new modular architecture also **improved the app's scalability and maintainability**. It helped the Pinterest app for future development, making it easier to integrate additional features and adapt to new requirements.

}}}

{{{ .components.organisms.projects.ProjectSection ("/ learnings", "Key learnings.")

This project demonstrated the importance of **strategic planning and effective communication** in large-scale development initiatives. By keeping all team members informed and involved, we were able to navigate the complexities of modularizing a live and evolving codebase.

It also highlighted the value of an **incremental approach** to tackling complex problems. Breaking down the modularization process into manageable chunks made the task less overwhelming and allowed for continuous progress without significant disruptions.

From a technical perspective, the experience deepened my expertise in **Kotlin and Java, modular architectures, dynamic feature modules, and dependency injection with Dagger**. It reinforced the necessity of innovative problem-solving and the willingness to rethink established methods when they no longer serve the project's needs.

}}}
