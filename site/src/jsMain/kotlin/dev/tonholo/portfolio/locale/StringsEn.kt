package dev.tonholo.portfolio.locale

/**
 * English (Canada) translations. This is the authoritative set; every other
 * locale layers on top by overriding only the groups it actually translates.
 */
object StringsEn : Strings() {
    override val nav: Nav = Nav(
        home = "home",
        projects = "projects",
        about = "about",
        articles = "articles",
        resume = "resume",
        mobileMenuAriaLabel = "Toggle navigation menu",
    )

    override val statusBadge: StatusBadge = StatusBadge(
        statusLabel = "not looking",
        presenceLabel = "happy @ Mozilla",
    )

    override val themeToggle: ThemeToggle = ThemeToggle(
        ariaLabel = "toggle theme",
        titleToLight = "switch to light",
        titleToDark = "switch to dark",
    )

    override val footer: Footer = Footer(
        tagline = "Android engineer. Halifax, Nova Scotia, by way of Belo Horizonte. " +
            "Writing Kotlin since 2015.",
        headingName = "Rafael Tonholo",
        headingPages = "Pages",
        headingWork = "Work",
        headingElsewhere = "Elsewhere",
        linkHome = "Home",
        linkAbout = "About",
        linkArticles = "Articles",
        linkResume = "Resume",
        bottomLeftSegments = listOf(
            "\u00A9 2026",
            "Built with Kotlin + Compose HTML",
            "Designed in the terminal",
        ),
    )

    override val languageToast: LanguageToast = LanguageToast(
        primary = " - translation in progress",
        secondary = "Most content is still in English for now.",
    )

    override val hero: Hero = Hero(
        titleLine1 = "Android",
        titleLine2 = "engineer",
        titleLine3 = "since 2010.",
        subtitle = "I build apps that millions of people use every day. Lately it's Kotlin, " +
            "Jetpack Compose, and occasionally venturing into iOS via KMP. Currently " +
            "based in Halifax, Nova Scotia.",
        ctaPrimary = "see featured work",
        ctaSecondary = "download CV",
    )

    override val about: About = About(
        titlePrefix = "The long way to ",
        titleEmphasis = "Kotlin.",
        lede = "Brazilian, Halifax-based, Android-focused. Started programming at 12 on a " +
            "MU server for my cousins. Haven't stopped since.",
        portraitAlt = "Rafael Tonholo writing at his desk.",
        tabStory = "Story",
        tabNow = "Now",
        tabHobbies = "Hobbies",
        tabBeyond = "Beyond code",
        storyIntro1 = "Ever since I was little, I've been fascinated with technology - " +
            "spending hours customizing my parents' computer and trying to make it my own.",
        storyIntro2 = "My first real contact with programming was at twelve, when I decided I " +
            "wanted a private MU server for me and my cousins. I didn't know what I was " +
            "doing. The server was unstable. The cousins were thrilled.",
        storyHeading2010 = "2010 - first Android build",
        storyBody2010 = "High school technical program, first Java class, and an Android SDK " +
            "that was still very young. I built a barely functional TODO app. It " +
            "crashed on rotation. I was hooked.",
        storyHeading2015 = "2015 - full-time mobile",
        storyBody2015 = "After a few years of full-stack work - .NET in healthcare and marketing, " +
            "then cross-platform surveys - I realized I only wanted to work on what I could " +
            "hold. Android became the focus. Kotlin landed the next year and never left.",
        storyHeading2021 = "2021 - Pinterest",
        storyBody2021 = "First time working on a codebase with millions of users and nine years " +
            "of history. A 30-minute clean build became a personal vendetta. Modularizing " +
            "it - incrementally, without a freeze - taught me more about architecture than " +
            "any book ever has.",
        storyHeadingToday = "Today",
        storyBodyToday = "Thunderbird for Android at Mozilla. Kotlin-first, privacy-first, " +
            "Compose-forward, and everything shipping in the open. Writing a bit more. " +
            "Cooking a lot more.",
        nowUpdatedPrefix = "// updated apr 12, 2026",
        nowHeadingWorking = "Working on",
        nowBodyWorking = "Thunderbird for Android at Mozilla. Feature work across the flagship " +
            "app and contributing upstream to shared mobile libraries. Open-source, " +
            "privacy-first, and the community cares.",
        nowHeadingLearning = "Learning",
        nowBodyLearning = "Compose Multiplatform on desktop. It's rough in spots but the feeling " +
            "of one codebase across 4 targets is genuinely exciting again.",
        nowHeadingWriting = "Writing",
        nowBodyWriting = "Two drafts in the queue - one on Compose stability (the good, the " +
            "painful, the \"actually fine\") and one on why I stopped fearing Gradle.",
        nowHeadingThinking = "Thinking about",
        nowBodyThinking = "How to share knowledge without falling into the trap of writing " +
            "beginner content I don't enjoy writing. Also, whether to start a YouTube " +
            "channel (probably not).",
        hobbies = listOf(
            About.Hobby(
                label = "Cooking",
                body = "Mostly Italian, slow, on Sundays. My pasta game is solid, my bread game " +
                    "is a work in progress.",
            ),
            About.Hobby(
                label = "Coffee",
                body = "V60 every morning, usually Ethiopian. I own more filters than I should " +
                    "admit.",
            ),
            About.Hobby(
                label = "Running",
                body = "The Halifax harbourwalk in every season. 10K most weeks, a half-marathon " +
                    "twice a year.",
            ),
            About.Hobby(
                label = "Games",
                body = "Metroidvanias. Currently replaying Hollow Knight (again). Silksong, any " +
                    "day now.",
            ),
            About.Hobby(
                label = "Reading",
                body = "Non-fiction tech + one novel always going in parallel. Current pair: " +
                    "DDIA and Station Eleven.",
            ),
            About.Hobby(
                label = "Travel",
                body = "Short trips inside Canada. Long trips back home to Brazil every year or " +
                    "two.",
            ),
        ),
        beyondBody1 = "I moved from Brazil to Canada in 2022 - a small decision that became a " +
            "huge one. Adjusting to the cold, the snow, the French, the strange Tim " +
            "Hortons loyalty that comes with living here.",
        beyondBody2 = "My wife is a product designer (and my first reader for anything I write). " +
            "We met at a university lab in Belo Horizonte where she was building, of all " +
            "things, a physical board-game prototype with Arduino. We've been making things " +
            "together ever since.",
        beyondBody3 = "I try to give something back to the communities I learned from - mostly " +
            "through open source and occasional talks. If you're an early-career engineer " +
            "and want to chat, my inbox is open.",
    )

    override val articlesList: ArticlesList = ArticlesList(
        title = "Writing.",
        postsSuffix = "posts",
        rssCopy = "RSS available",
        subscribersCopy = "subscribed by 342 readers",
        statusDraft = "draft",
        statusPublished = "published",
    )

    override val articlesSidebar: ArticlesSidebar = ArticlesSidebar(
        contents = "Contents",
        share = "Share",
        shareCopyLink = "copy link",
        shareTwitter = "on twitter",
        shareLinkedIn = "on linkedin",
    )

    override val articleMeta: ArticleMeta = ArticleMeta(
        backLabel = "articles",
        authorPrefix = "by",
        readSuffix = "read",
    )

    override val resume: Resume = Resume(
        titleLine1 = "Sixteen years of",
        titleLine2 = "shipping software.",
        body = "Mostly Android. Increasingly Kotlin. Occasionally iOS via KMP, " +
            "whenever someone asks nicely. Always in production.",
        downloadCta = "download PDF",
    )

    override val experience: Experience = Experience(
        sectionTitle = "Experience",
        sectionTrailing = "most recent first",
    )

    override val skills: Skills = Skills(
        primaryStack = "// primary stack",
        alsoComfortable = "// also comfortable",
        education = "// education",
    )

    override val projectsList: ProjectsList = ProjectsList(
        title = "Work.",
    )

    override val projectsFilter: ProjectsFilter = ProjectsFilter(
        all = "all",
        commercial = "commercial",
        openSource = "open source",
    )

    override val projectDetail: ProjectDetail = ProjectDetail(
        commercialKind = "commercial project",
        openSourceKind = "open source",
        labelRole = "Role",
        labelTimeline = "Timeline",
        labelStack = "Stack",
        labelStatus = "Status",
        statusShipped = "shipped to production",
        contextLabel = "/ context",
        outcomesLabel = "/ outcomes",
        outcomesTitle = "Numbers worth keeping.",
        notFoundCopy = "Project not found",
        breadcrumbRoot = "work",
        breadcrumbFile = "case-study.md",
        coverPlaceholderLabel = "hero image - 16:9",
    )

    override val prevNext: PrevNext = PrevNext(
        back = "back to work",
        nextPrefix = "next: ",
    )

    override val hiringCta: HiringCta = HiringCta(
        heading = "If you're building something mobile, we should talk.",
        emailLabel = "rafael@tonholo.dev",
        bookingLabel = "book 30 min",
    )

    override val snippet: Snippet = Snippet(
        sectionTitle = "Snippet of the week",
        copyTitle = "A tiny Compose extension I keep re-writing.",
        copyBody = "Every time I need a debounced click, I reach for this. It's 9 lines, and " +
            "it saves hours of \"why did the button fire three times?\"",
    )

    override val nowStrip: NowStrip = NowStrip(
        sectionTitle = "Currently",
        labelWorkingOn = "working on",
        labelWriting = "writing",
        labelOffTheClock = "off the clock",
        statusDraft = "draft",
        statusPublished = "published",
    )

    override val uses: Uses = Uses(
        sectionTitle = "Uses",
        sectionTrailingLabel = "full list ->",
    )

    override val selectedWork: SelectedWork = SelectedWork(
        sectionTitle = "Selected work",
        projectsSuffix = "projects",
    )

    override val notFound: NotFound = NotFound(
        title = "That path does not resolve.",
        lead = "No file at the URL you landed on - a stale link, a typo, or " +
            "something the last refactor moved. Pick a door below and keep going.",
        shellCommandPrefix = "cat ",
        recoveryHome = "-> /",
        recoveryProjects = "-> /projects",
        recoveryArticles = "-> /articles",
        recoveryResume = "-> /resume",
    )
}
