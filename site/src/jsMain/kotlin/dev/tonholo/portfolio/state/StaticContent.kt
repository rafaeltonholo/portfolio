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
import dev.tonholo.portfolio.locale.Locale

/**
 * Seed static content keyed by [Locale]. English is the source of truth;
 * [Locale.PtBr] provides parallel copy for every content surface that the site
 * renders directly (projects, experience, education, uses, ticker, now strip).
 * Skills, articles, and the projects list slug-index are locale-neutral: skill
 * names are proper nouns, articles are authored in English only for v1, and
 * slugs are shared across locales.
 *
 * Callers should read content through the locale-aware accessors
 * ([projectsFor], [experienceFor], ...) so UI text flips with the active
 * [dev.tonholo.portfolio.state.LocaleState].
 */
object StaticContent {
    /**
     * English (source-of-truth) project list. Consumers should prefer
     * [projectsFor] so the list flips with the active locale.
     */
    val PROJECTS: List<Project> = PROJECTS_EN

    /**
     * The article index is generated at build time from the markdown sources
     * under `site/src/jsMain/resources/markdown/articles/`. Articles are
     * authored in English only for v1; pt-BR readers still see English copy.
     */
    val ARTICLES: List<Article> = GeneratedArticles.ALL

    /** English (source-of-truth) experience list. Prefer [experienceFor]. */
    val EXPERIENCE: List<Experience> = EXPERIENCE_EN

    /** English (source-of-truth) education list. Prefer [educationFor]. */
    val EDUCATION: List<Education> = EDUCATION_EN

    /**
     * Primary-stack skill bucket. Skill names are proper nouns and stay the
     * same across locales; the localized heading above the column comes from
     * [dev.tonholo.portfolio.locale.Strings.Skills].
     */
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

    /** Secondary-stack skill bucket. Same locale-neutral rationale as [PRIMARY_STACK]. */
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

    /** English (source-of-truth) uses list. Prefer [usesFor]. */
    val USES: List<UseCategory> = USES_EN

    /** English (source-of-truth) ticker items. Prefer [tickerFor]. */
    val TICKER: List<TickerItem> = TICKER_EN

    /** English (source-of-truth) now-strip entries. Prefer [nowStripFor]. */
    val NOW_STRIP: List<NowStripEntry> = NOW_STRIP_EN

    fun projectsFor(locale: Locale): List<Project> = when (locale) {
        Locale.EnCa -> PROJECTS_EN
        Locale.PtBr -> PROJECTS_PT_BR
    }

    fun experienceFor(locale: Locale): List<Experience> = when (locale) {
        Locale.EnCa -> EXPERIENCE_EN
        Locale.PtBr -> EXPERIENCE_PT_BR
    }

    fun educationFor(locale: Locale): List<Education> = when (locale) {
        Locale.EnCa -> EDUCATION_EN
        Locale.PtBr -> EDUCATION_PT_BR
    }

    fun usesFor(locale: Locale): List<UseCategory> = when (locale) {
        Locale.EnCa -> USES_EN
        Locale.PtBr -> USES_PT_BR
    }

    fun tickerFor(locale: Locale): List<TickerItem> = when (locale) {
        Locale.EnCa -> TICKER_EN
        Locale.PtBr -> TICKER_PT_BR
    }

    fun nowStripFor(locale: Locale): List<NowStripEntry> = when (locale) {
        Locale.EnCa -> NOW_STRIP_EN
        Locale.PtBr -> NOW_STRIP_PT_BR
    }
}

// ---------------------------------------------------------------------------
// Projects
// ---------------------------------------------------------------------------

private val PROJECTS_EN: List<Project> = listOf(
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
        externalCta = "Case study",
        coverImage = "/images/projects/thunderbird/hero.png",
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
        role = "Maintainer",
        year = "2023 - ongoing",
        description = "A command-line tool to convert SVG or an Android Vector Drawable " +
            "(AVG) to Jetpack Compose Icons.",
        stack = listOf("Kotlin", "CLI", "Compose"),
        stats = listOf(
            ProjectStat(value = "1.2k", label = "GitHub stars"),
            ProjectStat(value = "24", label = "contributors"),
            ProjectStat(value = "v2.1", label = "current release"),
        ),
        externalCta = "View on GitHub",
    ),
)

private val PROJECTS_PT_BR: List<Project> = listOf(
    Project(
        slug = "thunderbird",
        index = "01",
        name = "Thunderbird para Android",
        kind = ProjectKind.OpenSource,
        role = "Engenheiro de Software Sênior, Mobile - Mozilla",
        year = "2025 - presente",
        description = "Cliente de email open-source da Mozilla chegando ao Android. " +
            "Focado em Compose, privacidade em primeiro lugar, construído no aberto " +
            "com a comunidade K-9 Mail.",
        stack = listOf("Kotlin", "Jetpack Compose", "K-9 Mail", "OSS"),
        stats = listOf(
            ProjectStat(value = "OSS", label = "totalmente open source"),
            ProjectStat(value = "Mozilla", label = "afiliado"),
            ProjectStat(value = "Compose", label = "UI moderna"),
        ),
        externalCta = "Estudo de caso",
        coverImage = "/images/projects/thunderbird/hero.png",
    ),
    Project(
        slug = "bleacher-report",
        index = "02",
        name = "Bleacher Report",
        kind = ProjectKind.Commercial,
        role = "Engenheiro Android Sênior - REDspace",
        year = "2022 - 2025",
        description = "Três anos no app Android de uma das maiores plataformas de mídia " +
            "esportiva. Migrações para Compose, reestruturação de CI/CD e o trabalho " +
            "meticuloso de estabilidade que mantém apps em produção em escala.",
        stack = listOf("Kotlin", "Compose", "CI/CD"),
        stats = listOf(
            ProjectStat(value = "-50%", label = "tempo de release"),
            ProjectStat(value = "10M+", label = "usuários mensais"),
            ProjectStat(value = "3 anos", label = "no app"),
        ),
        externalCta = "Estudo de caso",
    ),
    Project(
        slug = "pinterest",
        index = "03",
        name = "Pinterest",
        kind = ProjectKind.Commercial,
        role = "Engenheiro Android Sênior (Contrato)",
        year = "2021 - 2022",
        description = "Liderou a modularização e a entrega de features dinâmicas em uma " +
            "base de código de 9 anos. Reduziu o clean-build em 65% e entregou a " +
            "primeira integração com Play Feature Delivery do app.",
        stack = listOf("Kotlin", "Play Feature Delivery", "Dagger"),
        stats = listOf(
            ProjectStat(value = "-65%", label = "tempo de clean build"),
            ProjectStat(value = "30", label = "feature modules"),
            ProjectStat(value = "9 anos", label = "de código legado"),
        ),
        externalCta = "Estudo de caso",
    ),
    Project(
        slug = "svg-compose",
        index = "04",
        name = "SVG to Compose",
        kind = ProjectKind.OpenSource,
        role = "Mantenedor",
        year = "2023 - em andamento",
        description = "Uma ferramenta command-line para converter SVG ou um Android Vector " +
            "Drawable (AVG) para Jetpack Compose icones.",
        stack = listOf("Kotlin", "CLI", "Compose"),
        stats = listOf(
            ProjectStat(value = "1.2k", label = "estrelas no GitHub"),
            ProjectStat(value = "24", label = "contribuidores"),
            ProjectStat(value = "v2.1", label = "versão atual"),
        ),
        externalCta = "Ver no GitHub",
    ),
)

// ---------------------------------------------------------------------------
// Experience
// ---------------------------------------------------------------------------

private val EXPERIENCE_EN: List<Experience> = listOf(
    Experience(
        date = "apr 2025 - oct 2026",
        duration = "1 yr 7 mos",
        role = "Senior Software Engineer, Mobile",
        company = "Mozilla / Thunderbird for Android - Remote - Canada",
        body = "Helped bring Thunderbird to Android alongside broader Mozilla " +
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

private val EXPERIENCE_PT_BR: List<Experience> = listOf(
    Experience(
        date = "abr 2025 - out 2026",
        duration = "1 ano 7 meses",
        role = "Engenheiro de Software Sênior, Mobile",
        company = "Mozilla / Thunderbird para Android - Remoto - Canadá",
        body = "Ajudei a trazer o Thunderbird para Android ao lado de outros projetos " +
            "mobile da Mozilla. Kotlin em primeiro, privacidade em primeiro, Compose " +
            "em primeiro, tudo aberto.",
        bullets = listOf(
            "Desenvolvimento de features no app Android principal",
            "Android, Jetpack Compose e uma longa lista de infraestrutura",
            "Contribuindo upstream em bibliotecas mobile compartilhadas",
            "Entregando para uma comunidade que realmente se importa",
        ),
    ),
    Experience(
        date = "mar 2022 - mar 2025",
        duration = "3 anos 1 mês",
        role = "Engenheiro Android Sênior",
        company = "REDspace Inc. - Halifax, NS - Híbrido",
        body = "Projetando e desenvolvendo aplicações mobile robustas, fáceis de manter " +
            "e em conformidade com os padrões da plataforma Android em diversos produtos.",
        bullets = listOf(
            "Arquitetura de código Kotlin/Java reutilizável e de alto desempenho",
            "Uso de profiling para corrigir gargalos de memória e performance",
            "Entrega de apps robustos e de alta qualidade dentro do cronograma",
            "Migrações para Compose e melhorias em CI/CD",
        ),
    ),
    Experience(
        date = "jul 2021 - mar 2022",
        duration = "9 meses",
        role = "Engenheiro Android Sênior",
        company = "Pinterest - Contrato - Remoto",
        body = "Projetei soluções, otimizei e mantive frameworks que ajudam o time de " +
            "engenharia a entregar a melhor experiência possível em escala.",
        bullets = listOf(
            "Melhoria da velocidade de build via modularização com dynamic features",
            "Criação de um gerenciador central de dependências para features dinâmicas",
            "Início da migração de XML views para Jetpack Compose no app",
            "Parceria com engenharia de produto para criar enablers de novas features",
        ),
    ),
    Experience(
        date = "out 2019 - jul 2021",
        duration = "1 ano 10 meses",
        role = "Engenheiro de Software Principal",
        company = "Questrade Financial Group - Toronto, ON",
        body = "Liderança técnica do time que construiu um novo app cross-platform em " +
            "React Native. Projetei serviços baseados em eventos com alta disponibilidade.",
        bullets = listOf(
            "Módulo React Native para autenticação biométrica + OIDC + PKCE",
            "Microsserviços baseados em eventos usando Kafka e Google Pub/Sub",
            "Serviço de mock dirigido por contrato para destravar o mobile",
            "Coaching do time novo enquanto aprendia React Native",
        ),
    ),
    Experience(
        date = "ago 2017 - out 2019",
        duration = "2 anos 2 meses",
        role = "Engenheiro de Software Sênior",
        company = "Questrade Financial Group - Toronto, ON",
        body = "Responsável por features, performance e uma reescrita completa de " +
            "autenticação no app mobile híbrido da Questrade, compartilhado entre " +
            "iOS, Android, desktop e web.",
        bullets = listOf(
            "Subiu o app híbrido de 1.5 a 3.3 estrelas com 100k+ usos diários",
            "Autenticação biométrica nativa (Fingerprint, TouchID, FaceID)",
            "Entrega de auth ponta-a-ponta: mobile, front, back, métricas, release",
            "Credenciais criptografadas em repouso usando RSA em código nativo",
        ),
    ),
    Experience(
        date = "jun 2015 - ago 2017",
        duration = "2 anos 2 meses",
        role = "Analista Desenvolvedor Mobile",
        company = "Axxiom - Belo Horizonte, BR",
        body = "Projetei e implementei um novo app nativo Android do zero para a CEMIG. " +
            "Equipes de campo o usavam para localizar ativos que precisavam de reparo.",
        bullets = listOf(
            "Android nativo na era do Material Design, do zero",
            "Integração com Google Maps para inspeção de ativos",
            "Comunicação entre apps distribuídos via AIDL",
            "Central de notificação roteando por Wi-Fi, rede móvel ou satélite",
        ),
    ),
    Experience(
        date = "fev 2013 - jun 2015",
        duration = "2 anos 4 meses",
        role = "Analista Desenvolvedor .NET",
        company = "Planear Sistemas - Belo Horizonte, BR",
        body = "Full-stack .NET em saúde e marketing. App mobile para agendamento, " +
            "prescrição e financeiro em clínicas. App de pesquisas dinâmicas para marketing.",
        bullets = listOf(
            "Soluções mobile + web para clínicas privadas e policlínicas públicas",
            "Engine de formulários dinâmicos a partir de contrato RESTful",
            "Rede social gamificada para campanhas políticas",
        ),
    ),
)

// ---------------------------------------------------------------------------
// Education
// ---------------------------------------------------------------------------

private val EDUCATION_EN: List<Education> = listOf(
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

private val EDUCATION_PT_BR: List<Education> = listOf(
    Education(
        degree = "Pós-graduação - Desenvolvimento de Aplicativos Mobile",
        institution = "Centro Universitário UNA",
        year = "2015",
    ),
    Education(
        degree = "Tecnólogo - Jogos Digitais",
        institution = "PUC-MG",
        year = "2012",
    ),
    Education(
        degree = "Técnico em Informática",
        institution = "COTEMIG",
        year = "2010",
    ),
)

// ---------------------------------------------------------------------------
// Uses
// ---------------------------------------------------------------------------

private val USES_EN: List<UseCategory> = listOf(
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

private val USES_PT_BR: List<UseCategory> = listOf(
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
        name = "mesa",
        items = listOf(
            "HHKB Pro Hybrid",
            "MX Master 3S",
            "Tampo Ikea autoportante",
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

// ---------------------------------------------------------------------------
// Ticker
// ---------------------------------------------------------------------------

private val TICKER_EN: List<TickerItem> = listOf(
    TickerItem(label = "now", value = "dynamic-feature system"),
    TickerItem(label = "reading", value = "Designing Data-Intensive Applications"),
    TickerItem(label = "learning", value = "Compose Multiplatform"),
    TickerItem(label = "playing", value = "Hollow Knight"),
)

private val TICKER_PT_BR: List<TickerItem> = listOf(
    TickerItem(label = "agora", value = "sistema de dynamic features"),
    TickerItem(label = "lendo", value = "Designing Data-Intensive Applications"),
    TickerItem(label = "aprendendo", value = "Compose Multiplatform"),
    TickerItem(label = "jogando", value = "Hollow Knight"),
)

// ---------------------------------------------------------------------------
// Now strip
// ---------------------------------------------------------------------------

private val NOW_STRIP_EN: List<NowStripEntry> = listOf(
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

private val NOW_STRIP_PT_BR: List<NowStripEntry> = listOf(
    NowStripEntry(
        label = "trabalhando em",
        title = "Um sistema de dynamic features que não briga com o dev loop.",
        body = "Grafos de módulos que escalam além dos 400 módulos sem tornar os " +
            "tempos de compilação locais trágicos. No fim, o ajuste estava quase todo no Gradle.",
    ),
    NowStripEntry(
        label = "escrevendo",
        title = "Compose estável, sem seita",
        body = "Dois rascunhos na fila - um sobre estabilidade em Compose (o bom, o " +
            "doloroso, o \"na verdade tá tranquilo\") e outro sobre por que parei de ter medo do Gradle.",
    ),
    NowStripEntry(
        label = "fora do expediente",
        title = "Ragu, quase todo domingo.",
        body = "Cozinhando italiano devagar nos domingos, sencha em folha pela manhã " +
            "e correndo o Halifax harbourwalk em todas as estações.",
    ),
)
