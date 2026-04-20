package dev.tonholo.portfolio.state

import dev.tonholo.portfolio.domain.model.Article
import dev.tonholo.portfolio.domain.model.Education
import dev.tonholo.portfolio.domain.model.Experience
import dev.tonholo.portfolio.domain.model.NowStripEntry
import dev.tonholo.portfolio.domain.model.Project
import dev.tonholo.portfolio.domain.model.ProjectKind
import dev.tonholo.portfolio.domain.model.ProjectStat
import dev.tonholo.portfolio.domain.model.SkillBucket
import dev.tonholo.portfolio.domain.model.SkillCategory
import dev.tonholo.portfolio.domain.model.TickerItem
import dev.tonholo.portfolio.domain.model.UseCategory
import dev.tonholo.portfolio.generated.articles.GeneratedArticles

/**
 * Seed static content copied verbatim from the handoff JSX prototype. Drives
 * the "Selected work" grid, the /articles list, the resume timeline + skills,
 * the /uses grid, the ticker bar, and the home "Currently" strip.
 *
 * Em dashes in the source JSX have been normalized to regular dashes to
 * satisfy the project's no-em-dash rule.
 */
object StaticContent {
    val PROJECTS: List<Project> = listOf(
        Project(
            slug = "thunderbird",
            index = "01",
            name = "Thunderbird for Android",
            kind = ProjectKind.OpenSource,
            role = "Senior Software Engineer, Mobile - Mozilla",
            year = "2025 - present",
            description = "Mozilla's open-source email client coming to Android. " +
                "Compose-forward, privacy-first, built in the open with the K-9 Mail community.",
            stack = listOf("Kotlin", "Jetpack Compose", "K-9 Mail", "OSS"),
            stats = listOf(
                ProjectStat(value = "OSS", label = "fully open source"),
                ProjectStat(value = "Mozilla", label = "affiliated"),
                ProjectStat(value = "Compose", label = "modern UI"),
            ),
            externalCta = "View on GitHub",
        ),
        Project(
            slug = "bleacher-report",
            index = "02",
            name = "Bleacher Report",
            kind = ProjectKind.Commercial,
            role = "Senior Android Engineer - REDspace",
            year = "2022 - 2025",
            description = "Three years on the Android app of a top sports-media platform. " +
                "Compose migrations, CI/CD overhauls, and the careful stability work that " +
                "keeps apps in production at scale.",
            stack = listOf("Kotlin", "Compose", "CI/CD"),
            stats = listOf(
                ProjectStat(value = "-50%", label = "release lead-time"),
                ProjectStat(value = "10M+", label = "monthly users"),
                ProjectStat(value = "3 yrs", label = "on the app"),
            ),
            externalCta = "Case study",
        ),
        Project(
            slug = "pinterest",
            index = "03",
            name = "Pinterest",
            kind = ProjectKind.Commercial,
            role = "Senior Android Engineer (Contract)",
            year = "2021 - 2022",
            description = "Led modularization and dynamic-feature delivery on a 9-year-old " +
                "codebase. Cut clean-build time by 65% and shipped the first Play Feature " +
                "Delivery integration on the app.",
            stack = listOf("Kotlin", "Play Feature Delivery", "Dagger"),
            stats = listOf(
                ProjectStat(value = "-65%", label = "clean build time"),
                ProjectStat(value = "30", label = "feature modules"),
                ProjectStat(value = "9 yrs", label = "of legacy code"),
            ),
            externalCta = "Case study",
        ),
        Project(
            slug = "svg-compose",
            index = "04",
            name = "SVG to Compose",
            kind = ProjectKind.OpenSource,
            role = "Creator",
            year = "2023 - ongoing",
            description = "Command-line tool that converts SVG and Android Vector Drawables " +
                "into idiomatic Jetpack Compose Icons. Drops design handoff friction to near zero.",
            stack = listOf("Kotlin", "CLI", "Compose"),
            stats = listOf(
                ProjectStat(value = "1.2k", label = "GitHub stars"),
                ProjectStat(value = "24", label = "contributors"),
                ProjectStat(value = "v2.1", label = "current release"),
            ),
            externalCta = "View on GitHub",
        ),
        Project(
            slug = "ivy-wallet",
            index = "05",
            name = "ivy-wallet",
            kind = ProjectKind.OpenSource,
            role = "Contributor",
            year = "2023",
            description = "Contributed to a 100% Kotlin + Compose personal finance app used " +
                "by 100k+ people. Focus areas: architecture refactors and stability.",
            stack = listOf("Kotlin", "Compose", "Room"),
            stats = listOf(
                ProjectStat(value = "100k+", label = "installs"),
                ProjectStat(value = "4.6", label = "Play Store star"),
                ProjectStat(value = "OSS", label = "fully open source"),
            ),
            externalCta = "View in Play Store",
        ),
    )

    /**
     * The article index is generated at build time from the markdown sources
     * under `site/src/jsMain/resources/markdown/articles/`. See
     * [GeneratedArticles] (produced by the kobwebx-markdown `process` block
     * in `site/build.gradle.kts`) for the canonical list.
     */
    val ARTICLES: List<Article> = GeneratedArticles.ALL

    val EXPERIENCE: List<Experience> = listOf(
        Experience(
            date = "apr 2025 - present",
            duration = "1 yr 1 mo",
            role = "Senior Software Engineer, Mobile",
            company = "Mozilla / Thunderbird for Android - Remote - Canada",
            body = "Helping bring Thunderbird to Android alongside broader Mozilla " +
                "mobile work. Kotlin-first, privacy-first, Compose-forward, " +
                "everything-in-the-open.",
            bullets = listOf(
                "Feature work across the flagship Android app",
                "Android, Jetpack Compose, and a long list of infrastructure bits",
                "Contributing upstream to shared mobile libraries",
                "Shipping to a community that genuinely cares",
            ),
        ),
        Experience(
            date = "mar 2022 - mar 2025",
            duration = "3 yrs 1 mo",
            role = "Senior Android Engineer",
            company = "REDspace Inc. - Halifax, NS - Hybrid",
            body = "Designing and developing robust, maintainable, standards-compliant " +
                "Android apps and libraries across multiple large-scale client products.",
            bullets = listOf(
                "Architected high-performance, reusable Kotlin/Java code",
                "Used profiling to fix memory and performance bottlenecks",
                "Delivered high-quality, feature-specific software on schedule",
                "Drove Compose migrations and CI/CD improvements",
            ),
        ),
        Experience(
            date = "jul 2021 - mar 2022",
            duration = "9 mo",
            role = "Senior Android Engineer",
            company = "Pinterest - Contract - Remote",
            body = "Designed solutions, optimized and maintained frameworks that empower " +
                "the engineering team to deliver the best possible experience at scale.",
            bullets = listOf(
                "Improved build speed via dynamic-feature modularization",
                "Built a centralized dynamic-feature dependency manager",
                "Started XML-to-Jetpack Compose migration on the app",
                "Partnered with product engineering to ship enablers for new features",
            ),
        ),
        Experience(
            date = "oct 2019 - jul 2021",
            duration = "1 yr 10 mo",
            role = "Principal Software Engineer",
            company = "Questrade Financial Group - Toronto, ON",
            body = "Technical lead for a new cross-platform app built with React Native. " +
                "Designed high-availability event-based services behind it.",
            bullets = listOf(
                "Built a React Native module for biometric auth + OIDC + PKCE",
                "Designed event-based microservices on Kafka and Google Pub/Sub",
                "Stood up a contract-driven mock service so mobile could ship in parallel",
                "Coached the new team as it ramped on React Native",
            ),
        ),
        Experience(
            date = "aug 2017 - oct 2019",
            duration = "2 yrs 2 mo",
            role = "Senior Software Engineer",
            company = "Questrade Financial Group - Toronto, ON",
            body = "Owned features, performance, and a full auth rewrite on Questrade's " +
                "hybrid mobile app - shared across iOS, Android, desktop, and web.",
            bullets = listOf(
                "Took the hybrid app from 1.5 to 3.3 stars at 100k+ daily users",
                "Implemented native biometric auth (Fingerprint, TouchID, FaceID)",
                "Shipped end-to-end auth: mobile, front-end, back-end, metrics, delivery",
                "Encrypted all credentials at rest with native-code RSA",
            ),
        ),
        Experience(
            date = "jun 2015 - aug 2017",
            duration = "2 yrs 2 mo",
            role = "Mobile Analyst Developer",
            company = "Axxiom - Belo Horizonte, BR",
            body = "Built a native Android app from scratch for CEMIG, a Brazilian energy " +
                "utility. Field crews used it to locate assets that needed repair.",
            bullets = listOf(
                "Material Design-era native Android, ground up",
                "Integrated Google Maps for asset inspection workflows",
                "AIDL-based inter-process communication across distributed apps",
                "Notification center routing over Wi-Fi, mobile, or satellite links",
            ),
        ),
        Experience(
            date = "feb 2013 - jun 2015",
            duration = "2 yrs 4 mo",
            role = "Analyst Developer, .NET",
            company = "Planear Sistemas - Belo Horizonte, BR",
            body = "Full-stack .NET across healthcare and marketing. Mobile app for " +
                "medical scheduling, treatment, and finance. Dynamic survey app for marketing.",
            bullets = listOf(
                "Shipped mobile + web for private clinics and public polyclinics",
                "Built a dynamic-form engine driven by a RESTful contract",
                "Launched a gamified social network for political campaigns",
            ),
        ),
    )

    val EDUCATION: List<Education> = listOf(
        Education(
            degree = "Post-graduate - Mobile Application Development",
            institution = "University Center UNA",
            year = "2015",
        ),
        Education(
            degree = "Technologist - Digital Games",
            institution = "PUC-MG",
            year = "2012",
        ),
        Education(
            degree = "Computer Technician",
            institution = "COTEMIG",
            year = "2010",
        ),
    )

    val PRIMARY_STACK: SkillBucket = SkillBucket(
        category = SkillCategory.PrimaryStack,
        skills = listOf(
            "Kotlin",
            "Jetpack Compose",
            "Coroutines",
            "Flow",
            "Hilt",
            "Dagger",
            "Room",
            "KMP",
            "Gradle",
            "Play Feature Delivery",
        ),
    )

    val ALSO_COMFORTABLE: SkillBucket = SkillBucket(
        category = SkillCategory.AlsoComfortable,
        skills = listOf(
            "Java",
            "Compose HTML",
            "Kobweb",
            "REST",
            "GraphQL",
            "Firebase",
            "CI - GitHub Actions",
            "Kotest",
        ),
    )

    val USES: List<UseCategory> = listOf(
        UseCategory(
            name = "editor",
            items = listOf(
                "Android Studio Koala",
                "IntelliJ IDEA Ultimate",
                "JetBrains Mono - 14pt",
            ),
        ),
        UseCategory(
            name = "hardware",
            items = listOf(
                "MacBook Pro 16\" M3 Max",
                "Pixel 8 Pro + Pixel 5",
                "LG UltraFine 5K",
            ),
        ),
        UseCategory(
            name = "desk",
            items = listOf(
                "HHKB Pro Hybrid",
                "MX Master 3S",
                "Freestanding Ikea top",
            ),
        ),
        UseCategory(
            name = "cli",
            items = listOf(
                "zsh + starship",
                "fzf, bat, eza",
                "lazygit",
            ),
        ),
    )

    val TICKER: List<TickerItem> = listOf(
        TickerItem(label = "now", value = "dynamic-feature system"),
        TickerItem(label = "reading", value = "Designing Data-Intensive Applications"),
        TickerItem(label = "learning", value = "Compose Multiplatform"),
        TickerItem(label = "playing", value = "Hollow Knight"),
    )

    val NOW_STRIP: List<NowStripEntry> = listOf(
        NowStripEntry(
            label = "working on",
            title = "A dynamic-feature system that doesn't fight the dev loop.",
            body = "Module graphs that scale past 400 modules, without making local " +
                "compile-times tragic. Turns out the fix was in Gradle, mostly.",
        ),
        NowStripEntry(
            label = "writing",
            title = "Compose stability, without the cult",
            body = "Two drafts in the queue - one on Compose stability (the good, the " +
                "painful, the \"actually fine\") and one on why I stopped fearing Gradle.",
        ),
        NowStripEntry(
            label = "off the clock",
            title = "Ragu, most Sundays.",
            body = "Cooking slow Italian on Sundays, loose-leaf sencha in the mornings, " +
                "and running the Halifax harbourwalk in every season.",
        ),
    )
}
