package dev.tonholo.portfolio.resources.workExperience

import dev.tonholo.portfolio.resources.Description
import dev.tonholo.portfolio.resources.HomePage
import kotlinx.datetime.LocalDate

internal val REDSpace = HomePage.HistorySection.Experience(
    name = "REDspace",
    description = Description(
        "Designing and developing robust, maintainable and standards-compliant mobile applications and libraries for the Android Platform.",
        "Designing, architecting, and maintaining high-performance, reusable, and reliable Kotlin/Java code.",
        "Using profiling techniques and software to identify and correct memory management problems and performance bottlenecks.",
        "Delivering high-quality, robust, feature-specific software on schedule.",
    ),
    title = "Android Engineer",
    starting = LocalDate.parse("2022-03-28"),
    technologiesUsed = listOf(
        "Android",
        "Kotlin",
        "Hilt",
        "Jetpack Compose",
        "git",
        "Java",
    )
)

internal val BairesDev = HomePage.HistorySection.Experience(
    name = "BairesDev / Pinterest",
    description = Description(
        "Working designing solutions, optimizing and maintaining frameworks that empower the engineering team to deliver the best possible experience for people who use Pinterest.",
        "Working with dynamic feature module, improving build speed.",
        "Working closely with product engineering team creating enablers to new products development.",
        "Maintaining unit and integration tests ensuring code quality.",
        "Started migration of XML views to Jetpack Compose.",
    ),
    title = "Android Engineer",
    starting = LocalDate.parse("2021-07-23"),
    ending = LocalDate.parse("2022-03-18"),
    technologiesUsed = listOf(
        "Android",
        "Kotlin",
        "Play Feature Delivery",
        "Dagger2",
        "Hilt",
        "Jetpack Compose",
        "Java",
        "Phabricator",
        "git",
        "Jira",
    )
)

internal val QuestradePrincipal = HomePage.HistorySection.Experience(
    name = "Questrade Financial Group",
    description = Description(
        "Technical leadership, coaching the new team working on the new apps for Questrade, using React Native. Designed solutions event-based with high availability to support the new apps and the huge customer demand.",
        "Implemented React Native module to support biometric authentication and OIDC Auth code + PCKE flow.",
        "Designed and developed event-based microservices using Apache Kafka and Google Pub-Sub.",
        "Designed and developed a mock service with payload internal validation, based on the open API contract, in order to enable mobile development during microservice creation.",
    ),
    title = "Principal Software Engineer",
    starting = LocalDate.parse("2019-10-01"),
    ending = LocalDate.parse("2021-07-23"),
    technologiesUsed = listOf(
        "Android",
        "Kotlin",
        "iOS",
        "Swift",
        "Bitrise",
        "C#",
        ".NET Core",
        "GCP",
        "TypeScript",
        "NodeJS",
        "git",
        "Jenkins",
        "Datadog",
        "Jira",
        "GitLab",
    )
)

internal val QuestradeSenior = HomePage.HistorySection.Experience(
    name = "Questrade Financial Group",
    description = Description(
        "Implemented new features and improved the performance and quality of the Questrade mobile hybrid app. Implemented a new authentication method using biometric authentications (Fingerprint for Android, TouchID and FaceID for iOS). Development of a whole new authentication process for mobile, desktop and web, based on Identity Server.",
        "Increased customer satisfaction in the hybrid mobile app from 1.5 to 3.3 stars for an application with 100.000+ daily usage.",
        "Created a secure authentication method for the hybrid app using native code (Kotlin for Android and Swift for iOS), with all data encrypted using the RSA method.",
        "Executed new authentication project from concept through to completion, including mobile integration, front and back-end implementation, logging, metrics and delivery.",
    ),
    title = "Senior Software Engineer",
    starting = LocalDate.parse("2017-08-28"),
    ending = LocalDate.parse("2019-10-01"),
    technologiesUsed = listOf(
        "Android",
        "Kotlin",
        "Java",
        "iOS",
        "Swift",
        "Objective-C",
        "Cordova",
        "C#",
        ".NET Core",
        "git",
        "SVN",
        "Jenkins",
        "Jira",
        "gitLab",
    )
)

internal val Axxiom = HomePage.HistorySection.Experience(
    name = "Axxiom",
    description = Description(
        "Designed and implemented a new app from scratch for CEMIG, the energy company from Minas Gerais, using Android native. Integration with Google Maps showing objects that need repairs or installation.",
        "Designed an application using the Material Design pattern.",
        "Distributed application that communicates with others using AIDL.",
        "Designed and developed a notification center that provides messages between the base and the apps using the best available connection (Wi-Fi, Mobile Network or Satellite connection)."
    ),
    title = "Mobile Analyst Developer",
    starting = LocalDate.parse("2015-06-08"),
    ending = LocalDate.parse("2017-08-18"),
    technologiesUsed = listOf(
        "Android",
        "Java",
        "SQLite",
        "Symmetric DS",
        "Google Maps API",
        "C#",
        "git",
        "Jira",
        "Stash",
        "Bamboo",
        "Informix",
    )
)

internal val Planear = HomePage.HistorySection.Experience(
    name = "Planear Sistemas",
    description = Description(
        "Designed solutions to Health care management and marketing area. Developed a mobile application to manage medical scheduling, treatment prescriptions, and financial receipts. Developed an application to the marketing area creating dynamic surveys in the mobile app.",
        "Implemented both mobile and web solutions to manage health clinics and public polyclinics managing themselves.",
        "Created a dynamic application that receives data from RESTful service and builds a dynamic form based on that.",
        "Created a social network to help politics and his supporters in his campaign using gamification.",
    ),
    title = "Analyst Developer .NET",
    starting = LocalDate.parse("2013-02-01"),
    ending = LocalDate.parse("2015-06-08"),
    technologiesUsed = listOf(
        "C#",
        "Android",
        "Java",
        "RESTful",
        "SVN",
        "SQL Server",
        "Unity 3D",
        "JavaScript",
        "ASP.NET WebForms",
        "ASP.NET MVC 5",
    )
)
