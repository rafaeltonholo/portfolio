package dev.tonholo.portfolio.locale

/**
 * Brazilian-Portuguese translations. Every group is translated; layers on top
 * of [StringsEn] only where a translation is missing. Long-form editorial copy
 * (hero, resume body, about lede, hiring CTA, 404, etc.) is authored here to
 * match the IDE-voice of the English originals.
 */
object StringsPtBr : Strings() {
    override val nav: Nav = Nav(
        home = "início",
        projects = "projetos",
        about = "sobre",
        articles = "artigos",
        resume = "currículo",
    )

    override val statusBadge: StatusBadge = StatusBadge(
        statusLabel = "não procurando",
        presenceLabel = "feliz na Mozilla",
    )

    override val themeToggle: ThemeToggle = ThemeToggle(
        ariaLabel = "alternar tema",
        titleToLight = "mudar para claro",
        titleToDark = "mudar para escuro",
    )

    override val footer: Footer = Footer(
        tagline = "Engenheiro Android. Halifax, Nova Escócia, vindo de Belo Horizonte. " +
            "Escrevendo Kotlin desde 2015.",
        headingName = "Rafael Tonholo",
        headingPages = "Páginas",
        headingWork = "Trabalho",
        headingElsewhere = "Em outros lugares",
        linkHome = "Início",
        linkAbout = "Sobre",
        linkArticles = "Artigos",
        linkResume = "Currículo",
        bottomLeftSegments = listOf(
            "\u00A9 2026",
            "Feito com Kotlin + Compose HTML",
            "Desenhado no terminal",
        ),
    )

    override val languageToast: LanguageToast = LanguageToast(
        primary = " - tradução em andamento",
        secondary = "A maior parte do conteúdo ainda está em inglês por enquanto.",
    )

    override val hero: Hero = Hero(
        titleLine1 = "Engenheiro",
        titleLine2 = "Android",
        titleLine3 = "desde 2010.",
        subtitle = "Construo apps que milhões de pessoas usam todos os dias. Hoje é Kotlin, " +
            "Jetpack Compose e, de vez em quando, uma incursão no iOS via KMP. " +
            "Vivendo em Halifax, Nova Escócia.",
        ctaPrimary = "ver trabalhos em destaque",
        ctaSecondary = "baixar CV",
    )

    override val about: About = About(
        titlePrefix = "O caminho longo até ",
        titleEmphasis = "Kotlin.",
        lede = "Brasileiro, morando em Halifax, focado em Android. Comecei a programar aos 12 " +
            "em um servidor de MU para meus primos. Nunca parei desde então.",
        tabStory = "História",
        tabNow = "Agora",
        tabHobbies = "Hobbies",
        tabBeyond = "Além do código",
        nowUpdatedPrefix = "// atualizado em 12 abr 2026",
        nowHeadingWorking = "Trabalhando em",
        nowHeadingLearning = "Aprendendo",
        nowHeadingWriting = "Escrevendo",
        nowHeadingThinking = "Pensando sobre",
        storyHeading2010 = "2010 - primeiro build Android",
        storyHeading2015 = "2015 - mobile em tempo integral",
        storyHeading2021 = "2021 - Pinterest",
        storyHeadingToday = "Hoje",
    )

    override val articlesList: ArticlesList = ArticlesList(
        title = "Escrita.",
        postsSuffix = "posts",
        rssCopy = "RSS disponível",
        subscribersCopy = "acompanhado por 342 leitores",
        statusDraft = "rascunho",
        statusPublished = "publicado",
    )

    override val articlesSidebar: ArticlesSidebar = ArticlesSidebar(
        contents = "Conteúdo",
        share = "Compartilhar",
        shareCopyLink = "copiar link",
        shareTwitter = "no twitter",
        shareLinkedIn = "no linkedin",
    )

    override val articleMeta: ArticleMeta = ArticleMeta(
        backLabel = "artigos",
        authorPrefix = "por",
        readSuffix = "de leitura",
    )

    override val resume: Resume = Resume(
        titleLine1 = "Dezesseis anos",
        titleLine2 = "entregando software.",
        body = "Principalmente Android. Cada vez mais Kotlin. Ocasionalmente iOS via KMP, " +
            "quando alguém pede gentilmente. Sempre em produção.",
        downloadCta = "baixar PDF",
    )

    override val experience: Experience = Experience(
        sectionTitle = "Experiência",
        sectionTrailing = "mais recente primeiro",
    )

    override val skills: Skills = Skills(
        primaryStack = "// stack principal",
        alsoComfortable = "// também confortável",
        education = "// educação",
    )

    override val projectsList: ProjectsList = ProjectsList(
        title = "Trabalho.",
    )

    override val projectsFilter: ProjectsFilter = ProjectsFilter(
        all = "todos",
        commercial = "comercial",
        openSource = "open source",
    )

    override val projectDetail: ProjectDetail = ProjectDetail(
        commercialKind = "projeto comercial",
        openSourceKind = "open source",
        labelRole = "Função",
        labelTimeline = "Duração",
        labelStack = "Stack",
        labelStatus = "Status",
        statusShipped = "entregue em produção",
        contextLabel = "/ contexto",
        outcomesLabel = "/ resultados",
        outcomesTitle = "Números que vale a pena guardar.",
        notFoundCopy = "Projeto não encontrado",
        breadcrumbRoot = "trabalho",
        breadcrumbFile = "case-study.md",
        coverPlaceholderLabel = "imagem de capa - 16:9",
    )

    override val prevNext: PrevNext = PrevNext(
        back = "voltar para trabalho",
        nextPrefix = "próximo: ",
    )

    override val hiringCta: HiringCta = HiringCta(
        heading = "Se você está construindo algo mobile, vamos conversar.",
        emailLabel = "rafael@tonholo.dev",
        bookingLabel = "reservar 30 min",
    )

    override val snippet: Snippet = Snippet(
        sectionTitle = "Snippet da semana",
        copyTitle = "Uma pequena extensão Compose que eu reescrevo toda hora.",
        copyBody = "Toda vez que preciso de um clique com debounce, busco este código. São 9 " +
            "linhas que economizam horas de \"por que o botão disparou três vezes?\"",
    )

    override val nowStrip: NowStrip = NowStrip(
        sectionTitle = "Atualmente",
        labelWorkingOn = "trabalhando em",
        labelWriting = "escrevendo",
        labelOffTheClock = "fora do expediente",
        statusDraft = "rascunho",
        statusPublished = "publicado",
    )

    override val uses: Uses = Uses(
        sectionTitle = "Uso",
        sectionTrailingLabel = "lista completa ->",
    )

    override val selectedWork: SelectedWork = SelectedWork(
        sectionTitle = "Trabalhos selecionados",
        projectsSuffix = "projetos",
    )

    override val notFound: NotFound = NotFound(
        title = "Esse caminho não resolve.",
        lead = "Nenhum arquivo na URL em que você caiu - um link antigo, um typo, ou algo " +
            "que o último refactor moveu. Escolha uma porta abaixo e siga em frente.",
        shellCommandPrefix = "cat ",
        recoveryHome = "-> /",
        recoveryProjects = "-> /projects",
        recoveryArticles = "-> /articles",
        recoveryResume = "-> /resume",
    )
}
