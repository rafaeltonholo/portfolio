package dev.tonholo.portfolio.locale

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Hand-rolled i18n surface for the site. Each locale provides a [Strings]
 * instance whose nested groups (for example [Nav], [Hero]) hold the copy the
 * UI renders. Groups are plain data classes so consumers can read them with
 * safe property access; the whole tree is typed and null-free.
 *
 * Translations are layered via inheritance: [StringsEn] provides the English
 * defaults; [StringsPtBr] overrides only the keys with a Brazilian-Portuguese
 * translation and re-uses [StringsEn] groups wholesale for the rest.
 *
 * Access through [LocalStrings] inside the composition; outside composition
 * use [stringsFor].
 */
sealed class Strings {
    abstract val nav: Nav
    abstract val statusBadge: StatusBadge
    abstract val themeToggle: ThemeToggle
    abstract val footer: Footer
    abstract val languageToast: LanguageToast
    abstract val hero: Hero
    abstract val about: About
    abstract val articlesList: ArticlesList
    abstract val articlesSidebar: ArticlesSidebar
    abstract val articleMeta: ArticleMeta
    abstract val resume: Resume
    abstract val experience: Experience
    abstract val skills: Skills
    abstract val projectsList: ProjectsList
    abstract val projectsFilter: ProjectsFilter
    abstract val projectDetail: ProjectDetail
    abstract val prevNext: PrevNext
    abstract val hiringCta: HiringCta
    abstract val snippet: Snippet
    abstract val nowStrip: NowStrip
    abstract val uses: Uses
    abstract val selectedWork: SelectedWork
    abstract val notFound: NotFound

    /** Primary navigation link labels rendered in [dev.tonholo.portfolio.components.organisms.chrome.Nav]. */
    data class Nav(
        val home: String,
        val projects: String,
        val about: String,
        val articles: String,
        val resume: String,
        /** Screen-reader label for the mobile drawer toggle button. */
        val mobileMenuAriaLabel: String,
    )

    /**
     * Copy used by the nav status pill. The badge renders [statusLabel] and
     * [presenceLabel] on either side of a [dev.tonholo.portfolio.components.atoms.MetaSeparator]
     * dot, so each segment ships as its own translated string.
     */
    data class StatusBadge(val statusLabel: String, val presenceLabel: String)

    /** Copy used by the theme-toggle [dev.tonholo.portfolio.components.atoms.IconButton]. */
    data class ThemeToggle(val ariaLabel: String, val titleToLight: String, val titleToDark: String)

    /** Strings for the site-wide [dev.tonholo.portfolio.components.organisms.chrome.Footer]. */
    data class Footer(
        val tagline: String,
        val headingName: String,
        val headingPages: String,
        val headingWork: String,
        val headingElsewhere: String,
        val linkHome: String,
        val linkAbout: String,
        val linkArticles: String,
        val linkResume: String,
        val bottomLeftSegments: List<String>,
    )

    /** Copy shown in the transient pt-BR [dev.tonholo.portfolio.components.organisms.chrome.LanguageToast]. */
    data class LanguageToast(val primary: String, val secondary: String)

    /** Home hero editorial copy ([dev.tonholo.portfolio.components.organisms.home.HeroEditorial]). */
    data class Hero(
        val titleLine1: String,
        val titleLine2: String,
        val titleLine3: String,
        val subtitle: String,
        val ctaPrimary: String,
        val ctaSecondary: String,
    )

    /** Copy used by the About page header, tab strip, and every tab panel. */
    data class About(
        val titlePrefix: String,
        val titleEmphasis: String,
        val lede: String,
        val portraitAlt: String,
        val tabStory: String,
        val tabNow: String,
        val tabHobbies: String,
        val tabBeyond: String,
        val storyIntro1: String,
        val storyIntro2: String,
        val storyHeading2010: String,
        val storyBody2010: String,
        val storyHeading2015: String,
        val storyBody2015: String,
        val storyHeading2021: String,
        val storyBody2021: String,
        val storyHeadingToday: String,
        val storyBodyToday: String,
        val nowUpdatedPrefix: String,
        val nowHeadingWorking: String,
        val nowBodyWorking: String,
        val nowHeadingLearning: String,
        val nowBodyLearning: String,
        val nowHeadingWriting: String,
        val nowBodyWriting: String,
        val nowHeadingThinking: String,
        val nowBodyThinking: String,
        val hobbies: List<Hobby>,
        val beyondBody1: String,
        val beyondBody2: String,
        val beyondBody3: String,
    ) {
        /**
         * One entry in the hobbies grid. [label] is the accent-meta prefix
         * (for example "Cooking") and is lowercased into the `/label` slug
         * shown above the body copy; [body] is the serif paragraph below it.
         */
        data class Hobby(val label: String, val body: String)
    }

    /** Copy rendered at the top of the Articles index page. */
    data class ArticlesList(
        val title: String,
        val postsSuffix: String,
        val rssCopy: String,
        val subscribersCopy: String,
        val statusDraft: String,
        val statusPublished: String,
    )

    /** Sidebar copy shown on the article detail page. */
    data class ArticlesSidebar(
        val contents: String,
        val share: String,
        val shareCopyLink: String,
        val shareCopied: String,
        val shareTwitter: String,
        val shareLinkedIn: String,
        val shareBluesky: String,
    )

    /** Copy shown by the back-to-articles meta row on the detail page. */
    data class ArticleMeta(val backLabel: String, val authorPrefix: String, val readSuffix: String)

    /** Copy on the resume page header and download CTA. */
    data class Resume(
        val titleLine1: String,
        val titleLine2: String,
        val body: String,
        val downloadCta: String,
    )

    /** Copy for the [dev.tonholo.portfolio.components.organisms.resume.ExperienceTimeline] section head. */
    data class Experience(val sectionTitle: String, val sectionTrailing: String)

    /** Column headings for the [dev.tonholo.portfolio.components.organisms.resume.SkillsGrid]. */
    data class Skills(val primaryStack: String, val alsoComfortable: String, val education: String)

    /** Copy for the Projects index page header. */
    data class ProjectsList(val title: String)

    /** Labels for the [dev.tonholo.portfolio.components.organisms.projects.ProjectsFilterBar]. */
    data class ProjectsFilter(val all: String, val commercial: String, val openSource: String)

    /** Copy shown by the project detail hero. */
    data class ProjectDetail(
        val commercialKind: String,
        val openSourceKind: String,
        val labelRole: String,
        val labelTimeline: String,
        val labelStack: String,
        val labelStatus: String,
        val statusShipped: String,
        val contextLabel: String,
        val outcomesLabel: String,
        val outcomesTitle: String,
        val notFoundCopy: String,
        val breadcrumbRoot: String,
        val breadcrumbFile: String,
        val coverPlaceholderLabel: String,
    )

    /** Copy for the [dev.tonholo.portfolio.components.organisms.projects.PrevNextNav]. */
    data class PrevNext(val back: String, val nextPrefix: String)

    /** Copy for the [dev.tonholo.portfolio.components.organisms.home.HiringCta] block. */
    data class HiringCta(val heading: String, val emailLabel: String, val bookingLabel: String)

    /** Copy for the [dev.tonholo.portfolio.components.organisms.home.SnippetOfTheWeek] organism. */
    data class Snippet(val sectionTitle: String, val copyTitle: String, val copyBody: String)

    /** Copy for the [dev.tonholo.portfolio.components.organisms.home.NowStrip] organism. */
    data class NowStrip(
        val sectionTitle: String,
        val labelWorkingOn: String,
        val labelWriting: String,
        val labelOffTheClock: String,
        val statusDraft: String,
        val statusPublished: String,
    )

    /** Copy for the [dev.tonholo.portfolio.components.organisms.home.UsesGrid] organism. */
    data class Uses(val sectionTitle: String, val sectionTrailingLabel: String)

    /** Copy for the [dev.tonholo.portfolio.components.organisms.home.SelectedWorkGrid] section head. */
    data class SelectedWork(val sectionTitle: String, val projectsSuffix: String)

    /** Copy rendered by the IDE-themed 404 page. */
    data class NotFound(
        val title: String,
        val lead: String,
        val shellCommandPrefix: String,
        val recoveryHome: String,
        val recoveryProjects: String,
        val recoveryArticles: String,
        val recoveryResume: String,
    )
}

/**
 * Returns the [Strings] implementation for [locale]. Falls back to [StringsEn]
 * so callers never need to juggle nulls.
 */
fun stringsFor(locale: Locale): Strings = when (locale) {
    Locale.EnCa -> StringsEn
    Locale.PtBr -> StringsPtBr
}

/**
 * Ambient [Strings]. Provided at the root by
 * [dev.tonholo.portfolio.PortfolioApp] based on the active [Locale].
 */
val LocalStrings = staticCompositionLocalOf<Strings> {
    error("Strings not provided")
}
