package dev.tonholo.portfolio.locale

/**
 * Brazilian-Portuguese translations. Every group is translated; layers on top
 * of [StringsEn] only where a translation is missing. Long-form editorial copy
 * (hero, resume body, about lede, hiring CTA, 404, etc.) is authored here to
 * match the IDE-voice of the English originals.
 */
object StringsPtBr : Strings() {
    override val a11y: A11y = A11y(
        opensInNewTab = "(abre em uma nova aba)",
    )

    override val nav: Nav = Nav(
        home = "início",
        projects = "projetos",
        about = "sobre",
        articles = "artigos",
        resume = "currículo",
        mobileMenuAriaLabel = "Abrir menu de navegação",
    )

    override val statusBadge: StatusBadge = StatusBadge(
        statusLabel = "disponível",
        presenceLabel = "remoto / Halifax",
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
        portraitAlt = "Rafael Tonholo escrevendo em sua mesa.",
        tabStory = "História",
        tabNow = "Agora",
        tabHobbies = "Hobbies",
        tabBeyond = "Além do código",
        storyIntro1 = "Desde pequeno sempre fui apaixonado por tecnologia - passei horas " +
            "personalizando o computador dos meus pais tentando deixá-lo do meu jeito.",
        storyIntro2 = "Meu primeiro contato real com programação foi aos 12 anos, quando resolvi " +
            "criar um servidor privado de MU para mim e meus primos. Eu não fazia ideia do " +
            "que estava fazendo. O servidor era instável. Os primos amavam.",
        storyHeading2010 = "2010 - primeiro build Android",
        storyBody2010 = "Curso técnico no ensino médio, primeira aula de Java, e um Android SDK " +
            "que ainda era muito novo. Construí um app de TODO mal-funcionando. Ele crashava " +
            "na rotação da tela. Estava viciado.",
        storyHeading2015 = "2015 - mobile em tempo integral",
        storyBody2015 = "Depois de alguns anos trabalhando com full-stack - .NET em saúde e " +
            "marketing, depois surveys cross-platform - percebi que só queria trabalhar com " +
            "o que eu pudesse segurar na mão. Android virou o foco. Kotlin chegou no ano " +
            "seguinte e nunca mais saiu.",
        storyHeading2021 = "2021 - Pinterest",
        storyBody2021 = "Primeira vez trabalhando numa codebase com milhões de usuários e nove " +
            "anos de história. Um clean build de 30 minutos virou vingança pessoal. " +
            "Modularizar o projeto - de forma incremental, sem freeze - me ensinou mais sobre " +
            "arquitetura do que qualquer livro.",
        storyHeadingToday = "Hoje",
        storyBodyToday = "Mais recentemente construindo o Thunderbird para Android na Mozilla - " +
            "Kotlin em primeiro lugar, privacidade em primeiro lugar, Compose à frente e tudo em open source. " +
            "Agora aberto a novas oportunidades em engenharia mobile sênior e arquitetura.",
        nowUpdatedPrefix = "// atualizado em out 2026",
        nowHeadingWorking = "Buscando",
        nowBodyWorking = "Oportunidades como Engenheiro Android / Mobile Sênior - Kotlin, Jetpack Compose, " +
            "KMP e arquitetura mobile escalável. Aberto a posições remotas ou em Halifax.",
        nowHeadingLearning = "Aprendendo",
        nowBodyLearning = "Compose Multiplatform no desktop. Ainda tem arestas em alguns pontos, " +
            "mas a sensação de uma única codebase rodando em 4 targets voltou a ser " +
            "empolgante de verdade.",
        nowHeadingWriting = "Escrevendo",
        nowBodyWriting = "Dois rascunhos na fila - um sobre estabilidade no Compose (o bom, o " +
            "doloroso e o \"na verdade, tá tranquilo\") e outro sobre por que parei de ter " +
            "medo do Gradle.",
        nowHeadingThinking = "Pensando sobre",
        nowBodyThinking = "Como compartilhar conhecimento sem cair na armadilha de escrever " +
            "conteúdo iniciante que eu não gosto de escrever. E também se vale a pena " +
            "começar um canal no YouTube (provavelmente não).",
        hobbies = listOf(
            About.Hobby(
                label = "Cozinha",
                body = "Mais comida italiana, devagar, aos domingos. Minha massa tá sólida, o " +
                    "pão ainda é trabalho em andamento.",
            ),
            About.Hobby(
                label = "Café",
                body = "V60 toda manhã, geralmente um etíope. Tenho mais filtros do que gostaria " +
                    "de admitir.",
            ),
            About.Hobby(
                label = "Corrida",
                body = "O harbourwalk de Halifax em todas as estações. 10K na maioria das " +
                    "semanas, uma meia-maratona duas vezes por ano.",
            ),
            About.Hobby(
                label = "Games",
                body = "Metroidvanias. No momento rejogando Hollow Knight (de novo). Silksong a " +
                    "qualquer momento.",
            ),
            About.Hobby(
                label = "Leitura",
                body = "Não-ficção técnica + um romance sempre em paralelo. Dupla atual: DDIA e " +
                    "Station Eleven.",
            ),
            About.Hobby(
                label = "Viagens",
                body = "Viagens curtas dentro do Canadá. Viagens longas de volta pra casa, no " +
                    "Brasil, a cada um ou dois anos.",
            ),
        ),
        beyondBody1 = "Me mudei do Brasil pro Canadá em 2022 - uma decisão pequena que virou " +
            "enorme. Me adaptando ao frio, à neve, ao francês, e à estranha fidelidade ao " +
            "Tim Hortons que vem junto com morar aqui.",
        beyondBody2 = "Minha esposa é product designer (e a primeira leitora de qualquer coisa " +
            "que eu escrevo). A gente se conheceu num laboratório de universidade em Belo " +
            "Horizonte onde ela estava construindo, pasmem, um protótipo físico de jogo de " +
            "tabuleiro com Arduino. Desde então a gente faz coisas juntos.",
        beyondBody3 = "Tento devolver um pouco pras comunidades com quem aprendi - principalmente " +
            "via open source e palestras ocasionais. Se você está no começo da carreira em " +
            "engenharia e quer conversar, minha caixa de entrada está aberta.",
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
        shareCopied = "link copiado!",
        shareTwitter = "no twitter",
        shareLinkedIn = "no linkedin",
        shareBluesky = "no bluesky",
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
