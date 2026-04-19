package dev.tonholo.portfolio.locale

/**
 * Brazilian-Portuguese translations. Only a small surface is translated for
 * v1 (nav labels, language toast, footer column headings, footer tagline,
 * hero CTA buttons); every other group falls back to [StringsEn] by direct
 * reuse. Extending the translation coverage later is additive: override the
 * group on this object and the rest of the UI picks it up automatically.
 */
object StringsPtBr : Strings() {
    override val nav: Nav = Nav(
        home = "início",
        projects = "projetos",
        about = "sobre",
        articles = "artigos",
        resume = "currículo",
    )

    override val languageToast: LanguageToast = LanguageToast(
        primary = " - tradução em andamento",
        secondary = "A maior parte do conteúdo ainda está em inglês por enquanto.",
    )

    override val footer: Footer = StringsEn.footer.copy(
        tagline = "Engenheiro Android. Halifax, Nova Escócia, vindo de Belo Horizonte. " +
            "Escrevendo Kotlin desde 2015.",
        headingPages = "Páginas",
        headingWork = "Trabalho",
        headingElsewhere = "Em outros lugares",
    )

    override val hero: Hero = StringsEn.hero.copy(
        ctaPrimary = "ver trabalhos em destaque",
        ctaSecondary = "baixar CV",
    )

    // Groups below are not translated yet; reuse the English defaults verbatim
    // so the surface stays typed without null juggling.
    override val statusBadge: StatusBadge = StringsEn.statusBadge
    override val themeToggle: ThemeToggle = StringsEn.themeToggle
    override val about: About = StringsEn.about
    override val articlesList: ArticlesList = StringsEn.articlesList
    override val articlesSidebar: ArticlesSidebar = StringsEn.articlesSidebar
    override val articleMeta: ArticleMeta = StringsEn.articleMeta
    override val resume: Resume = StringsEn.resume
    override val experience: Experience = StringsEn.experience
    override val skills: Skills = StringsEn.skills
    override val projectsList: ProjectsList = StringsEn.projectsList
    override val projectsFilter: ProjectsFilter = StringsEn.projectsFilter
    override val projectDetail: ProjectDetail = StringsEn.projectDetail
    override val prevNext: PrevNext = StringsEn.prevNext
    override val hiringCta: HiringCta = StringsEn.hiringCta
    override val snippet: Snippet = StringsEn.snippet
    override val nowStrip: NowStrip = StringsEn.nowStrip
    override val uses: Uses = StringsEn.uses
    override val selectedWork: SelectedWork = StringsEn.selectedWork
    override val notFound: NotFound = StringsEn.notFound
}
