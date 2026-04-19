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
            body = "Client-facing Android engineering across multiple large-scale products. " +
                "Compose migrations, CD pipelines, and the careful stability work that keeps " +
                "apps in production.",
            bullets = listOf(
                "Owned Android delivery across two major client products",
                "Drove a CI/CD overhaul that halved release lead-time",
                "Mentored the mobile practice as it doubled in size",
            ),
        ),
        Experience(
            date = "jul 2021 - mar 2022",
            duration = "9 mo",
            role = "Senior Android Engineer",
            company = "BairesDev - Full-time",
            body = "Contract Android work across several client engagements. MVP, CI, and " +
                "a lot of rapid onboarding.",
            bullets = emptyList(),
        ),
        Experience(
            date = "jul 2021 - mar 2022",
            duration = "9 mo",
            role = "Senior Android Engineer",
            company = "Pinterest - Contract - Remote",
            body = "Platform Android. Cut build-time chaos on a 9-year-old codebase and " +
                "stood up a modular dynamic-feature system.",
            bullets = listOf(
                "-65% clean build time via modularization into 30 feature modules",
                "Led the monolith -> modular migration, incrementally, without a freeze",
                "Shipped the first Play Feature Delivery integration on the app",
            ),
        ),
        Experience(
            date = "oct 2019 - jul 2021",
            duration = "1 yr 9 mo",
            role = "Principal Software Engineer",
            company = "Grupo Financeiro Quocredi",
            body = "Led Android engineering for a fintech shipping to 2M+ monthly users. " +
                "Architected the full Compose rewrite.",
            bullets = emptyList(),
        ),
        Experience(
            date = "aug 2017 - oct 2019",
            duration = "2 yrs",
            role = "Senior Software Engineer",
            company = "Grupo Financeiro Quocredi",
            body = "Full-stack work on a growing consumer lending platform. Moved the " +
                "mobile stack from Java to Kotlin.",
            bullets = emptyList(),
        ),
        Experience(
            date = "jun 2015 - aug 2017",
            duration = "2 yrs",
            role = "Mobile Analyst Developer",
            company = "Accion",
            body = "First full-time Android role. Learned what it meant to keep an app " +
                "in production for years, not months.",
            bullets = emptyList(),
        ),
        Experience(
            date = "feb 2013 - jun 2015",
            duration = "2 yrs",
            role = ".NET Analyst Developer",
            company = "Pioneer Sistemas",
            body = "Before mobile - enterprise desktop and web apps. Where I learned to " +
                "actually read logs.",
            bullets = emptyList(),
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
                "and running the Ottawa river path in every season.",
        ),
    )
}
