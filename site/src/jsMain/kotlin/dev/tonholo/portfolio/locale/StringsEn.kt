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
    )

    override val statusBadge: StatusBadge = StatusBadge(
        text = "not looking \u00B7 happy @ Mozilla",
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
        bottomLeftCopy = "\u00A9 2026 \u00B7 Built with Kotlin + Compose HTML \u00B7 " +
            "Designed in the terminal",
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
        lede = "Brazilian, Ottawa-based, Android-focused. Started programming at 12 on a " +
            "MU server for my cousins. Haven't stopped since.",
        tabStory = "Story",
        tabNow = "Now",
        tabHobbies = "Hobbies",
        tabBeyond = "Beyond code",
        nowUpdatedPrefix = "// updated apr 12, 2026",
        nowHeadingWorking = "Working on",
        nowHeadingLearning = "Learning",
        nowHeadingWriting = "Writing",
        nowHeadingThinking = "Thinking about",
        storyHeading2010 = "2010 - first Android build",
        storyHeading2015 = "2015 - full-time mobile",
        storyHeading2021 = "2021 - Pinterest",
        storyHeadingToday = "Today",
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
